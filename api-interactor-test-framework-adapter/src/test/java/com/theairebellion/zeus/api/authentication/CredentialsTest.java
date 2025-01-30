package com.theairebellion.zeus.api.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsTest {

    @Test
    void testCredentialsImpl() {
        Credentials creds = new Credentials() {
            @Override public String username() { return "demoUser"; }
            @Override public String password() { return "demoPass"; }
        };
        assertEquals("demoUser", creds.username());
        assertEquals("demoPass", creds.password());
    }
}