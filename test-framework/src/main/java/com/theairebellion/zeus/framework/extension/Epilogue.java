package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;

/**
 * JUnit 5 {@code AfterTestExecutionCallback} extension for logging test outcomes and attaching logs to Allure reports.
 * <p>
 * This extension executes after each test method, logging the test result (success or failure),
 * calculating execution duration, and appending filtered logs to the Allure report.
 * </p>
 *
 * <p>
 * Logs are filtered based on the test name and extracted from the system log file.
 * If logs cannot be retrieved, a fallback message is added to Allure.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Order(Integer.MAX_VALUE)
public class Epilogue implements AfterTestExecutionCallback {

    /**
     * Executes after test execution to log results and attach filtered logs to Allure.
     *
     * @param context The test execution context.
     */
    @Override
    public void afterTestExecution(final ExtensionContext context) {
        Throwable throwable = context.getExecutionException().orElse(null);
        String status = (throwable == null) ? "SUCCESS" : "FAILED";

        long startTime = context.getStore(ExtensionContext.Namespace.GLOBAL).get(START_TIME, long.class);
        long durationInSeconds = (System.currentTimeMillis() - startTime) / 1000;

        logTestOutcome(context.getDisplayName(), status, durationInSeconds, throwable);

        attachFilteredLogsToAllure(ThreadContext.get("testName"));
        ThreadContext.remove("testName");
    }

    /**
     * Logs the outcome of a test execution.
     *
     * @param testName          The name of the executed test.
     * @param status            The test result status (SUCCESS or FAILED).
     * @param durationInSeconds The execution duration in seconds.
     * @param throwable         The exception thrown if the test failed, otherwise {@code null}.
     */
    private void logTestOutcome(String testName, String status, long durationInSeconds, Throwable throwable) {
        if (throwable == null) {
            LogTest.info("The quest of '{}' has concluded with glory. Status: {}. Duration: {} seconds.",
                testName, status, durationInSeconds);
        } else {
            LogTest.info("The quest of '{}' has ended in defeat. Status: {}. Duration: {} seconds.",
                testName, status, durationInSeconds);
            LogTest.debug("Failure reason:", throwable);
        }
    }

    /**
     * Extracts and attaches filtered logs to the Allure report for the executed test.
     *
     * @param testName The name of the test for which logs should be extracted.
     */
    private static void attachFilteredLogsToAllure(String testName) {
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

            if (!filteredLogs.isEmpty()) {
                Allure.addAttachment("Filtered Logs for Test: " + testName, "text/plain", filteredLogs, ".log");
            } else {
                Allure.addAttachment("Filtered Logs for Test: " + testName, "text/plain",
                    "No logs found for test: " + testName, ".log");
            }
        } catch (IOException e) {
            String errorMessage = "Failed to read or filter logs for test: " + testName + ". Error: " + e.getMessage();
            Allure.addAttachment("Filtered Logs for Test: " + testName, "text/plain", errorMessage, ".log");
        }
    }
}
