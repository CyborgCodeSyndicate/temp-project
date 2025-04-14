package com.theairebellion.zeus.ui.components.table.sort;

/**
 * Defines sorting strategies that can be applied to table columns.
 *
 * <p>This enum provides different sorting options for organizing table data.</p>
 *
 * <p>This enum is primarily used in the {@code TableService} and {@code TableServiceImpl} classes
 * to control the sorting behavior of table columns.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum SortingStrategy {

   /**
    * Sorts the column in ascending order (e.g., A-Z, 0-9).
    */
   ASC,

   /**
    * Sorts the column in descending order (e.g., Z-A, 9-0).
    */
   DESC,

   /**
    * No sorting is applied to the column.
    */
   NO_SORT

}
