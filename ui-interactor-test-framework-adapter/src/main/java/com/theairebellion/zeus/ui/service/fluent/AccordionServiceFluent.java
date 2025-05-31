package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;
import com.theairebellion.zeus.ui.components.accordion.AccordionService;
import com.theairebellion.zeus.ui.selenium.AccordionUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent API methods for interacting with Accordion UI components.
 *
 * <p>This class encapsulates interactions with Accordion elements, allowing actions such as expansion,
 * collapsing, retrieving states, and verifying their properties. It integrates with
 * {@link AccordionService} to perform operations in a structured manner.
 *
 * <p>The generic type {@code T} represents the main UI service fluent class that this service extends,
 * allowing method chaining within the fluent API structure.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings({"java:S5960", "unchecked"})
public class AccordionServiceFluent<T extends UiServiceFluent<?>> {

   private static final String VALIDATING_ACCORDIONS = "Validating Accordions";
   private final AccordionService accordionService;
   private final T uiServiceFluent;
   private final Storage storage;
   private final SmartWebDriver driver;

   /**
    * Constructs an instance of {@link AccordionServiceFluent}.
    *
    * @param uiServiceFluent  The main UI service fluent instance.
    * @param storage          The storage object for maintaining test state.
    * @param accordionService The service handling accordion interactions.
    * @param webDriver        The instance of {@link SmartWebDriver}.
    */
   public AccordionServiceFluent(T uiServiceFluent, Storage storage, AccordionService accordionService,
                                 SmartWebDriver webDriver) {
      this.accordionService = accordionService;
      this.uiServiceFluent = uiServiceFluent;
      this.storage = storage;
      this.driver = webDriver;
   }

