package com.theairebellion.zeus.api.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthenticationKeyTest {

    private static final String USER1 = "user1";
    private static final String PASS1 = "pass1";
    private static final String USER2 = "user2";
    private static final String PASS2 = "pass2";

    private static final Class<? extends BaseAuthenticationClient> CLIENT_CLASS =
            BaseAuthenticationClient.class;

    private static final String EXPECTED_TO_STRING =
            "AuthenticationKey(username=user, password=pass, type=class com.theairebellion.zeus.api.authentication.BaseAuthenticationClient)";

    @Test
    void testAuthenticationKeyEquality() {
        var k1 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);
        var k2 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);
        assertEquals(k1, k2);
        assertEquals(k1.hashCode(), k2.hashCode());
    }

    @Test
    void testAuthenticationKeyInequality() {
        var k1 = new AuthenticationKey(USER1, PASS1, CLIENT_CLASS);
        var k2 = new AuthenticationKey(USER2, PASS2, CLIENT_CLASS);
        assertNotEquals(k1, k2);
    }

    @Test
    void testToString() {
        var key = new AuthenticationKey("user", "pass", CLIENT_CLASS);
        assertEquals(EXPECTED_TO_STRING, key.toString());
    }
}
