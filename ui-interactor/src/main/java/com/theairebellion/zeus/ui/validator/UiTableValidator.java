package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

/**
 * Interface for validating tables in UI automation.
 * <p>
 * This interface defines a method for applying multiple assertions
 * to a table object and returning validation results.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface UiTableValidator {

    /**
     * Validates a given table object against a set of assertions.
     * <p>
     * This method applies the specified assertions to the table and returns
     * a list of validation results indicating whether the assertions passed or failed.
     * </p>
     *
     * @param object     The table object to be validated.
     * @param assertions The assertions to apply.
     * @param <T>        The type of the expected assertion results.
     * @return A list of {@link AssertionResult} containing the validation outcomes.
     */
    <T> List<AssertionResult<T>> validateTable(Object object, Assertion<?>... assertions);
}
