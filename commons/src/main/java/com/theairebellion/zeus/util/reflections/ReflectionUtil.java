package com.theairebellion.zeus.util.reflections;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ReflectionUtil {

    public static <T> Class<? extends Enum> findEnumClassImplementationsOfInterface(Class<T> interfaceClass,
                                                                                    String packagePrefix) {
        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(interfaceClass);

        return result.stream()
                   .filter(Class::isEnum)
                   .map(clazz -> (Class<? extends Enum>) clazz)
                   .findFirst()
                   .orElseThrow(() -> new RuntimeException(
                       String.format("There is no Enum implementing interface: %s", interfaceClass.getName())));
    }


    public static <T> T findEnumImplementationsOfInterface(Class<T> interfaceClass, String enumName,
                                                           String packagePrefix) {
        Class<? extends Enum> enumClassImplementationsOfInterface = findEnumClassImplementationsOfInterface(
            interfaceClass, packagePrefix);
        Enum<?> enumValue = Enum.valueOf(enumClassImplementationsOfInterface, enumName);
        return (T) enumValue;
    }


    public static <T> List<Class<? extends T>> findImplementationsOfInterface(Class<T> interfaceClass,
                                                                              String packagePrefix) {
        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(interfaceClass);
        return new ArrayList<>(result);
    }


    public static <K> K getFieldValue(Object instance, Class<K> fieldType) {
        try {
            Field field = Arrays.stream(instance.getClass().getDeclaredFields())
                              .filter(f -> {
                                  f.setAccessible(true);
                                  return fieldType.isAssignableFrom(f.getType());
                              })
                              .findFirst()
                              .orElseThrow(() -> new NoSuchFieldException(
                                  "No field of type " + fieldType.getName() + " found in class " + instance.getClass()
                                                                                                       .getName()));

            Object value = field.get(instance);
            if (fieldType.isInstance(value)) {
                return fieldType.cast(value);
            } else {
                throw new ClassCastException("Field value is not of the expected type: " + fieldType.getName());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(
                "Failed to access the field of type " + fieldType.getName() + " in class " + instance.getClass()
                                                                                                 .getName(), e);
        }
    }


    public static <T> T getAttributeOfClass(String fieldName, Object object, Class<T> returnType) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null.");
        }
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);

            if (returnType.isInstance(value)) {
                return (T) value;
            } else {
                throw new ClassCastException("Field value is not of the expected type: " + returnType.getName());
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field '" + fieldName + "' not found in class " + object.getClass().getName(),
                e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                "Cannot access field '" + fieldName + "' in class " + object.getClass().getName(), e);
        }
    }

}
