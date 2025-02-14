package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PrologueTest {

    public static final String TEST_NAME = "testName";
    public static final String DEFAULT_DISPLAY_NAME = "DefaultDisplayName";
    public static final String TEST_DISPLAY_NAME = "TestDisplayName";

    @Test
    void beforeTestExecution_SetsThreadContextAndStoresStartTime() throws Exception {
        Prologue prologue = new Prologue();
        ExtensionContext context = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        Class<?> testClass = String.class;
        Method testMethod = String.class.getMethod("toString");
        when(context.getTestClass()).thenReturn(Optional.of(testClass));
        when(context.getTestMethod()).thenReturn(Optional.of(testMethod));
        when(context.getDisplayName()).thenReturn(TEST_DISPLAY_NAME);
        try (MockedStatic<ThreadContext> threadContext = mockStatic(ThreadContext.class);
             MockedStatic<LogTest> logTest = mockStatic(LogTest.class)) {
            prologue.beforeTestExecution(context);
            threadContext.verify(() -> ThreadContext.put(eq(TEST_NAME), eq("String.toString")));
            verify(store).put(eq(START_TIME), anyLong());
            logTest.verify(() -> LogTest.info(eq("The quest: '{}' has begun."), eq(TEST_DISPLAY_NAME)));
        }
    }

    @Test
    void beforeTestExecution_UsesUnknownValuesWhenAbsent() {
        Prologue prologue = new Prologue();
        ExtensionContext context = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(context.getTestClass()).thenReturn(Optional.empty());
        when(context.getTestMethod()).thenReturn(Optional.empty());
        when(context.getDisplayName()).thenReturn(DEFAULT_DISPLAY_NAME);
        try (MockedStatic<ThreadContext> threadContext = mockStatic(ThreadContext.class);
             MockedStatic<LogTest> logTest = mockStatic(LogTest.class)) {
            prologue.beforeTestExecution(context);
            threadContext.verify(() -> ThreadContext.put(eq(TEST_NAME), eq("UnknownClass.UnknownMethod")));
            verify(store).put(eq(START_TIME), anyLong());
            logTest.verify(() -> LogTest.info(eq("The quest: '{}' has begun."), eq(DEFAULT_DISPLAY_NAME)));
        }
    }
}
