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

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String ID = "id";
    private static final String X_HEADER = "X-Header";
    private static final String X_LIST = "X-List";
    private static final List<String> LIST_VALS = List.of("one", "two");

    private Endpoint baseEndpointMock;
    private ParametrizedEndpoint parametrized;

    @BeforeEach
    void setUp() {
        baseEndpointMock = mock(Endpoint.class);
        when(baseEndpointMock.method()).thenReturn(Method.GET);
        when(baseEndpointMock.url()).thenReturn("/test");
        doReturn(TestEnum.SAMPLE).when(baseEndpointMock).enumImpl();
        when(baseEndpointMock.baseUrl()).thenReturn("https://api.example.com");
        when(baseEndpointMock.headers()).thenReturn(Collections.emptyMap());
        when(baseEndpointMock.prepareRequestSpec(any())).thenReturn(mock(RequestSpecification.class));

        parametrized = new ParametrizedEndpoint(baseEndpointMock);
    }

    @Test
    void testWithQueryParam_Valid() {
        assertNotEquals(parametrized, parametrized.withQueryParam(KEY, VALUE));
    }

    @Test
    void testWithQueryParam_InvalidKey() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withQueryParam(null, VALUE)),
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withQueryParam("", VALUE))
        );
    }

    @Test
    void testWithQueryParam_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> parametrized.withQueryParam(KEY, null));
    }

    @Test
    void testWithPathParam_Valid() {
        assertNotNull(parametrized.withPathParam(ID, 123));
    }

    @Test
    void testWithPathParam_Invalid() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withPathParam("", 123)),
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withPathParam(ID, null))
        );
    }

    @Test
    void testWithHeader_Single_Valid() {
        assertNotNull(parametrized.withHeader(X_HEADER, "Value"));
    }

    @Test
    void testWithHeader_Single_Invalid() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withHeader(null, "Value")),
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withHeader("", "Value")),
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withHeader(X_HEADER, (String) null))
        );
    }

    @Test
    void testWithHeader_List_Valid() {
        assertNotNull(parametrized.withHeader(X_LIST, LIST_VALS));
    }

    @Test
    void testWithHeader_List_Invalid() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withHeader(null, List.of("one"))),
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withHeader(X_LIST, (List<String>) null)),
                () -> assertThrows(IllegalArgumentException.class, () -> parametrized.withHeader(X_LIST, Collections.emptyList()))
        );
    }

    @Test
    void testHeaders_Merging() {
        var newEp = parametrized.withHeader("Header1", "Val1")
                .withHeader("Header1", "Val2")
                .withHeader("Header2", "Val3");

        assertAll(
                () -> assertEquals(2, newEp.headers().size()),
                () -> assertTrue(newEp.headers().get("Header1").containsAll(List.of("Val1", "Val2"))),
                () -> assertEquals(List.of("Val3"), newEp.headers().get("Header2"))
        );
    }

    @Test
    void testPrepareRequestSpec_CallsOriginalAndAddsParams() {
        var newEp = parametrized
                .withQueryParam("q", "123")
                .withPathParam("id", 999)
                .withHeader("X-Hdr", "Hello");

        var specMock = newEp.prepareRequestSpec("body-payload");
        assertNotNull(specMock);
        verify(baseEndpointMock, times(1)).prepareRequestSpec("body-payload");
    }
}
