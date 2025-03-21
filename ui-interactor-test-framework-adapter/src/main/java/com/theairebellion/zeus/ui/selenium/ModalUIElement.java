package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.modal.ModalService;

/**
 * Represents a UI element for modal dialogs within the framework.
 * <p>
 * This interface extends {@link UIElement} and provides a default implementation
 * for retrieving the associated component type, ensuring that modal elements can
 * be interacted with in a standardized manner.
 * </p>
 *
 * <p>
 * Modal dialogs are commonly used for confirmations, warnings, and additional
 * information overlays. This interface enables automation for handling modals
 * efficiently.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface ModalUIElement extends UIElement {

    /**
     * Retrieves the component type associated with this modal element.
     * <p>
     * The default implementation returns {@link ModalService#DEFAULT_TYPE},
     * ensuring that the modal element is recognized as part of the modal service.
     * </p>
     *
     * @param <T> The component type.
     * @return The component type associated with this modal element.
     */
    @Override
    default <T extends ComponentType> T componentType() {
        return (T) ModalService.DEFAULT_TYPE;
    }

}
