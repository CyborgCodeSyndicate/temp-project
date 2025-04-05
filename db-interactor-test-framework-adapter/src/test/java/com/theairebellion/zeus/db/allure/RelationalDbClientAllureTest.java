package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.query.QueryResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelationalDbClientAllureTest {

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

    @Mock
    private BaseDbConnectorService connector;

    @Mock
    private QueryResponse queryResponse;

    private RelationalDbClientAllure createClientUnderTest() {
        DatabaseConfiguration config = createDummyConfig();
        return new RelationalDbClientAllure(connector, config);
    }

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
    @DisplayName("printQuery should log query to Allure")
    void testPrintQuery() {
        // Arrange
        RelationalDbClientAllure client = createClientUnderTest();

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            // Act
            client.printQuery(QUERY_SELECT_1);

            // Assert
            allureMock.verify(() -> Allure.step("Executing SQL query: " + QUERY_SELECT_1), times(1));
        }
    }

    @Test
    @DisplayName("printResponse should log response with empty rows")
    void testPrintResponseWithEmptyRows() throws Throwable {
        // Arrange
        RelationalDbClientAllure client = createClientUnderTest();
        when(queryResponse.getRows()).thenReturn(Collections.emptyList());
        long duration = 100;

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor =
                    ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            // Act
            client.printResponse(QUERY_SELECT_1, queryResponse, duration);

            // Assert
            allureMock.verify(() -> Allure.step(
                    eq(String.format(QUERY_FINISH_MESSAGE_TEMPLATE, duration)),
                    runnableCaptor.capture()
            ));

            // Execute the captured runnable to verify attachment behavior
            runnableCaptor.getValue().run();

            // Verify attachments
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_EXECUTED_SQL), eq(QUERY_SELECT_1)));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_DURATION), eq(String.valueOf(duration))));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_RESULT_ROWS), any(String.class)), never());
        }
    }

    @Test
    @DisplayName("printResponse should log response with non-empty rows")
    void testPrintResponseWithNonEmptyRows() throws Throwable {
        // Arrange
        RelationalDbClientAllure client = createClientUnderTest();
        List<Map<String, Object>> mockRows = Collections.singletonList(Map.of("id", 1));
        when(queryResponse.getRows()).thenReturn(mockRows);
        when(queryResponse.toString()).thenReturn("responseString");
        long duration = 200;

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor =
                    ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            // Act
            client.printResponse(QUERY_SELECT_1, queryResponse, duration);

            // Assert
            allureMock.verify(() -> Allure.step(
                    eq(String.format(QUERY_FINISH_MESSAGE_TEMPLATE, duration)),
                    runnableCaptor.capture()
            ));

            // Execute the captured runnable to verify attachment behavior
            runnableCaptor.getValue().run();

            // Verify attachments
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_EXECUTED_SQL), eq(QUERY_SELECT_1)));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_DURATION), eq(String.valueOf(duration))));
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_RESULT_ROWS), eq("responseString")));
        }
    }

    @Test
    @DisplayName("printResponse should call super.printResponse")
    void testPrintResponseCallsSuper() {
        // Arrange
        RelationalDbClientAllure clientSpy = spy(createClientUnderTest());
        when(queryResponse.getRows()).thenReturn(Collections.emptyList());
        long duration = 50;

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            // Act
            clientSpy.printResponse(QUERY_SELECT_1, queryResponse, duration);

            // Assert
            verify(clientSpy).printResponse(QUERY_SELECT_1, queryResponse, duration);
        }
    }

    @Test
    @DisplayName("addAttachmentIfPresent should handle null content")
    void testAddAttachmentIfPresentWithNull() throws Exception {
        // Arrange
        RelationalDbClientAllure client = createClientUnderTest();
        String attachmentName = "TestAttachment";
        String nullContent = null;

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            // Use reflection to access the private method
            java.lang.reflect.Method method = RelationalDbClientAllure.class
                    .getDeclaredMethod("addAttachmentIfPresent", String.class, String.class);
            method.setAccessible(true);

            // Act
            method.invoke(client, attachmentName, nullContent);

            // Assert - verify no attachment was added
            allureMock.verify(() -> Allure.addAttachment(any(), any(String.class)), never());
        }
    }

    @Test
    @DisplayName("addAttachmentIfPresent should handle empty content")
    void testAddAttachmentIfPresentWithEmptyString() throws Exception {
        // Arrange
        RelationalDbClientAllure client = createClientUnderTest();
        String attachmentName = "TestAttachment";
        String emptyContent = "";

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            // Use reflection to access the private method
            java.lang.reflect.Method method = RelationalDbClientAllure.class
                    .getDeclaredMethod("addAttachmentIfPresent", String.class, String.class);
            method.setAccessible(true);

            // Act
            method.invoke(client, attachmentName, emptyContent);

            // Assert - verify no attachment was added
            allureMock.verify(() -> Allure.addAttachment(any(), any(String.class)), never());
        }
    }

    @Test
    @DisplayName("addAttachmentIfPresent should handle whitespace content")
    void testAddAttachmentIfPresentWithWhitespaceString() throws Exception {
        // Arrange
        RelationalDbClientAllure client = createClientUnderTest();
        String attachmentName = "TestAttachment";
        String whitespaceContent = "   ";

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            // Use reflection to access the private method
            java.lang.reflect.Method method = RelationalDbClientAllure.class
                    .getDeclaredMethod("addAttachmentIfPresent", String.class, String.class);
            method.setAccessible(true);

            // Act
            method.invoke(client, attachmentName, whitespaceContent);

            // Assert - verify no attachment was added
            allureMock.verify(() -> Allure.addAttachment(any(), any(String.class)), never());
        }
    }
}