package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class InputServiceFluent {

    private final InputService inputService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public InputServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, InputService inputService,
                              SmartWebDriver webDriver) {
        this.inputService = inputService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent insert(final InputUIElement element, final String value) {
        Allure.step(String.format("Inserting value: '%s' into input component of type: '%s'.", value,
            element.componentType().toString()));
        element.before().accept(driver);
        inputService.insert(element.locator(), value, element.componentType());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent clear(final InputUIElement element) {
        element.before().accept(driver);
        inputService.clear(element.locator(), element.componentType());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent getValue(final InputUIElement element) {
        element.before().accept(driver);
        String value = inputService.getValue(element.locator(), element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), value);
        return uiServiceFluent;
    }


    public UIServiceFluent validateValue(final InputUIElement element, String expectedValue) {
        return validateValue(element, expectedValue, false);
    }


    public UIServiceFluent validateValue(final InputUIElement element, String expectedValue, boolean soft) {
        element.before().accept(driver);
        String value = inputService.getValue(element.locator(), element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), value);
        if (soft) {
            return uiServiceFluent.validate(
                softAssertions -> softAssertions.assertThat(value).as("Validating Input value")
                                      .isEqualTo(expectedValue));
        } else {
            return uiServiceFluent.validate(
                () -> Assertions.assertThat(value).as("Validating Input value")
                          .isEqualTo(expectedValue));
        }
    }


    public UIServiceFluent isEnabled(final InputUIElement element) {
        element.before().accept(driver);
        boolean enabled = inputService.isEnabled(element.locator(), element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent validateIsEnabled(final InputUIElement element) {
        return validateIsEnabled(element, true, false);
    }


    public UIServiceFluent validateIsEnabled(final InputUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }


    public UIServiceFluent validateIsDisabled(final InputUIElement element) {
        return validateIsEnabled(element, false, false);
    }


    public UIServiceFluent validateIsDisabled(final InputUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }


    private UIServiceFluent validateIsEnabled(final InputUIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = inputService.isEnabled(element.locator(), element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                                      ? "Validating Input is enabled"
                                      : "Validating Input is disabled";

        if (soft) {
            return uiServiceFluent.validate(
                softAssertions -> {
                    if (shouldBeEnabled) {
                        softAssertions.assertThat(enabled).as(assertionMessage).isTrue();
                    } else {
                        softAssertions.assertThat(enabled).as(assertionMessage).isFalse();
                    }
                }
            );
        } else {
            return uiServiceFluent.validate(
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


    public UIServiceFluent getErrorMessage(final InputUIElement element) {
        element.before().accept(driver);
        String errorMessage = inputService.getErrorMessage(element.locator(), element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), errorMessage);
        return uiServiceFluent;
    }


    public UIServiceFluent validateErrorMessage(final InputUIElement element, String expectedMessage) {
        return validateErrorMessage(element, expectedMessage, false);
    }


    public UIServiceFluent validateErrorMessage(final InputUIElement element, String expectedMessage, boolean soft) {
        element.before().accept(driver);
        String errorMessage = inputService.getErrorMessage(element.locator(), element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), errorMessage);
        if (soft) {
            return uiServiceFluent.validate(
                softAssertions -> softAssertions.assertThat(errorMessage).as("Validating UI Message")
                                      .isEqualTo(expectedMessage));
        } else {
            return uiServiceFluent.validate(
                () -> Assertions.assertThat(errorMessage).as("Validating UI Message")
                          .isEqualTo(expectedMessage));
        }
    }

}
