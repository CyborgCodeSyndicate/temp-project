package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.openqa.selenium.By;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseInsertionService<A extends Annotation> implements InsertionService {

    protected final InsertionServiceRegistry serviceRegistry;


    protected BaseInsertionService(final InsertionServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }


    @Override
    public void insertData(final Object data) {
        final Field[] fields = data.getClass().getDeclaredFields();
        final List<Field> targetedFields = filterAndSortFields(fields);

        for (Field field : targetedFields) {
            final A annotation = field.getAnnotation(getAnnotationClass());
            if (annotation == null) {
                continue;
            }

            field.setAccessible(true);
            try {
                final Class<? extends ComponentType> enumClass = getComponentTypeEnumClass(annotation);
                final Class<? extends ComponentType> componentTypeClass = extractComponentTypeClass(enumClass);
                final Insertion service = serviceRegistry.getService(componentTypeClass);
                if (service == null) {
                    throw new IllegalStateException(
                        "No InsertionService registered for: " + componentTypeClass.getSimpleName()
                    );
                }

                final By locator = buildLocator(annotation);
                final Object valueForField = field.get(data);

                if (valueForField != null) {
                    beforeInsertion(annotation);
                    service.insertion(getType(annotation), locator, valueForField);
                    afterInsertion(annotation);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
    }


    protected abstract Class<A> getAnnotationClass();

    protected abstract int getOrder(A annotation);

    protected abstract Class<? extends ComponentType> getComponentTypeEnumClass(A annotation);

    protected abstract By buildLocator(A annotation);

    protected abstract ComponentType getType(A annotation);


    protected void beforeInsertion(A annotation) {
        // default no-op
    }


    protected void afterInsertion(A annotation) {
        // default no-op
    }


    protected final List<Field> filterAndSortFields(final Field[] fields) {
        return Arrays.stream(fields)
                   .filter(field -> field.isAnnotationPresent(getAnnotationClass()))
                   .sorted(Comparator.comparingInt(field ->
                                                       getOrder(field.getAnnotation(getAnnotationClass()))))
                   .collect(Collectors.toList());
    }


    protected static Class<? extends ComponentType> extractComponentTypeClass(
        final Class<? extends ComponentType> componentTypeClass) {

        @SuppressWarnings("unchecked") final Class<? extends ComponentType> resolved =
            (Class<? extends ComponentType>) Arrays.stream(componentTypeClass.getInterfaces())
                                                 .filter(inter -> Arrays.asList(inter.getInterfaces())
                                                                      .contains(ComponentType.class))
                                                 .findFirst()
                                                 .orElseThrow(() -> new IllegalStateException(
                                                     "No interface extending ComponentType found in " + componentTypeClass
                                                 ));
        return resolved;
    }

}