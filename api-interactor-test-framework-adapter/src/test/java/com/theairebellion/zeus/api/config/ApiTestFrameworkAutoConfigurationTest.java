package com.theairebellion.zeus.api.config;

import com.theairebellion.zeus.api.allure.RestClientAllureImpl;
import com.theairebellion.zeus.api.allure.RestResponseValidatorAllureImpl;
import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiTestFrameworkAutoConfiguration Tests")
class ApiTestFrameworkAutoConfigurationTest {

    @Test
    @DisplayName("Should have component scan with correct base package")
    void shouldHaveComponentScanWithCorrectBasePackage() {
        // Verify component scanning configuration directly through reflection
        ComponentScan componentScan = ApiTestFrameworkAutoConfiguration.class
                .getAnnotation(ComponentScan.class);

        assertThat(componentScan).isNotNull();
        assertThat(componentScan.basePackages()).containsExactly("com.theairebellion.zeus.api");
    }

    @Test
    @DisplayName("RestClient bean method should be annotated with @Primary")
    void restClientBeanMethodShouldHavePrimaryAnnotation() throws NoSuchMethodException {
        // Verify @Primary annotation on the restClient method
        var method = ApiTestFrameworkAutoConfiguration.class.getDeclaredMethod(
                "restClient", RestClientAllureImpl.class);

        Primary primaryAnnotation = method.getAnnotation(Primary.class);
        assertThat(primaryAnnotation).isNotNull();
    }

    @Test
    @DisplayName("RestResponseValidator bean method should be annotated with @Primary")
    void restResponseValidatorBeanMethodShouldHavePrimaryAnnotation() throws NoSuchMethodException {
        // Verify @Primary annotation on the restResponseValidator method
        var method = ApiTestFrameworkAutoConfiguration.class.getDeclaredMethod(
                "restResponseValidator", RestResponseValidatorAllureImpl.class);

        Primary primaryAnnotation = method.getAnnotation(Primary.class);
        assertThat(primaryAnnotation).isNotNull();
    }

    @Test
    @DisplayName("Bean methods should exist and have correct return types")
    void beanMethodsShouldExistAndHaveCorrectReturnTypes() throws NoSuchMethodException {
        // Verify return types of bean methods
        var restClientMethod = ApiTestFrameworkAutoConfiguration.class.getDeclaredMethod(
                "restClient", RestClientAllureImpl.class);
        var restValidatorMethod = ApiTestFrameworkAutoConfiguration.class.getDeclaredMethod(
                "restResponseValidator", RestResponseValidatorAllureImpl.class);

        assertThat(restClientMethod.getReturnType()).isEqualTo(RestClient.class);
        assertThat(restValidatorMethod.getReturnType()).isEqualTo(RestResponseValidator.class);
    }

    @Test
    @DisplayName("Should create RestClient bean from RestClientAllureImpl")
    void shouldCreateRestClientBean() {
        // Create test instance and mock dependency
        ApiTestFrameworkAutoConfiguration config = new ApiTestFrameworkAutoConfiguration();
        RestClientAllureImpl mockImpl = mock(RestClientAllureImpl.class);

        // Invoke method directly
        RestClient client = config.restClient(mockImpl);

        // Verify the returned instance is the same as our mock
        assertThat(client).isSameAs(mockImpl);
    }

    @Test
    @DisplayName("Should create RestResponseValidator bean from RestResponseValidatorAllureImpl")
    void shouldCreateRestResponseValidatorBean() {
        // Create test instance and mock dependency
        ApiTestFrameworkAutoConfiguration config = new ApiTestFrameworkAutoConfiguration();
        RestResponseValidatorAllureImpl mockImpl = mock(RestResponseValidatorAllureImpl.class);

        // Invoke method directly
        RestResponseValidator validator = config.restResponseValidator(mockImpl);

        // Verify the returned instance is the same as our mock
        assertThat(validator).isSameAs(mockImpl);
    }
}