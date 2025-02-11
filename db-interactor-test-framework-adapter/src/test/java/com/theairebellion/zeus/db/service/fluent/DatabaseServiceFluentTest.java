package com.theairebellion.zeus.db.service.fluent;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.db.service.fluent.mock.DummyEnum;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;

class DatabaseServiceFluentTest {

    private DatabaseService databaseService;
    private DatabaseServiceFluent fluent;
    private DbQuery query;
    private QueryResponse queryResponse;
    private Assertion<?> assertion;
    private List<AssertionResult<Object>> assertionResults;

    @BeforeEach
    void setUp() throws Exception {
        databaseService = mock(DatabaseService.class);
        fluent = new DatabaseServiceFluent(databaseService);

        var realQuest = new Quest();
        var realSuperQuest = new SuperQuest(realQuest);

        Field questField = DatabaseServiceFluent.class.getSuperclass().getDeclaredField("quest");
        questField.setAccessible(true);
        questField.set(fluent, realSuperQuest);

        query = mock(DbQuery.class);
        queryResponse = mock(QueryResponse.class);
        assertion = mock(Assertion.class);

        AssertionResult<Object> result = new AssertionResult<>(true, "Validation", "expected", "actual", false);
        assertionResults = Collections.singletonList(result);
    }

    @Test
    void testQuery() {
        when(databaseService.query(query)).thenReturn(queryResponse);
        doReturn(DummyEnum.VALUE).when(query).enumImpl();

        var result = fluent.query(query);

        assert fluent == result;
        verify(databaseService).query(query);
    }

    @Test
    void testQueryWithJsonPath() {
        String jsonPath = "$.data";
        when(databaseService.query(query, jsonPath, String.class)).thenReturn("resultString");
        doReturn(DummyEnum.VALUE).when(query).enumImpl();

        var result = fluent.query(query, jsonPath, String.class);

        assert fluent == result;
        verify(databaseService).query(query, jsonPath, String.class);
    }

    @Test
    void testValidateWithQueryResponseAndAssertions() {
        when(databaseService.validate(queryResponse, assertion)).thenReturn(assertionResults);

        var result = fluent.validate(queryResponse, assertion);

        assert fluent == result;
        verify(databaseService).validate(queryResponse, assertion);
    }

    @Test
    void testQueryAndValidate() {
        when(databaseService.query(query)).thenReturn(queryResponse);
        when(databaseService.validate(queryResponse, assertion)).thenReturn(assertionResults);
        doReturn(DummyEnum.VALUE).when(query).enumImpl();

        var result = fluent.queryAndValidate(query, assertion);

        assert fluent == result;
        verify(databaseService).query(query);
        verify(databaseService).validate(queryResponse, assertion);
    }

    @Test
    void testValidateRunnable() {
        Runnable runnable = () -> {};

        var result = fluent.validate(runnable);

        assert fluent == result;
    }

    @Test
    void testValidateConsumer() {
        Consumer<SoftAssertions> consumer = softAssertions -> {};

        var result = fluent.validate(consumer);

        assert fluent == result;
    }

    @Test
    void testRetryUntil() {
        @SuppressWarnings("unchecked")
        RetryCondition<Boolean> retryCondition = mock(RetryCondition.class);
        when(retryCondition.function()).thenReturn(service -> true);
        when(retryCondition.condition()).thenReturn(Predicate.isEqual(true));

        DatabaseServiceFluent result = fluent.retryUntil(retryCondition, Duration.ofMillis(100), Duration.ofMillis(10));

        assert fluent == result;
        verify(retryCondition).function();
        verify(retryCondition).condition();
    }
}