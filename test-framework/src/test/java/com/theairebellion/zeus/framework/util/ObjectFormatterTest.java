package com.theairebellion.zeus.framework.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtensionContext;

import static org.mockito.Mockito.*;

@DisplayName("ObjectFormatter tests")
@ExtendWith(MockitoExtension.class)
public class ObjectFormatterTest {

    @Nested
    @DisplayName("formatObjectFields(Object)")
    class FormatObjectFieldsTests {

        @Test
        @DisplayName("Should return 'null' when the object is null")
        void shouldReturnNullForNullObject() {
            String result = ObjectFormatter.formatObjectFields(null);
            assertEquals("null", result);
        }

        @Test
        @DisplayName("Should format fields of a simple object")
        void shouldFormatFieldsOfSimpleObject() {
            class Dummy {
                String name = "John";
                int age = 30;
            }

            String formatted = ObjectFormatter.formatObjectFields(new Dummy());

            assertTrue(formatted.contains("name: John"));
            assertTrue(formatted.contains("age: 30"));
        }

        @Test
        @DisplayName("Should detect and report circular references")
        void shouldHandleCircularReference() {
            class Node {
                Node self;
            }
            Node node = new Node();
            node.self = node;

            String result = ObjectFormatter.formatObjectFields(node);
            assertTrue(result.contains("[Circular Reference Detected]"));
        }

        @Test
        @DisplayName("Should format nested objects recursively")
        void shouldFormatNestedObjects() {
            class Address {
                String city = "Skopje";
            }
            class User {
                String name = "Ana";
                Address address = new Address();
            }

            String formatted = ObjectFormatter.formatObjectFields(new User());

            assertTrue(formatted.contains("name: Ana"));
            assertTrue(formatted.contains("city: Skopje"));
        }

        @Test
        @DisplayName("Should format collections inside objects")
        void shouldFormatCollections() {
            class ListHolder {
                List<String> items = List.of("A", "B");
            }

            String formatted = ObjectFormatter.formatObjectFields(new ListHolder());

            assertTrue(formatted.contains("items: A, B"));
        }

        @Test
        @DisplayName("Should format arrays inside objects")
        void shouldFormatArrays() {
            class ArrayHolder {
                int[] numbers = {1, 2, 3};
            }

            String formatted = ObjectFormatter.formatObjectFields(new ArrayHolder());

            assertTrue(formatted.contains("numbers: [1, 2, 3]"));
        }

        @Test
        @DisplayName("Should handle objects with inaccessible fields gracefully")
        void shouldHandleInaccessibleFields() {
            Object anonymous = new Object() {
                private final String hidden = "secret";
            };

            String formatted = ObjectFormatter.formatObjectFields(anonymous);

            assertTrue(formatted.contains("hidden: secret") || formatted.contains("[Error accessing fields]"));
        }
    }

    @Nested
    @DisplayName("generateHtmlContent(Map)")
    class GenerateHtmlContentTests {

        @Test
        @DisplayName("Should insert formatted table rows into HTML template")
        void shouldInsertFormattedRowsIntoHtml() {
            Enum<?> dummyKey = TestKey.NAME;
            LinkedList<Object> values = new LinkedList<>();
            values.add("Value1");

            Map<Enum<?>, LinkedList<Object>> input = Map.of(dummyKey, values);

            // Mock ResourceLoader to return a fake HTML template
            String fakeTemplate = "<html><body>{{argumentRows}}</body></html>";
            mockStatic(ResourceLoader.class);
            when(ResourceLoader.loadResourceFile("allure/html/test-data.html")).thenReturn(fakeTemplate);

            String result = new ObjectFormatter().generateHtmlContent(input);

            assertTrue(result.contains("<tr><td>NAME</td><td>Value1</td></tr>"));
            assertFalse(result.contains("{{argumentRows}}"));

            clearAllCaches();
        }

        enum TestKey {
            NAME
        }
    }

    @Nested
    @DisplayName("getClassAnnotations(ExtensionContext)")
    class GetClassAnnotationsTests {

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.TYPE)
        @interface TestAnnotation {
            String value() default "default";
        }

        @TestAnnotation("hello")
        static class AnnotatedTestClass {
        }

