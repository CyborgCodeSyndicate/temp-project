package com.theairebellion.zeus.framework.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = TestConfig.class,
        properties = "project.package=com.theairebellion.zeus"
)
class TestConfigTest {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}