package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.CheckboxUIElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class CheckboxServiceFluent implements Insertion {

    private final CheckboxService checkboxService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public CheckboxServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, CheckboxService checkboxService) {
        this.checkboxService = checkboxService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }

    public UIServiceFluent select(final CheckboxUIElement element) {
        Allure.step(String.format("Selecting checkbox with locator: '%s' from checkbox component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        checkboxService.select((CheckboxComponentType) element.componentType(), element.locator()); //todo: Check why cast is needed here
        return uiServiceFluent;
    }

    public UIServiceFluent deSelect(final CheckboxUIElement element) {
        Allure.step(String.format("Deselecting checkbox with locator: '%s' from checkbox component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        checkboxService.deSelect((CheckboxComponentType) element.componentType(), element.locator());
        return uiServiceFluent;
    }

    public UIServiceFluent isSelected(final CheckboxUIElement element) {
        boolean selected = checkboxService.isSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    public UIServiceFluent areSelected(final CheckboxUIElement element) {
        boolean selected = checkboxService.areSelected((CheckboxComponentType) element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    public UIServiceFluent isEnabled(final CheckboxUIElement element) {
        boolean selected = checkboxService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    public UIServiceFluent areEnabled(final CheckboxUIElement element) {
        boolean selected = checkboxService.areEnabled((CheckboxComponentType) element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    public UIServiceFluent getSelected(final CheckboxUIElement element) {
        List<String> selectedValues = checkboxService.getSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selectedValues);
        return uiServiceFluent;
    }

    public UIServiceFluent getAll(final CheckboxUIElement element) {
        checkboxService.getAll(element.componentType(), element.locator()); //todo: Do we need storage
        return uiServiceFluent;
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        checkboxService.insertion(componentType, locator, values);
    }
}
