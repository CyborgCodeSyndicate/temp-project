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

    private DatabaseConfiguration createDummyConfig() {
        return DatabaseConfiguration.builder()
                .dbType(null)
                .host("localhost")
                .port(1234)
                .database("testdb")
                .dbUser("user")
                .dbPassword("pass")
                .build();
    }

    @Test
    void testPrintQuery() {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        DatabaseConfiguration config = createDummyConfig();
        RelationalDbClientAllure client = new RelationalDbClientAllure(connector, config);
        String query = "SELECT 1";
        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            client.printQuery(query);
            // Verify that Allure.step was called with the expected message.
            allureMock.verify(() -> Allure.step("Executing SQL query: SELECT 1"));
        }
    }

    @Test
    void testPrintResponseWithEmptyRows() throws Throwable {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        DatabaseConfiguration config = createDummyConfig();
        RelationalDbClientAllure client = new RelationalDbClientAllure(connector, config);
        QueryResponse response = mock(QueryResponse.class);

        // Use type-safe empty list
        when(response.getRows()).thenReturn(Collections.emptyList());

        long duration = 100;
        String query = "SELECT 1";

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor =
                    ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            client.printResponse(query, response, duration);

            allureMock.verify(() -> Allure.step(
                    eq("Finished executing query in 100ms"),
                    runnableCaptor.capture()
            ));

            runnableCaptor.getValue().run();

            // Resolve ambiguity with explicit String type
            allureMock.verify(() -> Allure.addAttachment(eq("Executed SQL"), eq(query)));
            allureMock.verify(() -> Allure.addAttachment(eq("Duration (ms)"), eq("100")));
            allureMock.verify(() -> Allure.addAttachment(eq("Result Rows"), any(String.class)), never());
        }
    }

    @Test
    void testPrintResponseWithNonEmptyRows() throws Throwable {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        DatabaseConfiguration config = createDummyConfig();
        RelationalDbClientAllure client = new RelationalDbClientAllure(connector, config);
        QueryResponse response = mock(QueryResponse.class);

        // Create properly typed mock list
        List<Map<String, Object>> mockRows = Collections.singletonList(mock(Map.class));
        when(response.getRows()).thenReturn(mockRows);
        when(response.toString()).thenReturn("responseString");

        long duration = 200;
        String query = "SELECT * FROM table";

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor =
                    ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            client.printResponse(query, response, duration);

            allureMock.verify(() -> Allure.step(
                    eq("Finished executing query in 200ms"),
                    runnableCaptor.capture()
            ));

            runnableCaptor.getValue().run();

            allureMock.verify(() -> Allure.addAttachment(eq("Executed SQL"), eq(query)));
            allureMock.verify(() -> Allure.addAttachment(eq("Duration (ms)"), eq("200")));
            allureMock.verify(() -> Allure.addAttachment(eq("Result Rows"), eq("responseString")));
        }
    }
}
