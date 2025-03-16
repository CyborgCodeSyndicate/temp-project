package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;
import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static com.theairebellion.zeus.framework.util.ResourceLoader.loadResourceFile;

/**
 * Utility class for managing test metadata and attaching logs to Allure reports.
 * <p>
 * This class provides helper methods to:
 * <ul>
 *     <li>Set HTML descriptions in Allure reports</li>
 *     <li>Attach filtered logs based on test execution</li>
 *     <li>Log test outcomes with structured messages</li>
 *     <li>Generate and inject metadata into test reports</li>
 * </ul>
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class AllureStepHelper extends ObjectFormatter {

    private static final String ALLURE_RESULTS_DIR = "allure-results";
    private static final String ENVIRONMENT_PROPERTIES_FILE = "environment.properties";
    private static final String CATEGORIES_JSON_PATH = "allure/json/categories.json";
    private static final String FRAMEWORK_PACKAGE = "com.theairebellion.zeus";
    private static final String CATEGORIES_JSON = "categories.json";

    /**
     * Sets an HTML description for the test execution in Allure reports.
     * <p>
     * This method retrieves the HTML content stored under the {@code HTML} key
     * in {@code ExtensionContext.Namespace.GLOBAL}, filters for valid table-based
     * content, and appends it as a formatted description in Allure.
     * </p>
     *
     * @param context The test execution context.
     */
    public static void setDescription(ExtensionContext context) {
        List<String> htmlContent = (List<String>) context.getStore(ExtensionContext.Namespace.GLOBAL).get(HTML);

        String combinedHtml = htmlContent.stream()
                .filter(Objects::nonNull)
                .filter(table -> table.contains("<td>"))
                .collect(Collectors.joining("", "<div style='margin: 20px;'>", "</div>"));

        Allure.descriptionHtml(combinedHtml);
    }

    /**
     * Attaches filtered logs to Allure based on the test name.
     * <p>
     * This method scans the system log file for log entries containing the test
     * scenario identifier. If no logs are found, a fallback message is attached.
     * </p>
     *
     * @param testName The name of the test scenario to filter logs for.
     */
    public static void attachFilteredLogsToAllure(String testName) {
        if (testName == null || testName.isEmpty()) {
            Allure.addAttachment("Filtered Logs", "text/plain", "Test name is not available.", ".log");
            return;
        }

        String logFilePath = System.getProperty("logFileName", "logs/zeus.log");
        String testIdentifier = "[scenario=" + testName + "]";

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String filteredLogs = reader.lines()
                    .filter(line -> line.contains(testIdentifier))
                    .collect(Collectors.joining(System.lineSeparator()));

            String attachmentContent = filteredLogs.isEmpty()
                    ? "No logs found for test: " + testName
                    : filteredLogs;

            Allure.addAttachment("Filtered Logs for Test: " + testName, "text/plain", attachmentContent, ".log");
        } catch (IOException e) {
            Allure.addAttachment("Filtered Logs for Test: " + testName, "text/plain",
                    "Failed to read logs. Error: " + e.getMessage(), ".log");
        }
    }

    /**
     * Logs the test outcome after execution.
     * <p>
     * This method logs whether the test concluded successfully or failed,
     * along with the duration of execution. If the test failed, additional
     * debugging details are logged.
     * </p>
     *
     * @param testName         The name of the test that was executed.
     * @param status           The test execution status ("SUCCESS" or "FAILED").
     * @param durationInSeconds The duration of the test execution in seconds.
     * @param throwable        The exception thrown (if any) during execution.
     */
    public static void logTestOutcome(String testName, String status, long durationInSeconds, Throwable throwable) {
        String logMessage = "The quest of '{}' has " +
                (throwable == null ? "concluded with glory" : "ended in defeat") +
                ". Status: {}. Duration: {} seconds.";

        LogTest.info(logMessage, testName, status, durationInSeconds);
        if (throwable != null) {
            LogTest.debug("Failure reason:", throwable);
        }
    }

    /**
     * Sets up and formats test metadata for Allure reports.
     * <p>
     * This method loads a predefined HTML template and dynamically populates it with:
     * <ul>
     *     <li>Test name and class details</li>
     *     <li>Annotations applied at the class and method levels</li>
     *     <li>Test argument values</li>
     * </ul>
     * The formatted HTML is then stored under the {@code HTML} key in {@code ExtensionContext.Namespace.GLOBAL}.
     * </p>
     *
     * @param context The test execution context.
     */
    public static void setUpTestMetadata(ExtensionContext context) {
        String htmlTemplate = ResourceLoader.loadResourceFile("allure/html/test-details.html");

        Map<String, String> placeholders = Map.of(
                "{{testName}}", escapeHtml(context.getRequiredTestMethod().getName()),
                "{{className}}", escapeHtml(context.getRequiredTestClass().getSimpleName()),
                "{{classAnnotations}}", formatAnnotationsToNewRows(escapeHtml(getClassAnnotations(context))),
                "{{methodAnnotations}}", formatAnnotationsToNewRows(escapeHtml(getMethodAnnotations(context))),
                "{{testArguments}}", formatLongText(escapeHtml(getTestArguments(context)))
        );

        String formattedHtml = placeholders.entrySet().stream()
                .reduce(htmlTemplate, (html, entry) -> html.replace(entry.getKey(), entry.getValue()), (a, b) -> a);
        List<String> htmlList = context.getStore(ExtensionContext.Namespace.GLOBAL).get(HTML, List.class);
        if (htmlList == null) {
            htmlList = new ArrayList<>();
        }
        htmlList.add(formattedHtml);
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(HTML, htmlList);
    }

    public static void initializeTestEnvironment() {
        Map<String, String> propertiesMap = collectConfigurationProperties();
        writeEnvironmentProperties(propertiesMap);
        writeCategoriesJson();
    }

    private static Map<String, String> collectConfigurationProperties() {
        List<Class<? extends PropertyConfig>> allConfig = findAllPropertyConfigImplementations();

        return allConfig.stream()
                .flatMap(aClass -> {
                    ConfigSource configSource = aClass.getAnnotation(ConfigSource.class);
                    String configSourceValue = (configSource != null) ? configSource.value() : "unknown";

                    PropertyConfig propertyConfig = ConfigCache.getOrCreate(aClass);
                    return Arrays.stream(propertyConfig.getClass().getInterfaces())
                            .filter(PropertyConfig.class::isAssignableFrom)
                            .flatMap(intf -> Arrays.stream(intf.getDeclaredMethods()))
                            .filter(method -> method.getAnnotation(Config.Key.class) != null)
                            .map(method -> {
                                try {
                                    Config.Key annotation = method.getAnnotation(Config.Key.class);
                                    String key = annotation.value();
                                    Object value = method.invoke(propertyConfig);

                                    if (value == null || value.toString().trim().isEmpty()) {
                                        System.out.println("Skipping key '" + key + "' because value is empty or null.");
                                        return null;
                                    }

                                    return new AbstractMap.SimpleEntry<>(key, value + " (Source: " + configSourceValue + ")");
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    System.err.println("Error invoking method: " + method.getName());
                                    e.printStackTrace();
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<Class<? extends PropertyConfig>> findAllPropertyConfigImplementations() {
        List<Class<? extends PropertyConfig>> implementationsOfInterfaceInFramework = ReflectionUtil.findImplementationsOfInterface(
                PropertyConfig.class, FRAMEWORK_PACKAGE);
        List<Class<? extends PropertyConfig>> implementationsOfInterfaceInProject = ReflectionUtil.findImplementationsOfInterface(
                PropertyConfig.class, getFrameworkConfig().projectPackage());

        List<Class<? extends PropertyConfig>> allConfig = new ArrayList<>();
        allConfig.addAll(implementationsOfInterfaceInFramework);
        allConfig.addAll(implementationsOfInterfaceInProject);
        return allConfig;
    }

    private static void writeEnvironmentProperties(Map<String, String> propertiesMap) {
        File allureResultsDir = new File(ALLURE_RESULTS_DIR);
        if (!allureResultsDir.exists()) {
            allureResultsDir.mkdirs();
        }

        File environmentFile = new File(allureResultsDir, ENVIRONMENT_PROPERTIES_FILE);
        try (FileWriter writer = new FileWriter(environmentFile)) {
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write environment.properties file", e);
        }
    }

    private static void writeCategoriesJson() {
        String categoriesJson = loadResourceFile(CATEGORIES_JSON_PATH);

        File allureResultsDir = new File(ALLURE_RESULTS_DIR);
        if (!allureResultsDir.exists()) {
            allureResultsDir.mkdirs();
        }

        File categoriesFile = new File(allureResultsDir, CATEGORIES_JSON);
        try (FileWriter writer = new FileWriter(categoriesFile)) {
            writer.write(categoriesJson);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write categories.json file", e);
        }
    }

    public static void setupTestContext(ExtensionContext context) {
        String className = context.getTestClass()
                .map(Class::getSimpleName)
                .orElse("UnknownClass");
        String methodName = context.getTestMethod()
                .map(Method::getName)
                .orElse("UnknownMethod");

        ThreadContext.put("testName", className + "." + methodName);
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(START_TIME, System.currentTimeMillis());
    }

}