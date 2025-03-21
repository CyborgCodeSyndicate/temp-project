package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.selenium.LinkUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

/**
 * Provides a fluent interface for interacting with link UI elements.
 * <p>
 * This class enables various link-related operations such as clicking,
 * double-clicking, and validation of visibility and enablement.
 * </p>
 *
 * The generic type {@code T} represents the UI service fluent implementation that extends {@link UIServiceFluent},
 * allowing method chaining for seamless interaction.
 *
 * @author Cyborg Code Syndicate
 */
public class LinkServiceFluent<T extends UIServiceFluent<?>> {

    private final LinkService linkService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a {@code LinkServiceFluent} instance.
     *
     * @param uiServiceFluent The UI service fluent instance to allow method chaining.
     * @param storage         The storage instance for tracking UI state.
     * @param linkService     The service handling link-related operations.
     * @param webDriver       The SmartWebDriver instance.
     */
    public LinkServiceFluent(T uiServiceFluent, Storage storage, LinkService linkService,
                             SmartWebDriver webDriver) {
        this.linkService = linkService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }

    /**
     * Clicks on the specified link element.
     *
     * @param element The {@link LinkUIElement} to be clicked.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T click(final LinkUIElement element) {
        Allure.step("[UI - Link] Clicking on the link element: " + element);
        element.before().accept(driver);
        linkService.click(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Performs a double-click action on the specified link element.
     *
     * @param element The {@link LinkUIElement} to be double-clicked.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T doubleClick(final LinkUIElement element) {
        Allure.step("[UI - Link] Double-clicking on the link element: " + element);
        element.before().accept(driver);
        linkService.doubleClick(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Checks if the specified link element is enabled.
     *
     * @param element The {@link LinkUIElement} to check.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T isEnabled(final LinkUIElement element) {
        Allure.step("[UI - Link] Checking if the link element is enabled: " + element);
        element.before().accept(driver);
        boolean enabled = linkService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified link element is enabled.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsEnabled(final LinkUIElement element) {
        return validateIsEnabled(element, true, false);
    }

    /**
     * Validates whether the specified link element is enabled, with an option for soft assertions.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @param soft    {@code true} to perform a soft assertion.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsEnabled(final LinkUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }

    /**
     * Validates that the specified link element is disabled.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsDisabled(final LinkUIElement element) {
        return validateIsEnabled(element, false, false);
    }

    /**
     * Validates whether the specified link element is disabled, with an option for soft assertions.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @param soft    {@code true} to perform a soft assertion.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsDisabled(final LinkUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }

    private T validateIsEnabled(final LinkUIElement element, boolean shouldBeEnabled, boolean soft) {
        Allure.step("[UI - Link] Validating if the link element should be " + (shouldBeEnabled ? "enabled" : "disabled") + ": " + element);
        element.before().accept(driver);
        boolean enabled = linkService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Link is enabled"
                : "Validating Link is disabled";

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
     * Checks if the specified link element is visible.
     *
     * @param element The {@link LinkUIElement} to check.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T isVisible(final LinkUIElement element) {
        Allure.step("[UI - Link] Checking if the link element is visible: " + element);
        element.before().accept(driver);
        boolean visible = linkService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified link element is visible.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsVisible(final LinkUIElement element) {
        return validateIsVisible(element, true, false);
    }

    /**
     * Validates whether the specified link element is visible, with an option for soft assertions.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @param soft    {@code true} to perform a soft assertion.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsVisible(final LinkUIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }

    /**
     * Validates that the specified link element is hidden.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsHidden(final LinkUIElement element) {
        return validateIsVisible(element, false, false);
    }

    /**
     * Validates whether the specified link element is hidden, with an option for soft assertions.
     *
     * @param element The {@link LinkUIElement} to validate.
     * @param soft    {@code true} to perform a soft assertion.
     * @return The current {@link UIServiceFluent} instance for method chaining.
     */
    public T validateIsHidden(final LinkUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }

    private T validateIsVisible(final LinkUIElement element, boolean shouldBeVisible, boolean soft) {
        Allure.step("[UI - Link] Validating if the link element should be " + (shouldBeVisible ? "visible" : "hidden") + ": " + element);
        element.before().accept(driver);
        boolean visible = linkService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Link is visible"
                : "Validating Link is hidden";

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
