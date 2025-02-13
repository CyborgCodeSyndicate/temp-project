package com.theairebellion.zeus.framework.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FrameworkConfigTest {

    @Test
    void testProjectPackage() {
        FrameworkConfig config = mock(FrameworkConfig.class);
        when(config.projectPackage()).thenReturn("com.theairebellion.test");
        assertEquals("com.theairebellion.test", config.projectPackage());
    }

    @Test
    void testDefaultStorage() {
        FrameworkConfig config = mock(FrameworkConfig.class);
        when(config.defaultStorage()).thenReturn("testStorage");
        assertEquals("testStorage", config.defaultStorage());
    }
}