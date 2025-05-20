package com.theairebellion.zeus.framework.spring;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class FrameworkAdapterContextCustomizerTest {

    @Mock
    private MergedContextConfiguration mergedConfig;

    /**
     * Custom extension of AnnotationConfigApplicationContext that allows testing the scan method
     */
    @Getter
    static class TestAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {
        private String[] scannedPackages;

        @Override
        public void scan(String... basePackages) {
            this.scannedPackages = basePackages;
        }

    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should throw NullPointerException when basePackages is null")
        void testConstructorThrowsForNullBasePackages() {
            // When/Then
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> new FrameworkAdapterContextCustomizer(null),
                    "Constructor should throw NullPointerException for null basePackages"
            );

            assertEquals("Base packages must not be null", ex.getMessage(),
                    "Exception message should mention basePackages");
        }
    }

    @Nested
    @DisplayName("customizeContext method tests")
    class CustomizeContextTests {
        @Test
        @DisplayName("Should scan basePackages when context is AnnotationConfigApplicationContext")
        void testCustomizeContextWithAnnotationConfigApplicationContext() {
            // Given
            String[] basePackages = new String[] { "com.example.package1", "com.example.package2" };
            FrameworkAdapterContextCustomizer customizer = new FrameworkAdapterContextCustomizer(basePackages);

            TestAnnotationConfigApplicationContext context = new TestAnnotationConfigApplicationContext();

            // When
            customizer.customizeContext(context, mergedConfig);

            // Then
            assertArrayEquals(basePackages, context.getScannedPackages(),
                    "Context should be scanned with the provided base packages");
        }

        @Test
        @DisplayName("Should do nothing when context is not AnnotationConfigApplicationContext")
        void testCustomizeContextWithNonAnnotationConfigApplicationContext() {
            // Given
            String[] basePackages = new String[] { "com.example.package" };
            FrameworkAdapterContextCustomizer customizer = new FrameworkAdapterContextCustomizer(basePackages);

            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

            // When
            customizer.customizeContext(mockContext, mergedConfig);

            // Then
            // No exception should be thrown and no method called on the mock context
            verifyNoInteractions(mockContext);
        }

        @Test
        @DisplayName("Should handle empty basePackages array")
        void handlesEmptyBasePackages() {
            // Given
            FrameworkAdapterContextCustomizer customizer =
                    new FrameworkAdapterContextCustomizer(new String[0]);
            TestAnnotationConfigApplicationContext context = new TestAnnotationConfigApplicationContext();

            // When
            customizer.customizeContext(context, mergedConfig);

            // Then
            assertArrayEquals(new String[0], context.getScannedPackages());
        }
    }

    @Nested
    @DisplayName("equals, hashCode, and toString tests")
    class ObjectMethodsTests {
        @Test
        @DisplayName("Should pass all equals contract tests")
        void testEqualsContract() {
            // Given
            String[] packages1 = new String[] { "com.example.a", "com.example.b" };
            String[] packages2 = new String[] { "com.example.a", "com.example.b" }; // Same content as packages1
            String[] packages3 = new String[] { "com.example.x" }; // Different content

            FrameworkAdapterContextCustomizer customizer1 = new FrameworkAdapterContextCustomizer(packages1);
            FrameworkAdapterContextCustomizer customizer2 = new FrameworkAdapterContextCustomizer(packages2);
            FrameworkAdapterContextCustomizer customizer3 = new FrameworkAdapterContextCustomizer(packages3);

            // Reflexive
            assertEquals(customizer1, customizer1, "Object should equal itself");

            // Symmetric
            assertEquals(customizer1, customizer2, "Objects with equal basePackages should be equal");
            assertEquals(customizer2, customizer1, "Equality should be symmetric");

            // Not equal to different object
            assertNotEquals(customizer1, customizer3, "Objects with different basePackages should not be equal");

            // Not equal to null
            assertNotEquals(null, customizer1, "Object should not equal null");

            // Not equal to different type
            assertNotEquals("string", customizer1, "Object should not equal different type");
        }

        @Test
        @DisplayName("Should have same hashCode for equal objects")
        void testHashCode() {
            // Given
            String[] packages1 = new String[] { "com.example.a", "com.example.b" };
            String[] packages2 = new String[] { "com.example.a", "com.example.b" };
            String[] packages3 = new String[] { "com.example.x" };

            FrameworkAdapterContextCustomizer customizer1 = new FrameworkAdapterContextCustomizer(packages1);
            FrameworkAdapterContextCustomizer customizer2 = new FrameworkAdapterContextCustomizer(packages2);
            FrameworkAdapterContextCustomizer customizer3 = new FrameworkAdapterContextCustomizer(packages3);

            // Then
            assertEquals(customizer1.hashCode(), customizer2.hashCode(),
                    "Equal objects should have same hashCode");

            assertNotEquals(customizer1.hashCode(), customizer3.hashCode(),
                    "Different objects should have different hashCode");
        }

        @Test
        @DisplayName("Should return correct string representation")
        void testToString() {
            // Given
            String[] packages = new String[] { "com.example.a", "com.example.b" };
            FrameworkAdapterContextCustomizer customizer = new FrameworkAdapterContextCustomizer(packages);

            // When
            String result = customizer.toString();

            // Then
            String expected = "FrameworkAdapterContextCustomizer{basePackages=" + Arrays.toString(packages) + "}";
            assertEquals(expected, result, "toString output should match expected format");
        }

        @Test
        @DisplayName("Equals should return false for different type")
        void equalsReturnsFalseForDifferentType() {
            // Given
            FrameworkAdapterContextCustomizer customizer =
                    new FrameworkAdapterContextCustomizer(new String[]{"com.example"});

            // When/Then
            assertFalse(customizer.equals("not a customizer"),
                    "Should return false when comparing with different type");
        }

        @Test
        @DisplayName("Equals should be order-sensitive for packages")
        void equalsIsOrderSensitive() {
            // Given
            FrameworkAdapterContextCustomizer customizer1 =
                    new FrameworkAdapterContextCustomizer(new String[]{"a", "b"});
            FrameworkAdapterContextCustomizer customizer2 =
                    new FrameworkAdapterContextCustomizer(new String[]{"b", "a"});

            // When/Then
            assertNotEquals(customizer1, customizer2,
                    "Should not be equal when package order differs");
        }
    }
}