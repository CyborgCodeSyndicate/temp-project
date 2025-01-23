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

@Order(Integer.MAX_VALUE)
public class Epilogue implements AfterTestExecutionCallback {

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

    private void logTestOutcome(String testName, String status, long durationInSeconds, Throwable throwable) {
        if (throwable == null) {
            LogTest.info("The quest of '{}' has concluded with glory. Status: {}. Duration: {} seconds.",
                testName, status, durationInSeconds);
        } else {
            LogTest.info("The quest of '{}' has ended in defeat. Status: {}. Duration: {} seconds.",
                testName, status, durationInSeconds);
        }
    }

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
