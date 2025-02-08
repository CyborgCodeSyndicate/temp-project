package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.query.QueryResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RelationalDbClientAllureTest {

    private static final String QUERY_SELECT_1 = "SELECT 1";
    private static final String QUERY_FINISH_MESSAGE_TEMPLATE = "Finished executing query in %dms";
    private static final String ATTACHMENT_EXECUTED_SQL = "Executed SQL";
    private static final String ATTACHMENT_DURATION = "Duration (ms)";
    private static final String ATTACHMENT_RESULT_ROWS = "Result Rows";
    private static final String DUMMY_DB_HOST = "localhost";
    private static final String DUMMY_DB_USER = "user";
    private static final String DUMMY_DB_PASS = "pass";
    private static final int DUMMY_DB_PORT = 1234;
    private static final String DUMMY_DB_NAME = "testdb";

    private DatabaseConfiguration createDummyConfig() {
        return DatabaseConfiguration.builder()
                .dbType(null)
                .host(DUMMY_DB_HOST)
                .port(DUMMY_DB_PORT)
                .database(DUMMY_DB_NAME)
                .dbUser(DUMMY_DB_USER)
                .dbPassword(DUMMY_DB_PASS)
                .build();
    }

    @Test
    void testPrintQuery() {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        DatabaseConfiguration config = createDummyConfig();
        RelationalDbClientAllure client = new RelationalDbClientAllure(connector, config);

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            client.printQuery(QUERY_SELECT_1);

            allureMock.verify(() -> Allure.step("Executing SQL query: " + QUERY_SELECT_1));
        }
    }

    @Test
    void testPrintResponseWithEmptyRows() throws Throwable {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        DatabaseConfiguration config = createDummyConfig();
        RelationalDbClientAllure client = new RelationalDbClientAllure(connector, config);
        QueryResponse response = mock(QueryResponse.class);

        when(response.getRows()).thenReturn(Collections.emptyList());
        long duration = 100;

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor = ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            client.printResponse(QUERY_SELECT_1, response, duration);

            allureMock.verify(() -> Allure.step(
                    eq(QUERY_FINISH_MESSAGE_TEMPLATE.formatted(duration)),
                    runnableCaptor.capture()
            ));

            runnableCaptor.getValue().run();

            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_EXECUTED_SQL), eq(QUERY_SELECT_1)));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_DURATION), eq(String.valueOf(duration))));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_RESULT_ROWS), any(String.class)), never());
        }
    }

    @Test
    void testPrintResponseWithNonEmptyRows() throws Throwable {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        DatabaseConfiguration config = createDummyConfig();
        RelationalDbClientAllure client = new RelationalDbClientAllure(connector, config);
        QueryResponse response = mock(QueryResponse.class);

        List<Map<String, Object>> mockRows = Collections.singletonList(mock(Map.class));
        when(response.getRows()).thenReturn(mockRows);
        when(response.toString()).thenReturn("responseString");
        long duration = 200;

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor = ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            client.printResponse(QUERY_SELECT_1, response, duration);

            allureMock.verify(() -> Allure.step(
                    eq(QUERY_FINISH_MESSAGE_TEMPLATE.formatted(duration)),
                    runnableCaptor.capture()
            ));

            runnableCaptor.getValue().run();

            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_EXECUTED_SQL), eq(QUERY_SELECT_1)));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_DURATION), eq(String.valueOf(duration))));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_RESULT_ROWS), eq("responseString")));
        }
    }
}