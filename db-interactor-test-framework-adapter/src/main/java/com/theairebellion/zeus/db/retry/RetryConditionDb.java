package com.theairebellion.zeus.db.retry;

import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.framework.retry.RetryConditionImpl;

import java.util.function.Predicate;

/**
 * Provides retry conditions for database queries.
 * <p>
 * This class defines conditions for retrying operations based on database query results,
 * allowing tests to wait until expected data is available.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class RetryConditionDb {

    private RetryConditionDb() {
    }

    /**
     * Creates a retry condition that waits until a query returns at least one row.
     * <p>
     * This condition is useful for ensuring that data has been inserted before proceeding
     * with further test steps.
     * </p>
     *
     * @param query The database query to execute.
     * @return A {@code RetryCondition} that evaluates to {@code true} when the query returns rows.
     */
    public static RetryCondition<Boolean> queryReturnsRows(DbQuery<?> query) {
        return new RetryConditionImpl<>(
                service -> {
                    DatabaseService databaseService = (DatabaseService) service;
                    QueryResponse queryResponse = databaseService.query(query);
                    return !queryResponse.getRows().isEmpty();
                }, Predicate.isEqual(true)
        );
    }

    /**
     * Creates a retry condition that waits until a specific field in the query result matches a value.
     * <p>
     * This condition is useful for waiting until a database update or insertion has propagated.
     * </p>
     *
     * @param query    The database query to execute.
     * @param jsonPath The JSONPath expression to extract the value from the query result.
     * @param value    The expected value to match.
     * @return A {@code RetryCondition} that evaluates to {@code true} when the field matches the expected value.
     */
    public static RetryCondition<Object> queryReturnsValueForField(DbQuery<?> query, String jsonPath, Object value) {
        return new RetryConditionImpl<>(
                service -> {
                    DatabaseService databaseService = (DatabaseService) service;
                    return databaseService.query(query, jsonPath, value.getClass());
                }, object -> object.equals(value)
        );
    }

}
