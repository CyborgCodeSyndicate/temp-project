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
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Order(Integer.MAX_VALUE)
public class Epilogue implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        Throwable throwable = context.getExecutionException().orElse(null);
        String status = (throwable == null) ? "SUCCESS" : "FAILED";

        long startTime = context.getStore(GLOBAL).get(START_TIME.getKey(), long.class);
        long durationInSeconds = (System.currentTimeMillis() - startTime) / 1000;

        if (throwable == null) {
            LogTest.info("The quest of '{}' has concluded with glory. Status: {}. Duration: {} seconds.",
                context.getDisplayName(), status, durationInSeconds);
        } else {
            LogTest.info("The quest of '{}' has ended in defeat. Status: {}. Duration: {} seconds.",
                context.getDisplayName(), status, durationInSeconds);
            LogTest.debug("Failure reason:", throwable);
        }
        attachFilteredLogsToAllure(ThreadContext.get("testName"));
        ThreadContext.remove("testName");
    }


    private static void attachFilteredLogsToAllure(String testName) {
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
