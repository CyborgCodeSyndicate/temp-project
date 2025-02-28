package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public class InsertionServiceFieldImpl extends BaseInsertionService {


    public InsertionServiceFieldImpl(final InsertionServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }


    @Override
    protected Object getFieldAnnotation(Field field) {
        return field.getAnnotation(InsertionField.class);
    }


    @Override
    protected Class<? extends ComponentType> getComponentType(Object annotation) {
        InsertionField insertionField = (InsertionField) annotation;
        return insertionField.type();
    }


    @Override
    protected By buildLocator(Object annotation) {
        InsertionField insertionField = (InsertionField) annotation;
        FindBy.FindByBuilder findByBuilder = new FindBy.FindByBuilder();
        return findByBuilder.buildIt(insertionField.locator(), null);
    }


    @Override
    protected Enum<?> getEnumValue(Object annotation) {
        InsertionField insertionField = (InsertionField) annotation;
        Class<? extends Enum> enumClass = ReflectionUtil.findEnumClassImplementationsOfInterface(
            insertionField.type(), getUiConfig().projectPackage()
        );
        return Enum.valueOf(enumClass, insertionField.componentType());
    }


    @Override
    protected List<Field> filterAndSortFields(final Field[] fields) {
        return Arrays.stream(fields)
                   .filter(field -> field.isAnnotationPresent(InsertionField.class))
                   .sorted(Comparator.comparing(field -> field.getAnnotation(InsertionField.class).order()))
                   .collect(Collectors.toList());
    }

    @Override
    protected ComponentType getType(Object annotation) { //todo: check this
        return null;
    }

}
