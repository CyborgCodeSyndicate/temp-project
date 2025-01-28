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
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
        restService = new RestService(restClient, restResponseValidator);
    }

    @Test
    void testRequest_NoBody() {
        Endpoint endpoint = mock(Endpoint.class);
        when(endpoint.prepareRequestSpec(null)).thenReturn(mock(RequestSpecification.class));
        when(endpoint.method()).thenReturn(Method.GET);
        when(endpoint.url()).thenReturn("/test");
        when(restClient.execute(any(RequestSpecification.class), eq(Method.GET)))
                .thenReturn(responseMock);

        Response actualResponse = restService.request(endpoint);
        assertEquals(responseMock, actualResponse);
    }

    @Test
    void testRequest_WithBody() {
        Endpoint endpoint = mock(Endpoint.class);
        RequestSpecification spec = mock(RequestSpecification.class);

        when(endpoint.prepareRequestSpec("someBody")).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.POST);
        when(endpoint.url()).thenReturn("/submit");
        when(restClient.execute(spec, Method.POST)).thenReturn(responseMock);

        Response actualResponse = restService.request(endpoint, "someBody");
        assertEquals(responseMock, actualResponse);
    }

    @Test
    void testRequest_NullEndpoint_ThrowsNullPointerException() {
        // This hits private executeRequest => Objects.requireNonNull(endpoint,...
        assertThrows(NullPointerException.class, () -> restService.request(null));
    }

    @Test
    void testValidate_NullResponse_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restService.validate(null));
    }

    @Test
    void testValidate_NoAssertions_ThrowsIllegalArgumentException() {
        when(responseMock.getStatusCode()).thenReturn(200);
        // Pass zero-length array
        assertThrows(IllegalArgumentException.class, () ->
                restService.validate(responseMock /*non-null*/, new Assertion<?>[0]));
    }

    @Test
    void testValidate_Valid() {
        @SuppressWarnings("unchecked")
        List<AssertionResult<String>> results = mock(List.class);

        Assertion<?> mockAssertion = mock(Assertion.class);
        when(restResponseValidator.validateResponse(responseMock, mockAssertion))
                .thenReturn((List) results);

        List<AssertionResult<String>> actual = restService.validate(responseMock, mockAssertion);
        assertEquals(results, actual);
    }

    @Test
    void testRequestAndValidate_NoBody() {
        Endpoint endpoint = mock(Endpoint.class);
        when(endpoint.prepareRequestSpec(null)).thenReturn(mock(RequestSpecification.class));
        when(endpoint.method()).thenReturn(Method.GET);
        when(endpoint.url()).thenReturn("/test");
        when(restClient.execute(any(RequestSpecification.class), eq(Method.GET)))
                .thenReturn(responseMock);

        @SuppressWarnings("unchecked")
        List<AssertionResult<String>> results = mock(List.class);
        Assertion<?> mockAssertion = mock(Assertion.class);
        when(restResponseValidator.validateResponse(responseMock, mockAssertion))
                .thenReturn((List) results);

        List<AssertionResult<String>> actual =
                restService.requestAndValidate(endpoint, mockAssertion);

        assertEquals(results, actual);
    }

    @Test
    void testRequestAndValidate_WithBody() {
        Endpoint endpoint = mock(Endpoint.class);
        RequestSpecification spec = mock(RequestSpecification.class);

        when(endpoint.prepareRequestSpec("body")).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.POST);
        when(endpoint.url()).thenReturn("/post");
        when(restClient.execute(spec, Method.POST)).thenReturn(responseMock);

        @SuppressWarnings("unchecked")
        List<AssertionResult<String>> results = mock(List.class);
        Assertion<?> mockAssertion = mock(Assertion.class);
        when(restResponseValidator.validateResponse(responseMock, mockAssertion))
                .thenReturn((List) results);

        List<AssertionResult<String>> actual =
                restService.requestAndValidate(endpoint, "body", mockAssertion);

        assertEquals(results, actual);
    }

    @Test
    void testAuthenticate_Success() {
        // Mock a client that extends BaseAuthenticationClient
        // We'll do a minimal class for this test:

        restService.authenticate("user", "pass", MockAuthClient.class);
        // If no exception => success
    }

    @Test
    void testAuthenticate_NullUsername() {
        assertThrows(NullPointerException.class, () ->
                restService.authenticate(null, "pass",
                        BaseAuthenticationClient.class));
    }

    @Test
    void testAuthenticate_NullPassword() {
        assertThrows(NullPointerException.class, () ->
                restService.authenticate("user", null,
                        BaseAuthenticationClient.class));
    }

    @Test
    void testAuthenticate_NullClass() {
        assertThrows(NullPointerException.class, () ->
                restService.authenticate("user", "pass", null));
    }

    @Test
    void testAuthenticate_ReflectionError_ThrowsRestServiceException() {
        // If the class can't be instantiated, reflection throws -> we wrap in RestServiceException
        class PrivateConstructorAuthClient extends BaseAuthenticationClient {
            private PrivateConstructorAuthClient() {} // private => reflection fails
            @Override
            public AuthenticationKey authenticate(
                    RestService restService, String user, String pass) {
                return null;
            }

            @Override
            protected Header authenticateImpl(RestService restService, String username, String password) {
                return null;
            }
        }
        // Expect a failure
        assertThrows(RestServiceException.class, () ->
                restService.authenticate("user", "pass", PrivateConstructorAuthClient.class));
    }

    @Test
    void testExecuteRequest_AuthenticationHeaderNotNull() {
        // Indirectly test the block that adds a header if baseAuthenticationClient != null
        // 1) We must call authenticate(...) with a real or mock client that returns a non-null header
        // or set it manually via reflection if needed. For coverage, let's do a simpler approach:

        // We'll do a small dynamic mock:
        BaseAuthenticationClient authClient =
                mock(BaseAuthenticationClient.class);
        AuthenticationKey keyMock =
                new AuthenticationKey("tokenABC", null, null);
        Header hdr = new Header("Authorization", "Bearer tokenABC");
        when(authClient.getAuthentication(keyMock)).thenReturn(hdr);

        // put that mock into restService
        // We can do it with reflection or by calling authenticate, but to keep it short:
        // ... reflection approach or if you want to do it "white-box" style is up to you.
        // We'll do reflection:

        try {
            var baseClientField = RestService.class.getDeclaredField("baseAuthenticationClient");
            baseClientField.setAccessible(true);
            baseClientField.set(restService, authClient);

            var authKeyField = RestService.class.getDeclaredField("authenticationKey");
            authKeyField.setAccessible(true);
            authKeyField.set(restService, keyMock);
        } catch (Exception e) {
            fail("Reflection setup failed: " + e.getMessage());
        }

        Endpoint endpoint = mock(Endpoint.class);
        RequestSpecification spec = mock(RequestSpecification.class);

        when(endpoint.prepareRequestSpec(null)).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.GET);
        when(endpoint.url()).thenReturn("/testAuth");
        when(restClient.execute(spec, Method.GET)).thenReturn(responseMock);

        // call request -> triggers private executeRequest
        restService.request(endpoint);

        // verify we set the header
        verify(spec).header(hdr);
    }

    @Test
    void testExecuteRequest_AuthenticationHeaderNull() {
        // If getAuthentication returns null, we do not call spec.header(...)
        BaseAuthenticationClient authClient =
                mock(BaseAuthenticationClient.class);
        when(authClient.getAuthentication(any())).thenReturn(null);

        try {
            var baseClientField = RestService.class.getDeclaredField("baseAuthenticationClient");
            baseClientField.setAccessible(true);
            baseClientField.set(restService, authClient);
        } catch (Exception e) {
            fail(e);
        }

        Endpoint endpoint = mock(Endpoint.class);
        RequestSpecification spec = mock(RequestSpecification.class);

        when(endpoint.prepareRequestSpec(null)).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.GET);
        when(endpoint.url()).thenReturn("/testAuthNull");
        when(restClient.execute(spec, Method.GET)).thenReturn(responseMock);

        restService.request(endpoint);

        // ensure we never call spec.header(...)
        verify(spec, never()).header(any(Header.class));
    }

    @Test
    void testExecuteRequest_ThrowsRestServiceExceptionOnAnyError() {
        Endpoint endpoint = mock(Endpoint.class);
        when(endpoint.prepareRequestSpec(null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RestServiceException.class, () -> restService.request(endpoint));
    }
}