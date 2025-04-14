package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.InputUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent methods for interacting with input elements in a UI test automation framework.
 *
 * <p>This class enables structured interactions with input fields, supporting actions such as inserting,
 * clearing, retrieving, and validating input values. It also allows checking input element states,
 * including enabled/disabled states and error messages.
 *
 * <p>The generic type {@code T} represents the fluent UI service that extends {@link UiServiceFluent},
 * ensuring method chaining returns the correct instance type.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings("unchecked")
public class InputServiceFluent<T extends UiServiceFluent<?>> implements Insertion {

   private final InputService inputService;
   private final T uiServiceFluent;
   private final Storage storage;
   private final SmartWebDriver driver;

   /**
    * Constructs an instance of {@link InputServiceFluent}.
    *
    * @param uiServiceFluent The UI service fluent instance for chaining method calls.
    * @param storage         The storage instance for persisting input-related data.
    * @param inputService    The service handling input interactions.
    * @param webDriver       The smart WebDriver instance for executing Selenium operations.
    */
   public InputServiceFluent(T uiServiceFluent, Storage storage, InputService inputService,
                             SmartWebDriver webDriver) {
      this.inputService = inputService;
      this.uiServiceFluent = uiServiceFluent;
      this.storage = storage;
      driver = webDriver;
   }

   /**
    * Inserts the specified value into the given input element.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @param value   The value to insert.
    * @return The UI service fluent instance for method chaining.
    */
   public T insert(final InputUiElement element, final String value) {
      Allure.step("[UI - Input] Insert value '" + value + "' into input element: " + element);
      element.before().accept(driver);
      inputService.insert(element.componentType(), element.locator(), value);
      element.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Clears the content of the given input element.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @return The UI service fluent instance for method chaining.
    */
   public T clear(final InputUiElement element) {
      Allure.step("[UI - Input] Clear input element: " + element);
      element.before().accept(driver);
      inputService.clear(element.componentType(), element.locator());
      element.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Retrieves the current value from the given input element and stores it in {@link Storage}.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @return The UI service fluent instance for method chaining.
    */
   public T getValue(final InputUiElement element) {
      Allure.step("[UI - Input] Get value from input element: " + element);
      element.before().accept(driver);
      String value = inputService.getValue(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), value);
      return uiServiceFluent;
   }

   /**
    * Validates that the given input element contains the expected value.
    *
    * @param element       The {@link InputUiElement} representing the input field.
    * @param expectedValue The expected value to validate.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateValue(final InputUiElement element, String expectedValue) {
      return validateValue(element, expectedValue, false);
   }

   /**
    * Validates that the given input element contains the expected value, with an option for soft assertions.
    *
    * @param element       The {@link InputUiElement} representing the input field.
    * @param expectedValue The expected value to validate.
    * @param soft          Whether to use soft assertions.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateValue(final InputUiElement element, String expectedValue, boolean soft) {
      Allure.step("[UI - Input] Validate value of input element: " + element + " is equal to: " + expectedValue);
      element.before().accept(driver);
      String value = inputService.getValue(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), value);
      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(value).as("Validating Input value")
                     .isEqualTo(expectedValue));
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(value).as("Validating Input value")
                     .isEqualTo(expectedValue));
      }
   }

   /**
    * Checks if the given input element is enabled.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @return The UI service fluent instance for method chaining.
    */
   public T isEnabled(final InputUiElement element) {
      Allure.step("[UI - Input] Check if input element is enabled: " + element);
      element.before().accept(driver);
      boolean enabled = inputService.isEnabled(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates that the input element is enabled.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateIsEnabled(final InputUiElement element) {
      return validateIsEnabled(element, true, false);
   }

   /**
    * Validates that the given input element is enabled or disabled, with an option for soft assertions.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @param soft    Whether to use soft assertions.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateIsEnabled(final InputUiElement element, boolean soft) {
      return validateIsEnabled(element, true, soft);
   }

   private T validateIsEnabled(final InputUiElement element, boolean shouldBeEnabled, boolean soft) {
      Allure.step(
            "[UI - Input] Validate if input element " + element + " is " + (shouldBeEnabled ? "enabled" : "disabled"));
      element.before().accept(driver);
      boolean enabled = inputService.isEnabled(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);

      String assertionMessage = shouldBeEnabled
            ? "Validating Input is enabled"
            : "Validating Input is disabled";

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
    * Validates that the given input element is disabled.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateIsDisabled(final InputUiElement element) {
      return validateIsEnabled(element, false, false);
   }

   /**
    * Validates that the given input element is disabled, with an option for soft assertions.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @param soft    Whether to use soft assertions.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateIsDisabled(final InputUiElement element, boolean soft) {
      return validateIsEnabled(element, false, soft);
   }

   /**
    * Retrieves and stores the error message associated with the given input element.
    *
    * @param element The {@link InputUiElement} representing the input field.
    * @return The UI service fluent instance for method chaining.
    */
   public T getErrorMessage(final InputUiElement element) {
      Allure.step("[UI - Input] Get error message for input element: " + element);
      element.before().accept(driver);
      String errorMessage = inputService.getErrorMessage(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), errorMessage);
      return uiServiceFluent;
   }

   /**
    * Validates that the input element displays the expected error message.
    *
    * @param element         The {@link InputUiElement} representing the input field.
    * @param expectedMessage The expected error message.
    * @return The UI service fluent instance for method chaining.
    */
   public T validateErrorMessage(final InputUiElement element, String expectedMessage) {
      return validateErrorMessage(element, expectedMessage, false);
   }

   /**
    * Validates that the error message displayed for the given input element matches the expected value.
    *
    * @param element         The {@link InputUiElement} representing the input field.
    * @param expectedMessage The expected error message.
    * @param soft            Whether to use soft assertions (non-blocking validation).
    * @return The UI service fluent instance for method chaining.
    */
   public T validateErrorMessage(final InputUiElement element, String expectedMessage, boolean soft) {
      Allure.step(
            "[UI - Input] Validate error message for input element: " + element + " is equal to: " + expectedMessage);
      element.before().accept(driver);
      String errorMessage = inputService.getErrorMessage(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), errorMessage);
      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(errorMessage)
                     .as("Validating UI Message")
                     .isEqualTo(expectedMessage));
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(errorMessage).as("Validating UI Message")
                     .isEqualTo(expectedMessage));
      }
   }

   /**
    * Inserts a value into an input field using the component type and locator.
    *
    * @param componentType The component type of the input field.
    * @param locator       The locator of the input field.
    * @param values        The values to be inserted.
    */
   @Override
   public void insertion(final ComponentType componentType, final By locator, final Object... values) {
      Allure.step("[UI - Input] Insert values into input field with locator: " + locator);
      inputService.insertion(componentType, locator, values);
   }
}
