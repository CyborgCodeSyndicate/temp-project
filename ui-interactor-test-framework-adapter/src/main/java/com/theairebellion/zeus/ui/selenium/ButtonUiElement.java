package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonService;

/**
 * Represents a Button UI element that integrates with the {@link ButtonService}.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, allowing interaction with button components
 * within the UI.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface ButtonUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this button element.
    *
    * <p>The default implementation returns {@link ButtonService#DEFAULT_TYPE},
    * ensuring that the button element is recognized as part of the button service.
    *
    * @param <T> The component type.
    * @return The component type associated with this button element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) ButtonService.DEFAULT_TYPE;
   }

}
