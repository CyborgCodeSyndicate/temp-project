package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.config.PropertyConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.util.AllureStepHelperTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.util.List;
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
            try (MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                 MockedStatic<ThreadContext> threadContext = mockStatic(ThreadContext.class);
                 MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {

                // Given
                List<Class<? extends PropertyConfig>> dummyConfigs = List.of(AllureStepHelperTest.BasicPropertyConfig.class);
                mockedReflectionUtil.when(() ->
                        ReflectionUtil.findImplementationsOfInterface(any(), any())
                ).thenReturn(dummyConfigs);

                AllureStepHelperTest.BasicPropertyConfig dummyConfig = new AllureStepHelperTest.BasicPropertyConfig();
                mockedConfigCache.when(() -> ConfigCache.getOrCreate(AllureStepHelperTest.BasicPropertyConfig.class))
                        .thenReturn(dummyConfig);

                FrameworkConfig dummyFrameworkConfig = mock(FrameworkConfig.class);
                lenient().when(dummyFrameworkConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
                mockedConfigCache.when(() -> ConfigCache.getOrCreate(FrameworkConfig.class))
                        .thenReturn(dummyFrameworkConfig);
                prologue.beforeTestExecution(context);

                // Verify thread context uses fallback values
                threadContext.verify(() ->
                        ThreadContext.put(eq(TEST_NAME), eq("UnknownClass.UnknownMethod")));

                // Verify start time is still stored
                verify(store).put(eq(START_TIME), anyLong());
            }
        }
    }
}