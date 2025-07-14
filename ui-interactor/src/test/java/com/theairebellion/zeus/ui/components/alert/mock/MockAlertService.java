package com.theairebellion.zeus.ui.components.alert.mock;

import com.theairebellion.zeus.ui.components.alert.AlertComponentType;
import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockAlertService implements AlertService {

   public static final String VALUE_CONTAINER = "valueContainer";
   public static final String VALUE_LOCATOR = "valueLocator";
   public static final boolean VISIBLE_RESULT = true;

   public AlertComponentType lastComponentTypeUsed;
   public AlertComponentType explicitComponentType;
   public SmartWebElement lastContainer;
   public By lastLocator;

   public MockAlertService() {
      reset();
   }

   private void setLastType(AlertComponentType type) {
      this.explicitComponentType = type;
      if (MockAlertComponentType.DUMMY_ALERT.equals(type)) {
         this.lastComponentTypeUsed = MockAlertComponentType.DUMMY_ALERT;
      } else {
         this.lastComponentTypeUsed = null;
      }
   }

   public void reset() {
      lastComponentTypeUsed = null;
      explicitComponentType = MockAlertComponentType.DUMMY_ALERT;
      lastContainer = null;
      lastLocator = null;
   }

   @Override
   public String getValue(AlertComponentType componentType, SmartWebElement container) {
      setLastType(componentType);
      lastContainer = container;
      return VALUE_CONTAINER;
   }

   @Override
   public String getValue(AlertComponentType componentType, By containerLocator) {
      setLastType(componentType);
      lastLocator = containerLocator;
      return VALUE_LOCATOR;
   }

   @Override
   public boolean isVisible(AlertComponentType componentType, SmartWebElement container) {
      setLastType(componentType);
      lastContainer = container;
      return VISIBLE_RESULT;
   }

   @Override
   public boolean isVisible(AlertComponentType componentType, By containerLocator) {
      setLastType(componentType);
      lastLocator = containerLocator;
      return VISIBLE_RESULT;
   }
}