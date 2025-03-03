package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public abstract class BaseInsertionService implements InsertionService {

    protected final InsertionServiceRegistry serviceRegistry;


    public BaseInsertionService(final InsertionServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }


    @Override
    public void insertData(final Object data) {
        Field[] fields = data.getClass().getDeclaredFields();
        List<Field> targetedFields = filterAndSortFields(fields);

        for (Field field : targetedFields) {
            Optional<?> annotation = Optional.ofNullable(getFieldAnnotation(field));
            if (annotation.isEmpty()) {
                continue;
            }
            field.setAccessible(true);

            try {
                Class<? extends ComponentType> componentTypeClass = getComponentType(annotation);
                Insertion service = serviceRegistry.getService(componentTypeClass);
                if (service == null) {
                    throw new IllegalStateException(
                        "No InsertionService registered for: " + componentTypeClass.getSimpleName()
                    );
                }

                By locator = buildLocator(annotation);
                LogUI.debug("Field [{}] -> Locator: [{}]", field.getName(), locator);

                Enum<?> enumValue = getEnumValue(annotation);
                LogUI.debug("Field [{}] -> Enum component type: [{}]", field.getName(), enumValue);

                Object valueForField = field.get(data);

                if (valueForField != null) {
                    beforeInsertion(annotation);
                    service.insertion((ComponentType) enumValue, locator, valueForField);
                    afterInsertion(annotation);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        LogUI.info("Finished data insertion for [{}].", data.getClass().getSimpleName());
    }


    protected abstract Object getFieldAnnotation(Field field);

    protected abstract Class<? extends ComponentType> getComponentType(Object annotation);

    protected abstract By buildLocator(Object annotation);

    protected abstract Enum<?> getEnumValue(Object annotation);

    protected abstract List<Field> filterAndSortFields(Field[] fields);


    protected void beforeInsertion(Object annotation) {

    }


    protected void afterInsertion(Object annotation) {

    }


}
