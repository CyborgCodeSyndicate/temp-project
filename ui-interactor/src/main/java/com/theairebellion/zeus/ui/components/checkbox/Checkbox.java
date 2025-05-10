package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.List;
import org.openqa.selenium.By;

/**
 * Defines operations for interacting with checkbox components.
 *
 * <p>This interface provides methods to select or deselect checkboxes,
 * verify their selected and enabled states, and retrieve checkbox labels.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface Checkbox {

   /**
    * Selects checkboxes with the specified text within the given container.
    *
    * @param container    The container holding the checkboxes.
    * @param checkBoxText The text identifiers for the checkboxes to select.
    */
   void select(SmartWebElement container, String... checkBoxText);

   /**
    * Selects checkboxes within the given container using a specified strategy.
    *
    * @param container The container holding the checkboxes.
    * @param strategy  The strategy used for selecting checkboxes.
    * @return A string representing the selected checkbox label if applicable.
    */
   String select(SmartWebElement container, Strategy strategy);

   /**
    * Selects checkboxes identified by the given text at the top level.
    *
    * @param checkBoxText The text identifiers for the checkboxes to select.
    */
   void select(String... checkBoxText);

   /**
    * Selects checkboxes located by the specified locator.
    *
    * @param checkBoxLocator The locator identifying the checkboxes.
    */
   void select(By... checkBoxLocator);

   /**
    * Deselects checkboxes with the specified text within the given container.
    *
    * @param container    The container holding the checkboxes.
    * @param checkBoxText The text identifiers for the checkboxes to deselect.
    */
   void deSelect(SmartWebElement container, String... checkBoxText);

   /**
    * Deselects checkboxes within the given container using a specified strategy.
    *
    * @param container The container holding the checkboxes.
    * @param strategy  The strategy used for deselecting checkboxes.
    * @return A string representing the deselected checkbox label if applicable.
    */
   String deSelect(SmartWebElement container, Strategy strategy);

   /**
    * Deselects checkboxes identified by the given text at the top level.
    *
    * @param checkBoxText The text identifiers for the checkboxes to deselect.
    */
   void deSelect(String... checkBoxText);

   /**
    * Deselects checkboxes located by the specified locator.
    *
    * @param checkBoxLocator The locator identifying the checkboxes.
    */
   void deSelect(By... checkBoxLocator);

   /**
    * Checks if checkboxes with the specified text within the given container are selected.
    *
    * @param container    The container holding the checkboxes.
    * @param checkBoxText The text identifiers for the checkboxes.
    * @return true if all specified checkboxes are selected; false otherwise.
    */
   boolean areSelected(SmartWebElement container, String... checkBoxText);

   /**
    * Checks if checkboxes identified by the given text at the top level are selected.
    *
    * @param checkBoxText The text identifiers for the checkboxes.
    * @return true if all specified checkboxes are selected; false otherwise.
    */
   boolean areSelected(String... checkBoxText);

   /**
    * Checks if checkboxes located by the specified locator are selected.
    *
    * @param checkBoxLocator The locator identifying the checkboxes.
    * @return true if all specified checkboxes are selected; false otherwise.
    */
   boolean areSelected(By... checkBoxLocator);

   /**
    * Checks if checkboxes with the specified text within the given container are enabled.
    *
    * @param container    The container holding the checkboxes.
    * @param checkBoxText The text identifiers for the checkboxes.
    * @return true if all specified checkboxes are enabled; false otherwise.
    */
   boolean areEnabled(SmartWebElement container, String... checkBoxText);

   /**
    * Checks if checkboxes identified by the given text at the top level are enabled.
    *
    * @param checkBoxText The text identifiers for the checkboxes.
    * @return true if all specified checkboxes are enabled; false otherwise.
    */
   boolean areEnabled(String... checkBoxText);

   /**
    * Checks if checkboxes located by the specified locator are enabled.
    *
    * @param checkBoxLocator The locator identifying the checkboxes.
    * @return true if all specified checkboxes are enabled; false otherwise.
    */
   boolean areEnabled(By... checkBoxLocator);

   /**
    * Retrieves a list of labels for all selected checkboxes within the given container.
    *
    * @param container The container holding the checkboxes.
    * @return A list of selected checkbox labels.
    */
   List<String> getSelected(SmartWebElement container);

   /**
    * Retrieves a list of labels for all selected checkboxes located by the specified locator.
    *
    * @param containerLocator The locator identifying the checkboxes.
    * @return A list of selected checkbox labels.
    */
   List<String> getSelected(By containerLocator);

   /**
    * Retrieves a list of labels for all checkboxes within the given container.
    *
    * @param container The container holding the checkboxes.
    * @return A list of all checkbox labels.
    */
   List<String> getAll(SmartWebElement container);

   /**
    * Retrieves a list of labels for all checkboxes located by the specified locator.
    *
    * @param containerLocator The locator identifying the checkboxes.
    * @return A list of all checkbox labels.
    */
   List<String> getAll(By containerLocator);

}
