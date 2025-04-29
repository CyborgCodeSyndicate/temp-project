package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.annotation.JourneyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@DisplayName("ObjectFormatter tests")
@ExtendWith(MockitoExtension.class)
public class ObjectFormatterTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface ClassAnnotationWithDefaultValue {
        String value() default "default";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface ClassAnnotationWithoutDefaultValue {
        String key();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ClassAnnotationWithoutArgs {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface ClassAnnotationWithArray {
        String[] tags() default {"one", "two"};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotationWithMultiValues {
        String key1() default "default";

        String key2();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MethodAnnotationWithDefaultValue {
        String value() default "";
    }


    @Nested
    @DisplayName("ObjectFormatter.formatObjectFields(Object) tests")
    class FormatObjectFieldsTests {

        enum TestKey {
            NULL_LIST, EMPTY_LIST, LAMBDA_CASE
        }

        @Test
        @DisplayName("Should return 'null' when the object is null")
        void shouldReturnNullForNullObject() {
            // Given
            Object input = null;

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertEquals("null", result);
        }

        @Test
        @DisplayName("Should format fields of a simple object")
        void shouldFormatFieldsOfSimpleObject() {
            // Given
            class SimpleObject {
                final String name = "John";
                final int age = 30;
            }

            SimpleObject input = new SimpleObject();

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertTrue(result.contains("name: John"));
            assertTrue(result.contains("age: 30"));
        }

        @Test
        @DisplayName("Should detect and report circular references")
        void shouldHandleCircularReference() {
            // Given
            class Node {
                Node self;
            }

            Node node = new Node();
            node.self = node;

            // When
            String result = ObjectFormatter.formatObjectFields(node);

            // Then
            assertTrue(result.contains("[Circular Reference Detected]"));
        }

        @Test
        @DisplayName("Should format nested objects recursively")
        void shouldFormatNestedObjects() {
            // Given
            class Address {
                final String city = "Skopje";
            }

            class User {
                final String name = "Ana";
                final Address address = new Address();
            }

            User user = new User();

            // When
            String result = ObjectFormatter.formatObjectFields(user);

            // Then
            assertTrue(result.contains("name: Ana"));
            assertTrue(result.contains("city: Skopje"));
        }

        @Test
        @DisplayName("Should format collections inside objects")
        void shouldFormatCollections() {
            // Given
            class CollectionHolder {
                final List<String> items = List.of("A", "B");
            }

            CollectionHolder input = new CollectionHolder();

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertTrue(result.contains("items: A, B"));
        }

        @Test
        @DisplayName("Should format arrays inside objects")
        void shouldFormatArrays() {
            // Given
            class ArrayHolder {
                final int[] numbers = {1, 2, 3};
            }

            ArrayHolder input = new ArrayHolder();

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertTrue(result.contains("numbers: [1, 2, 3]"));
        }

        @Test
        @DisplayName("Should handle objects with inaccessible fields gracefully")
        void shouldHandleInaccessibleFields() {
            // Given
            Object anonymousObject = new Object() {
                private final String hidden = "secret";
            };

            // When
            String result = ObjectFormatter.formatObjectFields(anonymousObject);

            // Then
            assertTrue(result.contains("hidden: secret") || result.contains("[Error accessing fields]"));
        }

        @Test
        @DisplayName("Should format enum values properly")
        void shouldFormatEnumValue() {
            // Given
            enum Status { OK }

            Status status = Status.OK;

            // When
            String result = ObjectFormatter.formatObjectFields(status);

            // Then
            assertEquals("OK", result);
        }

        @Test
        @DisplayName("Should format static fields as well")
        void shouldFormatStaticFields() {
            // Given
            class StaticHolder {
                static final String staticValue = "I am static";
            }

            StaticHolder input = new StaticHolder();

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertTrue(result.contains("staticValue: I am static"));
        }

        @Test
        @DisplayName("Should handle null field values")
        void shouldHandleNullFieldValues() {
            // Given
            class NullFieldHolder {
                final String name = null;
            }

            NullFieldHolder input = new NullFieldHolder();

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertTrue(result.contains("name: null"));
        }

        @Test
        @DisplayName("Should handle primitive wrapper fields (String, Number, Boolean)")
        void shouldHandlePrimitiveWrapperFields() {
            // Given
            class WrapperFields {
                final String str = "hello";
                final Integer number = 123;
                final Boolean bool = true;
            }

            WrapperFields input = new WrapperFields();

            // When
            String result = ObjectFormatter.formatObjectFields(input);

            // Then
            assertTrue(result.contains("str: hello"));
            assertTrue(result.contains("number: 123"));
            assertTrue(result.contains("bool: true"));
        }

        @Test
        @DisplayName("Should return empty string when input list is null")
        void shouldReturnEmptyStringWhenListIsNull() {
            // Given
            Map<Enum<?>, List<Object>> input = new HashMap<>();
            input.put(TestKey.NULL_LIST, null);

            try (MockedStatic<ResourceLoader> mocked = mockStatic(ResourceLoader.class)) {
                mocked.when(() -> ResourceLoader.loadResourceFile("allure/html/test-data.html"))
                        .thenReturn("<html><body>{{argumentRows}}</body></html>");

                // When
                String result = ObjectFormatter.generateHtmlContent(input);

                // Then
                assertFalse(result.contains("{{argumentRows}}"));
                assertEquals("<html><body></body></html>", result);
            }
        }

        @Test
        @DisplayName("Should return empty string when input list is empty")
        void shouldReturnEmptyStringWhenListIsEmpty() {
            // Given
            Map<Enum<?>, List<Object>> input = Map.of(TestKey.EMPTY_LIST, new ArrayList<>());

            try (MockedStatic<ResourceLoader> mocked = mockStatic(ResourceLoader.class)) {
                mocked.when(() -> ResourceLoader.loadResourceFile("allure/html/test-data.html"))
                        .thenReturn("<html><body>{{argumentRows}}</body></html>");

                // When
                String result = ObjectFormatter.generateHtmlContent(input);

                // Then
                assertFalse(result.contains("{{argumentRows}}"));
                assertEquals("<html><body></body></html>", result);
            }
        }

        @Test
        @DisplayName("Should skip lambda objects in formatting")
        void shouldSkipLambdaObjects() {
            // Given
            List<Object> values = new ArrayList<>();
            values.add((Runnable) () -> {});
            values.add("VisibleValue");

            Map<Enum<?>, List<Object>> input = Map.of(TestKey.LAMBDA_CASE, values);

            try (MockedStatic<ResourceLoader> mocked = mockStatic(ResourceLoader.class)) {
                mocked.when(() -> ResourceLoader.loadResourceFile("allure/html/test-data.html"))
                        .thenReturn("<html><body>{{argumentRows}}</body></html>");

                // When
                String result = ObjectFormatter.generateHtmlContent(input);

                // Then
                assertTrue(result.contains("VisibleValue"));
                assertFalse(result.contains("Lambda"));
            }
        }
    }

    @Nested
    @DisplayName("ObjectFormatter.generateHtmlContent(Map) tests")
    class GenerateHtmlContentTests {

        @Test
        @DisplayName("Should insert table rows for enum keys with values and remove template placeholders")
        void shouldInsertFormattedRowsIntoHtml() {
            // Given
            enum TestKey {
                NAME
            }
            Enum<?> enumKey = TestKey.NAME;
            List<Object> inputValues = new ArrayList<>();
            inputValues.add("Value1");
            Map<Enum<?>, List<Object>> inputMap = Map.of(enumKey, inputValues);
            String htmlTemplate = "<html><body>{{argumentRows}}</body></html>";

            try (MockedStatic<ResourceLoader> mocked = mockStatic(ResourceLoader.class)) {
                mocked.when(() -> ResourceLoader.loadResourceFile("allure/html/test-data.html"))
                        .thenReturn(htmlTemplate);

                // When
                String result = ObjectFormatter.generateHtmlContent(inputMap);

                // Then
                assertTrue(result.contains("<tr><td>NAME</td><td>Value1</td></tr>"), "HTML should contain the formatted table row");
                assertFalse(result.contains("{{argumentRows}}"), "HTML should not contain the placeholder anymore");
            }
        }

        @Test
        @DisplayName("Should ignore entries with null or empty lists and produce clean HTML")
        void shouldIgnoreNullOrEmptyLists() {
            // Given
            enum DummyEnum {
                KEY1, KEY2
            }
            Enum<?> nullListKey = DummyEnum.KEY1;
            Enum<?> emptyListKey = DummyEnum.KEY2;

            Map<Enum<?>, List<Object>> inputMap = new HashMap<>();
            inputMap.put(nullListKey, null);
            inputMap.put(emptyListKey, new ArrayList<>());

            String htmlTemplate = "<html><body>{{argumentRows}}</body></html>";

            try (MockedStatic<ResourceLoader> mocked = mockStatic(ResourceLoader.class)) {
                mocked.when(() -> ResourceLoader.loadResourceFile("allure/html/test-data.html"))
                        .thenReturn(htmlTemplate);

                // When
                String result = ObjectFormatter.generateHtmlContent(inputMap);

                // Then
                assertFalse(result.contains("<tr>"), "HTML should not contain any table rows");
                assertFalse(result.contains("{{argumentRows}}"), "HTML should not contain the template placeholder anymore");

                clearAllCaches();
            }
        }
    }

    @Nested
    @DisplayName("ObjectFormatter.getClassAnnotations(ExtensionContext) tests")
    class GetClassAnnotationsTests {

        @Test
        @DisplayName("Should return fallback message when no annotations are present on the class")
        void shouldReturnNoAnnotations() {
            // Given
            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) String.class);

            // When
            String annotations = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertEquals("No class annotations", annotations);
        }

        @Test
        @DisplayName("Should include annotation with argument values when present")
        void shouldFormatAnnotationsWithArguments() {
            // Given
            @ClassAnnotationWithoutDefaultValue(key = "value")
            class AnnotatedWithArgsTestClass {}

            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) AnnotatedWithArgsTestClass.class);

            // When
            String annotations = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertTrue(annotations.contains("@ClassAnnotationWithoutDefaultValue(key=value)"));
        }

        @Test
        @DisplayName("Should include annotations that only have default arguments")
        void shouldSkipDefaultOnlyAnnotationArgs() {
            // Given
            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) GetClassAnnotationsTests.class);

