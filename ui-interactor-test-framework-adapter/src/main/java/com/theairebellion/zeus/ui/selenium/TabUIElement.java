package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.tab.TabService;

/**
 * Represents a UI element for tab components within the framework.
 * <p>
 * This interface extends {@link UIElement} and provides a default implementation
 * for retrieving the associated component type, ensuring that tab elements
 * can be interacted with in a standardized manner.
 * </p>
 *
 * <p>
 * Tabs allow users to switch between different sections of content in a UI.
 * This interface facilitates automation of tab interactions, such as selecting,
 * validating visibility, and checking if a tab is enabled or selected.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface TabUIElement extends UIElement {

    /**
     * Retrieves the component type associated with this tab element.
     * <p>
     * The default implementation returns {@link TabService#DEFAULT_TYPE},
     * ensuring that the tab element is recognized as part of the tab service.
     * </p>
     *
     * @param <T> The component type.
     * @return The component type associated with this tab element.
     */
    @Override
    default <T extends ComponentType> T componentType() {
        return (T) TabService.DEFAULT_TYPE;
    }

}
