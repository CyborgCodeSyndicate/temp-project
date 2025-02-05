package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class RadioServiceFluent implements Insertion {

    private final RadioService radioService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public RadioServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, RadioService radioService,
                              SmartWebDriver webDriver) {
        this.radioService = radioService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent select(final UIElement element) {
        Allure.step(String.format("Selecting Radio Input with locator: '%s' from radio component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        radioService.select(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        element.before().accept(driver);
        boolean enabled = radioService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent validateIsEnabled(final UIElement element) {
        return validateIsEnabled(element, true, false);
    }


    public UIServiceFluent validateIsEnabled(final UIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }


    public UIServiceFluent validateIsDisabled(final UIElement element) {
        return validateIsEnabled(element, false, false);
    }


    public UIServiceFluent validateIsDisabled(final UIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }


    private UIServiceFluent validateIsEnabled(final UIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = radioService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Radio Input is enabled"
                : "Validating Radio Input is disabled";

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


    public UIServiceFluent isSelected(final UIElement element) {
        element.before().accept(driver);
        boolean selected = radioService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }


    public UIServiceFluent validateIsSelected(final UIElement element) {
        return validateIsSelected(element, true, false);
    }


    public UIServiceFluent validateIsSelected(final UIElement element, boolean soft) {
        return validateIsSelected(element, true, soft);
    }


    public UIServiceFluent validateIsNotSelected(final UIElement element) {
        return validateIsSelected(element, false, false);
    }


    public UIServiceFluent validateIsNotSelected(final UIElement element, boolean soft) {
        return validateIsSelected(element, false, soft);
    }


    private UIServiceFluent validateIsSelected(final UIElement element, boolean shouldBeSelected, boolean soft) {
        element.before().accept(driver);
        boolean selected = radioService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        String assertionMessage = shouldBeSelected
                ? "Validating Radio Input is selected"
                : "Validating Radio Input is not selected";

        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeSelected) {
                            softAssertions.assertThat(selected).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(selected).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return uiServiceFluent.validate(
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


    public UIServiceFluent isVisible(final UIElement element) {
        element.before().accept(driver);
        boolean visible = radioService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public UIServiceFluent validateIsVisible(final UIElement element) {
        return validateIsVisible(element, true, false);
    }


    public UIServiceFluent validateIsVisible(final UIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }


    public UIServiceFluent validateIsHidden(final UIElement element) {
        return validateIsVisible(element, false, false);
    }


    public UIServiceFluent validateIsHidden(final UIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }


    private UIServiceFluent validateIsVisible(final UIElement element, boolean shouldBeVisible, boolean soft) {
        element.before().accept(driver);
        boolean visible = radioService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Radio Input is visible"
                : "Validating Radio Input is hidden";

        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeVisible) {
                            softAssertions.assertThat(visible).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(visible).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return uiServiceFluent.validate(
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


    public UIServiceFluent getSelected(final UIElement element) {
        element.before().accept(driver);
        String selectedValue = radioService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedValue);
        return uiServiceFluent;
    }


    public UIServiceFluent validateSelected(final UIElement element, String expected) {
        return validateSelected(element, expected,false);
    }


    public UIServiceFluent validateSelected(final UIElement element, String expected, boolean soft) {
        element.before().accept(driver);
        String selectedRadioInput = radioService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedRadioInput);

        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedRadioInput)
                            .as("Validating Selected Radio Input").isEqualTo(expected)
            );
        } else {
            return uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedRadioInput)
                            .as("Validating Selected Radio Input").isEqualTo(expected)
            );
        }
    }


    public UIServiceFluent getAll(final UIElement element) {
        element.before().accept(driver);
        radioService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent validateAllRadioInputs(final UIElement element, final String... expectedValues) {
        return validateAllRadioInputs(element, false, expectedValues);
    }


    public UIServiceFluent validateAllRadioInputs(final UIElement element, boolean soft,
                                                  final String... expectedValues) {
        element.before().accept(driver);
        List<String> selectedRadioInputs = radioService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedRadioInputs);

        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedRadioInputs)
                            .as("Validating Radio Inputs").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return uiServiceFluent.validate(
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
