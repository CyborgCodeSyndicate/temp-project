package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.exceptions.DatabaseOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelationalDbClientTest {

    private static final String SELECT_QUERY = "SELECT * FROM users";
    private static final String UPDATE_QUERY = "UPDATE users SET name='test'";
    private static final String INVALID_QUERY = "SELECT * FROM invalid_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String TEST_USER_NAME = "Test User";
    private static final int TEST_USER_ID = 1;
    private static final int UPDATED_ROWS_COUNT = 1;
    private static final String EXCEPTION_MESSAGE = "Error executing query";
    private static final String CONNECTION_ERROR = "Database connection error";

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
        var connection = mock(Connection.class);
        var preparedStatement = mock(PreparedStatement.class);
        var resultSet = mock(ResultSet.class);
        var metaData = mock(ResultSetMetaData.class);

        when(connector.getConnection(dbConfig)).thenReturn(connection);
        when(connection.prepareStatement(SELECT_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnName(1)).thenReturn(COLUMN_ID);
        when(metaData.getColumnName(2)).thenReturn(COLUMN_NAME);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenReturn(TEST_USER_ID);
        when(resultSet.getObject(2)).thenReturn(TEST_USER_NAME);

        var response = client.executeQuery(SELECT_QUERY);

        assertNotNull(response, "QueryResponse should not be null");
        assertEquals(1, response.getRows().size(), "Result set should contain one row");
        assertEquals(TEST_USER_ID, response.getRows().get(0).get(COLUMN_ID));
        assertEquals(TEST_USER_NAME, response.getRows().get(0).get(COLUMN_NAME));

        verify(connector).getConnection(dbConfig);
        verify(connection).close();
    }

    @Test
    void testExecuteQuery_Update_ShouldReturnQueryResponse() throws Exception {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);

        when(connector.getConnection(dbConfig)).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(UPDATE_QUERY)).thenReturn(UPDATED_ROWS_COUNT);

        var response = client.executeQuery(UPDATE_QUERY);

        assertNotNull(response, "QueryResponse should not be null");
        assertEquals(UPDATED_ROWS_COUNT, response.getRows().get(0).get("updatedRows"));
        verify(connector).getConnection(dbConfig);
        verify(connection).close();
    }

    @Test
    void testExecuteQuery_ShouldThrowDatabaseOperationException_OnSQLException() {
        doAnswer(invocation -> {
            throw new SQLException(CONNECTION_ERROR);
        }).when(connector).getConnection(eq(dbConfig));

        var exception = assertThrows(
                DatabaseOperationException.class,
                () -> client.executeQuery(INVALID_QUERY)
        );

        assertNotNull(exception, "Exception should not be null");
        assertTrue(exception.getMessage().contains(EXCEPTION_MESSAGE), "Exception message should indicate query failure");
        assertInstanceOf(SQLException.class, exception.getCause(), "The root cause should be SQLException");
    }
}