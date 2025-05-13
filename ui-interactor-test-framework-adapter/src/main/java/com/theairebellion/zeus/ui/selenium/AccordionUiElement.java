package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.accordion.AccordionService;
import com.theairebellion.zeus.ui.components.base.ComponentType;

/**
 * Represents an Accordion UI element that integrates with the {@link AccordionService}.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, which is used for interacting with
 * accordion components within the UI.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface AccordionUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this accordion element.
    *
    * <p>The default implementation returns {@link AccordionService#DEFAULT_TYPE},
    * ensuring that the accordion element is recognized as part of the
    * accordion service.
    *
    * @param <T> The component type.
    * @return The component type associated with this accordion element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) AccordionService.DEFAULT_TYPE;
   }

}
