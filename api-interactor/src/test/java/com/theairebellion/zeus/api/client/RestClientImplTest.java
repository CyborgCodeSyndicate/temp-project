package com.theairebellion.zeus.api.client;

import com.theairebellion.zeus.api.config.ApiConfig;
import com.theairebellion.zeus.api.config.ApiConfigHolder;
import com.theairebellion.zeus.api.log.LogApi;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestClientImpl Tests")
class RestClientImplTest {

    private static final String BASE_URL = "https://example.com";
    private static final String V1_TEST_URL = BASE_URL + "/api/v1/test";
    private static final String V1_OPTIONS_URL = BASE_URL + "/api/v1/options";
    private static final String JSON_PAYLOAD = "{\"hello\":\"world\"}";
    private static final String INVALID_JSON = "This is not JSON";

    @Spy
    private RestClientImpl restClientImpl;

    @Mock
    private FilterableRequestSpecification filterableRequestSpec;

    @Mock
    private Response responseMock;

    @Nested
    @DisplayName("Execute Method Tests")
    class ExecuteMethodTests {

        @Test
        @DisplayName("Non-filterable specification should throw IllegalArgumentException")
        void nonFilterableSpecShouldThrowException() {
            // Arrange
            RequestSpecification plainSpec = mock(RequestSpecification.class);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> restClientImpl.execute(plainSpec, Method.GET),
                "Should throw IllegalArgumentException for non-filterable spec"
            );
            assertEquals(
                "RequestSpecification is not of type FilterableRequestSpecification",
                exception.getMessage()
            );
        }


        @ParameterizedTest(name = "HTTP method {0} should be executed correctly")
        @EnumSource(value = Method.class, names = {"GET", "POST", "PUT", "DELETE", "PATCH", "HEAD"})
        @DisplayName("Supported HTTP methods should be executed correctly")
        void supportedMethodsShouldBeExecutedCorrectly(Method method) {
            // Arrange
            when(filterableRequestSpec.getURI()).thenReturn(V1_TEST_URL);
            when(filterableRequestSpec.getBody()).thenReturn(null);
            when(filterableRequestSpec.getHeaders()).thenReturn(null);

            // Mock the appropriate method call based on the HTTP method
            Response mockResponse = mock(Response.class);
            when(mockResponse.getStatusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(null);
            when(mockResponse.getHeaders()).thenReturn(null);

            switch (method) {
                case GET -> when(filterableRequestSpec.get()).thenReturn(mockResponse);
                case POST -> when(filterableRequestSpec.post()).thenReturn(mockResponse);
                case PUT -> when(filterableRequestSpec.put()).thenReturn(mockResponse);
                case DELETE -> when(filterableRequestSpec.delete()).thenReturn(mockResponse);
                case PATCH -> when(filterableRequestSpec.patch()).thenReturn(mockResponse);
                case HEAD -> when(filterableRequestSpec.head()).thenReturn(mockResponse);
                default -> fail("Unsupported method in test: " + method);
            }

            // Act
            Response actualResponse = restClientImpl.execute(filterableRequestSpec, method);

            // Assert
            assertNotNull(actualResponse, "Response should not be null");
            assertEquals(mockResponse, actualResponse, "Response should match the mock");

            // Verify the correct method was called
            switch (method) {
                case GET -> verify(filterableRequestSpec).get();
                case POST -> verify(filterableRequestSpec).post();
                case PUT -> verify(filterableRequestSpec).put();
                case DELETE -> verify(filterableRequestSpec).delete();
                case PATCH -> verify(filterableRequestSpec).patch();
                case HEAD -> verify(filterableRequestSpec).head();
                default -> fail("Unsupported method in verification: " + method);
            }
        }


        @Test
        @DisplayName("Warn Message in log if requests takes more than 2 seconds")
        void warnMessageShouldBePrintedInLogIfExecuteTakesLongTime() {
            // Arrange
            when(filterableRequestSpec.getURI()).thenReturn(V1_TEST_URL);
            when(filterableRequestSpec.getBody()).thenReturn(null);
            when(filterableRequestSpec.getHeaders()).thenReturn(null);

            // Mock the appropriate method call based on the HTTP method
            Response mockResponse = mock(Response.class);
            when(mockResponse.getStatusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(null);
            when(mockResponse.getHeaders()).thenReturn(null);
            when(filterableRequestSpec.get()).thenAnswer(invocation -> {
                Thread.sleep(3000); // delay for 3 seconds
                return mockResponse;
            });

            try (MockedStatic<LogApi> logApiMock = mockStatic(LogApi.class)) {
                // Act
                restClientImpl.execute(filterableRequestSpec, Method.GET);

                // Assert
                logApiMock.verify(() ->
                                      LogApi.warn(
                                          matches("Request to endpoint .* took too long: .*ms."),
                                          eq("GET"),
                                          eq(V1_TEST_URL),
                                          anyLong()
                                      ), times(1)
                );
            } catch (Exception e) {
                fail("Test interrupted during sleep");
            }
        }


        @Test
        @DisplayName("Unsupported HTTP method should throw IllegalArgumentException")
        void unsupportedMethodShouldThrowException() {
            // Arrange
            when(filterableRequestSpec.getURI()).thenReturn(V1_OPTIONS_URL);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> restClientImpl.execute(filterableRequestSpec, Method.OPTIONS),
                "Should throw IllegalArgumentException for unsupported method"
            );
            assertTrue(
                exception.getMessage().contains("HTTP method OPTIONS is not supported"),
                "Exception message should mention unsupported method"
            );
        }


        @Test
        @DisplayName("Execute should correctly process request with body and headers")
        void shouldCorrectlyProcessRequestWithBodyAndHeaders() {
            // Arrange
            when(filterableRequestSpec.getURI()).thenReturn(V1_TEST_URL);
            when(filterableRequestSpec.getBody()).thenReturn(JSON_PAYLOAD);
            when(filterableRequestSpec.getHeaders()).thenReturn(mock(io.restassured.http.Headers.class));
            when(filterableRequestSpec.get()).thenReturn(responseMock);
            when(responseMock.getStatusCode()).thenReturn(200);
            when(responseMock.body()).thenReturn(null);
            when(responseMock.getHeaders()).thenReturn(null);

            // Act
            Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.GET);

            // Assert
            assertNotNull(actualResponse, "Response should not be null");

            // Verify logging was called with correct parameters
            verify(restClientImpl).printRequest(
                eq("GET"),
                eq(V1_TEST_URL),
                anyString(), // The pretty-printed JSON
                anyString()  // The headers
            );

            verify(restClientImpl).printResponse(
                eq("GET"),
                eq(V1_TEST_URL),
                eq(responseMock),
                anyLong()    // The duration
            );
        }


