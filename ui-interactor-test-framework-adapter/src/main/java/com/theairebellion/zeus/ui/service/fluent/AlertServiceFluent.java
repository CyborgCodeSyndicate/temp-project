package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class AlertServiceFluent {

    private final AlertService alertService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public AlertServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, AlertService alertService,
                              SmartWebDriver webDriver) {
        this.alertService = alertService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent getValue(final UIElement element) {
        element.before().accept(driver);
        String value = alertService.getValue(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), value);
        return uiServiceFluent;
    }


    public UIServiceFluent validateValue(final UIElement element, String expectedValue) {
        return validateValue(element, expectedValue, false);
    }


    public UIServiceFluent validateValue(final UIElement element, String expectedValue, boolean soft) {
        element.before().accept(driver);
        String value = alertService.getValue(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), value);
        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(value).as("Validating Alert value")
                            .isEqualTo(expectedValue));
        } else {
            return uiServiceFluent.validate(
                    () -> Assertions.assertThat(value).as("Validating Alert value")
                            .isEqualTo(expectedValue));
        }
    }


    public UIServiceFluent isVisible(final UIElement element) {
        element.before().accept(driver);
        boolean enabled = alertService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
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
        boolean visible = alertService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Alert is visible"
                : "Validating Alert is hidden";

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
}
