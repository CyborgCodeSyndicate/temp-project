package com.theairebellion.zeus.db.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LogDb Tests")
class LogDbTest {

    private static final String MSG = "message";
    private static final String TPL = "tpl: {}";
    private static final Object PARAM = 123;
    private LogDb originalInstance;

    @BeforeEach
    void setUp() throws Exception {
        // stash the existing singleton
        Field f = LogDb.class.getDeclaredField("instance");
        f.setAccessible(true);
        originalInstance = (LogDb) f.get(null);
    }

    @AfterEach
    void tearDown() throws Exception {
        // restore the original singleton
        Field f = LogDb.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, originalInstance);
    }

    @Test
    @DisplayName("All static log methods should not throw")
    void testBasicLogMethods() {
        assertDoesNotThrow(() -> LogDb.info(MSG));
        assertDoesNotThrow(() -> LogDb.warn(MSG));
        assertDoesNotThrow(() -> LogDb.error(MSG));
        assertDoesNotThrow(() -> LogDb.debug(MSG));
        assertDoesNotThrow(() -> LogDb.trace(MSG));
        assertDoesNotThrow(() -> LogDb.step(MSG));
        assertDoesNotThrow(() -> LogDb.validation(MSG));
        assertDoesNotThrow(() -> LogDb.extended(MSG));
    }

    @Test
    @DisplayName("Format-style log methods should accept parameters")
    void testLogWithParams() {
        assertDoesNotThrow(() -> LogDb.info(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.warn(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.error(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.debug(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.trace(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.step(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.validation(TPL, PARAM));
        assertDoesNotThrow(() -> LogDb.extended(TPL, PARAM));
    }

    @Test
    @DisplayName("Singleton should initialize on first use")
    void testSingletonInitialization() throws Exception {
        // reset
        Field f = LogDb.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        // trigger init
        LogDb.info(MSG);

        Object inst = f.get(null);
        assertNotNull(inst, "LogDb.instance should be created");
        assertTrue(inst instanceof LogDb);
    }

    @Test
    @DisplayName("Singleton reuse across calls")
    void testSingletonReuse() throws Exception {
        // reset
        Field f = LogDb.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        LogDb.info("first");
        Object first = f.get(null);

        LogDb.info("second");
        Object second = f.get(null);

        assertSame(first, second, "LogDb.instance should be reused");
    }

    @Test
    @DisplayName("extend() should replace the singleton instance")
    void testExtendSwapsInstance() throws Exception {
        // ensure the default singleton is initialized
        LogDb.info(MSG);
        Field f = LogDb.class.getDeclaredField("instance");
        f.setAccessible(true);

        // now create a fresh LogDb via its private constructor
        Constructor<LogDb> ctor = LogDb.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        LogDb custom = ctor.newInstance();

        // replace it
        LogDb.extend(custom);

        // verify the field was swapped
        LogDb current = (LogDb) f.get(null);
        assertSame(custom, current);

        // and that static calls still work (no exception)
        assertDoesNotThrow(() -> LogDb.info(MSG));
    }
}
