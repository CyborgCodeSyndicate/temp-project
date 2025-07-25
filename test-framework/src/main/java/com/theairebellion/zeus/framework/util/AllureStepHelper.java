package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;
import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static com.theairebellion.zeus.framework.util.ObjectFormatter.escapeHtml;
import static com.theairebellion.zeus.framework.util.ObjectFormatter.formatAnnotationsToNewRows;
import static com.theairebellion.zeus.framework.util.ObjectFormatter.formatLongText;
import static com.theairebellion.zeus.framework.util.ObjectFormatter.getClassAnnotations;
import static com.theairebellion.zeus.framework.util.ObjectFormatter.getMethodAnnotations;
import static com.theairebellion.zeus.framework.util.ObjectFormatter.getTestArguments;
import static com.theairebellion.zeus.framework.util.ResourceLoader.loadResourceFile;

/**
 * Utility class for managing test metadata and attaching logs to Allure reports.
 *
 * <p>This class provides helper methods to:
 * <ul>
 *     <li>Set HTML descriptions in Allure reports.</li>
 *     <li>Attach filtered logs based on test execution.</li>
 *     <li>Log test outcomes with structured messages.</li>
 *     <li>Generate and inject metadata into test reports.</li>
 *     <li>Initialize the test environment by writing configuration properties and categories.</li>
 *     <li>Set up the test context with start time and a unique test identifier.</li>
 * </ul>
 *
 * <p>The class leverages Allure for report attachments, the OWNER library for configuration management,
 * and reflection for dynamic retrieval of configuration properties.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class AllureStepHelper {

   public static final String CONTENT_TYPE = "text/plain";

   private AllureStepHelper() {
   }

   private static final String ALLURE_RESULTS_DIR = "allure-results";
   private static final String ENVIRONMENT_PROPERTIES_FILE = "environment.properties";
   private static final String CATEGORIES_JSON_PATH = "allure/json/categories.json";
   private static final String FRAMEWORK_PACKAGE = "com.theairebellion.zeus";
   private static final String CATEGORIES_JSON = "categories.json";

   /**
    * Sets an HTML description for the test execution in Allure reports.
    *
    * <p>This method retrieves HTML content stored under the {@code HTML} key in the global store of the
    * provided {@link ExtensionContext}, filters for valid table-based content, and appends it as a formatted
    * HTML description in Allure.
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
    *
    * <p>This method reads a system log file and filters for entries containing a test scenario identifier.
    * If the test name is unavailable or if no matching log entries are found, a fallback message is attached.
    *
    * @param testName The name of the test scenario to filter logs for.
    */
   public static void attachFilteredLogsToAllure(String testName) {
      if (testName == null || testName.isEmpty()) {
         Allure.addAttachment("Filtered Logs", CONTENT_TYPE, "Test name is not available.", ".log");
         return;
      }

      String logFilePath = System.getProperty("logFileName", "logs/zeus.log");
      String testIdentifier = "[scenario=" + testName + "]";

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFilePath),
            StandardCharsets.UTF_8))) {
         String filteredLogs = reader.lines()
               .filter(line -> line.contains(testIdentifier))
               .collect(Collectors.joining(System.lineSeparator()));

         String attachmentContent = filteredLogs.isEmpty()
               ? "No logs found for test: " + testName
               : filteredLogs;

         Allure.addAttachment("Filtered Logs for Test: " + testName, CONTENT_TYPE, attachmentContent, ".log");
      } catch (IOException e) {
         Allure.addAttachment("Filtered Logs for Test: " + testName, CONTENT_TYPE,
               "Failed to read logs. Error: " + e.getMessage(), ".log");
      }
   }

   /**
    * Logs the test outcome after execution.
    *
    * <p>This method logs whether the test concluded successfully or failed, along with the duration
    * of execution. If the test failed, additional debugging details are logged.
    *
    * @param testName          The name of the test that was executed.
    * @param status            The test execution status (e.g., "SUCCESS" or "FAILED").
    * @param durationInSeconds The duration of the test execution in seconds.
    * @param throwable         The exception thrown during execution (if any).
    */
   public static void logTestOutcome(String testName, String status, long durationInSeconds, Throwable throwable) {
      String logMessage = "The quest of '{}' has "
            + (throwable == null ? "concluded with glory" : "ended in defeat")
            + ". Status: {}. Duration: {} seconds.";

      LogTest.info(logMessage, testName, status, durationInSeconds);
      if (throwable != null) {
         LogTest.debug("Failure reason:", throwable);
      }
   }

   /**
    * Sets up and formats test metadata for Allure reports.
    *
    * <p>This method loads a predefined HTML template and dynamically populates it with:
    * <ul>
    *     <li>Test name and class details.</li>
    *     <li>Annotations applied at the class and method levels.</li>
    *     <li>Test argument values.</li>
    * </ul>
    * The formatted HTML is then stored under the {@code HTML} key in the global store of the
    * provided {@link ExtensionContext} for later attachment to the Allure report.
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

   /**
    * Initializes the test environment by collecting configuration properties and writing them to files.
    *
    * <p>This method performs the following actions:
    * <ul>
    *     <li>Collects configuration properties from all implementations of {@link PropertyConfig}.</li>
    *     <li>Writes the collected properties to an environment properties file.</li>
    *     <li>Writes a JSON file containing category definitions for Allure reports.</li>
    * </ul>
    */
   public static void initializeTestEnvironment() {
      Map<String, List<String>> propertiesMap = collectConfigurationProperties();
      writeEnvironmentProperties(propertiesMap);
      writeCategoriesJson();
   }

   /**
    * Collects configuration properties from all implementations of {@link PropertyConfig}.
    *
    * <p>This method uses reflection to find all classes implementing {@link PropertyConfig} in both the
    * framework package and the project package. It then retrieves configuration key-value pairs from each
    * implementation and returns them as a map.
    *
    * @return A map containing configuration keys and their corresponding values, annotated with their source.
    */
   private static Map<String, List<String>> collectConfigurationProperties() {
      List<Class<? extends PropertyConfig>> allConfig = findAllPropertyConfigImplementations();

      return allConfig.stream()
            .flatMap(configClass -> {
               ConfigSource configSource = configClass.getAnnotation(ConfigSource.class);
               String configSourceValue = (configSource != null) ? configSource.value() : "unknown";

               PropertyConfig propertyConfig = ConfigCache.getOrCreate(configClass);
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
                              LogTest.debug("Skipping key '" + key + "' because value is empty or null.");
                              return null;
                           }

                           return new AbstractMap.SimpleEntry<>(
                                 key,
                                 value + " (Source: " + configSourceValue + ")"
                           );
                        } catch (Exception e) {
                           LogTest.error("Error processing " + method.getName(), e);
                           return null;
                        }
                     })
                     .filter(Objects::nonNull);
            })
            .collect(Collectors.groupingBy(
                  Map.Entry::getKey,
                  Collectors.mapping(Map.Entry::getValue, Collectors.toList())
            ));
   }

   /**
    * Finds all implementations of the {@link PropertyConfig} interface within the framework and project packages.
    *
    * <p>This method leverages the {@link ReflectionUtil} to search for classes that implement {@link PropertyConfig}
    * in both the framework's base package and the project-specific package defined in the framework configuration.
    *
    * @return A list of classes implementing {@link PropertyConfig}.
    */
   private static List<Class<? extends PropertyConfig>> findAllPropertyConfigImplementations() {
      List<Class<? extends PropertyConfig>> implementationsOfInterfaceInFramework =
            ReflectionUtil.findImplementationsOfInterface(PropertyConfig.class, FRAMEWORK_PACKAGE);
      List<Class<? extends PropertyConfig>> implementationsOfInterfaceInProject =
            ReflectionUtil.findImplementationsOfInterface(PropertyConfig.class, getFrameworkConfig().projectPackage());

      List<Class<? extends PropertyConfig>> allConfig = new ArrayList<>();
      allConfig.addAll(implementationsOfInterfaceInFramework);
      allConfig.addAll(implementationsOfInterfaceInProject);
      return allConfig;
   }

   /**
    * Writes the collected configuration properties to the environment properties file.
    *
    * <p>The file is written to the {@code allure-results} directory. If the directory does not exist,
    * it is created.
    *
    * @param propertiesMap A map containing configuration keys and values.
    * @throws RuntimeException if writing to the file fails.
    */
   private static void writeEnvironmentProperties(Map<String, List<String>> propertiesMap) {
      File allureResultsDir = new File(ALLURE_RESULTS_DIR);

      if (!allureResultsDir.exists() && !allureResultsDir.mkdirs()) {
         throw new UncheckedIOException(new IOException("Failed to create allure results directory: "
               + allureResultsDir.getAbsolutePath()));
      }

      File environmentFile = new File(allureResultsDir, ENVIRONMENT_PROPERTIES_FILE);
      try (Writer writer = new OutputStreamWriter(new FileOutputStream(environmentFile), StandardCharsets.UTF_8)) {
         for (Map.Entry<String, List<String>> entry : propertiesMap.entrySet()) {
            String combinedValues = String.join(", ", entry.getValue());
            writer.write(entry.getKey() + "=" + combinedValues + "\n");
         }
      } catch (IOException e) {
         throw new UncheckedIOException("Failed to write environment.properties file", e);
      }
   }

   /**
    * Writes the categories JSON file for Allure reports.
    *
    * <p>This method loads the JSON content from a resource file and writes it to a file named
    * {@code categories.json} in the {@code allure-results} directory.
    *
    * @throws RuntimeException if writing to the file fails.
    */
   private static void writeCategoriesJson() {
      String categoriesJson = loadResourceFile(CATEGORIES_JSON_PATH);
      File allureResultsDir = new File(ALLURE_RESULTS_DIR);

      if (!allureResultsDir.exists() && !allureResultsDir.mkdirs()) {
         throw new UncheckedIOException(new IOException("Failed to create allure results directory: "
               + allureResultsDir.getAbsolutePath()));
      }

      File categoriesFile = new File(allureResultsDir, CATEGORIES_JSON);
      try (Writer writer = new OutputStreamWriter(new FileOutputStream(categoriesFile), StandardCharsets.UTF_8)) {
         writer.write(categoriesJson);
      } catch (IOException e) {
         throw new UncheckedIOException("Failed to write categories.json file", e);
      }
   }

   /**
    * Sets up the test context by storing the test name and start time.
    *
    * <p>This method extracts the simple names of the test class and method from the provided
    * {@link ExtensionContext} and stores them in the {@link ThreadContext} under the key "testName".
    * Additionally, it stores the current system time in milliseconds as the test start time in the global store.
    *
    * @param context The test execution context.
    */
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
