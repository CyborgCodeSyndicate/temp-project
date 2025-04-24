package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllureStepHelperTest {

    @Mock
    private ExtensionContext mockContext;

    @Mock
    private ExtensionContext.Store mockStore;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface DummyTestAnnotation {
        String value();
    }

    @DummyTestAnnotation("SomeValue")
    public void annotatedDummyTestMethod() {
        // Nothing here, just for metadata testing
    }

    @Test
    void testSetDescription() {
        // Arrange
        List<String> htmlContent = List.of("<td>Some content</td>", "<td>Other content</td>");

        // Mock behavior: getStore returns the mocked store
        lenient().when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

        // Mock the store's behavior: get("HTML") returns the htmlContent
        lenient().when(mockStore.get(HTML)).thenReturn(htmlContent);

        // Mock Allure's static method descriptionHtml
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            // Act
            AllureStepHelper.setDescription(mockContext);

            // Assert: Verify that Allure.descriptionHtml was called with the correct combinedHtml
            String expectedHtml = "<div style='margin: 20px;'><td>Some content</td><td>Other content</td></div>";
            mockedAllure.verify(() -> Allure.descriptionHtml(eq(expectedHtml)), times(1));
        }
    }

    @Test
    void testAttachFilteredLogsToAllure() {
        String testName = "testScenario";

        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            // Verify that logs are added as an attachment
            mockedAllure.verify(() ->
                            Allure.addAttachment(anyString(), eq("text/plain"), anyString(), eq(".log")),
                    times(1)
            );
        }
    }

    @Test
    void testAttachFilteredLogsToAllure_NullTestName() {
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(null);

            mockedAllure.verify(() ->
                            Allure.addAttachment(
                                    eq("Filtered Logs"),
                                    eq("text/plain"),
                                    eq("Test name is not available."),
                                    eq(".log")
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testAttachFilteredLogsToAllure_EmptyTestName() {
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure("");

            mockedAllure.verify(() ->
                            Allure.addAttachment(
                                    eq("Filtered Logs"),
                                    eq("text/plain"),
                                    eq("Test name is not available."),
                                    eq(".log")
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testAttachFilteredLogsToAllure_WhenIOExceptionOccurs() {
        String testName = "testScenario";
        String invalidLogPath = "invalid/path/to/logfile.log";
        System.setProperty("logFileName", invalidLogPath);

        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            mockedAllure.verify(() ->
                    Allure.addAttachment(
                            eq("Filtered Logs for Test: " + testName),
                            eq("text/plain"),
                            argThat((String content) -> content.startsWith("Failed to read logs. Error:")),
                            eq(".log")
                    )
            );
        }
    }

    @Test
    void testAttachFilteredLogsToAllure_NoLogsFound() {
        String testName = "nonExistentScenario";

        // Clear the property so default path ("logs/zeus.log") is used
        System.clearProperty("logFileName");

        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            mockedAllure.verify(() ->
                            Allure.addAttachment(
                                    eq("Filtered Logs for Test: nonExistentScenario"),
                                    eq("text/plain"),
                                    eq("No logs found for test: nonExistentScenario"),
                                    eq(".log")
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testAttachFilteredLogsToAllure_WhenMatchingLogsExist() throws IOException {
        String testName = "myScenario";
        String testIdentifier = "[scenario=" + testName + "]";
        String matchingLine1 = testIdentifier + " Log line 1";
        String matchingLine2 = testIdentifier + " Log line 2";

        // Create a temporary file and write matching lines
        Path tempLogFile = Files.createTempFile("logfile", ".log");
        Files.write(tempLogFile, List.of(
                "[scenario=someOtherScenario] Not related log",
                matchingLine1,
                matchingLine2
        ));

        // Point system property to the custom log file
        System.setProperty("logFileName", tempLogFile.toAbsolutePath().toString());

        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            String expectedAttachment = matchingLine1 + System.lineSeparator() + matchingLine2;

            mockedAllure.verify(() ->
                    Allure.addAttachment(
                            eq("Filtered Logs for Test: " + testName),
                            eq("text/plain"),
                            eq(expectedAttachment),
                            eq(".log")
                    )
            );
        } finally {
            Files.deleteIfExists(tempLogFile);
        }
    }


    @Test
    void testLogTestOutcome_Success() {
        String testName = "testScenario";
        long durationInSeconds = 5;

        try (MockedStatic<LogTest> mockedLogTest = mockStatic(LogTest.class)) {
            AllureStepHelper.logTestOutcome(testName, "SUCCESS", durationInSeconds, null);

            // Verify that LogTest.info was called
            mockedLogTest.verify(() ->
                            LogTest.info(anyString(), eq(testName), eq("SUCCESS"), eq(durationInSeconds)),
                    times(1)
            );
        }
    }

    @Test
    void testLogTestOutcome_Failure() {
        String testName = "testScenario";
        long durationInSeconds = 5;
        Throwable throwable = new RuntimeException("Test failed");

        try (MockedStatic<LogTest> mockedLogTest = mockStatic(LogTest.class)) {
            AllureStepHelper.logTestOutcome(testName, "FAILED", durationInSeconds, throwable);

            // Verify that LogTest.info and LogTest.debug were called
            mockedLogTest.verify(() ->
                            LogTest.info(anyString(), eq(testName), eq("FAILED"), eq(durationInSeconds)),
                    times(1)
            );
            mockedLogTest.verify(() ->
                            LogTest.debug(eq("Failure reason:"), eq(throwable)),
                    times(1)
            );
        }
    }

    @Test
    void testSetUpTestMetadata() {

        // Mock the static method ResourceLoader.loadResourceFile
        try (MockedStatic<ResourceLoader> mockedStatic = mockStatic(ResourceLoader.class)) {
            // Mock the ResourceLoader to return a simple HTML template
            String htmlTemplate = "<html><body>{{testName}} {{className}} {{methodAnnotations}}</body></html>";
            mockedStatic.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(htmlTemplate);

            // Mock the context methods
            Method mockMethod = mock(Method.class);
            when(mockMethod.getName()).thenReturn("testMethod");

            // Mock getAnnotations() to return an empty array or a mock annotation array
            Annotation[] annotations = {}; // You can also use mock annotations if needed
            when(mockMethod.getAnnotations()).thenReturn(annotations);

            Class<?> testClass = AllureStepHelperTest.class;
            when(mockContext.getRequiredTestMethod()).thenReturn(mockMethod);
            when(mockContext.getRequiredTestClass()).thenReturn((Class) testClass);

            // Mock the global store and the list that will hold the formatted HTML
            ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
            when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
            List<String> htmlList = new ArrayList<>();
            when(mockStore.get(HTML, List.class)).thenReturn(htmlList);

            // Act
            AllureStepHelper.setUpTestMetadata(mockContext);

            // Assert: Verify that the formatted HTML was added to the list and put into the store
            verify(mockStore, times(1)).put(eq(HTML), eq(htmlList));  // Verify that put was called once with the correct arguments
        }
    }

    @Test
    void testSetUpTestMetadata_WhenHtmlListIsNull() throws NoSuchMethodException {
        // Arrange
        ExtensionContext mockContext = mock(ExtensionContext.class);
        ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);

        // Mocking the store to return the mockStore
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

        // Simulate null value for HTML
        when(mockStore.get(HTML, List.class)).thenReturn(null);

        // Mock ResourceLoader to avoid file reading
        try (MockedStatic<ResourceLoader> mockedResourceLoader = mockStatic(ResourceLoader.class)) {
            String mockHtmlTemplate = "<html>{{testName}}</html>";
            mockedResourceLoader.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(mockHtmlTemplate);

            // Mock getRequiredTestMethod() to return a valid Method
            Method mockMethod = AllureStepHelperTest.class.getDeclaredMethod("testSetUpTestMetadata_WhenHtmlListIsNull");
            when(mockContext.getRequiredTestMethod()).thenReturn(mockMethod);

            // Mock getRequiredTestClass() if needed
            when(mockContext.getRequiredTestClass()).thenReturn((Class) AllureStepHelperTest.class);

            // Act
            AllureStepHelper.setUpTestMetadata(mockContext);

            // Assert
            verify(mockStore).put(eq(HTML), anyList()); // Ensure the list is added to the store
        }
    }

    @Test
    void testSetUpTestMetadata_WithCustomAnnotation() throws NoSuchMethodException {
        try (MockedStatic<ResourceLoader> mockedResourceLoader = mockStatic(ResourceLoader.class)) {
            // Simulate loading a template that expects annotations
            String template = "<html>{{methodAnnotations}}</html>";
            mockedResourceLoader.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(template);

            // Find the annotated method
            Method annotatedMethod = AllureStepHelperTest.class.getDeclaredMethod("annotatedDummyTestMethod");

            // Mock the context and store
            when(mockContext.getRequiredTestMethod()).thenReturn(annotatedMethod);
            when(mockContext.getRequiredTestClass()).thenReturn((Class) AllureStepHelperTest.class);
            when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

            List<String> htmlList = new ArrayList<>();
            when(mockStore.get(HTML, List.class)).thenReturn(htmlList);

            // Act
            AllureStepHelper.setUpTestMetadata(mockContext);

            // Assert the HTML list contains the processed annotation string
            assertFalse(htmlList.isEmpty());
            assertTrue(htmlList.get(0).contains("DummyTestAnnotation")); // or "SomeValue", depending on your reduce logic
        }
    }

    @Test
    void testSetupTestContext() throws NoSuchMethodException {
        // Mocking ExtensionContext
        ExtensionContext mockContext = mock(ExtensionContext.class);
        when(mockContext.getTestClass()).thenReturn(Optional.of(AllureStepHelperTest.class));
        when(mockContext.getTestMethod()).thenReturn(Optional.of(AllureStepHelperTest.class.getDeclaredMethod("testSetDescription")));
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

        // Mocking static method in ThreadContext using mockStatic
        try (MockedStatic<ThreadContext> mockedThreadContext = mockStatic(ThreadContext.class)) {
            AllureStepHelper.setupTestContext(mockContext);

            // Verify that the static method ThreadContext.put was called
            mockedThreadContext.verify(() ->
                            ThreadContext.put(eq("testName"), eq("AllureStepHelperTest.testSetDescription")),
                    times(1)
            );
        }
    }

    @Test
    void testInitializeTestEnvironment_withMockedReflectionUtil() {
        try (MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
             MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {

            // Dummy class to simulate found config
            List<Class<? extends PropertyConfig>> dummyConfigs = List.of(DummyPropertyConfig.class);
            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(any(), any())
            ).thenReturn(dummyConfigs);

            // Dummy config object
            DummyPropertyConfig dummyConfig = new DummyPropertyConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(DummyPropertyConfig.class))
                    .thenReturn(dummyConfig);

            // ⬇️ Fix for null FrameworkConfig
            FrameworkConfig dummyFrameworkConfig = mock(FrameworkConfig.class);
            lenient().when(dummyFrameworkConfig.projectPackage()).thenReturn("com.theairebellion.zeus");

            mockedConfigCache.when(() -> ConfigCache.getOrCreate(FrameworkConfig.class))
                    .thenReturn(dummyFrameworkConfig);

            // Act
            AllureStepHelper.initializeTestEnvironment();
            // Optionally assert that files were written, etc.
        }
    }

    @Test
    void test_collectConfigurationProperties_withValidConfig() throws Exception {
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            // Mock implementation return
            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(PropertyConfig.class, "com.theairebellion.zeus")
            ).thenReturn(List.of(DummyConfig.class));


            // Mock ConfigCache.getOrCreate
            DummyConfig dummyConfig = new DummyConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(DummyConfig.class)).thenReturn(dummyConfig);

            // Mock FrameworkConfigHolder.getFrameworkConfig
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            // Call private method via reflection
            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);
            System.out.println("Collected result: " + result);
            // Verify results
            assertNotNull(result);
            assertTrue(result.containsKey("test.key"));
            assertEquals("value123 (Source: testSource)", result.get("test.key").get(0));
        }
    }

    @Test
    void test_collectConfigurationProperties_skipsEmptyValues() throws Exception {
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class) // add the mock for FrameworkConfigHolder
        ) {
            // Mock FrameworkConfigHolder.getFrameworkConfig()
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(eq(PropertyConfig.class), anyString())
            ).thenReturn(List.of(DummyEmptyConfig.class));

            DummyEmptyConfig emptyConfig = new DummyEmptyConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(DummyEmptyConfig.class)).thenReturn(emptyConfig);

            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);

            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            assertNotNull(result);
            assertTrue(result.isEmpty() || !result.containsKey("empty.key"));
        }
    }

    @Test
    void test_collectConfigurationProperties_handlesReflectionException() throws Exception {
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            // ✅ Mock config holder
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            // Setup class that will be processed
            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(eq(PropertyConfig.class), anyString())
            ).thenReturn(List.of(DummyThrowsConfig.class));

            // Return the real class that throws
            DummyThrowsConfig dummy = new DummyThrowsConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(DummyThrowsConfig.class)).thenReturn(dummy);

            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            assertNotNull(result); // Should still return a map
            assertTrue(result.isEmpty()); // Because the method throws, nothing is added
        }
    }

