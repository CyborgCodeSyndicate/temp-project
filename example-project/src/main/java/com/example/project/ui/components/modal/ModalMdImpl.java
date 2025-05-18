package com.example.project.ui.components.modal;

import com.example.project.ui.types.ModalFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.modal.Modal;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

@ImplementationOfType(ModalFieldTypes.MD_MODAL)
public class ModalMdImpl extends BaseComponent implements Modal {

   private static final By MODAL_CONTAINER_LOCATOR = By.tagName("mat-dialog-container");
   public static final By BUTTONS_CONTAINER_LOCATOR = By.tagName("mat-dialog-actions");
   public static final By TITLE_LOCATOR = new ByChained(By.id("modal-title"), By.tagName("span"));
   public static final By CONFIRMATION_TITLE_LOCATOR = By.id("confirmation-title");
   public static final By SUB_TITLE_LOCATOR = By.id("modal-sub-title");
   public static final By CONTENT_LOCATOR = By.tagName("mat-dialog-content");
   private static final By CLOSE_ICON_LOCATOR = By.className("close-icon");

   public ModalMdImpl(SmartWebDriver driver) {
      super(driver);
   }

   @Override
   public boolean isOpened() {
      driver.waitUntilElementIsShown(MODAL_CONTAINER_LOCATOR, 2);
      return driver.checkNoException(() -> driver.findSmartElement(MODAL_CONTAINER_LOCATOR));
   }

   @Override
   public void clickButton(SmartWebElement container, String buttonText) {
      if (!isOpened()) {
         throw new IllegalStateException("Modal is not opened");
      }
      WebElement modalButton = container.findElements(By.tagName("button")).stream()
            .filter(btn -> btn.findElement(By.className("mat-button-wrapper")).getText().trim()
                  .equalsIgnoreCase(buttonText.trim()))
            .findFirst().orElseThrow(() -> new NotFoundException("Button with text: " + buttonText + " not found"));
      modalButton.click();
      driver.waitUntilElementIsRemoved(MODAL_CONTAINER_LOCATOR, 1);
      //info("Click button with text: " + buttonText + " in modal");
   }

   @Override
   public void clickButton(String buttonText) {
      if (!isOpened()) {
         throw new IllegalStateException("Modal is not opened");
      }
      WebElement buttonContainer = driver.findElement(BUTTONS_CONTAINER_LOCATOR);
      WebElement modalButton = buttonContainer.findElements(By.tagName("button")).stream()
            .filter(btn -> btn.findElement(By.className("mat-button-wrapper")).getText().trim()
                  .equalsIgnoreCase(buttonText.trim()))
            .findFirst().orElseThrow(() -> new NotFoundException("Button with text: " + buttonText + " not found"));
      modalButton.click();
      driver.waitUntilElementIsRemoved(MODAL_CONTAINER_LOCATOR, 1);
      //info("Click button with text: " + buttonText + " in modal");
   }

   @Override
   public void clickButton(By buttonLocator) {
      if (!isOpened()) {
         throw new IllegalStateException("Modal is not opened");
      }
      WebElement modalButton = driver.findElement(buttonLocator);
      modalButton.click();
      driver.waitUntilElementIsRemoved(MODAL_CONTAINER_LOCATOR, 1);
   }

   @Override
   public String getTitle() {
      if (!isOpened()) {
         throw new IllegalStateException("Modal is not opened");
      }

      if (isConfirmationModal()) {
         throw new IllegalStateException("This modal is not having a title");

      }
      return driver.findSmartElement(TITLE_LOCATOR).getText().trim();
   }

   @Override
   public String getBodyText() {
      if (!isOpened()) {
         throw new IllegalStateException("Modal is not opened");
      }
      SmartWebElement contentElement = driver.findSmartElement(CONTENT_LOCATOR);
      return contentElement.findSmartElement(By.tagName("p")).getText().trim();
   }

   @Override
   public String getContentTitle() {
      if (!isOpened()) {
         throw new IllegalStateException("Modal is not opened");
      }
      return isConfirmationModal()
            ? driver.findSmartElement(CONFIRMATION_TITLE_LOCATOR).getText().trim()
            : driver.findSmartElement(SUB_TITLE_LOCATOR).getText().trim();
   }

   @Override
   public void close() {
      if (!isOpened()) {
         throw new IllegalStateException("Dialog is not opened!");
      }

      WebElement closeIcon = driver.findSmartElement(CLOSE_ICON_LOCATOR);
      closeIcon.click();
      driver.waitUntilElementIsRemoved(MODAL_CONTAINER_LOCATOR, 1);
      //info("Click close icon in dialog");
   }

   private boolean isConfirmationModal() {
      return driver.checkNoException(() -> driver.findSmartElement(CONFIRMATION_TITLE_LOCATOR));
   }
}
