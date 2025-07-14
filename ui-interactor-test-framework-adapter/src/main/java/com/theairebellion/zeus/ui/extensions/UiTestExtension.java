package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.exceptions.ServiceInitializationException;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.exceptions.AuthenticationUiException;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.parameters.DataIntercept;
import com.theairebellion.zeus.ui.selenium.exceptions.UiInteractionException;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUiServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import com.theairebellion.zeus.ui.util.ResponseFormatter;
import com.theairebellion.zeus.ui.validator.TableAssertionFunctions;
import com.theairebellion.zeus.ui.validator.TableAssertionTypes;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.validator.registry.AssertionRegistry;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.network.model.RequestId;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.framework.allure.StepType.TEAR_DOWN;
import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.util.TestContextManager.getSuperQuest;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static com.theairebellion.zeus.ui.config.UiFrameworkConfigHolder.getUiFrameworkConfig;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.PASSWORD;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.RESPONSES;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.USERNAME;

/**
 * JUnit 5 test extension for managing UI-related test execution lifecycle.
 *
 * <p>This extension provides support for:
 * <ul>
 *     <li>Intercepting UI-related HTTP requests using {@link InterceptRequests}.</li>
 *     <li>Automating login via UI authentication using {@link AuthenticateViaUiAs}.</li>
 *     <li>Capturing screenshots on test failures and optionally on passed tests.</li>
 *     <li>Registering UI assertions and handling WebDriver session cleanup.</li>
 *     <li>Intercepting backend requests in Chrome DevTools.</li>
 * </ul>
 *
 * <p>It integrates with {@link SmartWebDriver} for Selenium interactions and works
 * with {@link Quest} for structured test execution.
 * </p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class UiTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback,
      TestExecutionExceptionHandler, LauncherSessionListener {

   private static final String SELENIUM_PACKAGE = "org.openqa.selenium";
   private static final String UI_MODULE_PACKAGE = "theairebellion.zeus.ui";

   static {
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_NOT_EMPTY,
            TableAssertionFunctions::validateTableNotEmpty);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_ROW_COUNT,
            TableAssertionFunctions::validateTableRowCount);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.EVERY_ROW_CONTAINS_VALUES,
            TableAssertionFunctions::validateEveryRowContainsValues);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_DOES_NOT_CONTAIN_ROW,
            TableAssertionFunctions::validateTableDoesNotContainRow);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ALL_ROWS_ARE_UNIQUE,
            TableAssertionFunctions::validateAllRowsAreUnique);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.NO_EMPTY_CELLS,
            TableAssertionFunctions::validateNoEmptyCells);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.COLUMN_VALUES_ARE_UNIQUE,
            TableAssertionFunctions::validateColumnValuesAreUnique);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_DATA_MATCHES_EXPECTED,
            TableAssertionFunctions::validateTableDataMatchesExpected);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ROW_NOT_EMPTY,
            TableAssertionFunctions::validateRowNotEmpty);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ROW_CONTAINS_VALUES,
            TableAssertionFunctions::validateRowContainsValues);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ALL_CELLS_ENABLED,
            TableAssertionFunctions::validateAllCellsEnabled);
      AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ALL_CELLS_CLICKABLE,
            TableAssertionFunctions::validateAllCellsClickable);
   }

   /**
    * Executes actions before the test runs, such as:
    * <ul>
    *     <li>Intercepting UI-related requests.</li>
    *     <li>Setting up authentication via UI.</li>
    *     <li>Registering custom assertions.</li>
    * </ul>
    *
    * @param context The current test execution context.
    */
   @Override
   public void beforeTestExecution(final ExtensionContext context) {
      context.getTestMethod().ifPresent(method -> {
         processInterceptRequestsAnnotation(context, method);
         registerAssertionConsumer(context);
         processAuthenticateViaUiAsAnnotation(context, method);
         registerCustomServices(context);
      });
   }


   private void registerCustomServices(ExtensionContext context) {

      Consumer<SuperQuest> questConsumer = UiTestExtension::postQuestCreationRegisterCustomServices;
      addQuestConsumer(context, questConsumer);
   }


   private void processInterceptRequestsAnnotation(ExtensionContext context, Method method) {
      Optional.ofNullable(method.getAnnotation(InterceptRequests.class))
            .ifPresent(intercept -> {
               String[] urlsForIntercepting;
               try {
                  List<Class<? extends Enum>> enumClassImplementations =
                        ReflectionUtil.findEnumClassImplementationsOfInterface(
                              DataIntercept.class, getFrameworkConfig().projectPackage());

                  if (enumClassImplementations.size() > 1) {
                     throw new IllegalStateException(
                           "There is more than one enum for representing different types of databases. "
                                 + "Only 1 is allowed");
                  }

                  if (enumClassImplementations.size() == 1) {
                     Class<? extends Enum> enumClass = enumClassImplementations.get(0);

                     List<String> resolvedEndpoints = Arrays.stream(intercept.requestUrlSubStrings())
                           .map(target -> ((DataIntercept<?>) Enum.valueOf(enumClass,
                                 target))
                                 .getEndpointSubString())
                           .toList();

                     if (resolvedEndpoints.isEmpty()) {
                        urlsForIntercepting = intercept.requestUrlSubStrings();
                     } else {
                        urlsForIntercepting = resolvedEndpoints.toArray(new String[0]);
                     }
                  } else {
                     urlsForIntercepting = intercept.requestUrlSubStrings();
                  }
               } catch (Exception e) {
                  urlsForIntercepting = intercept.requestUrlSubStrings();
               }

               final String[] finalUrlsForIntercepting = urlsForIntercepting;
               Consumer<SuperQuest> questConsumer = quest -> postQuestCreationIntercept(quest,
                     finalUrlsForIntercepting);
               addQuestConsumer(context, questConsumer);
            });
   }


   private static void postQuestCreationIntercept(final SuperQuest quest, final String[] urlsForIntercepting) {
      SmartWebDriver artifact = quest.artifact(UiServiceFluent.class, SmartWebDriver.class);
      WebDriver driver = unwrapDriver(artifact.getOriginal());
      if (driver instanceof ChromeDriver chromeDriver) {
         DevTools chromeDevTools = chromeDriver.getDevTools();
         chromeDevTools.createSession();
         chromeDevTools.send(Network.enable(Optional.empty(), Optional.empty(),
                 Optional.empty(), Optional.empty()));

         Map<String, String> requestMethodMap = new ConcurrentHashMap<>();


         chromeDevTools.addListener(Network.requestWillBeSent(), event -> {
            String method = event.getRequest().getMethod();
            RequestId requestId = event.getRequestId();
            requestMethodMap.put(requestId.toJson(), method);
         });

         chromeDevTools.addListener(Network.responseReceived(), entry -> {
            RequestId requestId = entry.getRequestId();
            int statusCode = entry.getResponse().getStatus();
            String method = requestMethodMap.get(requestId.toString());
            String url = entry.getResponse().getUrl();
            ApiResponse response = new ApiResponse(url, method, statusCode);

            if (checkUrl(urlsForIntercepting, url)) {
               try {
                  String body = chromeDevTools.send(Network.getResponseBody(entry.getRequestId())).getBody();
                  response.setBody(body);
               } catch (Exception e) {
                  response.setBody("Error retrieving response body: " + e.getMessage());
               }
            }
            addResponseInStorage(quest.getStorage(), response);
         });
      } else {
         throw new IllegalArgumentException("Intercepting Backend Requests is only acceptable with Chrome browser");
      }
   }


   private void processAuthenticateViaUiAsAnnotation(ExtensionContext context, Method method) {
      Optional.ofNullable(method.getAnnotation(AuthenticateViaUiAs.class))
            .ifPresent(login -> {
               try {
                  ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
                  DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
                  LoginCredentials credentials = login.credentials().getDeclaredConstructor().newInstance();
                  Consumer<SuperQuest> questConsumer =
                        quest -> postQuestCreationLogin(quest, decoratorsFactory, credentials.username(),
                              credentials.password(), login.type(), login.cacheCredentials());
                  addQuestConsumer(context, questConsumer);
               } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                        | NoSuchMethodException e) {
                  throw new AuthenticationUiException("Failed to instantiate login credentials", e);
               }
            });
   }


   private void registerAssertionConsumer(ExtensionContext context) {
      Consumer<SuperQuest> questConsumer = quest -> postQuestCreationAssertion(quest, context.getDisplayName());
      addQuestConsumer(context, questConsumer);
   }


   private void addQuestConsumer(ExtensionContext context, Consumer<SuperQuest> questConsumer) {
      var consumers = getOrCreateQuestConsumers(context);
      consumers.add(questConsumer);
   }


   @SuppressWarnings("unchecked")
   private List<Consumer<SuperQuest>> getOrCreateQuestConsumers(ExtensionContext context) {
      return (List<Consumer<SuperQuest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS,
                  key -> new ArrayList<>());
   }


   private static void addResponseInStorage(Storage storage, ApiResponse apiResponse) {
      List<ApiResponse> responses = storage.sub(UI).get(RESPONSES, new ParameterizedTypeReference<>() {
      });
      if (responses == null) {
         responses = new ArrayList<>();
      }
      responses.add(apiResponse);
      storage.sub(UI).put(RESPONSES, responses);

      LogUi.extended("Response added to storage: URL={}, Status={}", apiResponse.getUrl(), apiResponse.getStatus());
   }


   private static boolean checkUrl(String[] urlsForIntercepting, String url) {
      return Arrays.stream(urlsForIntercepting).anyMatch(url::contains);
   }


   /**
    * Executes actions after the test completes, such as:
    * <ul>
    *     <li>Taking screenshots if enabled.</li>
    *     <li>Cleaning up the WebDriver session.</li>
    * </ul>
    *
    * @param context The current test execution context.
    */
   @Override
   public void afterTestExecution(ExtensionContext context) {
      ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
      DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
      SmartWebDriver smartWebDriver = getSmartWebDriver(decoratorsFactory, context);
      WebDriver driver = unwrapDriver(smartWebDriver);
      if (context.getExecutionException().isEmpty() && getUiFrameworkConfig().makeScreenshotOnPassedTest()) {
         LogUi.warn("Test failed. Taking screenshot for: {}", context.getDisplayName());
         takeScreenshot(driver, context.getDisplayName());
      }
      List<ApiResponse> responses = getSuperQuest(context).getStorage().sub(UI).getAllByClass(RESPONSES,
            ApiResponse.class);
      if (!responses.isEmpty()) {
         String formattedResponses = ResponseFormatter.formatResponses(responses);
         Allure.addAttachment("Intercepted Requests", "text/html",
               new ByteArrayInputStream(formattedResponses.getBytes(StandardCharsets.UTF_8)), ".html");
      }
      if (!smartWebDriver.isKeepDriverForSession()) {
         driver.close();
         driver.quit();
         LogUi.info("WebDriver closed successfully.");
      }
   }


   /**
    * Handles test execution exceptions by capturing a screenshot and throwing the error.
    *
    * @param context   The current test execution context.
    * @param throwable The exception that occurred during test execution.
    * @throws Throwable The rethrown exception.
    */
   @Override
   public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
      throwable.printStackTrace();

      ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
      DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);

      SmartWebDriver smartWebDriver = getSmartWebDriver(decoratorsFactory, context);
      WebDriver driver = unwrapDriver(smartWebDriver);
      takeScreenshot(driver, context.getDisplayName());
      if (!smartWebDriver.isKeepDriverForSession()) {
         driver.close();
         driver.quit();
      }
      throw throwable;
   }


   @Override
   public void launcherSessionClosed(LauncherSession session) {
      BaseLoginClient.getDriverToKeep().forEach(smartWebDriver -> {
         WebDriver driver = unwrapDriver(smartWebDriver);
         driver.close();
         driver.quit();
      });

   }


   private static void postQuestCreationRegisterCustomServices(SuperQuest quest) {

      List<Class<? extends UiServiceFluent>> customUiServices =
            ReflectionUtil.findImplementationsOfInterface(UiServiceFluent.class, getUiConfig().projectPackage());
      if (customUiServices.size() > 1) {
         throw new IllegalStateException(
               "There is more than one UI services that extends from UiServiceFluent. Only 1 is allowed");
      }
      if (!customUiServices.isEmpty()) {
         Class<? extends UiServiceFluent> customUiServiceFluentClass = customUiServices.get(0);
         try {
            SmartWebDriver driver = quest.artifact(UiServiceFluent.class, SmartWebDriver.class);
            quest.registerWorld(customUiServiceFluentClass, customUiServiceFluentClass.getDeclaredConstructor(
                  SmartWebDriver.class, SuperQuest.class).newInstance(driver, quest));
            quest.removeWorld(UiServiceFluent.class);
         } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                  | NoSuchMethodException e) {
            throw new ServiceInitializationException("Failed to register custom UI service", e);
         }
      }

   }


   private static void postQuestCreationLogin(SuperQuest quest, DecoratorsFactory decoratorsFactory,
                                              final String username, final String password,
                                              final Class<? extends BaseLoginClient> type, boolean cache) {
      quest.getStorage().sub(UI).put(USERNAME, username);
      quest.getStorage().sub(UI).put(PASSWORD, password);
      UiServiceFluent<?> uiServiceFluent = quest.enters(UiServiceFluent.class);

      try {
         BaseLoginClient baseLoginClient = type.getDeclaredConstructor().newInstance();
         baseLoginClient.login(decoratorsFactory.decorate(uiServiceFluent, SuperUiServiceFluent.class), username,
               password, cache);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException
               | NoSuchMethodException e) {
         throw new AuthenticationUiException("Failed to instantiate or execute login client", e);
      }
   }


   private static void postQuestCreationAssertion(SuperQuest quest, String testName) {
      SmartWebDriver smartWebDriver = quest.artifact(UiServiceFluent.class, SmartWebDriver.class);
      quest.getSoftAssertions().registerObjectForPostErrorHandling(SmartWebDriver.class, smartWebDriver);

      CustomSoftAssertion.registerCustomAssertion(
            SmartWebDriver.class,
            (assertionError, driver) -> takeScreenshot(unwrapDriver(driver.getOriginal()),
                  "soft_assert_failure_" + testName),
            stackTrace -> Arrays.stream(stackTrace)
                  .anyMatch(element -> element.getClassName().contains(SELENIUM_PACKAGE)
                        || element.getClassName().contains(UI_MODULE_PACKAGE))
      );
   }


   private static void takeScreenshot(WebDriver driver, String testName) {
      if (!Objects.equals(CustomAllureListener.getActiveStepName(), TEAR_DOWN.getDisplayName())) {
         CustomAllureListener.stopStep();
         CustomAllureListener.startStep(TEAR_DOWN);
      }
      try {
         TakesScreenshot screenshot = (TakesScreenshot) driver;
         byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
         Allure.addAttachment(testName, new ByteArrayInputStream(screenshotBytes));
         LogTest.info("Screenshot taken and stored for: " + testName);
      } catch (Exception e) {
         LogUi.error("Failed to take screenshot for test '{}': {}", testName, e.getMessage());
      }
   }


   private static SmartWebDriver getSmartWebDriver(DecoratorsFactory decoratorsFactory, ExtensionContext context) {
      Quest quest = (Quest) context.getStore(ExtensionContext.Namespace.GLOBAL).get(StoreKeys.QUEST);
      SuperQuest superQuest = decoratorsFactory.decorate(quest, SuperQuest.class);
      return superQuest.artifact(UiServiceFluent.class, SmartWebDriver.class);
   }


   private static WebDriver unwrapDriver(WebDriver maybeProxy) {
      try {
         Method getOriginalMethod = maybeProxy.getClass().getMethod("getOriginal");
         return (WebDriver) getOriginalMethod.invoke(maybeProxy);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
         throw new UiInteractionException("Failed to unwrap WebDriver", e);
      }
   }

}