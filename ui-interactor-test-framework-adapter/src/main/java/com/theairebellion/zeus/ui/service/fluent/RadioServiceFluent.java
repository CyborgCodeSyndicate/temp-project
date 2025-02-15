package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.RadioUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class RadioServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final RadioService radioService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public RadioServiceFluent(T uiServiceFluent, Storage storage, RadioService radioService,
                              SmartWebDriver webDriver) {
        this.radioService = radioService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public T select(final RadioUIElement element) {
        Allure.step(String.format("Selecting Radio Input with locator: '%s' from radio component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        radioService.select(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T isEnabled(final RadioUIElement element) {
        element.before().accept(driver);
        boolean enabled = radioService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T validateIsEnabled(final RadioUIElement element) {
        return validateIsEnabled(element, true, false);
    }


    public T validateIsEnabled(final RadioUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }


    public T validateIsDisabled(final RadioUIElement element) {
        return validateIsEnabled(element, false, false);
    }


    public T validateIsDisabled(final RadioUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }


    private T validateIsEnabled(final RadioUIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = radioService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Radio Input is enabled"
                : "Validating Radio Input is disabled";

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


    public T isSelected(final RadioUIElement element) {
        element.before().accept(driver);
        boolean selected = radioService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }


    public T validateIsSelected(final RadioUIElement element) {
        return validateIsSelected(element, true, false);
    }


    public T validateIsSelected(final RadioUIElement element, boolean soft) {
        return validateIsSelected(element, true, soft);
    }


    public T validateIsNotSelected(final RadioUIElement element) {
        return validateIsSelected(element, false, false);
    }


    public T validateIsNotSelected(final RadioUIElement element, boolean soft) {
        return validateIsSelected(element, false, soft);
    }


    private T validateIsSelected(final RadioUIElement element, boolean shouldBeSelected, boolean soft) {
        element.before().accept(driver);
        boolean selected = radioService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        String assertionMessage = shouldBeSelected
                ? "Validating Radio Input is selected"
                : "Validating Radio Input is not selected";

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeSelected) {
                            softAssertions.assertThat(selected).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(selected).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeSelected) {
                            Assertions.assertThat(selected).as(assertionMessage).isTrue();
                        } else {
                            Assertions.assertThat(selected).as(assertionMessage).isFalse();
                        }
                    }
            );
        }
    }


    public T isVisible(final RadioUIElement element) {
        element.before().accept(driver);
        boolean visible = radioService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public T validateIsVisible(final RadioUIElement element) {
        return validateIsVisible(element, true, false);
    }


    public T validateIsVisible(final RadioUIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }


    public T validateIsHidden(final RadioUIElement element) {
        return validateIsVisible(element, false, false);
    }


    public T validateIsHidden(final RadioUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }


    private T validateIsVisible(final RadioUIElement element, boolean shouldBeVisible, boolean soft) {
        element.before().accept(driver);
        boolean visible = radioService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Radio Input is visible"
                : "Validating Radio Input is hidden";

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


    public T getSelected(final RadioUIElement element) {
        element.before().accept(driver);
        String selectedValue = radioService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedValue);
        return uiServiceFluent;
    }


    public T validateSelected(final RadioUIElement element, String expected) {
        return validateSelected(element, expected, false);
    }


    public T validateSelected(final RadioUIElement element, String expected, boolean soft) {
        element.before().accept(driver);
        String selectedRadioInput = radioService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedRadioInput);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedRadioInput)
                            .as("Validating Selected Radio Input").isEqualTo(expected)
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedRadioInput)
                            .as("Validating Selected Radio Input").isEqualTo(expected)
            );
        }
    }


    public T getAll(final RadioUIElement element) {
        element.before().accept(driver);
        radioService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T validateAllRadioInputs(final RadioUIElement element, final String... expectedValues) {
        return validateAllRadioInputs(element, false, expectedValues);
    }


    public T validateAllRadioInputs(final RadioUIElement element, boolean soft,
                                                  final String... expectedValues) {
        element.before().accept(driver);
        List<String> selectedRadioInputs = radioService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedRadioInputs);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedRadioInputs)
                            .as("Validating Radio Inputs").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedRadioInputs)
                            .as("Validating Radio Inputs").containsAll(Arrays.asList(expectedValues))
            );
        }
    }


    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        radioService.insertion(componentType, locator, values);
    }
}
