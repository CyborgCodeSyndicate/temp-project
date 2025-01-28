package com.theairebellion.zeus.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class LogZeusTest {

    @Test
    void testRegisterMarker_NewMarker() {
        // Clear the markers map (reflection is needed because MARKERS is private and final)
        ConcurrentHashMap<String, Marker> markers = getMarkersInstance();

        // Ensure the map is empty
        markers.clear();

        // Register a new marker
        Marker marker = LogZeus.registerMarker("TEST_MARKER");

        // Assertions
        assertNotNull(marker);
        assertEquals("TEST_MARKER", marker.getName());
        assertTrue(markers.containsKey("TEST_MARKER"));
    }

    @Test
    void testRegisterMarker_ExistingMarker() {
        // Clear the markers map
        ConcurrentHashMap<String, Marker> markers = getMarkersInstance();
        markers.clear();

        // Add a marker directly to the map
        Marker existingMarker = MarkerManager.getMarker("EXISTING_MARKER");
        markers.put("EXISTING_MARKER", existingMarker);

        // Register the same marker
        Marker marker = LogZeus.registerMarker("EXISTING_MARKER");

        // Assertions
        assertSame(existingMarker, marker);
    }

    @Test
    void testGetMarker_ExistingMarker() {
        // Clear the markers map
        ConcurrentHashMap<String, Marker> markers = getMarkersInstance();
        markers.clear();

        // Add a marker directly to the map
        Marker existingMarker = MarkerManager.getMarker("EXISTING_MARKER");
        markers.put("EXISTING_MARKER", existingMarker);

        // Retrieve the marker
        Marker result = LogZeus.getMarker("EXISTING_MARKER");

        // Assertions
        assertSame(existingMarker, result);
    }

    @Test
    void testGetMarker_NonExistentMarker() {
        // Clear the markers map
        ConcurrentHashMap<String, Marker> markers = getMarkersInstance();
        markers.clear();

        // Retrieve a marker that does not exist
        Marker result = LogZeus.getMarker("NON_EXISTENT_MARKER");

        // Assertions
        assertNull(result);
    }

    @Test
    void testGetLogger_ByClass() {
        // Retrieve a logger by class
        Logger logger = LogZeus.getLogger(LogZeusTest.class);

        // Assertions
        assertNotNull(logger);
        assertEquals(LogZeusTest.class.getName(), logger.getName());
    }

    @Test
    void testGetLogger_ByName() {
        // Retrieve a logger by name
        Logger logger = LogZeus.getLogger("TestLogger");

        // Assertions
        assertNotNull(logger);
        assertEquals("TestLogger", logger.getName());
    }

    // Helper method to access the private static MARKERS field via reflection
    private ConcurrentHashMap<String, Marker> getMarkersInstance() {
        try {
            var field = LogZeus.class.getDeclaredField("MARKERS");
            field.setAccessible(true);
            return (ConcurrentHashMap<String, Marker>) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access MARKERS field", e);
        }
    }
}