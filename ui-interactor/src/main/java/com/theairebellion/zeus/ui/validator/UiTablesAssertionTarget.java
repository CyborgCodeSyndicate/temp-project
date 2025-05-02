package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

/**
 * Enum representing different assertion targets within UI tables.
 *
 * <p>These targets specify the scope of validation when performing assertions on tables,
 * allowing for granular validation at the row or table level, either for values or elements.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum UiTablesAssertionTarget implements AssertionTarget<UiTablesAssertionTarget> {

   /**
    * Targets individual row values for assertions.
    */
   ROW_VALUES,

   /**
    * Targets entire table values for assertions.
    */
   TABLE_VALUES,

   /**
    * Targets row elements for UI-based assertions.
    */
   ROW_ELEMENTS,

   /**
    * Targets the entire table's elements for UI-based assertions.
    */
   TABLE_ELEMENTS;

   /**
    * Returns the current enum instance as the assertion target.
    *
    * @return The current enum instance.
    */
   @Override
   public UiTablesAssertionTarget target() {
      return this;
   }
}
