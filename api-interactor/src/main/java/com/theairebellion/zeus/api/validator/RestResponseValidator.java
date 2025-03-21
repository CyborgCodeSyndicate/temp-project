package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.response.Response;

import java.util.List;

/**
 * Defines a contract for validating API responses.
 * <p>
 * Implementations of this interface validate HTTP responses against a set of assertions,
 * ensuring the response meets expected criteria.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface RestResponseValidator {

    /**
     * Validates an API response against the provided assertions.
     * <p>
     * Implementations extract relevant data from the response and evaluate it using the assertions.
     * </p>
     *
     * @param response   The API response to validate.
     * @param assertions The assertions used to verify response correctness.
     * @param <T>        The expected data type for assertion validation.
     * @return A list of assertion results, indicating pass or failure for each assertion.
     * @throws IllegalArgumentException If the response or assertions are null.
     */
    <T> List<AssertionResult<T>> validateResponse(Response response, Assertion<?>... assertions);

}
