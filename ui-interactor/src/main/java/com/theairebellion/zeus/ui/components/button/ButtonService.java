package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Interface defining operations for interacting with button elements within a web interface.
 */
public interface ButtonService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    ButtonComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Clicks a button with the specified text inside a container, using the default button component type.
     *
     * @param container  The WebElement container that contains the button.
     * @param buttonText The text of the button to click.
     */
    default void click(WebElement container, String buttonText) {
        click(DEFAULT_TYPE, container, buttonText);
    }

    /**
     * Clicks a button with the specified text inside a container, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param container     The WebElement container that contains the button.
     * @param buttonText    The text of the button to click.
     */
    <T extends ButtonComponentType> void click(T componentType, WebElement container, String buttonText);

    /**
     * Clicks a button inside a container, using the default button component type.
     *
     * @param container The WebElement container that contains the button.
     */
    default void click(WebElement container) {
        click(DEFAULT_TYPE, container);
    }

    /**
     * Clicks a button inside a container, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param container     The WebElement container that contains the button.
     */
    <T extends ButtonComponentType> void click(T componentType, WebElement container);

    /**
     * Clicks a button with the specified text, using the default button component type.
     *
     * @param buttonText The text of the button to click.
     */
    default void click(String buttonText) {
        click(DEFAULT_TYPE, buttonText);
    }

    /**
     * Clicks a button with the specified text, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param buttonText    The text of the button to click.
     */
    <T extends ButtonComponentType> void click(T componentType, String buttonText);

    /**
     * Clicks a button located by the specified locator, using the default button component type.
     *
     * @param buttonLocator The By locator for the button to click.
     */
    default void click(By buttonLocator) {
        click(DEFAULT_TYPE, buttonLocator);
    }

    /**
     * Clicks a button located by the specified locator, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param buttonLocator The By locator for the button to click.
     */
    <T extends ButtonComponentType> void click(T componentType, By buttonLocator);

    /**
     * Checks if a button with the specified text inside a container is enabled, using the default button component type.
     *
     * @param container  The WebElement container that contains the button.
     * @param buttonText The text of the button to check.
     * @return true if the button is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container, String buttonText) {
        return isEnabled(DEFAULT_TYPE, container, buttonText);
    }

    /**
     * Checks if a button with the specified text inside a container is enabled, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param container     The WebElement container that contains the button.
     * @param buttonText    The text of the button to check.
     * @return true if the button is enabled, false otherwise.
     */
    <T extends ButtonComponentType> boolean isEnabled(T componentType, WebElement container, String buttonText);

    /**
     * Checks if a button inside a container is enabled, using the default button component type.
     *
     * @param container The WebElement container that contains the button.
     * @return true if the button is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container) {
        return isEnabled(DEFAULT_TYPE, container);
    }

    /**
     * Checks if a button inside a container is enabled, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param container     The WebElement container that contains the button.
     * @return true if the button is enabled, false otherwise.
     */
    <T extends ButtonComponentType> boolean isEnabled(T componentType, WebElement container);

    /**
     * Checks if a button with the specified text is enabled, using the default button component type.
     *
     * @param buttonText The text of the button to check.
     * @return true if the button is enabled, false otherwise.
     */
    default boolean isEnabled(String buttonText) {
        return isEnabled(DEFAULT_TYPE, buttonText);
    }

    /**
     * Checks if a button with the specified text is enabled, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param buttonText    The text of the button to check.
     * @return true if the button is enabled, false otherwise.
     */
    <T extends ButtonComponentType> boolean isEnabled(T componentType, String buttonText);

    /**
     * Checks if a button located by the specified locator is enabled, using the default button component type.
     *
     * @param buttonLocator The By locator for the button to check.
     * @return true if the button is enabled, false otherwise.
     */
    default boolean isEnabled(By buttonLocator) {
        return isEnabled(DEFAULT_TYPE, buttonLocator);
    }

    /**
     * Checks if a button located by the specified locator is enabled, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param buttonLocator The By locator for the button to check.
     * @return true if the button is enabled, false otherwise.
     */
    <T extends ButtonComponentType> boolean isEnabled(T componentType, By buttonLocator);

    /**
     * Checks if a button with the specified text inside a container is visible, using the default button component type.
     *
     * @param container  The WebElement container that contains the button.
     * @param buttonText The text of the button to check.
     * @return true if the button is visible, false otherwise.
     */
    default boolean isVisible(WebElement container, String buttonText) {
        return isVisible(DEFAULT_TYPE, container, buttonText);
    }

    /**
     * Checks if a button with the specified text inside a container is visible, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param container     The WebElement container that contains the button.
     * @param buttonText    The text of the button to check.
     * @return true if the button is visible, false otherwise.
     */
    <T extends ButtonComponentType> boolean isVisible(T componentType, WebElement container, String buttonText);

    /**
     * Checks if a button inside a container is visible, using the default button component type.
     *
     * @param container The WebElement container that contains the button.
     * @return true if the button is visible, false otherwise.
     */
    default boolean isVisible(WebElement container) {
        return isVisible(DEFAULT_TYPE, container);
    }

    /**
     * Checks if a button inside a container is visible, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param container     The WebElement container that contains the button.
     * @return true if the button is visible, false otherwise.
     */
    <T extends ButtonComponentType> boolean isVisible(T componentType, WebElement container);

    /**
     * Checks if a button with the specified text is visible, using the default button component type.
     *
     * @param buttonText The text of the button to check.
     * @return true if the button is visible, false otherwise.
     */
    default boolean isVisible(String buttonText) {
        return isVisible(DEFAULT_TYPE, buttonText);
    }

    /**
     * Checks if a button with the specified text is visible, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param buttonText    The text of the button to check.
     * @return true if the button is visible, false otherwise.
     */
    <T extends ButtonComponentType> boolean isVisible(T componentType, String buttonText);

    /**
     * Checks if a button located by the specified locator is visible, using the default button component type.
     *
     * @param buttonLocator The By locator for the button to check.
     * @return true if the button is visible, false otherwise.
     */
    default boolean isVisible(By buttonLocator) {
        return isVisible(DEFAULT_TYPE, buttonLocator);
    }

    /**
     * Checks if a button located by the specified locator is visible, using the given button component type.
     *
     * @param componentType The specific button component type.
     * @param buttonLocator The By locator for the button to check.
     * @return true if the button is visible, false otherwise.
     */
    <T extends ButtonComponentType> boolean isVisible(T componentType, By buttonLocator);

    /**
     * Retrieves the default button component type from the configuration.
     *
     * @return The default ButtonComponentType.
     */
    private static ButtonComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(ButtonComponentType.class,
                    uiConfig.buttonDefaultType(),
                    uiConfig.projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}