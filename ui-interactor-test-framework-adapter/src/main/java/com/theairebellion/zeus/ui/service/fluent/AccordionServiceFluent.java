package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;
import com.theairebellion.zeus.ui.components.accordion.AccordionService;
import com.theairebellion.zeus.ui.selenium.AccordionUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;

import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class AccordionServiceFluent<T extends UIServiceFluent<?>> {

    private final AccordionService accordionService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public AccordionServiceFluent(T uiServiceFluent, Storage storage, AccordionService accordionService,
                                  SmartWebDriver webDriver) {
        this.accordionService = accordionService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public T expand(final AccordionUIElement element) {
        Allure.step(String.format("Expanding panel with locator: '%s' from accordion component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        accordionService.expand((AccordionComponentType) element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T collapse(final AccordionUIElement element) {
        Allure.step(String.format("Collapsing panel with locator: '%s' from accordion component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        accordionService.collapse((AccordionComponentType) element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T areEnabled(final AccordionUIElement element) {
        element.before().accept(driver);
        boolean enabled = accordionService.areEnabled((AccordionComponentType) element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T getExpanded(final AccordionUIElement element) {
        element.before().accept(driver);
        List<String> expanded = accordionService.getExpanded(element.componentType()); //todo
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), expanded);
        return uiServiceFluent;
    }


    public T getCollapsed(final AccordionUIElement element) {
        element.before().accept(driver);
        List<String> collapsed = accordionService.getCollapsed(element.componentType()); //todo
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), collapsed);
        return uiServiceFluent;
    }


    public T getAll(final AccordionUIElement element) {
        element.before().accept(driver);
        List<String> all = accordionService.getAll(element.componentType()); //todo
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), all);
        return uiServiceFluent;
    }


    public T getTitle(final AccordionUIElement element) {
        element.before().accept(driver);
        String title = accordionService.getTitle(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), title);
        return uiServiceFluent;
    }


    public T getText(final AccordionUIElement element) {
        element.before().accept(driver);
        String text = accordionService.getText(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), text);
        return uiServiceFluent;
    }


//todo: Implement validation functions

}
