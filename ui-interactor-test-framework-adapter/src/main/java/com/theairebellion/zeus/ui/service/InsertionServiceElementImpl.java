package com.theairebellion.zeus.ui.service;

import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.insertion.BaseInsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InsertionServiceElementImpl extends BaseInsertionService {

    private final SmartWebDriver webDriver;

    public InsertionServiceElementImpl(final InsertionServiceRegistry serviceRegistry, final SmartWebDriver webDriver) {
        super(serviceRegistry);
        this.webDriver = webDriver;
    }


    @Override
    protected Object getFieldAnnotation(Field field) {
        return field.getAnnotation(InsertionElement.class);
    }


    @Override
    protected Class<? extends ComponentType> getComponentTypeEnumClass(Object annotation) {
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
        return uiElement.locator();
    }


    @Override
    protected ComponentType getType(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(),
                insertionElement.elementEnum());
        return uiElement.componentType();
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


    @Override
    protected void beforeInsertion(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(),
                insertionElement.elementEnum());
        uiElement.before().accept(webDriver);
    }


    @Override
    protected void afterInsertion(Object annotation) {
        InsertionElement insertionElement = (InsertionElement) annotation;
        UIElement uiElement = (UIElement) Enum.valueOf((Class<? extends Enum>) insertionElement.locatorClass(),
                insertionElement.elementEnum());
        uiElement.after().accept(webDriver);
    }

}
