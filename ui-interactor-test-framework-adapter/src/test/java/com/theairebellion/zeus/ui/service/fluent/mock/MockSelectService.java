package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;

public class MockSelectService implements SelectService {

   public SelectComponentType lastComponentType;
   public SmartWebElement lastContainer;
   public By lastLocator;
   public String[] lastValues;
   public Strategy lastStrategy;
   public List<String> returnOptions = Collections.emptyList();
   public boolean returnBool = false;

   public void reset() {
      lastComponentType = null;
      lastContainer = null;
      lastLocator = null;
      lastValues = null;
      lastStrategy = null;
      returnOptions = Collections.emptyList();
      returnBool = false;
   }

   @Override
   public void selectOptions(SelectComponentType componentType, SmartWebElement container, String... values) {
      lastComponentType = componentType;
      lastContainer = container;
      lastValues = values;
   }

   @Override
   public void selectOption(SelectComponentType componentType, SmartWebElement container, String value) {
      lastComponentType = componentType;
      lastContainer = container;
      lastValues = new String[] {value};
   }

   @Override
   public void selectOptions(SelectComponentType componentType, By containerLocator, String... values) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      lastValues = values;
   }

   @Override
   public void selectOption(SelectComponentType componentType, By containerLocator, String value) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      lastValues = new String[] {value};
   }

   @Override
   public List<String> selectOptions(SelectComponentType componentType, SmartWebElement container, Strategy strategy) {
      lastComponentType = componentType;
      lastContainer = container;
      lastStrategy = strategy;
      return returnOptions;
   }

   @Override
   public List<String> selectOptions(SelectComponentType componentType, By containerLocator, Strategy strategy) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      lastStrategy = strategy;
      return returnOptions;
   }

   @Override
   public List<String> getAvailableOptions(SelectComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
      return returnOptions;
   }

   @Override
   public List<String> getAvailableOptions(SelectComponentType componentType, By containerLocator) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      return returnOptions;
   }

   @Override
   public List<String> getSelectedOptions(SelectComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
      return returnOptions;
   }

   @Override
   public List<String> getSelectedOptions(SelectComponentType componentType, By containerLocator) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      return returnOptions;
   }

   @Override
   public boolean isOptionVisible(SelectComponentType componentType, SmartWebElement container, String value) {
      lastComponentType = componentType;
      lastContainer = container;
      lastValues = new String[] {value};
      return returnBool;
   }

   @Override
   public boolean isOptionVisible(SelectComponentType componentType, By containerLocator, String value) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      lastValues = new String[] {value};
      return returnBool;
   }

   @Override
   public boolean isOptionEnabled(SelectComponentType componentType, SmartWebElement container, String value) {
      lastComponentType = componentType;
      lastContainer = container;
      lastValues = new String[] {value};
      return returnBool;
   }

   @Override
   public boolean isOptionEnabled(SelectComponentType componentType, By containerLocator, String value) {
      lastComponentType = componentType;
      lastLocator = containerLocator;
      lastValues = new String[] {value};
      return returnBool;
   }

   @Override
   public void insertion(ComponentType componentType, By locator, Object... values) {
      lastComponentType = (SelectComponentType) componentType;
      lastLocator = locator;
      if (values != null && values.length > 0) {
         String[] strValues = new String[values.length];
         for (int i = 0; i < values.length; i++) {
            strValues[i] = String.valueOf(values[i]);
         }
         lastValues = strValues;
      }
   }
}