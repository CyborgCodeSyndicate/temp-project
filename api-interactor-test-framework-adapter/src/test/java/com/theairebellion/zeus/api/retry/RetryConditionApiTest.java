package com.theairebellion.zeus.api.retry;

import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RetryConditionApi Tests")
class RetryConditionApiTest {

    private static final String JSON_PATH = "some.path";
    private static final String EXPECTED_VALUE = "expectedValue";

    @Mock
    private RestService restService;

    @Mock
    private Endpoint endpoint;

    @Mock
    private Response response;

    @Mock
    private JsonPath jsonPath;

    @Test
    @DisplayName("Should create instance using default constructor")
    void shouldCreateInstanceUsingDefaultConstructor() throws Exception {
        // Using reflection to access and invoke the default constructor
        Constructor<RetryConditionApi> constructor = RetryConditionApi.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        RetryConditionApi instance = constructor.newInstance();

        // Just asserting that it's not null to confirm it was created
        assertThat(instance).isNotNull();
    }

    @ParameterizedTest(name = "When status is {0} and expected is {1}, result should be {2}")
    @CsvSource({
            "200, 200, true",  // Equal - should return true
            "404, 200, false", // Not equal - should return false
            "200, 404, false", // Not equal - should return false
            "0, 0, true"       // Edge case with zero
    })
    @DisplayName("Should correctly evaluate all status code branches")
    void shouldCorrectlyEvaluateAllStatusCodeBranches(int actualStatus, int expectedStatus, boolean expectedResult) {
        // Arrange
        when(restService.request(endpoint)).thenReturn(response);
        when(response.getStatusCode()).thenReturn(actualStatus);

        RetryCondition<Integer> condition = RetryConditionApi.statusEquals(endpoint, expectedStatus);

        // Act
        Integer result = condition.function().apply(restService);
        boolean matches = condition.condition().test(result);

        // Assert
        assertThat(matches).isEqualTo(expectedResult);
    }

    @Nested
    @DisplayName("Status Code Tests")
    class StatusCodeTests {

        static Stream<Arguments> statusCodesProvider() {
            return Stream.of(
                    Arguments.of(200, true),
                    Arguments.of(201, false),
                    Arguments.of(404, false),
                    Arguments.of(500, false)
            );
        }

        @ParameterizedTest(name = "Status {0} should match expected result: {1}")
        @MethodSource("statusCodesProvider")
        @DisplayName("Should correctly evaluate status code conditions")
        void shouldEvaluateStatusCodeConditions(int statusCode, boolean expectedResult) {
            // Arrange
            when(restService.request(endpoint)).thenReturn(response);
            when(response.getStatusCode()).thenReturn(statusCode);

            RetryCondition<Integer> condition = RetryConditionApi.statusEquals(endpoint, 200);

            // Act
            Integer result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isEqualTo(expectedResult);
            verify(restService).request(endpoint);
            verify(response).getStatusCode();
        }

        @Test
        @DisplayName("Should include body in request when provided")
        void shouldIncludeBodyInRequestWhenProvided() {
            // Arrange
            Object requestBody = new Object();
            when(restService.request(endpoint, requestBody)).thenReturn(response);
            when(response.getStatusCode()).thenReturn(201);

            RetryCondition<Integer> condition = RetryConditionApi.statusEquals(endpoint, requestBody, 201);

            // Act
            Integer result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isTrue();
            verify(restService).request(endpoint, requestBody);
            verify(response).getStatusCode();
        }
    }

    @Nested
    @DisplayName("Response Field Tests")
    class ResponseFieldTests {

        @Test
        @DisplayName("Should match when field equals expected value")
        void shouldMatchWhenFieldEqualsExpectedValue() {
            // Arrange
            when(restService.request(endpoint)).thenReturn(response);
            when(response.getBody()).thenReturn(response);
            when(response.jsonPath()).thenReturn(jsonPath);
            when(jsonPath.get(JSON_PATH)).thenReturn(EXPECTED_VALUE);

            RetryCondition<Object> condition = RetryConditionApi.responseFieldEqualsTo(endpoint, JSON_PATH, EXPECTED_VALUE);

            // Act
            Object result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isTrue();
            verify(jsonPath).get(JSON_PATH);
        }

