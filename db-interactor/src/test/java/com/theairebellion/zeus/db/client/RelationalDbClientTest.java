package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.exceptions.DatabaseOperationException;
import com.theairebellion.zeus.db.query.QueryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelationalDbClientTest {

    private BaseDbConnectorService connector;
    private DatabaseConfiguration dbConfig;
    private RelationalDbClient client;

    @BeforeEach
    void setUp() {
        connector = mock(BaseDbConnectorService.class);
        dbConfig = mock(DatabaseConfiguration.class);
        client = new RelationalDbClient(connector, dbConfig);
    }

    @Test
    void testExecuteQuery_Select_ShouldReturnQueryResponse() throws Exception {
        String query = "SELECT * FROM users";
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(connector.getConnection(dbConfig)).thenReturn(connection);
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnName(1)).thenReturn("id");
        when(metaData.getColumnName(2)).thenReturn("name");
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenReturn(1);
        when(resultSet.getObject(2)).thenReturn("Test User");

        QueryResponse response = client.executeQuery(query);

        assertNotNull(response, "Expected a non-null QueryResponse");
        assertEquals(1, response.getRows().size(), "Expected one row in the result set");
        assertEquals(1, response.getRows().get(0).get("id"));
        assertEquals("Test User", response.getRows().get(0).get("name"));

        verify(connector).getConnection(dbConfig);
        verify(connection).close();
    }

    @Test
    void testExecuteQuery_Update_ShouldReturnQueryResponse() throws Exception {
        String query = "UPDATE users SET name='test'";
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);

        when(connector.getConnection(dbConfig)).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(query)).thenReturn(1);

        QueryResponse response = client.executeQuery(query);

        assertNotNull(response, "Expected a non-null QueryResponse");
        assertEquals(1, response.getRows().get(0).get("updatedRows"), "Expected 1 row to be updated");
        verify(connector).getConnection(dbConfig);
        verify(connection).close();
    }

    @Test
    void testExecuteQuery_ShouldThrowDatabaseOperationException_OnSQLException() {
        // Setup
        String query = "SELECT * FROM invalid_table";

        // Mock the connector to simulate a SQLException
        doAnswer(invocation -> {
            throw new SQLException("Database connection error");
        }).when(connector).getConnection(eq(dbConfig));

        // Act and Assert
        DatabaseOperationException exception = assertThrows(
                DatabaseOperationException.class,
                () -> client.executeQuery(query),
                "Expected DatabaseOperationException for query failure"
        );

        // Assert Exception Details
        assertNotNull(exception, "Exception should not be null");
        assertTrue(exception.getMessage().contains("Error executing query"),
                "Exception message should mention query failure");
        assertInstanceOf(SQLException.class, exception.getCause(), "The root cause of DatabaseOperationException should be SQLException");
    }
}