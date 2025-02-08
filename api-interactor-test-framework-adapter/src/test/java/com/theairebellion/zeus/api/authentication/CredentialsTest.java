package com.theairebellion.zeus.api.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsTest {

    private static final String USERNAME = "demoUser";
    private static final String PASSWORD = "demoPass";

    @Test
    void testCredentialsImpl() {
        Credentials creds = new Credentials() {
            @Override public String username() { return USERNAME; }
            @Override public String password() { return PASSWORD; }
        };
        assertAll(
                () -> assertEquals(USERNAME, creds.username()),
                () -> assertEquals(PASSWORD, creds.password())
        );
    }
}