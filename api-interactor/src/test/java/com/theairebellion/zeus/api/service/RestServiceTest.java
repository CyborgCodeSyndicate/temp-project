package com.theairebellion.zeus.api.service;

import com.theairebellion.zeus.api.authentication.AuthenticationKey;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.exceptions.RestServiceException;
import com.theairebellion.zeus.api.service.mock.MockAuthClient;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestService Tests")
class RestServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestResponseValidator restResponseValidator;

    @Mock
    private Response responseMock;

    private RestService restService;

    @BeforeEach
    void setUp() {
        restService = new RestService(restClient, restResponseValidator);
    }

    @Nested
    @DisplayName("Request Methods Tests")
    class RequestMethodsTests {
        @ParameterizedTest
        @DisplayName("Request methods with different HTTP methods")
        @MethodSource("provideHttpMethodScenarios")
        void requestMethodsShouldHandleDifferentHttpMethods(Method httpMethod, Object body) {
            // Arrange
            Endpoint endpoint = mock(Endpoint.class);
            RequestSpecification specMock = mock(RequestSpecification.class);

            when(endpoint.prepareRequestSpec(body)).thenReturn(specMock);
            when(endpoint.method()).thenReturn(httpMethod);
            when(restClient.execute(any(RequestSpecification.class), eq(httpMethod))).thenReturn(responseMock);

            // Act
            Response result = body == null
                    ? restService.request(endpoint)
                    : restService.request(endpoint, body);

            // Assert
            assertEquals(responseMock, result, "Should return the response from restClient");

            // Verify
            verify(endpoint).prepareRequestSpec(body);
            verify(endpoint).method();
            verify(restClient).execute(specMock, httpMethod);
        }

        // Provides scenarios for different HTTP methods and body conditions
        private static Stream<Arguments> provideHttpMethodScenarios() {
            return Stream.of(
                    Arguments.of(Method.GET, null),
                    Arguments.of(Method.POST, "someBody"),
                    Arguments.of(Method.PUT, "updateBody"),
                    Arguments.of(Method.DELETE, null)
            );
        }

        @Test
        @DisplayName("request() with null endpoint should throw RestServiceException")
        void requestWithNullEndpointShouldThrow() {
            assertThrows(RestServiceException.class,
                    () -> restService.request(null),
                    "Should throw RestServiceException for null endpoint");
        }

        @Test
        @DisplayName("executeRequest() should wrap exceptions in RestServiceException")
        void executeRequestShouldWrapExceptionInRestServiceException() {
            // Arrange
            Endpoint endpoint = mock(Endpoint.class);
            when(endpoint.prepareRequestSpec(null)).thenThrow(new RuntimeException("boom"));
            when(endpoint.url()).thenReturn("/test");

            // Act & Assert
            RestServiceException exception = assertThrows(RestServiceException.class,
                    () -> restService.request(endpoint),
                    "Should throw RestServiceException when an error occurs");

            assertTrue(exception.getMessage().contains("/test"),
                    "Exception message should include the endpoint URL");
            assertInstanceOf(RuntimeException.class, exception.getCause(),
                    "Cause should be the original exception");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {
        @Test
        @DisplayName("validate() should pass response and assertions to validator")
        void validateShouldPassResponseAndAssertionsToValidator() {
            // Arrange
            @SuppressWarnings("unchecked")
            List<AssertionResult<String>> expectedResults = mock(List.class);
            Assertion<?> mockAssertion1 = mock(Assertion.class);
            Assertion<?> mockAssertion2 = mock(Assertion.class);

            when(restResponseValidator.validateResponse(eq(responseMock), any(Assertion[].class)))
                    .thenReturn((List) expectedResults);

            // Act
            List<AssertionResult<String>> results = restService.validate(responseMock, mockAssertion1, mockAssertion2);

            // Assert
            assertSame(expectedResults, results, "Should return results from validator");

            // Verify
            verify(restResponseValidator).validateResponse(responseMock, mockAssertion1, mockAssertion2);
        }

        @Test
        @DisplayName("validate() with null response should throw IllegalArgumentException")
        void validateWithNullResponseShouldThrow() {
            Assertion<?> mockAssertion = mock(Assertion.class);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> restService.validate(null, mockAssertion),
                    "Should throw IllegalArgumentException for null response");

            assertTrue(exception.getMessage().contains("Response cannot be null"),
                    "Exception message should mention null response");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("validate() with null or empty assertions should throw IllegalArgumentException")
        void validateWithNoAssertionsShouldThrow(Assertion<?>[] assertions) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> restService.validate(responseMock, assertions),
                    "Should throw IllegalArgumentException for null or empty assertions");

            assertTrue(exception.getMessage().contains("At least one assertion"),
                    "Exception message should mention assertions requirement");
        }

        @Test
        @DisplayName("Validate with validator throwing exception")
        void validateWithValidatorException() {
            Assertion<?> mockAssertion = mock(Assertion.class);

            when(restResponseValidator.validateResponse(eq(responseMock), any(Assertion[].class)))
                    .thenThrow(new RuntimeException("Validation failed"));

            assertThrows(RuntimeException.class,
                    () -> restService.validate(responseMock, mockAssertion)
            );
        }
    }

    @Nested
    @DisplayName("Request and Validate Tests")
    class RequestAndValidateTests {
        @Test
        @DisplayName("requestAndValidate() with endpoint and body")
        void requestAndValidateWithEndpointAndBody() {
            // Arrange: Set up the test scenario with mocked dependencies
            // Create a mock endpoint to simulate an API endpoint
            Endpoint endpoint = mock(Endpoint.class);

            // Prepare a sample body for the request
            Object body = "testRequestBody";

            // Create a mock assertion for validation
            Assertion<?> mockAssertion = mock(Assertion.class);

            // Mock the request specification preparation
            RequestSpecification specMock = mock(RequestSpecification.class);
            when(endpoint.prepareRequestSpec(body)).thenReturn(specMock);

            // Define the HTTP method for the request
            when(endpoint.method()).thenReturn(Method.POST);

            // Mock the client execution to return a response
            when(restClient.execute(any(), eq(Method.POST))).thenReturn(responseMock);

            // Prepare mock validation results
            @SuppressWarnings("unchecked")
            List<AssertionResult<String>> expectedResults = mock(List.class);
            when(restResponseValidator.validateResponse(eq(responseMock), any(Assertion[].class)))
                    .thenReturn((List) expectedResults);

            // Act: Perform the request and validation
            List<AssertionResult<String>> results = restService.requestAndValidate(endpoint, body, mockAssertion);

            // Assert: Verify the expected behaviors
            assertSame(expectedResults, results, "Should return results from validator");

            // Verify interactions with mocked dependencies
            verify(restClient).execute(any(), eq(Method.POST));
            verify(restResponseValidator).validateResponse(eq(responseMock), eq(mockAssertion));
        }

        @Test
        @DisplayName("requestAndValidate() with endpoint")
        void requestAndValidateWithEndpoint() {
            // Arrange: Set up the test scenario with mocked dependencies
            // Create a mock endpoint to simulate an API endpoint
            Endpoint endpoint = mock(Endpoint.class);

            // Create a mock assertion for validation
            Assertion<?> mockAssertion = mock(Assertion.class);

            // Mock the request specification preparation with null body
            RequestSpecification specMock = mock(RequestSpecification.class);
            when(endpoint.prepareRequestSpec(null)).thenReturn(specMock);

            // Define the HTTP method for the request
            when(endpoint.method()).thenReturn(Method.GET);

            // Mock the client execution to return a response
            when(restClient.execute(any(), eq(Method.GET))).thenReturn(responseMock);

            // Prepare mock validation results
            @SuppressWarnings("unchecked")
            List<AssertionResult<String>> expectedResults = mock(List.class);
            when(restResponseValidator.validateResponse(eq(responseMock), any(Assertion[].class)))
                    .thenReturn((List) expectedResults);

            // Act: Perform the request and validation
            List<AssertionResult<String>> results = restService.requestAndValidate(endpoint, mockAssertion);

            // Assert: Verify the expected behaviors
            assertSame(expectedResults, results, "Should return results from validator");

            // Verify interactions with mocked dependencies
            verify(restClient).execute(any(), eq(Method.GET));
            verify(restResponseValidator).validateResponse(eq(responseMock), eq(mockAssertion));
        }
    }

    @Nested
    @DisplayName("Authentication Tests")
    class AuthenticationTests {
        @ParameterizedTest
        @DisplayName("Authentication with invalid inputs")
        @MethodSource("provideInvalidAuthenticationInputs")
        void authenticateWithInvalidInputs(String username, String password, Class<? extends BaseAuthenticationClient> authClientClass, Class<? extends Exception> expectedException) {
            // Act & Assert
            assertThrows(expectedException,
                    () -> restService.authenticate(username, password, authClientClass),
                    "Should throw expected exception for invalid input"
            );
        }

        // Provides scenarios for invalid authentication inputs
        private static Stream<Arguments> provideInvalidAuthenticationInputs() {
            return Stream.of(
                    Arguments.of(null, "password", MockAuthClient.class, NullPointerException.class),
                    Arguments.of("username", null, MockAuthClient.class, NullPointerException.class),
                    Arguments.of("username", "password", null, NullPointerException.class)
            );
        }

        @Test
        @DisplayName("authenticate() with reflection errors should throw RestServiceException")
        void authenticateWithReflectionErrorsShouldThrow() {
            // Define a class with private constructor that can't be instantiated
            class PrivateConstructorAuthClient extends BaseAuthenticationClient {
                private PrivateConstructorAuthClient() {
                }

                @Override
                public AuthenticationKey authenticate(RestService restService, String user, String pass, boolean cache) {
                    return null;
                }

                @Override
                protected Header authenticateImpl(RestService restService, String username, String password) {
                    return null;
                }
            }

            RestServiceException exception = assertThrows(RestServiceException.class,
                    () -> restService.authenticate("user", "pass", PrivateConstructorAuthClient.class),
                    "Should throw RestServiceException for reflection errors");

            assertTrue(exception.getMessage().contains("Error instantiating"),
                    "Exception message should mention instantiation error");
        }

        @Test
        @DisplayName("Successful authentication with MockAuthClient")
        void successfulAuthentication() {
            restService.setCacheAuthentication(true);
            assertDoesNotThrow(() ->
                    restService.authenticate("testUser", "testPass", MockAuthClient.class)
            );
        }

        @Test
        @DisplayName("Authentication with MockAuthClient")
        void authenticationWithMockClient() {
            // Ensure cache authentication is set
            restService.setCacheAuthentication(true);

            // Act & Assert
            assertDoesNotThrow(() ->
                            restService.authenticate("user", "pass", MockAuthClient.class),
                    "Authentication with MockAuthClient should not throw an exception"
            );
        }

        @Test
        @DisplayName("Authentication with client throwing exception")
        void authenticationWithClientException() {
            // Exception throwing AuthClient implementation
            class ExceptionThrowingAuthClient extends BaseAuthenticationClient {
                @Override
                public AuthenticationKey authenticate(RestService restService, String user, String pass, boolean cache) {
                    throw new RuntimeException("Authentication failed");
                }

                @Override
                protected Header authenticateImpl(RestService restService, String username, String password) {
                    return null;
                }
            }

            assertThrows(RestServiceException.class, () ->
                    restService.authenticate("user", "pass", ExceptionThrowingAuthClient.class)
            );
        }
    }

    @Nested
    @DisplayName("Cache Control Tests")
    class CacheControlTests {
        @ParameterizedTest
        @DisplayName("Set cache authentication with different values")
        @ValueSource(booleans = {true, false})
        void setCacheAuthenticationWithDifferentValues(boolean cacheValue) throws Exception {
            // Act
            restService.setCacheAuthentication(cacheValue);

            // Assert via reflection
            java.lang.reflect.Field cacheField = RestService.class.getDeclaredField("cacheAuthentication");
            cacheField.setAccessible(true);
            assertEquals(cacheValue, cacheField.get(restService), "cacheAuthentication should match set value");
        }

        // Existing tests from the original class remain the same
        @Test
        @DisplayName("constructor with cacheAuthentication should set the field")
        void constructorWithCacheAuthenticationShouldSetField() throws Exception {
            // Act
            RestService service = new RestService(restClient, restResponseValidator, true);

            // Assert via reflection
            java.lang.reflect.Field cacheField = RestService.class.getDeclaredField("cacheAuthentication");
            cacheField.setAccessible(true);
            assertTrue((boolean) cacheField.get(service), "cacheAuthentication should be set to true");
        }

        @Test
        @DisplayName("Constructor with default cache authentication")
        void constructorWithDefaultCacheAuthentication() throws Exception {
            RestService serviceWithoutCache = new RestService(restClient, restResponseValidator);

            java.lang.reflect.Field cacheField = RestService.class.getDeclaredField("cacheAuthentication");
            cacheField.setAccessible(true);
            assertFalse((boolean) cacheField.get(serviceWithoutCache), "Cache authentication should be false by default");
        }
    }

    @Nested
    @DisplayName("Execute Request Tests")
    class ExecuteRequestTests {
        @Test
        @DisplayName("Execute request with null endpoint should throw exception")
        void executeRequestWithNullEndpointShouldThrow() {
            // Act & Assert
            RestServiceException exception = assertThrows(RestServiceException.class,
                    () -> restService.request(null),
                    "Should throw RestServiceException for null endpoint"
            );

            assertTrue(exception.getMessage().contains("Endpoint cannot be null"),
                    "Exception message should indicate null endpoint");
        }

        @Test
        @DisplayName("Execute request with authentication header")
        void executeRequestWithAuthenticationHeader() {
            // Arrange
            Endpoint endpoint = mock(Endpoint.class);
            Object body = "testBody";
            RequestSpecification specMock = mock(RequestSpecification.class);

            // Mock authentication client and key
            BaseAuthenticationClient mockAuthClient = mock(BaseAuthenticationClient.class);
            AuthenticationKey mockAuthKey = new AuthenticationKey("user", "pass", MockAuthClient.class);
            Header mockAuthHeader = new Header("Authorization", "Bearer testToken");

            // Set up mocking for the request execution
            when(endpoint.prepareRequestSpec(body)).thenReturn(specMock);
            when(endpoint.method()).thenReturn(Method.POST);
            when(restClient.execute(specMock, Method.POST)).thenReturn(responseMock);

            // Set private fields via reflection to simulate authenticated state
            try {
                java.lang.reflect.Field baseClientField = RestService.class.getDeclaredField("baseAuthenticationClient");
                baseClientField.setAccessible(true);
                baseClientField.set(restService, mockAuthClient);

                java.lang.reflect.Field authKeyField = RestService.class.getDeclaredField("authenticationKey");
                authKeyField.setAccessible(true);
                authKeyField.set(restService, mockAuthKey);
            } catch (Exception e) {
                fail("Could not set up test via reflection: " + e.getMessage());
            }

            // Mock authentication header retrieval
            when(mockAuthClient.getAuthentication(mockAuthKey)).thenReturn(mockAuthHeader);

            // Act
            Response response = restService.request(endpoint, body);

            // Assert
            assertNotNull(response, "Response should not be null");
            verify(specMock).header(mockAuthHeader);
            verify(restClient).execute(specMock, Method.POST);
        }

        @Test
        @DisplayName("Execute request with no authentication header")
        void executeRequestWithNoAuthenticationHeader() {
            // Arrange
            Endpoint endpoint = mock(Endpoint.class);
            Object body = "testBody";
            RequestSpecification specMock = mock(RequestSpecification.class);

            // Mock authentication client and key
            BaseAuthenticationClient mockAuthClient = mock(BaseAuthenticationClient.class);
            AuthenticationKey mockAuthKey = new AuthenticationKey("user", "pass", MockAuthClient.class);

            // Set up mocking for the request execution
            when(endpoint.prepareRequestSpec(body)).thenReturn(specMock);
            when(endpoint.method()).thenReturn(Method.POST);
            when(restClient.execute(specMock, Method.POST)).thenReturn(responseMock);

            // Set private fields via reflection to simulate authenticated state
            try {
                java.lang.reflect.Field baseClientField = RestService.class.getDeclaredField("baseAuthenticationClient");
                baseClientField.setAccessible(true);
                baseClientField.set(restService, mockAuthClient);

                java.lang.reflect.Field authKeyField = RestService.class.getDeclaredField("authenticationKey");
                authKeyField.setAccessible(true);
                authKeyField.set(restService, mockAuthKey);
            } catch (Exception e) {
                fail("Could not set up test via reflection: " + e.getMessage());
            }

            // Mock no authentication header
            when(mockAuthClient.getAuthentication(mockAuthKey)).thenReturn(null);

            // Act
            Response response = restService.request(endpoint, body);

            // Assert
            assertNotNull(response, "Response should not be null");
            verify(specMock, never()).header(any(Header.class));
            verify(restClient).execute(specMock, Method.POST);
        }

        @Test
        @DisplayName("Execute request with exception should wrap in RestServiceException")
        void executeRequestWithExceptionShouldWrap() {
            // Arrange
            Endpoint endpoint = mock(Endpoint.class);
            when(endpoint.prepareRequestSpec(null)).thenThrow(new RuntimeException("Test exception"));
            when(endpoint.url()).thenReturn("/test-endpoint");

            // Act & Assert
            RestServiceException exception = assertThrows(RestServiceException.class,
                    () -> restService.request(endpoint),
                    "Should throw RestServiceException when underlying execution fails"
            );

            // Verify exception details
            assertTrue(exception.getMessage().contains("/test-endpoint"),
                    "Exception message should include endpoint URL");
            assertInstanceOf(RuntimeException.class, exception.getCause(),
                    "Cause should be the original exception");
        }
    }
}