package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.TestServiceTestDummy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestServiceTest {

    @Test
    void testWorldNameAnnotation() {
        TestService wn = TestServiceTestDummy.class.getAnnotation(TestService.class);
        assertNotNull(wn, "Class should be annotated with @TestService");
        assertEquals("TestService", wn.value());
    }
}