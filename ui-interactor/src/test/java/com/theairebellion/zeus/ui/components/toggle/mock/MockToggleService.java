package com.theairebellion.zeus.ui.components.toggle.mock;

import com.theairebellion.zeus.ui.components.toggle.ToggleComponentType;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;

public class MockToggleService implements ToggleService {

   public ToggleComponentType lastComponentType;
   public SmartWebElement lastContainer;
   public By lastLocator;
   public String lastText;
   public boolean returnBool;
   public List<String> returnOptions = Collections.emptyList();

   public void reset() {
      lastComponentType = null;
      lastContainer = null;
      lastLocator = null;
      lastText = null;
      returnBool = false;
      returnOptions = Collections.emptyList();
   }

   @Override
   public void activate(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
      lastComponentType = componentType;
      lastContainer = container;
      lastText = toggleText;
   }

   @Override
   public void activate(ToggleComponentType componentType, String toggleText) {
      lastComponentType = componentType;
      lastText = toggleText;
   }

   @Override
   public void activate(ToggleComponentType componentType, By toggleLocator) {
      lastComponentType = componentType;
      lastLocator = toggleLocator;
   }

   @Override
   public void deactivate(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
      lastComponentType = componentType;
      lastContainer = container;
      lastText = toggleText;
   }

   @Override
   public void deactivate(ToggleComponentType componentType, String toggleText) {
      lastComponentType = componentType;
      lastText = toggleText;
   }

   @Override
   public void deactivate(ToggleComponentType componentType, By toggleLocator) {
      lastComponentType = componentType;
      lastLocator = toggleLocator;
   }

   @Override
   public boolean isEnabled(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
      lastComponentType = componentType;
      lastContainer = container;
      lastText = toggleText;
      return returnBool;
   }

   @Override
   public boolean isEnabled(ToggleComponentType componentType, String toggleText) {
      lastComponentType = componentType;
      lastText = toggleText;
      return returnBool;
   }

   @Override
   public boolean isEnabled(ToggleComponentType componentType, By toggleLocator) {
      lastComponentType = componentType;
      lastLocator = toggleLocator;
      return returnBool;
   }

   @Override
   public boolean isActivated(ToggleComponentType componentType, SmartWebElement container, String toggleText) {
      lastComponentType = componentType;
      lastContainer = container;
      lastText = toggleText;
      return returnBool;
   }

   @Override
   public boolean isActivated(ToggleComponentType componentType, String toggleText) {
      lastComponentType = componentType;
      lastText = toggleText;
      return returnBool;
   }

   @Override
   public boolean isActivated(ToggleComponentType componentType, By toggleLocator) {
      lastComponentType = componentType;
      lastLocator = toggleLocator;
      return returnBool;
   }
}