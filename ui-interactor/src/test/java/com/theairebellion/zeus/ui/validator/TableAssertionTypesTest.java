package com.theairebellion.zeus.ui.validator;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TableAssertionTypesTest {

   @Test
   void testEnumCreation() {
      // Verify all enum values are created
      TableAssertionTypes[] types = TableAssertionTypes.values();
      assertEquals(12, types.length);
   }

   @Test
   void testTypeMethod() {
      for (TableAssertionTypes type : TableAssertionTypes.values()) {
         assertEquals(type, type.type());
      }
   }

   @Test
   void testSupportedType() {
      for (TableAssertionTypes type : TableAssertionTypes.values()) {
         assertEquals(List.class, type.getSupportedType());
      }
   }

   @Test
   void testSpecificEnumValues() {
      // Verify specific enum values exist and have correct supported type
      TableAssertionTypes[] expectedTypes = {
            TableAssertionTypes.TABLE_NOT_EMPTY,
            TableAssertionTypes.TABLE_ROW_COUNT,
            TableAssertionTypes.EVERY_ROW_CONTAINS_VALUES,
            TableAssertionTypes.TABLE_DOES_NOT_CONTAIN_ROW,
            TableAssertionTypes.ALL_ROWS_ARE_UNIQUE,
            TableAssertionTypes.NO_EMPTY_CELLS,
            TableAssertionTypes.COLUMN_VALUES_ARE_UNIQUE,
            TableAssertionTypes.TABLE_DATA_MATCHES_EXPECTED,
            TableAssertionTypes.ROW_NOT_EMPTY,
            TableAssertionTypes.ROW_CONTAINS_VALUES,
            TableAssertionTypes.ALL_CELLS_ENABLED,
            TableAssertionTypes.ALL_CELLS_CLICKABLE
      };

      for (TableAssertionTypes type : expectedTypes) {
         assertNotNull(type);
         assertEquals(List.class, type.getSupportedType());
         assertEquals(type, type.type());
      }
   }
}