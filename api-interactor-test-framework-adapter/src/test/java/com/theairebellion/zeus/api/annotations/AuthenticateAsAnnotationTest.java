package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticateAsAnnotationTest {

    @Test
    @AuthenticateAs(credentials = TestCreds.class, type = TestAuthClient.class)
    public void testAuthenticateAsAnnotationUsage() {
        try {
            var method = this.getClass()
                    .getMethod("testAuthenticateAsAnnotationUsage");  // must be public, no params
            var annotation = method.getAnnotation(AuthenticateAs.class);
            assertNotNull(annotation);
            assertEquals(TestCreds.class, annotation.credentials());
            assertEquals(TestAuthClient.class, annotation.type());
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        }
    }
}