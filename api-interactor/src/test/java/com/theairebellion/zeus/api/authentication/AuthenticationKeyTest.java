package com.theairebellion.zeus.api.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationKeyTest {

    @Test
    void testAuthenticationKeyEquality() {
        AuthenticationKey key1 = new AuthenticationKey("user1", "pass1", BaseAuthenticationClient.class);
        AuthenticationKey key2 = new AuthenticationKey("user1", "pass1", BaseAuthenticationClient.class);

        // Test equality
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    void testAuthenticationKeyInequality() {
        AuthenticationKey key1 = new AuthenticationKey("user1", "pass1", BaseAuthenticationClient.class);
        AuthenticationKey key2 = new AuthenticationKey("user2", "pass2", BaseAuthenticationClient.class);

        // Test inequality
        assertNotEquals(key1, key2);
    }

    @Test
    void testToString() {
        AuthenticationKey key = new AuthenticationKey("user", "pass", BaseAuthenticationClient.class);

        // Verify the toString output
        String expected = "AuthenticationKey(username=user, password=pass, type=class com.theairebellion.zeus.api.authentication.BaseAuthenticationClient)";
        assertEquals(expected, key.toString());
    }
}