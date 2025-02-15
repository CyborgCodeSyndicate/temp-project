package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockPreQuestTest;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class PreQuestTest {

    @Test
    void testPreQuestAnnotation() throws Exception {
        Method m = MockPreQuestTest.class.getMethod("mockPreQuestMethod");
        PreQuest preQuest = m.getAnnotation(PreQuest.class);
        assertNotNull(preQuest, "Method should be annotated with @PreQuest");
        Journey[] journeys = preQuest.value();
        assertEquals(1, journeys.length);
        assertEquals("prequest", journeys[0].value());
        assertEquals(5, journeys[0].order());
    }
}