//    @Test
//    void test_collectConfigurationProperties_skipsMethodsWithoutKeyAnnotation() throws Exception {
//        try (
//                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
//                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
//                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
//        ) {
//            // Mock FrameworkConfigHolder.getFrameworkConfig()
//            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
//            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
//            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);
//
//            // Mock ReflectionUtil to return DummyConfigWithoutKey class
//            mockedReflectionUtil.when(() ->
//                    ReflectionUtil.findImplementationsOfInterface(eq(PropertyConfig.class), anyString())
//            ).thenReturn(List.of(DummyConfigWithoutKey.class));
//
//            // Create the DummyConfigWithoutKey instance and mock ConfigCache.getOrCreate
//            DummyConfigWithoutKey dummyConfigWithoutKey = new DummyConfigWithoutKey();
//            mockedConfigCache.when(() -> ConfigCache.getOrCreate(DummyConfigWithoutKey.class)).thenReturn(dummyConfigWithoutKey);
//
//            // Call the method
//            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
//            method.setAccessible(true);
//
//            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);
//
//            // Assert the result and verify that methods without @Config.Key are skipped
//            assertNotNull(result);
//            assertTrue(result.containsKey("valid.key"));
//            assertFalse(result.containsKey("invalidMethod"));  // This should be skipped because it has no @Config.Key
//        }
//    }

    @ConfigSource("testSource")
    public static class DummyConfigWithoutKey implements PropertyConfig {
        // Method with @Config.Key annotation
        @Config.Key("valid.key")
        public String validMethod() {
            return "valid value";
        }

        // Method without @Config.Key annotation (This is what we're testing for)
        public String invalidMethod() {
            return "invalid value";
        }
    }

    @ConfigSource("dummy")
    public static class DummyPropertyConfig implements PropertyConfig {
        public interface DummyInner extends PropertyConfig {
            @Config.Key("dummy.key")
            String dummyValue();
        }

        public String dummyValue() {
            return "dummyValue";
        }
    }

    @ConfigSource("testSource")
    public static class DummyConfig implements DummyConfigInterface {
        @Override
        public String testMethod() {
            return "value123";
        }
    }

    public interface DummyConfigInterface extends PropertyConfig {
        @Config.Key("test.key")
        String testMethod();
    }

    public static class DummyEmptyConfig implements PropertyConfig, DummyEmptyConfigInterface {
        @Override
        public String testMethod() {
            return "";
        }
    }

    public interface DummyEmptyConfigInterface {
        @Config.Key("empty.key")
        String testMethod();
    }

    @ConfigSource(value = "testSource")
    public class DummyThrowsConfig implements PropertyConfig {

        @Config.Key("exception.key")
        public String getExceptionKey() throws Exception {
            throw new Exception("Simulating an exception during method invocation");
        }

        // other methods as needed...
    }

    public interface DummyThrowsInterface extends PropertyConfig {

        @Config.Key("exception.key")
        String getExceptionKey() throws Exception;
    }

//    @Retention(RetentionPolicy.RUNTIME)
//    public @interface Config {
//        @Retention(RetentionPolicy.RUNTIME)
//        public @interface Key {
//            String value();
//        }
//    }
}
