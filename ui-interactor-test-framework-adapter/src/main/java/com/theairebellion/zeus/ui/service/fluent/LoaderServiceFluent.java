package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.selenium.LoaderUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * A fluent service class for handling loader UI elements in test automation.
 * <p>
 * Provides methods to check visibility, validate state, and wait for loader elements to disappear or become available.
 * </p>
 *
 * The generic type {@code T} represents the UI service fluent implementation that extends {@link UIServiceFluent},
 * allowing method chaining for seamless interaction.
 *
 * @author Cyborg Code Syndicate
 */
public class LoaderServiceFluent<T extends UIServiceFluent<?>> {

    private final LoaderService loaderService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a new {@code LoaderServiceFluent} instance.
     *
     * @param uiServiceFluent The parent fluent UI service instance.
     * @param storage         The storage instance for storing validation results.
     * @param loaderService   The loader service responsible for interacting with loaders.
     * @param webDriver       The smart web driver used for interactions.
     */
    public LoaderServiceFluent(T uiServiceFluent, Storage storage, LoaderService loaderService,
                               SmartWebDriver webDriver) {
        this.loaderService = loaderService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }

    /**
     * Checks if the specified loader UI element is currently visible.
     *
     * @param element The {@link LoaderUIElement} representing the loader component.
     * @return The fluent UI service instance.
     */
    public T isVisible(final LoaderUIElement element) {
        Allure.step("[UI - Loader] Check if the loader UI element is visible");

        element.before().accept(driver);
        boolean visible = loaderService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }

    /**
     * Validates that the loader UI element is visible.
     *
     * @param element The {@link LoaderUIElement} to validate.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final LoaderUIElement element) {
        Allure.step("[UI - Loader] Validate that the loader UI element is visible");
        return validateIsVisible(element, true, false);
    }

    /**
     * Validates that the loader UI element is visible with an optional soft assertion.
     *
     * @param element The {@link LoaderUIElement} to validate.
     * @param soft    A boolean indicating whether to perform soft validation.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final LoaderUIElement element, boolean soft) {
        Allure.step("[UI - Loader] Validate that the loader UI element is visible with soft validation option");
        return validateIsVisible(element, true, soft);
    }

    /**
     * Validates that the loader UI element is hidden.
     *
     * @param element The {@link LoaderUIElement} to validate.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final LoaderUIElement element) {
        Allure.step("[UI - Loader] Validate that the loader UI element is hidden");
        return validateIsVisible(element, false, false);
    }

    /**
     * Validates that the loader UI element is hidden with an optional soft assertion.
     *
     * @param element The {@link LoaderUIElement} to validate.
     * @param soft    A boolean indicating whether to perform soft validation.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final LoaderUIElement element, boolean soft) {
        Allure.step("[UI - Loader] Validate that the loader UI element is hidden with soft validation option");
        return validateIsVisible(element, false, soft);
    }

    /**
     * Performs validation on whether the loader is visible or hidden.
     *
     * @param element         The {@link LoaderUIElement} to validate.
     * @param shouldBeVisible A boolean indicating the expected visibility state.
     * @param soft            A boolean indicating whether to perform soft validation.
     * @return The fluent UI service instance.
     */
    private T validateIsVisible(final LoaderUIElement element, boolean shouldBeVisible, boolean soft) {
        element.before().accept(driver);
        boolean visible = loaderService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Loader is visible"
                : "Validating Loader is hidden";

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

    /**
     * Waits until the specified loader UI element is visible within the given timeout.
     *
     * @param element      The {@link LoaderUIElement} to wait for.
     * @param secondsShown The maximum time (in seconds) to wait for visibility.
     * @return The fluent UI service instance.
     */
    public T waitToBeShown(final LoaderUIElement element, int secondsShown) {
        Allure.step("[UI - Loader] [UI - Loader] Wait for the loader UI element to be shown for " + secondsShown + " seconds");

        element.before().accept(driver);
        loaderService.waitToBeShown(element.componentType(), element.locator(), secondsShown);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Waits until the specified loader UI element is removed from visibility within the given timeout.
     *
     * @param element        The {@link LoaderUIElement} to wait for.
     * @param secondsRemoved The maximum time (in seconds) to wait for removal.
     * @return The fluent UI service instance.
     */
    public T waitToBeRemoved(final LoaderUIElement element, int secondsRemoved) {
        Allure.step("[UI - Loader] Wait for the loader UI element to be removed for " + secondsRemoved + " seconds");

        element.before().accept(driver);
        loaderService.waitToBeRemoved(element.componentType(), element.locator(), secondsRemoved);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Waits for the loader UI element to become visible and then disappear within the given time limits.
     *
     * @param element        The {@link LoaderUIElement} to wait for.
     * @param secondsShown   The maximum time (in seconds) to wait for visibility.
     * @param secondsRemoved The maximum time (in seconds) to wait for removal.
     * @return The fluent UI service instance.
     */
    public T waitToBeShownAndRemoved(final LoaderUIElement element, int secondsShown, int secondsRemoved) {
        Allure.step("[UI - Loader] Wait for the loader UI element to be shown and then removed within " +
                secondsShown + " and " + secondsRemoved + " seconds respectively");

        element.before().accept(driver);
        loaderService.waitToBeShownAndRemoved(element.componentType(), element.locator(), secondsShown, secondsRemoved);
        element.after().accept(driver);
        return uiServiceFluent;
    }

}
