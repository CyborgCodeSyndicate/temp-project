package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.selenium.AlertUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent API methods for interacting with Alert UI components.
 *
 * <p>This class encapsulates interactions with Alert elements, allowing retrieval,
 * validation, and visibility checks. It integrates with {@link AlertService} to
 * perform operations in a structured manner.
 *
 * <p>The generic type {@code T} represents the main UI service fluent class that this service extends,
 * allowing method chaining within the fluent API structure.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings("java:S5960")
public class AlertServiceFluent<T extends UiServiceFluent<?>> {

   private final AlertService alertService;
   private final T uiServiceFluent;
   private final Storage storage;
   private final SmartWebDriver driver;

   /**
    * Constructs an instance of {@link AlertServiceFluent}.
    *
    * @param uiServiceFluent The main UI service fluent instance.
    * @param storage         The storage object for maintaining test state.
    * @param alertService    The service handling alert interactions.
    * @param webDriver       The instance of {@link SmartWebDriver}.
    */
   public AlertServiceFluent(T uiServiceFluent, Storage storage, AlertService alertService,
                             SmartWebDriver webDriver) {
      this.alertService = alertService;
      this.uiServiceFluent = uiServiceFluent;
      this.storage = storage;
      this.driver = webDriver;
   }

   /**
    * Retrieves the value of the specified alert element.
    *
    * @param element The alert UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T getValue(final AlertUiElement element) {
      Allure.step(String.format("[UI - Alert] Retrieving value for alert with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      String value = alertService.getValue(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), value);
      return uiServiceFluent;
   }

   /**
    * Validates that the alert element's value matches the expected value.
    *
    * @param element       The alert UI element.
    * @param expectedValue The expected value of the alert.
    * @return The current fluent service instance for method chaining.
    */
   public T validateValue(final AlertUiElement element, String expectedValue) {
      return validateValue(element, expectedValue, false);
   }

   /**
    * Validates that the alert element's value matches the expected value with an option for soft assertions.
    *
    * @param element       The alert UI element.
    * @param expectedValue The expected value of the alert.
    * @param soft          Whether to perform a soft assertion.
    * @return The current fluent service instance for method chaining.
    */
   public T validateValue(final AlertUiElement element, String expectedValue, boolean soft) {
      Allure.step(String.format("[UI - Alert] Validating value for alert with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      String value = alertService.getValue(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), value);
      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(value).as("Validating Alert value")
                     .isEqualTo(expectedValue));
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(value).as("Validating Alert value")
                     .isEqualTo(expectedValue));
      }
   }

   /**
    * Checks if the specified alert element is visible.
    *
    * @param element The alert UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T isVisible(final AlertUiElement element) {
      Allure.step(String.format("[UI - Alert] Checking visibility for alert with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      boolean enabled = alertService.isVisible(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates if the specified alert element is visible.
    *
    * @param element The alert UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsVisible(final AlertUiElement element) {
      return validateIsVisible(element, true, false);
   }

   /**
    * Validates if the specified alert element is visible with an option for soft assertions.
    *
    * @param element The alert UI element.
    * @param soft    Whether to perform a soft assertion.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsVisible(final AlertUiElement element, boolean soft) {
      return validateIsVisible(element, true, soft);
   }

   /**
    * Validates if the specified alert element is visible or hidden based on the provided flag.
    *
    * @param element         The alert UI element.
    * @param shouldBeVisible Whether the element should be visible.
    * @param soft            Whether to perform a soft assertion.
    * @return The current fluent service instance for method chaining.
    */
   private T validateIsVisible(final AlertUiElement element, boolean shouldBeVisible, boolean soft) {
      Allure.step(String.format("[UI - Alert] Validating visibility for alert with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      boolean visible = alertService.isVisible(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), visible);

      String assertionMessage = shouldBeVisible
            ? "Validating Alert is visible"
            : "Validating Alert is hidden";

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> {
                  if (shouldBeVisible) {
                     softAssertions.assertThat(visible).as(assertionMessage).isTrue();
                  } else {
                     softAssertions.assertThat(visible).as(assertionMessage).isFalse();
                  }
               }
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> {
                  if (shouldBeVisible) {
                     Assertions.assertThat(visible).as(assertionMessage).isTrue();
                  } else {
                     Assertions.assertThat(visible).as(assertionMessage).isFalse();
                  }
               }
         );
      }
   }

   /**
    * Validates if the specified alert element is hidden.
    *
    * @param element The alert UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsHidden(final AlertUiElement element) {
      return validateIsVisible(element, false, false);
   }

   /**
    * Validates if the specified alert element is hidden with an option for soft assertions.
    *
    * @param element The alert UI element.
    * @param soft    Whether to perform a soft assertion.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsHidden(final AlertUiElement element, boolean soft) {
      return validateIsVisible(element, false, soft);
   }
}
