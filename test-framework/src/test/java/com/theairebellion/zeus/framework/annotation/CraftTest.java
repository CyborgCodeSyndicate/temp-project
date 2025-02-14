package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockCraftTest;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CraftTest {

    @Test
    void testCraftAnnotation() throws Exception {
        Method m = MockCraftTest.class.getMethod("mockMethod", String.class);
        Craft craft = m.getParameters()[0].getAnnotation(Craft.class);
        assertNotNull(craft, "Parameter should be annotated with @Craft");
        assertEquals("testModel", craft.model());
    }
}