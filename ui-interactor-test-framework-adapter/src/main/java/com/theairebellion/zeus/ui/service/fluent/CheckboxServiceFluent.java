package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.CheckboxUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent methods for interacting with checkbox UI elements.
 *
 * <p>This class enables structured interactions with checkboxes, supporting actions such as selection,
 * deselection, validation, and state retrieval. It integrates with {@link CheckboxService} to handle
 * operations effectively and allows method chaining for better readability.
 *
 * <p>The generic type {@code T} represents the fluent UI service that extends {@link UiServiceFluent},
 * ensuring method chaining returns the correct instance type.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings("unchecked")
public class CheckboxServiceFluent<T extends UiServiceFluent<?>> implements Insertion {

   private final CheckboxService checkboxService;
   private final T uiServiceFluent;
   private final Storage storage;
   private final SmartWebDriver driver;

   /**
    * Constructs a fluent service for interacting with checkboxes.
    *
    * @param uiServiceFluent The parent UI service fluent instance.
    * @param storage         The storage instance for persisting checkbox states.
    * @param checkboxService The service handling checkbox interactions.
    * @param smartWebDriver  The WebDriver instance used for UI interactions.
    */
   public CheckboxServiceFluent(T uiServiceFluent, Storage storage, CheckboxService checkboxService,
                                SmartWebDriver smartWebDriver) {
      this.checkboxService = checkboxService;
      this.uiServiceFluent = uiServiceFluent;
      this.storage = storage;
      this.driver = smartWebDriver;
   }

   /**
    * Selects the specified checkbox element.
    *
    * @param element The {@link CheckboxUiElement} to be selected.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T select(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Select checkbox with element: " + element);
      checkboxService.select((CheckboxComponentType) element.componentType(), element.locator());
      return uiServiceFluent;
   }

   /**
    * Deselects the specified checkbox element.
    *
    * @param element The {@link CheckboxUiElement} to be deselected.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T deSelect(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Deselect checkbox with element: " + element);
      checkboxService.deSelect((CheckboxComponentType) element.componentType(), element.locator());
      return uiServiceFluent;
   }

   /**
    * Checks if the specified checkbox element is selected.
    *
    * @param element The {@link CheckboxUiElement} to check.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T isSelected(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Check if checkbox with element " + element + " is selected");
      boolean selected = checkboxService.isSelected(element.componentType(), element.locator());
      storage.sub(UI).put(element.enumImpl(), selected);
      return uiServiceFluent;
   }

   /**
    * Validates whether the checkbox is selected.
    *
    * @param element The {@link CheckboxUiElement} to validate.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T validateIsSelected(final CheckboxUiElement element) {
      return validateIsSelected(element, false);
   }

   /**
    * Validates whether the checkbox is selected.
    *
    * @param element The {@link CheckboxUiElement} to validate.
    * @param soft    If {@code true}, performs a soft assertion.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T validateIsSelected(final CheckboxUiElement element, boolean soft) {
      Allure.step("[UI - Checkbox] Validate if checkbox with element " + element + " is selected");
      element.before().accept(driver);
      boolean selected = checkboxService.isSelected(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), selected);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(selected)
                     .as("Validating Checkbox Selected").isTrue()
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(selected)
                     .as("Validating Checkbox Selected").isTrue()
         );
      }
   }

   /**
    * Checks if multiple checkboxes associated with the given {@link CheckboxUiElement} are selected.
    * The selection state is stored in the {@link Storage} for future reference.
    *
    * @param element The {@link CheckboxUiElement} representing the checkbox group or a single checkbox.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T areSelected(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Check if multiple checkboxes with element " + element + " are selected");
      boolean selected =
            checkboxService.areSelected((CheckboxComponentType) element.componentType(), element.locator());
      storage.sub(UI).put(element.enumImpl(), selected);
      return uiServiceFluent;
   }

   /**
    * Checks if the specified checkbox element is enabled.
    *
    * @param element The {@link CheckboxUiElement} to check.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T isEnabled(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Check if checkbox with element " + element + " is enabled");
      boolean enabled = checkboxService.isEnabled(element.componentType(), element.locator());
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates whether the checkbox is enabled.
    *
    * @param element The {@link CheckboxUiElement} to validate.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T validateIsEnabled(final CheckboxUiElement element) {
      return validateIsEnabled(element, false);
   }

   /**
    * Validates whether the checkbox is enabled.
    *
    * @param element The {@link CheckboxUiElement} to validate.
    * @param soft    If {@code true}, performs a soft assertion.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T validateIsEnabled(final CheckboxUiElement element, boolean soft) {
      Allure.step("[UI - Checkbox] Validate if checkbox with element " + element + " is enabled");
      element.before().accept(driver);
      boolean enabled = checkboxService.isEnabled(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(enabled)
                     .as("Validating Checkbox Enabled").isTrue()
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(enabled)
                     .as("Validating Checkbox Enabled").isTrue()
         );
      }
   }

   /**
    * Checks if multiple checkboxes associated with the given {@link CheckboxUiElement} are enabled.
    * The enabled state is stored in the {@link Storage} for future reference.
    *
    * @param element The {@link CheckboxUiElement} representing the checkbox group or a single checkbox.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T areEnabled(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Check if multiple checkboxes with element " + element + " are enabled");
      boolean enabled = checkboxService.areEnabled((CheckboxComponentType) element.componentType(), element.locator());
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Retrieves the selected values from the checkbox.
    *
    * @param element The {@link CheckboxUiElement} to retrieve values from.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T getSelected(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Retrieve the selected values from checkbox " + element);
      List<String> selectedValues = checkboxService.getSelected(element.componentType(), element.locator());
      storage.sub(UI).put(element.enumImpl(), selectedValues);
      return uiServiceFluent;
   }

   /**
    * Retrieves all checkbox values.
    *
    * @param element The {@link CheckboxUiElement} to retrieve all values from.
    * @return The instance of {@link UiServiceFluent} for method chaining.
    */
   public T getAll(final CheckboxUiElement element) {
      Allure.step("[UI - Checkbox] Retrieve all checkbox values from " + element);
      checkboxService.getAll(element.componentType(), element.locator()); //todo: Do we need storage
      return uiServiceFluent;
   }

   /**
    * Inserts a value into a checkbox element.
    *
    * @param componentType The type of the checkbox component.
    * @param locator       The locator of the checkbox element.
    * @param values        The values to be inserted.
    */
   @Override
   public void insertion(final ComponentType componentType, final By locator, final Object... values) {
      Allure.step("[UI - Checkbox] Insert a value into checkbox " + locator + " of type " + componentType);
      checkboxService.insertion(componentType, locator, values);
   }

}
