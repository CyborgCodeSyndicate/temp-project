package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;

/**
 * Represents a UI element for radio button components within the framework.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, ensuring that radio buttons can
 * be interacted with in a standardized manner.
 *
 * <p>Radio buttons are commonly used in forms to allow users to select a single option
 * from a predefined set. This interface enables automation for selecting, validating,
 * and interacting with radio buttons efficiently.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface RadioUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this radio button element.
    *
    * <p>The default implementation returns {@link RadioService#DEFAULT_TYPE},
    * ensuring that the radio button element is recognized as part of the radio button service.
    *
    * @param <T> The component type.
    * @return The component type associated with this radio button element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) RadioService.DEFAULT_TYPE;
   }

}
