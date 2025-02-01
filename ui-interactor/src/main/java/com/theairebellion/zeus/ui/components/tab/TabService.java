package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Interface defining operations for interacting with tab elements within a web interface.
 */
public interface TabService extends ButtonService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    TabComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Checks if a tab with the specified text inside a container is selected, using the default tab component type.
     *
     * @param container The WebElement container that contains the tab.
     * @param tabText   The text of the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    default boolean isSelected(WebElement container, String tabText) {
        return isSelected(DEFAULT_TYPE, container, tabText);
    }

    /**
     * Checks if a tab with the specified text inside a container is selected, using the given tab component type.
     *
     * @param componentType The specific tab component type.
     * @param container     The WebElement container that contains the tab.
     * @param tabText       The text of the tab to select.
     * @return true if the tab is selected, false otherwise.
     */
    boolean isSelected(TabComponentType componentType, WebElement container, String tabText);

    /**
     * Checks if a tab inside a container is selected, using the default tab component type.
     *
     * @param container The WebElement container that contains the tab.
     * @return true if the tab is selected, false otherwise.
     */
    default boolean isSelected(WebElement container) {
        return isSelected(DEFAULT_TYPE, container);
    }

    /**
     * Checks if a tab inside a container is selected, using the given tab component type.
     *
     * @param componentType The specific tab component type.
     * @param container     The WebElement container that contains the tab.
     * @return true if the tab is selected, false otherwise.
     */
    boolean isSelected(TabComponentType componentType, WebElement container);

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
                    uiConfig.tabDefaultType(),
                    uiConfig.projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}