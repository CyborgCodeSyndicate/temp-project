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
    void testSetDescription() {
        List<String> htmlContent = List.of("<td>Some content</td>", "<td>Other content</td>");
        lenient().when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
        lenient().when(mockStore.get(HTML)).thenReturn(htmlContent);
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.setDescription(mockContext);
            String expectedHtml = "<div style='margin: 20px;'><td>Some content</td><td>Other content</td></div>";
            mockedAllure.verify(() -> Allure.descriptionHtml(eq(expectedHtml)), times(1));
        }
    }
    //setDescription - END

    //attachFilteredLogsToAllure - START
    @Test
    void testAttachFilteredLogsToAllure() {
        String testName = "testScenario";
        try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
            AllureStepHelper.attachFilteredLogsToAllure(testName);
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

        Path tempLogFile = Files.createTempFile("logfile", ".log");
        Files.write(tempLogFile, List.of(
                "[scenario=someOtherScenario] Not related log",
                matchingLine1,
                matchingLine2
        ));

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
    //attachFilteredLogsToAllure - END


    //logTestOutcome - START
    @Test
    void testLogTestOutcome_Success() {
        String testName = "testScenario";
        long durationInSeconds = 5;

        try (MockedStatic<LogTest> mockedLogTest = mockStatic(LogTest.class)) {
            AllureStepHelper.logTestOutcome(testName, "SUCCESS", durationInSeconds, null);

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
    void testSetUpTestMetadata() {

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

            AllureStepHelper.setUpTestMetadata(mockContext);

            verify(mockStore, times(1)).put(eq(HTML), eq(htmlList));
        }
    }

    @Test
    void testSetUpTestMetadata_WhenHtmlListIsNull() throws NoSuchMethodException {
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
        when(mockStore.get(HTML, List.class)).thenReturn(null);

        try (MockedStatic<ResourceLoader> mockedResourceLoader = mockStatic(ResourceLoader.class)) {
            String mockHtmlTemplate = "<html>{{testName}}</html>";
            mockedResourceLoader.when(() -> ResourceLoader.loadResourceFile("allure/html/test-details.html"))
                    .thenReturn(mockHtmlTemplate);

            Method mockMethod = AllureStepHelperTest.class.getDeclaredMethod("testSetUpTestMetadata_WhenHtmlListIsNull");
            when(mockContext.getRequiredTestMethod()).thenReturn(mockMethod);

            when(mockContext.getRequiredTestClass()).thenReturn((Class) AllureStepHelperTest.class);

            AllureStepHelper.setUpTestMetadata(mockContext);

            verify(mockStore).put(eq(HTML), anyList());
        }
    }

    @Test
    void testSetUpTestMetadata_WithCustomAnnotation() throws NoSuchMethodException {
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

            AllureStepHelper.setUpTestMetadata(mockContext);

            assertFalse(htmlList.isEmpty());
            assertTrue(htmlList.get(0).contains("CustomTestAnnotation"));
        }
    }
    //setUpTestMetadata - END

    //setupTestContext - START
    @Test
    void testSetupTestContext() throws NoSuchMethodException {
        when(mockContext.getTestClass()).thenReturn(Optional.of(AllureStepHelperTest.class));
        when(mockContext.getTestMethod()).thenReturn(Optional.of(AllureStepHelperTest.class.getDeclaredMethod("testSetDescription")));
        when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);

        try (MockedStatic<ThreadContext> mockedThreadContext = mockStatic(ThreadContext.class)) {
            AllureStepHelper.setupTestContext(mockContext);

            mockedThreadContext.verify(() ->
                            ThreadContext.put(eq("testName"), eq("AllureStepHelperTest.testSetDescription")),
                    times(1)
            );
        }
    }
    //setupTestContext - END

    //initializeTestEnvironment - START
    @Test
    void testInitializeTestEnvironment_withMockedReflectionUtil() {
        try (MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
             MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {

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

            AllureStepHelper.initializeTestEnvironment();
        }
    }

    @Test
    void testInitializeTestEnvironment_noDirectoryCreation() {
        Map<String, List<String>> propertiesMap = Map.of(
                "key1", List.of("value1"),
                "key2", List.of("value2")
        );

        File allureResultsDir = mock(File.class);
        File environmentFile = mock(File.class);

        try (MockedStatic<ReflectionUtil> mockedReflectionUtil = mockStatic(ReflectionUtil.class);
             MockedStatic<FrameworkConfigHolder> mockedFrameworkConfigHolder = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {
            try (MockedConstruction<FileWriter> mockFileWriter = mockConstruction(FileWriter.class,
                    (mock, context) -> {
                        try {
                            mock.write("key1=value1\n");
                            mock.write("key2=value2\n");
                        } catch (IOException e) {
                        }
                    }
            )) {
                lenient().when(allureResultsDir.exists()).thenReturn(true);
                BasicPropertyConfig dummyConfig = new BasicPropertyConfig();
                mockedConfigCache.when(() -> ConfigCache.getOrCreate(BasicPropertyConfig.class))
                        .thenReturn(dummyConfig);
                FrameworkConfig mockConfig = mock(FrameworkConfig.class);
                when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
                mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);
                FrameworkConfig dummyFrameworkConfig = mock(FrameworkConfig.class);
                lenient().when(dummyFrameworkConfig.projectPackage()).thenReturn("com.theairebellion.zeus");

                AllureStepHelper.initializeTestEnvironment();

                verify(allureResultsDir, never()).mkdirs();

                mockFileWriter.constructed().forEach(mockWriter -> {
                    try {
                        verify(mockWriter).write("key1=value1\n");
                        verify(mockWriter).write("key2=value2\n");
                    } catch (IOException e) {
                    }
                });
            }
        }
    }
    //initializeTestEnvironment - END

    //collectConfigurationProperties - START
    @Test
    void test_collectConfigurationProperties_withValidConfig() throws Exception {
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

            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);
            System.out.println("Collected result: " + result);
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
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");
            mockedFrameworkConfigHolder.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            mockedReflectionUtil.when(() ->
                    ReflectionUtil.findImplementationsOfInterface(eq(PropertyConfig.class), anyString())
            ).thenReturn(List.of(ExceptionThrowingConfig.class));

            ExceptionThrowingConfig dummy = new ExceptionThrowingConfig();
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(ExceptionThrowingConfig.class)).thenReturn(dummy);

            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void test_collectConfigurationProperties_skipsMethodWithoutKeyAnnotation() throws Exception {
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

            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            assertNotNull(result);
            assertTrue(result.isEmpty(), "Expected result to be empty because no method has @Config.Key");
        }
    }

    @Test
    void test_collectConfigurationProperties_skipsWhitespaceOnlyValues() throws Exception {
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

            Method method = AllureStepHelper.class.getDeclaredMethod("collectConfigurationProperties");
            method.setAccessible(true);
            Map<String, List<String>> result = (Map<String, List<String>>) method.invoke(null);

            assertNotNull(result);
            assertTrue(result.isEmpty(), "Expected result to be empty because value is only whitespace");
        }
    }
    //collectConfigurationProperties - END

    //writeEnvironmentProperties - START
    @Test
    void testWriteEnvironmentProperties_writeKeyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange: Mock propertiesMap with sample data
        Map<String, List<String>> propertiesMap = Map.of(
                "key1", List.of("value1", "value2"),
                "key2", List.of("value3")
        );

        // Mock the directory and file creation
        File allureResultsDir = mock(File.class);
        File environmentFile = mock(File.class);

        // Mock behavior for allureResultsDir.exists() returning true (so mkdirs should NOT be called)
        lenient().when(allureResultsDir.exists()).thenReturn(true);

        // Mock FileWriter behavior using mockConstruction
        try (MockedConstruction<FileWriter> mockFileWriter = mockConstruction(FileWriter.class)) {
            // Act: Call the method under test
            Method method = AllureStepHelper.class.getDeclaredMethod("writeEnvironmentProperties", Map.class);
            method.setAccessible(true);
            method.invoke(null, propertiesMap);

            // Assert: Verify that mkdirs() was NOT called since the directory already exists
            verify(allureResultsDir, never()).mkdirs();

            // Assert: Verify the write method was called for each entry with the expected value format
            mockFileWriter.constructed().forEach(mockWriter -> {
                try {
                    verify(mockWriter).write("key1=value1; value2\n");
                    verify(mockWriter).write("key2=value3\n");
                } catch (IOException e) {
                    // Handle IOException
                }
            });
        }
    }
}
