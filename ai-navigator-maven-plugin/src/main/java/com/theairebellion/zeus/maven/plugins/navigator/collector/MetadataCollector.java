package com.theairebellion.zeus.maven.plugins.navigator.collector;

import com.theairebellion.zeus.ai.metadata.model.classes.AiClassInfo;
import com.theairebellion.zeus.ai.metadata.model.classes.Level;
import com.theairebellion.zeus.ai.metadata.service.AIMetadataService;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.framework.annotation.AIDisableUsage;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.maven.plugins.navigator.exception.ServiceConfigurationException;
import com.theairebellion.zeus.maven.plugins.navigator.model.ServiceType;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MetadataCollector {

    private final AIMetadataService metadataService;
    private final Reflections reflections;


    public MetadataCollector(AIMetadataService metadataService, Reflections reflections) {
        this.metadataService = Objects.requireNonNull(metadataService, "MetadataService cannot be null");
        this.reflections = Objects.requireNonNull(reflections, "Reflections cannot be null");
    }


    public void collectServiceMetadata(ServiceType serviceType, List<AiClassInfo> classInfoList) {
        Class<? extends FluentService> serviceClass = serviceType.getServiceClass();
        Class<?> implementationClass = findImplementationClass(serviceClass);

        classInfoList.add(metadataService.extractMetadata(implementationClass));
        processDownstreamClasses(implementationClass, classInfoList);
    }


    public void collectFluentServiceMetadata(List<AiClassInfo> classInfoList) {
        List<Class<? extends FluentService>> modifiedImplementations = reflections.getSubTypesOf(FluentService.class)
                                                                           .stream()
                                                                           .filter(
                                                                               aClass -> !aClass.isAnnotationPresent(
                                                                                   AIDisableUsage.class))
                                                                           .toList();

        modifiedImplementations.forEach(impl -> {
            AiClassInfo info = metadataService.extractMetadata(impl);
            List<? extends Class<?>> existingClasses = classInfoList.stream().map(AiClassInfo::getType).toList();
            List<Class<?>> classesToIgnore = List.of(UIServiceFluent.class, RestServiceFluent.class,
                DatabaseServiceFluent.class);

            if (!existingClasses.contains(info.getType()) &&
                    existingClasses.stream().noneMatch(aClass -> aClass.isAssignableFrom(info.getType())) &&
                    !classesToIgnore.contains(info.getType())) {
                classInfoList.add(info);
            }
        });
    }


    private Class<?> findImplementationClass(Class<?> serviceClass) {
        List<Class<?>> modifiedImplementations = (List<Class<?>>) reflections.getSubTypesOf(serviceClass)
                                                                      .stream()
                                                                      .filter(aClass -> !aClass.isAnnotationPresent(
                                                                          AIDisableUsage.class))
                                                                      .toList();

        if (modifiedImplementations.size() > 1) {
            throw new ServiceConfigurationException(
                "Multiple implementations found for " + serviceClass.getSimpleName() +
                    ". Only a single implementation is allowed.");
        }

        return modifiedImplementations.isEmpty() ? serviceClass : modifiedImplementations.get(0);
    }


    private void processDownstreamClasses(Class<?> serviceClass, List<AiClassInfo> classInfoList) {
        Optional.ofNullable(serviceClass.getAnnotation(InfoAIClass.class))
            .filter(annotation -> annotation.level() == Level.MIDDLE)
            .ifPresent(annotation -> getEndLevelClasses(serviceClass)
                                         .stream()
                                         .map(metadataService::extractMetadata)
                                         .forEach(classInfoList::add));
    }


    private Set<Class<?>> getEndLevelClasses(Class<?> serviceClass) {
        return Arrays.stream(serviceClass.getDeclaredMethods())
                   .map(Method::getReturnType)
                   .filter(returnType -> returnType.isAnnotationPresent(InfoAIClass.class) &&
                                             returnType.getAnnotation(InfoAIClass.class).level() == Level.LAST)
                   .collect(Collectors.toUnmodifiableSet());
    }

}