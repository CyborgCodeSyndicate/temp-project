package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class SelectServiceFluent implements Insertion {

    private final SelectService selectService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public SelectServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, SelectService selectService,
                               SmartWebDriver webDriver) {
        this.selectService = selectService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent selectOptions(final UIElement element, final String... values) {
        Allure.step(String.format("Selecting options: '%s' from select component of type: '%s'.", Arrays.toString(values),
                element.componentType().toString()));
        element.before().accept(driver);
        selectService.selectOptions(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent selectOption(final UIElement element, final String value) {
        Allure.step(String.format("Selecting option: '%s' from select component of type: '%s'.", value,
                element.componentType().toString()));
        element.before().accept(driver);
        selectService.selectOption(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent getAvailableOptions(final UIElement element) {
        element.before().accept(driver);
        List<String> availableOptions = selectService.getAvailableOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), availableOptions);
        return uiServiceFluent;
    }


    public UIServiceFluent validateAvailableOptions(final UIElement element, final String... expectedValues) {
        return validateAvailableOptions(element, false, expectedValues);
    }


    public UIServiceFluent validateAvailableOptions(final UIElement element, boolean soft, final String... expectedValues) {
        element.before().accept(driver);
        List<String> availableOptions = selectService.getAvailableOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), availableOptions);

        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(availableOptions)
                            .as("Validating Available Options").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return uiServiceFluent.validate(
                    () -> Assertions.assertThat(availableOptions)
                            .as("Validating Available Options").containsAll(Arrays.asList(expectedValues))
            );
        }
    }


    public UIServiceFluent getSelectedOptions(final UIElement element) {
        element.before().accept(driver);
        List<String> selectedOptions = selectService.getSelectedOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedOptions);
        return uiServiceFluent;
    }


    public UIServiceFluent validateSelectedOptions(final UIElement element, final String... expectedValues) {
        return validateSelectedOptions(element, false, expectedValues);
    }


    public UIServiceFluent validateSelectedOptions(final UIElement element, boolean soft, final String... expectedValues) {
        element.before().accept(driver);
        List<String> availableOptions = selectService.getAvailableOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), availableOptions);

        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(availableOptions)
                            .as("Validating Selected Options").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return uiServiceFluent.validate(
                    () -> Assertions.assertThat(availableOptions)
                            .as("Validating Selected Options").containsAll(Arrays.asList(expectedValues))
            );
        }
    }


    public UIServiceFluent isOptionVisible(final UIElement element, final String value) {
        element.before().accept(driver);
        boolean visibleOption = selectService.isOptionVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visibleOption);
        return uiServiceFluent;
    }


    public UIServiceFluent validateIsOptionVisible(final UIElement element, final String value) {
        return validateIsVisible(element, true, value, false);
    }


    public UIServiceFluent validateIsOptionVisible(final UIElement element, final String value, boolean soft) {
        return validateIsVisible(element, true, value, soft);
    }


    public UIServiceFluent validateIsOptionHidden(final UIElement element, final String value) {
        return validateIsVisible(element, false, value, false);
    }


    public UIServiceFluent validateIsOptionHidden(final UIElement element, final String value, boolean soft) {
        return validateIsVisible(element, false, value, soft);
    }


    private UIServiceFluent validateIsVisible(final UIElement element, boolean shouldBeVisible, String value,
                                              boolean soft) {
        element.before().accept(driver);
        boolean visible = selectService.isOptionVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Option is visible"
                : "Validating Option is hidden";

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


    public UIServiceFluent isOptionEnabled(final UIElement element, final String value) {
        element.before().accept(driver);
        boolean enabledOption = selectService.isOptionEnabled(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabledOption);
        return uiServiceFluent;
    }


    public UIServiceFluent validateIsOptionEnabled(final UIElement element, final String value) {
        return validateIsEnabled(element, true, value, false);
    }


    public UIServiceFluent validateIsOptionEnabled(final UIElement element, final String value, boolean soft) {
        return validateIsEnabled(element, true, value, soft);
    }


    public UIServiceFluent validateIsOptionDisabled(final UIElement element, final String value) {
        return validateIsEnabled(element, false, value, false);
    }


    public UIServiceFluent validateIsOptionDisabled(final UIElement element, final String value, boolean soft) {
        return validateIsEnabled(element, false, value, soft);
    }


    private UIServiceFluent validateIsEnabled(final UIElement element, boolean shouldBeEnabled, String value,
                                              boolean soft) {
        element.before().accept(driver);
        boolean enabled = selectService.isOptionVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Option is enabled"
                : "Validating Option is disabled";

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


    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        selectService.insertion(componentType, locator, values);
    }

}
