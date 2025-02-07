package com.theairebellion.zeus.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class LogZeusTest {

    public static final String MARKERS = "MARKERS";
    public static final String TEST_MARKER = "TEST_MARKER";
    public static final String EXISTING_MARKER = "EXISTING_MARKER";
    public static final String NON_EXISTENT_MARKER = "NON_EXISTENT_MARKER";
    public static final String TEST_LOGGER = "TestLogger";

    private static ConcurrentHashMap<String, Marker> markers;

    @BeforeEach
    void setUp() throws Exception {
        Field field = LogZeus.class.getDeclaredField(MARKERS);
        field.setAccessible(true);
        markers = (ConcurrentHashMap<String, Marker>) field.get(null);
        markers.clear();
    }

    @Test
    void registerMarker_shouldCreateNewMarker_whenNotExists() {
        Marker marker = LogZeus.registerMarker(TEST_MARKER);

        assertAll(
                () -> assertNotNull(marker),
                () -> assertEquals(TEST_MARKER, marker.getName()),
                () -> assertTrue(markers.containsKey(TEST_MARKER))
        );
    }

    @Test
    void registerMarker_shouldReturnExistingMarker_whenAlreadyExists() {
        Marker existingMarker = MarkerManager.getMarker(EXISTING_MARKER);
        markers.put(EXISTING_MARKER, existingMarker);

        Marker result = LogZeus.registerMarker(EXISTING_MARKER);

        assertSame(existingMarker, result);
    }

    @Test
    void getMarker_shouldReturnExistingMarker() {
        Marker expected = MarkerManager.getMarker(EXISTING_MARKER);
        markers.put(EXISTING_MARKER, expected);

        Marker result = LogZeus.getMarker(EXISTING_MARKER);

        assertSame(expected, result);
    }

    @Test
    void getMarker_shouldReturnNullForNonExistentMarker() {
        Marker result = LogZeus.getMarker(NON_EXISTENT_MARKER);
        assertNull(result);
    }

    @Test
    void getLogger_shouldReturnLoggerWithClassName() {
        Logger logger = LogZeus.getLogger(LogZeusTest.class);
        assertEquals(LogZeusTest.class.getName(), logger.getName());
    }

    @Test
    void getLogger_shouldReturnLoggerWithSpecifiedName() {
        Logger logger = LogZeus.getLogger(TEST_LOGGER);
        assertEquals(TEST_LOGGER, logger.getName());
    }
}