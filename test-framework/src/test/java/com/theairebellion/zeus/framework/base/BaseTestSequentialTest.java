package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class, BaseTestSequential.class})
@TestPropertySource(properties = {"project.package=com.theairebellion.zeus.framework"})
public class BaseTestSequentialTest {

    @Autowired
    private BaseTestSequential baseTestSequential;

    @Test
    void testServicesInjection() throws Exception {
        assertNotNull(baseTestSequential, "BaseTestSequential bean should not be null");
        // Use reflection to retrieve the private 'services' field.
        Field servicesField = BaseTestSequential.class.getDeclaredField("services");
        servicesField.setAccessible(true);
        Object services = servicesField.get(baseTestSequential);
        assertNotNull(services, "Services should be injected in BaseTestSequential");
    }
}