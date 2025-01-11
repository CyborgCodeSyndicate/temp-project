package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.log.LogDB;
import com.theairebellion.zeus.db.query.QueryResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationalDbClient implements DbClient {

    private final BaseDbConnectorService connector;
    private final DatabaseConfiguration dbConfig;


    public RelationalDbClient(BaseDbConnectorService connector, DatabaseConfiguration dbConfig) {
        this.connector = connector;
        this.dbConfig = dbConfig;
    }


    @Override
    public QueryResponse executeQuery(String query) {
        Connection connection = connector.getConnection(dbConfig);
        return executeQueryAndReturn(connection, query);
    }


    protected void printQuery(String query) {
        LogDB.step("Executing database query: {}", query);
    }


    protected void printResponse(String query, QueryResponse response, long duration) {
        List<Map<String, Object>> rows = response.getRows();
        LogDB.step("Query '{}' executed in {}ms, row count: {}", query, duration, rows.size());
        LogDB.extended("Query response data: {}", rows);
    }


    private QueryResponse executeQueryAndReturn(Connection connection, String sqlQuery) {
        printQuery(sqlQuery);
        List<Map<String, Object>> resultList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        long startTime = System.currentTimeMillis();

        try {
            if (sqlQuery.trim().toLowerCase().startsWith("select")) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                resultSet = preparedStatement.executeQuery();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = resultSet.getObject(i);
                        row.put(columnName, columnValue);
                    }
                    resultList.add(row);
                }
            } else {
                statement = connection.createStatement();
                int updatedCount = statement.executeUpdate(sqlQuery);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + sqlQuery, e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException ignore) {
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ignore) {
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ignore) {
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        QueryResponse queryResponse = new QueryResponse(resultList);
        printResponse(sqlQuery, queryResponse, duration);
        return queryResponse;
    }

}
