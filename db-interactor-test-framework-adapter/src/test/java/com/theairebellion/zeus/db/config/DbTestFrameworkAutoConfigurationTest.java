package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.db.allure.AllureDbClientManager;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DbTestFrameworkAutoConfigurationTest {

    @Mock
    private AllureDbClientManager mockAllureDbClientManager;

    @Mock
    private QueryResponseValidatorAllureImpl mockQueryResponseValidator;

    @Test
    @DisplayName("dbClientManager method should return the provided AllureDbClientManager instance")
    void dbClientManagerShouldReturnAllureInstance() {
        // Given
        DbTestFrameworkAutoConfiguration config = new DbTestFrameworkAutoConfiguration();

        // When
        DbClientManager result = config.dbClientManager(mockAllureDbClientManager);

        // Then
        assertThat(result)
                .as("dbClientManager should return the AllureDbClientManager instance")
                .isSameAs(mockAllureDbClientManager);
    }

    @Test
    @DisplayName("queryResponseValidator method should return the provided QueryResponseValidatorAllureImpl instance")
    void queryResponseValidatorShouldReturnAllureInstance() {
        // Given
        DbTestFrameworkAutoConfiguration config = new DbTestFrameworkAutoConfiguration();

        // When
        QueryResponseValidator result = config.queryResponseValidator(mockQueryResponseValidator);

        // Then
        assertThat(result)
                .as("queryResponseValidator should return the QueryResponseValidatorAllureImpl instance")
                .isSameAs(mockQueryResponseValidator);
    }

    @Test
    @DisplayName("Component scan should register the correct base packages")
    void componentScanShouldRegisterCorrectBasePackages() {
        // Given: The class has @ComponentScan with correct base packages
        // When: We inspect the annotation
        ComponentScan componentScan = DbTestFrameworkAutoConfiguration.class
                .getAnnotation(ComponentScan.class);

        // Then: The component scan annotation should be present with the expected base packages
        assertThat(componentScan)
                .as("@ComponentScan annotation should be present")
                .isNotNull();

        assertThat(componentScan.basePackages())
                .as("Component scan base packages should be correct")
                .containsExactly("com.theairebellion.zeus.db");
    }
}