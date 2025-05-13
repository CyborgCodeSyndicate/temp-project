package com.bakery.project.ui.elements.bakery;

import com.bakery.project.ui.functions.ContextConsumer;
import com.bakery.project.ui.functions.SharedUi;
import com.bakery.project.ui.functions.SharedUiFunctions;
import com.bakery.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.selenium.ButtonUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;
import org.openqa.selenium.By;

public enum ButtonFields implements ButtonUiElement {


   SIGN_IN_BUTTON(By.tagName("vaadin-button"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_FOR_LOADING),
   NEW_ORDER_BUTTON(By.cssSelector("vaadin-button#action"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         //SharedUI.WAIT_FOR_LOADING), //todo: test the loading
         driver -> SharedUiFunctions.waitForPresence(driver,
               By.cssSelector("vaadin-dialog-overlay#overlay"))), //todo: test if loading doesn't work
   REVIEW_ORDER_BUTTON(By.cssSelector("vaadin-button#review"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         SharedUi.WAIT_TO_BE_REMOVED),
   CANCEL_ORDER_BUTTON(By.cssSelector("vaadin-button#cancel"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_FOR_TIMEOUT,
         SharedUi.WAIT_TO_BE_REMOVED),
   PLACE_ORDER_BUTTON(By.cssSelector("vaadin-button#save"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         SharedUi.WAIT_TO_BE_REMOVED),
   CLEAR_SEARCH(By.cssSelector("vaadin-button#clear"), ButtonFieldTypes.VA_BUTTON_TYPE,
         SharedUi.WAIT_TO_BE_CLICKABLE,
         //SharedUI.WAIT_FOR_LOADING) //todo: test the loading
         SharedUi.WAIT_TO_BE_REMOVED),
   ;

   public static final class Data {

      public static final String SIGN_IN_BUTTON = "SIGN_IN_BUTTON";
      public static final String NEW_ORDER_BUTTON = "NEW_ORDER_BUTTON";


      private Data() {
      }

   }

   private final By locator;
   private final ButtonComponentType componentType;
   private final Consumer<SmartWebDriver> before;
   private final Consumer<SmartWebDriver> after;


   ButtonFields(By locator) {
      this(locator, null, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }


   ButtonFields(By locator, ButtonComponentType componentType) {
      this(locator, componentType, smartWebDriver -> {
      }, smartWebDriver -> {
      });
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                Consumer<SmartWebDriver> before) {
      this(locator, componentType, before, smartWebDriver -> {
      });
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                ContextConsumer before) {
      this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
      });
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                Consumer<SmartWebDriver> before,
                Consumer<SmartWebDriver> after) {
      this.locator = locator;
      this.componentType = componentType;
      this.before = before;
      this.after = after;
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                ContextConsumer before,
                ContextConsumer after) {
      this(locator, componentType, before.asConsumer(locator), after.asConsumer(locator));
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                ContextConsumer before,
                Consumer<SmartWebDriver> after) {
      this(locator, componentType, before.asConsumer(locator), after);
   }


   ButtonFields(By locator,
                ButtonComponentType componentType,
                Consumer<SmartWebDriver> before,
                ContextConsumer after) {
      this(locator, componentType, before, after.asConsumer(locator));
   }


   @Override
   public By locator() {
      return locator;
   }


   @Override
   public <T extends ComponentType> T componentType() {
      return (T) componentType;
   }


   @Override
   public Enum<?> enumImpl() {
      return this;
   }


   @Override
   public Consumer<SmartWebDriver> before() {
      return before;
   }


   @Override
   public Consumer<SmartWebDriver> after() {
      return after;
   }

}
