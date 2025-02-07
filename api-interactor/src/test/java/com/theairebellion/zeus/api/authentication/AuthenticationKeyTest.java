package com.theairebellion.zeus.api.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationKeyTest {

    @Test
    void testAuthenticationKeyEquality() {
        var key1 = new AuthenticationKey("user1", "pass1", BaseAuthenticationClient.class);
        var key2 = new AuthenticationKey("user1", "pass1", BaseAuthenticationClient.class);

        assertAll(
                () -> assertEquals(key1, key2, "Keys should be equal"),
                () -> assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes should be equal")
        );
    }

    @Test
    void testAuthenticationKeyInequality() {
        var key1 = new AuthenticationKey("user1", "pass1", BaseAuthenticationClient.class);
        var key2 = new AuthenticationKey("user2", "pass2", BaseAuthenticationClient.class);

        assertNotEquals(key1, key2, "Keys should not be equal");
    }

    @Test
    void testToString() {
        var key = new AuthenticationKey("user", "pass", BaseAuthenticationClient.class);

        var expected = """
            AuthenticationKey(username=user, password=pass, type=class com.theairebellion.zeus.api.authentication.BaseAuthenticationClient)
            """;

        assertEquals(expected, key.toString(), "toString output should match");
    }
}