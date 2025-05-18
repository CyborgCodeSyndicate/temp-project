package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;

/**
 * Represents a list UI element that integrates with the {@link ItemListService}.
 *
 * <p>This interface extends {@link UiElement} and provides a default implementation
 * for retrieving the associated component type, ensuring that list elements can
 * be interacted with as part of the UI automation framework.
 *
 * <p>This element supports actions such as selecting, deselecting, and verifying
 * visibility and state of list items.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface ListUiElement extends UiElement {

   /**
    * Retrieves the component type associated with this list element.
    *
    * <p>The default implementation returns {@link ItemListService#DEFAULT_TYPE},
    * ensuring that the list element is recognized as part of the item list service.
    *
    * @param <T> The component type.
    * @return The component type associated with this list element.
    */
   @Override
   default <T extends ComponentType> T componentType() {
      return (T) ItemListService.DEFAULT_TYPE;
   }

}
