package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.AssertionType;
import java.util.List;

/**
 * Enum representing different types of table assertions.
 *
 * <p>Each entry corresponds to a specific validation check that can be performed on a table.
 * These assertions ensure data integrity, uniqueness, and expected behavior within table elements.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum TableAssertionTypes implements AssertionType {

   /**
    * Asserts that the table is not empty.
    * Expected type: {@link List}
    */
   TABLE_NOT_EMPTY(List.class),

   /**
    * Asserts that the table contains the expected number of rows.
    * Expected type: {@link List}
    */
   TABLE_ROW_COUNT(List.class),

   /**
    * Asserts that every row in the table contains specific expected values.
    * Expected type: {@link List}
    */
   EVERY_ROW_CONTAINS_VALUES(List.class),

   /**
    * Asserts that the table does not contain a specific row.
    * Expected type: {@link List}
    */
   TABLE_DOES_NOT_CONTAIN_ROW(List.class),

   /**
    * Asserts that all rows in the table are unique.
    * Expected type: {@link List}
    */
   ALL_ROWS_ARE_UNIQUE(List.class),

   /**
    * Asserts that there are no empty cells in the table.
    * Expected type: {@link List}
    */
   NO_EMPTY_CELLS(List.class),

   /**
    * Asserts that all values in a given column are unique.
    * Expected type: {@link List}
    */
   COLUMN_VALUES_ARE_UNIQUE(List.class),

   /**
    * Asserts that the entire table data matches the expected data.
    * Expected type: {@link List}
    */
   TABLE_DATA_MATCHES_EXPECTED(List.class),

   /**
    * Asserts that a given row is not empty.
    * Expected type: {@link List}
    */
   ROW_NOT_EMPTY(List.class),

   /**
    * Asserts that a given row contains specific expected values.
    * Expected type: {@link List}
    */
   ROW_CONTAINS_VALUES(List.class),

   /**
    * Asserts that all cells in the table are enabled.
    * Expected type: {@link List}
    */
   ALL_CELLS_ENABLED(List.class),

   /**
    * Asserts that all cells in the table are clickable.
    * Expected type: {@link List}
    */
   ALL_CELLS_CLICKABLE(List.class);

   /**
    * The data type that this assertion type supports.
    */
   private final Class<?> supportedType;

   /**
    * Constructor for defining the assertion type and its supported data type.
    *
    * @param supportedType The class type that this assertion operates on.
    * @param <T>           Generic type parameter for supported type.
    */
   <T> TableAssertionTypes(Class<T> supportedType) {
      this.supportedType = supportedType;
   }

   /**
    * Returns the type of the current enum value.
    *
    * @return The current enum instance.
    */
   @Override
   public Enum<?> type() {
      return this;
   }

   /**
    * Gets the class type that this assertion type supports.
    *
    * @return The supported class type.
    */
   @Override
   public Class<?> getSupportedType() {
      return supportedType;
   }

}
