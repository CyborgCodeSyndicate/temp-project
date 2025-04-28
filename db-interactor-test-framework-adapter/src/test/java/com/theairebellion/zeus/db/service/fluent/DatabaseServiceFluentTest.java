package com.theairebellion.zeus.db.service.fluent;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.db.service.fluent.mock.DummyEnum;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DatabaseServiceFluent tests")
class DatabaseServiceFluentTest {

    @Mock
    private DatabaseService databaseService;

    private DatabaseServiceFluent fluent;

    @Mock
    private DbQuery query;

    @Mock
    private QueryResponse queryResponse;

    @Mock
    private Assertion assertion;

    private List<AssertionResult<Object>> assertionResults;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize the DatabaseServiceFluent with the mocked service
        fluent = new DatabaseServiceFluent(databaseService);

        // Set up the quest field via reflection (needed for the fluent API to work)
        var realQuest = new Quest();
        var realSuperQuest = new SuperQuest(realQuest);

        Field questField = DatabaseServiceFluent.class.getSuperclass().getDeclaredField("quest");
        questField.setAccessible(true);
        questField.set(fluent, realSuperQuest);

        // Prepare common test data
        AssertionResult<Object> result = new AssertionResult<>(true, "Validation", "expected", "actual", false);
        assertionResults = Collections.singletonList(result);

        // Setup common mock behavior - use lenient() to avoid UnnecessaryStubbingException
        // when this stubbing isn't used in all tests
        lenient().doReturn(DummyEnum.VALUE).when(query).enumImpl();
    }

    @Nested
    @DisplayName("Query operations")
    class QueryOperations {

        @Test
        @DisplayName("query() should delegate to databaseService and return fluent instance")
        void testQuery() {
            // Given
            when(databaseService.query(query)).thenReturn(queryResponse);

            // When
            DatabaseServiceFluent result = fluent.query(query);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            verify(databaseService).query(query);
        }

        @Test
        @DisplayName("query() with JsonPath should delegate to databaseService and return fluent instance")
        void testQueryWithJsonPath() {
            // Given
            String jsonPath = "$.data";
            when(databaseService.query(query, jsonPath, String.class)).thenReturn("resultString");

            // When
            DatabaseServiceFluent result = fluent.query(query, jsonPath, String.class);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            verify(databaseService).query(query, jsonPath, String.class);
        }

        @Test
        @DisplayName("queryAndValidate() should chain query and validate operations and return fluent instance")
        void testQueryAndValidate() {
            // Given
            when(databaseService.query(query)).thenReturn(queryResponse);
            when(databaseService.validate(queryResponse, assertion)).thenReturn(assertionResults);

            // When
            DatabaseServiceFluent result = fluent.queryAndValidate(query, assertion);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            // Verify both query and validate were called with the expected arguments
            verify(databaseService).query(query);
            verify(databaseService).validate(queryResponse, assertion);
        }
    }

    @Nested
    @DisplayName("Validation operations")
    class ValidationOperations {

        @Test
        @DisplayName("validate() with queryResponse should delegate to databaseService and return fluent instance")
        void testValidateWithQueryResponseAndAssertions() {
            // Given
            when(databaseService.validate(queryResponse, assertion)).thenReturn(assertionResults);

            // When
            DatabaseServiceFluent result = fluent.validate(queryResponse, assertion);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            verify(databaseService).validate(queryResponse, assertion);
        }

        @Test
        @DisplayName("validate() with Runnable should execute the runnable and return fluent instance")
        void testValidateRunnable() {
            // Given
            Runnable mockRunnable = mock(Runnable.class);

            // When
            DatabaseServiceFluent result = fluent.validate(mockRunnable);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            verify(mockRunnable).run();
        }

        @Test
        @DisplayName("validate() with Consumer should apply the consumer and return fluent instance")
        void testValidateConsumer() {
            // Given
            @SuppressWarnings("unchecked")
            Consumer<SoftAssertions> mockConsumer = mock(Consumer.class);

            // When
            DatabaseServiceFluent result = fluent.validate(mockConsumer);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            // Verify the consumer was applied with any SoftAssertions instance
            verify(mockConsumer).accept(any(SoftAssertions.class));
        }
    }

    @Nested
    @DisplayName("Retry operations")
    class RetryOperations {

        @Test
        @DisplayName("retryUntil() should configure retry condition and return fluent instance")
        void testRetryUntil() {
            // Given
            @SuppressWarnings("unchecked")
            RetryCondition<Boolean> retryCondition = mock(RetryCondition.class);
            Duration timeout = Duration.ofMillis(100);
            Duration interval = Duration.ofMillis(10);

            when(retryCondition.function()).thenReturn(service -> true);
            when(retryCondition.condition()).thenReturn(Predicate.isEqual(true));

            // When
            DatabaseServiceFluent result = fluent.retryUntil(retryCondition, timeout, interval);

            // Then
            assertThat(result)
                    .as("The method should return the fluent instance for method chaining")
                    .isSameAs(fluent);

            verify(retryCondition).function();
            verify(retryCondition).condition();
        }
    }

    @Nested
    @DisplayName("Protected method coverage")
    class PrivateMethodCoverage {

        @Test
        @DisplayName("getDatabaseService() should return the database service instance")
        void getDatabaseServiceShouldReturnDatabaseServiceInstance() throws Exception {
            // When
            Object result = fluent.getDatabaseService();

            // Then
            assertThat(result)
                    .as("getDatabaseService() should return the DatabaseService instance passed to constructor")
                    .isSameAs(databaseService);
        }
    }
}