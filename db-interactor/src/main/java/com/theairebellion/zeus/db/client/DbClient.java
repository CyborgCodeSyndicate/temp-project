package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.query.QueryResponse;

/**
 * Defines a contract for executing database queries.
 * <p>
 * Implementing classes provide mechanisms for executing SQL queries
 * and returning structured query responses.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface DbClient {

    /**
     * Executes the given SQL query and returns the query response.
     *
     * @param query The SQL query to execute.
     * @return The response containing query results.
     */
    QueryResponse executeQuery(String query);
}
