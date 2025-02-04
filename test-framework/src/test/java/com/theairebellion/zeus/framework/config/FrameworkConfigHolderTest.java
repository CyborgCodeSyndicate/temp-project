package com.theairebellion.zeus.framework.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrameworkConfigHolderTest {

    @Test
    void testSingletonBehavior() {
        FrameworkConfig config1 = FrameworkConfigHolder.getFrameworkConfig();
        FrameworkConfig config2 = FrameworkConfigHolder.getFrameworkConfig();
        assertSame(config1, config2, "FrameworkConfigHolder should return a singleton instance");
    }
}