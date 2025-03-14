package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.log.LogTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;

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
        String htmlTemplate = ResourceLoader.loadHtmlTemplate("allure/html/test-details.html");

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

    /**
     * Functional interface for supplying values within an Allure step execution context.
     * <p>
     * This interface is primarily used for deferred execution of test data retrieval
     * or parameter resolution within an Allure step. Implementations can throw
     * a {@link ParameterResolutionException} when unable to resolve the required parameter.
     * </p>
     *
     * @param <T> The type of the value supplied by this function.
     */
    @FunctionalInterface
    public interface AllureStepSupplier<T> {

        /**
         * Retrieves a value within an Allure step execution.
         * <p>
         * Implementations should handle any necessary logic for retrieving test-related
         * data, including parameter resolution for dynamic test inputs.
         * </p>
         *
         * @return The supplied value of type {@code T}.
         * @throws ParameterResolutionException If the value cannot be resolved.
         */
        T get() throws ParameterResolutionException;
    }
}