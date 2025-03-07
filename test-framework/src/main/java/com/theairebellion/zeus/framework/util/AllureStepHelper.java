package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class AllureStepHelper extends ObjectFormatter {

    public <T> T executeWithAllureStep(String stepName, AllureStepHelper.AllureStepSupplier<T> supplier) {
        String stepId = UUID.randomUUID().toString();
        Allure.getLifecycle().startStep(stepId, new StepResult().setName(stepName).setStatus(Status.PASSED));
        try {
            return supplier.get();
        } finally {
            Allure.getLifecycle().stopStep(stepId);
        }
    }

    public void setDescription(SuperQuest superQuest) {
        List<String> htmlContent = superQuest.getStorage()
                .sub(StorageKeysTest.ALLURE_DESCRIPTION)
                .getAllByClass(StorageKeysTest.HTML, String.class);

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

    public void logTestOutcome(String testName, String status, long durationInSeconds, Throwable throwable) {
        String logMessage = "The quest of '{}' has " +
                (throwable == null ? "concluded with glory" : "ended in defeat") +
                ". Status: {}. Duration: {} seconds.";

        LogTest.info(logMessage, testName, status, durationInSeconds);
        if (throwable != null) {
            LogTest.debug("Failure reason:", throwable);
        }
    }

    public void setUpTestMetadata(ExtensionContext context, SuperQuest superQuest) {
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

        superQuest.getStorage().sub(StorageKeysTest.ALLURE_DESCRIPTION).put(StorageKeysTest.HTML, formattedHtml);
    }


    @FunctionalInterface
    public interface AllureStepSupplier<T> {
        T get() throws ParameterResolutionException;
    }
}
