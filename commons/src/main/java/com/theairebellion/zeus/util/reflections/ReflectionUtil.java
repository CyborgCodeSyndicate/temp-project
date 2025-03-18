package com.theairebellion.zeus.util.reflections;

import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Utility class for performing advanced reflection-based operations.
 * <p>
 * This class provides methods to dynamically discover class implementations, retrieve
 * field values, and interact with class hierarchies using reflection.
 * It facilitates working with enums, interfaces, and generic field retrieval.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Finding implementations of interfaces or subclasses in a given package.</li>
 *     <li>Retrieving private or protected field values from objects.</li>
 *     <li>Handling enums that implement interfaces dynamically.</li>
 *     <li>Safeguarding reflection operations with structured exception handling.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate
 */
public class ReflectionUtil {

    /**
     * Finds an enum class that implements a given interface within a specified package.
     *
     * @param interfaceClass The interface whose enum implementation is being searched.
     * @param packagePrefix  The package to search within.
     * @param <T>            The type of the interface.
     * @return The enum class implementing the specified interface.
     * @throws ReflectionException If no matching enum is found.
     */
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

    /**
     * Finds a specific enum constant that implements a given interface.
     *
     * @param interfaceClass The interface implemented by the enum.
     * @param enumName       The name of the enum constant.
     * @param packagePrefix  The package to search within.
     * @param <T>            The type of the interface.
     * @return The matching enum constant.
     * @throws ReflectionException If the enum or constant is not found.
     */
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

    /**
     * Finds all class implementations of a given interface within a package.
     *
     * @param interfaceClass The interface whose implementations are to be found.
     * @param packagePrefix  The package to search within.
     * @param <T>            The type of the interface.
     * @return A list of classes implementing the specified interface.
     */
    public static <T> List<Class<? extends T>> findImplementationsOfInterface(Class<T> interfaceClass,
                                                                              String packagePrefix) {
        validateInputs(interfaceClass, packagePrefix);

        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(interfaceClass);
        return new ArrayList<>(result);
    }

    /**
     * Retrieves a field value of a specified type from an object.
     *
     * @param instance  The object whose field value is being retrieved.
     * @param fieldType The expected type of the field.
     * @param <K>       The type parameter of the field value.
     * @return The value of the field.
     * @throws ReflectionException If the field is not found or cannot be accessed.
     */
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

    /**
     * Retrieves a specific field value from an object's class hierarchy.
     *
     * @param fieldName  The name of the field to retrieve.
     * @param object     The object from which the field value is retrieved.
     * @param returnType The expected return type.
     * @param <T>        The type parameter of the return value.
     * @return The value of the field.
     * @throws ReflectionException If the field is not found or is inaccessible.
     */
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

    /**
     * Finds a class that extends a given parent class within a package.
     *
     * @param parentClass    The parent class whose subclass is being searched for.
     * @param packagePrefix  The package to search within.
     * @param <T>            The type parameter for the parent class.
     * @return The first subclass found or {@code null} if none exist.
     */
    public static <T> Class<? extends T> findClassThatExtendsClass(
        Class<T> parentClass, String packagePrefix) {

        validateInputs(parentClass, packagePrefix);

        Reflections reflections = new Reflections(packagePrefix);
        Set<Class<? extends T>> result = reflections.getSubTypesOf(parentClass);

        Optional<Class<? extends T>> match = result.stream()
                                                 .findFirst();
        return match.orElse(null);
    }

    /**
     * Retrieves a field from a class hierarchy.
     *
     * @param clazz     The class to search.
     * @param fieldName The field name to find.
     * @return The matching field.
     * @throws NoSuchFieldException If the field does not exist.
     */
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

    /**
     * Validates input parameters, ensuring they are non-null and non-empty.
     *
     * @param objects The objects to validate.
     * @throws IllegalArgumentException If any parameter is invalid.
     */
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
