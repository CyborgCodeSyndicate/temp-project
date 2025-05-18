package com.example.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.example.project.ui.functions.ExpectedConditionsStore.elementToBeClickableCustom;
import static com.example.project.ui.functions.ExpectedConditionsStore.invisibilityOfElementLocatedCustom;
import static com.example.project.ui.functions.ExpectedConditionsStore.visibilityOfElementLocatedCustom;

public class SharedUiFunctions {

   public static void waitForTimeout(SmartWebDriver smartWebDriver) {
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         throw new RuntimeException("Thread was interrupted during sleep", e);
      }
   }

   public static void waitForLoading(SmartWebDriver smartWebDriver) {
      smartWebDriver.getWait().until(
            ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
   }

   public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
   }

   public static void waitToBeClickable(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(elementToBeClickableCustom(locator));
   }

   public static void waitToBeRemoved(SmartWebDriver smartWebDriver, By locator) {
      smartWebDriver.getWait().until(invisibilityOfElementLocatedCustom(locator));
   }
}
