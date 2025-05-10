package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.components.base.ComponentType;

/**
 * Represents an Alert UI element that integrates with the {@link AlertService}.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, allowing interaction with alert components
 * within the UI.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface AlertUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this alert element.
    *
    * <p>The default implementation returns {@link AlertService#DEFAULT_TYPE},
    * ensuring that the alert element is recognized as part of the alert service.
    *
    * @param <T> The component type.
    * @return The component type associated with this alert element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) AlertService.DEFAULT_TYPE;
   }

}
