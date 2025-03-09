package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.CheckboxUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class CheckboxServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final CheckboxService checkboxService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public CheckboxServiceFluent(T uiServiceFluent, Storage storage, CheckboxService checkboxService,
                                 SmartWebDriver smartWebDriver) {
        this.checkboxService = checkboxService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        this.driver = smartWebDriver;
    }

    public T select(final CheckboxUIElement element) {
        checkboxService.select((CheckboxComponentType) element.componentType(), element.locator());
        return uiServiceFluent;
    }

    public T deSelect(final CheckboxUIElement element) {
        checkboxService.deSelect((CheckboxComponentType) element.componentType(), element.locator());
        return uiServiceFluent;
    }

    public T isSelected(final CheckboxUIElement element) {
        boolean selected = checkboxService.isSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    public T validateIsSelected(final CheckboxUIElement element) {
        return validateIsSelected(element, false);
    }

    public T validateIsSelected(final CheckboxUIElement element, boolean soft) {
        element.before().accept(driver);
        boolean selected = checkboxService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selected)
                            .as("Validating Checkbox Selected").isTrue()
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selected)
                            .as("Validating Checkbox Selected").isTrue()
            );
        }
    }

    public T areSelected(final CheckboxUIElement element) {
        boolean selected = checkboxService.areSelected((CheckboxComponentType) element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    public T isEnabled(final CheckboxUIElement element) {
        boolean enabled = checkboxService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    public T validateIsEnabled(final CheckboxUIElement element) {
        return validateIsEnabled(element, false);
    }

    public T validateIsEnabled(final CheckboxUIElement element, boolean soft) {
        element.before().accept(driver);
        boolean enabled = checkboxService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(enabled)
                            .as("Validating Checkbox Enabled").isTrue()
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(enabled)
                            .as("Validating Checkbox Enabled").isTrue()
            );
        }
    }

    public T areEnabled(final CheckboxUIElement element) {
        boolean enabled = checkboxService.areEnabled((CheckboxComponentType) element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    public T getSelected(final CheckboxUIElement element) {
        List<String> selectedValues = checkboxService.getSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selectedValues);
        return uiServiceFluent;
    }

    public T getAll(final CheckboxUIElement element) {
        checkboxService.getAll(element.componentType(), element.locator()); //todo: Do we need storage
        return uiServiceFluent;
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        checkboxService.insertion(componentType, locator, values);
    }
}
