package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.selenium.ButtonUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides a fluent API for interacting with button components.
 * <p>
 * This class enables structured interactions with UI buttons, including actions such as clicking,
 * validating visibility, and checking whether a button is enabled. It integrates with {@link ButtonService}
 * to perform UI operations efficiently.
 * </p>
 *
 * <p>
 * The fluent API design allows method chaining to improve test readability and maintainability.
 * </p>
 *
 * @param <T> Represents the fluent UI service that extends {@link UIServiceFluent}.
 * This type parameter ensures that method chaining correctly returns the calling instance type.
 *
 * @author Cyborg Code Syndicate
 */
@SuppressWarnings("java:S5960")
public class ButtonServiceFluent<T extends UIServiceFluent<?>> {

    private final ButtonService buttonService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs an instance of {@code ButtonServiceFluent}.
     *
     * @param uiServiceFluent The UI service fluent instance
     * @param storage         The storage instance for persisting test values
     * @param buttonService   The button service instance
     * @param webDriver       The WebDriver instance
     */
    public ButtonServiceFluent(T uiServiceFluent, Storage storage, ButtonService buttonService,
                               SmartWebDriver webDriver) {
        this.buttonService = buttonService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }

    /**
     * Clicks the specified button element.
     *
     * @param element The button element to click
     * @return The fluent UI service instance
     */
    public T click(final ButtonUIElement element) {
        Allure.step(String.format("[UI - Button] Clicking button with componentType: %s, locator: %s",
                element.componentType(), element.locator()));
        element.before().accept(driver);
        buttonService.click(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Checks if the button is enabled.
     *
     * @param element The button element
     * @return The fluent UI service instance
     */
    public T isEnabled(final ButtonUIElement element) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled with componentType: %s, locator: %s",
                element.componentType(), element.locator()));
        element.before().accept(driver);
        boolean enabled = buttonService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Validates if the button is enabled.
     *
     * @param element The button element
     * @return The fluent UI service instance
     */
    public T validateIsEnabled(final ButtonUIElement element) {
        return validateIsEnabled(element, true, false);
    }

    /**
     * Validates if the button is enabled with soft assertion.
     *
     * @param element The button element
     * @param soft    Whether to perform a soft assertion
     * @return The fluent UI service instance
     */
    public T validateIsEnabled(final ButtonUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }

    /**
     * Validates if the button is disabled.
     *
     * @param element The button element
     * @return The fluent UI service instance
     */
    public T validateIsDisabled(final ButtonUIElement element) {
        return validateIsEnabled(element, false, false);
    }

    /**
     * Validates if the button is disabled with soft assertion.
     *
     * @param element The button element
     * @param soft    Whether to perform a soft assertion
     * @return The fluent UI service instance
     */
    public T validateIsDisabled(final ButtonUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }

    private T validateIsEnabled(final ButtonUIElement element, boolean shouldBeEnabled, boolean soft) {
        Allure.step(String.format("[UI - Button] Validating if button is enabled/disabled with componentType: %s, locator: %s",
                element.componentType(), element.locator()));
        element.before().accept(driver);
        boolean enabled = buttonService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Button is enabled"
                : "Validating Button is disabled";

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

    /**
     * Checks if the button is visible.
     *
     * @param element The button element
     * @return The fluent UI service instance
     */
    public T isVisible(final ButtonUIElement element) {
        Allure.step(String.format("[UI - Button] Checking if button is visible with componentType: %s, locator: %s",
                element.componentType(), element.locator()));
        element.before().accept(driver);
        boolean visible = buttonService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }

    /**
     * Validates if the button is visible.
     *
     * @param element The button element
     * @return The fluent UI service instance
     */
    public T validateIsVisible(final ButtonUIElement element) {
        return validateIsVisible(element, true, false);
    }

    /**
     * Validates whether the specified button UI element is visible.
     *
     * @param element The {@link ButtonUIElement} to be validated.
     * @param soft    If {@code true}, the validation will be performed as a soft assertion.
     * @return The instance of {@link UIServiceFluent} to allow method chaining.
     */
    public T validateIsVisible(final ButtonUIElement element, boolean soft) {
        Allure.step(String.format("[UI - Button] Validating if button is visible with componentType: %s, locator: %s",
                element.componentType(), element.locator()));
        return validateIsVisible(element, true, soft);
    }

    /**
     * Validates if the button is hidden.
     *
     * @param element The button element
     * @return The fluent UI service instance
     */
    public T validateIsHidden(final ButtonUIElement element) {
        return validateIsVisible(element, false, false);
    }

    /**
     * Validates whether the specified button UI element is hidden.
     *
     * @param element The {@link ButtonUIElement} to be validated.
     * @param soft    If {@code true}, the validation will be performed as a soft assertion.
     * @return The instance of {@link UIServiceFluent} to allow method chaining.
     */
    public T validateIsHidden(final ButtonUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }

    private T validateIsVisible(final ButtonUIElement element, boolean shouldBeVisible, boolean soft) {
        Allure.step(String.format("[UI - Button] Validating if button is visible/hidden with componentType: %s, locator: %s",
                element.componentType(), element.locator()));
        element.before().accept(driver);
        boolean visible = buttonService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Button is visible"
                : "Validating Button is hidden";

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
}
