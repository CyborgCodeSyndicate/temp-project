package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

/**
 * Defines a contract for filtering table cells using various filter strategies.
 *
 * <p>Implementations of this interface enable interaction with table filtering mechanisms,
 * applying different strategies to filter values dynamically.
 *
 * <p>This interface is used in {@code TableImpl} and other table-related components.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface TableFilter {

   /**
    * Applies a filter to a specific table cell using a defined component type and strategy.
    *
    * @param cellElement    The table cell element to filter.
    * @param componentType  The component type associated with the filtering process.
    * @param filterStrategy The strategy to apply for filtering.
    * @param values         The values to use for filtering.
    */
   void tableFilter(SmartWebElement cellElement, ComponentType componentType, FilterStrategy filterStrategy,
                    String... values);
}
