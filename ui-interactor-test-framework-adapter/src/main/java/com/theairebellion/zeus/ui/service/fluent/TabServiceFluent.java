package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.tab.TabService;
import com.theairebellion.zeus.ui.selenium.TabUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

/**
 * Provides a fluent API for interacting with tab UI components.
 * <p>
 * This class enables interactions with tab elements, including clicking,
 * checking visibility, validating selection, and enabling or disabling tabs.
 * It integrates seamlessly with {@link UIServiceFluent} to support method chaining.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class TabServiceFluent<T extends UIServiceFluent<?>> {

    private final TabService tabService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a new {@code TabServiceFluent} instance.
     *
     * @param uiServiceFluent The parent UI service fluent instance.
     * @param storage         The storage instance for persisting UI states.
     * @param tabService      The tab service instance for performing tab-related operations.
     * @param webDriver       The WebDriver instance for browser interactions.
     */
    public TabServiceFluent(T uiServiceFluent, Storage storage, TabService tabService,
                            SmartWebDriver webDriver) {
        this.tabService = tabService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }

    /**
     * Clicks on the specified tab UI element.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T click(final TabUIElement element) {
        Allure.step("[UI - Tab] Clicking on the tab " + element);
        element.before().accept(driver);
        tabService.click(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Checks if the specified tab is enabled.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T isEnabled(final TabUIElement element) {
        Allure.step("[UI - Tab] Checking if the tab " + element + " is enabled.");
        element.before().accept(driver);
        boolean enabled = tabService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified tab is enabled.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T validateIsEnabled(final TabUIElement element) {
        return validateIsEnabled(element, true, false);
    }

    /**
     * Validates whether the specified tab is enabled or disabled.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @param soft    Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateIsEnabled(final TabUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }

    /**
     * Validates that the specified tab is disabled.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T validateIsDisabled(final TabUIElement element) {
        return validateIsEnabled(element, false, false);
    }

    /**
     * Validates whether the specified tab is disabled.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @param soft    Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateIsDisabled(final TabUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }

    private T validateIsEnabled(final TabUIElement element, boolean shouldBeEnabled, boolean soft) {
        Allure.step("[UI - Tab] Validating if the tab " + element + " is " + (shouldBeEnabled ? "enabled" : "disabled"));
        element.before().accept(driver);
        boolean enabled = tabService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Tab is enabled"
                : "Validating Tab is disabled";

        if (soft) {
            Allure.step("[UI - Tab] Performing soft validation for the tab " + element);
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
            Allure.step("[UI - Tab] Performing strict validation for the tab " + element);
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

    /**
     * Checks if the specified tab is visible.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T isVisible(final TabUIElement element) {
        Allure.step("[UI - Tab] Checking if the tab " + element + " is visible.");
        element.before().accept(driver);
        boolean visible = tabService.isVisible(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), visible);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified tab is visible.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final TabUIElement element) {
        return validateIsVisible(element, true, false);
    }

    /**
     * Validates whether the specified tab is visible.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @param soft    Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final TabUIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }

    /**
     * Validates that the specified tab is hidden.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final TabUIElement element) {
        return validateIsVisible(element, false, false);
    }

    /**
     * Validates whether the specified tab is hidden.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @param soft    Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final TabUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }

    private T validateIsVisible(final TabUIElement element, boolean shouldBeVisible, boolean soft) {
        Allure.step("[UI - Tab] Validating if the tab " + element + " is " + (shouldBeVisible ? "visible" : "hidden"));
        element.before().accept(driver);
        boolean visible = tabService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Tab is visible"
                : "Validating Tab is hidden";

        if (soft) {
            Allure.step("[UI - Tab] Performing soft validation for the tab " + element);
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
            Allure.step("[UI - Tab] Performing strict validation for the tab " + element);
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

    /**
     * Checks if the specified tab is selected.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T isSelected(final TabUIElement element) {
        Allure.step("[UI - Tab] Checking if the tab " + element + " is selected.");
        element.before().accept(driver);
        boolean selected = tabService.isSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified tab is selected.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T validateIsSelected(final TabUIElement element) {
        return validateIsSelected(element, true, false);
    }

    /**
     * Validates whether the specified tab is selected.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsSelected(final TabUIElement element, boolean soft) {
        return validateIsSelected(element, true, soft);
    }

    /**
     * Validates whether the specified tab is not selected.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @return The fluent UI service instance.
     */
    public T validateIsNotSelected(final TabUIElement element) {
        return validateIsSelected(element, false, false);
    }

    /**
     * Validates whether the specified tab is not selected.
     *
     * @param element The {@link TabUIElement} representing the tab.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsNotSelected(final TabUIElement element, boolean soft) {
        return validateIsSelected(element, false, soft);
    }

    private T validateIsSelected(final TabUIElement element, boolean shouldBeSelected, boolean soft) {
        Allure.step("[UI - Tab] Validating if the tab " + element + " is " + (shouldBeSelected ? "selected" : "not selected"));
        element.before().accept(driver);
        boolean selected = tabService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        String assertionMessage = shouldBeSelected
                ? "Validating Tab is selected"
                : "Validating Tab is not selected";

        if (soft) {
            Allure.step("[UI - Tab] Performing soft validation for the tab " + element);
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
            Allure.step("[UI - Tab] Performing strict validation for the tab " + element);
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
