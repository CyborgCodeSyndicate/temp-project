package com.theairebellion.zeus.ai.metadata.extractor;

import com.theairebellion.zeus.ai.metadata.model.AiFieldInfo;
import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.logging.LogCommon;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class FieldInfoExtractor {

    private final Reflections reflections;
    private final UsageProvider usageProvider;


    public FieldInfoExtractor(final Reflections reflections, UsageProvider usageProvider) {
        this.reflections = Objects.requireNonNull(reflections, "Reflections cannot be null");
        this.usageProvider = Objects.requireNonNull(usageProvider, "UsageProvider cannot be null");
    }


    public List<AiFieldInfo> extractFieldsInfo(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                   .map(field -> createFieldInfo(type.getName(), field))
                   .toList();
    }


    private AiFieldInfo createFieldInfo(String className, Field field) {
        AiFieldInfo aiFieldInfo = new AiFieldInfo();
        aiFieldInfo.setName(field.getName());
        aiFieldInfo.setType(field.getType());

        Optional.ofNullable(field.getAnnotation(InfoAI.class))
            .ifPresent(infoAi -> aiFieldInfo.setDescription(infoAi.description()));

        Optional.ofNullable(field.getType().getAnnotation(InfoAIClass.class)).ifPresent(annotation -> {
            aiFieldInfo.setDescription(annotation.description());
            aiFieldInfo.setCreationType(annotation.creationType());

            String fieldSignature = className + "." + field.getType().getSimpleName() + "." + field.getName();
            aiFieldInfo.setUsage(usageProvider.getUsageList(fieldSignature));

            if (annotation.creationType() == CreationType.ENUM && field.getType().isInterface()) {
                aiFieldInfo.setAvailableEnumOptions(getEnumOptions(field.getType()));
            }
        });

        return aiFieldInfo;
    }


    public List<String> getEnumOptions(Class<?> interfaceType) {
        try {
            Set<Class<?>> subTypes = (Set<Class<?>>) reflections.getSubTypesOf(interfaceType);

            return subTypes.stream()
                       .filter(Class::isEnum)
                       .flatMap(enumClass -> extractEnumValues((Class<? extends Enum<?>>) enumClass))
                       .toList();
        } catch (Exception e) {
            LogCommon.warn("Failed to get enum options for " + interfaceType.getName(), e);
            return Collections.emptyList();
        }
    }


    private Stream<String> extractEnumValues(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                   .map(enumConstant -> "ENUM: " + enumClass.getName() + "." + printEnumValue(enumConstant));
    }


    private String printEnumValue(Enum<?> enumValue) {
        try {
            return (String) enumValue.getClass().getMethod("aiPrint").invoke(enumValue);
        } catch (NoSuchMethodException ignored) {
            return enumValue.name();
        } catch (Exception e) {
            LogCommon.warn("Error invoking aiPrint for enum " + enumValue.name(), e);
            return enumValue.toString();
        }
    }

}