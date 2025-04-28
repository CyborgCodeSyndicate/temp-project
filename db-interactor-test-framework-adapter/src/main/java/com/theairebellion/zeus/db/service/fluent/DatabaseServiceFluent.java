package com.theairebellion.zeus.db.service.fluent;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.base.ClassLevelHook;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import static com.theairebellion.zeus.db.storage.StorageKeysDb.DB;

/**
 * Provides a fluent API for database interactions.
 * <p>
 * This class enables structured database query execution, validation, and retry logic
 * using a fluent API approach, simplifying test interactions with databases.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@TestService("DB")
public class DatabaseServiceFluent extends FluentService implements ClassLevelHook {

    private final DatabaseService databaseService;

    /**
     * Constructs a new {@code DatabaseServiceFluent} with the given database service.
     *
     * @param databaseService The service responsible for executing database queries.
     */
    @Autowired
    public DatabaseServiceFluent(final DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Executes a database query and stores the response in the test storage.
     *
     * @param query The database query to execute.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    public DatabaseServiceFluent query(final DbQuery<?> query) {
        final QueryResponse queryResponse = databaseService.query(query);
        quest.getStorage().sub(DB).put(query.enumImpl(), queryResponse);
        return this;
    }

    /**
     * Executes a database query, extracts a value using JSONPath, and stores it in the test storage.
     *
     * @param query      The database query to execute.
     * @param jsonPath   The JSONPath expression to extract data from the query result.
     * @param resultType The expected type of the extracted value.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    public <T> DatabaseServiceFluent query(final DbQuery<?> query, final String jsonPath, final Class<T> resultType) {
        final T queryResult = databaseService.query(query, jsonPath, resultType);
        quest.getStorage().sub(DB).put(query.enumImpl(), queryResult);
        return this;
    }

    /**
     * Validates a database query response against the specified assertions.
     *
     * @param queryResponse The query response to validate.
     * @param assertions    The assertions to apply.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    public DatabaseServiceFluent validate(final QueryResponse queryResponse, final Assertion... assertions) {
        final List<AssertionResult<Object>> assertionResults = databaseService.validate(queryResponse, assertions);
        validation(assertionResults);
        return this;
    }

    /**
     * Executes a database query and validates the response.
     *
     * @param query      The database query to execute.
     * @param assertions The assertions to apply.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    public DatabaseServiceFluent queryAndValidate(final DbQuery<?> query, final Assertion... assertions) {
        final QueryResponse queryResponse = databaseService.query(query);
        quest.getStorage().sub(DB).put(query.enumImpl(), queryResponse);
        return validate(queryResponse, assertions);
    }

    /**
     * Performs validation with a custom assertion.
     *
     * @param assertion The assertion to execute.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    @Override
    public DatabaseServiceFluent validate(final Runnable assertion) {
        return (DatabaseServiceFluent) super.validate(assertion);
    }

    /**
     * Performs validation with a consumer of {@link SoftAssertions}.
     *
     * @param assertion The assertion to execute.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    @Override
    public DatabaseServiceFluent validate(final Consumer<SoftAssertions> assertion) {
        return (DatabaseServiceFluent) super.validate(assertion);
    }

    /**
     * Retries a database operation until the specified condition is met or the timeout expires.
     *
     * @param retryCondition The retry condition to evaluate.
     * @param maxWait        The maximum duration to wait.
     * @param retryInterval  The interval between retries.
     * @param <T>            The type used in the retry condition.
     * @return The current {@code DatabaseServiceFluent} instance for method chaining.
     */
    public <T> DatabaseServiceFluent retryUntil(final RetryCondition<T> retryCondition, final Duration maxWait,
                                                final Duration retryInterval) {
        return (DatabaseServiceFluent) super.retryUntil(retryCondition, maxWait, retryInterval, databaseService);
    }

    /**
     * Retrieves the underlying database service.
     *
     * @return The {@code DatabaseService} instance.
     */
    protected DatabaseService getDatabaseService() {
        return databaseService;
    }

}
