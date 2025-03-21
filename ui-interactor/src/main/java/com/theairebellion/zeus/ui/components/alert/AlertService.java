package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides service-level methods for retrieving and checking the visibility
 * of alert components, either by a user-specified type or a default configuration.
 * <p>
 * Each method resolves the appropriate {@link AlertComponentType}—for instance,
 * corresponding to a particular UI framework—and delegates to the correct
 * {@link Alert} implementation under the hood.
 * </p>
 *
 * <p>
 * The default alert type can be overridden by specifying an {@link AlertComponentType} parameter,
 * allowing different UI implementations (e.g., Bootstrap, Material Design, etc.).
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface AlertService {

    AlertComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Retrieves the value of an alert within the specified container, using the default alert component type.
     *
     * @param container The SmartWebElement container that contains the alert.
     * @return The value of the alert as a String.
     */
    default String getValue(SmartWebElement container) {
        return getValue(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the value of an alert within the specified container, using the given alert component type.
     *
     * @param componentType The specific alert component type.
     * @param container     The SmartWebElement container that contains the alert.
     * @return The value of the alert as a String.
     */
    String getValue(AlertComponentType componentType, SmartWebElement container);

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
     * @param container The SmartWebElement container that contains the alert.
     * @return true if the alert is visible, false otherwise.
     */
    default boolean isVisible(SmartWebElement container) {
        return isVisible(DEFAULT_TYPE, container);
    }

    /**
     * Checks if an alert within the specified container is visible, using the given alert component type.
     *
     * @param componentType The specific alert component type.
     * @param container     The SmartWebElement container that contains the alert.
     * @return true if the alert is visible, false otherwise.
     */
    boolean isVisible(AlertComponentType componentType, SmartWebElement container);

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
                    getUiConfig().alertDefaultType(),
                    getUiConfig().projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}
