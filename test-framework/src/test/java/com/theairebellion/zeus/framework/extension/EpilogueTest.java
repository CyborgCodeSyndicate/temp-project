package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpilogueTest {

    public static final String TEST_DISPLAY_NAME = "TestDisplayName";
    public static final String ERROR = "Error";
    public static final String TEST_NAME = "testName";
    public static final String EMPTY_LOGS = "emptyLogs";
    public static final String LOG_FILE_NAME = "logFileName";
    public static final String NO_MATCHES = "NoMatches";
    public static final String SOME_TEST = "SomeTest";
    public static final String THISPATHDOESNOTEXIST = "thispathdoesnotexist";
    public static final String IO_EXCEPTION_TEST = "IOExceptionTest";

    private Epilogue epilogue;
    private ExtensionContext extensionContext;
    private ExtensionContext.Store store;

    @BeforeEach
    void setUp() {
        epilogue = new Epilogue();
        extensionContext = mock(ExtensionContext.class);
        store = mock(ExtensionContext.Store.class);
        lenient().when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
    }

    @Test
    void testAfterTestExecutionSuccess() {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis() - 2000);
        when(extensionContext.getDisplayName()).thenReturn(TEST_DISPLAY_NAME);
        when(extensionContext.getExecutionException()).thenReturn(Optional.empty());
        try (MockedStatic<ThreadContext> ignored1 = mockStatic(ThreadContext.class);
             MockedStatic<LogTest> ignored2 = mockStatic(LogTest.class)) {
            epilogue.afterTestExecution(extensionContext);
        }
    }

    @Test
    void testAfterTestExecutionFailure() {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis() - 3000);
        when(extensionContext.getDisplayName()).thenReturn(TEST_DISPLAY_NAME);
        when(extensionContext.getExecutionException()).thenReturn(Optional.of(new RuntimeException(ERROR)));
        try (MockedStatic<ThreadContext> ignored1 = mockStatic(ThreadContext.class);
             MockedStatic<LogTest> ignored2 = mockStatic(LogTest.class)) {
            epilogue.afterTestExecution(extensionContext);
        }
    }

    @Test
    void testAttachFilteredLogsNullName() {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
        try (MockedStatic<ThreadContext> ignored1 = mockStatic(ThreadContext.class);
             MockedStatic<Allure> ignored2 = mockStatic(Allure.class)) {
            ignored1.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(null);
            epilogue.afterTestExecution(extensionContext);
        }
    }

    @Test
    void testAttachFilteredLogsEmptyName() {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
        try (MockedStatic<ThreadContext> ignored1 = mockStatic(ThreadContext.class);
             MockedStatic<Allure> ignored2 = mockStatic(Allure.class)) {
            ignored1.when(() -> ThreadContext.get(TEST_NAME)).thenReturn("");
            epilogue.afterTestExecution(extensionContext);
        }
    }

    @Test
    void testAttachFilteredLogsNoLogsFound() throws IOException {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
        var tempFile = File.createTempFile(EMPTY_LOGS, ".log");
        tempFile.deleteOnExit();
        System.setProperty(LOG_FILE_NAME, tempFile.getAbsolutePath());
        try (MockedStatic<ThreadContext> ignored1 = mockStatic(ThreadContext.class);
             MockedStatic<Allure> ignored2 = mockStatic(Allure.class)) {
            ignored1.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(NO_MATCHES);
            epilogue.afterTestExecution(extensionContext);
        }
    }

    @Test
    void testAttachFilteredLogsIOException() {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
        System.setProperty(LOG_FILE_NAME, File.separator + THISPATHDOESNOTEXIST + File.separator + "fake.log");
        try (MockedStatic<ThreadContext> ignored1 = mockStatic(ThreadContext.class);
             MockedStatic<Allure> ignored2 = mockStatic(Allure.class)) {
            ignored1.when(() -> ThreadContext.get(TEST_NAME)).thenReturn(IO_EXCEPTION_TEST);
            epilogue.afterTestExecution(extensionContext);
        }
    }

    @Test
    void testStoreNullStartTimeThrowsNPE() {
        when(store.get(StoreKeys.START_TIME, long.class)).thenReturn(null);
        try (MockedStatic<ThreadContext> ignored = mockStatic(ThreadContext.class)) {
            assertThrows(NullPointerException.class, () -> epilogue.afterTestExecution(extensionContext));
        }
    }

    @Test
    void testNoExceptionWhenStoreNotNull() {
        when(store.get(START_TIME, long.class)).thenReturn(System.currentTimeMillis());
        when(extensionContext.getExecutionException()).thenReturn(Optional.empty());
        when(extensionContext.getDisplayName()).thenReturn(SOME_TEST);
        try (MockedStatic<ThreadContext> ignored = mockStatic(ThreadContext.class)) {
            assertDoesNotThrow(() -> epilogue.afterTestExecution(extensionContext));
        }
    }
}