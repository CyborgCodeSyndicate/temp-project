package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;
import com.theairebellion.zeus.ui.selenium.ToggleUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent methods for interacting with toggle UI elements.
 * This service allows activation, deactivation, and validation of toggles.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings({"java:S5960", "unchecked"})
public class ToggleServiceFluent<T extends UiServiceFluent<?>> {

   private final ToggleService toggleService;
   private final T uiServiceFluent;
   private final Storage storage;
   private final SmartWebDriver driver;

   /**
    * Constructs a {@code ToggleServiceFluent} instance.
    *
    * @param uiServiceFluent The fluent UI service instance.
    * @param storage         The storage instance for storing toggle states.
    * @param toggleService   The toggle service responsible for interacting with toggle components.
    */
   public ToggleServiceFluent(T uiServiceFluent, Storage storage, ToggleService toggleService,
                              SmartWebDriver webDriver) {
      this.uiServiceFluent = uiServiceFluent;
      this.toggleService = toggleService;
      this.storage = storage;
      driver = webDriver;
   }

   /**
    * Activates the specified toggle element.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T activate(final ToggleUiElement element) {
      Allure.step("[UI - Toggle] Activating the toggle element: " + element);
      element.before().accept(driver);
      toggleService.activate(element.componentType(), element.locator());
      element.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Deactivates the specified toggle element.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T deactivate(final ToggleUiElement element) {
      Allure.step("[UI - Toggle] Deactivating the toggle element: " + element);
      element.before().accept(driver);
      toggleService.deactivate(element.componentType(), element.locator());
      element.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Checks if the specified toggle element is enabled.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T isEnabled(final ToggleUiElement element) {
      Allure.step("[UI - Toggle] Checking if the toggle element is enabled: " + element);
      element.before().accept(driver);
      boolean enabled = toggleService.isEnabled(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates that the specified toggle element is enabled.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T validateIsEnabled(final ToggleUiElement element) {
      return validateIsEnabled(element, true, false);
   }

   /**
    * Validates that the specified toggle element is enabled.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @param soft    If true, performs a soft assertion; otherwise, performs a hard assertion.
    * @return The fluent UI service instance.
    */
   public T validateIsEnabled(final ToggleUiElement element, boolean soft) {
      return validateIsEnabled(element, true, soft);
   }

   private T validateIsEnabled(final ToggleUiElement element, boolean shouldBeEnabled, boolean soft) {
      Allure.step("Validating if the toggle " + element + " is " + (shouldBeEnabled ? "enabled" : "disabled"));
      element.before().accept(driver);
      boolean enabled = toggleService.isEnabled(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);

      String assertionMessage = shouldBeEnabled
            ? "Validating toggle is enabled"
            : "Validating toggle is disabled";

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> {
                  if (shouldBeEnabled) {
                     softAssertions.assertThat(enabled).as(assertionMessage).isTrue();
                  } else {
                     softAssertions.assertThat(enabled).as(assertionMessage).isFalse();
                  }
               }
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> {
                  if (shouldBeEnabled) {
                     Assertions.assertThat(enabled).as(assertionMessage).isTrue();
                  } else {
                     Assertions.assertThat(enabled).as(assertionMessage).isFalse();
                  }
               }
         );
      }
   }

   /**
    * Validates that the specified toggle element is disabled.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T validateIsDisabled(final ToggleUiElement element) {
      return validateIsEnabled(element, false, false);
   }

   /**
    * Validates that the specified toggle element is disabled.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @param soft    If true, performs a soft assertion; otherwise, performs a hard assertion.
    * @return The fluent UI service instance.
    */
   public T validateIsDisabled(final ToggleUiElement element, boolean soft) {
      return validateIsEnabled(element, false, soft);
   }

   /**
    * Checks if the specified toggle element is activated.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T isActivated(final ToggleUiElement element) {
      Allure.step("[UI - Toggle] Checking if the toggle element is activated: " + element);
      boolean enabled = toggleService.isActivated(element.componentType(), element.locator());
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates that the specified toggle element is activated.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T validateIsActivated(final ToggleUiElement element) {
      return validateIsActivated(element, true, false);
   }

   /**
    * Validates that the specified toggle element is activated.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @param soft    If true, performs a soft assertion; otherwise, performs a hard assertion.
    * @return The fluent UI service instance.
    */
   public T validateIsActivated(final ToggleUiElement element, boolean soft) {
      return validateIsActivated(element, true, soft);
   }

   private T validateIsActivated(final ToggleUiElement element, boolean shouldBeEnabled, boolean soft) {
      Allure.step("Validating if the toggle " + element + " is " + (shouldBeEnabled ? "activated" : "deactivated"));
      element.before().accept(driver);
      boolean activated = toggleService.isActivated(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), activated);

      String assertionMessage = shouldBeEnabled
            ? "Validating toggle is activated"
            : "Validating toggle is deactivated";

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> {
                  if (shouldBeEnabled) {
                     softAssertions.assertThat(activated).as(assertionMessage).isTrue();
                  } else {
                     softAssertions.assertThat(activated).as(assertionMessage).isFalse();
                  }
               }
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> {
                  if (shouldBeEnabled) {
                     Assertions.assertThat(activated).as(assertionMessage).isTrue();
                  } else {
                     Assertions.assertThat(activated).as(assertionMessage).isFalse();
                  }
               }
         );
      }
   }

   /**
    * Validates that the specified toggle element is deactivated.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @return The fluent UI service instance.
    */
   public T validateIsDeactivated(final ToggleUiElement element) {
      return validateIsActivated(element, false, false);
   }

   /**
    * Validates that the specified toggle element is deactivated.
    *
    * @param element The {@link ToggleUiElement} representing the toggle component.
    * @param soft    If true, performs a soft assertion; otherwise, performs a hard assertion.
    * @return The fluent UI service instance.
    */
   public T validateIsDeactivated(final ToggleUiElement element, boolean soft) {
      return validateIsActivated(element, false, soft);
   }

}
