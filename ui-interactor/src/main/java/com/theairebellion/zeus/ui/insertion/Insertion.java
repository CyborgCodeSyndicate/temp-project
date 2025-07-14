package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.openqa.selenium.By;

/**
 * Defines a contract for inserting data into UI components.
 *
 * <p>This interface abstracts the behavior of inserting values into various UI elements,
 * ensuring that all components implement a consistent insertion mechanism.
 *
 * <p>Implementing classes are responsible for handling specific UI elements, applying
 * the correct interaction method (e.g., setting text fields, selecting checkboxes, etc.).
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface Insertion {

   /**
    * Performs an insertion operation on a specified UI component.
    *
    * <p>This method allows dynamic interaction with different UI elements,
    * such as entering text in input fields, selecting checkboxes, or choosing dropdown values.
    *
    * @param componentType The type of UI component that should receive the insertion.
    * @param locator       The {@link By} locator identifying the UI element.
    * @param values        The values to be inserted into the component. The interpretation of these
    *                      values depends on the component type.
    */
   void insertion(ComponentType componentType, By locator, Object... values);
}
