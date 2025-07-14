package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterComponent;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionComponent;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.By;

/**
 * Represents the location and behavior of a table cell within a UI table structure.
 *
 * <p>This class encapsulates locators for identifying table cells and their associated headers,
 * along with metadata for handling custom insertion and filtering operations.
 *
 * <p>Used in {@code TableImpl} for handling dynamic table operations.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CellLocator {

   /**
    * The field name associated with this cell.
    */
   private String fieldName;

   /**
    * The locator used to find the table cell.
    */
   private By locator;

   /**
    * The locator used to extract the text content of the table cell.
    */
   private By cellTextLocator;

   /**
    * The locator used to identify the corresponding header cell.
    */
   private By headerCellLocator;

   /**
    * Indicates whether this cell represents a collection of elements.
    */
   private boolean collection;

   /**
    * The table section where this cell is located.
    */
   private String tableSection;

   /**
    * The component responsible for inserting values into this cell.
    */
   private CellInsertionComponent cellInsertionComponent;

   /**
    * The custom function used for inserting values into the cell.
    */
   private Class<? extends CellInsertionFunction> customCellInsertion;

   /**
    * The component responsible for filtering values in this cell.
    */
   private CellFilterComponent cellFilterComponent;

   /**
    * The custom function used for filtering values in this cell.
    */
   private Class<? extends CellFilterFunction> customCellFilter;
}
