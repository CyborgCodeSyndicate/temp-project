package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.selenium.ButtonUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ButtonServiceFluent<T extends UIServiceFluent<?>> {

    private final ButtonService buttonService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public ButtonServiceFluent(T uiServiceFluent, Storage storage, ButtonService buttonService,
                               SmartWebDriver webDriver) {
        this.buttonService = buttonService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public T click(final ButtonUIElement element) {
        element.before().accept(driver);
        buttonService.click(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T isEnabled(final ButtonUIElement element) {
        element.before().accept(driver);
        boolean enabled = buttonService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T validateIsEnabled(final ButtonUIElement element) {
        return validateIsEnabled(element, true, false);
    }


    public T validateIsEnabled(final ButtonUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }


    public T validateIsDisabled(final ButtonUIElement element) {
        return validateIsEnabled(element, false, false);
    }


    public T validateIsDisabled(final ButtonUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }


    private T validateIsEnabled(final ButtonUIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = buttonService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Button is enabled"
                : "Validating Button is disabled";

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeEnabled) {
                            softAssertions.assertThat(enabled).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(enabled).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeEnabled) {
                            Assertions.assertThat(enabled).as(assertionMessage).isTrue();
                        } else {
                            Assertions.assertThat(enabled).as(assertionMessage).isFalse();
                        }
                    }
            );
        }
    }


    public T isVisible(final ButtonUIElement element) {
        element.before().accept(driver);
        boolean visible = buttonService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public T validateIsVisible(final ButtonUIElement element) {
        return validateIsVisible(element, true, false);
    }


    public T validateIsVisible(final ButtonUIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }


    public T validateIsHidden(final ButtonUIElement element) {
        return validateIsVisible(element, false, false);
    }


    public T validateIsHidden(final ButtonUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }


    private T validateIsVisible(final ButtonUIElement element, boolean shouldBeVisible, boolean soft) {
        element.before().accept(driver);
        boolean visible = buttonService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Button is visible"
                : "Validating Button is hidden";

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeVisible) {
                            softAssertions.assertThat(visible).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(visible).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeVisible) {
                            Assertions.assertThat(visible).as(assertionMessage).isTrue();
                        } else {
                            Assertions.assertThat(visible).as(assertionMessage).isFalse();
                        }
                    }
            );
        }
    }
}
