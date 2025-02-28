package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.authentication.mock.TestAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BaseAuthenticationClient Tests")
class BaseAuthenticationClientTest {

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String BEARER_TOKEN = "Bearer token";
    private static final String NULL_SERVICE_MESSAGE = "RestService must not be null";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String PASS = "pass";

    @Mock
    private Header mockHeader;

    @Mock
    private RestService mockRestService;

    private TestAuthenticationClient testClient;

    @Spy
    private BaseAuthenticationClient spyClient = new TestAuthenticationClient();

    @BeforeEach
    void setUp() {
        testClient = new TestAuthenticationClient();
        BaseAuthenticationClient.userAuthenticationHeaderMap.clear();
    }

    @Nested
    @DisplayName("Authenticate Method Tests")
    class AuthenticateTests {

        @Test
        @DisplayName("authenticate should add new entry to cache when cache=false")
        void shouldAddToCacheWhenCacheFalse() {
            // Arrange
            doReturn(new Header(AUTH_HEADER_KEY, BEARER_TOKEN))
                    .when(spyClient)
                    .authenticateImpl(mockRestService, USERNAME, PASSWORD);

            // Act
            AuthenticationKey key = spyClient.authenticate(mockRestService, USERNAME, PASSWORD, false);

            // Assert
            assertNotNull(key, "Authentication key should not be null");
            assertTrue(BaseAuthenticationClient.userAuthenticationHeaderMap.containsKey(key),
                    "Key should be added to cache");
            assertEquals(USERNAME, key.getUsername(), "Username should match");
            assertEquals(PASSWORD, key.getPassword(), "Password should match");

            // Verify
            verify(spyClient).authenticateImpl(mockRestService, USERNAME, PASSWORD);
        }

        @Test
        @DisplayName("authenticate should throw NullPointerException for null service")
        void shouldThrowExceptionForNullService() {
            // Arrange
            doThrow(new NullPointerException(NULL_SERVICE_MESSAGE))
                    .when(spyClient)
                    .authenticateImpl(eq(null), eq(USERNAME), eq(PASSWORD));

            // Act & Assert
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> spyClient.authenticate(null, USERNAME, PASSWORD, false),
                    "Should throw NullPointerException"
            );
            assertEquals(NULL_SERVICE_MESSAGE, ex.getMessage(), "Exception message should match");

            // Verify
            verify(spyClient).authenticateImpl(null, USERNAME, PASSWORD);
        }

        @Test
        @DisplayName("authenticate should add to cache when cache=true and key not present")
        void shouldAddToCacheWhenCacheTrueAndKeyNotPresent() {
            // Arrange
            doReturn(new Header(AUTH_HEADER_KEY, BEARER_TOKEN))
                    .when(spyClient)
                    .authenticateImpl(mockRestService, USERNAME, PASSWORD);

            // Act
            AuthenticationKey key = spyClient.authenticate(mockRestService, USERNAME, PASSWORD, true);

            // Assert
            assertNotNull(key, "Authentication key should not be null");
            assertTrue(BaseAuthenticationClient.userAuthenticationHeaderMap.containsKey(key),
                    "Key should be added to cache");

            // Verify
            verify(spyClient).authenticateImpl(mockRestService, USERNAME, PASSWORD);
        }

        @Test
        @DisplayName("authenticate should return existing key when cache=true and key present")
        void shouldReturnExistingKeyWhenCacheTrueAndKeyPresent() {
            // Arrange
            AuthenticationKey existingKey = new AuthenticationKey(USERNAME, PASSWORD, spyClient.getClass());
            BaseAuthenticationClient.userAuthenticationHeaderMap.put(
                    existingKey, new Header(AUTH_HEADER_KEY, BEARER_TOKEN));

            // Act
            AuthenticationKey returnedKey = spyClient.authenticate(mockRestService, USERNAME, PASSWORD, true);

            // Assert
            assertEquals(existingKey, returnedKey, "Should return the existing key");

            // Verify
            verify(spyClient, never()).authenticateImpl(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("GetAuthentication Method Tests")
    class GetAuthenticationTests {

        @Test
        @DisplayName("getAuthentication should retrieve header from cache")
        void shouldRetrieveHeaderFromCache() {
            // Arrange
            AuthenticationKey key = new AuthenticationKey(USER, PASS, TestAuthenticationClient.class);
            BaseAuthenticationClient.userAuthenticationHeaderMap.put(key, mockHeader);

            // Act
            Header retrievedHeader = testClient.getAuthentication(key);

            // Assert
            assertNotNull(retrievedHeader, "Retrieved header should not be null");
            assertEquals(mockHeader, retrievedHeader, "Retrieved header should match the cached header");
        }

        @Test
        @DisplayName("getAuthentication should return null when key not in cache")
        void shouldReturnNullWhenKeyNotInCache() {
            // Arrange
            AuthenticationKey key = new AuthenticationKey(USER, PASS, TestAuthenticationClient.class);

            // Act
            Header retrievedHeader = testClient.getAuthentication(key);

            // Assert
            assertNull(retrievedHeader, "Should return null when key is not in cache");
        }
    }
}