package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class LoaderServiceFluent {

    private final LoaderService loaderService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public LoaderServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, LoaderService loaderService,
                               SmartWebDriver webDriver) {
        this.loaderService = loaderService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent isVisible(final UIElement element) {
        Allure.step(String.format("Checking if loader is visible for loader component of type: '%s'.",
                element.componentType().toString()));
        element.before().accept(driver);
        boolean visible = loaderService.isVisible(element.componentType(), element.locator());
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
        boolean visible = loaderService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Loader is visible"
                : "Validating Loader is hidden";

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


    public UIServiceFluent waitToBeShown(final UIElement element, int secondsShown) {
        element.before().accept(driver);
        loaderService.waitToBeShown(element.componentType(), element.locator(), secondsShown);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent waitToBeRemoved(final UIElement element, int secondsRemoved) {
        element.before().accept(driver);
        loaderService.waitToBeRemoved(element.componentType(), element.locator(), secondsRemoved);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent waitToBeShownAndRemoved(final UIElement element, int secondsShown, int secondsRemoved) {
        element.before().accept(driver);
        loaderService.waitToBeShownAndRemoved(element.componentType(), element.locator(), secondsShown, secondsRemoved);
        element.after().accept(driver);
        return uiServiceFluent;
    }
}
