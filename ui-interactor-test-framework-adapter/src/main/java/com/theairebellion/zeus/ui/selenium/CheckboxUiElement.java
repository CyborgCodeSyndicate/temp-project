package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;

/**
 * Represents a Checkbox UI element that integrates with the {@link CheckboxService}.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, allowing interaction with checkbox components
 * within the UI.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface CheckboxUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this checkbox element.
    *
    * <p>The default implementation returns {@link CheckboxService#DEFAULT_TYPE},
    * ensuring that the checkbox element is recognized as part of the checkbox service.
    *
    * @param <T> The component type.
    * @return The component type associated with this checkbox element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) CheckboxService.DEFAULT_TYPE;
   }

}
