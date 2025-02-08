package com.theairebellion.zeus.db.service;

import com.theairebellion.zeus.db.client.DbClient;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DatabaseServiceTest {

    private static final String QUERY_SELECT_USERS = "SELECT * FROM users";
    private static final String JSON_PATH_NAME = "$.name";
    private static final String EXPECTED_NAME = "John Doe";
    private static final Map<String, Object> ROW_ID_1 = Map.of("id", 1);
    private static final Map<String, Object> ROW_NAME_JOHN_DOE = Map.of("name", EXPECTED_NAME);

    private DatabaseService databaseService;
    private DbClientManager dbClientManager;
    private DbClient dbClient;
    private JsonPathExtractor jsonPathExtractor;
    private QueryResponseValidator queryResponseValidator;

    @BeforeEach
    void setup() {
        dbClientManager = mock(DbClientManager.class);
        dbClient = mock(DbClient.class);
        jsonPathExtractor = mock(JsonPathExtractor.class);
        queryResponseValidator = mock(QueryResponseValidator.class);

        databaseService = new DatabaseService(jsonPathExtractor, dbClientManager, queryResponseValidator);
    }

    @Test
    void testQuery_ShouldReturnQueryResponse() {
        DbQuery query = mock(DbQuery.class);
        QueryResponse response = new QueryResponse(List.of(ROW_ID_1));

        when(query.query()).thenReturn(QUERY_SELECT_USERS);
        when(dbClientManager.getClient(any())).thenReturn(dbClient);
        when(dbClient.executeQuery(any())).thenReturn(response);

        QueryResponse result = databaseService.query(query);

        assertEquals(response, result);
        verify(dbClient).executeQuery(QUERY_SELECT_USERS);
    }

    @Test
    void testQueryWithJsonPath_ShouldReturnExtractedValue() {
        DbQuery query = mock(DbQuery.class);
        QueryResponse response = new QueryResponse(List.of(ROW_NAME_JOHN_DOE));

        when(query.query()).thenReturn(QUERY_SELECT_USERS);
        when(dbClientManager.getClient(any())).thenReturn(dbClient);
        when(dbClient.executeQuery(any())).thenReturn(response);
        when(jsonPathExtractor.extract(response.getRows(), JSON_PATH_NAME, String.class)).thenReturn(EXPECTED_NAME);

        String result = databaseService.query(query, JSON_PATH_NAME, String.class);

        assertEquals(EXPECTED_NAME, result);
        verify(jsonPathExtractor).extract(response.getRows(), JSON_PATH_NAME, String.class);
    }

    @Test
    void testValidate_ShouldReturnAssertionResults() {
        QueryResponse queryResponse = new QueryResponse(List.of(ROW_ID_1));
        Assertion<?> assertion = mock(Assertion.class);
        List expectedResults = List.of(mock(AssertionResult.class));

        when(queryResponseValidator.validateQueryResponse(queryResponse, assertion)).thenReturn(expectedResults);

        List<AssertionResult<Object>> results = databaseService.validate(queryResponse, assertion);

        assertEquals(expectedResults, results);
        verify(queryResponseValidator).validateQueryResponse(queryResponse, assertion);
    }

    @Test
    void testQueryAndValidate_ShouldReturnAssertionResults() {
        DbQuery query = mock(DbQuery.class);
        QueryResponse response = new QueryResponse(List.of(ROW_ID_1));
        Assertion<?> assertion = mock(Assertion.class);
        List expectedResults = List.of(mock(AssertionResult.class));

        when(query.query()).thenReturn(QUERY_SELECT_USERS);
        when(dbClientManager.getClient(any())).thenReturn(dbClient);
        when(dbClient.executeQuery(any())).thenReturn(response);
        when(queryResponseValidator.validateQueryResponse(response, assertion)).thenReturn(expectedResults);

        List<AssertionResult<Object>> results = databaseService.queryAndValidate(query, assertion);

        assertEquals(expectedResults, results);
        verify(queryResponseValidator).validateQueryResponse(response, assertion);
    }
}