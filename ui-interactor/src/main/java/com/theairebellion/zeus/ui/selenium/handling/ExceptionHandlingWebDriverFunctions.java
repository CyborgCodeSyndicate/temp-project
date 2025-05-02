package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Provides exception handling functions for WebDriver actions.
 *
 * <p>This class includes utility methods to handle exceptions that may occur during
 * WebDriver operations, such as handling {@link NoSuchElementException} when elements are not found.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ExceptionHandlingWebDriverFunctions {

   private ExceptionHandlingWebDriverFunctions() {
   }

   /**
    * Handles {@link NoSuchElementException} by attempting to locate the element within iFrames.
    *
    * <p>If an element is not found in the main DOM, this method will search within available iFrames.
    * If found, the specified {@link WebElementAction} is performed.
    * If the element is still not found, an error is logged and a {@link NoSuchElementException} is thrown.
    *
    * @param driver           The WebDriver instance.
    * @param webElementAction The WebElementAction to perform if the element is found.
    * @param args             The arguments, where the first argument must be a {@link By} locator.
    * @return The result of the performed action, if successful.
    * @throws IllegalArgumentException If the locator argument is missing or invalid.
    * @throws NoSuchElementException   If the element cannot be found in the main DOM or any iframe.
    */
   public static Object handleNoSuchElement(WebDriver driver, WebElementAction webElementAction, Object... args) {
      if (args.length == 0 || !(args[0] instanceof By)) {
         LogUi.error("Invalid or missing locator argument for FIND_ELEMENT.");
         throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
      }

      WebElement foundElement = FrameHelper.searchElementInIframes(driver, (By) args[0]);
      if (foundElement != null) {
         return webElementAction.performActionWebDriver(driver, foundElement);
      }
      LogUi.error("Element not found in the main DOM or any iframe.");
      throw new NoSuchElementException("Element not found in the main DOM or any iframe.");
   }
}
