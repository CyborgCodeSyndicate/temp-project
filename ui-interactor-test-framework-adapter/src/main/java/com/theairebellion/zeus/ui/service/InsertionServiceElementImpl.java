package com.theairebellion.zeus.ui.service;

import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.insertion.BaseInsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

public class InsertionServiceElementImpl extends BaseInsertionService<InsertionElement> {

    private final SmartWebDriver webDriver;


    public InsertionServiceElementImpl(final InsertionServiceRegistry serviceRegistry,
                                       final SmartWebDriver webDriver) {
        super(serviceRegistry);
        this.webDriver = webDriver;
    }


    @Override
    protected Class<InsertionElement> getAnnotationClass() {
        return InsertionElement.class;
    }


    @Override
    protected int getOrder(final InsertionElement annotation) {
        return annotation.order();
    }


    @Override
    protected Class<? extends ComponentType> getComponentTypeEnumClass(final InsertionElement annotation) {
        final UIElement uiElement = (UIElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
        );
        return uiElement.componentType().getClass();
    }


    @Override
    protected By buildLocator(final InsertionElement annotation) {
        final UIElement uiElement = (UIElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
        );
        return uiElement.locator();
    }


    @Override
    protected ComponentType getType(final InsertionElement annotation) {
        final UIElement uiElement = (UIElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
        );
        return uiElement.componentType();
    }


    @Override
    protected void beforeInsertion(final InsertionElement annotation) {
        final UIElement uiElement = (UIElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
        );
        uiElement.before().accept(webDriver);
    }


    @Override
    protected void afterInsertion(final InsertionElement annotation) {
        final UIElement uiElement = (UIElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
        );
        uiElement.after().accept(webDriver);
    }

}
