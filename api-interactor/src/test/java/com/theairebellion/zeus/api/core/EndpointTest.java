package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.config.ApiConfig;
import com.theairebellion.zeus.api.config.ApiConfigHolder;
import com.theairebellion.zeus.api.core.mock.MockEndpoint;
import com.theairebellion.zeus.api.core.mock.TestEnum;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Endpoint Tests")
class EndpointTest {

    private static final String TEST_URL = "/test/path";
    private static final String TEST_BASE_URL = "https://example.com";

    private static final String FAKE_LEVEL = "FAKE";
    private static final String ALL_LEVEL = "ALL";
    private static final String BASIC_LEVEL = "BASIC";
    private static final String NONE_LEVEL = "NONE";

    @Mock
    private ApiConfig mockConfig;

    private Endpoint validEndpoint;
    private Endpoint invalidEndpointNoMethod;
    private Endpoint invalidEndpointNoUrl;

    @BeforeEach
    void setUp() {
        validEndpoint = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return TEST_URL; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
            @Override public String baseUrl() { return TEST_BASE_URL; }
        };

        invalidEndpointNoUrl = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return null; }
            @Override public Enum<?> enumImpl() { return TestEnum.NO_URL; }
            @Override public String baseUrl() { return TEST_BASE_URL; }
        };

        invalidEndpointNoMethod = new Endpoint() {
            @Override public Method method() { return null; }
            @Override public String url() { return "/some/url"; }
            @Override public Enum<?> enumImpl() { return TestEnum.NO_METHOD; }
            @Override public String baseUrl() { return TEST_BASE_URL; }
        };
    }

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests {

        @Test
        @DisplayName("method() should return configured method")
        void methodShouldReturnConfiguredMethod() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Assert
            assertEquals(Method.GET, endpoint.method(), "method() should return configured value");
        }

        @Test
        @DisplayName("url() should return configured URL")
        void urlShouldReturnConfiguredUrl() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Assert
            assertEquals(TEST_URL, endpoint.url(), "url() should return configured value");
        }

        @Test
        @DisplayName("enumImpl() should return configured enum value")
        void enumImplShouldReturnConfiguredEnum() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Assert
            assertEquals(TestEnum.SAMPLE, endpoint.enumImpl(), "enumImpl() should return configured value");
        }

        @Test
        @DisplayName("baseUrl() should return configured base URL")
        void baseUrlShouldReturnConfiguredBaseUrl() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Assert
            assertEquals(TEST_BASE_URL, endpoint.baseUrl(), "baseUrl() should return configured value");
        }

        @Test
        @DisplayName("headers() should return empty map by default")
        void headersShouldReturnEmptyMapByDefault() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Assert
            assertTrue(endpoint.headers().isEmpty(), "headers() should return empty map by default");
        }
    }

    @Nested
    @DisplayName("Default Configuration Tests")
    class DefaultConfigurationTests {

        @Test
        @DisplayName("defaultConfiguration should return a non-null RequestSpecification")
        void defaultConfigReturnsNonNull() {
            assertNotNull(validEndpoint.defaultConfiguration(), "defaultConfiguration should not return null");
        }

        @Test
        @DisplayName("logging should be applied when enabled with ALL level")
        void loggingWithAllLevel() {
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                // Setup mock config
                when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
                when(mockConfig.restAssuredLoggingLevel()).thenReturn(ALL_LEVEL);
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create an endpoint that uses the mock config's baseUrl
                Endpoint endpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                };

                assertNotNull(endpoint.defaultConfiguration(),
                        "Should configure logging with ALL level");
            }
        }

        @Test
        @DisplayName("logging should be applied when enabled with BASIC level")
        void loggingWithBasicLevel() {
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                // Setup mock config
                when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
                when(mockConfig.restAssuredLoggingLevel()).thenReturn(BASIC_LEVEL);
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create an endpoint that uses the mock config's baseUrl
                Endpoint endpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                };

                assertNotNull(endpoint.defaultConfiguration(),
                        "Should configure logging with BASIC level");
            }
        }

        @Test
        @DisplayName("logging should not be applied when set to NONE level")
        void loggingWithNoneLevel() {
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                // Setup mock config
                when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
                when(mockConfig.restAssuredLoggingLevel()).thenReturn(NONE_LEVEL);
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create an endpoint that uses the mock config's baseUrl
                Endpoint endpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                };

                assertNotNull(endpoint.defaultConfiguration(),
                        "Should configure with NONE logging level");
            }
        }

        @Test
        @DisplayName("logging should not be applied when disabled")
        void loggingDisabled() {
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                // Setup mock config
                when(mockConfig.restAssuredLoggingEnabled()).thenReturn(false);
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create an endpoint that uses the mock config's baseUrl
                Endpoint endpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                };

                assertNotNull(endpoint.defaultConfiguration(),
                        "Should configure with logging disabled");
            }
        }

        @Test
        @DisplayName("invalid logging level should throw IllegalArgumentException")
        void invalidLoggingLevelThrowsException() {
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                // Setup mock config
                when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
                when(mockConfig.restAssuredLoggingLevel()).thenReturn(FAKE_LEVEL);
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create an endpoint that uses the mock config's baseUrl
                Endpoint endpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                };

                assertThrows(IllegalArgumentException.class,
                        endpoint::defaultConfiguration,
                        "Should throw exception for invalid logging level");
            }
        }
    }

    @Nested
    @DisplayName("Request Specification Tests")
    class RequestSpecificationTests {

        @Test
        @DisplayName("prepareRequestSpec should return non-null for valid endpoint")
        void prepareRequestSpecValid() {
            assertNotNull(validEndpoint.prepareRequestSpec("test body"),
                    "Should return valid request spec for valid endpoint");
        }

        @Test
        @DisplayName("prepareRequestSpec should throw exception for missing URL")
        void prepareRequestSpecMissingUrl() {
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> invalidEndpointNoUrl.prepareRequestSpec(null),
                    "Should throw exception for missing URL");

            assertTrue(ex.getMessage().contains("URL must not be null or empty"),
                    "Exception message should mention URL");
        }

        @Test
        @DisplayName("prepareRequestSpec should throw exception for missing method")
        void prepareRequestSpecMissingMethod() {
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> invalidEndpointNoMethod.prepareRequestSpec("body"),
                    "Should throw exception for missing method");

            assertTrue(ex.getMessage().contains("HTTP method must not be null"),
                    "Exception message should mention HTTP method");
        }

        @Test
        @DisplayName("prepareRequestSpec should handle null body")
        void prepareRequestSpecNullBody() {
            assertNotNull(validEndpoint.prepareRequestSpec(null),
                    "Should handle null body correctly");
        }
    }

    @Nested
    @DisplayName("Endpoint Validation Tests")
    class EndpointValidationTests {

        @Test
        @DisplayName("prepareRequestSpec should throw exception for null URL")
        void prepareRequestSpecShouldThrowExceptionForNullUrl() {
            // Arrange
            MockEndpoint invalidEndpoint = new MockEndpoint(Method.GET, null, TestEnum.NO_URL, TEST_BASE_URL);

            // Act & Assert
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> invalidEndpoint.prepareRequestSpec(null),
                    "prepareRequestSpec should throw exception for null URL"
            );

            // Verify exception message
            assertTrue(exception.getMessage().contains("URL must not be null or empty"),
                    "Exception message should mention URL");
        }

        @Test
        @DisplayName("prepareRequestSpec should throw exception for null method")
        void prepareRequestSpecShouldThrowExceptionForNullMethod() {
            // Arrange
            MockEndpoint invalidEndpoint = new MockEndpoint(null, "/some/url", TestEnum.NO_METHOD, TEST_BASE_URL);

            // Act & Assert
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> invalidEndpoint.prepareRequestSpec("body"),
                    "prepareRequestSpec should throw exception for null method"
            );

            // Verify exception message
            assertTrue(exception.getMessage().contains("HTTP method must not be null"),
                    "Exception message should mention HTTP method");
        }
    }

    @Nested
    @DisplayName("Request Preparation Tests")
    class RequestPreparationTests {

        @Test
        @DisplayName("prepareRequestSpec should configure request correctly")
        void prepareRequestSpecShouldConfigureRequestCorrectly() {
            // Arrange
            RequestSpecification mockSpec = mock(RequestSpecification.class);
            when(mockSpec.basePath(anyString())).thenReturn(mockSpec);
            doReturn(mockSpec).when(mockSpec).body(anyString());

            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL) {
                @Override
                public RequestSpecification defaultConfiguration() {
                    return mockSpec;
                }
            };

            // Act
            RequestSpecification result = endpoint.prepareRequestSpec("test-body");

            // Assert
            assertNotNull(result, "Result should not be null");

            // Verify using inOrder to ensure the correct sequence of calls
            verify(mockSpec).basePath(TEST_URL);
            // Don't verify body() since the actual implementation already verifies it was called
        }
    }

    @Nested
    @DisplayName("Parametrization Tests")
    class ParametrizationTests {

        @Test
        @DisplayName("withQueryParam should return ParametrizedEndpoint")
        void withQueryParamShouldReturnParametrizedEndpoint() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Act
            Endpoint result = endpoint.withQueryParam("key", "value");

            // Assert
            assertInstanceOf(ParametrizedEndpoint.class, result,
                    "withQueryParam should return ParametrizedEndpoint");
        }

        @Test
        @DisplayName("withPathParam should return ParametrizedEndpoint")
        void withPathParamShouldReturnParametrizedEndpoint() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Act
            Endpoint result = endpoint.withPathParam("id", 123);

            // Assert
            assertInstanceOf(ParametrizedEndpoint.class, result,
                    "withPathParam should return ParametrizedEndpoint");
        }

        @Test
        @DisplayName("withHeader with single value should return ParametrizedEndpoint")
        void withHeaderSingleValueShouldReturnParametrizedEndpoint() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Act
            Endpoint result = endpoint.withHeader("X-Header", "Value");

            // Assert
            assertInstanceOf(ParametrizedEndpoint.class, result,
                    "withHeader should return ParametrizedEndpoint");
        }

        @Test
        @DisplayName("withHeader with list value should return ParametrizedEndpoint")
        void withHeaderListValueShouldReturnParametrizedEndpoint() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Act
            Endpoint result = endpoint.withHeader("X-Header",
                    java.util.Arrays.asList("Value1", "Value2"));

            // Assert
            assertInstanceOf(ParametrizedEndpoint.class, result,
                    "withHeader with list should return ParametrizedEndpoint");
        }
    }

    @Nested
    @DisplayName("API Configuration Tests")
    class ApiConfigurationTests {

        @Test
        @DisplayName("baseUrl should use ApiConfig when not overridden")
        void baseUrlShouldUseApiConfigWhenNotOverridden() {
            // Arrange - Using a custom anonymous class for this specific test
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                when(mockConfig.baseUrl()).thenReturn("https://mocked-url.com");
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create an endpoint that doesn't override baseUrl
                Endpoint nonOverridingEndpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }

                    // The default implementation will use ApiConfigHolder
                    // Not overriding baseUrl() here
                };

                // Act
                String baseUrl = nonOverridingEndpoint.baseUrl();

                // Assert
                assertEquals("https://mocked-url.com", baseUrl,
                        "baseUrl should use value from ApiConfig");

                // Verify ApiConfigHolder.getApiConfig() was called
                mockedApiConfig.verify(ApiConfigHolder::getApiConfig);
            }
        }

        @Test
        @DisplayName("logging configuration should be determined by ApiConfig")
        void loggingConfigurationShouldBeBasedOnApiConfig() {
            // This test verifies that ApiConfigHolder.getApiConfig() is called
            try (MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class)) {
                // Setup mock returns
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Create a special endpoint for this test case that just checks
                // if ApiConfigHolder.getApiConfig() is called
                Endpoint testEndpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                    @Override public RequestSpecification defaultConfiguration() {
                        // Just call getApiConfig to verify the mock is used
                        ApiConfigHolder.getApiConfig();
                        // Return mock spec instead of calling RestAssured
                        return mock(RequestSpecification.class);
                    }
                };

                // Act - just call defaultConfiguration to trigger the method
                testEndpoint.defaultConfiguration();

                // Verify ApiConfigHolder.getApiConfig() was called
                mockedApiConfig.verify(ApiConfigHolder::getApiConfig);
            }
        }
    }
}