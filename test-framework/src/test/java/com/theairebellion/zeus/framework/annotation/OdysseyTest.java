package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockOdysseyTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

public class OdysseyTest {

    @Test
    void odysseyAnnotation_ShouldContainCorrectExtensions() {
        Odyssey odyssey = MockOdysseyTest.class.getAnnotation(Odyssey.class);
        ExtendWith extendWith = odyssey.annotationType().getAnnotation(ExtendWith.class);

        assertNotNull(extendWith);
        assertEquals(6, extendWith.value().length);
    }
}