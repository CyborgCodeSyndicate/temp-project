package com.theairebellion.zeus.ai.metadata.service;

import com.theairebellion.zeus.ai.metadata.extractor.ArgumentInfoExtractor;
import com.theairebellion.zeus.ai.metadata.extractor.FieldInfoExtractor;
import com.theairebellion.zeus.ai.metadata.extractor.MethodInfoExtractor;
import com.theairebellion.zeus.ai.metadata.extractor.UsageProvider;
import com.theairebellion.zeus.ai.metadata.model.classes.AiClassInfo;
import com.theairebellion.zeus.ai.metadata.model.classes.AiMethodInfo;
import com.theairebellion.zeus.annotations.InfoAIClass;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class AIMetadataService {

    private final MethodInfoExtractor methodInfoExtractor;


    public AIMetadataService(Reflections reflections, final UsageProvider usageProvider) {
        FieldInfoExtractor fieldInfoExtractor = new FieldInfoExtractor(reflections, usageProvider);
        ArgumentInfoExtractor argumentInfoExtractor = new ArgumentInfoExtractor(fieldInfoExtractor, usageProvider);
        this.methodInfoExtractor = new MethodInfoExtractor(argumentInfoExtractor, usageProvider);
    }


    public AiClassInfo extractMetadata(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Class cannot be null");

        AiClassInfo aiClassInfo = new AiClassInfo();
        aiClassInfo.setType(clazz);

        Optional.ofNullable(clazz.getAnnotation(InfoAIClass.class)).ifPresent(infoAIClass -> {
            aiClassInfo.setDescription(infoAIClass.description());
            List<Method> accessibleMethods = getAccessibleMethods(clazz);
            List<AiMethodInfo> methodInfoList = methodInfoExtractor.extractMethodsInfo(accessibleMethods, infoAIClass);
            aiClassInfo.setMethodsInfo(methodInfoList);
        });

        return aiClassInfo;
    }


    private List<Method> getAccessibleMethods(Class<?> clazz) {
        Set<Method> declaredMethods = new HashSet<>(Arrays.asList(clazz.getDeclaredMethods()));
        Map<String, List<Method>> methodMap = Arrays.stream(clazz.getMethods())
                .filter(method -> Modifier.isPublic(
                        method.getModifiers()) && !method.getDeclaringClass()
                        .equals(Object.class))
                .collect(Collectors.groupingBy(AIMetadataService::methodSignature));

        return methodMap.values().stream()
                .map(methods -> methods.stream()
                        .filter(declaredMethods::contains)
                        .filter(method -> method.getReturnType().equals(clazz))
                        .findFirst()
                        .orElse(methods.get(0)))
                .toList();
    }


    private static String methodSignature(Method method) {
        return method.getName() + "(" + Arrays.stream(method.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.joining(",")) + ")";
    }

}