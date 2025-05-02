package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Provides utility functions for handling WebElement exceptions.
 *
 * <p>This class defines methods to recover from common Selenium exceptions such as {@link NoSuchElementException},
 * {@link TimeoutException}, and {@link org.openqa.selenium.StaleElementReferenceException}. It aims to improve test
 * stability by implementing fallback strategies for locating and interacting with elements.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ExceptionHandlingWebElementFunctions {

   private ExceptionHandlingWebElementFunctions() {
   }

   /**
    * Handles a stale element reference by attempting to re-locate the element.
    *
    * @param driver           The WebDriver instance.
    * @param element          The stale {@link SmartWebElement}.
    * @param webElementAction The action to be performed on the element.
    * @param args             Additional arguments required for the action.
    * @return The result of the attempted action.
    */
   public static Object handleStaleElement(WebDriver driver, SmartWebElement element,
                                           WebElementAction webElementAction, Object... args) {
      try {
         element = LocatorParser.updateWebElement(driver, element);
         return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
      } catch (Exception e) {
         String errorMessage = String.format(
               "[BROKEN] WebElement action '%s' failed for stale element exception",
               webElementAction.getMethodName());
         LogUi.error(errorMessage);
         throw e;
      }
   }


   /**
    * Handles the case where an element is not found by searching within iframes.
    *
    * @param driver           The WebDriver instance.
    * @param element          The {@link SmartWebElement} that was not found.
    * @param webElementAction The action to be performed if the element is found.
    * @param args             Additional arguments required for the action.
    * @return The result of the attempted action if the element is found.
    * @throws NoSuchElementException If the element cannot be found in any iframe.
    */
   public static Object handleNoSuchElement(WebDriver driver, SmartWebElement element,
                                            WebElementAction webElementAction, Object... args) {
      if (args.length == 0 || !(args[0] instanceof By)) {
         LogUi.error("Invalid or missing locator argument for FIND_ELEMENT.");
         throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
      }

      WebElement foundElement = FrameHelper.findElementInIframes(driver, element.getOriginal());
      if (foundElement != null) {
         return webElementAction.performActionWebElement(driver, foundElement, args);
      }

      String errorMessage = String.format(
            "[BROKEN] WebElement action '%s' could not be executed - Element with locator '%s' not found.",
            webElementAction.getMethodName(), args[0]
      );

      LogUi.error(errorMessage);
      throw new NoSuchElementException("Element not found in any iframe.");
   }


   /**
    * Handles the case where an element is intercepted by another element when clicked.
    *
    * @param driver           The WebDriver instance.
    * @param element          The {@link SmartWebElement} that was intercepted.
    * @param webElementAction The action to be retried after waiting for the blocker to disappear.
    * @param exception        The exception that occurred during the click action.
    * @param args             Additional arguments required for the action.
    * @return The result of the attempted action.
    */
   public static Object handleElementClickIntercepted(WebDriver driver, SmartWebElement element,
                                                      WebElementAction webElementAction, Exception exception,
                                                      Object... args) {
      String xpathExpression = null;
      try {
         xpathExpression = LocatorParser.extractBlockingElementLocator(exception.getMessage());
         By blocker = By.xpath(xpathExpression);
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
         try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
         } catch (TimeoutException e) {
            LogUi.warn("Blocking element did not disappear after waiting, attempting action anyway.");
         }

         return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
      } catch (Exception e) {
         String errorMessage = String.format(
               "[BROKEN] WebElement action '%s' failed due to intercepted click with locator '%s'. Exception: '%s'",
               webElementAction.getMethodName(), xpathExpression == null ? "Cannot Extract Locator" : xpathExpression,
               e.getClass().getSimpleName()
         );
         LogUi.error(errorMessage);
         throw e;
      }
   }


   /**
    * Handles the case where an element is not interactable by scrolling to it and retrying the action.
    *
    * @param driver           The WebDriver instance.
    * @param element          The {@link SmartWebElement} that was not interactable.
    * @param webElementAction The action to be retried.
    * @param args             Additional arguments required for the action.
    * @return The result of the attempted action.
    */
   public static Object handleElementNotInteractable(WebDriver driver, SmartWebElement element,
                                                     WebElementAction webElementAction, Object... args) {
      try {
         element = LocatorParser.updateWebElement(driver, element);
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
         Actions actions = new Actions(driver);
         actions.moveToElement(element.getOriginal()).perform();
         WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal()));

         return WebElementAction.performAction(driver, clickableElement, webElementAction, args);
      } catch (Exception e) {
         String errorMessage = String.format(
               "[BROKEN] WebElement action '%s' failed because element was not interactable. Exception: '%s'",
               webElementAction.getMethodName(), e.getClass().getSimpleName()
         );
         LogUi.error(errorMessage);
         throw e;
      }
   }

}