   /**
    * Expands the specified accordion panel.
    *
    * @param element The accordion UI element to expand.
    * @return The current fluent service instance for method chaining.
    */
   public T expand(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Expanding accordion with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      accordionService.expand((AccordionComponentType) element.componentType(), element.locator());
      element.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Collapses the specified accordion panel.
    *
    * @param element The accordion UI element to collapse.
    * @return The current fluent service instance for method chaining.
    */
   public T collapse(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Collapsing accordion with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      accordionService.collapse((AccordionComponentType) element.componentType(), element.locator());
      element.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Checks if the specified accordion element is enabled.
    *
    * @param element The accordion UI element to check.
    * @return The current fluent service instance for method chaining.
    */
   public T areEnabled(final AccordionUiElement element, String... values) {
      Allure.step(String.format("[UI - Accordion] Checking if accordion is enabled with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      boolean enabled = accordionService.areEnabled((AccordionComponentType) element.componentType(), values);
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates that the accordion elements are enabled.
    *
    * @param element The accordion UI element.
    * @param values  The values to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateAreEnabled(final AccordionUiElement element, final String... values) {
      Allure.step("[UI - Accordion] Validate if accordions are enabled: " + Arrays.toString(values));
      return validateAreEnabled(element, true, false, values);
   }

   /**
    * Validates that the accordion elements are enabled.
    *
    * @param element The accordion UI element.
    * @param soft    A boolean indicating whether the validation should be performed softly.
    *                If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param values  The values to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateAreEnabled(final AccordionUiElement element, boolean soft, final String... values) {
      Allure.step("[UI - Accordion] Validate if accordions are enabled (soft: " + soft + "): "
            + Arrays.toString(values));
      return validateAreEnabled(element, true, soft, values);
   }

   private T validateAreEnabled(final AccordionUiElement element, boolean shouldBeEnabled, boolean soft,
                                final String... values) {
      element.before().accept(driver);
      boolean enabled = accordionService.areEnabled((AccordionComponentType) element.componentType(), values);
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);

      String assertionMessage = shouldBeEnabled
            ? "Validating accordions are enabled"
            : "Validating accordions are disabled";

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
    * Validates that the accordion elements are disabled.
    *
    * @param element The accordion UI element.
    * @param values  The values to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateAreDisabled(final AccordionUiElement element, final String... values) {
      Allure.step("[UI - Accordion] Validate if accordions are disabled: " + Arrays.toString(values));
      return validateAreEnabled(element, false, false, values);
   }

   /**
    * Validates that the accordion elements are disabled.
    *
    * @param element The accordion UI element.
    * @param soft    A boolean indicating whether the validation should be performed softly.
    *                If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param values  The values to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateAreDisabled(final AccordionUiElement element, boolean soft, final String... values) {
      Allure.step("[UI - Accordion] Validate if accordions are disabled (soft: " + soft + "): "
            + Arrays.toString(values));
      return validateAreEnabled(element, false, soft, values);
   }

   /**
    * Checks if specified accordion is enabled, using the given accordion component type.
    *
    * @param element The accordion UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T isEnabled(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Checking if accordion is enabled with componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      boolean enabled = accordionService.isEnabled(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), enabled);
      return uiServiceFluent;
   }

   /**
    * Validates that the accordion element is enabled.
    *
    * @param element The accordion UI element.
    * @param value   The value to validate
    *                * @return The current fluent service instance for method chaining.
    */
   public T validateIsEnabled(final AccordionUiElement element, final String value) {
      Allure.step("[UI - Accordion] Validate that the accordion is enabled");
      return validateAreEnabled(element, true, false, value);
   }

   /**
    * Validates that the accordion element is enabled.
    *
    * @param element The accordion UI element.
    * @param soft    A boolean indicating whether the validation should be performed softly.
    *                If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param value   The value to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsEnabled(final AccordionUiElement element, boolean soft, final String value) {
      Allure.step("[UI - Accordion] Validate that the accordion is enabled (soft: " + soft + "): " + value);
      return validateAreEnabled(element, true, soft, value);
   }

   /**
    * Validates that the accordion element is disabled.
    *
    * @param element The accordion UI element.
    * @param value   The value to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsDisabled(final AccordionUiElement element, final String value) {
      Allure.step("[UI - Accordion] Validate that the accordion is disabled");
      return validateAreEnabled(element, false, false, value);
   }

   /**
    * Validates that the accordion element is disabled.
    *
    * @param element The accordion UI element.
    * @param soft    A boolean indicating whether the validation should be performed softly.
    *                If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param value   The value to validate.
    * @return The current fluent service instance for method chaining.
    */
   public T validateIsDisabled(final AccordionUiElement element, boolean soft, final String value) {
      Allure.step("[UI - Accordion] Validate that the accordion is disabled (soft: " + soft + "): " + value);
      return validateAreEnabled(element, false, soft, value);
   }

   /**
    * Retrieves a list of currently expanded accordions for the given Accordion UI element.
    *
    * @param element The accordion UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T getExpanded(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Retrieve a list of currently expanded accordions for "
            + "componentType: %s", element.componentType()));
      element.before().accept(driver);
      List<String> expanded = accordionService.getExpanded(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), expanded);
      return uiServiceFluent;
   }

   /**
    * Validates that the specified accordion elements are expanded for the given Accordion UI element.
    *
    * @param element        The {@link AccordionUiElement} representing the Accordion UI component.
    * @param expectedValues The expected values that should be expanded.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateExpandedItems(final AccordionUiElement element, final String... expectedValues) {
      Allure.step("[UI - Accordion] Validate that the accordions are expanded");
      return validateExpandedItems(element, false, expectedValues);
   }

   /**
    * Validates that the specified accordion elements are expanded for the given Accordion UI element.
    *
    * @param element        The {@link AccordionUiElement} representing the Accordion UI component.
    * @param soft           A boolean indicating whether the validation should be performed softly.
    *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param expectedValues The expected values that should be expanded.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateExpandedItems(final AccordionUiElement element, boolean soft, final String... expectedValues) {
      Allure.step("[UI - Accordion] Validate that the accordions are expanded (soft: " + soft + "): "
            + Arrays.toString(expectedValues));
      element.before().accept(driver);
      List<String> expandedItems = accordionService.getExpanded(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), expandedItems);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(expandedItems)
                     .as(VALIDATING_ACCORDIONS)
                     .containsAll(Arrays.asList(expectedValues))
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(expandedItems)
                     .as(VALIDATING_ACCORDIONS)
                     .containsAll(Arrays.asList(expectedValues))
         );
      }
   }

   /**
    * Retrieves a list of currently collapsed accordions for the given Accordion UI element.
    *
    * @param element The accordion UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T getCollapsed(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Retrieve a list of currently collapsed accordions for "
            + "componentType: %s", element.componentType()));
      element.before().accept(driver);
      List<String> collapsed = accordionService.getCollapsed(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), collapsed);
      return uiServiceFluent;
   }

   /**
    * Validates that the specified accordion elements are collapsed for the given Accordion UI element.
    *
    * @param element        The {@link AccordionUiElement} representing the Accordion UI component.
    * @param expectedValues The expected values that should be collapsed.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateCollapsedItems(final AccordionUiElement element, final String... expectedValues) {
      Allure.step("[UI - Accordion] Validate that the accordions are collapsed");
      return validateCollapsedItems(element, false, expectedValues);
   }

   /**
    * Validates that the specified accordion elements are collapsed for the given Accordion UI element.
    *
    * @param element        The {@link AccordionUiElement} representing the Accordion UI component.
    * @param soft           A boolean indicating whether the validation should be performed softly.
    *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param expectedValues The expected values that should be collapsed.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateCollapsedItems(final AccordionUiElement element, boolean soft, final String... expectedValues) {
      Allure.step("[UI - Accordion] Validate that the accordions are collapsed (soft: " + soft + "): "
            + Arrays.toString(expectedValues));
      element.before().accept(driver);
      List<String> collapsedItems = accordionService.getCollapsed(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), collapsedItems);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(collapsedItems)
                     .as(VALIDATING_ACCORDIONS)
                     .containsAll(Arrays.asList(expectedValues))
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(collapsedItems)
                     .as(VALIDATING_ACCORDIONS)
                     .containsAll(Arrays.asList(expectedValues))
         );
      }
   }

   /**
    * Retrieves all available accordions.
    *
    * @param element The Accordion UI element.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T getAll(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Retrieve all the accordions for componentType: %s",
            element.componentType()));
      element.before().accept(driver);
      List<String> allAccordions = accordionService.getAll(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), allAccordions);
      return uiServiceFluent;
   }

   /**
    * Validates that all expected accordions are present for the given accordion UI element.
    *
    * @param element        The {@link AccordionUiElement} representing the Accordion UI component.
    * @param expectedValues The expected values that should be present.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateAllAccordions(final AccordionUiElement element, final String... expectedValues) {
      Allure.step("[UI - Accordion] Validate that all expected accordions are present");
      return validateAllAccordions(element, false, expectedValues);
   }

   /**
    * Validates that all expected accordions are present for the given accordion UI element.
    *
    * @param element        The {@link AccordionUiElement} representing the Accordion UI component.
    * @param soft           A boolean indicating whether the validation should be performed softly.
    *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param expectedValues The expected values that should be present.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateAllAccordions(final AccordionUiElement element, boolean soft, final String... expectedValues) {
      Allure.step("[UI - Accordion] Validate that all expected accordions are present (soft: " + soft + "): "
            + Arrays.toString(expectedValues));
      element.before().accept(driver);
      List<String> allItems = accordionService.getAll(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), allItems);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(expectedValues)
                     .as(VALIDATING_ACCORDIONS).containsAll(Arrays.asList(expectedValues))
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(expectedValues)
                     .as(VALIDATING_ACCORDIONS).containsAll(Arrays.asList(expectedValues))
         );
      }
   }

   /**
    * Retrieves the title of the specified accordion element.
    *
    * @param element The accordion UI element.
    * @return The current fluent service instance for method chaining.
    */
   public T getTitle(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Getting title for componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      String title = accordionService.getTitle(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), title);
      return uiServiceFluent;
   }

   /**
    * Validates the title of the specified accordion.
    *
    * @param element       The {@link AccordionUiElement} representing the accordion UI component.
    * @param expectedValue The expected value of the accordion title.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateTitle(final AccordionUiElement element, final String expectedValue) {
      Allure.step("[UI - Accordion] Validate accordion title has value: " + expectedValue);
      return validateTitle(element, false, expectedValue);
   }

   /**
    * Validates the title of the specified accordion.
    *
    * @param element       The {@link AccordionUiElement} representing the accordion UI component.
    * @param soft          A boolean indicating whether the validation should be performed softly.
    *                      If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param expectedValue The expected value of the accordion title.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateTitle(final AccordionUiElement element, boolean soft, final String expectedValue) {
      Allure.step("[UI - Accordion] Validate accordion title has value: " + expectedValue
            + " (soft: " + soft + ")");
      element.before().accept(driver);
      String title = accordionService.getTitle(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), title);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(title)
                     .as(VALIDATING_ACCORDIONS).isEqualTo(expectedValue)
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(title)
                     .as(VALIDATING_ACCORDIONS).isEqualTo(expectedValue)
         );
      }
   }

   /**
    * Retrieves the text content of the specified accordion element.
    *
    * @param element The accordion UI element.
    * @return The current fluent service instance for method chaining.+
    */
   public T getText(final AccordionUiElement element) {
      Allure.step(String.format("[UI - Accordion] Getting text for componentType: %s, locator: %s",
            element.componentType(), element.locator()));
      element.before().accept(driver);
      String text = accordionService.getText(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), text);
      return uiServiceFluent;
   }

   /**
    * Validates the content for the specified accordion.
    *
    * @param element       The {@link AccordionUiElement} representing the accordion UI component.
    * @param expectedValue The expected content for the specified accordion.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateText(final AccordionUiElement element, final String expectedValue) {
      Allure.step("[UI - Accordion] Validate text content of the accordion with type: " + element.componentType());
      return validateTitle(element, false, expectedValue);
   }

   /**
    * Validates the text content for the specified accordion.
    *
    * @param element         The {@link AccordionUiElement} representing the accordion UI component.
    * @param soft            A boolean indicating whether the validation should be performed softly.
    *                        If {@code true}, failures will be collected rather than throwing an exception immediately.
    * @param expectedContent The expected text content for the specified accordion.
    * @return The fluent UI service instance, allowing for method chaining.
    */
   public T validateText(final AccordionUiElement element, boolean soft, final String expectedContent) {
      Allure.step("[UI - Accordion] Validate text content of the accordion with type: " + element.componentType()
            + " (soft: " + soft + ")");
      element.before().accept(driver);
      String textContent = accordionService.getText(element.componentType(), element.locator());
      element.after().accept(driver);
      storage.sub(UI).put(element.enumImpl(), textContent);

      if (soft) {
         return (T) uiServiceFluent.validate(
               softAssertions -> softAssertions.assertThat(textContent)
                     .as(VALIDATING_ACCORDIONS).isEqualTo(expectedContent)
         );
      } else {
         return (T) uiServiceFluent.validate(
               () -> Assertions.assertThat(textContent)
                     .as(VALIDATING_ACCORDIONS).isEqualTo(expectedContent)
         );
      }
   }
}
