package com.theairebellion.zeus.ui.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UiTablesAssertionTargetTest {

   @Test
   void testEnumCreation() {
      // Verify all enum values are created
      UiTablesAssertionTarget[] targets = UiTablesAssertionTarget.values();
      assertEquals(4, targets.length);
   }

   @Test
   void testTargetMethod() {
      for (UiTablesAssertionTarget target : UiTablesAssertionTarget.values()) {
         assertEquals(target, target.target());
      }
   }

   @Test
   void testSpecificEnumValues() {
      // Verify specific enum values exist
      UiTablesAssertionTarget[] expectedTargets = {
            UiTablesAssertionTarget.ROW_VALUES,
            UiTablesAssertionTarget.TABLE_VALUES,
            UiTablesAssertionTarget.ROW_ELEMENTS,
            UiTablesAssertionTarget.TABLE_ELEMENTS
      };

      for (UiTablesAssertionTarget target : expectedTargets) {
         assertNotNull(target);
         assertEquals(target, target.target());
      }
   }
}