        @Test
        @DisplayName("Should not match when field value is different")
        void shouldNotMatchWhenFieldValueIsDifferent() {
            // Arrange
            when(restService.request(endpoint)).thenReturn(response);
            when(response.getBody()).thenReturn(response);
            when(response.jsonPath()).thenReturn(jsonPath);
            when(jsonPath.get(JSON_PATH)).thenReturn("differentValue");

            RetryCondition<Object> condition = RetryConditionApi.responseFieldEqualsTo(endpoint, JSON_PATH, EXPECTED_VALUE);

            // Act
            Object result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isFalse();
            verify(jsonPath).get(JSON_PATH);
        }

        @Test
        @DisplayName("Should include body in request when checking field value")
        void shouldIncludeBodyInRequestWhenCheckingFieldValue() {
            // Arrange
            Object requestBody = new Object();
            when(restService.request(endpoint, requestBody)).thenReturn(response);
            when(response.getBody()).thenReturn(response);
            when(response.jsonPath()).thenReturn(jsonPath);
            when(jsonPath.get(JSON_PATH)).thenReturn(EXPECTED_VALUE);

            RetryCondition<Object> condition = RetryConditionApi.responseFieldEqualsTo(
                    endpoint, requestBody, JSON_PATH, EXPECTED_VALUE);

            // Act
            Object result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isTrue();
            verify(restService).request(endpoint, requestBody);
            verify(jsonPath).get(JSON_PATH);
        }
    }

    @Nested
    @DisplayName("Non-Null Field Tests")
    class NonNullFieldTests {

        @Test
        @DisplayName("Should match when field is not null")
        void shouldMatchWhenFieldIsNotNull() {
            // Arrange
            when(restService.request(endpoint)).thenReturn(response);
            when(response.getBody()).thenReturn(response);
            when(response.jsonPath()).thenReturn(jsonPath);
            when(jsonPath.get(JSON_PATH)).thenReturn("nonNullValue");

            RetryCondition<Object> condition = RetryConditionApi.responseFieldNonNull(endpoint, JSON_PATH);

            // Act
            Object result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isTrue();
            verify(jsonPath).get(JSON_PATH);
        }

        @Test
        @DisplayName("Should not match when field is null")
        void shouldNotMatchWhenFieldIsNull() {
            // Arrange
            when(restService.request(endpoint)).thenReturn(response);
            when(response.getBody()).thenReturn(response);
            when(response.jsonPath()).thenReturn(jsonPath);
            when(jsonPath.get(JSON_PATH)).thenReturn(null);

            RetryCondition<Object> condition = RetryConditionApi.responseFieldNonNull(endpoint, JSON_PATH);

            // Act
            Object result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isFalse();
            verify(jsonPath).get(JSON_PATH);
        }

        @Test
        @DisplayName("Should include body in request when checking for non-null field")
        void shouldIncludeBodyInRequestWhenCheckingForNonNullField() {
            // Arrange
            Object requestBody = new Object();
            when(restService.request(endpoint, requestBody)).thenReturn(response);
            when(response.getBody()).thenReturn(response);
            when(response.jsonPath()).thenReturn(jsonPath);
            when(jsonPath.get(JSON_PATH)).thenReturn("nonNullValue");

            RetryCondition<Object> condition = RetryConditionApi.responseFieldNonNull(
                    endpoint, requestBody, JSON_PATH);

            // Act
            Object result = condition.function().apply(restService);
            boolean matches = condition.condition().test(result);

            // Assert
            assertThat(matches).isTrue();
            verify(restService).request(endpoint, requestBody);
            verify(jsonPath).get(JSON_PATH);
        }
    }
}