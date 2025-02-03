package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryResponseValidatorImplTest {

    private QueryResponseValidatorImpl validator;
    private JsonPathExtractor mockExtractor;

    @BeforeEach
    void setUp() {
        // Create a mock for JsonPathExtractor
        mockExtractor = Mockito.mock(JsonPathExtractor.class);
        validator = new QueryResponseValidatorImpl(mockExtractor);
    }

    @Test
    void testValidateQueryResponse_NumberRowsValidation() {
        // Arrange
        QueryResponse queryResponse = new QueryResponse(List.of(Map.of("id", 1), Map.of("id", 2)));
        Assertion<Integer> assertion = Assertion.builder(Integer.class)
                .key("numRows")
                .type(AssertionTypes.IS)
                .target(DbAssertionTarget.NUMBER_ROWS) // Set the target
                .expected(2)
                .soft(false)
                .build();

        // Act
        List<AssertionResult<Integer>> results = validator.validateQueryResponse(queryResponse, assertion);

        // Assert
        assertEquals(1, results.size());
        AssertionResult<Integer> result = results.get(0);
        assertTrue(result.isPassed(), "Expected the validation to pass");
        assertEquals(2, result.getExpectedValue());
        assertEquals(2, result.getActualValue());
    }

    @Test
    void testValidateQueryResponse_JsonPathValidation() {
        // Arrange
        QueryResponse queryResponse = new QueryResponse(List.of(Map.of("name", "John Doe")));
        when(mockExtractor.extract(queryResponse.getRows(), "$.name", Object.class)).thenReturn("John Doe");

        Assertion<String> assertion = Assertion.builder(String.class)
                .key("$.name")
                .type(AssertionTypes.IS)
                .target(DbAssertionTarget.QUERY_RESULT) // Set the target
                .expected("John Doe")
                .soft(false)
                .build();

        // Act
        List<AssertionResult<String>> results = validator.validateQueryResponse(queryResponse, assertion);

        // Assert
        assertEquals(1, results.size());
        AssertionResult<String> result = results.get(0);
        assertTrue(result.isPassed(), "Expected the validation to pass");
        assertEquals("John Doe", result.getExpectedValue());
        assertEquals("John Doe", result.getActualValue());
    }

    @Test
    void testValidateQueryResponse_ColumnValidation() {
        // Arrange
        QueryResponse queryResponse = new QueryResponse(List.of(Map.of("id", 1, "name", "John Doe")));
        when(mockExtractor.extract(queryResponse.getRows().get(0).keySet(), "name", Object.class)).thenReturn("name");

        Assertion<String> assertion = Assertion.builder(String.class)
                .key("name")
                .target(DbAssertionTarget.COLUMNS) // Set the target
                .type(AssertionTypes.CONTAINS)
                .expected("name")
                .soft(false)
                .build();

        // Act
        List<AssertionResult<String>> results = validator.validateQueryResponse(queryResponse, assertion);

        // Assert
        assertEquals(1, results.size());
        AssertionResult<String> result = results.get(0);
        assertTrue(result.isPassed(), "Expected the validation to pass");
        assertEquals("name", result.getExpectedValue());
        assertEquals("name", result.getActualValue());
    }
}