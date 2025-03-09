package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.tab.TabService;
import com.theairebellion.zeus.ui.selenium.TabUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class TabServiceFluent<T extends UIServiceFluent<?>> {

    private final TabService tabService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public TabServiceFluent(T uiServiceFluent, Storage storage, TabService tabService,
                            SmartWebDriver webDriver) {
        this.tabService = tabService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public T click(final TabUIElement element) {
        element.before().accept(driver);
        tabService.click(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T isEnabled(final TabUIElement element) {
        element.before().accept(driver);
        boolean enabled = tabService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T validateIsEnabled(final TabUIElement element) {
        return validateIsEnabled(element, true, false);
    }


    public T validateIsEnabled(final TabUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }


    public T validateIsDisabled(final TabUIElement element) {
        return validateIsEnabled(element, false, false);
    }


    public T validateIsDisabled(final TabUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }


    private T validateIsEnabled(final TabUIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = tabService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Tab is enabled"
                : "Validating Tab is disabled";

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


    public T isVisible(final TabUIElement element) {
        element.before().accept(driver);
        boolean visible = tabService.isVisible(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), visible);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T validateIsVisible(final TabUIElement element) {
        return validateIsVisible(element, true, false);
    }


    public T validateIsVisible(final TabUIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }


    public T validateIsHidden(final TabUIElement element) {
        return validateIsVisible(element, false, false);
    }


    public T validateIsHidden(final TabUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }


    private T validateIsVisible(final TabUIElement element, boolean shouldBeVisible, boolean soft) {
        element.before().accept(driver);
        boolean visible = tabService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Tab is visible"
                : "Validating Tab is hidden";

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


    public T isSelected(final TabUIElement element) {
        element.before().accept(driver);
        boolean selected = tabService.isSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T validateIsSelected(final TabUIElement element) {
        return validateIsSelected(element, true, false);
    }


    public T validateIsSelected(final TabUIElement element, boolean soft) {
        return validateIsSelected(element, true, soft);
    }


    public T validateIsNotSelected(final TabUIElement element) {
        return validateIsSelected(element, false, false);
    }


    public T validateIsNotSelected(final TabUIElement element, boolean soft) {
        return validateIsSelected(element, false, soft);
    }


    private T validateIsSelected(final TabUIElement element, boolean shouldBeSelected, boolean soft) {
        element.before().accept(driver);
        boolean selected = tabService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        String assertionMessage = shouldBeSelected
                ? "Validating Tab is selected"
                : "Validating Tab is not selected";

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
}
