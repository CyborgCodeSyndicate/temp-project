package com.theairebellion.zeus.db.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QueryResponseTest {

   private static final String KEY1 = "name";
   private static final String KEY2 = "id";
   private static final String VALUE1 = "John";
   private static final int VALUE2 = 1;

   @Test
   @DisplayName("Should create QueryResponse with provided rows")
   void testConstructorAndGetters() {
      // Given
      Map<String, Object> row = new HashMap<>();
      row.put(KEY1, VALUE1);
      row.put(KEY2, VALUE2);
      List<Map<String, Object>> rows = Collections.singletonList(row);

      // When
      QueryResponse response = new QueryResponse(rows);

      // Then
      assertAll(
            "QueryResponse should be initialized with provided rows",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(rows, response.getRows(), "Rows should match provided list"),
            () -> assertEquals(1, response.getRows().size(), "Rows should contain exactly one entry"),
            () -> assertEquals(VALUE1, response.getRows().get(0).get(KEY1), "First row should contain expected name value"),
            () -> assertEquals(VALUE2, response.getRows().get(0).get(KEY2), "First row should contain expected id value")
      );
   }

   @Test
   @DisplayName("Should accept null rows without throwing exception")
   void testConstructorWithNull() {
      // When - Then
      assertDoesNotThrow(() -> new QueryResponse(null),
            "Constructor should accept null without throwing exception");

      // Verify behavior with null
      QueryResponse response = new QueryResponse(null);
      assertNull(response.getRows(), "Rows should be null when constructor is passed null");
   }

   @Test
   @DisplayName("Should reflect modifications to external list (no defensive copy)")
   void testExternalListModification() {
      // Given
      List<Map<String, Object>> originalRows = new ArrayList<>();
      Map<String, Object> row = new HashMap<>();
      row.put(KEY1, VALUE1);
      originalRows.add(row);

      QueryResponse response = new QueryResponse(originalRows);
      assertEquals(1, response.getRows().size(), "Initial size should be 1");

      // When - modifying external list
      originalRows.clear();

      // Then - change should be reflected since no defensive copy is made
      assertEquals(0, response.getRows().size(),
            "Changes to external list should be reflected in response");
   }

   @Test
   @DisplayName("Should allow modification of returned rows list")
   void testReturnedListModification() {
      // Given
      List<Map<String, Object>> originalRows = new ArrayList<>();
      Map<String, Object> row = new HashMap<>();
      row.put(KEY1, VALUE1);
      originalRows.add(row);

      QueryResponse response = new QueryResponse(originalRows);

      // When - Then
      assertDoesNotThrow(() -> response.getRows().clear(),
            "Should allow modification of returned list");

      // Verify modification worked
      assertAll(
            "Modifications to returned list should affect both returned and original lists",
            () -> assertEquals(0, response.getRows().size(), "Returned list should be modified"),
            () -> assertEquals(0, originalRows.size(), "Original list should also be affected")
      );
   }

   @Test
   @DisplayName("Should support equals and hashCode")
   void testEqualsAndHashCode() {
      // Given
      List<Map<String, Object>> rows1 = new ArrayList<>();
      Map<String, Object> row1 = new HashMap<>();
      row1.put(KEY1, VALUE1);
      rows1.add(row1);

      List<Map<String, Object>> rows2 = new ArrayList<>();
      Map<String, Object> row2 = new HashMap<>();
      row2.put(KEY1, VALUE1);
      rows2.add(row2);

      // When
      QueryResponse response1 = new QueryResponse(rows1);
      QueryResponse response2 = new QueryResponse(rows2);

      // Then
      assertAll(
            "Equal content should result in equal objects with equal hash codes",
            () -> assertEquals(response1, response2, "Equal content should result in equal objects"),
            () -> assertEquals(response1.hashCode(), response2.hashCode(), "Equal objects should have equal hash codes")
      );
   }

   @Test
   @DisplayName("Should not be equal with different content")
   void testNotEqual() {
      // Given
      List<Map<String, Object>> rows1 = new ArrayList<>();
      Map<String, Object> row1 = new HashMap<>();
      row1.put(KEY1, VALUE1);
      rows1.add(row1);

      List<Map<String, Object>> rows2 = new ArrayList<>();
      Map<String, Object> row2 = new HashMap<>();
      row2.put(KEY1, "Different value");
      rows2.add(row2);

      // When
      QueryResponse response1 = new QueryResponse(rows1);
      QueryResponse response2 = new QueryResponse(rows2);

      // Then
      assertNotEquals(response1, response2, "Different content should result in unequal objects");
   }

   @Test
   @DisplayName("Should implement toString method")
   void testToString() {
      // Given
      List<Map<String, Object>> rows = new ArrayList<>();
      Map<String, Object> row = new HashMap<>();
      row.put(KEY1, VALUE1);
      rows.add(row);

      // When
      QueryResponse response = new QueryResponse(rows);
      String toString = response.toString();

      // Then
      assertAll(
            "toString should include fields and values",
            () -> assertNotNull(toString, "toString result should not be null"),
            () -> assertTrue(toString.contains("rows"), "toString should include field names"),
            () -> assertTrue(toString.contains(KEY1), "toString should include map keys"),
            () -> assertTrue(toString.contains(VALUE1), "toString should include map values")
      );
   }

   @Test
   @DisplayName("Should support setter for rows")
   void testSetRows() {
      // Given
      List<Map<String, Object>> initialRows = new ArrayList<>();
      List<Map<String, Object>> newRows = new ArrayList<>();
      Map<String, Object> newRow = new HashMap<>();
      newRow.put(KEY1, VALUE1);
      newRows.add(newRow);

      QueryResponse response = new QueryResponse(initialRows);

      // When
      response.setRows(newRows);

      // Then
      assertAll(
            "Setter should update rows reference",
            () -> assertSame(newRows, response.getRows(), "Setter should update the rows reference"),
            () -> assertEquals(1, response.getRows().size(), "Size should reflect new rows"),
            () -> assertEquals(VALUE1, response.getRows().get(0).get(KEY1), "Content should reflect new rows")
      );
   }
}