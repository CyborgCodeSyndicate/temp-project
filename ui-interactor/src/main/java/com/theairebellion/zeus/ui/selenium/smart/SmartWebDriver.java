package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUiException;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebDriver;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * A custom wrapper for {@link WebDriver} that enhances element handling,
 * adds exception management, and provides built-in support for Shadow DOM elements.
 *
 * <p>It integrates with Selenium functions while allowing configuration-based
 * handling of standard and shadow root elements.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public class SmartWebDriver extends WebDriverDecorator {

   private final WebDriverWait wait;
   @Setter
   private boolean keepDriverForSession;

   /**
    * Constructs a {@code SmartWebDriver} wrapping the given {@link WebDriver}.
    *
    * @param original The original Selenium WebDriver instance to wrap.
    */
   public SmartWebDriver(WebDriver original) {
      super(original);
      this.wait = new WebDriverWait(original, Duration.ofSeconds(getUiConfig().waitDuration()));
   }

   /**
    * Finds a single element using {@link By} and returns a wrapped {@link SmartWebElement}.
    *
    * @param by The {@link By} locator to find the element.
    * @return A {@link SmartWebElement} instance.
    */
   @HandleUiException
   public SmartWebElement findSmartElement(By by) {
      return findSmartElementInternal(by, null);
   }

   /**
    * Finds a single element with a specified wait time.
    *
    * @param by           The {@link By} locator.
    * @param waitInMillis The wait time in milliseconds.
    * @return A {@link SmartWebElement} instance.
    */
   public SmartWebElement findSmartElement(By by, long waitInMillis) {
      return findSmartElementInternal(by, waitInMillis);
   }

   /**
    * Finds multiple elements matching the given {@link By} locator.
    *
    * @param by The {@link By} locator to find elements.
    * @return A list of {@link SmartWebElement}.
    */
   @HandleUiException
   public List<SmartWebElement> findSmartElements(By by) {
      if (!getUiConfig().useWrappedSeleniumFunctions()) {
         return SmartFinder.findElementsNoWrap(getOriginal(), by);
      }

      Consumer<Function<WebDriver, ?>> waitFn = this::waitWithoutFailure;
      try {
         if (getUiConfig().useShadowRoot()) {
            return SmartFinder.findElementsWithShadowRootDriver(this, by, waitFn);
         } else {
            return SmartFinder.findElementsNormally(getOriginal(), by, waitFn);
         }
      } catch (Exception e) {
         return handleException("findElements", e, new Object[] {by});
      }
   }

   /**
    * Overrides Selenium's {@link WebDriver#findElement(By)} to support both normal and Shadow DOM elements.
    *
    * @param by The {@link By} locator to find an element.
    * @return The found {@link WebElement}.
    */
   @Override
   @NonNull
   public WebElement findElement(@NonNull By by) {
      Consumer<Function<WebDriver, ?>> waitFn =
            condition -> {
               try {
                  WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(10));
                  customWait.until(condition);
               } catch (Exception ignored) {
               }
            };
      if (getUiConfig().useShadowRoot()) {
         return SmartFinder.findElementWithShadowRootDriver(this, by, waitFn, 10L);
      } else {
         return SmartFinder.findElementNormally(getOriginal(), by, waitFn);
      }
   }

   /**
    * Overrides Selenium's {@link WebDriver#findElements(By)} to support both normal and Shadow DOM elements.
    *
    * @param by The {@link By} locator to find elements.
    * @return A list of found {@link WebElement}.
    */
   @Override
   @NonNull
   public List<WebElement> findElements(@NonNull By by) {
      Consumer<Function<WebDriver, ?>> waitFn =
            condition -> {
               try {
                  WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(10));
                  customWait.until(condition);
               } catch (Exception ignored) {
               }
            };
      if (getUiConfig().useShadowRoot()) {
         List<SmartWebElement> elementsWithShadowRootDriver =
               SmartFinder.findElementsWithShadowRootDriver(this, by, waitFn);
         return elementsWithShadowRootDriver.stream()
               .map(element -> (WebElement) element)
               .collect(Collectors.toList());
      } else {
         List<SmartWebElement> elementsNormally = SmartFinder.findElementsNormally(getOriginal(), by, waitFn);
         return elementsNormally.stream()
               .map(element -> (WebElement) element)
               .collect(Collectors.toList());
      }
   }

   /**
    * Finds a single {@link SmartWebElement} with optional wait time, supporting both standard and Shadow DOM searches.
    *
    * @param by           The {@link By} locator used to find the element.
    * @param waitInMillis The optional wait duration in milliseconds. If null, a default wait function is used.
    * @return The found {@link SmartWebElement}, or handles the exception if the element is not found.
    */
   private SmartWebElement findSmartElementInternal(By by, Long waitInMillis) {
      if (!getUiConfig().useWrappedSeleniumFunctions()) {
         return SmartFinder.findElementNoWrap(getOriginal(), by);
      }

      Consumer<Function<WebDriver, ?>> waitFn = (waitInMillis == null)
            ? this::waitWithoutFailure
            : condition -> {
               try {
                  WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(waitInMillis));
                  customWait.until(condition);
               } catch (Exception ignored) {
               }
            };

      try {
         if (getUiConfig().useShadowRoot()) {
            return SmartFinder.findElementWithShadowRootDriver(this, by, waitFn, waitInMillis);
         } else {
            return SmartFinder.findElementNormally(getOriginal(), by, waitFn);
         }
      } catch (Exception e) {
         return handleException("findElement", e, new Object[] {by});
      }
   }

   /**
    * Checks if an action can be performed without throwing an exception.
    *
    * @param runnable The action to execute.
    * @return {@code true} if no exception occurs, {@code false} otherwise.
    */
   public boolean checkNoException(Runnable runnable) {
      try {
         runnable.run();
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   /**
    * Waits until a specific {@link SmartWebElement} becomes visible.
    *
    * @param element The element to wait for.
    * @param seconds Maximum wait time in seconds.
    */
   public void waitUntilElementIsShown(SmartWebElement element, int seconds) {
      WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
      try {
         wait.until(ExpectedConditions.visibilityOf(element));
      } catch (Exception e) {
         LogUi.error("Element wasn't displayed after: " + seconds + " seconds");
      }
   }

   /**
    * Waits until an element located by {@link By} becomes visible.
    *
    * @param by      The locator.
    * @param seconds Maximum wait time in seconds.
    */
   public void waitUntilElementIsShown(By by, int seconds) {
      WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
      try {
         wait.until(ExpectedConditions.presenceOfElementLocated(by));
      } catch (Exception e) {
         LogUi.error("Element wasn't displayed after: " + seconds + " seconds");
      }
   }

   /**
    * Waits until a specific {@link SmartWebElement} is no longer visible.
    *
    * @param element The element to wait for.
    * @param seconds Maximum wait time in seconds.
    */
   public void waitUntilElementIsRemoved(SmartWebElement element, int seconds) {
      WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
      try {
         wait.until(ExpectedConditions.invisibilityOf(element));
      } catch (Exception e) {
         LogUi.error("Element wasn't removed after: " + seconds + " seconds");
      }
   }

   /**
    * Waits until an element located by {@link By} is no longer visible.
    *
    * @param by      The locator.
    * @param seconds Maximum wait time in seconds.
    */
   public void waitUntilElementIsRemoved(By by, int seconds) {
      WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
      try {
         wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
      } catch (Exception e) {
         LogUi.error("Element wasn't removed after: " + seconds + " seconds");
      }
   }


   private <T> void waitWithoutFailure(Function<WebDriver, T> condition) {
      try {
         wait.until(condition);
      } catch (Exception ignored) {
      }
   }

   @SneakyThrows
   @SuppressWarnings("unchecked")
   private <T> T handleException(String methodName, Exception exception, Object[] params) {
      Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

      Optional<ExceptionHandlingWebDriver> exceptionHandlingOptional =
            Arrays.stream(ExceptionHandlingWebDriver.values())
                  .filter(enumVal ->
                        enumVal.getMethodName().equals(methodName)
                              && Objects.nonNull(enumVal.getExceptionHandlingMap().get(cause.getClass()))
                  )
                  .findFirst();

      if (exceptionHandlingOptional.isPresent()) {
         try {
            return (T) exceptionHandlingOptional.get()
                  .getExceptionHandlingMap()
                  .get(cause.getClass())
                  .apply(this.getOriginal(), params);
         } catch (Exception handlerException) {
            LogUi.error("Framework attempted to handle an exception in method '" + methodName
                  + "', but the handler failed with: " + handlerException.getClass().getSimpleName() + ": "
                  + handlerException.getMessage(), handlerException);
            exception.addSuppressed(handlerException);
            LogUi.error("Propagating original exception: " + exception.getClass().getSimpleName()
                  + ": " + exception.getMessage(), exception);
            throw exception;
         }
      } else {
         String locator = (params.length > 0 && params[0] instanceof By) ? params[0].toString() : "Unknown locator";
         String exceptionMessage = exception.getClass().getSimpleName();

         String errorMessage = String.format(
               "Exception handling failed for method '%s'. Exception: '%s'. Parameters: Locator - '%s'.",
               methodName, exceptionMessage, locator
         );
         LogUi.error(errorMessage);
         throw exception;
      }
   }
}
