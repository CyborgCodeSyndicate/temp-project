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
        // Given: A RequestSpecification that is NOT FilterableRequestSpecification
        RequestSpecification plainSpec = mock(RequestSpecification.class);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                restClientImpl.execute(plainSpec, Method.GET)
        );
    }

    @Test
    void testExecute_SupportedMethod_GET() {
        // Given
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.getBody()).thenReturn("{\"key\":\"value\"}");
        when(filterableRequestSpec.getHeaders()).thenReturn(null); // no headers
        when(filterableRequestSpec.get()).thenReturn(responseMock);
        when(responseMock.getStatusCode()).thenReturn(200);
        when(responseMock.body()).thenReturn(null); // no body
        // call is GET so we use the "get()" from method map

        // When
        Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.GET);

        // Then
        assertNotNull(actualResponse);
        verify(filterableRequestSpec, times(1)).get();
    }

    @Test
    void testExecute_SupportedMethod_POST() {
        // Given
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.getBody()).thenReturn("{\"hello\":\"world\"}");
        when(filterableRequestSpec.getHeaders()).thenReturn(null);
        when(filterableRequestSpec.post()).thenReturn(responseMock);
        when(responseMock.getStatusCode()).thenReturn(201);
        when(responseMock.body()).thenReturn(null);

        // When
        Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.POST);

        // Then
        assertEquals(responseMock, actualResponse);
        verify(filterableRequestSpec, times(1)).post();
    }

    @Test
    void testExecute_SupportedMethod_PUT() {
        // Similar approach, verifying coverage of PUT executor
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.getBody()).thenReturn("{\"update\":\"value\"}");
        when(filterableRequestSpec.getHeaders()).thenReturn(null);
        when(filterableRequestSpec.put()).thenReturn(responseMock);

        Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.PUT);
        assertEquals(responseMock, actualResponse);
        verify(filterableRequestSpec, times(1)).put();
    }

    @Test
    void testExecute_SupportedMethod_DELETE() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.getBody()).thenReturn(null);
        when(filterableRequestSpec.delete()).thenReturn(responseMock);

        Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.DELETE);
        assertEquals(responseMock, actualResponse);
        verify(filterableRequestSpec, times(1)).delete();
    }

    @Test
    void testExecute_SupportedMethod_PATCH() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/test");
        when(filterableRequestSpec.getBody()).thenReturn("{}");
        when(filterableRequestSpec.getHeaders()).thenReturn(null);
        when(filterableRequestSpec.patch()).thenReturn(responseMock);

        Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.PATCH);
        assertEquals(responseMock, actualResponse);
        verify(filterableRequestSpec, times(1)).patch();
    }

    @Test
    void testExecute_SupportedMethod_HEAD() {
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/head");
        when(filterableRequestSpec.getBody()).thenReturn(null);
        when(filterableRequestSpec.getHeaders()).thenReturn(null);
        when(filterableRequestSpec.head()).thenReturn(responseMock);

        Response actualResponse = restClientImpl.execute(filterableRequestSpec, Method.HEAD);
        assertEquals(responseMock, actualResponse);
        verify(filterableRequestSpec, times(1)).head();
    }

    @Test
    void testExecute_UnsupportedMethod_ThrowsException() {
        // e.g. Method.OPTIONS is NOT in the METHOD_EXECUTORS map
        when(filterableRequestSpec.getURI()).thenReturn("https://example.com/api/v1/options");
        assertThrows(IllegalArgumentException.class, () ->
                restClientImpl.execute(filterableRequestSpec, Method.OPTIONS)
        );
    }

    @Test
    void testTryPrettyPrintJson_NullOrEmpty() {
        assertNull(restClientImpl.tryPrettyPrintJson(null));
        assertEquals("", restClientImpl.tryPrettyPrintJson(""));
        assertEquals("   ", restClientImpl.tryPrettyPrintJson("   "));
    }

    @Test
    void testTryPrettyPrintJson_ValidJson() {
        String input = "{\"key\":\"value\"}";
        String pretty = restClientImpl.tryPrettyPrintJson(input);
        // The pretty printing would typically add new lines/spaces
        // We only assert that it isn't the same single-line
        assertTrue(pretty.contains("\n") || pretty.contains("\r"),
                "Expected new line(s) in the pretty-printed JSON");
    }

    @Test
    void testTryPrettyPrintJson_InvalidJson() {
        String invalidJson = "This is not JSON";
        String result = restClientImpl.tryPrettyPrintJson(invalidJson);
        // It should just return the original if it cannot parse
        assertEquals(invalidJson, result);
    }

    // We can do a direct coverage test for printRequest / printResponse by calling them directly
    // Usually you'd do these in "execute()" tests, but you can also do them in separate tests:
    @Test
    void testPrintRequest() {
        restClientImpl.printRequest("GET", "https://example.com", "{\"hello\":\"world\"}", "Header1: val1");
        // If needed, you can add verifies for logging calls if you use a logging framework you can mock or intercept.
        // For coverage, just calling it is typically enough to ensure those lines are visited.
    }

    @Test
    void testPrintResponse() {
        when(responseMock.getStatusCode()).thenReturn(200);
        when(responseMock.getHeaders()).thenReturn(null);
        when(responseMock.body()).thenReturn(null);

        restClientImpl.printResponse("GET", "https://example.com", responseMock, 123);
        // Similar logic as above
    }
}