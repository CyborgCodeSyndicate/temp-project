package com.theairebellion.zeus.ui.service;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.insertion.BaseInsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.selenium.UIElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InsertionServiceElementImpl extends BaseInsertionService {

    public InsertionServiceElementImpl(final InsertionServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }


    @Override
    protected Object getFieldAnnotation(Field field) {
        return field.getAnnotation(InsertionElement.class);
    }


    @Override
    protected Class<? extends ComponentType> getComponentType(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(),
            insertionElement.elementEnum());
        return uiElement.componentType().getClass();
    }


    @Override
    protected By buildLocator(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(),
            insertionElement.elementEnum());
        FindBy.FindByBuilder findByBuilder = new FindBy.FindByBuilder();
        return findByBuilder.buildIt(uiElement.locator(), null);
    }


    @Override
    protected Enum<?> getEnumValue(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        return Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(), insertionElement.elementEnum());
    }

    @Override
    protected List<Field> filterAndSortFields(final Field[] fields) {
        return Arrays.stream(fields)
                   .filter(field -> field.isAnnotationPresent(InsertionElement.class))
                   .sorted(Comparator.comparing(field -> field.getAnnotation(InsertionElement.class).order()))
                   .collect(Collectors.toList());
    }

}