            // When
            String annotations = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertTrue(annotations.contains("@Nested"));
        }

        @Test
        @DisplayName("Should skip annotation argument if its value equals the default")
        void shouldSkipAnnotationArgumentIfItEqualsDefault() {
            // Given
            @ClassAnnotationWithDefaultValue
            class Annotated {}

            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) Annotated.class);

            // When
            String annotations = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertTrue(annotations.contains("@ClassAnnotationWithDefaultValue"));
            assertFalse(annotations.contains("value="));
        }

        @Test
        @DisplayName("Should correctly format annotations with array values")
        void shouldFormatArrayAnnotationArguments() {
            // Given
            @ClassAnnotationWithArray(tags = {"foo", "bar"})
            class ArrayAnnotatedClass {}

            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) ArrayAnnotatedClass.class);

            // When
            String annotations = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertTrue(annotations.contains("@ClassAnnotationWithArray(tags=foo bar)"));
        }

        @Test
        @DisplayName("Should include annotation argument when it differs from its default")
        void shouldIncludeArgIfNotDefault() {
            // Given
            @ClassAnnotationWithDefaultValue("nonDefaultValue")
            class NonDefaultTestClass {}

            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) NonDefaultTestClass.class);

            // When
            String annotations = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertTrue(annotations.contains("value=nonDefaultValue"));
        }

        @Test
        @DisplayName("Should skip default value and include only custom ones in multi-value annotation")
        void shouldTriggerEqualsDefaultValueBranch() {
            // Given
            @ClassAnnotationWithMultiValues(key2 = "something")
            class Annotated {}

            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getRequiredTestClass()).thenReturn((Class) Annotated.class);

            // When
            String result = ObjectFormatter.getClassAnnotations(context);

            // Then
            assertTrue(result.contains("@ClassAnnotationWithMultiValues(key2=something)"));
            assertFalse(result.contains("key1="));
        }

    }

    @Nested
    @DisplayName("ObjectFormatter.getMethodAnnotations(ExtensionContext) tests")
    class GetMethodAnnotationsTests {

        @Test
        @DisplayName("Should return formatted method annotation with non-default value")
        void shouldReturnFormattedMethodAnnotations() throws NoSuchMethodException {
            // Given
            class AnnotatedMethodTestClass {
                @MethodAnnotationWithDefaultValue("TestValue")
                public void method() {}
            }

            ExtensionContext context = mock(ExtensionContext.class);
            Method testMethod = AnnotatedMethodTestClass.class.getDeclaredMethod("method");
            when(context.getRequiredTestMethod()).thenReturn(testMethod);

            // When
            String annotations = ObjectFormatter.getMethodAnnotations(context);

            // Then
            assertTrue(annotations.contains("@MethodAnnotationWithDefaultValue(value=TestValue)"));
        }

        @Test
        @DisplayName("Should format method annotation when no arguments are present")
        void shouldFormatMethodAnnotationWithoutArguments() throws NoSuchMethodException {
            // Given
            class NoArgAnnotatedMethodTestClass {
                @ClassAnnotationWithoutArgs
                public void noArgMethod() {}
            }

            ExtensionContext context = mock(ExtensionContext.class);
            Method testMethod = NoArgAnnotatedMethodTestClass.class.getDeclaredMethod("noArgMethod");
            when(context.getRequiredTestMethod()).thenReturn(testMethod);

            // When
            String annotations = ObjectFormatter.getMethodAnnotations(context);

            // Then
            assertTrue(annotations.contains("@ClassAnnotationWithoutArgs"));
        }
    }

    @Nested
    @DisplayName("ObjectFormatter.escapeHtml(String) tests")
    class EscapeHtmlTests {

        @Test
        @DisplayName("Should escape all special HTML characters")
        void shouldEscapeSpecialCharacters() {
            // Given
            String input = "<div>& \" ' %";
            String expected = "&lt;div&gt;&amp; &quot; &#39; %%";

            // When
            String result = ObjectFormatter.escapeHtml(input);

            // Then
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Should return 'N/A' when input is null")
        void shouldReturnNAForNull() {
            // Given
            String input = null;

            // When
            String result = ObjectFormatter.escapeHtml(input);

            // Then
            assertEquals("N/A", result);
        }
    }

    @Nested
    @DisplayName("ObjectFormatter.formatAnnotationsToNewRows(String) tests")
    class FormatAnnotationsToNewRowsTests {

        @Test
        @DisplayName("Should return 'None' when annotations input is null or empty")
        void shouldReturnNoneForEmptyAnnotations() {
            // Given & When
            String resultNull = ObjectFormatter.formatAnnotationsToNewRows(null);
            String resultEmpty = ObjectFormatter.formatAnnotationsToNewRows("");

            // Then
            assertEquals("None", resultNull);
            assertEquals("None", resultEmpty);
        }

        @Test
        @DisplayName("Should wrap each annotation line inside <pre> correctly")
        void shouldFormatAnnotationsCorrectly() {
            // Given
            String annotations = "@Test\n@CustomAnnotation(value=hello)";

            // When
            String result = ObjectFormatter.formatAnnotationsToNewRows(annotations);

            // Then
            assertTrue(result.contains("<pre class='annotation'>"));
            assertTrue(result.contains("@Test"));
            assertTrue(result.contains("@CustomAnnotation"));
        }

        @Test
        @DisplayName("Should not skip annotation line with only leading spaces")
        void shouldTriggerTrimIsEmptyCondition() {
            // Given
            String annotations = "   @Test";

            // When
            String result = ObjectFormatter.formatAnnotationsToNewRows(annotations);

            // Then
            assertTrue(result.contains("@Test"));
        }
    }

    @Nested
    @DisplayName("ObjectFormatter.formatLongText(String) tests")
    class FormatLongTextTests {

        @Test
        @DisplayName("Should return 'None' when input is null or empty")
        void shouldReturnNoneForEmptyText() {
            // Given & When
            String resultNull = ObjectFormatter.formatLongText(null);
            String resultEmpty = ObjectFormatter.formatLongText("");

            // Then
            assertEquals("None", resultNull);
            assertEquals("None", resultEmpty);
        }

        @Test
        @DisplayName("Should insert <br> tags for lines longer than 80 characters")
        void shouldFormatTextCorrectly() {
            // Given
            String longText = "This is a long text that needs to be formatted into multiple lines so that each line is no more than 80 characters long.";

            // When
            String result = ObjectFormatter.formatLongText(longText);

            // Then
            assertTrue(result.contains("<br>"));
        }
    }

    @Nested
    @DisplayName("ObjectFormatter.getTestArguments(ExtensionContext) tests")
    class GetTestArgumentsTests {

        void dummyMethodWithArgs(String name, int count) {
        }

        void dummyMethodWithoutArgs() {
        }

        @Test
        @DisplayName("Should return 'No arguments available.' when test method is absent")
        void shouldReturnNoArgumentsAvailable() {
            // Given
            ExtensionContext context = mock(ExtensionContext.class);
            lenient().when(context.getTestMethod()).thenReturn(Optional.empty());

            // When
            String result = ObjectFormatter.getTestArguments(context);

            // Then
            assertEquals("No arguments available.", result);
        }

        @Test
        @DisplayName("Should return a comma-separated list of argument types for methods with parameters")
        void shouldReturnArgumentTypes() throws NoSuchMethodException {
            // Given
            Method method = GetTestArgumentsTests.class.getDeclaredMethod("dummyMethodWithArgs", String.class, int.class);
            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getTestMethod()).thenReturn(Optional.of(method));
            when(context.getTestInstance()).thenReturn(Optional.of(this));

            // When
            String result = ObjectFormatter.getTestArguments(context);

            // Then
            assertEquals("String, int", result);
        }

        @Test
        @DisplayName("Should return empty string for methods with no arguments")
        void shouldReturnEmptyForNoArguments() throws NoSuchMethodException {
            // Given
            Method method = GetTestArgumentsTests.class.getDeclaredMethod("dummyMethodWithoutArgs");
            ExtensionContext context = mock(ExtensionContext.class);
            when(context.getTestMethod()).thenReturn(Optional.of(method));
            when(context.getTestInstance()).thenReturn(Optional.of(this));

            // When
            String result = ObjectFormatter.getTestArguments(context);

            // Then
            assertEquals("", result);
        }
    }


    @Nested
    @DisplayName("ObjectFormatter.formatResponses(List<Object>) tests") //todo: Check these tests, they seem complex
    class FormatResponsesTests {

        private static JourneyData createJourneyData(String value, boolean late) {
            return new JourneyData() {
                @Override
                public String value() {
                    return value;
                }

                @Override
                public boolean late() {
                    return late;
                }

                @Override
                public Class<? extends java.lang.annotation.Annotation> annotationType() {
                    return JourneyData.class;
                }
            };
        }

//        @Test
//        @DisplayName("Should correctly format responses with success, warning, and error counts")
//        void shouldFormatResponsesCorrectly() {
//            // Given
//            List<Object> responses = Arrays.asList(
//                    createMockResponse("http://test.com/endpoint1", 200, "OK"),
//                    createMockResponse("http://test.com/endpoint2", 300, "Redirect"),
//                    createMockResponse("http://test.com/endpoint3", 400, "Error")
//            );
//
//            // When
//            String result = ObjectFormatter.formatResponses(responses);
//
//            // Then
//            assertTrue(result.contains("<div class='status status-success'>200</div>"));
//            assertTrue(result.contains("<div class='status status-warning'>300</div>"));
//            assertTrue(result.contains("<div class='status status-error'>400</div>"));
//        }
//
//        @Test
//        @DisplayName("Should handle an empty list of responses")
//        void shouldHandleEmptyResponseList() {
//            // Given
//            List<Object> responses = new ArrayList<>();
//            ObjectFormatter formatter = ObjectFormatter;
//
//            // When
//            String result = formatter.formatResponses(responses);
//
//            // Then
//            assertTrue(result.contains("0"));
//        }

        @Test
        @DisplayName("Should return 'No data available' when processedData is null")
        void shouldReturnNoDataWhenProcessedDataIsNull() {
            // Given

            // When
            String result = ObjectFormatter.formatProcessedData(new JourneyData[0], null);

            // Then
            assertEquals("No data available", result);
        }

        @Test
        @DisplayName("Should return 'No data available' when processedData is empty")
        void shouldReturnNoDataWhenProcessedDataIsEmpty() {
            // Given

            // When
            String result = ObjectFormatter.formatProcessedData(new JourneyData[0], new Object[0]);

            // Then
            assertEquals("No data available", result);
        }

        @Test
        @DisplayName("Should format null processedData entry")
        void shouldHandleNullProcessedDataEntry() {
            // Given
            JourneyData[] originalData = {
                    createJourneyData("MockedValue", false)
            };
            Object[] processedData = { null };

            // When
            String result = ObjectFormatter.formatProcessedData(originalData, processedData);

            // Then
            assertTrue(result.contains("Data: null"));
        }

        @Test
        @DisplayName("Should add newline between multiple entries")
        void shouldAddNewlineBetweenEntries() {
            // Given
            JourneyData[] originalData = {
                    createJourneyData("J1", false),
                    createJourneyData("J2", false)
            };
            Object[] processedData = { "data1", "data2" };

            // When
            String result = ObjectFormatter.formatProcessedData(originalData, processedData);

            // Then
            assertTrue(result.contains("Journey: J1"));
            assertTrue(result.contains("Journey: J2"));
            assertTrue(result.contains("\n\n"));
        }

        @Test
        @DisplayName("Should fallback to data.toString() when JSON serialization fails")
        void shouldFallbackToToStringOnJsonException() {
            // Given
            JourneyData[] originalData = new JourneyData[]{createJourneyData("FailingJourney", false)};

            Object[] processedData = {
                    new Object() {
                        public final Object self = this;

                        @Override
                        public String toString() {
                            return "UnserializableObject";
                        }
                    }
            };

            // When
            String result = ObjectFormatter.formatProcessedData(originalData, processedData);

            // Then
            assertTrue(result.contains("Journey: FailingJourney"));
            assertTrue(result.contains("Data: UnserializableObject"));
        }

    @Nested
    @DisplayName("ObjectFormatter.formatProcessedData(JourneyData[], Object[]) tests")
    class FormatProcessingDataTests {

        static class MockResponse {
            private final String url;
            private final int status;
            private final String body;

            public MockResponse(String url, int status, String body) {
                this.url = url;
                this.status = status;
                this.body = body;
            }

            public String getUrl() {
                return url;
            }

            public int getStatus() {
                return status;
            }

            public String getBody() {
                return body;
            }

            public String getMethod() {
                return "GET";
            }
        }

        private MockResponse createMockResponse(String url, int status, String body) {
            return new MockResponse(url, status, body);
        }

        private JourneyData createJourney(String value) {
            return new JourneyData() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return JourneyData.class;
                }

                @Override
                public String value() {
                    return value;
                }

                @Override
                public boolean late() {
                    return false;
                }
            };
        }

        @Test
        @DisplayName("Should correctly handle response formatting when no responses are provided")
        void shouldHandleEmptyResponsesGracefully() {
            // Given
            List<Object> responses = new ArrayList<>();

            // When
            String result = ObjectFormatter.formatProcessedData(new JourneyData[]{}, responses.toArray());

            // Then
            assertEquals("No data available", result);
        }

        @Test
        @DisplayName("Should correctly handle a response with a missing URL")
        void shouldHandleMissingUrlGracefully() {
            // Given
            MockResponse response = new MockResponse(null, 200, "OK");

            JourneyData[] journeys = new JourneyData[] {
                    createJourney("Test Journey")
            };

            // When
            String result = ObjectFormatter.formatProcessedData(journeys, new Object[]{response});

            // Then
            assertTrue(result.contains("Test Journey"));
        }

        @Test
        @DisplayName("Should return a default message when response body is empty")
        void shouldHandleEmptyResponseBodyGracefully() {
            // Given
            MockResponse response = createMockResponse("http://test.com/endpoint1", 200, "");

            JourneyData[] journeys = new JourneyData[] {
                    createJourney("Test Journey")
            };

            // When
            String result = ObjectFormatter.formatProcessedData(journeys, new Object[]{response});

            // Then
            assertTrue(result.contains("Test Journey"));
        }
    }


}}
