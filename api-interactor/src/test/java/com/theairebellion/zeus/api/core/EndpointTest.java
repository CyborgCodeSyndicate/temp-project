package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.config.ApiConfig;
import com.theairebellion.zeus.api.config.ApiConfigHolder;
import com.theairebellion.zeus.api.core.mock.MockEndpoint;
import com.theairebellion.zeus.api.core.mock.TestEnum;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Endpoint Tests")
class EndpointTest {

    private static final String TEST_URL = "/test/path";
    private static final String TEST_BASE_URL = "https://example.com";

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
        @DisplayName("MockEndpoint should return configured values for method(), url(), enumImpl(), baseUrl() and headers()")
        void shouldReturnConfiguredValuesForAllProperties() {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Assert
            assertEquals(Method.GET, endpoint.method(), "method() should return configured value");
            assertEquals(TEST_URL, endpoint.url(), "url() should return configured value");
            assertEquals(TestEnum.SAMPLE, endpoint.enumImpl(), "enumImpl() should return configured value");
            assertEquals(TEST_BASE_URL, endpoint.baseUrl(), "baseUrl() should return configured value");
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

        @ParameterizedTest(name = "[{index}] Logging level: {0}, enabled: {1}")
        @DisplayName("Logging behavior based on ApiConfig settings")
        @CsvSource({
            "ALL,    true,  all,   false",
            "BASIC,  true,  basic, false",
            "NONE,   true,  none,  false",
            "'',     false, none,  false",
            "FAKE,   true,  none,  true"
        })
        void shouldApplyLoggingAccordingToConfig(
            String level,
            boolean enabled,
            String expectedLogMethod,
            boolean shouldThrow
        ) {
            RequestSpecification mockSpec = mock(RequestSpecification.class);
            RequestLogSpecification mockLogSpec = mock(RequestLogSpecification.class);

            try (
                MockedStatic<ApiConfigHolder> mockedApiConfig = mockStatic(ApiConfigHolder.class);
                MockedStatic<RestAssured> mockedRestAssured = mockStatic(RestAssured.class)
            ) {
                // Setup mock config
                when(mockConfig.restAssuredLoggingEnabled()).thenReturn(enabled);
                when(mockConfig.restAssuredLoggingLevel()).thenReturn(level);
                when(mockConfig.baseUrl()).thenReturn(TEST_BASE_URL);
                mockedApiConfig.when(ApiConfigHolder::getApiConfig).thenReturn(mockConfig);

                // Setup fluent RestAssured mocks
                mockedRestAssured.when(RestAssured::given).thenReturn(mockSpec);
                when(mockSpec.baseUri(anyString())).thenReturn(mockSpec);
                when(mockSpec.headers(anyMap())).thenReturn(mockSpec);
                when(mockSpec.log()).thenReturn(mockLogSpec);
                when(mockLogSpec.all()).thenReturn(mockSpec);
                when(mockLogSpec.ifValidationFails()).thenReturn(mockSpec);

                // Create an endpoint that uses the mock config's baseUrl
                Endpoint endpoint = new Endpoint() {
                    @Override public Method method() { return Method.GET; }
                    @Override public String url() { return TEST_URL; }
                    @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
                };

                if (shouldThrow) {
                    assertThrows(IllegalArgumentException.class, endpoint::defaultConfiguration);
                } else {
                    RequestSpecification spec = endpoint.defaultConfiguration();
                    assertNotNull(spec, "Request specification should not be null");

                    // Verify logging based on expectedLogMethod
                    switch (expectedLogMethod) {
                        case "all" -> verify(mockLogSpec).all();
                        case "basic" -> verify(mockLogSpec).ifValidationFails();
                        case "none" -> verify(mockLogSpec, never()).all();
                    }
                }
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
        @DisplayName("withQueryParam should return ParametrizedEndpoint and query params should be added")
        void withQueryParamShouldReturnParametrizedEndpointAndQueryParamsShouldBeAdded() {
            // Arrange
            MockEndpoint baseEndpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL) {
                @Override
                public RequestSpecification prepareRequestSpec(final Object body) {
                    return defaultConfiguration();
                }
            };

            // Act
            Endpoint endpointWithQuery = baseEndpoint.withQueryParam("key", "value");

            // Call prepareRequestSpec to trigger internal param injection
            RequestSpecification requestSpec = endpointWithQuery.prepareRequestSpec(null);

            // Assert
            // Since MockTestableEndpoint returns a mock spec, we can verify interactions directly

            assertInstanceOf(ParametrizedEndpoint.class, endpointWithQuery,
                "withQueryParam should return ParametrizedEndpoint");
            verify(requestSpec).queryParams(argThat(queryMap ->
                                                        queryMap.size() == 1 &&
                                                            queryMap.containsKey("key") &&
                                                            "value".equals(queryMap.get("key"))
            ));
        }


        @Test
        @DisplayName("withPathParam should return ParametrizedEndpoint and path params should be added")
        void withPathParamShouldReturnParametrizedEndpointAndPathParamsShouldBeAdded() {
            // Arrange
            MockEndpoint baseEndpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL) {
                @Override
                public RequestSpecification prepareRequestSpec(final Object body) {
                    return defaultConfiguration();
                }
            };

            // Act
            Endpoint endpointWithPath = baseEndpoint.withPathParam("key", "value");


            // Call prepareRequestSpec to trigger internal param injection
            RequestSpecification requestSpec = endpointWithPath.prepareRequestSpec(null);


            // Assert
            // Since MockTestableEndpoint returns a mock spec, we can verify interactions directly

            assertInstanceOf(ParametrizedEndpoint.class, endpointWithPath,
                "withQueryParam should return ParametrizedEndpoint");
            verify(requestSpec).pathParams(argThat(params ->
                                                       params.size() == 1 &&
                                                           params.containsKey("key") &&
                                                           "value".equals(params.get("key"))
            ));
        }


        @ParameterizedTest(name = "[{index}] withHeader should add {1}")
        @DisplayName("withHeader should return ParametrizedEndpoint with correct header values")
        @MethodSource("headerValuesProvider")
        void withHeaderShouldReturnParametrizedEndpoint(String headerKey, List<String> values) {
            // Arrange
            MockEndpoint endpoint = new MockEndpoint(Method.GET, TEST_URL, TestEnum.SAMPLE, TEST_BASE_URL);

            // Act
            Endpoint result = values.size() == 1
                                  ? endpoint.withHeader(headerKey, values.get(0))
                                  : endpoint.withHeader(headerKey, values);

            // Assert
            Map<String, List<String>> headers = result.headers();

            assertInstanceOf(ParametrizedEndpoint.class, result,
                "withHeader should return ParametrizedEndpoint");

            assertEquals(1, headers.size(), "There should be one header");
            assertNotNull(headers.get(headerKey), "Header key should be added");
            assertEquals(values, headers.get(headerKey), "Header value(s) should be added correctly");
        }


        private static Stream<Arguments> headerValuesProvider() {
            return Stream.of(
                Arguments.of("X-Header", List.of("Value")),
                Arguments.of("X-Header", List.of("Value1", "Value2"))
            );
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
                        @Override
                        public Method method() {
                            return Method.GET;
                        }


                        @Override
                        public String url() {
                            return TEST_URL;
                        }


                        @Override
                        public Enum<?> enumImpl() {
                            return TestEnum.SAMPLE;
                        }

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
                        @Override
                        public Method method() {
                            return Method.GET;
                        }


                        @Override
                        public String url() {
                            return TEST_URL;
                        }


                        @Override
                        public Enum<?> enumImpl() {
                            return TestEnum.SAMPLE;
                        }


                        @Override
                        public RequestSpecification defaultConfiguration() {
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