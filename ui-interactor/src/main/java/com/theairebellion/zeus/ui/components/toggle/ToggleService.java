package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface ToggleService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    ToggleComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Activates the toggle with the specified text inside a container, using the default toggle component type.
     *
     * @param container  The WebElement container that contains the toggle.
     * @param toggleText The text of the toggle to activate.
     */
    default void activate(WebElement container, String toggleText) {
        activate(DEFAULT_TYPE, container, toggleText);
    }

    /**
     * Activates the toggle with the specified text inside a container, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param container     The WebElement container that contains the toggle.
     * @param toggleText    The text of the toggle to activate.
     */
    void activate(ToggleComponentType componentType, WebElement container, String toggleText);

    /**
     * Activates the toggle with the specified text, using the default toggle component type.
     *
     * @param toggleText The text of the toggle to activate.
     */
    default void activate(String toggleText) {
        activate(DEFAULT_TYPE, toggleText);
    }

    /**
     * Activates the toggle with the specified text, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleText    The text of the toggle to activate.
     */
    void activate(ToggleComponentType componentType, String toggleText);

    /**
     * Activates the toggle identified by the specified locator, using the default toggle component type.
     *
     * @param toggleLocator The locator for the toggle to activate.
     */
    default void activate(By toggleLocator) {
        activate(DEFAULT_TYPE, toggleLocator);
    }

    /**
     * Activates the toggle identified by the specified locator, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleLocator The locator for the toggle to activate.
     */
    void activate(ToggleComponentType componentType, By toggleLocator);

    /**
     * Deactivates the toggle with the specified text inside a container, using the default toggle component type.
     *
     * @param container  The WebElement container that contains the toggle.
     * @param toggleText The text of the toggle to deactivate.
     */
    default void deactivate(WebElement container, String toggleText) {
        deactivate(DEFAULT_TYPE, container, toggleText);
    }

    /**
     * Deactivates the toggle with the specified text inside a container, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param container     The WebElement container that contains the toggle.
     * @param toggleText    The text of the toggle to deactivate.
     */
    void deactivate(ToggleComponentType componentType, WebElement container, String toggleText);

    /**
     * Deactivates the toggle with the specified text, using the default toggle component type.
     *
     * @param toggleText The text of the toggle to deactivate.
     */
    default void deactivate(String toggleText) {
        deactivate(DEFAULT_TYPE, toggleText);
    }

    /**
     * Deactivates the toggle with the specified text, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleText    The text of the toggle to deactivate.
     */
    void deactivate(ToggleComponentType componentType, String toggleText);

    /**
     * Deactivates the toggle identified by the specified locator, using the default toggle component type.
     *
     * @param toggleLocator The locator for the toggle to deactivate.
     */
    default void deactivate(By toggleLocator) {
        deactivate(DEFAULT_TYPE, toggleLocator);
    }

    /**
     * Deactivates the toggle identified by the specified locator, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleLocator The locator for the toggle to deactivate.
     */
    void deactivate(ToggleComponentType componentType, By toggleLocator);

    /**
     * Checks if the toggle with the specified text inside a container is enabled, using the default toggle component type.
     *
     * @param container  The WebElement container that contains the toggle.
     * @param toggleText The text of the toggle to check.
     * @return true if the toggle is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container, String toggleText) {
        return isEnabled(DEFAULT_TYPE, container, toggleText);
    }

    /**
     * Checks if the toggle with the specified text inside a container is enabled, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param container     The WebElement container that contains the toggle.
     * @param toggleText    The text of the toggle to check.
     * @return true if the toggle is enabled, false otherwise.
     */
    boolean isEnabled(ToggleComponentType componentType, WebElement container, String toggleText);

    /**
     * Checks if the toggle with the specified text is enabled, using the default toggle component type.
     *
     * @param toggleText The text of the toggle to check.
     * @return true if the toggle is enabled, false otherwise.
     */
    default boolean isEnabled(String toggleText) {
        return isEnabled(DEFAULT_TYPE, toggleText);
    }

    /**
     * Checks if the toggle with the specified text is enabled, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleText    The text of the toggle to check.
     * @return true if the toggle is enabled, false otherwise.
     */
    boolean isEnabled(ToggleComponentType componentType, String toggleText);

    /**
     * Checks if the toggle identified by the specified locator is enabled, using the default toggle component type.
     *
     * @param toggleLocator The locator for the toggle to check.
     * @return true if the toggle is enabled, false otherwise.
     */
    default boolean isEnabled(By toggleLocator) {
        return isEnabled(DEFAULT_TYPE, toggleLocator);
    }

    /**
     * Checks if the toggle identified by the specified locator is enabled, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleLocator The locator for the toggle to check.
     * @return true if the toggle is enabled, false otherwise.
     */
    boolean isEnabled(ToggleComponentType componentType, By toggleLocator);

    /**
     * Checks if the toggle with the specified text inside a container is activated, using the default toggle component type.
     *
     * @param container  The WebElement container that contains the toggle.
     * @param toggleText The text of the toggle to check.
     * @return true if the toggle is activated, false otherwise.
     */
    default boolean isActivated(WebElement container, String toggleText) {
        return isActivated(DEFAULT_TYPE, container, toggleText);
    }

    /**
     * Checks if the toggle with the specified text inside a container is activated, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param container     The WebElement container that contains the toggle.
     * @param toggleText    The text of the toggle to check.
     * @return true if the toggle is activated, false otherwise.
     */
    boolean isActivated(ToggleComponentType componentType, WebElement container, String toggleText);

    /**
     * Checks if the toggle with the specified text is activated, using the default toggle component type.
     *
     * @param toggleText The text of the toggle to check.
     * @return true if the toggle is activated, false otherwise.
     */
    default boolean isActivated(String toggleText) {
        return isActivated(DEFAULT_TYPE, toggleText);
    }

    /**
     * Checks if the toggle with the specified text is activated, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleText    The text of the toggle to check.
     * @return true if the toggle is activated, false otherwise.
     */
    boolean isActivated(ToggleComponentType componentType, String toggleText);

    /**
     * Checks if the toggle identified by the specified locator is activated, using the default toggle component type.
     *
     * @param toggleLocator The locator for the toggle to check.
     * @return true if the toggle is activated, false otherwise.
     */
    default boolean isActivated(By toggleLocator) {
        return isActivated(DEFAULT_TYPE, toggleLocator);
    }

    /**
     * Checks if the toggle identified by the specified locator is activated, using the given toggle component type.
     *
     * @param componentType The specific toggle component type.
     * @param toggleLocator The locator for the toggle to check.
     * @return true if the toggle is activated, false otherwise.
     */
    boolean isActivated(ToggleComponentType componentType, By toggleLocator);

    /**
     * Retrieves the default toggle component type from the configuration.
     *
     * @return The default ToggleComponentType.
     */
    private static ToggleComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(ToggleComponentType.class,
                uiConfig.toggleDefaultType(),
                uiConfig.projectPackage());
    }

}
