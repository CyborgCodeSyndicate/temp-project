package com.theairebellion.zeus.ai.metadata.extractor;

import com.theairebellion.zeus.ai.metadata.model.annotations.AiAnnotationFieldInfo;
import com.theairebellion.zeus.ai.metadata.model.annotations.AiAnnotationInfo;
import com.theairebellion.zeus.annotations.InfoAI;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationExtractor {

    private final AnnotationFieldExtractor fieldExtractor;
    private final UsageProvider usageProvider;


    public AnnotationExtractor(AnnotationFieldExtractor fieldExtractor,
                               UsageProvider usageProvider) {
        this.fieldExtractor = fieldExtractor;
        this.usageProvider = usageProvider;
    }


    public AiAnnotationInfo extract(Class<?> annotationClass) {
        AiAnnotationInfo aiAnnotationInfo = new AiAnnotationInfo();

        aiAnnotationInfo.setAnnotationName(annotationClass.getSimpleName());

        InfoAI infoAi = annotationClass.getAnnotation(InfoAI.class);
        aiAnnotationInfo.setDescription(infoAi != null ? infoAi.description() : "");

        aiAnnotationInfo.setUsage(usageProvider.getUsageList(annotationClass.getName()));

        Retention retention = annotationClass.getAnnotation(Retention.class);
        if (retention != null) {
            aiAnnotationInfo.setRetentionPolicy(retention.value().name());
        } else {
            aiAnnotationInfo.setRetentionPolicy("RUNTIME");
        }

        Target target = annotationClass.getAnnotation(Target.class);
        if (target != null) {
            List<String> targets = Arrays.stream(target.value())
                                       .map(Enum::name)
                                       .collect(Collectors.toList());
            aiAnnotationInfo.setTargets(targets);
        } else {
            aiAnnotationInfo.setTargets(Collections.emptyList());
        }

        // 6. Repeatable
        Repeatable repeatable = annotationClass.getAnnotation(Repeatable.class);
        aiAnnotationInfo.setRepeatable(repeatable != null);

        // 7. Fields (annotation methods)
        List<AiAnnotationFieldInfo> fieldDescriptions = new ArrayList<>();
        for (Method method : annotationClass.getDeclaredMethods()) {
            AiAnnotationFieldInfo fd = fieldExtractor.extractField(method);
            fieldDescriptions.add(fd);
        }
        aiAnnotationInfo.setFields(fieldDescriptions);

        // 8. Nested annotations
        List<AiAnnotationInfo> nested = new ArrayList<>();
        for (Method method : annotationClass.getDeclaredMethods()) {
            Class<?> returnType = method.getReturnType();
            if (returnType.isAnnotation()) {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> nestedClass = (Class<? extends Annotation>) returnType;
                nested.add(extract(nestedClass));
            } else if (returnType.isArray() && returnType.getComponentType().isAnnotation()) {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> nestedClass = (Class<? extends Annotation>) returnType.getComponentType();
                nested.add(extract(nestedClass));
            }
        }
        aiAnnotationInfo.setNestedAnnotations(nested.isEmpty() ? null : nested);

        return aiAnnotationInfo;
    }

}


