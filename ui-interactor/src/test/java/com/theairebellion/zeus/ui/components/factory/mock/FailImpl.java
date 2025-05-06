package com.theairebellion.zeus.ui.components.factory.mock;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

@ImplementationOfType("FAIL")
public class FailImpl implements MockInterface {
   private final SmartWebDriver smartWebDriver;

   public FailImpl(SmartWebDriver driver) {
      smartWebDriver = driver;
      throw new IllegalStateException("Failure!");
   }

   @Override
   public Enum<?> getType() {
      return MockComponentType.FAIL;
   }
}