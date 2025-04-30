package com.theairebellion.zeus.db.log;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogDbTest {

    private static final String MSG = "foo";
    private static final String TPL = "format: {}";

    private LogDb original;

    @BeforeEach
    void setUp() throws Exception {
        // Store the original instance
        Field instanceField = LogDb.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        originalInstance = (LogDb) instanceField.get(null);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Restore the original instance
        Field instanceField = LogDb.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, originalInstance);
    }

    @Test
    @DisplayName("All logging methods should not throw exceptions")
    void testAllLogMethodsDoNotThrow() {
        // Testing all methods to ensure they don't throw
        assertDoesNotThrow(() -> LogDb.info(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.warn(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.error(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.debug(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.trace(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.step(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.validation(TEST_MESSAGE));
        assertDoesNotThrow(() -> LogDb.extended(TEST_MESSAGE));
    }

    @Test
    @DisplayName("Logging methods should accept message templates with parameters")
    void testLoggingWithParameters() {
        // Testing with parameter formatting
        assertDoesNotThrow(() -> LogDb.info(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.warn(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.error(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.debug(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.trace(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.step(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.validation(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
        assertDoesNotThrow(() -> LogDb.extended(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
    }

    @Test
    @DisplayName("Should create instance on first call")
    void testSingletonInitialization() throws Exception {
        // Given - reset the instance for this test
        Field instanceField = LogDb.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        // When
        LogDb.info(TEST_MESSAGE); // This should initialize the instance

        // Then
        LogDb instance = (LogDb) instanceField.get(null);
        assertNotNull(instance, "LogDb instance should be created");
    }

    @Test
    @DisplayName("Should reuse the same instance")
    void testSingletonReuse() throws Exception {
        // Given - reset the instance for this test
        Field instanceField = LogDb.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        // When
        LogDb.info("First call"); // Initialize instance
        LogDb firstInstance = (LogDb) instanceField.get(null);

        LogDb.info("Second call"); // Should reuse instance
        LogDb secondInstance = (LogDb) instanceField.get(null);

        // Then
        assertSame(firstInstance, secondInstance, "Same LogDb instance should be reused");
    }

    @Test
    @DisplayName("Static methods should delegate to instance methods")
    void testStaticMethodsDelegateToInstanceMethods() {
        // Since we can't directly test the protected methods due to the final class,
        // we can use static mocking to verify the static method calls
        try (MockedStatic<LogDb> mockedLogDb = mockStatic(LogDb.class)) {
            // When
            LogDb.info(TEST_MESSAGE);

            // templates
            assertDoesNotThrow(() -> LogDb.info(TPL, "x"));
            assertDoesNotThrow(() -> LogDb.error(TPL, new RuntimeException("e")));
        }

        @Test
        @DisplayName("null message/args are safe")
        void testNulls() {
            assertDoesNotThrow(() -> LogDb.info(null));
            assertDoesNotThrow(() -> LogDb.info(TPL, (Object) null));
        }

        @Test
        @DisplayName("singleton initializes on first call")
        void testSingletonInit() throws Exception {
            // reset
            Field inst = LogDb.class.getDeclaredField("INSTANCE");
            inst.setAccessible(true);
            inst.set(null, null);

            LogDb.info(MSG);
            assertNotNull(inst.get(null));
        }

        @Test
        @DisplayName("singleton reused across calls")
        void testSingletonReuse() throws Exception {
            Field inst = LogDb.class.getDeclaredField("INSTANCE");
            inst.setAccessible(true);
            inst.set(null, null);

            LogDb.info("a");
            Object first = inst.get(null);

            LogDb.info("b");
            Object second = inst.get(null);

            assertSame(first, second);
        }
    }

    @Nested
    @DisplayName("ExtendTests")
    class ExtendTests {
        @Test
        @DisplayName("extend() swaps in your instance")
        void testExtendReplacesInstance() throws Exception {
            // ensure there's an original
            LogDb.info("init");
            Field inst = LogDb.class.getDeclaredField("INSTANCE");
            inst.setAccessible(true);
            LogDb before = (LogDb) inst.get(null);

            // make a spy of it
            LogDb spy = org.mockito.Mockito.spy(before);
            LogDb.extend(spy);

            // now the INSTANCE field is our spy
            LogDb current = (LogDb) inst.get(null);
            assertSame(spy, current);

            // and static calls go to that spy (no exception)
            assertDoesNotThrow(() -> LogDb.info("hello"));
        }
    }
}
