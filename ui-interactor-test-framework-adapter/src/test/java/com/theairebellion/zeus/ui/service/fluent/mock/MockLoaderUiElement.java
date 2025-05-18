package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.LoaderUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;
import org.openqa.selenium.By;


public class MockLoaderUiElement implements LoaderUiElement {

   private final By locator;
   private final Object enumKey;

   public MockLoaderUiElement(By locator, Object enumKey) {
      this.locator = locator;
      this.enumKey = enumKey;
   }

   @Override
   public By locator() {
      return locator;
   }

   @Override
   public Consumer<SmartWebDriver> before() {
      return w -> {
      };
   }

   @Override
   public Consumer<SmartWebDriver> after() {
      return w -> {
      };
   }

   @Override
   public Enum<?> enumImpl() {
      return MockLoaderComponentType.DUMMY;
   }

   @Override
   public <T extends ComponentType> T componentType() {
      return (T) MockLoaderComponentType.DUMMY;
   }
}
