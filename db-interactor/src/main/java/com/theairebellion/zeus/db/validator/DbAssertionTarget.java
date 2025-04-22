package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

/**
 * Defines assertion targets for database query validation.
 * <p>
 * This enum categorizes different aspects of a database query result that can be validated,
 * ensuring structured assertions for database testing.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public enum DbAssertionTarget implements AssertionTarget<DbAssertionTarget> {

    /** Validates the query result content. */
    QUERY_RESULT,

    /** Validates the number of rows returned by the query. */
    NUMBER_ROWS,

    /** Validates the column structure of the result set. */
    COLUMNS;

    /**
     * Retrieves the specific assertion target.
     *
     * @return The enum representing the assertion target.
     */
    @Override
    public DbAssertionTarget target() {
        return this;
    }
}
