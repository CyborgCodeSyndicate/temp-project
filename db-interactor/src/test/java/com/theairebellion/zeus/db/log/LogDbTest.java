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
    void stashOriginal() throws Exception {
        Field inst = LogDb.class.getDeclaredField("INSTANCE");
        inst.setAccessible(true);
        original = (LogDb) inst.get(null);
    }

    @AfterEach
    void restoreOriginal() throws Exception {
        Field inst = LogDb.class.getDeclaredField("INSTANCE");
        inst.setAccessible(true);
        inst.set(null, original);
    }

    @Nested
    @DisplayName("BasicFunctionality")
    class BasicFunctionality {
        @Test
        @DisplayName("all static methods never throw")
        void testAllDoNotThrow() {
            assertDoesNotThrow(() -> LogDb.info(MSG));
            assertDoesNotThrow(() -> LogDb.warn(MSG));
            assertDoesNotThrow(() -> LogDb.error(MSG));
            assertDoesNotThrow(() -> LogDb.debug(MSG));
            assertDoesNotThrow(() -> LogDb.trace(MSG));
            assertDoesNotThrow(() -> LogDb.step(MSG));
            assertDoesNotThrow(() -> LogDb.validation(MSG));
            assertDoesNotThrow(() -> LogDb.extended(MSG));

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
