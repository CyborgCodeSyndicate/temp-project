package com.theairebellion.zeus.util.reflections;

import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReflectionUtil {

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends Enum<?>> findEnumClassImplementationsOfInterface(
        Class<T> interfaceClass, String packagePrefix) {

        validateInputs(interfaceClass, packagePrefix);

        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(interfaceClass);

        return result.stream()
                   .filter(Class::isEnum)
                   .map(clazz -> (Class<? extends Enum<?>>) clazz)
                   .findFirst()
                   .orElseThrow(() -> new ReflectionException(String.format(
                       "No Enum implementing interface '%s' found in package '%s'.",
                       interfaceClass.getName(), packagePrefix)));
    }


    @SuppressWarnings("unchecked")
    public static <T> T findEnumImplementationsOfInterface(
        Class<T> interfaceClass, String enumName, String packagePrefix) {

        Class<? extends Enum<?>> enumClass = findEnumClassImplementationsOfInterface(interfaceClass, packagePrefix);

        return (T) Arrays.stream(enumClass.getEnumConstants())
                       .filter(e -> e.name().equals(enumName))
                       .findFirst()
                       .orElseThrow(() -> new ReflectionException(String.format(
                           "Enum value '%s' not found in Enum '%s'.", enumName, enumClass.getName())));
    }


    public static <T> List<Class<? extends T>> findImplementationsOfInterface(Class<T> interfaceClass,
                                                                              String packagePrefix) {
        validateInputs(interfaceClass, packagePrefix);

        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(interfaceClass);
        return new ArrayList<>(result);
    }


    public static <K> K getFieldValue(Object instance, Class<K> fieldType) {
        validateInputs(instance, fieldType);


        try {
            Class<?> currentClass = instance.getClass();
            List<Field> allFields = new ArrayList<>();

            while (currentClass != null && currentClass != Object.class) {
                allFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                currentClass = currentClass.getSuperclass();
            }

            Field field = allFields.stream()
                              .filter(f -> {
                                  f.setAccessible(true);
                                  return fieldType.isAssignableFrom(f.getType());
                              })
                              .findFirst()
                              .orElseThrow(() -> new ReflectionException(
                                  String.format("No field of type '%s' found in class '%s'.",
                                      fieldType.getName(), instance.getClass().getName())));

            Object value = field.get(instance);
            if (!fieldType.isInstance(value)) {
                throw new ReflectionException(String.format(
                    "Field value is not of the expected type '%s'. Actual value type: '%s'.",
                    fieldType.getName(), value != null ? value.getClass().getName() : "null"));
            }
            return fieldType.cast(value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(
                String.format("Cannot access field of type '%s' in class '%s'.",
                    fieldType.getName(), instance.getClass().getName()), e);
        }
    }


    public static <T> T getAttributeOfClass(String fieldName, Object object, Class<T> returnType) {
        validateInputs(fieldName, object, returnType);

        try {
            Field field = getFieldFromClassHierarchy(object.getClass(), fieldName);
            field.setAccessible(true);
            Object value = field.get(object);

            if (!returnType.isInstance(value)) {
                throw new ReflectionException(String.format(
                    "Field '%s' value is not of expected type '%s'. Actual value type: '%s'.",
                    fieldName, returnType.getName(), value != null ? value.getClass().getName() : "null"));
            }
            return returnType.cast(value);
        } catch (NoSuchFieldException e) {
            throw new ReflectionException(String.format(
                "Field '%s' not found in class hierarchy of '%s'.",
                fieldName, object.getClass().getName()), e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(
                String.format("Cannot access field '%s' in class hierarchy of '%s'.",
                    fieldName, object.getClass().getName()), e);
        }
    }


    public static <T> Class<? extends T> findClassThatExtendsClass(
        Class<T> parentClass, String packagePrefix) {

        validateInputs(parentClass, packagePrefix);

        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(parentClass);

        Optional<Class<? extends T>> match = result.stream()
                                                 .findFirst();
        return match.orElse(null);
    }


    private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy.");
    }


    private static void validateInputs(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalArgumentException("Input parameter cannot be null.");
            }
            if (obj instanceof String && ((String) obj).trim().isEmpty()) {
                throw new IllegalArgumentException("String input parameter cannot be empty.");
            }
        }
    }

}
