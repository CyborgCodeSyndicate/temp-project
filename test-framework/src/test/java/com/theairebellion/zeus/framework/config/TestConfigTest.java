package com.theairebellion.zeus.framework.config;

import com.theairebellion.zeus.framework.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestConfig.class)
@TestPropertySource(properties = {"project.package=com.theairebellion.zeus.framework"})
public class TestConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testComponentScan() {
        // Verify that a bean of type BaseTest is found in the context.
        BaseTest baseTestBean = context.getBean(BaseTest.class);
        assertNotNull(baseTestBean, "BaseTest bean should be available from component scan");
    }
}