package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Provides logging functions for exception handling during Selenium WebDriver interactions.
 * This class logs detailed information for various exceptions encountered while performing
 * actions such as finding elements, clicking, sending keys, and submitting forms.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class LoggingFunctions {

   private static final String OUTER_HTML = "outerHTML";
   private static final String INNER_HTML = "innerHTML";
   private static final int MAX_LENGTH = 1000;
   private static final String PAGE_SOURCE_UNAVAILABLE = "Page source unavailable.";


   private LoggingFunctions() {
   }

   /**
    * Logs a {@link NoSuchElementException} when an element cannot be found from the root level.
    *
    * @param target The WebDriver instance.
    * @param action The WebElement action being performed.
    * @param args   The method arguments.
    * @param e      The thrown exception.
    */
   public static void logFindElementFromRootNoSuchElementException(final Object target, final WebElementAction action,
         final Object[] args, final InvocationTargetException e) {
      By locator = LocatorParser.extractLocator(args);
      Throwable cause = e.getCause();
      WebDriver driver = (WebDriver) target;
      String pageSource = truncateString(driver.getPageSource(), MAX_LENGTH);
      String additionalInfo = String.format(
            "Exception [%s]: Failed to locate element using locator [%s] at the root level.%n"
                  + "Possible reasons: incorrect locator, element not present in the DOM, or slow page loading.%n%n"
                  + "Current Page Source (truncated):%n%s",
            cause.getClass().getSimpleName(), locator, pageSource);
      logException(cause.getClass(), target, action.getMethodName(), args, additionalInfo);
   }

   /**
    * Logs a {@link NoSuchElementException} when an element cannot be found from another element.
    *
    * @param target The target WebElement.
    * @param action The WebElement action being performed.
    * @param args   The method arguments.
    * @param e      The thrown exception.
    */
   public static void logNoSuchElementException(final Object target, final WebElementAction action,
         final Object[] args, final InvocationTargetException e) {
      Throwable cause = e.getCause();
      By locator = LocatorParser.extractLocator(args);
      WebElement element = target instanceof WebElement we ? we : null;
      if (element == null) {
         logException(cause.getClass(), target, action.getMethodName(), args,
               "Exception [" + cause.getClass().getSimpleName()
                     + "]: Element is null, possibly due to a stale reference or incorrect locator.");
         return;
      }
      String outerHtml = truncateString(element.getAttribute(OUTER_HTML), MAX_LENGTH);
      String innerHtml = truncateString(element.getAttribute(INNER_HTML), MAX_LENGTH);
      String elementDetails = LocatorParser.getElementDetails(element, outerHtml, innerHtml);
      String actionMessage = switch (action) {
         case FIND_ELEMENT, FIND_ELEMENTS -> locator != null
               ? String.format("Exception [%s]: Unable to locate element [%s] using locator [%s].",
               cause.getClass().getSimpleName(), element.getTagName(), locator)
               : String.format("Exception [%s]: Unable to locate element [%s], but no locator was provided.",
               cause.getClass().getSimpleName(), element.getTagName());
         case CLICK -> String.format("Exception [%s]: Click action failed on element [%s] with text [%s].",
               cause.getClass().getSimpleName(), element.getTagName(), element.getText());
         case SEND_KEYS -> String.format("Exception [%s]: Unable to send keys to element [%s] with text [%s].",
               cause.getClass().getSimpleName(), element.getTagName(), element.getText());
         case SUBMIT -> String.format("Exception [%s]: Unable to submit form using element [%s].",
               cause.getClass().getSimpleName(), element.getTagName());
         default -> "Unknown action.";
      };
      String additionalInfo = actionMessage + "\n" + elementDetails;
      logException(cause.getClass(), target, action.getMethodName(), args, additionalInfo);
   }

   /**
    * Logs an {@link ElementNotInteractableException} when an element cannot be interacted with.
    *
    * @param target The target WebElement.
    * @param action The WebElement action being performed.
    * @param args   The method arguments.
    * @param e      The thrown exception.
    */
   public static void logElementNotInteractableException(final Object target, final WebElementAction action,
         final Object[] args, final InvocationTargetException e) {
      Throwable cause = e.getCause();
      WebElement element = target instanceof WebElement we ? we : null;
      if (element == null) {
         logException(cause.getClass(), target, action.getMethodName(), args,
               "Element is null, cannot log additional info.");
         return;
      }
      String outerHtml = truncateString(element.getAttribute(OUTER_HTML), MAX_LENGTH);
      String innerHtml = truncateString(element.getAttribute(INNER_HTML), MAX_LENGTH);
      String elementDetails = LocatorParser.getElementDetails(element, outerHtml, innerHtml);
      String additionalInfo = String.format(
            "Exception [%s]: Element [%s] with text [%s] is not interactable when attempting to perform %s().%n"
                  + "Possible reasons: hidden, disabled, overlapped, or detached from the DOM.%n%nElement Details:%n%s",
            cause.getClass().getSimpleName(), element.getTagName(), element.getText(), action.getMethodName(),
            elementDetails);
      logException(cause.getClass(), target, action.getMethodName(), args, additionalInfo);
   }

   /**
    * Logs an {@link org.openqa.selenium.InvalidSelectorException} when an invalid selector is used.
    *
    * @param target The WebDriver or WebElement instance where the action occurred.
    * @param action The WebElement action being performed.
    * @param args   The method arguments, expected to contain a locator.
    * @param e      The thrown exception.
    */
   public static void logClickInvalidSelectorException(final Object target, final WebElementAction action,
         final Object[] args, final InvocationTargetException e) {
      By locator = LocatorParser.extractLocator(args);
      Throwable cause = e.getCause();
      String additionalInfo = String.format("Invalid selector while clicking element. Malformed locator: [%s].",
            locator != null ? locator.toString() : "null");
      logException(cause.getClass(), target, action.getMethodName(), args, additionalInfo);
   }

   /**
    * Logs an {@link ElementClickInterceptedException} when an element cannot be clicked.
    *
    * @param target The target WebElement.
    * @param action The WebElement action being performed.
    * @param args   The method arguments.
    * @param e      The thrown exception.
    */
   public static void logElementClickInterceptedException(final Object target, final WebElementAction action,
         final Object[] args, final InvocationTargetException e) {
      Throwable cause = e.getCause();
      WebElement element = null;
      if (target instanceof WebElement webElement) {
         element = webElement;
      }
      if (element == null) {
         logException(cause.getClass(), target, action.getMethodName(), args,
               "Element is null, cannot log additional info.");
         return;
      }
      String outerHtml = truncateString(element.getAttribute(OUTER_HTML), MAX_LENGTH);
      String innerHtml = truncateString(element.getAttribute(INNER_HTML), MAX_LENGTH);
      String elementDetails = LocatorParser.getElementDetails(element, outerHtml, innerHtml);
      String blockingLocator = LocatorParser.extractLocatorFromMessage(cause.getMessage());
      String additionalInfo = String.format(
            "Failed to %s on element:%n%s%nLocator of the blocking element: [%s]",
            action.getMethodName(), elementDetails, blockingLocator);
      logException(cause.getClass(), target, action.getMethodName(), args, additionalInfo);
   }

   /**
    * Logs a {@link TimeoutException} when an element is not found within a given time frame.
    *
    * @param target The WebDriver or WebElement instance where the action occurred.
    * @param action The WebElement action being performed.
    * @param args   The method arguments, expected to contain a locator and an optional timeout duration.
    * @param e      The thrown exception.
    */
   public static void logClickTimeoutException(final Object target, final WebElementAction action,
         final Object[] args, final InvocationTargetException e) {
      Throwable cause = e.getCause();
      By locator = LocatorParser.extractLocator(args);
      long timeout = 0L;
      if (args != null && args.length > 1 && args[1] instanceof Long l) {
         timeout = l;
      }
      String timeoutMessage = timeout > 0 ? "Waited for " + timeout + " milliseconds." : "No specific timeout set.";
      String additionalInfo = String.format(
            "Timeout while waiting for element to meet condition: [%s]. Locator: [%s]. %s",
            action.getMethodName(), locator != null ? locator.toString() : "Unknown locator", timeoutMessage);
      logException(cause.getClass(), target, action.getMethodName(), args, additionalInfo);
   }


   private static void logException(Class<? extends Throwable> throwable, Object target, String methodName,
         Object[] methodArgs, String additionalInfo) {
      LogUi.extended("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with argument types: [{}]",
            throwable.getSimpleName(),
            target.getClass().getSimpleName(),
            target.toString(),
            methodName,
            printMethodArgsClasses(methodArgs));
      LogUi.extended("Additional Info for the problem: {}", additionalInfo);
   }

   private static String printMethodArgsClasses(Object[] methodArgs) {
      if (methodArgs == null || methodArgs.length == 0) {
         return "";
      }
      return Arrays.stream(methodArgs)
            .map(arg -> arg == null ? "null" : arg.getClass().getSimpleName())
            .collect(Collectors.joining(","));
   }

   private static String truncateString(String input, int length) {
      if (input == null) {
         return PAGE_SOURCE_UNAVAILABLE;
      }
      return input.length() > length ? input.substring(0, length) + "..." : input;
   }

}
