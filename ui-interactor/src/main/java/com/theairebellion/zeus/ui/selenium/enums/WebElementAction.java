package com.theairebellion.zeus.ui.selenium.enums;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Enum representing different WebElement actions that can be performed.
 *
 * <p>Each action provides an implementation for executing the respective WebElement operation
 * and wrapping elements in {@link SmartWebElement} where applicable.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public enum WebElementAction {

   /**
    * Action to find a single WebElement and wrap it in a {@link SmartWebElement}.
    */
   FIND_ELEMENT("findElement") {
      @Override
      public Object performActionWebElement(WebDriver driver, WebElement element, Object... args) {
         return new SmartWebElement(element, driver);
      }

      @Override
      public Object performActionWebDriver(WebDriver driver, Object... args) {
         return new SmartWebElement((WebElement) args[0], driver);
      }
   },

   /**
    * Action to find multiple WebElements and wrap them in {@link SmartWebElement} instances.
    */
   FIND_ELEMENTS("findElements") {
      @Override
      public Object performActionWebElement(WebDriver driver, WebElement element, Object... args) {
         return element.findElements((By) args[0]).stream()
               .map(e -> new SmartWebElement(e, driver))
               .toList();
      }

      @Override
      public Object performActionWebDriver(WebDriver driver, Object... args) {
         return driver.findElements((By) args[0]).stream()
               .map(e -> new SmartWebElement(e, driver))
               .toList();
      }
   },

   /**
    * Action to perform a click operation on a WebElement.
    */
   CLICK("click") {
      @Override
      public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
         element.click();
         return null;
      }

      @Override
      public Object performActionWebDriver(WebDriver driver, Object... args) {
         return null;
      }
   },

   /**
    * Action to send keys (text input) to a WebElement.
    */
   SEND_KEYS("sendKeys") {
      @Override
      public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
         element.sendKeys((String) args[0]);
         return null;
      }

      @Override
      public Object performActionWebDriver(WebDriver driver, Object... args) {
         return null;
      }
   },

   /**
    * Action to submit a WebElement (commonly used for forms).
    */
   SUBMIT("submit") {
      @Override
      public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
         element.submit();
         return null;
      }

      @Override
      public Object performActionWebDriver(WebDriver driver, Object... args) {
         return null;
      }
   },

   /**
    * Action to clear the content of a WebElement (commonly input fields).
    */
   CLEAR("clear") {
      @Override
      public Void performActionWebElement(WebDriver driver, WebElement element, Object... args) {
         element.clear();
         return null;
      }

      @Override
      public Object performActionWebDriver(WebDriver driver, Object... args) {
         return null;
      }
   };

   /**
    * The method name associated with the action.
    */
   private final String methodName;

   /**
    * Constructor to associate a method name with the enum constant.
    *
    * @param methodName The method name as a string.
    */
   WebElementAction(String methodName) {
      this.methodName = methodName;
   }

   /**
    * Performs the given action on a WebElement.
    *
    * @param driver  The WebDriver instance.
    * @param element The target WebElement.
    * @param action  The action to perform.
    * @param args    Additional arguments for the action.
    * @return The result of the action, if applicable.
    */
   public static Object performAction(WebDriver driver, WebElement element, WebElementAction action, Object... args) {
      return action.performActionWebElement(driver, element, args);
   }

   /**
    * Performs the defined action on a WebElement.
    *
    * @param driver  The WebDriver instance.
    * @param element The target WebElement.
    * @param args    Additional arguments required for the action.
    * @return The result of the action, if applicable.
    */
   public abstract Object performActionWebElement(WebDriver driver, WebElement element, Object... args);

   /**
    * Performs the defined action using only a WebDriver instance.
    *
    * @param driver The WebDriver instance.
    * @param args   Additional arguments required for the action.
    * @return The result of the action, if applicable.
    */
   public abstract Object performActionWebDriver(WebDriver driver, Object... args);
}
