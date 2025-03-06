package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Prologue Extension Tests")
class PrologueTest {

    private static final String TEST_NAME = "testName";
    private static final String DEFAULT_DISPLAY_NAME = "DefaultDisplayName";
    private static final String TEST_DISPLAY_NAME = "TestDisplayName";

    @Nested
    @DisplayName("Test Execution Setup")
    class TestExecutionSetup {

        @Test
        @DisplayName("Should set thread context and store start time when class and method are available")
        void beforeTestExecution_SetsThreadContextAndStoresStartTime() throws Exception {
            // Arrange
            Prologue prologue = new Prologue();
            ExtensionContext context = mock(ExtensionContext.class);
            ExtensionContext.Store store = mock(ExtensionContext.Store.class);
            when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

            Class<?> testClass = String.class;
            Method testMethod = String.class.getMethod("toString");
            when(context.getTestClass()).thenReturn(Optional.of(testClass));
            when(context.getTestMethod()).thenReturn(Optional.of(testMethod));
            when(context.getDisplayName()).thenReturn(TEST_DISPLAY_NAME);

            // Act & Assert
            try (MockedStatic<ThreadContext> threadContext = mockStatic(ThreadContext.class);
                 MockedStatic<LogTest> logTest = mockStatic(LogTest.class)) {
                prologue.beforeTestExecution(context);

                // Verify thread context is set with class.method format
                threadContext.verify(() ->
                        ThreadContext.put(eq(TEST_NAME), eq("String.toString")));

                // Verify start time is stored
                verify(store).put(eq(START_TIME), anyLong());

                // Verify quest beginning is logged
                logTest.verify(() ->
                        LogTest.info(eq("The quest: '{}' has begun."), eq(TEST_DISPLAY_NAME)));
            }
        }

        @Test
        @DisplayName("Should use unknown values when class and method are not available")
        void beforeTestExecution_UsesUnknownValuesWhenAbsent() {
            // Arrange
            Prologue prologue = new Prologue();
            ExtensionContext context = mock(ExtensionContext.class);
            ExtensionContext.Store store = mock(ExtensionContext.Store.class);
            when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

            // No class or method available
            when(context.getTestClass()).thenReturn(Optional.empty());
            when(context.getTestMethod()).thenReturn(Optional.empty());
            when(context.getDisplayName()).thenReturn(DEFAULT_DISPLAY_NAME);

            // Act & Assert
            try (MockedStatic<ThreadContext> threadContext = mockStatic(ThreadContext.class);
                 MockedStatic<LogTest> logTest = mockStatic(LogTest.class)) {
                prologue.beforeTestExecution(context);

                // Verify thread context uses fallback values
                threadContext.verify(() ->
                        ThreadContext.put(eq(TEST_NAME), eq("UnknownClass.UnknownMethod")));

                // Verify start time is still stored
                verify(store).put(eq(START_TIME), anyLong());

                // Verify quest beginning is logged with available display name
                logTest.verify(() ->
                        LogTest.info(eq("The quest: '{}' has begun."), eq(DEFAULT_DISPLAY_NAME)));
            }
        }
    }
}