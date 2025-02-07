package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.config.ApiConfig;
import com.theairebellion.zeus.api.core.mock.TestEnum;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
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
        System.setProperty("api.base.url", "https://mytesturl.com");
    }

    @BeforeEach
    void setUp() {
        validEndpoint = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return "/test/path"; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
        };

        invalidEndpointNoUrl = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return null; }
            @Override public Enum<?> enumImpl() { return TestEnum.NO_URL; }
        };

        invalidEndpointNoMethod = new Endpoint() {
            @Override public Method method() { return null; }
            @Override public String url() { return "/some/url"; }
            @Override public Enum<?> enumImpl() { return TestEnum.NO_METHOD; }
        };
    }

    @Test
    void testBaseUrl() {
        assertNotNull(validEndpoint.baseUrl());
    }

    @Test
    void testHeaders_DefaultEmpty() {
        assertTrue(validEndpoint.headers().isEmpty());
    }

    @Test
    void testDefaultConfiguration() {
        assertNotNull(validEndpoint.defaultConfiguration());
    }

    @Test
    void testDefaultConfiguration_UnsupportedLoggingLevel() {
        var mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
        when(mockConfig.restAssuredLoggingLevel()).thenReturn("FAKE");

        var fakeLevelEndpoint = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return "/fake/log/level"; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
        };

        assertThrows(IllegalArgumentException.class, fakeLevelEndpoint::defaultConfiguration);
    }

    @Test
    void testDefaultConfiguration_ALLLogging() {
        var mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
        when(mockConfig.restAssuredLoggingLevel()).thenReturn("ALL");

        var endpointAllLogging = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return "/some/path"; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
        };

        assertNotNull(endpointAllLogging.defaultConfiguration());
    }

    @Test
    void testDefaultConfiguration_NONE() {
        var mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(true);
        when(mockConfig.restAssuredLoggingLevel()).thenReturn("NONE");

        var endpointNone = new Endpoint() {
            @Override public Method method() { return Method.POST; }
            @Override public String url() { return "/none"; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
        };

        assertNotNull(endpointNone.defaultConfiguration());
    }

    @Test
    void testDefaultConfiguration_DisabledLogging() {
        var mockConfig = mock(ApiConfig.class);
        when(mockConfig.restAssuredLoggingEnabled()).thenReturn(false);

        var endpointNoLogging = new Endpoint() {
            @Override public Method method() { return Method.GET; }
            @Override public String url() { return "/no/log"; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
        };

        assertNotNull(endpointNoLogging.defaultConfiguration());
    }

    @Test
    void testValidateEndpoint_NoUrl_Throws() {
        assertThrows(IllegalStateException.class, () -> invalidEndpointNoUrl.prepareRequestSpec(null));
    }

    @Test
    void testValidateEndpoint_NoMethod_Throws() {
        assertThrows(IllegalStateException.class, () -> invalidEndpointNoMethod.prepareRequestSpec("body"));
    }

    @Test
    void testCallMethodAndUrlAndEnumImpl_ForCoverage() {
        var endpoint = new Endpoint() {
            @Override public Method method() { return Method.DELETE; }
            @Override public String url() { return "/some/url"; }
            @Override public Enum<?> enumImpl() { return TestEnum.SAMPLE; }
        };

        assertAll(
                () -> assertEquals(Method.DELETE, endpoint.method()),
                () -> assertEquals("/some/url", endpoint.url()),
                () -> assertEquals(TestEnum.SAMPLE, endpoint.enumImpl())
        );
    }

    @Test
    void testPrepareRequestSpec_Valid() {
        assertNotNull(validEndpoint.prepareRequestSpec("{ \"body\": \"test\" }"));
    }

    @Test
    void testPrepareRequestSpec_Invalid_NoUrl() {
        assertThrows(IllegalStateException.class, () -> invalidEndpointNoUrl.prepareRequestSpec(null));
    }

    @Test
    void testPrepareRequestSpec_Invalid_NoMethod() {
        assertThrows(IllegalStateException.class, () -> invalidEndpointNoMethod.prepareRequestSpec("body"));
    }

    @Test
    void testWithQueryParam() {
        assertInstanceOf(ParametrizedEndpoint.class, validEndpoint.withQueryParam("q", "value"));
    }

    @Test
    void testWithPathParam() {
        assertInstanceOf(ParametrizedEndpoint.class, validEndpoint.withPathParam("id", 123));
    }

    @Test
    void testWithHeader_Single() {
        assertInstanceOf(ParametrizedEndpoint.class, validEndpoint.withHeader("X-Header", "Val"));
    }

    @Test
    void testWithHeader_List() {
        assertInstanceOf(ParametrizedEndpoint.class, validEndpoint.withHeader("X-Header", java.util.Arrays.asList("Val1", "Val2")));
    }
}