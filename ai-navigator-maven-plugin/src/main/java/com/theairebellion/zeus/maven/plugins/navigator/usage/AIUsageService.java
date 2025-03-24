package com.theairebellion.zeus.maven.plugins.navigator.usage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.theairebellion.zeus.ai.metadata.extractor.AnnotationExtractor;
import com.theairebellion.zeus.ai.metadata.extractor.JsonFileUsageProvider;
import com.theairebellion.zeus.ai.metadata.model.annotations.AiAnnotationInfo;
import com.theairebellion.zeus.ai.metadata.model.classes.AiClassInfo;
import com.theairebellion.zeus.ai.metadata.model.classes.AiUsage;
import com.theairebellion.zeus.ai.metadata.service.AIMetadataService;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.maven.plugins.navigator.collector.MetadataCollector;
import com.theairebellion.zeus.maven.plugins.navigator.model.ServiceType;
import org.apache.maven.plugin.logging.Log;
import org.reflections.Reflections;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AIUsageService {

    private final AIMetadataService metadataService;
    private final MetadataCollector metadataCollector;
    private final AnnotationExtractor annotationExtractor;
    private final Log log;
    private final S3Client s3Client;
    private final String bucket;


    public AIUsageService(AIMetadataService metadataService, Reflections reflections,
                          final AnnotationExtractor annotationExtractor, Log log,
                          S3Client s3Client, String bucket) {
        this.metadataService = Objects.requireNonNull(metadataService, "MetadataService cannot be null");
        this.annotationExtractor = annotationExtractor;
        this.log = Objects.requireNonNull(log, "Log cannot be null");
        this.s3Client = Objects.requireNonNull(s3Client, "S3Client cannot be null");
        this.bucket = Objects.requireNonNull(bucket, "Bucket cannot be null");
        this.metadataCollector = new MetadataCollector(metadataService, reflections);
    }


    public AiUsage generateUsage(Set<ServiceType> serviceTypes) {
        AiUsage aiUsage = new AiUsage();
        List<AiClassInfo> classInfoList = new ArrayList<>();
        List<AiAnnotationInfo> annotationInfoList = new ArrayList<>();

        classInfoList.add(metadataService.extractMetadata(BaseTest.class));

        serviceTypes.forEach(serviceType -> metadataCollector.collectServiceMetadata(serviceType, classInfoList));
        metadataCollector.collectFluentServiceMetadata(classInfoList);

        Set<Class<?>> availableServices = classInfoList.stream()
                                              .map(AiClassInfo::getType)
                                              .filter(type -> type.isAnnotationPresent(TestService.class))
                                              .collect(Collectors.toUnmodifiableSet());

        AiClassInfo questInfo = metadataService.extractMetadata(Quest.class);

        questInfo.getMethodsInfo().stream()
            .filter(aiMethodInfo -> "enters".equals(aiMethodInfo.getName()))
            .findFirst()
            .ifPresent(entersMethod -> entersMethod.getArgumentsInfo().get(0)
                                           .setAvailableOptions(
                                               availableServices.stream().map(Class::getName).toList()));

        classInfoList.add(questInfo);
        aiUsage.setAiClassInfo(classInfoList);

        serviceTypes.stream().flatMap(serviceType -> serviceType.getAnnotationClasses().stream()).forEach(aClass -> {
            annotationInfoList.add(annotationExtractor.extract(aClass));
        });

        aiUsage.setAiAnnotationInfos(annotationInfoList);
        return aiUsage;
    }


    public AiUsage generateUsage(boolean useRest, boolean useUI, boolean useDB) {
        Set<ServiceType> serviceTypes = EnumSet.noneOf(ServiceType.class);
        List<String> usageFiles = new ArrayList<>();

        downloadUsageFile(usageFiles, "framework-ai");
        if (useRest) {
            serviceTypes.add(ServiceType.REST);
            downloadUsageFile(usageFiles, "api-ai");
        }
        if (useUI) {
            serviceTypes.add(ServiceType.UI);
            downloadUsageFile(usageFiles, "ui-ai");
        }
        if (useDB) {
            serviceTypes.add(ServiceType.DATABASE);
            downloadUsageFile(usageFiles, "db-ai");
        }

        String usageJson = mergeJsons(usageFiles);
        JsonFileUsageProvider.setUsageJson(usageJson);
        return generateUsage(serviceTypes);
    }


    private void downloadUsageFile(List<String> jsonFiles, String prefix) {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder().bucket(bucket).build();
        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);

        listRes.contents().stream()
            .map(s3Object -> s3Object.key())
            .filter(key -> key.startsWith(prefix))
            .forEach(key -> {
                try {
                    String content = s3Client.getObjectAsBytes(
                        GetObjectRequest.builder().bucket(bucket).key(key).build()
                    ).asUtf8String();
                    jsonFiles.add(content);
                } catch (Exception e) {
                    log.warn(String.format("Failed to download %s: %s", key, e.getMessage()));
                }
            });
    }


    private String mergeJsons(List<String> jsonFiles) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, ArrayNode> keyToValueMap = new LinkedHashMap<>();

        jsonFiles.forEach(json -> processJson(json, mapper, keyToValueMap));

        return buildMergedJson(mapper, keyToValueMap);
    }


    private void processJson(String json, ObjectMapper mapper, Map<String, ArrayNode> keyToValueMap) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                itemsNode.forEach(itemNode -> {
                    String key = itemNode.path("key").asText();
                    if (!key.isEmpty()) {
                        keyToValueMap
                            .computeIfAbsent(key, k -> mapper.createArrayNode())
                            .addAll((ArrayNode) itemNode.get("value"));
                    }
                });
            }
        } catch (IOException e) {
            log.error("Error processing JSON content", e);
        }
    }


    private String buildMergedJson(ObjectMapper mapper, Map<String, ArrayNode> keyToValueMap) {
        ObjectNode combinedRoot = mapper.createObjectNode();
        ArrayNode combinedItems = mapper.createArrayNode();
        combinedRoot.set("items", combinedItems);

        keyToValueMap.forEach((key, value) -> {
            ObjectNode itemObject = mapper.createObjectNode();
            itemObject.put("key", key);
            itemObject.set("value", value);
            combinedItems.add(itemObject);
        });

        return combinedRoot.toPrettyString();
    }

}