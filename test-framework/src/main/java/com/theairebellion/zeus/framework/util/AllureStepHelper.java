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

public class AllureStepHelper extends ObjectFormatter {

    public static void setDescription(ExtensionContext context) {
        List<String> htmlContent = (List<String>) context.getStore(ExtensionContext.Namespace.GLOBAL).get(HTML);

        String combinedHtml = htmlContent.stream()
                .filter(Objects::nonNull)
                .filter(table -> table.contains("<td>"))
                .collect(Collectors.joining("", "<div style='margin: 20px;'>", "</div>"));

        Allure.descriptionHtml(combinedHtml);
    }

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

    public static void logTestOutcome(String testName, String status, long durationInSeconds, Throwable throwable) {
        String logMessage = "The quest of '{}' has " +
                (throwable == null ? "concluded with glory" : "ended in defeat") +
                ". Status: {}. Duration: {} seconds.";

        LogTest.info(logMessage, testName, status, durationInSeconds);
        if (throwable != null) {
            LogTest.debug("Failure reason:", throwable);
        }
    }

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

    @FunctionalInterface
    public interface AllureStepSupplier<T> {
        T get() throws ParameterResolutionException;
    }
}