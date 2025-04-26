package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.exceptions.DatabaseOperationException;
import com.theairebellion.zeus.db.log.LogDb;
import com.theairebellion.zeus.db.query.QueryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private BaseDbConnectorService connector;

    @Mock
    private DatabaseConfiguration dbConfig;

    @Spy
    @InjectMocks
    private RelationalDbClient client;

    @Nested
    @DisplayName("Select Query Tests")
    class SelectQueryTests {

        @Test
        @DisplayName("Should return query response with result data when executing select query")
        void testExecuteQuery_Select_ShouldReturnQueryResponse() throws Exception {
            // Given
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

            // When
            doNothing().when(client).printQuery(any());
            doNothing().when(client).printResponse(any(), any(), anyLong());

            var response = client.executeQuery(SELECT_QUERY);

            // Then
            assertNotNull(response, "QueryResponse should not be null");
            assertEquals(1, response.getRows().size(), "Result set should contain one row");
            assertEquals(TEST_USER_ID, response.getRows().get(0).get(COLUMN_ID));
            assertEquals(TEST_USER_NAME, response.getRows().get(0).get(COLUMN_NAME));

            verify(connector).getConnection(dbConfig);
            verify(client).printQuery(SELECT_QUERY);
            verify(client).printResponse(eq(SELECT_QUERY), any(QueryResponse.class), anyLong());
        }

        @Test
        @DisplayName("Should handle empty result set when executing select query")
        void testExecuteQuery_Select_EmptyResultSet() throws Exception {
            // Given
            var connection = mock(Connection.class);
            var preparedStatement = mock(PreparedStatement.class);
            var resultSet = mock(ResultSet.class);
            var metaData = mock(ResultSetMetaData.class);

            when(connector.getConnection(dbConfig)).thenReturn(connection);
            when(connection.prepareStatement(SELECT_QUERY)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.getMetaData()).thenReturn(metaData);
            when(metaData.getColumnCount()).thenReturn(2);
            when(resultSet.next()).thenReturn(false); // No results

            // When
            doNothing().when(client).printQuery(any());
            doNothing().when(client).printResponse(any(), any(), anyLong());

            var response = client.executeQuery(SELECT_QUERY);

            // Then
            assertNotNull(response, "QueryResponse should not be null");
            assertEquals(0, response.getRows().size(), "Result set should be empty");

            verify(connector).getConnection(dbConfig);
            verify(client).printQuery(SELECT_QUERY);
            verify(client).printResponse(eq(SELECT_QUERY), any(QueryResponse.class), anyLong());
        }
    }

    @Nested
    @DisplayName("Update Query Tests")
    class UpdateQueryTests {

        @Test
        @DisplayName("Should return query response with updated rows count when executing update query")
        void testExecuteQuery_Update_ShouldReturnQueryResponse() throws Exception {
            // Given
            var connection = mock(Connection.class);
            var statement = mock(Statement.class);

            when(connector.getConnection(dbConfig)).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeUpdate(UPDATE_QUERY)).thenReturn(UPDATED_ROWS_COUNT);

            // When
            doNothing().when(client).printQuery(any());
            var response = client.executeQuery(UPDATE_QUERY);

            // Then
            assertNotNull(response, "QueryResponse should not be null");
            assertEquals(1, response.getRows().size(), "Result should contain one row");
            assertEquals(UPDATED_ROWS_COUNT, response.getRows().get(0).get("updatedRows"));

            verify(connector).getConnection(dbConfig);
            verify(client).printQuery(UPDATE_QUERY);
        }

        @Test
        @DisplayName("Should handle zero affected rows when executing update query")
        void testExecuteQuery_Update_ZeroRowsAffected() throws Exception {
            // Given
            var connection = mock(Connection.class);
            var statement = mock(Statement.class);

            when(connector.getConnection(dbConfig)).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeUpdate(UPDATE_QUERY)).thenReturn(0); // No rows updated

            // When
            doNothing().when(client).printQuery(any());
            var response = client.executeQuery(UPDATE_QUERY);

            // Then
            assertNotNull(response, "QueryResponse should not be null");
            assertEquals(1, response.getRows().size(), "Result should contain one row");
            assertEquals(0, response.getRows().get(0).get("updatedRows"));

            verify(connector).getConnection(dbConfig);
            verify(client).printQuery(UPDATE_QUERY);
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should handle connection exceptions properly")
        void testExecuteQuery_CustomSubclass_ShouldHandleConnectionError() {

            BaseDbConnectorService errorConnector = new BaseDbConnectorService() {
                @Override
                public Connection getConnection(DatabaseConfiguration config) {
                    throw new IllegalStateException("Connection failed");
                }
            };

            RelationalDbClient clientToTest = new RelationalDbClient(errorConnector, dbConfig);

            var exception = assertThrows(
                    IllegalStateException.class,
                    () -> clientToTest.executeQuery(INVALID_QUERY)
            );

            assertNotNull(exception, "Exception should not be null");
            assertEquals("Connection failed", exception.getMessage(),
                    "Exception message should match the one we set");
        }

        @Test
        @DisplayName("Should throw DatabaseOperationException when SQL exception occurs during select query execution")
        void testExecuteQuery_ShouldThrowDatabaseOperationException_OnSelectQueryError() throws Exception {
            // Given
            var connection = mock(Connection.class);
            var preparedStatement = mock(PreparedStatement.class);

            when(connector.getConnection(dbConfig)).thenReturn(connection);
            when(connection.prepareStatement(SELECT_QUERY)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException("Query execution error"));

            // When & Then
            doNothing().when(client).printQuery(any());
            var exception = assertThrows(
                    DatabaseOperationException.class,
                    () -> client.executeQuery(SELECT_QUERY)
            );

            assertNotNull(exception, "Exception should not be null");
            assertTrue(exception.getMessage().contains(EXCEPTION_MESSAGE),
                    "Exception message should indicate query failure");

            verify(connector).getConnection(dbConfig);
            verify(client).printQuery(SELECT_QUERY);
        }

        @Test
        @DisplayName("Should throw DatabaseOperationException when SQL exception occurs during update query execution")
        void testExecuteQuery_ShouldThrowDatabaseOperationException_OnUpdateQueryError() throws Exception {
            // Given
            var connection = mock(Connection.class);
            var statement = mock(Statement.class);

            when(connector.getConnection(dbConfig)).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeUpdate(UPDATE_QUERY)).thenThrow(new SQLException("Update execution error"));

            // When & Then
            doNothing().when(client).printQuery(any());
            var exception = assertThrows(
                    DatabaseOperationException.class,
                    () -> client.executeQuery(UPDATE_QUERY)
            );

            assertNotNull(exception, "Exception should not be null");
            assertTrue(exception.getMessage().contains(EXCEPTION_MESSAGE),
                    "Exception message should indicate query failure");

            verify(connector).getConnection(dbConfig);
            verify(client).printQuery(UPDATE_QUERY);
        }
    }

    @Nested
    @DisplayName("Helper Method Tests")
    class HelperMethodTests {

        @Test
        @DisplayName("Should call printQuery and printResponse methods")
        void testPrintMethods() throws Exception {
            // Given
            var connection = mock(Connection.class);
            var preparedStatement = mock(PreparedStatement.class);
            var resultSet = mock(ResultSet.class);
            var metaData = mock(ResultSetMetaData.class);

            when(connector.getConnection(dbConfig)).thenReturn(connection);
            when(connection.prepareStatement(SELECT_QUERY)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.getMetaData()).thenReturn(metaData);
            when(metaData.getColumnCount()).thenReturn(0);
            when(resultSet.next()).thenReturn(false);

            // Using real methods instead of spying
            var realClient = new RelationalDbClient(connector, dbConfig) {
                @Override
                protected void printQuery(String query) {
                    // Do nothing, but we can verify it was called
                    super.printQuery(query);
                }

                @Override
                protected void printResponse(String query, QueryResponse response, long duration) {
                    // Do nothing, but we can verify it was called
                    super.printResponse(query, response, duration);
                }
            };

            var spyClient = spy(realClient);

            // When
            spyClient.executeQuery(SELECT_QUERY);

            // Then
            verify(spyClient).printQuery(SELECT_QUERY);
            verify(spyClient).printResponse(eq(SELECT_QUERY), any(QueryResponse.class), anyLong());
        }
    }

    @Nested
    @DisplayName("Slow-Query Warning Tests")
    class SlowQueryWarningTests {

        @Test
        @DisplayName("executeSelectQuery should log a warning when duration > 1000ms")
        void selectQuery_slowness_triggersWarning() throws Exception {
            // 1) Prepare a fake Connection/PreparedStatement/ResultSet chain:
            Connection connection          = mock(Connection.class);
            PreparedStatement ps           = mock(PreparedStatement.class);
            ResultSet rs                   = mock(ResultSet.class);
            ResultSetMetaData meta         = mock(ResultSetMetaData.class);

            when(connection.prepareStatement(SELECT_QUERY)).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.getMetaData()).thenReturn(meta);
            when(meta.getColumnCount()).thenReturn(0);
            when(rs.next()).thenReturn(false);

            // 2) Grab the private method
            Method m = RelationalDbClient.class
                    .getDeclaredMethod("executeSelectQuery", Connection.class, String.class, long.class);
            m.setAccessible(true);

            // 3) Run it as if the query took 2 seconds
            try (MockedStatic<LogDb> logs = mockStatic(LogDb.class)) {
                long start = System.currentTimeMillis() - 2_000;
                QueryResponse qr = (QueryResponse) m.invoke(client, connection, SELECT_QUERY, start);
                assertNotNull(qr);

                // 4) Verify the slow-warning
                logs.verify(() -> LogDb.warn(
                        eq("Slow query detected: '{}' took {}ms"),
                        eq(SELECT_QUERY),
                        anyLong()
                ), times(1));
            }
        }

        @Test
        @DisplayName("executeUpdateQuery should log a warning when duration > 1000ms")
        void updateQuery_slowness_triggersWarning() throws Exception {
            Connection conn    = mock(Connection.class);
            Statement stmt     = mock(Statement.class);

            when(conn.createStatement()).thenReturn(stmt);
            when(stmt.executeUpdate(UPDATE_QUERY)).thenReturn(0);

            Method m = RelationalDbClient.class
                    .getDeclaredMethod("executeUpdateQuery", Connection.class, String.class, long.class);
            m.setAccessible(true);

            try (MockedStatic<LogDb> logs = mockStatic(LogDb.class)) {
                long start = System.currentTimeMillis() - 2_000;
                QueryResponse qr = (QueryResponse) m.invoke(client, conn, UPDATE_QUERY, start);
                assertNotNull(qr);

                logs.verify(() -> LogDb.warn(
                        eq("Slow update query detected: '{}' took {}ms"),
                        eq(UPDATE_QUERY),
                        anyLong()
                ), times(1));
            }
        }
    }
}