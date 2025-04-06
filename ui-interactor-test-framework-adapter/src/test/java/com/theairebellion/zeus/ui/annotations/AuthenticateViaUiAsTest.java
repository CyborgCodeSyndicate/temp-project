package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticateViaUiAsTest {

    @Test
    void shouldHaveCorrectRetentionPolicy() {
        Retention retention = AuthenticateViaUiAs.class.getAnnotation(Retention.class);
        assertNotNull(retention, "AuthenticateViaUiAs should have Retention annotation");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(),
                "AuthenticateViaUiAs should have RUNTIME retention policy");
    }

    @Test
    void shouldHaveCorrectTarget() {
        Target target = AuthenticateViaUiAs.class.getAnnotation(Target.class);
        assertNotNull(target, "AuthenticateViaUiAs should have Target annotation");
        assertArrayEquals(new ElementType[]{ElementType.METHOD}, target.value(),
                "AuthenticateViaUiAs should target METHOD elements only");
    }

    @Test
    void shouldDeclareRequiredElements() {
        try {
            Method credentialsMethod = AuthenticateViaUiAs.class.getDeclaredMethod("credentials");
            assertNotNull(credentialsMethod, "credentials element should be declared");
            assertEquals(Class.class, credentialsMethod.getReturnType(),
                    "credentials should return Class type");
            // Annotations don't have default values unless explicitly set
            // No need to check default value as it's required
        } catch (NoSuchMethodException e) {
            fail("credentials element should be defined in AuthenticateViaUiAs annotation", e);
        }

        try {
            Method typeMethod = AuthenticateViaUiAs.class.getDeclaredMethod("type");
            assertNotNull(typeMethod, "type element should be declared");
            assertEquals(Class.class, typeMethod.getReturnType(),
                    "type should return Class type");
            // Annotations don't have default values unless explicitly set
            // No need to check default value as it's required
        } catch (NoSuchMethodException e) {
            fail("type element should be defined in AuthenticateViaUiAs annotation", e);
        }
    }

    @Test
    void shouldHaveDefaultValueForCacheCredentials() {
        try {
            Method cacheCredentialsMethod = AuthenticateViaUiAs.class.getDeclaredMethod("cacheCredentials");
            assertNotNull(cacheCredentialsMethod, "cacheCredentials element should be declared");
            assertEquals(boolean.class, cacheCredentialsMethod.getReturnType(),
                    "cacheCredentials should return boolean type");
            assertEquals(false, cacheCredentialsMethod.getDefaultValue(),
                    "cacheCredentials should have default value of false");
        } catch (NoSuchMethodException e) {
            fail("cacheCredentials element should be defined in AuthenticateViaUiAs annotation", e);
        }
    }

    // Mock class for testing the annotation usage
    private static class MockCredentials implements LoginCredentials {
        @Override
        public String username() {
            return "mockUser";
        }

        @Override
        public String password() {
            return "mockPassword";
        }
    }

    private static class MockLoginClient extends BaseLoginClient {
        @Override
        protected <T extends UIServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
            // Mock implementation
        }

        @Override
        protected By successfulLoginElementLocator() {
            return By.id("successElement");
        }
    }

    @Test
    void shouldBeUsableOnMethod() {
        try {
            // Get the annotated method
            Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod");

            // Check if our annotation is present
            AuthenticateViaUiAs annotation = method.getAnnotation(AuthenticateViaUiAs.class);
            assertNotNull(annotation, "Method should be annotated with AuthenticateViaUiAs");

            // Test annotation attributes
            assertEquals(MockCredentials.class, annotation.credentials(),
                    "credentials attribute should match");
            assertEquals(MockLoginClient.class, annotation.type(),
                    "type attribute should match");
            assertFalse(annotation.cacheCredentials(),
                    "cacheCredentials should have default value false");
        } catch (NoSuchMethodException e) {
            fail("Failed to find annotated method", e);
        }
    }

    static class AnnotatedClass {
        @AuthenticateViaUiAs(
                credentials = MockCredentials.class,
                type = MockLoginClient.class
        )
        void annotatedMethod() {
            // Method body is irrelevant for the test
        }
    }
}