package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.core.mock.TestEnum;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import com.theairebellion.zeus.api.config.ApiConfig;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EndpointTest {

    private Endpoint validEndpoint;
    private Endpoint invalidEndpointNoUrl;
    private Endpoint invalidEndpointNoMethod;

    @BeforeAll
    static void setupSystemProps() {
        // Provide a real or fake URL for test
        System.setProperty("api.base.url", "https://mytesturl.com");
    }

    @BeforeEach
    void setUp() {
        // A simple valid Endpoint
        validEndpoint = new Endpoint() {
            @Override
            public Method method() {
                return Method.GET;
            }
            @Override
            public String url() {
                return "/test/path";
            }
            @Override
            public Enum<?> enumImpl() {
                // You can return an enum if you have one. For example:
                return TestEnum.SAMPLE;
            }
        };

        // No URL
        invalidEndpointNoUrl = new Endpoint() {
            @Override
            public Method method() {
                return Method.GET;
            }
            @Override
            public String url() {
                return null; // invalid
            }
            @Override
            public Enum<?> enumImpl() {
                return TestEnum.NO_URL;
            }
        };

        // No Method
        invalidEndpointNoMethod = new Endpoint() {
            @Override
            public Method method() {
                return null;
            }
            @Override
            public String url() {
                return "/some/url";
            }
            @Override
            public Enum<?> enumImpl() {
                return TestEnum.NO_METHOD;
            }
        };
    }

    @Test
    void testBaseUrl() {
        // Just calls the default method: apiConfig.baseUrl()
        // If you have set property for "api.base.url", this returns it.
        String base = validEndpoint.baseUrl();
        assertNotNull(base);  // or assertEquals("someConfiguredValue", base)
    }

    @Test
    void testHeaders_DefaultEmpty() {
        // By default, headers() is empty
        assertTrue(validEndpoint.headers().isEmpty());
    }

    @Test
    void testDefaultConfiguration() {
        RequestSpecification spec = validEndpoint.defaultConfiguration();
        // Confirms no exception, covers the switch-case on logging
        assertNotNull(spec);
    }

    @Test
    void testDefaultConfiguration_UnsupportedLoggingLevel() {
        // 1) Mock the ApiConfig
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
        when(mockConfig.restAssuredLoggingLevel()).thenReturn("FAKE");
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        // 2) Create an Endpoint that uses the mocked config
        Endpoint fakeLevelEndpoint = new Endpoint() {
            @Override
            public Method method() { return Method.GET; }
            @Override
            public String url() { return "/fake/log/level"; }
            @Override
            public Enum<?> enumImpl() { return TestEnum.SAMPLE; }

            // Overriding getApiConfig() to return our mock
            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        // 3) Expect the switch-default to throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, fakeLevelEndpoint::defaultConfiguration);
        // => This covers the default branch ("FAKE") in your switch statement
    }

    @Test
    void testDefaultConfiguration_ALLLogging() {
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
        when(mockConfig.restAssuredLoggingLevel()).thenReturn("ALL");
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        Endpoint endpointAllLogging = new Endpoint() {
            @Override
            public Method method() { return Method.GET; }
            @Override
            public String url() { return "/some/path"; }
            @Override
            public Enum<?> enumImpl() { return TestEnum.SAMPLE; }

            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        RequestSpecification spec = endpointAllLogging.defaultConfiguration();
        // We only need to confirm no exception was thrown
        assertNotNull(spec);
    }

    @Test
    void testDefaultConfiguration_NONE() {
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
        when(mockConfig.restAssuredLoggingLevel()).thenReturn("NONE");
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        Endpoint endpointNone = new Endpoint() {
            @Override
            public Method method() { return Method.POST; }
            @Override
            public String url() { return "/none"; }
            @Override
            public Enum<?> enumImpl() { return TestEnum.SAMPLE; }

            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        RequestSpecification spec = endpointNone.defaultConfiguration();
        assertNotNull(spec);
        // covers the "NONE" branch
    }

    @Test
    void testDefaultConfiguration_DisabledLogging() {
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(false);
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        Endpoint endpointNoLogging = new Endpoint() {
            @Override
            public Method method() { return Method.GET; }
            @Override
            public String url() { return "/no/log"; }
            @Override
            public Enum<?> enumImpl() { return TestEnum.SAMPLE; }

            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        RequestSpecification spec = endpointNoLogging.defaultConfiguration();
        // covers the “if (enabled) { switch(...) }” => false path
        assertNotNull(spec);
    }

    @Test
    void testValidateEndpoint_NoUrl_Throws() {
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        Endpoint endpointNoUrl = new Endpoint() {
            @Override
            public Method method() { return Method.GET; }
            @Override
            public String url() { return null; }  // invalid
            @Override
            public Enum<?> enumImpl() { return TestEnum.NO_URL; }

            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        assertThrows(IllegalStateException.class, () ->
                endpointNoUrl.prepareRequestSpec(null)
        );
    }

    @Test
    void testValidateEndpoint_NoMethod_Throws() {
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        Endpoint endpointNoMethod = new Endpoint() {
            @Override
            public Method method() { return null; }  // invalid
            @Override
            public String url() { return "/valid/url"; }
            @Override
            public Enum<?> enumImpl() { return TestEnum.NO_METHOD; }

            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        assertThrows(IllegalStateException.class, () ->
                endpointNoMethod.prepareRequestSpec("body")
        );
    }

    @Test
    void testCallMethodAndUrlAndEnumImpl_ForCoverage() {
        ApiConfig mockConfig = mock(ApiConfig.class);
        when(mockConfig.baseUrl()).thenReturn("https://mock-base-url.com");

        Endpoint endpoint = new Endpoint() {
            @Override
            public Method method() { return Method.DELETE; }
            @Override
            public String url() { return "/some/url"; }
            @Override
            public Enum<?> enumImpl() { return TestEnum.SAMPLE; }

            @Override
            public ApiConfig getApiConfig() {
                return mockConfig;
            }
        };

        // Just calling them ensures coverage of method(), url(), enumImpl()
        assertEquals(Method.DELETE, endpoint.method());
        assertEquals("/some/url", endpoint.url());
        assertEquals(TestEnum.SAMPLE, endpoint.enumImpl());
    }

    @Test
    void testPrepareRequestSpec_Valid() {
        // Should also call validateEndpoint() internally
        RequestSpecification spec = validEndpoint.prepareRequestSpec("{ \"body\": \"test\" }");
        assertNotNull(spec);
        // base path = /test/path
    }

    @Test
    void testPrepareRequestSpec_Invalid_NoUrl() {
        assertThrows(IllegalStateException.class, () ->
                invalidEndpointNoUrl.prepareRequestSpec(null)
        );
    }

    @Test
    void testPrepareRequestSpec_Invalid_NoMethod() {
        assertThrows(IllegalStateException.class, () ->
                invalidEndpointNoMethod.prepareRequestSpec("body")
        );
    }

    @Test
    void testWithQueryParam() {
        Endpoint newEp = validEndpoint.withQueryParam("q", "value");
        // it should return a ParametrizedEndpoint
        assertNotNull(newEp);
        assertTrue(newEp instanceof ParametrizedEndpoint);
    }

    @Test
    void testWithPathParam() {
        Endpoint newEp = validEndpoint.withPathParam("id", 123);
        assertNotNull(newEp);
        assertTrue(newEp instanceof ParametrizedEndpoint);
    }

    @Test
    void testWithHeader_Single() {
        Endpoint newEp = validEndpoint.withHeader("X-Header", "Val");
        assertNotNull(newEp);
        assertTrue(newEp instanceof ParametrizedEndpoint);
    }

    @Test
    void testWithHeader_List() {
        Endpoint newEp = validEndpoint.withHeader("X-Header", java.util.Arrays.asList("Val1", "Val2"));
        assertNotNull(newEp);
        assertTrue(newEp instanceof ParametrizedEndpoint);
    }


}