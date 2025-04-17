package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.core.mock.TestEnum;
import com.theairebellion.zeus.api.log.LogApi;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyMap;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParametrizedEndpoint Tests")
class ParametrizedEndpointTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String ID = "id";
    private static final String X_HEADER = "X-Header";
    private static final String X_LIST = "X-List";
    private static final List<String> LIST_VALS = List.of("one", "two");

    @Mock
    private Endpoint baseEndpointMock;

    @Mock
    private RequestSpecification requestSpecMock;

    private ParametrizedEndpoint parametrized;

    @BeforeEach
    void setUp() {
        // Setup mocks based on which tests will access them
        lenient().when(baseEndpointMock.prepareRequestSpec(any())).thenReturn(requestSpecMock);

        parametrized = new ParametrizedEndpoint(baseEndpointMock);
    }

    @Nested
    @DisplayName("Delegate Methods Tests")
    class DelegateMethodTests {

        @Test
        @DisplayName("method() should delegate to original endpoint")
        void methodDelegation() {
            when(baseEndpointMock.method()).thenReturn(Method.GET);

            assertEquals(Method.GET, parametrized.method(),
                    "method() should delegate to original endpoint");
            verify(baseEndpointMock).method();
        }

        @Test
        @DisplayName("url() should delegate to original endpoint")
        void urlDelegation() {
            when(baseEndpointMock.url()).thenReturn("/test");

            assertEquals("/test", parametrized.url(),
                    "url() should delegate to original endpoint");
            verify(baseEndpointMock).url();
        }

        @Test
        @DisplayName("enumImpl() should delegate to original endpoint")
        void enumImplDelegation() {
            doReturn(TestEnum.SAMPLE).when(baseEndpointMock).enumImpl();

            assertEquals(TestEnum.SAMPLE, parametrized.enumImpl(),
                    "enumImpl() should delegate to original endpoint");
            verify(baseEndpointMock).enumImpl();
        }

        @Test
        @DisplayName("baseUrl() should delegate to original endpoint")
        void baseUrlDelegation() {
            when(baseEndpointMock.baseUrl()).thenReturn("https://api.example.com");

            assertEquals("https://api.example.com", parametrized.baseUrl(),
                    "baseUrl() should delegate to original endpoint");
            verify(baseEndpointMock).baseUrl();
        }
    }

    @Nested
    @DisplayName("Parameter Addition Tests")
    class ParameterAdditionTests {

        @Test
        @DisplayName("withQueryParam should create new instance with added parameter")
        void withQueryParamValid() {
            ParametrizedEndpoint result = parametrized.withQueryParam(KEY, VALUE);

            assertNotEquals(parametrized, result,
                    "Should return a new instance");
            assertNotNull(result, "Result should not be null");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("withQueryParam should throw exception for invalid key")
        void withQueryParamInvalidKey(String invalidKey) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withQueryParam(invalidKey, VALUE),
                    "Should throw exception for invalid key");

            assertTrue(ex.getMessage().contains("key must not be null or empty"),
                    "Exception message should mention invalid key");
        }

        @Test
        @DisplayName("withQueryParam should throw exception for null value")
        void withQueryParamNullValue() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withQueryParam(KEY, null),
                    "Should throw exception for null value");

            assertTrue(ex.getMessage().contains("value must not be null"),
                    "Exception message should mention null value");
        }

        @Test
        @DisplayName("withPathParam should create new instance with added parameter")
        void withPathParamValid() {
            ParametrizedEndpoint result = parametrized.withPathParam(ID, 123);

            assertNotNull(result, "Result should not be null");
            assertNotEquals(parametrized, result, "Should return a new instance");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("withPathParam should throw exception for invalid key")
        void withPathParamInvalidKey(String invalidKey) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withPathParam(invalidKey, 123),
                    "Should throw exception for invalid key");

            assertTrue(ex.getMessage().contains("key must not be null or empty"),
                    "Exception message should mention invalid key");
        }

        @Test
        @DisplayName("withPathParam should throw exception for null value")
        void withPathParamNullValue() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withPathParam(ID, null),
                    "Should throw exception for null value");

            assertTrue(ex.getMessage().contains("value must not be null"),
                    "Exception message should mention null value");
        }
    }

    @Nested
    @DisplayName("Header Addition Tests")
    class HeaderAdditionTests {

        @Test
        @DisplayName("withHeader with single value should create new instance with added header")
        void withHeaderSingleValid() {
            ParametrizedEndpoint result = parametrized.withHeader(X_HEADER, "Value");

            assertNotNull(result, "Result should not be null");
            assertNotEquals(parametrized, result, "Should return a new instance");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("withHeader should throw exception for invalid key")
        void withHeaderSingleInvalidKey(String invalidKey) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withHeader(invalidKey, "Value"),
                    "Should throw exception for invalid key");

            assertTrue(ex.getMessage().contains("key must not be null or empty"),
                    "Exception message should mention invalid key");
        }

        @Test
        @DisplayName("withHeader should throw exception for null string value")
        void withHeaderSingleNullValue() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withHeader(X_HEADER, (String) null),
                    "Should throw exception for null value");

            assertTrue(ex.getMessage().contains("value must not be null"),
                    "Exception message should mention null value");
        }

        @Test
        @DisplayName("withHeader with list value should create new instance with added headers")
        void withHeaderListValid() {
            ParametrizedEndpoint result = parametrized.withHeader(X_LIST, LIST_VALS);

            assertNotNull(result, "Result should not be null");
            assertNotEquals(parametrized, result, "Should return a new instance");
        }

        @Test
        @DisplayName("withHeader should throw exception for null list value")
        void withHeaderListNullValue() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withHeader(X_LIST, (List<String>) null),
                    "Should throw exception for null list");

            assertTrue(ex.getMessage().contains("value must not be null"),
                    "Exception message should mention null value");
        }

        @Test
        @DisplayName("withHeader should throw exception for empty list value")
        void withHeaderListEmptyValue() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> parametrized.withHeader(X_LIST, Collections.emptyList()),
                    "Should throw exception for empty list");

            assertTrue(ex.getMessage().contains("value list must not be empty"),
                    "Exception message should mention empty list");
        }
    }

    @Nested
    @DisplayName("Headers Merging Tests")
    class HeadersMergingTests {

        @Test
        @DisplayName("headers() should merge headers from original endpoint and added headers")
        void headersMerging() {
            // Arrange
            Map<String, List<String>> originalHeaders = Map.of("Original", List.of("Val"));
            when(baseEndpointMock.headers()).thenReturn(originalHeaders);

            // Act
            var newEp = parametrized.withHeader("Header1", "Val1")
                    .withHeader("Header1", "Val2")
                    .withHeader("Header2", "Val3");

            Map<String, List<String>> mergedHeaders = newEp.headers();

            // Assert
            assertTrue(mergedHeaders.containsKey("Original"),
                    "Should contain original headers");
            assertTrue(mergedHeaders.containsKey("Header1"),
                    "Should contain added header");
            assertTrue(mergedHeaders.containsKey("Header2"),
                    "Should contain added header");
            assertTrue(mergedHeaders.get("Header1").containsAll(List.of("Val1", "Val2")),
                    "Should merge multiple values for same header");
            assertEquals(List.of("Val3"), mergedHeaders.get("Header2"),
                    "Should add single value header correctly");

            // Verify
            verify(baseEndpointMock).headers();
        }

        @Test
        @DisplayName("headers() should be immutable")
        void headersImmutability() {
            // Arrange
            when(baseEndpointMock.headers()).thenReturn(Collections.emptyMap());

            // Act
            var newEp = parametrized.withHeader("Header1", "Val1");
            Map<String, List<String>> headers = newEp.headers();

            // Assert
            assertThrows(UnsupportedOperationException.class,
                    () -> headers.put("NewHeader", List.of("NewValue")),
                    "Headers map should be immutable");

            // Verify
            verify(baseEndpointMock).headers();
        }
    }

    @Nested
    @DisplayName("Request Specification Tests")
    class RequestSpecificationTests {

        @Test
        @DisplayName("prepareRequestSpec should add path params, query params, and headers")
        void prepareRequestSpecAddsParams() {
            // Arrange
            when(requestSpecMock.pathParams(anyMap())).thenReturn(requestSpecMock);
            when(requestSpecMock.queryParams(anyMap())).thenReturn(requestSpecMock);
            when(requestSpecMock.header(anyString(), anyString())).thenReturn(requestSpecMock);

            // Act
            var newEp = parametrized
                    .withQueryParam("q", "123")
                    .withPathParam("id", 999)
                    .withHeader("X-Hdr", "Hello");

            var result = newEp.prepareRequestSpec("body-payload");

            // Assert
            assertNotNull(result, "Result should not be null");

            // Verify
            verify(baseEndpointMock).prepareRequestSpec("body-payload");
            verify(requestSpecMock).pathParams(argThat(map -> map.containsKey("id")));
            verify(requestSpecMock).queryParams(argThat(map -> map.containsKey("q")));
            verify(requestSpecMock).header(eq("X-Hdr"), anyString());
        }

        @Test
        @DisplayName("prepareRequestSpec should join multiple header values with commas")
        void prepareRequestSpecHeaderJoin() {
            // Arrange: stub the chaining methods
            when(requestSpecMock.pathParams(anyMap())).thenReturn(requestSpecMock);
            when(requestSpecMock.queryParams(anyMap())).thenReturn(requestSpecMock);
            when(requestSpecMock.header(anyString(), anyString()))
                    .thenReturn(requestSpecMock);

            // Act: inject two header values
            ParametrizedEndpoint ep = parametrized.withHeader("H", List.of("A", "B"));
            ep.prepareRequestSpec(null);

            // Assert: exact comma‑join
            verify(requestSpecMock).header("H", "A,B");
        }

        @Test
        @DisplayName("withQueryParam chaining accumulates parameters")
        void withQueryParamChainingAccumulates() {
            // Arrange: stub only the injection point
            when(requestSpecMock.queryParams(anyMap())).thenReturn(requestSpecMock);
            // we don’t care here about path/header

            // Act: chain two query additions
            ParametrizedEndpoint ep2 = parametrized
                    .withQueryParam("a", "1")
                    .withQueryParam("b", "2");
            ep2.prepareRequestSpec(null);

            // Assert: the final map has both
            verify(requestSpecMock).queryParams(argThat(map ->
                    map.size() == 2 &&
                            "1".equals(map.get("a")) &&
                            "2".equals(map.get("b"))
            ));
        }

        @Test
        @DisplayName("withPathParam chaining accumulates parameters")
        void withPathParamChainingAccumulates() {
            // Arrange
            when(requestSpecMock.pathParams(anyMap())).thenReturn(requestSpecMock);

            // Act
            ParametrizedEndpoint ep2 = parametrized
                    .withPathParam("x", 42)
                    .withPathParam("y", 99);
            ep2.prepareRequestSpec(null);

            // Assert
            verify(requestSpecMock).pathParams(argThat(map ->
                    map.size() == 2 &&
                            Integer.valueOf(42).equals(map.get("x")) &&
                            Integer.valueOf(99).equals(map.get("y"))
            ));
        }
    }

    @Nested
    @DisplayName("Logging Tests")
    class LoggingTests {

        @Test
        @DisplayName("prepareRequestSpec should call LogApi.info with path/query/header maps")
        void prepareRequestSpecLogsInfo() {
            // Arrange: stub all mutation points
            when(requestSpecMock.pathParams(anyMap())).thenReturn(requestSpecMock);
            when(requestSpecMock.queryParams(anyMap())).thenReturn(requestSpecMock);
            when(requestSpecMock.header(anyString(), anyString()))
                    .thenReturn(requestSpecMock);

            // Scope static‑mock of LogApi
            try (MockedStatic<LogApi> logApi = mockStatic(LogApi.class)) {
                // Act: mix one of each
                ParametrizedEndpoint ep = parametrized
                        .withQueryParam("q", "v1")
                        .withPathParam("p", 123)
                        .withHeader("H", List.of("X"));
                ep.prepareRequestSpec("body");

                // Assert: info was called exactly once, with the correct template
                logApi.verify(() ->
                        LogApi.info(
                                eq("Prepared RequestSpecification with pathParams: {}, queryParams: {}, headers: {}"),
                                anyMap(),   // pathParams
                                anyMap(),   // queryParams
                                anyMap()    // headers
                        ), times(1)
                );
            }
        }

    }
}