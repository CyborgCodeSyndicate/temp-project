package com.theairebellion.zeus.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

class LogCoreTest {

    private TestLogCore testLogCore;
    private Logger mockLogger;
    private Marker mockMarker;

    @BeforeEach
    void setup() {
        mockLogger = mock(Logger.class);
        mockMarker = mock(Marker.class);

        // Mock LogZeus static methods
        try (MockedStatic<LogZeus> mockedStatic = Mockito.mockStatic(LogZeus.class)) {
            mockedStatic.when(() -> LogZeus.getLogger("TestLogger"))
                    .thenReturn(mockLogger);
            mockedStatic.when(() -> LogZeus.registerMarker("TestMarker"))
                    .thenReturn(mockMarker);

            // Instantiate the concrete implementation of LogCore
            testLogCore = new TestLogCore("TestLogger", "TestMarker");
        }
    }

    @Test
    void testStepLog() {
        testLogCore.stepLog("Step message", "arg1", "arg2");
        verify(mockLogger).log(eq(Level.forName("STEP", 350)), eq(mockMarker), eq("Step message"), eq(new Object[]{"arg1", "arg2"}));
    }

    @Test
    void testValidationLog() {
        testLogCore.validationLog("Validation message", "arg1", "arg2");
        verify(mockLogger).log(eq(Level.forName("VALIDATION", 350)), eq(mockMarker), eq("Validation message"), eq(new Object[]{"arg1", "arg2"}));
    }

    @Test
    void testExtendedLogWhenEnabled() throws Exception {
        // Use Unsafe to set the EXTENDED_LOGGING field to true
        setFinalStaticField(LogCore.class, "EXTENDED_LOGGING", true);

        // Call the method under test
        testLogCore.extendedLog("Extended message", "arg1", "arg2");

        // Verify the logger was called
        verify(mockLogger).log(eq(Level.forName("EXTENDED", 450)),
                eq(mockMarker),
                eq("Extended message"),
                (Object[]) eq(new Object[]{"arg1", "arg2"}));
    }

    @Test
    void testExtendedLogWhenDisabled() throws Exception {
        // Use Unsafe to set the EXTENDED_LOGGING field to false
        setFinalStaticField(LogCore.class, "EXTENDED_LOGGING", false);

        // Call the method under test
        testLogCore.extendedLog("Extended message", "arg1", "arg2");

        // Verify the logger was not called
        verify(mockLogger, never()).log(any(Level.class),
                eq(mockMarker),
                anyString(),
                (Object[]) any());
    }

    // Concrete implementation of LogCore for testing
    private static class TestLogCore extends LogCore {
        protected TestLogCore(String loggerName, String markerName) {
            super(loggerName, markerName);
        }
    }

    // Helper method to modify final static fields
    private void setFinalStaticField(Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Get the Unsafe instance
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        // Get the base offset of the static field
        Object staticFieldBase = unsafe.staticFieldBase(field);
        long staticFieldOffset = unsafe.staticFieldOffset(field);

        // Set the new value
        unsafe.putBoolean(staticFieldBase, staticFieldOffset, (boolean) value);
    }
}