package com.theairebellion.zeus.ui.components.accordion.mock;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.mock;

/**
 * A mock implementation of SmartWebElement for testing purposes.
 */
public class MockSmartWebElement extends SmartWebElement {

   /**
    * Creates a new MockSmartWebElement with mocked WebElement and WebDriver.
    */
   public static MockSmartWebElement createMock() {
      WebElement element = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      return new MockSmartWebElement(element, driver);
   }

   /**
    * Constructor for MockSmartWebElement.
    * Made protected to encourage use of the factory method.
    */
   public MockSmartWebElement(WebElement original, WebDriver driver) {
      super(original, driver);
   }

   @Override
   public String getText() {
      return "dummy";
   }

   @Override
   public String getAttribute(String name) {
      return "dummy";
   }

   @Override
   public void click() {
      // No-op for testing
   }

   @Override
   public void submit() {
      // No-op for testing
   }

   @Override
   public void clear() {
      // No-op for testing
   }

   @Override
   public void sendKeys(CharSequence... keysToSend) {
      // No-op for testing
   }

   @Override
   public boolean isDisplayed() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public boolean isSelected() {
      return true;
   }
}