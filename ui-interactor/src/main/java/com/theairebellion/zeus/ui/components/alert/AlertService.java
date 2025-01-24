package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Interface defining operations for interacting with alert components in a web interface.
 */
public interface AlertService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    AlertComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Retrieves the value of an alert within the specified container, using the default alert component type.
     *
     * @param container The WebElement container that contains the alert.
     * @return The value of the alert as a String.
     */
    default String getValue(WebElement container) {
        return getValue(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the value of an alert within the specified container, using the given alert component type.
     *
     * @param componentType The specific alert component type.
     * @param container     The WebElement container that contains the alert.
     * @return The value of the alert as a String.
     */
    String getValue(AlertComponentType componentType, WebElement container);

    /**
     * Retrieves the value of an alert located by the specified locator, using the default alert component type.
     *
     * @param containerLocator The locator for the container that contains the alert.
     * @return The value of the alert as a String.
     */
    default String getValue(By containerLocator) {
        return getValue(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves the value of an alert located by the specified locator, using the given alert component type.
     *
     * @param componentType    The specific alert component type.
     * @param containerLocator The locator for the container that contains the alert.
     * @return The value of the alert as a String.
     */
    String getValue(AlertComponentType componentType, By containerLocator);

    /**
     * Checks if an alert within the specified container is visible, using the default alert component type.
     *
     * @param container The WebElement container that contains the alert.
     * @return true if the alert is visible, false otherwise.
     */
    default boolean isVisible(WebElement container) {
        return isVisible(DEFAULT_TYPE, container);
    }

    /**
     * Checks if an alert within the specified container is visible, using the given alert component type.
     *
     * @param componentType The specific alert component type.
     * @param container     The WebElement container that contains the alert.
     * @return true if the alert is visible, false otherwise.
     */
    boolean isVisible(AlertComponentType componentType, WebElement container);

    /**
     * Checks if an alert located by the specified locator is visible, using the default alert component type.
     *
     * @param containerLocator The locator for the container that contains the alert.
     * @return true if the alert is visible, false otherwise.
     */
    default boolean isVisible(By containerLocator) {
        return isVisible(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Checks if an alert located by the specified locator is visible, using the given alert component type.
     *
     * @param componentType    The specific alert component type.
     * @param containerLocator The locator for the container that contains the alert.
     * @return true if the alert is visible, false otherwise.
     */
    boolean isVisible(AlertComponentType componentType, By containerLocator);

    /**
     * Retrieves the default alert component type from the configuration.
     *
     * @return The default AlertComponentType.
     */
    private static AlertComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(AlertComponentType.class,
                    uiConfig.alertDefaultType(),
                    uiConfig.projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}
