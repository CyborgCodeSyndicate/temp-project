package com.theairebellion.zeus.ai.metadata.extractor;

import com.theairebellion.zeus.ai.metadata.model.annotations.AiAnnotationFieldInfo;
import com.theairebellion.zeus.annotations.AvailableOptionsAI;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.UseEnumInAnnotation;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AnnotationFieldExtractor {

    private final Reflections reflections;


    public AnnotationFieldExtractor(final Reflections reflections) {
        this.reflections = reflections;
    }


    public AiAnnotationFieldInfo extractField(Method method) {
        AiAnnotationFieldInfo fd = new AiAnnotationFieldInfo();
        fd.setName(method.getName());

        Class<?> returnType = method.getReturnType();
        String typeName = returnType.getSimpleName() + (returnType.isArray() ? "[]" : "");
        fd.setType(typeName);


        AvailableOptionsAI opts = method.getAnnotation(AvailableOptionsAI.class);
        if (opts != null) {
            Class<?> targetClass = opts.interfaceClass();
            fd.setAvailableOptions(extractEnumValues(targetClass));
        } else {
            fd.setAvailableOptions(Collections.emptyList());
        }

        InfoAI infoAi = method.getAnnotation(InfoAI.class);
        if (infoAi != null) {
            fd.setDescription(infoAi.description());
        } else {
            fd.setDescription("");
        }

        return fd;
    }


    private List<String> extractEnumValues(Class<?> clazz) {
        List<String> results = new ArrayList<>();
        if (clazz.isInterface()) {
            Set<Class<?>> subTypes = (Set<Class<?>>) reflections.getSubTypesOf(clazz);

            List<Class<?>> enumClasses = subTypes.stream()
                                             .filter(Class::isEnum)
                                             .toList();

            enumClasses.forEach(aClass -> {

                List<String> innerClassValues = extractEnumValuesFromInnerClassIf((Class<? extends Enum<?>>) aClass);
                if (!innerClassValues.isEmpty()) {
                    results.addAll(innerClassValues);
                } else {
                    Object[] enumConstants = aClass.getEnumConstants();
                    results.addAll(Arrays.stream(enumConstants).map(object -> ((Enum<?>) object).name()).toList());
                }
            });
            return results;
        }
        return null;
    }


    public List<String> extractEnumValuesFromInnerClassIf(Class<? extends Enum<?>> enumClass) {
        List<String> result = new ArrayList<>();

        Optional<Class<?>> innerClassForEnumValues = Arrays.stream(enumClass.getDeclaredClasses()).filter(
            aClass -> aClass.isAnnotationPresent(UseEnumInAnnotation.class)).findFirst();

        if (innerClassForEnumValues.isPresent()) {
            Class<?> innerClass = innerClassForEnumValues.get();
            for (Field field : innerClass.getDeclaredFields()) {
                if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(
                    field.getModifiers()) && field.getType().equals(String.class)) {
                    try {
                        String formattedName = enumClass.getSimpleName() + "." + innerClass.getSimpleName() + "." + field.getName();
                        result.add(formattedName);
                    } catch (Exception ignore) {
                    }
                }
            }
        }

        return result;
    }


}
