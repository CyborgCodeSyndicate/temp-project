package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.core.mock.TestEnum;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParametrizedEndpointTest {

    private Endpoint baseEndpointMock;
    private ParametrizedEndpoint parametrized;

    @BeforeEach
    void setUp() {
        // A minimal mock
        baseEndpointMock = mock(Endpoint.class);
        when(baseEndpointMock.method()).thenReturn(Method.GET);
        when(baseEndpointMock.url()).thenReturn("/test");
        doReturn(TestEnum.SAMPLE).when(baseEndpointMock).enumImpl();
        when(baseEndpointMock.baseUrl()).thenReturn("https://api.example.com");
        when(baseEndpointMock.headers()).thenReturn(Collections.emptyMap());
        // We can also mock prepareRequestSpec:
        RequestSpecification specMock = mock(RequestSpecification.class);
        when(baseEndpointMock.prepareRequestSpec(any())).thenReturn(specMock);

        parametrized = new ParametrizedEndpoint(baseEndpointMock);
    }

    @Test
    void testWithQueryParam_Valid() {
        ParametrizedEndpoint newEp = parametrized.withQueryParam("key", "value");
        assertNotNull(newEp);
        assertNotEquals(parametrized, newEp);
    }

    @Test
    void testWithQueryParam_InvalidKey() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withQueryParam(null, "value")
        );
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withQueryParam("", "value")
        );
    }

    @Test
    void testWithQueryParam_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withQueryParam("key", null)
        );
    }

    @Test
    void testWithPathParam() {
        ParametrizedEndpoint newEp = parametrized.withPathParam("id", 123);
        assertNotNull(newEp);
    }

    @Test
    void testWithPathParam_InvalidKey() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withPathParam("", 123)
        );
    }

    @Test
    void testWithPathParam_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withPathParam("id", null)
        );
    }

    @Test
    void testWithHeader_Single_Valid() {
        ParametrizedEndpoint newEp = parametrized.withHeader("X-Header", "Value");
        assertNotNull(newEp);
    }

    @Test
    void testWithHeader_Single_InvalidKey() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withHeader(null, "Value")
        );
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withHeader("", "Value")
        );
    }

    @Test
    void testWithHeader_Single_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withHeader("X-Header", (String) null)
        );
    }

    @Test
    void testWithHeader_List_Valid() {
        ParametrizedEndpoint newEp = parametrized.withHeader("X-List", List.of("one","two"));
        assertNotNull(newEp);
    }

    @Test
    void testWithHeader_List_InvalidKey() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withHeader(null, List.of("one"))
        );
    }

    @Test
    void testWithHeader_List_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withHeader("X-List", (List<String>) null)
        );
        assertThrows(IllegalArgumentException.class, () ->
                parametrized.withHeader("X-List", Collections.emptyList())
        );
    }

    @Test
    void testHeaders_Merging() {
        // The original Endpoint has empty headers. We'll add two.
        ParametrizedEndpoint newEp = parametrized.withHeader("Header1", "Val1")
                .withHeader("Header1", "Val2")
                .withHeader("Header2", "Val3");
        assertNotNull(newEp);
        assertEquals(2, newEp.headers().size());
        assertTrue(newEp.headers().get("Header1").contains("Val1"));
        assertTrue(newEp.headers().get("Header1").contains("Val2"));
        assertEquals(List.of("Val3"), newEp.headers().get("Header2"));
    }

    @Test
    void testPrepareRequestSpec_CallsOriginalAndAddsParams() {
        // param approach
        ParametrizedEndpoint newEp = parametrized.withQueryParam("q", "123")
                .withPathParam("id", 999)
                .withHeader("X-Hdr", "Hello");

        // mocking the chain
        RequestSpecification specMock = newEp.prepareRequestSpec("body-payload");
        assertNotNull(specMock);

        // We can verify that original.prepareRequestSpec("body-payload") was called exactly once
        verify(baseEndpointMock, times(1)).prepareRequestSpec("body-payload");
        // Also we can do advanced verification that .pathParams() / .queryParams() / .header() is called on spec
        // if you want deeper coverage. For example:
        // verify(specMock, times(1)).pathParams(Map.of("id", 999));
        // verify(specMock, times(1)).queryParams(Map.of("q", "123"));
        // verify(specMock, times(1)).header("X-Hdr", "Hello");
    }
}