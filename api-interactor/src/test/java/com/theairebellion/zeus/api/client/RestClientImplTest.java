package com.theairebellion.zeus.api.client;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestClientImplTest {

    private RestClientImpl restClientImpl;

    @Mock
    private FilterableRequestSpecification filterableRequestSpec;

    @Mock
    private Response responseMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restClientImpl = new RestClientImpl();
    }

    @Test
    void testExecute_NonFilterableSpec_ThrowsException() {
        var plainSpec = mock(RequestSpecification.class);
        assertThrows(IllegalArgumentException.class, () -> restClientImpl.execute(plainSpec, Method.GET));
    }

    @Test
    void testExecute_SupportedMethod_GET() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.get()).thenReturn(responseMock);
        when(responseMock.getStatusCode()).thenReturn(200);

        var actualResponse = restClientImpl.execute(filterableRequestSpec, Method.GET);

        assertAll(
                () -> assertNotNull(actualResponse),
                () -> verify(filterableRequestSpec, times(1)).get()
        );
    }

    @Test
    void testExecute_SupportedMethod_POST() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.post()).thenReturn(responseMock);

        var actualResponse = restClientImpl.execute(filterableRequestSpec, Method.POST);

        assertAll(
                () -> assertEquals(responseMock, actualResponse),
                () -> verify(filterableRequestSpec, times(1)).post()
        );
    }

    @Test
    void testExecute_SupportedMethod_PUT() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.put()).thenReturn(responseMock);

        var actualResponse = restClientImpl.execute(filterableRequestSpec, Method.PUT);

        assertAll(
                () -> assertEquals(responseMock, actualResponse),
                () -> verify(filterableRequestSpec, times(1)).put()
        );
    }

    @Test
    void testExecute_SupportedMethod_DELETE() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.delete()).thenReturn(responseMock);

        var actualResponse = restClientImpl.execute(filterableRequestSpec, Method.DELETE);

        assertAll(
                () -> assertEquals(responseMock, actualResponse),
                () -> verify(filterableRequestSpec, times(1)).delete()
        );
    }

    @Test
    void testExecute_SupportedMethod_PATCH() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.patch()).thenReturn(responseMock);

        var actualResponse = restClientImpl.execute(filterableRequestSpec, Method.PATCH);

        assertAll(
                () -> assertEquals(responseMock, actualResponse),
                () -> verify(filterableRequestSpec, times(1)).patch()
        );
    }

    @Test
    void testExecute_SupportedMethod_HEAD() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/head");
        when(filterableRequestSpec.head()).thenReturn(responseMock);

        var actualResponse = restClientImpl.execute(filterableRequestSpec, Method.HEAD);

        assertAll(
                () -> assertEquals(responseMock, actualResponse),
                () -> verify(filterableRequestSpec, times(1)).head()
        );
    }

    @Test
    void testExecute_UnsupportedMethod_ThrowsException() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/options");
        assertThrows(IllegalArgumentException.class, () -> restClientImpl.execute(filterableRequestSpec, Method.OPTIONS));
    }

    @Test
    void testTryPrettyPrintJson_NullOrEmpty() {
        assertAll(
                () -> assertNull(restClientImpl.tryPrettyPrintJson(null)),
                () -> assertEquals("", restClientImpl.tryPrettyPrintJson("")),
                () -> assertEquals("   ", restClientImpl.tryPrettyPrintJson("   "))
        );
    }

    @Test
    void testTryPrettyPrintJson_ValidJson() {
        var input = "{\"key\":\"value\"}";
        var pretty = restClientImpl.tryPrettyPrintJson(input);

        assertTrue(pretty.contains("\n") || pretty.contains("\r"), "Expected formatted JSON");
    }

    @Test
    void testTryPrettyPrintJson_InvalidJson() {
        var invalidJson = "This is not JSON";
        assertEquals(invalidJson, restClientImpl.tryPrettyPrintJson(invalidJson));
    }

    @Test
    void testPrintRequest() {
        restClientImpl.printRequest("GET", "https://example.com", "{\"hello\":\"world\"}", "Header1: val1");
    }

    @Test
    void testPrintResponse() {
        when(responseMock.getStatusCode()).thenReturn(200);

        restClientImpl.printResponse("GET", "https://example.com", responseMock, 123);
    }
}