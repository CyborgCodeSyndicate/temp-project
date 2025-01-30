package com.theairebellion.zeus.api.config;

import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ApiTestFrameworkAutoConfigurationTest {

    @Test
    void testBeanLoading() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(ApiTestFrameworkAutoConfiguration.class);
            context.refresh();

            RestClient client = context.getBean(RestClient.class);
            RestResponseValidator validator = context.getBean(RestResponseValidator.class);

            assertNotNull(client, "RestClient bean should not be null");
            assertNotNull(validator, "RestResponseValidator bean should not be null");
        }
    }
}