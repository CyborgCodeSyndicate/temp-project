package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticateViaApiAsAnnotationTest {

    @Test
    @AuthenticateViaApiAs(credentials = TestCreds.class, type = TestAuthClient.class)
    public void testAuthenticateViaApiAsAnnotationUsage() {
        assertDoesNotThrow(() -> {
            var method = AuthenticateViaApiAsAnnotationTest.class.getDeclaredMethod("testAuthenticateViaApiAsAnnotationUsage");
            var annotation = method.getAnnotation(AuthenticateViaApiAs.class);
            assertAll(
                    () -> assertNotNull(annotation, "Annotation should be present"),
                    () -> assertEquals(TestCreds.class, annotation.credentials(), "Credentials class should match"),
                    () -> assertEquals(TestAuthClient.class, annotation.type(), "Type class should match")
            );
        });
    }
}