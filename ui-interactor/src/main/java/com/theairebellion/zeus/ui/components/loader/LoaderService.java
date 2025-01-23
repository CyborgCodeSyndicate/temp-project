package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

/**
 * Interface defining operations for interacting with loader elements within a web interface using Selenium.
 */
public interface LoaderService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    LoaderComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Checks if a loader is present within the specified container using the default loader component type.
     *
     * @param container The WebElement container to check for the presence of the loader.
     * @return true if the loader is present, false otherwise.
     */
    default boolean isPresent(WebElement container) {
        return isPresent(DEFAULT_TYPE, container);
    }

    /**
     * Checks if a loader is present within the specified container using the given loader component type.
     *
     * @param componentType The specific loader component type.
     * @param container     The WebElement container to check for the presence of the loader.
     * @return true if the loader is present, false otherwise.
     */
    boolean isPresent(LoaderComponentType componentType, WebElement container);

    /**
     * Checks if a loader is present using the specified locator and the default loader component type.
     *
     * @param loaderLocator The By locator to check for the presence of the loader.
     * @return true if the loader is present, false otherwise.
     */
    default boolean isPresent(By loaderLocator) {
        return isPresent(DEFAULT_TYPE, loaderLocator);
    }

    /**
     * Checks if a loader is present using the specified locator and loader component type.
     *
     * @param componentType The specific loader component type.
     * @param loaderLocator The By locator to check for the presence of the loader.
     * @return true if the loader is present, false otherwise.
     */
    boolean isPresent(LoaderComponentType componentType, By loaderLocator);

    /**
     * Waits for the loader to be shown within the specified container for the given duration, using the default loader component type.
     *
     * @param container    The WebElement container where the loader is expected to appear.
     * @param secondsShown The maximum time to wait for the loader to be shown, in seconds.
     */
    default void waitToBeShown(WebElement container, int secondsShown) {
        waitToBeShown(DEFAULT_TYPE, container, secondsShown);
    }

    /**
     * Waits for the loader to be shown within the specified container for the given duration, using the given loader component type.
     *
     * @param componentType The specific loader component type.
     * @param container     The WebElement container where the loader is expected to appear.
     * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
     */
    void waitToBeShown(LoaderComponentType componentType, WebElement container, int secondsShown);

    /**
     * Waits for the loader to be shown for the given duration, using the default loader component type.
     *
     * @param secondsShown The maximum time to wait for the loader to be shown, in seconds.
     */
    default void waitToBeShown(int secondsShown) {
        waitToBeShown(DEFAULT_TYPE, secondsShown);
    }

    /**
     * Waits for the loader to be shown for the given duration, using the given loader component type.
     *
     * @param componentType The specific loader component type.
     * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
     */
    void waitToBeShown(LoaderComponentType componentType, int secondsShown);

    /**
     * Waits for the loader to be shown using the specified locator for the given duration, using the default loader component type.
     *
     * @param loaderLocator The By locator for the loader element.
     * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
     */
    default void waitToBeShown(By loaderLocator, int secondsShown) {
        waitToBeShown(DEFAULT_TYPE, loaderLocator, secondsShown);
    }

    /**
     * Waits for the loader to be shown using the specified locator for the given duration, using the given loader component type.
     *
     * @param componentType The specific loader component type.
     * @param loaderLocator The By locator for the loader element.
     * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
     */
    void waitToBeShown(LoaderComponentType componentType, By loaderLocator, int secondsShown);

    /**
     * Waits for the loader to be removed from the specified container within the given duration, using the default loader component type.
     *
     * @param container      The WebElement container where the loader is expected to be removed.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    default void waitToBeRemoved(WebElement container, int secondsRemoved) {
        waitToBeRemoved(DEFAULT_TYPE, container, secondsRemoved);
    }

    /**
     * Waits for the loader to be removed from the specified container within the given duration, using the given loader component type.
     *
     * @param componentType  The specific loader component type.
     * @param container      The WebElement container where the loader is expected to be removed.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    void waitToBeRemoved(LoaderComponentType componentType, WebElement container, int secondsRemoved);

    /**
     * Waits for the loader to be removed within the given duration, using the default loader component type.
     *
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    default void waitToBeRemoved(int secondsRemoved) {
        waitToBeRemoved(DEFAULT_TYPE, secondsRemoved);
    }

    /**
     * Waits for the loader to be removed within the given duration, using the given loader component type.
     *
     * @param componentType  The specific loader component type.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    void waitToBeRemoved(LoaderComponentType componentType, int secondsRemoved);

    /**
     * Waits for the loader to be removed using the specified locator within the given duration, using the default loader component type.
     *
     * @param loaderLocator  The By locator for the loader element.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    default void waitToBeRemoved(By loaderLocator, int secondsRemoved) {
        waitToBeRemoved(DEFAULT_TYPE, loaderLocator, secondsRemoved);
    }

    /**
     * Waits for the loader to be removed using the specified locator within the given duration, using the given loader component type.
     *
     * @param componentType  The specific loader component type.
     * @param loaderLocator  The By locator for the loader element.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    void waitToBeRemoved(LoaderComponentType componentType, By loaderLocator, int secondsRemoved);

    /**
     * Waits for the loader to be shown and then removed from the specified container within the given durations.
     *
     * @param componentType  The specific loader component type.
     * @param container      The WebElement container.
     * @param secondsShown   The maximum time to wait for the loader to be shown, in seconds.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    default void waitToBeShownAndRemoved(LoaderComponentType componentType, WebElement container, int secondsShown, int secondsRemoved) {
        waitToBeShown(componentType, container, secondsShown);
        waitToBeRemoved(componentType, container, secondsRemoved);
    }

    /**
     * Waits for the loader to be shown and then removed within the given durations, using the given loader component type.
     *
     * @param componentType  The specific loader component type.
     * @param secondsShown   The maximum time to wait for the loader to be shown, in seconds.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    default void waitToBeShownAndRemoved(LoaderComponentType componentType, int secondsShown, int secondsRemoved) {
        waitToBeShown(componentType, secondsShown);
        waitToBeRemoved(componentType, secondsRemoved);
    }

    /**
     * Waits for the loader to be shown and then removed using the specified locator within the given durations, using the given loader component type.
     *
     * @param componentType  The specific loader component type.
     * @param loaderLocator  The By locator for the loader element.
     * @param secondsShown   The maximum time to wait for the loader to be shown, in seconds.
     * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
     */
    default void waitToBeShownAndRemoved(LoaderComponentType componentType, By loaderLocator, int secondsShown, int secondsRemoved) {
        waitToBeShown(componentType, loaderLocator, secondsShown);
        waitToBeRemoved(componentType, loaderLocator, secondsRemoved);
    }

    /**
     * Retrieves the default loader component type from the configuration.
     *
     * @return The default LoaderComponentType.
     */
    private static LoaderComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(LoaderComponentType.class,
                uiConfig.loaderDefaultType(),
                uiConfig.projectPackage());
    }
}
