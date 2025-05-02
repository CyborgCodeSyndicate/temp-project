package com.theairebellion.zeus.db.retry;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RetryConditionDb tests")
class RetryConditionDbTest {

   @Mock
   private DatabaseService databaseService;

   @Mock
   private DbQuery query;

   @Mock
   private QueryResponse queryResponse;


   @Nested
   @DisplayName("queryReturnsRows condition tests")
   class QueryReturnsRowsTests {

      @Test
      @DisplayName("should return true when rows exist")
      void shouldReturnTrueWhenRowsExist() {
         // Given
         Map<String, Object> row = new HashMap<>();
         row.put("column1", "value1");

         when(databaseService.query(query)).thenReturn(queryResponse);
         when(queryResponse.getRows()).thenReturn(Collections.singletonList(row));

         // When
         RetryCondition<Boolean> condition = RetryConditionDb.queryReturnsRows(query);
         boolean result = condition.condition().test(
               condition.function().apply(databaseService)
         );

         // Then
         assertThat(result)
               .as("Condition should be true when rows exist")
               .isTrue();

         verify(databaseService).query(query);
         verify(queryResponse).getRows();
      }

      @Test
      @DisplayName("should return false when no rows exist")
      void shouldReturnFalseWhenNoRowsExist() {
         // Given
         when(databaseService.query(query)).thenReturn(queryResponse);
         when(queryResponse.getRows()).thenReturn(Collections.emptyList());

         // When
         RetryCondition<Boolean> condition = RetryConditionDb.queryReturnsRows(query);
         boolean result = condition.condition().test(
               condition.function().apply(databaseService)
         );

         // Then
         assertThat(result)
               .as("Condition should be false when no rows exist")
               .isFalse();

         verify(databaseService).query(query);
         verify(queryResponse).getRows();
      }
   }

   @Nested
   @DisplayName("queryReturnsValueForField condition tests")
   class QueryReturnsValueForFieldTests {

      @Test
      @DisplayName("should return true when value matches expected")
      void shouldReturnTrueWhenValueMatches() {
         // Given
         String jsonPath = "somePath";
         String expectedValue = "expectedValue";

         when(databaseService.query(eq(query), eq(jsonPath), eq(String.class)))
               .thenReturn(expectedValue);

         // When
         RetryCondition<Object> condition = RetryConditionDb.queryReturnsValueForField(
               query, jsonPath, expectedValue);
         boolean result = condition.condition().test(
               condition.function().apply(databaseService)
         );

         // Then
         assertThat(result)
               .as("Condition should be true when returned value matches expected")
               .isTrue();

         verify(databaseService).query(query, jsonPath, String.class);
      }

      @Test
      @DisplayName("should return false when value does not match expected")
      void shouldReturnFalseWhenValueDoesNotMatch() {
         // Given
         String jsonPath = "somePath";
         String expectedValue = "expectedValue";
         String actualValue = "differentValue";

         when(databaseService.query(eq(query), eq(jsonPath), eq(String.class)))
               .thenReturn(actualValue);

         // When
         RetryCondition<Object> condition = RetryConditionDb.queryReturnsValueForField(
               query, jsonPath, expectedValue);
         boolean result = condition.condition().test(
               condition.function().apply(databaseService)
         );

         // Then
         assertThat(result)
               .as("Condition should be false when returned value does not match expected")
               .isFalse();

         verify(databaseService).query(query, jsonPath, String.class);
      }

      @Test
      @DisplayName("should handle different object types")
      void shouldHandleDifferentObjectTypes() {
         // Given
         String jsonPath = "somePath";
         Integer expectedValue = 42;

         when(databaseService.query(eq(query), eq(jsonPath), eq(Integer.class)))
               .thenReturn(expectedValue);

         // When
         RetryCondition<Object> condition = RetryConditionDb.queryReturnsValueForField(
               query, jsonPath, expectedValue);
         boolean result = condition.condition().test(
               condition.function().apply(databaseService)
         );

         // Then
         assertThat(result)
               .as("Condition should work with different object types")
               .isTrue();

         verify(databaseService).query(query, jsonPath, Integer.class);
      }
   }
}