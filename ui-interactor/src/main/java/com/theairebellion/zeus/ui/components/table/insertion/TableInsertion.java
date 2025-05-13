package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

/**
 * Defines the contract for inserting values into table cells.
 *
 * <p>This interface provides a mechanism to insert data into a table cell dynamically,
 * leveraging different component types for handling the insertion process.
 *
 * <p>Used in {@code TableServiceRegistry} and {@code TableImpl}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface TableInsertion {

   /**
    * Inserts values into the specified table cell.
    *
    * @param cellElement   The {@link SmartWebElement} representing the target cell.
    * @param componentType The {@link ComponentType} defining the type of UI component used for insertion.
    * @param values        The values to be inserted into the cell.
    */
   void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values);
}
