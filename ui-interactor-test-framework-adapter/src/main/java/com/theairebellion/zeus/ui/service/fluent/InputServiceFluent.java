package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.ai.metadata.model.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

@InfoAIClass(level = Level.LAST,
    description = "InputServiceFluent has all the functions needed for manipulation of input fields")
public class InputServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final InputService inputService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public InputServiceFluent(T uiServiceFluent, Storage storage, InputService inputService,
                              SmartWebDriver webDriver) {
        this.inputService = inputService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    @InfoAI(description = "Inserts text value into InputUIElement declared in a enum")
    public T insert(final InputUIElement element,
                    final @InfoAI(description = "Thr value to be inserted") String value) {
        Allure.step(String.format("Inserting value: '%s' into input component of type: '%s'.", value,
            element.componentType().toString()));
        element.before().accept(driver);
        inputService.insert(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T clear(final InputUIElement element) {
        element.before().accept(driver);
        inputService.clear(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T getValue(final InputUIElement element) {
        element.before().accept(driver);
        String value = inputService.getValue(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), value);
        return uiServiceFluent;
    }


    public T validateValue(final InputUIElement element, String expectedValue) {
        return validateValue(element, expectedValue, false);
    }


    public T validateValue(final InputUIElement element, String expectedValue, boolean soft) {
        element.before().accept(driver);
        String value = inputService.getValue(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), value);
        if (soft) {
            return (T) uiServiceFluent.validate(
                softAssertions -> softAssertions.assertThat(value).as("Validating Input value")
                                      .isEqualTo(expectedValue));
        } else {
            return (T) uiServiceFluent.validate(
                () -> Assertions.assertThat(value).as("Validating Input value")
                          .isEqualTo(expectedValue));
        }
    }


    public T isEnabled(final InputUIElement element) {
        element.before().accept(driver);
        boolean enabled = inputService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T validateIsEnabled(final InputUIElement element) {
        return validateIsEnabled(element, true, false);
    }


    public T validateIsEnabled(final InputUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }


    public T validateIsDisabled(final InputUIElement element) {
        return validateIsEnabled(element, false, false);
    }


    public T validateIsDisabled(final InputUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }


    private T validateIsEnabled(final InputUIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = inputService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                                      ? "Validating Input is enabled"
                                      : "Validating Input is disabled";

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


    public T getErrorMessage(final InputUIElement element) {
        element.before().accept(driver);
        String errorMessage = inputService.getErrorMessage(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), errorMessage);
        return uiServiceFluent;
    }


    public T validateErrorMessage(final InputUIElement element, String expectedMessage) {
        return validateErrorMessage(element, expectedMessage, false);
    }


    public T validateErrorMessage(final InputUIElement element, String expectedMessage, boolean soft) {
        element.before().accept(driver);
        String errorMessage = inputService.getErrorMessage(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), errorMessage);
        if (soft) {
            return (T) uiServiceFluent.validate(
                softAssertions -> softAssertions.assertThat(errorMessage)
                                      .as("Validating UI Message")
                                      .isEqualTo(expectedMessage));
        } else {
            return (T) uiServiceFluent.validate(
                () -> Assertions.assertThat(errorMessage).as("Validating UI Message")
                          .isEqualTo(expectedMessage));
        }
    }


    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        inputService.insertion(componentType, locator, values);
    }

}
