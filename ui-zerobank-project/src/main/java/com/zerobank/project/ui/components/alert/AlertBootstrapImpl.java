package com.zerobank.project.ui.components.alert;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.alert.Alert;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.zerobank.project.ui.types.AlertFieldTypes;
import org.openqa.selenium.By;


@ImplementationOfType(AlertFieldTypes.BOOTSTRAP_ALERT)
public class AlertBootstrapImpl extends BaseComponent implements Alert {

   private static final By ALERT_CONTAINER_LOCATOR = By.className("alert");


   public AlertBootstrapImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public String getValue(final SmartWebElement container) {
      return container.findSmartElement(ALERT_CONTAINER_LOCATOR).getText();
   }


   @Override
   public String getValue(final By containerLocator) {
      return driver.findSmartElement(containerLocator).getText();
   }


   @Override
   public boolean isVisible(final SmartWebElement container) {
      return driver.checkNoException(() -> container.findSmartElement(ALERT_CONTAINER_LOCATOR));
   }


   @Override
   public boolean isVisible(final By containerLocator) {
      return driver.checkNoException(() -> driver.findSmartElement(containerLocator));
   }
}