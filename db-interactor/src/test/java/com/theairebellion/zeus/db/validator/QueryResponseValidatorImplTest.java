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

    private static final String KEY_NUM_ROWS = "numRows";
    private static final String KEY_JSON_PATH_NAME = "$.name";
    private static final String KEY_COLUMN_NAME = "name";
    private static final String VALUE_NAME = "John Doe";
    private static final int EXPECTED_ROW_COUNT = 2;

    private QueryResponseValidatorImpl validator;
    private JsonPathExtractor mockExtractor;

    @BeforeEach
    void setUp() {
        mockExtractor = Mockito.mock(JsonPathExtractor.class);
        validator = new QueryResponseValidatorImpl(mockExtractor);
    }

    @Test
    void testValidateQueryResponse_NumberRowsValidation() {
        QueryResponse queryResponse = new QueryResponse(List.of(Map.of("id", 1), Map.of("id", 2)));

        Assertion<?> assertion = Assertion.builder()
                .key(KEY_NUM_ROWS)
                .type(AssertionTypes.IS)
                .target(DbAssertionTarget.NUMBER_ROWS)
                .expected(EXPECTED_ROW_COUNT)
                .soft(false)
                .build();

        List<AssertionResult<Integer>> results = validator.validateQueryResponse(queryResponse, assertion);

        assertEquals(1, results.size());
        AssertionResult<Integer> result = results.get(0);
        assertTrue(result.isPassed());
        assertEquals(EXPECTED_ROW_COUNT, result.getExpectedValue());
        assertEquals(EXPECTED_ROW_COUNT, result.getActualValue());
    }

    @Test
    void testValidateQueryResponse_JsonPathValidation() {
        QueryResponse queryResponse = new QueryResponse(List.of(Map.of("name", VALUE_NAME)));
        when(mockExtractor.extract(queryResponse.getRows(), KEY_JSON_PATH_NAME, Object.class)).thenReturn(VALUE_NAME);

        Assertion<?> assertion = Assertion.builder()
                .key(KEY_JSON_PATH_NAME)
                .type(AssertionTypes.IS)
                .target(DbAssertionTarget.QUERY_RESULT)
                .expected(VALUE_NAME)
                .soft(false)
                .build();

        List<AssertionResult<String>> results = validator.validateQueryResponse(queryResponse, assertion);

        assertEquals(1, results.size());
        AssertionResult<String> result = results.get(0);
        assertTrue(result.isPassed());
        assertEquals(VALUE_NAME, result.getExpectedValue());
        assertEquals(VALUE_NAME, result.getActualValue());
    }

    @Test
    void testValidateQueryResponse_ColumnValidation() {
        QueryResponse queryResponse = new QueryResponse(List.of(Map.of("id", 1, "name", VALUE_NAME)));
        when(mockExtractor.extract(queryResponse.getRows().get(0).keySet(), KEY_COLUMN_NAME, Object.class)).thenReturn(KEY_COLUMN_NAME);

        Assertion<?> assertion = Assertion.builder()
                .key(KEY_COLUMN_NAME)
                .target(DbAssertionTarget.COLUMNS)
                .type(AssertionTypes.CONTAINS)
                .expected(KEY_COLUMN_NAME)
                .soft(false)
                .build();

        List<AssertionResult<String>> results = validator.validateQueryResponse(queryResponse, assertion);

        assertEquals(1, results.size());
        AssertionResult<String> result = results.get(0);
        assertTrue(result.isPassed());
        assertEquals(KEY_COLUMN_NAME, result.getExpectedValue());
        assertEquals(KEY_COLUMN_NAME, result.getActualValue());
    }
}