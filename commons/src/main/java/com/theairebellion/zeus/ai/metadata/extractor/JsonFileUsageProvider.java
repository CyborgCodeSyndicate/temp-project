package com.theairebellion.zeus.ai.metadata.extractor;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.ai.metadata.model.classes.Usage;
import com.theairebellion.zeus.logging.LogCommon;
import lombok.Setter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class JsonFileUsageProvider implements UsageProvider {

    private final ObjectMapper objectMapper;

    @Setter
    private static String usageJson;


    public JsonFileUsageProvider() {
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public List<Usage> getUsageList(String key) {
        if (usageJson == null || usageJson.isBlank()) {
            LogCommon.error("Usage JSON is not set or empty. There won't be any usage data.");
            return Collections.emptyList();
        }

        try {
            JsonNode rootNode = objectMapper.readTree(usageJson);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode == null || !itemsNode.isArray()) {
                return Collections.emptyList();
            }

            return findMatchingUsage(itemsNode, key).orElse(Collections.emptyList());

        } catch (IOException e) {
            LogCommon.error("Error reading usage JSON data.", e);
            return Collections.emptyList();
        }
    }


    private Optional<List<Usage>> findMatchingUsage(JsonNode itemsNode, String key) {
        return StreamSupport.stream(itemsNode.spliterator(), false)
                   .filter(itemNode -> {
                       JsonNode keyNode = itemNode.get("key");
                       return keyNode != null && keyNode.asText().equals(key);
                   })
                   .findFirst()
                   .flatMap(itemNode -> {
                       JsonNode valueNode = itemNode.get("value");
                       if (valueNode != null && valueNode.isArray()) {
                           try {
                               List<Usage> usageList = objectMapper.readValue(
                                   valueNode.toString(),
                                   new TypeReference<>() {
                                   }
                               );
                               return Optional.of(usageList);
                           } catch (IOException e) {
                               LogCommon.error("Error parsing usage list for key: " + key, e);
                           }
                       }
                       return Optional.empty();
                   });
    }

}