        @Test
        @DisplayName("Should return formatted class annotations")
        void shouldReturnFormattedClassAnnotations() {
            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) AnnotatedTestClass.class);

            String annotations = ObjectFormatter.getClassAnnotations(context);

            assertTrue(annotations.contains("@TestAnnotation"));
        }

        @Test
        @DisplayName("Should return 'No class annotations' if none present")
        void shouldReturnNoAnnotations() {
            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) String.class);

            String annotations = ObjectFormatter.getClassAnnotations(context);

            assertEquals("No class annotations", annotations);
        }
    }

    @Nested
    @DisplayName("getMethodAnnotations(ExtensionContext)")
    class GetMethodAnnotationsTests {

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CustomTest {
            String value() default "";
        }

        static class AnnotatedMethodTestClass {
            @CustomTest("TestValue")
            public void method() {
            }
        }

        @Test
        @DisplayName("Should return formatted method annotations")
        void shouldReturnFormattedMethodAnnotations() throws NoSuchMethodException {
            ExtensionContext context = mock(ExtensionContext.class);
            Method method = AnnotatedMethodTestClass.class.getDeclaredMethod("method");

            when(context.getRequiredTestMethod()).thenReturn(method);

            String annotations = ObjectFormatter.getMethodAnnotations(context);

            assertTrue(annotations.contains("@CustomTest(value=TestValue)"));
        }
    }

    @Nested
    @DisplayName("escapeHtml(String)")
    class EscapeHtmlTests {

        @Test
        @DisplayName("Should escape HTML special characters")
        void shouldEscapeSpecialCharacters() {
            String input = "<div>& \" ' %";
            String expected = "&lt;div&gt;&amp; &quot; &#39; %%";
            assertEquals(expected, ObjectFormatter.escapeHtml(input));
        }

        @Test
        @DisplayName("Should return 'N/A' for null input")
        void shouldReturnNAForNull() {
            assertEquals("N/A", ObjectFormatter.escapeHtml(null));
        }
    }

    //

    @Nested
    @DisplayName("formatAnnotationsToNewRows")
    class FormatAnnotationsToNewRowsTests {

        @Test
        @DisplayName("Should return 'None' for null or empty annotations")
        void shouldReturnNoneForEmptyAnnotations() {
            String result = ObjectFormatter.formatAnnotationsToNewRows(null);
            assertEquals("None", result);

            result = ObjectFormatter.formatAnnotationsToNewRows("");
            assertEquals("None", result);
        }

        @Test
        @DisplayName("Should format annotations with multiple lines correctly")
        void shouldFormatAnnotationsCorrectly() {
            String annotations = "@Test\n@CustomAnnotation(value=hello)";
            String result = ObjectFormatter.formatAnnotationsToNewRows(annotations);
            assertTrue(result.contains("<pre class='annotation'>"));
            assertTrue(result.contains("@Test"));
            assertTrue(result.contains("@CustomAnnotation"));
        }
    }

    @Nested
    @DisplayName("formatLongText")
    class FormatLongTextTests {

        @Test
        @DisplayName("Should return 'None' for null or empty text")
        void shouldReturnNoneForEmptyText() {
            String result = ObjectFormatter.formatLongText(null);
            assertEquals("None", result);

            result = ObjectFormatter.formatLongText("");
            assertEquals("None", result);
        }

        @Test
        @DisplayName("Should format text correctly, breaking lines at 80 characters")
        void shouldFormatTextCorrectly() {
            String longText = "This is a long text that needs to be formatted into multiple lines so that each line is no more than 80 characters long.";
            String result = ObjectFormatter.formatLongText(longText);
            assertTrue(result.contains("<br>"));
        }
    }

    @Nested
    @DisplayName("getTestArguments")
    class GetTestArgumentsTests {

//        @Test
//        @DisplayName("Should return 'No arguments available' when no arguments are present")
//        void shouldReturnNoArgumentsAvailable() throws NoSuchMethodException {
//            Method method = ObjectFormatterTests.class.getDeclaredMethod("shouldReturnNoArgumentsAvailable");
//            ExtensionContext context = mock(ExtensionContext.class);
//            when(context.getTestMethod()).thenReturn(Optional.of(method));
//
//            String result = ObjectFormatter.getTestArguments(context);
//            assertEquals("No arguments available.", result);
//        }
//
//        @Test
//        @DisplayName("Should return argument types when method has arguments")
//        void shouldReturnArgumentTypes() throws NoSuchMethodException {
//            Method method = ObjectFormatterTests.class.getDeclaredMethod("shouldReturnArgumentTypes", String.class, int.class);
//            ExtensionContext context = mock(ExtensionContext.class);
//            when(context.getTestMethod()).thenReturn(Optional.of(method));
//
//            String result = ObjectFormatter.getTestArguments(context);
//            assertEquals("String, int", result);
//        }
    }

    @Nested
    @DisplayName("formatResponses")
    class FormatResponsesTests {

//            @Test
//            @DisplayName("Should correctly format responses with success, warning, and error counts")
//            void shouldFormatResponsesCorrectly() {
//                List<Object> responses = Arrays.asList(
//                        new Response(200),
//                        new Response(300),
//                        new Response(400)
//                );
//
//                ObjectFormatter formatter = new ObjectFormatter();
//                String result = formatter.formatResponses(responses);
//                assertTrue(result.contains("200"));
//                assertTrue(result.contains("300"));
//                assertTrue(result.contains("400"));
//            }

        @Test
        @DisplayName("Should handle an empty list of responses")
        void shouldHandleEmptyResponseList() {
            List<Object> responses = new ArrayList<>();

            ObjectFormatter formatter = new ObjectFormatter();
            String result = formatter.formatResponses(responses);
            assertTrue(result.contains("0"));
        }
    }


}
