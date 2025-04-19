package com.theairebellion.zeus.api.storage;

import com.theairebellion.zeus.api.mock.TestEnum;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataExtractorsApi Tests")
class DataExtractorsApiTest {

    @Mock
    private Response response;

    @Mock
    private ResponseBody<?> responseBody;

    @Nested
    @DisplayName("ResponseBodyExtraction Tests")
    class ResponseBodyExtractionTests {

        @Test
        @DisplayName("Should extract value from JSON path")
        void shouldExtractValueFromJsonPath() {
            // Arrange
            String jsonPathExpression = "some.json.path";
            String expectedValue = "extractedValue";
            JsonPath jsonPath = new JsonPath("{\"some\":{\"json\":{\"path\":\"" + expectedValue + "\"}}}");

            when(response.body()).thenReturn(responseBody);
            when(responseBody.jsonPath()).thenReturn(jsonPath);

            DataExtractor<String> extractor = DataExtractorsApi.responseBodyExtraction(TestEnum.API_RESPONSE, jsonPathExpression);

            // Act
            String result = extractor.extract(response);

            // Assert
            assertThat(result).isEqualTo(expectedValue);
            verify(response).body();
            verify(responseBody).jsonPath();
        }

        @Test
        @DisplayName("Should handle null JSON path result")
        void shouldHandleNullJsonPathResult() {
            // Arrange
            String jsonPathExpression = "missing.path";
            JsonPath jsonPath = new JsonPath("{\"some\":{\"json\":{\"path\":\"value\"}}}");

            when(response.body()).thenReturn(responseBody);
            when(responseBody.jsonPath()).thenReturn(jsonPath);

            DataExtractor<String> extractor = DataExtractorsApi.responseBodyExtraction(TestEnum.API_RESPONSE, jsonPathExpression);

            // Act
            String result = extractor.extract(response);

            // Assert
            assertThat(result).isNull();
            verify(response).body();
            verify(responseBody).jsonPath();
        }

        @Test
        @DisplayName("Should handle different data types")
        void shouldHandleDifferentDataTypes() {
            // Arrange
            String jsonPathExpression = "some.number";
            JsonPath jsonPath = new JsonPath("{\"some\":{\"number\":42}}");

            when(response.body()).thenReturn(responseBody);
            when(responseBody.jsonPath()).thenReturn(jsonPath);

            DataExtractor<Integer> extractor = DataExtractorsApi.responseBodyExtraction(TestEnum.API_RESPONSE, jsonPathExpression);

            // Act
            Integer result = extractor.extract(response);

            // Assert
            assertThat(result).isEqualTo(42);
        }

        @Test
        @DisplayName("Should throw exception if input is not a Response")
        void shouldThrowExceptionIfInputIsNotResponse() {
            // Arrange
            String jsonPathExpression = "some.path";
            DataExtractor<String> extractor = DataExtractorsApi.responseBodyExtraction(TestEnum.API_RESPONSE, jsonPathExpression);
            Object notAResponse = new Object();

            // Act & Assert
            assertThatThrownBy(() -> extractor.extract(notAResponse))
                    .isInstanceOf(ClassCastException.class);
        }
    }

    @Nested
    @DisplayName("StatusExtraction Tests")
    class StatusExtractionTests {

        static Stream<Arguments> statusCodesProvider() {
            return Stream.of(
                    Arguments.of(200, "OK"),
                    Arguments.of(201, "Created"),
                    Arguments.of(400, "Bad Request"),
                    Arguments.of(404, "Not Found"),
                    Arguments.of(500, "Server Error")
            );
        }

        @ParameterizedTest(name = "Status code {0} should be extracted correctly")
        @MethodSource("statusCodesProvider")
        @DisplayName("Should extract different status codes")
        void shouldExtractDifferentStatusCodes(int statusCode) {
            // Arrange
            when(response.statusCode()).thenReturn(statusCode);
            DataExtractor<Integer> extractor = DataExtractorsApi.statusExtraction(TestEnum.API_RESPONSE);

            // Act
            Integer result = extractor.extract(response);

            // Assert
            assertThat(result).isEqualTo(statusCode);
            verify(response).statusCode();
        }

        @Test
        @DisplayName("Should throw exception if input is not a Response")
        void shouldThrowExceptionIfInputIsNotResponse() {
            // Arrange
            DataExtractor<Integer> extractor = DataExtractorsApi.statusExtraction(TestEnum.API_RESPONSE);
            Object notAResponse = new Object();

            // Act & Assert
            assertThatThrownBy(() -> extractor.extract(notAResponse))
                    .isInstanceOf(ClassCastException.class);
        }
    }

    @Nested
    @DisplayName("Utility Constructor Test")
    class UtilityConstructorTest {

        @Test
        @DisplayName("Should instantiate DataExtractorsApi via reflection")
        void shouldInstantiateViaReflection() throws Exception {
            var constructor = DataExtractorsApi.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            Object instance = constructor.newInstance();

            assertThat(instance).isNotNull();
        }
    }
}