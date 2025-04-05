package com.theairebellion.zeus.api.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Credentials Interface Tests")
class CredentialsTest {

    @Nested
    @DisplayName("Implementation Tests")
    class ImplementationTests {

        static Stream<Arguments> credentialsProvider() {
            return Stream.of(
                    Arguments.of("demoUser", "demoPass"),
                    Arguments.of("admin", "admin123"),
                    Arguments.of("", ""),  // Testing empty credentials
                    Arguments.of("special@user.com", "P@ssw0rd!")  // Testing special characters
            );
        }

        @ParameterizedTest(name = "Implementation should return correct values: username={0}, password={1}")
        @MethodSource("credentialsProvider")
        void shouldReturnCorrectValues(String username, String password) {
            // Create implementation
            Credentials credentials = new Credentials() {
                @Override public String username() { return username; }
                @Override public String password() { return password; }
            };

            // Verify using AssertJ
            assertThat(credentials.username()).isEqualTo(username);
            assertThat(credentials.password()).isEqualTo(password);
        }
    }

    @Nested
    @DisplayName("Security Tests")
    class SecurityTests {

        @Test
        @DisplayName("Credentials implementation should not expose sensitive data in toString()")
        void shouldNotExposeCredentialsInToString() {
            // Create implementation with sensitive data
            Credentials credentials = new Credentials() {
                @Override public String username() { return "admin"; }
                @Override public String password() { return "secretPassword123"; }

                // Many implementations might override toString() which could expose passwords
                @Override
                public String toString() {
                    return "User: " + username() + ", Pass: [REDACTED]";
                }
            };

            // Verify password is not exposed
            assertThat(credentials.toString()).doesNotContain("secretPassword123");
            assertThat(credentials.toString()).contains("[REDACTED]");
        }
    }
}