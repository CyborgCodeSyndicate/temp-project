package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseAuthenticationClientTest {

    @Mock
    private RestService restService;

    @Mock
    private Header header;

    private BaseAuthenticationClient authenticationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationClient = new TestAuthenticationClient();
    }

    @Test
    void testAuthenticate_AddsToCache() {
        RestService restServiceMock = mock(RestService.class);
        BaseAuthenticationClient authClient = spy(BaseAuthenticationClient.class);

        doReturn(new Header("Authorization", "Bearer token"))
                .when(authClient)
                .authenticateImpl(restServiceMock, "username", "password");

        AuthenticationKey key = authClient.authenticate(restServiceMock, "username", "password");

        assertNotNull(key); // Ensure a non-null key is returned
        assertTrue(BaseAuthenticationClient.userAuthenticationHeaderMap.containsKey(key)); // Ensure it's cached
        assertEquals("username", key.getUsername());
        assertEquals("password", key.getPassword());
    }

    @Test
    void testGetAuthentication_RetrievesFromCache() {
        String username = "user";
        String password = "pass";
        AuthenticationKey key = new AuthenticationKey(username, password, TestAuthenticationClient.class);

        // Add to cache manually
        BaseAuthenticationClient.userAuthenticationHeaderMap.put(key, header);

        // Retrieve from cache
        Header retrievedHeader = authenticationClient.getAuthentication(key);

        assertNotNull(retrievedHeader);
        assertEquals(header, retrievedHeader);
    }

    @Test
    void testGetAuthentication_ReturnsNullIfNotInCache() {
        String username = "user";
        String password = "pass";
        AuthenticationKey key = new AuthenticationKey(username, password, TestAuthenticationClient.class);

        // Retrieve from cache (should return null)
        Header retrievedHeader = authenticationClient.getAuthentication(key);

        assertNull(retrievedHeader);
    }

    @Test
    void testAuthenticate_ThrowsExceptionForNullService() {
        BaseAuthenticationClient authClient = spy(BaseAuthenticationClient.class);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> authClient.authenticate(null, "username", "password")
        );

        assertEquals("RestService must not be null", exception.getMessage());
    }

    // Concrete class for testing purposes
    private static class TestAuthenticationClient extends BaseAuthenticationClient {
        @Override
        protected Header authenticateImpl(RestService restService, String username, String password) {
            return new Header("Authorization", "Bearer dummy-token");
        }
    }
}