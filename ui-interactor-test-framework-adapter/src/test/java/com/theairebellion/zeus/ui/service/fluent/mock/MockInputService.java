package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockInputService implements InputService {

   public InputComponentType lastComponentType;
   public SmartWebElement lastContainer;
   public String lastValue;
   public String lastLabel;
   public By lastLocator;
   public boolean returnEnabled = true;
   public String returnValue = "";
   public String returnErrorMessage = "";
   public SmartWebElement tableCell;
   public String[] tableInsertionValues;
   public SmartWebElement headerCell;
   public FilterStrategy filterStrategy;
   public String[] filterValues;

   public void reset() {
      lastComponentType = null;
      lastContainer = null;
      lastValue = null;
      lastLabel = null;
      lastLocator = null;
      returnEnabled = true;
      returnValue = "";
      returnErrorMessage = "";
      tableCell = null;
      tableInsertionValues = null;
      headerCell = null;
      filterStrategy = null;
      filterValues = null;
   }

   @Override
   public void insert(InputComponentType componentType, SmartWebElement container, String value) {
      lastComponentType = componentType;
      lastContainer = container;
      lastValue = value;
   }

   @Override
   public void insert(InputComponentType componentType, SmartWebElement container, String inputFieldLabel, String value) {
      lastComponentType = componentType;
      lastContainer = container;
      lastLabel = inputFieldLabel;
      lastValue = value;
   }

   @Override
   public void insert(InputComponentType componentType, String inputFieldLabel, String value) {
      lastComponentType = componentType;
      lastLabel = inputFieldLabel;
      lastValue = value;
   }

   @Override
   public void insert(InputComponentType componentType, By inputFieldContainerLocator, String value) {
      lastComponentType = componentType;
      lastLocator = inputFieldContainerLocator;
      lastValue = value;
   }

   @Override
   public void clear(InputComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
   }

   @Override
   public void clear(InputComponentType componentType, SmartWebElement container, String inputFieldLabel) {
      lastComponentType = componentType;
      lastContainer = container;
      lastLabel = inputFieldLabel;
   }

   @Override
   public void clear(InputComponentType componentType, String inputFieldLabel) {
      lastComponentType = componentType;
      lastLabel = inputFieldLabel;
   }

   @Override
   public void clear(InputComponentType componentType, By inputFieldContainerLocator) {
      lastComponentType = componentType;
      lastLocator = inputFieldContainerLocator;
   }

   @Override
   public String getValue(InputComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
      return returnValue;
   }

   @Override
   public String getValue(InputComponentType componentType, SmartWebElement container, String inputFieldLabel) {
      lastComponentType = componentType;
      lastContainer = container;
      lastLabel = inputFieldLabel;
      return returnValue;
   }

   @Override
   public String getValue(InputComponentType componentType, String inputFieldLabel) {
      lastComponentType = componentType;
      lastLabel = inputFieldLabel;
      return returnValue;
   }

   @Override
   public String getValue(InputComponentType componentType, By inputFieldContainerLocator) {
      lastComponentType = componentType;
      lastLocator = inputFieldContainerLocator;
      return returnValue;
   }

   @Override
   public boolean isEnabled(InputComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
      return returnEnabled;
   }

   @Override
   public boolean isEnabled(InputComponentType componentType, SmartWebElement container, String inputFieldLabel) {
      lastComponentType = componentType;
      lastContainer = container;
      lastLabel = inputFieldLabel;
      return returnEnabled;
   }

   @Override
   public boolean isEnabled(InputComponentType componentType, String inputFieldLabel) {
      lastComponentType = componentType;
      lastLabel = inputFieldLabel;
      return returnEnabled;
   }

   @Override
   public boolean isEnabled(InputComponentType componentType, By inputFieldContainerLocator) {
      lastComponentType = componentType;
      lastLocator = inputFieldContainerLocator;
      return returnEnabled;
   }

   @Override
   public String getErrorMessage(InputComponentType componentType, SmartWebElement container) {
      lastComponentType = componentType;
      lastContainer = container;
      return returnErrorMessage;
   }

   @Override
   public String getErrorMessage(InputComponentType componentType, SmartWebElement container, String inputFieldLabel) {
      lastComponentType = componentType;
      lastContainer = container;
      lastLabel = inputFieldLabel;
      return returnErrorMessage;
   }

   @Override
   public String getErrorMessage(InputComponentType componentType, String inputFieldLabel) {
      lastComponentType = componentType;
      lastLabel = inputFieldLabel;
      return returnErrorMessage;
   }

   @Override
   public String getErrorMessage(InputComponentType componentType, By inputFieldContainerLocator) {
      lastComponentType = componentType;
      lastLocator = inputFieldContainerLocator;
      return returnErrorMessage;
   }

   @Override
   public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
      tableCell = cellElement;
      tableInsertionValues = values;
   }

   @Override
   public void tableFilter(SmartWebElement headerCellElement, ComponentType componentType, FilterStrategy filterStrategy, String... values) {
      headerCell = headerCellElement;
      this.filterStrategy = filterStrategy;
      filterValues = values;
   }

   @Override
   public void insertion(ComponentType componentType, By locator, Object... values) {
      lastComponentType = (InputComponentType) componentType;
      lastLocator = locator;
      if (values != null && values.length > 0) {
         lastValue = String.valueOf(values[0]);
      }
   }
}