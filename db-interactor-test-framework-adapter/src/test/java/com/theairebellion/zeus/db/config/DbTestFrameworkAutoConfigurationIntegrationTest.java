package com.theairebellion.zeus.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.allure.AllureDbClientManager;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for DbTestFrameworkAutoConfiguration using mocked dependencies
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbTestFrameworkAutoConfigurationIntegrationTest.TestConfig.class,
        DbTestFrameworkAutoConfiguration.class
})
class DbTestFrameworkAutoConfigurationIntegrationTest {

    /**
     * Test configuration that provides mock dependencies
     */
    @Configuration
    static class TestConfig {

        @Bean
        @Primary
        public ObjectMapper objectMapper() {
            return Mockito.mock(ObjectMapper.class);
        }

        @Bean
        public BaseDbConnectorService baseDbConnectorService() {
            return Mockito.mock(BaseDbConnectorService.class);
        }

        @Bean
        public com.jayway.jsonpath.Configuration jsonPathConfiguration() {
            return Mockito.mock(com.jayway.jsonpath.Configuration.class);
        }

        @Bean
        public JsonPathExtractor jsonPathExtractor(
                ObjectMapper objectMapper,
                com.jayway.jsonpath.Configuration jsonPathConfig) {
            return new JsonPathExtractor(objectMapper, jsonPathConfig);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Auto-configuration should create and wire all required beans with the correct implementation types")
    void shouldCreateAndWireAllRequiredBeans() {
        // First verify that the context was loaded properly
        assertThat(applicationContext).isNotNull();

        // Verify that the primary beans are correctly registered with the expected implementation types
        assertThat(applicationContext.getBean(DbClientManager.class))
                .as("DbClientManager bean should be available")
                .isNotNull()
                .isInstanceOf(AllureDbClientManager.class);

        assertThat(applicationContext.getBean(QueryResponseValidator.class))
                .as("QueryResponseValidator bean should be available")
                .isNotNull()
                .isInstanceOf(QueryResponseValidatorAllureImpl.class);
    }
}