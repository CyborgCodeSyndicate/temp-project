package com.theairebellion.zeus.ui.components.list.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;

public class MockItemListService implements ItemListService {

   private static final String SELECT_STRATEGY_MOCK_RESULT = "mockSelectStrategy";
   private static final String DESELECT_STRATEGY_MOCK_RESULT = "mockDeSelectStrategy";
   public ItemListComponentType lastComponentTypeUsed;
   public ItemListComponentType explicitComponentType;
   public SmartWebElement lastContainer;
   public By[] lastLocators;
   public String[] lastText;
   public Strategy lastStrategy;
   public boolean returnBool = true;
   public List<String> returnSelectedList = Collections.emptyList();
   public List<String> returnAllList = Collections.emptyList();


   public MockItemListService() {
      reset();
   }

   private void setLastType(ItemListComponentType type) {
      this.explicitComponentType = type;
      if (MockItemListComponentType.DUMMY_LIST.equals(type)) {
         this.lastComponentTypeUsed = MockItemListComponentType.DUMMY_LIST;
      } else {
         this.lastComponentTypeUsed = null;
      }
   }

   public void reset() {
      lastComponentTypeUsed = null;
      explicitComponentType = MockItemListComponentType.DUMMY_LIST;
      lastContainer = null;
      lastLocators = null;
      lastText = null;
      lastStrategy = null;
      returnBool = true;
      returnSelectedList = new ArrayList<>();
      returnAllList = new ArrayList<>();
   }

   @Override
   public final void select(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = itemText;
   }

   @Override
   public final void select(ItemListComponentType componentType, By containerLocator, String... itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = itemText;
   }

   @Override
   public final String select(ItemListComponentType componentType, SmartWebElement container, Strategy strategy) {
      setLastType(componentType);
      lastContainer = container;
      lastStrategy = strategy;
      return SELECT_STRATEGY_MOCK_RESULT;
   }

   @Override
   public final String select(ItemListComponentType componentType, By containerLocator, Strategy strategy) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastStrategy = strategy;
      return SELECT_STRATEGY_MOCK_RESULT;
   }

   @Override
   public final void select(ItemListComponentType componentType, String... itemText) {
      setLastType(componentType);
      lastText = itemText;
   }

   @Override
   public final void select(ItemListComponentType componentType, By... itemListLocator) {
      setLastType(componentType);
      lastLocators = itemListLocator;
   }

   @Override
   public final void deSelect(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = itemText;
   }

   @Override
   public final void deSelect(ItemListComponentType componentType, By containerLocator, String... itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = itemText;
   }

   @Override
   public final String deSelect(ItemListComponentType componentType, SmartWebElement container, Strategy strategy) {
      setLastType(componentType);
      lastContainer = container;
      lastStrategy = strategy;
      return DESELECT_STRATEGY_MOCK_RESULT;
   }

   @Override
   public final String deSelect(ItemListComponentType componentType, By containerLocator, Strategy strategy) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastStrategy = strategy;
      return DESELECT_STRATEGY_MOCK_RESULT;
   }

   @Override
   public final void deSelect(ItemListComponentType componentType, String... itemText) {
      setLastType(componentType);
      lastText = itemText;
   }

   @Override
   public final void deSelect(ItemListComponentType componentType, By... itemListLocator) {
      setLastType(componentType);
      lastLocators = itemListLocator;
   }

   @Override
   public final boolean areSelected(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areSelected(ItemListComponentType componentType, By containerLocator, String... itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areSelected(ItemListComponentType componentType, String... itemText) {
      setLastType(componentType);
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isSelected(ItemListComponentType componentType, String itemText) {
      setLastType(componentType);
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areSelected(ItemListComponentType componentType, By... itemListLocator) {
      setLastType(componentType);
      lastLocators = itemListLocator;
      return returnBool;
   }

   @Override
   public final boolean isSelected(ItemListComponentType componentType, By itemListLocator) {
      setLastType(componentType);
      lastLocators = new By[] {itemListLocator};
      return returnBool;
   }

   @Override
   public final boolean areEnabled(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areEnabled(ItemListComponentType componentType, By containerLocator, String... itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areEnabled(ItemListComponentType componentType, String... itemText) {
      setLastType(componentType);
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isEnabled(ItemListComponentType componentType, String itemText) {
      setLastType(componentType);
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areEnabled(ItemListComponentType componentType, By... itemLocator) {
      setLastType(componentType);
      lastLocators = itemLocator;
      return returnBool;
   }

   @Override
   public final boolean isEnabled(ItemListComponentType componentType, By itemLocator) {
      setLastType(componentType);
      lastLocators = new By[] {itemLocator};
      return returnBool;
   }

   @Override
   public final boolean areVisible(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText) {
      setLastType(componentType);
      lastContainer = container;
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areVisible(ItemListComponentType componentType, By containerLocator, String... itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areVisible(ItemListComponentType componentType, String... itemText) {
      setLastType(componentType);
      lastText = itemText;
      return returnBool;
   }

   @Override
   public final boolean isVisible(ItemListComponentType componentType, String itemText) {
      setLastType(componentType);
      lastText = new String[] {itemText};
      return returnBool;
   }

   @Override
   public final boolean areVisible(ItemListComponentType componentType, By... itemLocator) {
      setLastType(componentType);
      lastLocators = itemLocator;
      return returnBool;
   }

   @Override
   public final boolean isVisible(ItemListComponentType componentType, By itemLocator) {
      setLastType(componentType);
      lastLocators = new By[] {itemLocator};
      return returnBool;
   }

   @Override
   public final List<String> getSelected(ItemListComponentType componentType, SmartWebElement container) {
      setLastType(componentType);
      lastContainer = container;
      return returnSelectedList;
   }

   @Override
   public final List<String> getSelected(ItemListComponentType componentType, By containerLocator) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      return returnSelectedList;
   }

   @Override
   public final List<String> getAll(ItemListComponentType componentType, SmartWebElement container) {
      setLastType(componentType);
      lastContainer = container;
      return returnAllList;
   }

   @Override
   public final List<String> getAll(ItemListComponentType componentType, By containerLocator) {
      setLastType(componentType);
      lastLocators = new By[] {containerLocator};
      return returnAllList;
   }

   @Override
   public final void insertion(ComponentType type, By locator, Object... values) {
      setLastType((ItemListComponentType) type);
      lastLocators = new By[] {locator};
      if (values != null) {
         lastText = Arrays.stream(values).map(String::valueOf).toArray(String[]::new);
      }
   }
}