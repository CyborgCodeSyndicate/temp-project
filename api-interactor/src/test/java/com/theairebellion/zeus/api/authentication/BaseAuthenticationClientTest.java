package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.authentication.mock.TestAuthenticationClient;
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
    private Header header;

    private BaseAuthenticationClient authenticationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationClient = new TestAuthenticationClient();
    }

    @Test
    void testAuthenticate_AddsToCache() {
        var restServiceMock = mock(RestService.class);
        var authClient = spy(BaseAuthenticationClient.class);

        doReturn(new Header("Authorization", "Bearer token"))
                .when(authClient)
                .authenticateImpl(restServiceMock, "username", "password");

        var key = authClient.authenticate(restServiceMock, "username", "password", false);

        assertAll(
                () -> assertNotNull(key),
                () -> assertTrue(BaseAuthenticationClient.userAuthenticationHeaderMap.containsKey(key)),
                () -> assertEquals("username", key.getUsername()),
                () -> assertEquals("password", key.getPassword())
        );
    }

    @Test
    void testGetAuthentication_RetrievesFromCache() {
        var key = new AuthenticationKey("user", "pass", TestAuthenticationClient.class);
        BaseAuthenticationClient.userAuthenticationHeaderMap.put(key, header);

        var retrievedHeader = authenticationClient.getAuthentication(key);

        assertAll(
                () -> assertNotNull(retrievedHeader),
                () -> assertEquals(header, retrievedHeader)
        );
    }

    @Test
    void testGetAuthentication_ReturnsNullIfNotInCache() {
        var key = new AuthenticationKey("user", "pass", TestAuthenticationClient.class);

        assertNull(authenticationClient.getAuthentication(key));
    }

    @Test
    void testAuthenticate_ThrowsExceptionForNullService() {
        var authClient = spy(BaseAuthenticationClient.class);

        var exception = assertThrows(NullPointerException.class,
                () -> authClient.authenticate(null, "username", "password", false));

        assertEquals("RestService must not be null", exception.getMessage());
    }
}