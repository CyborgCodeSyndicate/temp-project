package com.theairebellion.zeus.ui.components.factory.mock;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

@ImplementationOfType("FAIL")
public class FailInputImpl implements Input {
   private final SmartWebDriver smartWebDriver;

   public FailInputImpl(SmartWebDriver driver) {
      this.smartWebDriver = driver;
      throw new IllegalStateException("Simulated constructor failure!");
   }

   @Override
   public void insert(SmartWebElement container, String value) {
   }

   @Override
   public void insert(SmartWebElement container, String inputFieldLabel, String value) {
   }

   @Override
   public void insert(String inputFieldLabel, String value) {
   }

   @Override
   public void insert(By inputFieldContainerLocator, String value) {
   }

   @Override
   public void clear(SmartWebElement container) {
   }

   @Override
   public void clear(SmartWebElement container, String inputFieldLabel) {
   }

   @Override
   public void clear(String inputFieldLabel) {
   }

   @Override
   public void clear(By inputFieldContainerLocator) {
   }

   @Override
   public String getValue(SmartWebElement container) {
      return "";
   }

   @Override
   public String getValue(SmartWebElement container, String inputFieldLabel) {
      return "";
   }

   @Override
   public String getValue(String inputFieldLabel) {
      return "";
   }

   @Override
   public String getValue(By inputFieldContainerLocator) {
      return "";
   }

   @Override
   public boolean isEnabled(SmartWebElement container) {
      return false;
   }

   @Override
   public boolean isEnabled(SmartWebElement container, String inputFieldLabel) {
      return false;
   }

   @Override
   public boolean isEnabled(String inputFieldLabel) {
      return false;
   }

   @Override
   public boolean isEnabled(By inputFieldContainerLocator) {
      return false;
   }

   @Override
   public String getErrorMessage(SmartWebElement container) {
      return "";
   }

   @Override
   public String getErrorMessage(SmartWebElement container, String inputFieldLabel) {
      return "";
   }

   @Override
   public String getErrorMessage(String inputFieldLabel) {
      return "";
   }

   @Override
   public String getErrorMessage(By inputFieldContainerLocator) {
      return "";
   }
}