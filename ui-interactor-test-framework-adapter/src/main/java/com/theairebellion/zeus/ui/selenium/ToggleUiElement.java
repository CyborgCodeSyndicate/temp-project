package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;

/**
 * Represents a UI element for toggle switch components within the framework.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, ensuring that toggle elements
 * can be interacted with in a standardized manner.
 *
 * <p>Toggle switches allow users to enable or disable specific settings in the UI.
 * This interface facilitates automation of toggle interactions, such as activation,
 * deactivation, and state validation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface ToggleUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this toggle element.
    *
    * <p>The default implementation returns {@link ToggleService#DEFAULT_TYPE},
    * ensuring that the toggle element is recognized as part of the toggle service.
    *
    * @param <T> The component type.
    * @return The component type associated with this toggle element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) ToggleService.DEFAULT_TYPE;
   }

}
