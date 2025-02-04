package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.JourneyTestDummy;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

public class JourneyTest {

    @Test
    void testJourneyAnnotations() throws Exception {
        Method m = JourneyTestDummy.class.getMethod("dummyJourneyMethod");
        Journey[] journeys = m.getAnnotationsByType(Journey.class);
        assertEquals(2, journeys.length, "Should have two Journey annotations");
        // Assuming declaration order is preserved:
        assertEquals("first", journeys[0].value());
        assertEquals(1, journeys[0].order());
        JourneyData[] data = journeys[0].journeyData();
        assertEquals(1, data.length);
        assertEquals("data1", data[0].value());
        assertTrue(data[0].late());
        assertEquals("second", journeys[1].value());
        assertEquals(2, journeys[1].order());
    }
}