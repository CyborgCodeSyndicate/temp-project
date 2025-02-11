package com.theairebellion.zeus.db.retry;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RetryConditionDbTest {

    private DatabaseService databaseServiceMock;
    private DbQuery queryMock;
    private QueryResponse queryResponseMock;

    @BeforeEach
    void setUp() {
        databaseServiceMock = mock(DatabaseService.class);
        queryMock = mock(DbQuery.class);
        queryResponseMock = mock(QueryResponse.class);
    }

    @Test
    void testQueryReturnsRows_WhenRowsExist() {
        Map<String, Object> row = new HashMap<>();
        row.put("column1", "value1");

        when(databaseServiceMock.query(queryMock)).thenReturn(queryResponseMock);
        when(queryResponseMock.getRows()).thenReturn(Collections.singletonList(row));


        RetryCondition<Boolean> condition = RetryConditionDb.queryReturnsRows(queryMock);

        assertTrue(condition.condition().test(condition.function().apply(databaseServiceMock)));
        verify(databaseServiceMock).query(queryMock);
    }

    @Test
    void testQueryReturnsRows_WhenNoRowsExist() {
        when(databaseServiceMock.query(queryMock)).thenReturn(queryResponseMock);
        when(queryResponseMock.getRows()).thenReturn(Collections.emptyList());

        RetryCondition<Boolean> condition = RetryConditionDb.queryReturnsRows(queryMock);

        assertFalse(condition.condition().test(condition.function().apply(databaseServiceMock)));
        verify(databaseServiceMock).query(queryMock);
    }

    @Test
    void testQueryReturnsValueForField_WhenValueMatches() {
        when(databaseServiceMock.query(eq(queryMock), eq("somePath"), eq(String.class)))
                .thenReturn("expectedValue");

        RetryCondition<Object> condition = RetryConditionDb.queryReturnsValueForField(queryMock, "somePath", "expectedValue");

        assertTrue(condition.condition().test(condition.function().apply(databaseServiceMock)));
        verify(databaseServiceMock).query(queryMock, "somePath", String.class);
    }

    @Test
    void testQueryReturnsValueForField_WhenValueDoesNotMatch() {
        when(databaseServiceMock.query(eq(queryMock), eq("somePath"), eq(String.class)))
                .thenReturn("differentValue");

        RetryCondition<Object> condition = RetryConditionDb.queryReturnsValueForField(queryMock, "somePath", "expectedValue");

        assertFalse(condition.condition().test(condition.function().apply(databaseServiceMock)));
        verify(databaseServiceMock).query(queryMock, "somePath", String.class);
    }
}
