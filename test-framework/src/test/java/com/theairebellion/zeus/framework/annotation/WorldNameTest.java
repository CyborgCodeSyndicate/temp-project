package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.WorldNameTestDummy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorldNameTest {

    @Test
    void testWorldNameAnnotation() {
        WorldName wn = WorldNameTestDummy.class.getAnnotation(WorldName.class);
        assertNotNull(wn, "Class should be annotated with @WorldName");
        assertEquals("TestWorld", wn.value());
    }
}