package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

/**
 * Defines the contract for validating database query responses.
 * <p>
 * This interface provides a mechanism for applying assertions on query results,
 * ensuring data integrity and expected outcomes in database interactions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface QueryResponseValidator {

    /**
     * Validates a database query response against the provided assertions.
     * <p>
     * This method processes the query result and applies the assertions to check
     * conditions such as row count, specific field values, and structural integrity.
     * </p>
     *
     * @param queryResponse The query response to validate.
     * @param assertions    The assertions used for validation.
     * @param <T>           The expected data type for assertion validation.
     * @return A list of assertion results indicating pass or failure for each assertion.
     */
    <T> List<AssertionResult<T>> validateQueryResponse(QueryResponse queryResponse, Assertion<?>... assertions);
}
