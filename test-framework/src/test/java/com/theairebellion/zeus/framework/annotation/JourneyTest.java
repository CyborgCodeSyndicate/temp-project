package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockJourneyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Journey Annotation Tests")
class JourneyTest {

    @Test
    @DisplayName("Should retrieve multiple Journey annotations with correct attributes")
    void testJourneyAnnotations() throws Exception {
        // When
        Method method = MockJourneyTest.class.getMethod("mockJourneyMethod");
        Journey[] journeys = method.getAnnotationsByType(Journey.class);

        // Then
        assertEquals(2, journeys.length, "Should have two Journey annotations");

        // First journey
        Journey first = journeys[0];
        assertEquals("first", first.value(), "First journey value should match");
        assertEquals(1, first.order(), "First journey order should match");

        JourneyData[] firstData = first.journeyData();
        assertEquals(1, firstData.length, "First journey should have one data item");
        assertEquals("data1", firstData[0].value(), "Journey data value should match");
        assertTrue(firstData[0].late(), "Journey data late flag should be true");

        // Second journey
        Journey second = journeys[1];
        assertEquals("second", second.value(), "Second journey value should match");
        assertEquals(2, second.order(), "Second journey order should match");
        assertEquals(0, second.journeyData().length, "Second journey should have no data items");
    }

    @Test
    @DisplayName("Should have correct meta annotations")
    void testJourneyMetaAnnotations() {
        // When
        Retention retention = Journey.class.getAnnotation(Retention.class);
        Target target = Journey.class.getAnnotation(Target.class);
        Repeatable repeatable = Journey.class.getAnnotation(Repeatable.class);

        // Then
        assertNotNull(retention, "Should have @Retention annotation");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

        assertNotNull(target, "Should have @Target annotation");
        assertArrayEquals(new ElementType[]{ElementType.METHOD}, target.value(),
                "Should target methods only");

        assertNotNull(repeatable, "Should have @Repeatable annotation");
        assertEquals(PreQuest.class, repeatable.value(), "Repeatable container should be PreQuest");
    }

    @Test
    @DisplayName("Should use default values when not specified")
    void testJourneyDefaultValues() throws NoSuchMethodException {
        // Given
        class MockDefaultJourneyTest {
            @Journey("simple")
            public void mockSimpleJourneyMethod() {}
        }

        // When
        Method method = MockDefaultJourneyTest.class.getMethod("mockSimpleJourneyMethod");
        Journey journey = method.getAnnotation(Journey.class);

        // Then
        assertNotNull(journey, "Method should be annotated with @Journey");
        assertEquals("simple", journey.value(), "Journey value should match");
        assertEquals(0, journey.order(), "Default order should be 0");
        assertEquals(0, journey.journeyData().length, "Default journeyData should be empty");
    }
}