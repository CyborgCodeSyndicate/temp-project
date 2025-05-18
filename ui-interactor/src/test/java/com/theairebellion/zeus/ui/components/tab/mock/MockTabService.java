package com.theairebellion.zeus.ui.components.tab.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.tab.TabComponentType;
import com.theairebellion.zeus.ui.components.tab.TabService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockTabService implements TabService {

   public TabComponentType lastComponentType;
   public SmartWebElement lastContainer;
   public By lastLocator;
   public String lastText;
   public boolean returnBool;

   public void reset() {
      lastComponentType = null;
      lastContainer = null;
      lastLocator = null;
      lastText = null;
      returnBool = false;
   }

   @Override
   public boolean isSelected(TabComponentType componentType, SmartWebElement container, String tabText) {
      lastComponentType = componentType;
      lastContainer = container;
      lastText = tabText;
      return returnBool;
   }

   @Override
   public boolean isSelected(TabComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
      return returnBool;
   }

   @Override
   public boolean isSelected(TabComponentType componentType, String tabText) {
      lastComponentType = componentType;
      lastText = tabText;
      return returnBool;
   }

   @Override
   public boolean isSelected(TabComponentType componentType, By tabLocator) {
      lastComponentType = componentType;
      lastLocator = tabLocator;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> void click(T componentType, SmartWebElement container, String buttonText) {
      lastComponentType = (TabComponentType) componentType;
      lastContainer = container;
      lastText = buttonText;
   }

   @Override
   public <T extends ButtonComponentType> void click(T componentType, SmartWebElement container) {
      lastComponentType = (TabComponentType) componentType;
      lastContainer = container;
   }

   @Override
   public <T extends ButtonComponentType> void click(T componentType, String buttonText) {
      lastComponentType = (TabComponentType) componentType;
      lastText = buttonText;
   }

   @Override
   public <T extends ButtonComponentType> void click(T componentType, By buttonLocator) {
      lastComponentType = (TabComponentType) componentType;
      lastLocator = buttonLocator;
   }

   @Override
   public <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container, String buttonText) {
      lastComponentType = (TabComponentType) componentType;
      lastContainer = container;
      lastText = buttonText;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container) {
      lastComponentType = (TabComponentType) componentType;
      lastContainer = container;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isEnabled(T componentType, String buttonText) {
      lastComponentType = (TabComponentType) componentType;
      lastText = buttonText;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isEnabled(T componentType, By buttonLocator) {
      lastComponentType = (TabComponentType) componentType;
      lastLocator = buttonLocator;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container, String buttonText) {
      lastComponentType = (TabComponentType) componentType;
      lastContainer = container;
      lastText = buttonText;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container) {
      lastComponentType = (TabComponentType) componentType;
      lastContainer = container;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isVisible(T componentType, String buttonText) {
      lastComponentType = (TabComponentType) componentType;
      lastText = buttonText;
      return returnBool;
   }

   @Override
   public <T extends ButtonComponentType> boolean isVisible(T componentType, By buttonLocator) {
      lastComponentType = (TabComponentType) componentType;
      lastLocator = buttonLocator;
      return returnBool;
   }

   @Override
   public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
   }
}
