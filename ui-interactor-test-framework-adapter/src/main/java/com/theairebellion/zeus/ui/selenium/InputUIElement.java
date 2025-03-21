package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;

/**
 * Represents an Input UI element that integrates with the {@link InputService}.
 * <p>
 * This interface extends {@link UIElement} and provides a default implementation
 * for retrieving the associated component type, allowing interaction with input fields
 * within the UI.
 * </p>
 *
 * <p>
 * This element can be used for performing actions such as inserting text, clearing
 * input fields, and validating their values.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface InputUIElement extends UIElement {

    /**
     * Retrieves the component type associated with this input element.
     * <p>
     * The default implementation returns {@link InputService#DEFAULT_TYPE},
     * ensuring that the input element is recognized as part of the input service.
     * </p>
     *
     * @param <T> The component type.
     * @return The component type associated with this input element.
     */
    @Override
    default <T extends ComponentType> T componentType() {
        return (T) InputService.DEFAULT_TYPE;
    }

}
