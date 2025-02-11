package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.api.authentication.mock.TestAuthenticationClient;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BaseAuthenticationClientTest {

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String BEARER_TOKEN = "Bearer token";
    private static final String NULL_SERVICE_MESSAGE = "RestService must not be null";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String PASS = "pass";

    @Mock
    private Header header;

    private BaseAuthenticationClient client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new TestAuthenticationClient();
        BaseAuthenticationClient.userAuthenticationHeaderMap.clear();
    }

    @Test
    void testAuthenticate_AddsToCache() {
        RestService service = mock(RestService.class);
        BaseAuthenticationClient spyClient = spy(BaseAuthenticationClient.class);
        doReturn(new Header(AUTH_HEADER_KEY, BEARER_TOKEN))
                .when(spyClient)
                .authenticateImpl(service, USERNAME, PASSWORD);

        AuthenticationKey key = spyClient.authenticate(service, USERNAME, PASSWORD, false);

        assertNotNull(key);
        assertTrue(BaseAuthenticationClient.userAuthenticationHeaderMap.containsKey(key));
        assertEquals(USERNAME, key.getUsername());
        assertEquals(PASSWORD, key.getPassword());
    }

    @Test
    void testAuthenticate_ThrowsExceptionForNullService() {
        BaseAuthenticationClient spyClient = spy(BaseAuthenticationClient.class);
        doThrow(new NullPointerException(NULL_SERVICE_MESSAGE))
                .when(spyClient)
                .authenticateImpl(eq(null), eq(USERNAME), eq(PASSWORD));

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> spyClient.authenticate(null, USERNAME, PASSWORD, false)
        );
        assertEquals(NULL_SERVICE_MESSAGE, ex.getMessage());
    }

    @Test
    void testGetAuthentication_RetrievesFromCache() {
        AuthenticationKey key = new AuthenticationKey(USER, PASS, TestAuthenticationClient.class);
        BaseAuthenticationClient.userAuthenticationHeaderMap.put(key, header);

        Header retrieved = client.getAuthentication(key);

        assertNotNull(retrieved);
        assertEquals(header, retrieved);
    }

    @Test
    void testGetAuthentication_ReturnsNullIfNotInCache() {
        AuthenticationKey key = new AuthenticationKey(USER, PASS, TestAuthenticationClient.class);
        Header retrieved = client.getAuthentication(key);
        assertNull(retrieved);
    }
}
