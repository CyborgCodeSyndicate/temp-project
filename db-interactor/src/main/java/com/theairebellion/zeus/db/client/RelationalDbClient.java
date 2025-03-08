package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.exceptions.DatabaseOperationException;
import com.theairebellion.zeus.db.log.LogDb;
import com.theairebellion.zeus.db.query.QueryResponse;

import java.sql.*;
import java.util.*;

/**
 * Implements a database client for executing SQL queries on relational databases.
 * <p>
 * This class handles database interactions, including executing queries and processing
 * results for both SELECT and UPDATE statements.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class RelationalDbClient implements DbClient {

    private final BaseDbConnectorService connector;
    private final DatabaseConfiguration dbConfig;

    /**
     * Constructs a new {@code RelationalDbClient} with the specified connector and configuration.
     *
     * @param connector The database connection service.
     * @param dbConfig  The database configuration details.
     */
    public RelationalDbClient(BaseDbConnectorService connector, DatabaseConfiguration dbConfig) {
        this.connector = connector;
        this.dbConfig = dbConfig;
    }

    /**
     * Executes a SQL query and processes the result.
     *
     * @param query The SQL query to execute.
     * @return The {@code QueryResponse} containing the query results.
     * @throws DatabaseOperationException If the query execution fails.
     */
    @Override
    public QueryResponse executeQuery(String query) {
        try (Connection connection = connector.getConnection(dbConfig)) {
            return executeAndProcessQuery(connection, query);
        } catch (SQLException e) {
            LogDb.error("Failed to execute query: {}", query, e);
            throw new DatabaseOperationException("Error executing query: " + query, e);
        }
    }

    private QueryResponse executeAndProcessQuery(Connection connection, String query) throws SQLException {
        printQuery(query);
        long startTime = System.currentTimeMillis();

        if (query.trim().toLowerCase().startsWith("select")) {
            return executeSelectQuery(connection, query, startTime);
        } else {
            return executeUpdateQuery(connection, query, startTime);
        }
    }

    private QueryResponse executeSelectQuery(Connection connection, String query, long startTime) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(row);
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        QueryResponse response = new QueryResponse(resultList);
        printResponse(query, response, duration);
        return response;
    }

    private QueryResponse executeUpdateQuery(Connection connection, String query, long startTime) throws SQLException {
        List<Map<String, Object>> resultList = Collections.singletonList(
                Collections.singletonMap("updatedRows", executeUpdate(connection, query))
        );

        long duration = System.currentTimeMillis() - startTime;
        QueryResponse response = new QueryResponse(resultList);
        LogDb.step("Update query '{}' executed in {}ms, updated rows count: {}", query, duration, resultList.get(0).get("updatedRows"));
        return response;
    }

    private int executeUpdate(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    /**
     * Logs the executed query.
     *
     * @param query The SQL query being executed.
     */
    protected void printQuery(String query) {
        LogDb.step("Executing database query: {}", query);
    }

    /**
     * Logs the query response details.
     *
     * @param query    The executed SQL query.
     * @param response The query response containing results.
     * @param duration The execution duration in milliseconds.
     */
    protected void printResponse(String query, QueryResponse response, long duration) {
        LogDb.step("Query '{}' executed in {}ms, result count: {}", query, duration, response.getRows().size());
        LogDb.extended("Query response data: {}", response.getRows());
    }

}
