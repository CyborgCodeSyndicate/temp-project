package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.function.BiConsumer;

/**
 * Functional interface for inserting values into a table cell.
 *
 * <p>This interface provides a contract for custom cell insertion logic,
 * enabling dynamic handling of table cell modifications.
 *
 * <p>It is used in {@code TableImpl}, {@code CellLocator}, {@code TableEntry}, and {@code CustomCellInsertion}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@FunctionalInterface
public interface CellInsertionFunction extends BiConsumer<SmartWebElement, String[]> {

   /**
    * Performs the insertion of values into the specified table cell.
    *
    * @param cellElement The {@link SmartWebElement} representing the target cell.
    * @param values      The values to be inserted into the cell.
    */
   void cellInsertionFunction(SmartWebElement cellElement, String... values);

   /**
    * Default implementation of {@link BiConsumer#accept}, forwarding execution
    * to {@link #cellInsertionFunction(SmartWebElement, String...)}.
    *
    * @param smartWebElement The target table cell.
    * @param objects         The values to insert.
    */
   @Override
   default void accept(SmartWebElement smartWebElement, String[] objects) {
      cellInsertionFunction(smartWebElement, objects);
   }

}
