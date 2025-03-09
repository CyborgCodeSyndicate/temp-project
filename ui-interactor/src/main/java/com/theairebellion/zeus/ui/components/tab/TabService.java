package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides an interface for interacting with tab elements in a UI automation framework,
 * extending the capabilities of {@link ButtonService}. By referencing a {@link TabComponentType}
 * and the {@link #DEFAULT_TYPE}, implementations can handle tab-based navigation and
 * state checks consistently across various UI designs.
 *
 * <p>Classes implementing this interface generally leverage Selenium-based strategies to
 * identify tabs either by container (using {@link SmartWebElement}) or locator (using {@link By}),
 * ensuring a flexible approach that accommodates multiple application layouts.</p>
 *
 * <p>No usage examples are included here.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface TabService extends ButtonService {

    TabComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Checks if a tab with the specified text inside a container is selected, using the default tab component type.
     *
     * @param container The SmartWebElement container that contains the tab.
     * @param tabText   The text of the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    default boolean isSelected(SmartWebElement container, String tabText) {
        return isSelected(DEFAULT_TYPE, container, tabText);
    }

    /**
     * Checks if a tab with the specified text inside a container is selected, using the given tab component type.
     *
     * @param componentType The specific tab component type.
     * @param container     The SmartWebElement container that contains the tab.
     * @param tabText       The text of the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    boolean isSelected(TabComponentType componentType, SmartWebElement container, String tabText);

    /**
     * Checks if a tab inside a container is selected, using the default tab component type.
     *
     * @param container The SmartWebElement container that contains the tab.
     * @return true if the tab is selected, false otherwise.
     */
    default boolean isSelected(SmartWebElement container) {
        return isSelected(DEFAULT_TYPE, container);
    }

    /**
     * Checks if a tab inside a container is selected, using the given tab component type.
     *
     * @param componentType The specific tab component type.
     * @param container     The SmartWebElement container that contains the tab.
     * @return true if the tab is selected, false otherwise.
     */
    boolean isSelected(TabComponentType componentType, SmartWebElement container);

    /**
     * Checks if a tab with the specified text is selected, using the default tab component type.
     *
     * @param tabText The text of the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    default boolean isSelected(String tabText) {
        return isSelected(DEFAULT_TYPE, tabText);
    }

    /**
     * Checks if a tab with the specified text is selected, using the given tab component type.
     *
     * @param componentType The specific tab component type.
     * @param tabText       The text of the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    boolean isSelected(TabComponentType componentType, String tabText);

    /**
     * Checks if a tab located by the specified locator is selected, using the default tab component type.
     *
     * @param tabLocator The By locator for the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    default boolean isSelected(By tabLocator) {
        return isSelected(DEFAULT_TYPE, tabLocator);
    }

    /**
     * Checks if a tab located by the specified locator is selected, using the given tab component type.
     *
     * @param componentType The specific tab component type.
     * @param tabLocator    The By locator for the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    boolean isSelected(TabComponentType componentType, By tabLocator);


    /**
     * Retrieves the default tab component type from the configuration.
     *
     * @return The default TabComponentType.
     */
    private static TabComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(TabComponentType.class,
                    getUiConfig().tabDefaultType(),
                    getUiConfig().projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}