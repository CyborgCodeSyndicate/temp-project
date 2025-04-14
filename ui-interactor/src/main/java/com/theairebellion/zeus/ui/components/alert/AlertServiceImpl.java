package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Default implementation of {@link AlertService} that manages retrieval and interactions
 * with various {@link Alert} implementations.
 *
 * <p>This class leverages a cache for created alert components, reducing overhead when
 * performing repeated operations. Each method delegates to a retrieved or newly created
 * {@link Alert} instance appropriate to the given {@link AlertComponentType}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class AlertServiceImpl extends AbstractComponentService<AlertComponentType, Alert>
      implements AlertService {

   /**
    * Constructs a new {@code AlertServiceImpl} with the given WebDriver.
    *
    * @param driver The {@link SmartWebDriver} used for UI interactions.
    */
   public AlertServiceImpl(SmartWebDriver driver) {
      super(driver);
   }

   /**
    * Creates a new {@link Alert} instance for the specified type by calling
    * {@link ComponentFactory#getAlertComponent(AlertComponentType, SmartWebDriver)}.
    *
    * @param componentType The enum constant representing the alert type.
    * @return A new or cached {@link Alert} instance for the given type.
    */
   @Override
   protected Alert createComponent(final AlertComponentType componentType) {
      return ComponentFactory.getAlertComponent(componentType, driver);
   }

   /**
    * Retrieves the value of an alert using the specified component type and a container element.
    *
    * @param componentType The specific alert component type.
    * @param container     The container element housing the alert.
    * @return The text displayed by the alert.
    */
   @Override
   public String getValue(AlertComponentType componentType, SmartWebElement container) {
      LogUi.step("Getting value from alert: " + componentType);
      return alertComponent(componentType).getValue(container);
   }

   /**
    * Retrieves the value of an alert using the specified component type and a locator.
    *
    * @param componentType    The specific alert component type.
    * @param containerLocator A {@link By} locator identifying the alert element.
    * @return The text displayed by the alert.
    */
   @Override
   public String getValue(AlertComponentType componentType, By containerLocator) {
      LogUi.step("Getting value from alert located by: " + containerLocator);
      return alertComponent(componentType).getValue(containerLocator);
   }

   /**
    * Checks if an alert is visible using the specified component type and a container element.
    *
    * @param componentType The specific alert component type.
    * @param container     The container element housing the alert.
    * @return {@code true} if the alert is visible, otherwise {@code false}.
    */
   @Override
   public boolean isVisible(AlertComponentType componentType, SmartWebElement container) {
      LogUi.step("Checking if alert is visible: " + componentType);
      return alertComponent(componentType).isVisible(container);
   }

   /**
    * Checks if an alert is visible using the specified component type and a locator.
    *
    * @param componentType    The specific alert component type.
    * @param containerLocator A {@link By} locator identifying the alert element.
    * @return {@code true} if the alert is visible, otherwise {@code false}.
    */
   @Override
   public boolean isVisible(AlertComponentType componentType, By containerLocator) {
      LogUi.step("Checking if alert is visible at: " + containerLocator);
      return alertComponent(componentType).isVisible(containerLocator);
   }

   /**
    * Retrieves or creates (and caches) the {@link Alert} instance for the specified type.
    *
    * @param componentType The enum constant representing the alert type.
    * @return The alert implementation associated with the given type.
    */
   private Alert alertComponent(final AlertComponentType componentType) {
      return getOrCreateComponent(componentType);
   }

}
