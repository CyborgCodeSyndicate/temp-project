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

    @Mock private RestClient restClient;
    @Mock private RestResponseValidator restResponseValidator;
    @Mock private Response responseMock;
    private RestService restService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restService = new RestService(restClient, restResponseValidator);
    }

    @Test
    void testRequest_NoBody() {
        var endpoint = mock(Endpoint.class);
        when(endpoint.prepareRequestSpec(null)).thenReturn(mock(RequestSpecification.class));
        when(endpoint.method()).thenReturn(Method.GET);
        when(restClient.execute(any(RequestSpecification.class), eq(Method.GET))).thenReturn(responseMock);

        assertEquals(responseMock, restService.request(endpoint));
    }

    @Test
    void testRequest_WithBody() {
        var endpoint = mock(Endpoint.class);
        var spec = mock(RequestSpecification.class);
        when(endpoint.prepareRequestSpec("someBody")).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.POST);
        when(restClient.execute(spec, Method.POST)).thenReturn(responseMock);

        assertEquals(responseMock, restService.request(endpoint, "someBody"));
    }

    @Test
    void testRequest_NullEndpoint_Throws() {
        assertThrows(NullPointerException.class, () -> restService.request(null));
    }

    @Test
    void testValidate_NullResponse_Throws() {
        assertThrows(IllegalArgumentException.class, () -> restService.validate(null));
    }

    @Test
    void testValidate_NoAssertions_Throws() {
        when(responseMock.getStatusCode()).thenReturn(200);
        assertThrows(IllegalArgumentException.class, () -> restService.validate(responseMock, new Assertion<?>[0]));
    }

    @Test
    void testValidate_Valid() {
        @SuppressWarnings("unchecked") List<AssertionResult<String>> results = mock(List.class);
        var mockAssertion = mock(Assertion.class);
        when(restResponseValidator.validateResponse(responseMock, mockAssertion)).thenReturn((List) results);

        assertEquals(results, restService.validate(responseMock, mockAssertion));
    }

    @Test
    void testRequestAndValidate_NoBody() {
        var endpoint = mock(Endpoint.class);
        when(endpoint.prepareRequestSpec(null)).thenReturn(mock(RequestSpecification.class));
        when(endpoint.method()).thenReturn(Method.GET);
        when(restClient.execute(any(RequestSpecification.class), eq(Method.GET))).thenReturn(responseMock);

        @SuppressWarnings("unchecked") List<AssertionResult<String>> results = mock(List.class);
        var mockAssertion = mock(Assertion.class);
        when(restResponseValidator.validateResponse(responseMock, mockAssertion)).thenReturn((List) results);

        assertEquals(results, restService.requestAndValidate(endpoint, mockAssertion));
    }

    @Test
    void testAuthenticate_Success() {
        restService.authenticate("user", "pass", MockAuthClient.class);
    }

    @Test
    void testAuthenticate_NullInputs_Throw() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> restService.authenticate(null, "pass", BaseAuthenticationClient.class)),
                () -> assertThrows(NullPointerException.class, () -> restService.authenticate("user", null, BaseAuthenticationClient.class)),
                () -> assertThrows(NullPointerException.class, () -> restService.authenticate("user", "pass", null))
        );
    }

    @Test
    void testAuthenticate_ReflectionError_ThrowsException() {
        class PrivateConstructorAuthClient extends BaseAuthenticationClient {
            private PrivateConstructorAuthClient() {}
            @Override public AuthenticationKey authenticate(RestService restService, String user, String pass, boolean cache) { return null; }
            @Override protected Header authenticateImpl(RestService restService, String username, String password) { return null; }
        }

        assertThrows(RestServiceException.class, () -> restService.authenticate("user", "pass", PrivateConstructorAuthClient.class));
    }

    @Test
    void testExecuteRequest_AuthenticationHeaderNotNull() throws Exception {
        var authClient = mock(BaseAuthenticationClient.class);
        var keyMock = new AuthenticationKey("tokenABC", null, null);
        var hdr = new Header("Authorization", "Bearer tokenABC");
        when(authClient.getAuthentication(keyMock)).thenReturn(hdr);

        var baseClientField = RestService.class.getDeclaredField("baseAuthenticationClient");
        baseClientField.setAccessible(true);
        baseClientField.set(restService, authClient);

        var authKeyField = RestService.class.getDeclaredField("authenticationKey");
        authKeyField.setAccessible(true);
        authKeyField.set(restService, keyMock);

        var endpoint = mock(Endpoint.class);
        var spec = mock(RequestSpecification.class);
        when(endpoint.prepareRequestSpec(null)).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.GET);
        when(restClient.execute(spec, Method.GET)).thenReturn(responseMock);

        restService.request(endpoint);
        verify(spec).header(hdr);
    }

    @Test
    void testExecuteRequest_AuthenticationHeaderNull() throws Exception {
        var authClient = mock(BaseAuthenticationClient.class);
        when(authClient.getAuthentication(any())).thenReturn(null);

        var baseClientField = RestService.class.getDeclaredField("baseAuthenticationClient");
        baseClientField.setAccessible(true);
        baseClientField.set(restService, authClient);

        var endpoint = mock(Endpoint.class);
        var spec = mock(RequestSpecification.class);
        when(endpoint.prepareRequestSpec(null)).thenReturn(spec);
        when(endpoint.method()).thenReturn(Method.GET);
        when(restClient.execute(spec, Method.GET)).thenReturn(responseMock);

        restService.request(endpoint);
        verify(spec, never()).header(any(Header.class));
    }

    @Test
    void testExecuteRequest_ThrowsRestServiceExceptionOnAnyError() {
        var endpoint = mock(Endpoint.class);
        when(endpoint.prepareRequestSpec(null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RestServiceException.class, () -> restService.request(endpoint));
    }
}