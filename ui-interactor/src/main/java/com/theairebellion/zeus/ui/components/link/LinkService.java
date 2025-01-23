package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Interface defining operations for interacting with link elements within a web interface.
 */
public interface LinkService extends ButtonService {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    LinkComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Double-clicks a link with the specified text inside a container, using the default link component type.
     *
     * @param container The WebElement container that contains the link.
     * @param linkText  The text of the link to click.
     */
    default void doubleClick(WebElement container, String linkText) {
        doubleClick(DEFAULT_TYPE, container, linkText);
    }

    /**
     * Double-clicks a link with the specified text inside a container, using the given link component type.
     *
     * @param componentType The specific link component type.
     * @param container     The WebElement container that contains the link.
     * @param linkText      The text of the link to click.
     */
    void doubleClick(LinkComponentType componentType, WebElement container, String linkText);

    /**
     * Double-clicks a link inside a container, using the default link component type.
     *
     * @param container The WebElement container that contains the link.
     */
    default void doubleClick(WebElement container) {
        doubleClick(DEFAULT_TYPE, container);
    }

    /**
     * Double-clicks a link inside a container, using the given link component type.
     *
     * @param componentType The specific link component type.
     * @param container     The WebElement container that contains the link.
     */
    void doubleClick(LinkComponentType componentType, WebElement container);

    /**
     * Double-clicks a link with the specified text, using the default link component type.
     *
     * @param linkText The text of the link to click.
     */
    default void doubleClick(String linkText) {
        doubleClick(DEFAULT_TYPE, linkText);
    }

    /**
     * Double-clicks a link with the specified text, using the given link component type.
     *
     * @param componentType The specific link component type.
     * @param linkText      The text of the link to click.
     */
    void doubleClick(LinkComponentType componentType, String linkText);

    /**
     * Double-clicks a link located by the specified locator, using the default link component type.
     *
     * @param linkLocator The By locator for the link to click.
     */
    default void doubleClick(By linkLocator) {
        doubleClick(DEFAULT_TYPE, linkLocator);
    }

    /**
     * Double-clicks a link located by the specified locator, using the given link component type.
     *
     * @param componentType The specific link component type.
     * @param linkLocator   The By locator for the link to click.
     */
    void doubleClick(LinkComponentType componentType, By linkLocator);


    /**
     * Retrieves the default link component type from the configuration.
     *
     * @return The default LinkComponentType.
     */
    private static LinkComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(LinkComponentType.class,
                uiConfig.linkDefaultType(),
                uiConfig.projectPackage());
    }
}