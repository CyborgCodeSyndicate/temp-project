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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("BaseAuthenticationClient Tests")
class BaseAuthenticationClientTest {

   private static final String AUTH_HEADER_KEY = "Authorization";
   private static final String BEARER_TOKEN = "Bearer token";
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
         assertEquals(TestAuthenticationClient.class, key.getType(), "Type should match");

         // Verify
         verify(spyClient).authenticateImpl(mockRestService, USERNAME, PASSWORD);
      }


      @Test
      @DisplayName("authenticate should throw NullPointerException for null service")
      void shouldThrowExceptionForNullServiceOrUsername() {
         // Act & Assert
         assertThrows(
               NullPointerException.class,
               () -> spyClient.authenticate(null, USERNAME, PASSWORD, false),
               "Should throw NullPointerException"
         );

         assertThrows(
               NullPointerException.class,
               () -> spyClient.authenticate(mockRestService, null, PASSWORD, false),
               "Should throw NullPointerException"
         );
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


      @Test
      @DisplayName("authenticate should return new key when cache=false and key present")
      void shouldReturnNewKeyWhenCacheIsFalseAndKeyIsPresent() {
         // Arrange
         AuthenticationKey existingKey = new AuthenticationKey(USERNAME, PASSWORD, spyClient.getClass());
         Header firstHeader = new Header(AUTH_HEADER_KEY, "Bearer dummy-token");
         BaseAuthenticationClient.userAuthenticationHeaderMap.put(
               existingKey, firstHeader);

         // Act
         AuthenticationKey returnedKey = spyClient.authenticate(mockRestService, USERNAME, PASSWORD, false);
         Header secondHeader = spyClient.getAuthentication(existingKey);

         // Assert
         assertEquals(existingKey, returnedKey, "Keys should be the same");
         assertEquals(firstHeader, secondHeader, "Headers should be the be the same");
         assertNotSame(existingKey, returnedKey, "Should return new instance of key");
         assertNotSame(firstHeader, secondHeader, "Should return new instance of header");
         verify(spyClient, times(1)).authenticateImpl(any(), any(), any());

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


      @Test
      @DisplayName("getAuthentication should throw exception when null is send for authentication key")
      void shouldThrowExceptionWhenNullAsKeyForGetAuthentication() {
         assertThrows(
               IllegalArgumentException.class,
               () -> testClient.getAuthentication(null),
               "Should throw IllegalArgumentException when assertion key is null"
         );
      }

   }

}