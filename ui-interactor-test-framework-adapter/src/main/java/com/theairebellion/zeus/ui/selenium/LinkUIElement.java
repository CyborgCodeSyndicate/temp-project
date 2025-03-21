package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.link.LinkService;

/**
 * Represents a link UI element that integrates with the {@link LinkService}.
 * <p>
 * This interface extends {@link UIElement} and provides a default implementation
 * for retrieving the associated component type, ensuring that link elements can
 * be interacted with as part of the UI automation framework.
 * </p>
 *
 * <p>
 * This element allows performing actions such as clicking, verifying visibility,
 * and checking if the link is enabled.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface LinkUIElement extends UIElement {

    /**
     * Retrieves the component type associated with this link element.
     * <p>
     * The default implementation returns {@link LinkService#DEFAULT_TYPE},
     * ensuring that the link element is recognized as part of the link service.
     * </p>
     *
     * @param <T> The component type.
     * @return The component type associated with this link element.
     */
    @Override
    default <T extends ComponentType> T componentType() {
        return (T) LinkService.DEFAULT_TYPE;
    }

}
