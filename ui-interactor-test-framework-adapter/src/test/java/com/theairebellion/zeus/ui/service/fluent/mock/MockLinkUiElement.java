package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.LinkUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;
import org.openqa.selenium.By;


public class MockLinkUiElement implements LinkUiElement {

   private final By locator;
   private final Object enumKey;

   public MockLinkUiElement(By locator, Object enumKey) {
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
      return MockLinkComponentType.DUMMY;
   }

   @Override
   public <T extends ComponentType> T componentType() {
      return (T) MockLinkComponentType.DUMMY;
   }
}
