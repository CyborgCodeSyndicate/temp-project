package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.authentication.mock.TestAuthenticationClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("AuthenticationKey Tests")
class AuthenticationKeyTest {

    private static final String USER1 = "user1";
    private static final String PASS1 = "pass1";
    private static final String USER2 = "user2";
    private static final String PASS2 = "pass2";
    private static final Class<? extends BaseAuthenticationClient> CLIENT_CLASS = BaseAuthenticationClient.class;
    private static final Class<? extends BaseAuthenticationClient> DIFFERENT_CLIENT_CLASS = TestAuthenticationClient.class;
    private static final String EXPECTED_TO_STRING =
            "AuthenticationKey(username=user, password=pass, type=class com.theairebellion.zeus.api.authentication.BaseAuthenticationClient)";

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Keys with same values should be equal")
        void sameValueKeysShouldBeEqual() {
            // Arrange
            var key1 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);
            var key2 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);

            // Assert
            assertEquals(key1, key2, "Keys with identical values should be equal");
            assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes should match for equal keys");
        }

        @Test
        @DisplayName("Keys with different values should not be equal")
        void differentValueKeysShouldNotBeEqual() {
            // Arrange
            var key1 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);
            var key2 = new AuthenticationKey(USER2, PASS2, CLIENT_CLASS);

            // Assert
            assertNotEquals(key1, key2, "Keys with different values should not be equal");
        }
    }

    @Test
    @DisplayName("toString() should return expected format")
    void toStringReturnsExpectedFormat() {
        // Arrange
        var key = new AuthenticationKey("user", "pass", CLIENT_CLASS);

        // Assert
        assertEquals(EXPECTED_TO_STRING, key.toString(), "toString output should match expected format");
    }

    @Nested
    @DisplayName("Null Value Tests")
    class NullValueTests {

        @Test
        @DisplayName("Key with null username should handle equality correctly")
        void nullUsernameHandlesEqualityCorrectly() {
            // Arrange
            var key1 = new AuthenticationKey(null, PASS1, CLIENT_CLASS);
            var key2 = new AuthenticationKey(null, PASS1, CLIENT_CLASS);
            var key3 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);

            // Assert
            assertEquals(key1, key2, "Keys with null usernames should be equal if other fields match");
            assertNotEquals(key1, key3, "Key with null username should not equal key with non-null username");
        }

        @Test
        @DisplayName("Key with null password should handle equality correctly")
        void nullPasswordHandlesEqualityCorrectly() {
            // Arrange
            var key1 = new AuthenticationKey(USER1, null, CLIENT_CLASS);
            var key2 = new AuthenticationKey(USER1, null, CLIENT_CLASS);
            var key3 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);

            // Assert
            assertEquals(key1, key2, "Keys with null passwords should be equal if other fields match");
            assertNotEquals(key1, key3, "Key with null password should not equal key with non-null password");
        }

        @Test
        @DisplayName("Key with null type should handle equality correctly")
        void nullTypeHandlesEqualityCorrectly() {
            // Arrange
            var key1 = new AuthenticationKey(USER1, PASS1, null);
            var key2 = new AuthenticationKey(USER1, PASS1, null);
            var key3 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);

            // Assert
            assertEquals(key1, key2, "Keys with null types should be equal if other fields match");
            assertNotEquals(key1, key3, "Key with null type should not equal key with non-null type");
        }
    }

    @Test
    @DisplayName("Different client types should result in different keys")
    void differentClientTypesShouldResultInDifferentKeys() {
        // Arrange
        var key1 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);
        var key2 = new AuthenticationKey(USER1, PASS1, DIFFERENT_CLIENT_CLASS);

        // Assert
        assertNotEquals(key1, key2, "Keys with different client types should not be equal");
    }
}