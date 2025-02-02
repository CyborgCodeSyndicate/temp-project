package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class LinkServiceFluent {

    private final LinkService linkService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public LinkServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, LinkService linkService,
                             SmartWebDriver webDriver) {
        this.linkService = linkService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent click(final UIElement element) {
        Allure.step(String.format("Clicking link with locator: '%s' from link component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        linkService.click(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent doubleClick(final UIElement element) {
        element.before().accept(driver);
        linkService.doubleClick(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        element.before().accept(driver);
        boolean enabled = linkService.isEnabled(element.componentType(), element.locator());
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
        boolean enabled = linkService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Link is enabled"
                : "Validating Link is disabled";

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


    public UIServiceFluent isVisible(final UIElement element) {
        element.before().accept(driver);
        boolean visible = linkService.isVisible(element.componentType(), element.locator());
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
        boolean visible = linkService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Link is visible"
                : "Validating Link is hidden";

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