        @Test
        @DisplayName("Non-filterable specification should throw IllegalArgumentException")
        void nullForMethodArgumentShouldThrowException() {
            // Arrange
            RequestSpecification plainSpec = mock(RequestSpecification.class);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> restClientImpl.execute(plainSpec, null),
                "Should throw IllegalArgumentException for null as method"
            );
            assertEquals(
                "HTTP method must not be null",
                exception.getMessage()
            );
        }

    }

    @Nested
    @DisplayName("JSON Formatting Tests")
    class JsonFormattingTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("tryPrettyPrintJson should handle null and empty inputs")
        void shouldHandleNullAndEmptyInputs(String input) {
            assertEquals(input, restClientImpl.tryPrettyPrintJson(input),
                "Should return input unchanged for null or empty strings");
        }


        @Test
        @DisplayName("tryPrettyPrintJson should format valid JSON")
        void shouldFormatValidJson() {
            // Act
            String prettyJson = restClientImpl.tryPrettyPrintJson(JSON_PAYLOAD);

            // Assert
            assertTrue(
                prettyJson.contains("\n") || prettyJson.contains("\r"),
                "Pretty-printed JSON should contain line breaks"
            );
        }


        @Test
        @DisplayName("tryPrettyPrintJson should return original string for invalid JSON")
        void shouldReturnOriginalStringForInvalidJson() {
            // Act
            String result = restClientImpl.tryPrettyPrintJson(INVALID_JSON);

            // Assert
            assertEquals(INVALID_JSON, result, "Should return original string for invalid JSON");
        }

    }

    @Nested
    @DisplayName("Logging Tests")
    class LoggingTests {

        @Test
        @DisplayName("printRequest should handle null body and headers")
        void printRequestShouldHandleNullBodyAndHeaders() {
            try (MockedStatic<LogApi> logApiMock = mockStatic(LogApi.class)) {
                // Act
                restClientImpl.printRequest("GET", BASE_URL, null, null);

                // Assert
                logApiMock.verify(() ->
                                      LogApi.step("Sending request to endpoint {}-{}.", "GET", BASE_URL), times(1)
                );
                logApiMock.verify(() ->
                                      LogApi.extended("Request body: {}.", ""), times(1)
                );
                logApiMock.verify(() ->
                                      LogApi.extended("Request headers: {}.", ""), times(1)
                );
            } catch (Exception e) {
                fail("Test failed with exception: " + e.getMessage());
            }

        }



        @ParameterizedTest(name = "Should log response correctly when logFullBody = {0}")
        @DisplayName("Logs full or shortened response body based on ApiConfig")
        @CsvSource({
            "true,  { \"message\": \"OK\" },  { \"message\": \"OK\" }, false",
            "false, ABCDEFGHIJKLMNOPQRSTUVWXYZ, ABCDEFGHIJ..., false",
            "true, '', '', true",
            "false, '', '', true"
        })
        void shouldLogResponseBodyBasedOnConfig(boolean logFullBody, String fullBody, String expectedBody, boolean nullBody) {
            // Arrange
            Response mockResponse = mock(Response.class);
            Headers mockHeaders = mock(Headers.class);

            when(mockResponse.getStatusCode()).thenReturn(200);
            if(nullBody){
                when(mockResponse.body()).thenReturn(null);
            }else {
                when(mockResponse.body()).thenReturn(mockResponse);
                when(mockResponse.asPrettyString()).thenReturn(fullBody);
            }
            when(mockResponse.getHeaders()).thenReturn(mockHeaders);
            when(mockHeaders.toString()).thenReturn("Content-Type: application/json");

            ApiConfig mockApiConfig = mock(ApiConfig.class);
            when(mockApiConfig.logFullBody()).thenReturn(logFullBody);
            if (!logFullBody && !nullBody) {
                when(mockApiConfig.shortenBody()).thenReturn(10);
            }

            try (
                MockedStatic<ApiConfigHolder> apiConfigMock = mockStatic(ApiConfigHolder.class);
                MockedStatic<LogApi> logApiMock = mockStatic(LogApi.class)
            ) {
                // Mock static call
                apiConfigMock.when(ApiConfigHolder::getApiConfig).thenReturn(mockApiConfig);

                // Act
                restClientImpl.printResponse("GET", BASE_URL, mockResponse, 1234L);

                // Assert
                logApiMock.verify(() -> LogApi.step(
                    "Response with status: {} received from endpoint: {}-{} in {}ms.",
                    200, "GET", BASE_URL, 1234L
                ), times(1));

                logApiMock.verify(() -> LogApi.extended(
                    "Response body: {}.", expectedBody
                ), times(1));

                logApiMock.verify(() -> LogApi.extended(
                    "Response headers: {}.", "Content-Type: application/json"
                ), times(1));
            }
        }


    }

}