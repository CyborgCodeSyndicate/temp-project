package com.theairebellion.zeus.api.config;

import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ApiTestFrameworkAutoConfigurationTest {

    @Test
    void testBeanLoading() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(ApiTestFrameworkAutoConfiguration.class);
            context.refresh();

            assertAll(
                    () -> assertNotNull(context.getBean(RestClient.class), "RestClient bean should not be null"),
                    () -> assertNotNull(context.getBean(RestResponseValidator.class), "RestResponseValidator bean should not be null")
            );
        }
    }
}