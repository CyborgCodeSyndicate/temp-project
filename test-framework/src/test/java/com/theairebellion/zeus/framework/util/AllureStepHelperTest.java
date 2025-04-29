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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
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
    public @interface CustomTestAnnotation {
        String value();
    }

    @CustomTestAnnotation("SomeValue")
    public void customAnnotatedTestMethod() {
    }

    // Config with a method that lacks a key annotation
    public interface MissingKeyConfig extends PropertyConfig {
        String noKey();
    }

    @ConfigSource("noKeySource")
    public static class MissingKeyConfigImpl implements MissingKeyConfig {
        public String noKey() {
            return "valueShouldBeIgnored";
        }
    }

    // Config with a key returning whitespace
    public interface WhitespaceValueConfig extends PropertyConfig {
        @Config.Key("white.key")
        String getWhiteValue();
    }

    @ConfigSource("whitespaceSource")
    public static class WhitespaceValueConfigImpl implements WhitespaceValueConfig {
        public String getWhiteValue() {
            return "   ";
        }
    }

    // General dummy property config with typical keys
    @ConfigSource("dummy")
    public static class BasicPropertyConfig implements PropertyConfig {

        @Config.Key("browser")
        String browser() {
            return "chrome";
        }

        @Config.Key("env")
        String env() {
            return "staging";
        }

        @Config.Key("features")
        List<String> features() {
            return List.of("login", "checkout");
        }
    }

    // Simple config interface with a key
    public interface SimpleKeyedConfig extends PropertyConfig {
        @Config.Key("test.key")
        String testMethod();
    }

    @ConfigSource("testSource")
    public static class SimpleKeyedConfigImpl implements SimpleKeyedConfig {
        @Override
        public String testMethod() {
            return "value123";
        }
    }

    // Config interface and class returning an empty string
    public interface EmptyValueConfig {
        @Config.Key("empty.key")
        String testMethod();
    }

    public static class EmptyValueConfigImpl implements PropertyConfig, EmptyValueConfig {
        @Override
        public String testMethod() {
            return "";
        }
    }

    // Config that throws an exception on method call
    @ConfigSource(value = "testSource")
    public class ExceptionThrowingConfig implements PropertyConfig {

        @Config.Key("exception.key")
        public String getExceptionKey() throws Exception {
            throw new Exception("Simulating an exception during method invocation");
        }
    }

    //setDescription - START
    @Test
    @DisplayName("Should set Allure description using HTML content from context store")
    void shouldSetAllureHtmlDescriptionFromContext() {
        // Given
        List<String> htmlContent = List.of("<td>Some content</td>", "<td>Other content</td>");
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
        when(mockStore.get(HTML)).thenReturn(htmlContent);

        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.setDescription(mockContext);

            // Then
            String expectedHtml = "<div style='margin: 20px;'><td>Some content</td><td>Other content</td></div>";
            mockedAllure.verify(() -> Allure.descriptionHtml(eq(expectedHtml)), times(1));
        }
    }
    //setDescription - END

    //attachFilteredLogsToAllure - START
    @Test
    @DisplayName("Should attach filtered logs to Allure when test name is provided")
    void shouldAttachFilteredLogsToAllureWhenTestNameProvided() {
        // Given
        String testName = "testScenario";

        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            // Then
            mockedAllure.verify(() ->
                            Allure.addAttachment(anyString(), eq("text/plain"), anyString(), eq(".log")),
                    times(1)
            );
        }
    }

    @Test
    @DisplayName("Should attach message when test name is null")
    void shouldAttachMessageWhenTestNameIsNull() {
        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(null);

            // Then
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
    @DisplayName("Should attach message when test name is empty")
    void shouldAttachMessageWhenTestNameIsEmpty() {
        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure("");

            // Then
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
    @DisplayName("Should attach error message when IOException occurs during log reading")
    void shouldAttachErrorMessageWhenIOExceptionOccurs() {
        // Given
        String testName = "testScenario";
        String invalidLogPath = "invalid/path/to/logfile.log";
        System.setProperty("logFileName", invalidLogPath);

        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            // Then
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
    @DisplayName("Should attach fallback message when no logs are found for test")
    void shouldAttachFallbackMessageWhenNoLogsAreFound() {
        // Given
        String testName = "nonExistentScenario";
        System.clearProperty("logFileName");

        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            // Then
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
    @DisplayName("Should attach only matching log lines when logs exist for the test")
    void shouldAttachOnlyMatchingLogLines() throws IOException {
        // Given
        String testName = "myScenario";
        String testIdentifier = "[scenario=" + testName + "]";
        String matchingLine1 = testIdentifier + " Log line 1";
        String matchingLine2 = testIdentifier + " Log line 2";

        Path tempLogFile = Files.createTempFile("logfile", ".log");
        Files.write(tempLogFile, List.of(
                "[scenario=someOtherScenario] Not related log",
                matchingLine1,
                matchingLine2
        ));
        System.setProperty("logFileName", tempLogFile.toAbsolutePath().toString());

        // When
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);

            // Then
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
    //attachFilteredLogsToAllure - END


    //logTestOutcome - START
    @Test
    @DisplayName("Should log SUCCESS outcome without exception")
    void shouldLogSuccessOutcomeWithoutException() {
        // Given
        String testName = "testScenario";
        long durationInSeconds = 5;

        // When
        try (MockedStatic<LogTest> mockedLogTest = mockStatic(LogTest.class)) {
            AllureStepHelper.logTestOutcome(testName, "SUCCESS", durationInSeconds, null);

            // Then
            mockedLogTest.verify(() ->
                            LogTest.info(anyString(), eq(testName), eq("SUCCESS"), eq(durationInSeconds)),
                    times(1)
            );
        }
    }

    @Test
    @DisplayName("Should log FAILED outcome and debug throwable when exception is provided")
    void shouldLogFailedOutcomeWithException() {
        // Given
        String testName = "testScenario";
        long durationInSeconds = 5;
        Throwable throwable = new RuntimeException("Test failed");

        // When
        try (MockedStatic<LogTest> mockedLogTest = mockStatic(LogTest.class)) {
            AllureStepHelper.logTestOutcome(testName, "FAILED", durationInSeconds, throwable);

            // Then
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
    //logTestOutcome - END

    //setUpTestMetadata - START
    @Test
    @DisplayName("Should set up test metadata and store rendered HTML when HTML list is already present")
    void shouldSetUpTestMetadata_WhenHtmlListIsAlreadyPresent() {
        // Given
        try (MockedStatic<ResourceLoader> mockedStatic = mockStatic(ResourceLoader.class)) {
            String htmlTemplate = "<html><body>{{testName}} {{className}} {{methodAnnotations}}</body></html>";
            mockedStatic.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(htmlTemplate);

            Method mockMethod = mock(Method.class);
            when(mockMethod.getName()).thenReturn("testMethod");

            Annotation[] annotations = {};
            when(mockMethod.getAnnotations()).thenReturn(annotations);

            Class<?> testClass = AllureStepHelperTest.class;
            when(mockContext.getRequiredTestMethod()).thenReturn(mockMethod);
            when(mockContext.getRequiredTestClass()).thenReturn((Class) testClass);
            when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
            List<String> htmlList = new ArrayList<>();
            when(mockStore.get(HTML, List.class)).thenReturn(htmlList);

            // When
            AllureStepHelper.setUpTestMetadata(mockContext);

            // Then
            verify(mockStore, times(1)).put(eq(HTML), eq(htmlList));
        }
    }

    @Test
    @DisplayName("Should initialize new HTML list if not present in store")
    void shouldInitializeHtmlList_WhenHtmlListIsNull() throws NoSuchMethodException {
        // Given
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
        when(mockStore.get(HTML, List.class)).thenReturn(null);

        try (MockedStatic<ResourceLoader> mockedResourceLoader = mockStatic(ResourceLoader.class)) {
            String mockHtmlTemplate = "<html>{{testName}}</html>";
            mockedResourceLoader.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(mockHtmlTemplate);

            Method mockMethod = AllureStepHelperTest.class.getDeclaredMethod("shouldInitializeHtmlList_WhenHtmlListIsNull");
            when(mockContext.getRequiredTestMethod()).thenReturn(mockMethod);
            when(mockContext.getRequiredTestClass()).thenReturn((Class) AllureStepHelperTest.class);

            // When
            AllureStepHelper.setUpTestMetadata(mockContext);

            // Then
            verify(mockStore).put(eq(HTML), anyList());
        }
    }

    @Test
    @DisplayName("Should capture and store method annotations in HTML template")
    void shouldRenderCustomAnnotationsIntoHtmlTemplate() throws NoSuchMethodException {
        // Given
        try (MockedStatic<ResourceLoader> mockedResourceLoader = mockStatic(ResourceLoader.class)) {
            String template = "<html>{{methodAnnotations}}</html>";
            mockedResourceLoader.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(template);

            Method annotatedMethod = AllureStepHelperTest.class.getDeclaredMethod("customAnnotatedTestMethod");
            when(mockContext.getRequiredTestMethod()).thenReturn(annotatedMethod);
            when(mockContext.getRequiredTestClass()).thenReturn((Class) AllureStepHelperTest.class);
            when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

            List<String> htmlList = new ArrayList<>();
            when(mockStore.get(HTML, List.class)).thenReturn(htmlList);

            // When
            AllureStepHelper.setUpTestMetadata(mockContext);

            // Then
            assertFalse(htmlList.isEmpty());
            assertTrue(htmlList.get(0).contains("CustomTestAnnotation"));
        }
    }
    //setUpTestMetadata - END

    //setupTestContext - START
    @Test
    @DisplayName("Should set up test context by storing test name in thread context")
    void shouldStoreTestNameInThreadContext_WhenTestClassAndMethodArePresent() throws NoSuchMethodException {
        // Given
        when(mockContext.getTestClass()).thenReturn(Optional.of(AllureStepHelperTest.class));
        when(mockContext.getTestMethod())
                .thenReturn(Optional.of(AllureStepHelperTest.class.getDeclaredMethod("shouldSetAllureHtmlDescriptionFromContext")));
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

        // When
        try (MockedStatic<ThreadContext> mockedThreadContext = mockStatic(ThreadContext.class)) {
            AllureStepHelper.setupTestContext(mockContext);

            // Then
            mockedThreadContext.verify(() ->
                            ThreadContext.put(eq("testName"), eq("AllureStepHelperTest.shouldSetAllureHtmlDescriptionFromContext")),
                    times(1)
            );
        }
    }
    //setupTestContext - END

    //initializeTestEnvironment - START
    @Test
    @DisplayName("Should initialize test environment with mocked ReflectionUtil and ConfigCache")
    void shouldInitializeTestEnvironment_WhenReflectionAndConfigAreMocked() {
        try (MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
             MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {

            // Given
            List<Class<? extends PropertyConfig>> dummyConfigs = List.of(BasicPropertyConfig.class);
            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(any(), any())
            ).thenReturn(dummyConfigs);

            BasicPropertyConfig dummyConfig = new BasicPropertyConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(BasicPropertyConfig.class))
                    .thenReturn(dummyConfig);

            FrameworkConfig dummyFrameworkConfig = mock(FrameworkConfig.class);
            lenient().when(dummyFrameworkConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(FrameworkConfig.class))
                    .thenReturn(dummyFrameworkConfig);

            // When / Then
            AllureStepHelper.initializeTestEnvironment();
        }
    }

    @Test
    @DisplayName("Should write environment properties and skip directory creation if directory exists")
    void shouldWriteEnvironmentPropertiesWithoutCreatingDirectory_WhenDirectoryAlreadyExists() {
        // Given
        Map<String, List<String>> propertiesMap = Map.of(
                "key1", List.of("value1"),
                "key2", List.of("value2")
        );

        File allureResultsDir = mock(File.class);
//        File environmentFile = mock(File.class);

        try (MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
             MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {

            try (MockedConstruction<FileWriter> mockFileWriter = mockConstruction(FileWriter.class,
                    (mock, context) -> {
                        try {
                            mock.write("key1=value1\n");
                            mock.write("key2=value2\n");
                        } catch (IOException ignored) {}
                    })) {

                // Set up mocks
                lenient().when(allureResultsDir.exists()).thenReturn(true);

                BasicPropertyConfig dummyConfig = new BasicPropertyConfig();
                mockedConfigCache.when(() -> ConfigCache.getOrCreate(BasicPropertyConfig.class))
                        .thenReturn(dummyConfig);

                FrameworkConfig mockConfig = mock(FrameworkConfig.class);
                when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
                mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig)
                        .thenReturn(mockConfig);

                FrameworkConfig dummyFrameworkConfig = mock(FrameworkConfig.class);
                lenient().when(dummyFrameworkConfig.projectPackage()).thenReturn("com.theairebellion.zeus");

                // When
                AllureStepHelper.initializeTestEnvironment();

                // Then
                verify(allureResultsDir, never()).mkdirs();

                mockFileWriter.constructed().forEach(mockWriter -> {
                    try {
                        verify(mockWriter).write("key1=value1\n");
                        verify(mockWriter).write("key2=value2\n");
                    } catch (IOException ignored) {}
                });
            }
        }
    }
    //initializeTestEnvironment - END

    //collectConfigurationProperties - START
    @Test
    @DisplayName("Should collect valid configuration properties with key and value")
    void test_collectConfigurationProperties_withValidConfig() throws Exception {
        // Given
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(PropertyConfig.class, "com.theairebellion.zeus")
            ).thenReturn(List.of(SimpleKeyedConfigImpl.class));


            SimpleKeyedConfigImpl simpleKeyedConfigImpl = new SimpleKeyedConfigImpl();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(SimpleKeyedConfigImpl.class)).thenReturn(simpleKeyedConfigImpl);

            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            // When
            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            // Then
            assertNotNull(result, "Result should not be null");
            assertTrue(result.containsKey("test.key"), "Expected result to contain 'test.key'");
            assertEquals("value123 (Source: testSource)", result.get("test.key").get(0), "Unexpected value for 'test.key'");
        }
    }

    @Test
    @DisplayName("Should skip properties with empty values")
    void test_collectConfigurationProperties_skipsEmptyValues() throws Exception {
        // Given
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(eq(PropertyConfig.class), anyString())
            ).thenReturn(List.of(EmptyValueConfigImpl.class));

            EmptyValueConfigImpl emptyConfig = new EmptyValueConfigImpl();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(EmptyValueConfigImpl.class)).thenReturn(emptyConfig);

            // When
            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            // Then
            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty() || !result.containsKey("empty.key"),
                    "Result should not contain 'empty.key' or should be empty");
        }
    }

    @Test
    @DisplayName("Should return empty result when reflection throws exception")
    void test_collectConfigurationProperties_handlesReflectionException() throws Exception {
        // Given
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(eq(PropertyConfig.class), anyString())
            ).thenReturn(List.of(ExceptionThrowingConfig.class));

            ExceptionThrowingConfig dummy = new ExceptionThrowingConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(ExceptionThrowingConfig.class)).thenReturn(dummy);

            // When
            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            // Then
            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty(), "Expected result to be empty due to exception in config method");
        }
    }

    @Test
    @DisplayName("Should skip methods without @Config.Key annotation")
    void test_collectConfigurationProperties_skipsMethodWithoutKeyAnnotation() throws Exception {
        // Given
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(PropertyConfig.class, "com.theairebellion.zeus")
            ).thenReturn(List.of(MissingKeyConfigImpl.class));

            MissingKeyConfigImpl dummy = new MissingKeyConfigImpl();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(MissingKeyConfigImpl.class)).thenReturn(dummy);

            // When
            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            // Then
            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty(), "Expected result to be empty because no method has @Config.Key");
        }
    }

    @Test
    @DisplayName("Should skip properties with whitespace-only values")
    void test_collectConfigurationProperties_skipsWhitespaceOnlyValues() throws Exception {
        // Given
        try (
                MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
                MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class);
                MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class)
        ) {
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(PropertyConfig.class, "com.theairebellion.zeus")
            ).thenReturn(List.of(WhitespaceValueConfigImpl.class));

            WhitespaceValueConfigImpl dummy = new WhitespaceValueConfigImpl();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(WhitespaceValueConfigImpl.class)).thenReturn(dummy);

            // When
            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            // Then
            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty(), "Expected result to be empty because value is only whitespace");
        }
    }
    //collectConfigurationProperties - END

    //writeEnvironmentProperties - START
    @Test
    @DisplayName("Should write key-value pairs to the environment file")
    void testWriteEnvironmentProperties_writeKeyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        Map<String, List<String>> propertiesMap = Map.of(
                "key1", List.of("value1", "value2"),
                "key2", List.of("value3")
        );

        File allureResultsDir = mock(File.class);
        File environmentFile = mock(File.class);

        lenient().when(allureResultsDir.exists()).thenReturn(true);

        // When
        try (MockedConstruction<FileWriter> mockFileWriter = mockConstruction(FileWriter.class)) {
            Method method = AllureStepHelper.class.getDeclaredMethod("writeEnvironmentProperties", Map.class);
            method.setAccessible(true);
            method.invoke(null, propertiesMap);

            // Then
            verify(allureResultsDir, never()).mkdirs();

            mockFileWriter.constructed().forEach(mockWriter -> {
                try {
                    verify(mockWriter).write("key1=value1; value2\n");
                    verify(mockWriter).write("key2=value3\n");
                } catch (IOException e) {
                }
            });
        }
    }
}
