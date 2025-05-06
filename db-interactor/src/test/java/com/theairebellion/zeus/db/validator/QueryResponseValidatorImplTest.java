package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.log.LogDb;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryResponseValidatorImplTest {

   private static final String KEY_NUM_ROWS = "numRows";
   private static final String KEY_JSON_PATH_NAME = "$.name";
   private static final String KEY_COLUMN_NAME = "name";
   private static final String VALUE_NAME = "John Doe";
   private static final int EXPECTED_ROW_COUNT = 2;

   @Mock
   private JsonPathExtractor jsonPathExtractor;

   @InjectMocks
   private QueryResponseValidatorImpl validator;

   @Test
   @DisplayName("Should validate number of rows")
   void testValidateQueryResponse_NumberRowsValidation() {
      // Arrange
      QueryResponse queryResponse = new QueryResponse(List.of(Map.of("id", 1), Map.of("id", 2)));

      Assertion assertion = Assertion.builder()
            .key(KEY_NUM_ROWS)
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.NUMBER_ROWS)
            .expected(EXPECTED_ROW_COUNT)
            .soft(false)
            .build();

      // Act
      List<AssertionResult<Integer>> results = validator.validateQueryResponse(queryResponse, assertion);

      // Assert
      assertAll(
            "Number of rows validation should work correctly",
            () -> assertEquals(1, results.size(), "Should return one result"),
            () -> assertTrue(results.get(0).isPassed(), "Validation should pass"),
            () -> assertEquals(EXPECTED_ROW_COUNT, results.get(0).getExpectedValue(), "Expected value should match"),
            () -> assertEquals(EXPECTED_ROW_COUNT, results.get(0).getActualValue(), "Actual value should match")
      );

      // No interaction with JsonPathExtractor is expected for NUMBER_ROWS target
      verifyNoInteractions(jsonPathExtractor);
   }

   @Test
   @DisplayName("Should validate using JsonPath expression")
   void testValidateQueryResponse_JsonPathValidation() {
      // Arrange
      QueryResponse queryResponse = new QueryResponse(List.of(Map.of("name", VALUE_NAME)));
      when(jsonPathExtractor.extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class)).thenReturn(VALUE_NAME);

      Assertion assertion = Assertion.builder()
            .key(KEY_JSON_PATH_NAME)
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.QUERY_RESULT)
            .expected(VALUE_NAME)
            .soft(false)
            .build();

      // Act
      List<AssertionResult<String>> results = validator.validateQueryResponse(queryResponse, assertion);

      // Assert
      assertAll(
            "JsonPath validation should work correctly",
            () -> assertEquals(1, results.size(), "Should return one result"),
            () -> assertTrue(results.get(0).isPassed(), "Validation should pass"),
            () -> assertEquals(VALUE_NAME, results.get(0).getExpectedValue(), "Expected value should match"),
            () -> assertEquals(VALUE_NAME, results.get(0).getActualValue(), "Actual value should match")
      );

      verify(jsonPathExtractor).extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class);
   }

   @Test
   @DisplayName("Should validate column existence")
   void testValidateQueryResponse_ColumnValidation() {
      // Arrange
      Map<String, Object> row = Map.of("id", 1, "name", VALUE_NAME);
      QueryResponse queryResponse = new QueryResponse(List.of(row));

      when(jsonPathExtractor.extract(row.keySet(), KEY_COLUMN_NAME, Object.class)).thenReturn(KEY_COLUMN_NAME);

      Assertion assertion = Assertion.builder()
            .key(KEY_COLUMN_NAME)
            .target(DbAssertionTarget.COLUMNS)
            .type(AssertionTypes.CONTAINS)
            .expected(KEY_COLUMN_NAME)
            .soft(false)
            .build();

      // Act
      List<AssertionResult<String>> results = validator.validateQueryResponse(queryResponse, assertion);

      // Assert
      assertAll(
            "Column validation should work correctly",
            () -> assertEquals(1, results.size(), "Should return one result"),
            () -> assertTrue(results.get(0).isPassed(), "Validation should pass"),
            () -> assertEquals(KEY_COLUMN_NAME, results.get(0).getExpectedValue(), "Expected value should match"),
            () -> assertEquals(KEY_COLUMN_NAME, results.get(0).getActualValue(), "Actual value should match")
      );

      verify(jsonPathExtractor).extract(row.keySet(), KEY_COLUMN_NAME, Object.class);
   }

   @Test
   @DisplayName("Should throw exception when JsonPath expression returns null")
   void testValidateQueryResponse_JsonPathReturnsNull() {
      // Arrange
      QueryResponse queryResponse = new QueryResponse(List.of(Map.of("other", "value")));
      when(jsonPathExtractor.extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class)).thenReturn(null);

      Assertion assertion = Assertion.builder()
            .key(KEY_JSON_PATH_NAME)
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.QUERY_RESULT)
            .expected(VALUE_NAME)
            .soft(false)
            .build();

      // Act & Assert
      IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateQueryResponse(queryResponse, assertion),
            "Should throw exception when JsonPath returns null"
      );

      assertTrue(exception.getMessage().contains(KEY_JSON_PATH_NAME),
            "Exception message should contain the JsonPath expression");
      verify(jsonPathExtractor).extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class);
   }

   @Test
   @DisplayName("Should throw exception when assertion key is null for QUERY_RESULT target")
   void testValidateQueryResponse_NullKeyForQueryResult() {
      // Arrange
      QueryResponse queryResponse = new QueryResponse(List.of(Map.of("name", VALUE_NAME)));

      Assertion assertion = Assertion.builder()
            .key(null)
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.QUERY_RESULT)
            .expected(VALUE_NAME)
            .soft(false)
            .build();

      // Act & Assert
      InvalidAssertionException exception = assertThrows(
            InvalidAssertionException.class,
            () -> validator.validateQueryResponse(queryResponse, assertion),
            "Should throw exception when key is null for QUERY_RESULT target"
      );

      assertTrue(exception.getMessage().contains("non-null key"),
            "Exception message should mention the need for a non-null key");
   }

   @Test
   @DisplayName("Should throw exception when assertion key is null for COLUMNS target")
   void testValidateQueryResponse_NullKeyForColumns() {
      // Arrange
      QueryResponse queryResponse = new QueryResponse(List.of(Map.of("name", VALUE_NAME)));

      Assertion assertion = Assertion.builder()
            .key(null)
            .type(AssertionTypes.CONTAINS)
            .target(DbAssertionTarget.COLUMNS)
            .expected(KEY_COLUMN_NAME)
            .soft(false)
            .build();

      // Act & Assert
      InvalidAssertionException exception = assertThrows(
            InvalidAssertionException.class,
            () -> validator.validateQueryResponse(queryResponse, assertion),
            "Should throw exception when key is null for COLUMNS target"
      );

      assertTrue(exception.getMessage().contains("non-null key"),
            "Exception message should mention the need for a non-null key");
   }

   @Test
   @DisplayName("Should throw exception when query result is empty for COLUMNS target")
   void testValidateQueryResponse_EmptyRowsForColumns() {
      // Arrange
      QueryResponse queryResponse = new QueryResponse(Collections.emptyList());

      Assertion assertion = Assertion.builder()
            .key(KEY_COLUMN_NAME)
            .type(AssertionTypes.CONTAINS)
            .target(DbAssertionTarget.COLUMNS)
            .expected(KEY_COLUMN_NAME)
            .soft(false)
            .build();

      // Act & Assert
      IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateQueryResponse(queryResponse, assertion),
            "Should throw exception when rows are empty for COLUMNS target"
      );

      assertTrue(exception.getMessage().contains("empty"),
            "Exception message should mention that the query result is empty");
   }

   @Test
   @DisplayName("Should throw exception when column is not found")
   void testValidateQueryResponse_ColumnNotFound() {
      // Arrange
      Map<String, Object> row = Map.of("id", 1, "other", "value");
      QueryResponse queryResponse = new QueryResponse(List.of(row));

      when(jsonPathExtractor.extract(row.keySet(), KEY_COLUMN_NAME, Object.class)).thenReturn(null);

      Assertion assertion = Assertion.builder()
            .key(KEY_COLUMN_NAME)
            .type(AssertionTypes.CONTAINS)
            .target(DbAssertionTarget.COLUMNS)
            .expected(KEY_COLUMN_NAME)
            .soft(false)
            .build();

      // Act & Assert
      IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateQueryResponse(queryResponse, assertion),
            "Should throw exception when column is not found"
      );

      assertTrue(exception.getMessage().contains(KEY_COLUMN_NAME),
            "Exception message should contain the column name");
      verify(jsonPathExtractor).extract(row.keySet(), KEY_COLUMN_NAME, Object.class);
   }

   @Test
   @DisplayName("Should validate with multiple assertions")
   void testValidateQueryResponse_MultipleAssertions() {
      // Arrange
      Map<String, Object> row = Map.of("id", 1, "name", VALUE_NAME);
      QueryResponse queryResponse = new QueryResponse(List.of(row, Map.of("id", 2, "name", "Jane Doe")));

      when(jsonPathExtractor.extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class)).thenReturn(VALUE_NAME);
      when(jsonPathExtractor.extract(row.keySet(), KEY_COLUMN_NAME, Object.class)).thenReturn(KEY_COLUMN_NAME);

      Assertion rowCountAssertion = Assertion.builder()
            .key(KEY_NUM_ROWS)
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.NUMBER_ROWS)
            .expected(2)
            .soft(false)
            .build();

      Assertion jsonPathAssertion = Assertion.builder()
            .key(KEY_JSON_PATH_NAME)
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.QUERY_RESULT)
            .expected(VALUE_NAME)
            .soft(false)
            .build();

      Assertion columnAssertion = Assertion.builder()
            .key(KEY_COLUMN_NAME)
            .type(AssertionTypes.CONTAINS)
            .target(DbAssertionTarget.COLUMNS)
            .expected(KEY_COLUMN_NAME)
            .soft(false)
            .build();

      // Act
      List<AssertionResult<Object>> results = validator.validateQueryResponse(
            queryResponse, rowCountAssertion, jsonPathAssertion, columnAssertion);

      // Assert
      assertEquals(3, results.size(), "Should return three results");
      assertTrue(results.stream().allMatch(AssertionResult::isPassed), "All validations should pass");

      verify(jsonPathExtractor).extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class);
      verify(jsonPathExtractor).extract(row.keySet(), KEY_COLUMN_NAME, Object.class);
   }

   @Test
   @DisplayName("Should log extended validation target after processing assertions")
   void testPrintAssertionTarget_LogsExtended() {
      // Arrange: a 2‐row response and a simple row‐count assertion
      QueryResponse queryResponse = new QueryResponse(List.of(
            Map.of("id", 1),
            Map.of("id", 2)
      ));
      Assertion assertion = Assertion.builder()
            .key("numRows")
            .type(AssertionTypes.IS)
            .target(DbAssertionTarget.NUMBER_ROWS)
            .expected(2)
            .soft(false)
            .build();

      // Act & Assert: static‐mock LogDb and verify extended() was called once
      try (MockedStatic<LogDb> logs = mockStatic(LogDb.class)) {
         validator.validateQueryResponse(queryResponse, assertion);
         logs.verify(() -> LogDb.extended(
               eq("Validation target: [{}]"),
               any(String.class)
         ), times(1));
      }
   }
}