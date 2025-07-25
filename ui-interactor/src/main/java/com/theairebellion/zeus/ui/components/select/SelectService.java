package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.util.List;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides an interface for interacting with select components in a UI automation
 * framework, enabling selection of multiple options and retrieval of their states
 * or availability. By referencing a {@link SelectComponentType} and the {@link #DEFAULT_TYPE},
 * implementations can manage different styles of dropdown or multi-select elements
 * with consistent operations.
 *
 * <p>Classes implementing this interface typically leverage Selenium-based approaches
 * for identifying containers (via {@link SmartWebElement}) or using direct locators
 * (via {@link By}), ensuring robust and flexible automation across various UI designs.
 *
 * <p>This interface also extends the {@link Insertion}
 * contract, thereby supporting scenarios where select components are part of
 * automated data entry flows.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface SelectService extends Insertion {

   SelectComponentType DEFAULT_TYPE = getDefaultType();

   /**
    * Retrieves the default select component type from the configuration.
    *
    * @return The default SelectComponentType.
    */
   private static SelectComponentType getDefaultType() {
      try {
         return ReflectionUtil.findEnumImplementationsOfInterface(SelectComponentType.class,
               getUiConfig().selectDefaultType(),
               getUiConfig().projectPackage());
      } catch (Exception ignored) {
         return null;
      }
   }

   /**
    * Selects one or more options in a select component based on the given values. Uses the default select
    * component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @param values    The values of the options to be selected. Varargs argument to select multiple options.
    */
   default void selectOptions(SmartWebElement container, String... values) {
      selectOptions(DEFAULT_TYPE, container, values);
   }

   /**
    * Selects one or more options in a select component based on the given values.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @param values        The values of the options to be selected. Varargs argument to select multiple options.
    */
   void selectOptions(SelectComponentType componentType, SmartWebElement container, String... values);

   /**
    * Selects one or more options in a select component based on the given values using the default select
    * component type.
    * The select component is identified by the provided container locator.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param values           The values of the options to be selected. Varargs argument to select multiple options.
    */
   default void selectOptions(By containerLocator, String... values) {
      selectOptions(DEFAULT_TYPE, containerLocator, values);
   }

   /**
    * Selects one or more options in a select component based on the given values.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param values           The values of the options to be selected. Varargs argument to select multiple options.
    */
   void selectOptions(SelectComponentType componentType, By containerLocator, String... values);

   /**
    * Selects options in a select component based on a specific strategy. Uses the default select component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @param strategy  The strategy to use for selecting options.
    * @return A List of Strings representing the values of the selected option(s).
    */
   default List<String> selectOptions(SmartWebElement container, Strategy strategy) {
      return selectOptions(DEFAULT_TYPE, container, strategy);
   }

   /**
    * Selects options in a select component based on a specific strategy.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @param strategy      The strategy to use for selecting options.
    * @return A List of Strings representing the values of the selected option(s).
    */
   List<String> selectOptions(SelectComponentType componentType, SmartWebElement container, Strategy strategy);

   /**
    * Selects options in a select component based on a specific strategy using the default select component type.
    * The select component is identified by the provided container locator.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param strategy         The strategy to use for selecting options.
    * @return A List of Strings representing the values of the selected option(s).
    */
   default List<String> selectOptions(By containerLocator, Strategy strategy) {
      return selectOptions(DEFAULT_TYPE, containerLocator, strategy);
   }

   /**
    * Selects options in a select component based on a specific strategy.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param strategy         The strategy to use for selecting options.
    * @return A List of Strings representing the values of the selected option(s).
    */
   List<String> selectOptions(SelectComponentType componentType, By containerLocator, Strategy strategy);

   /**
    * Selects one or more options in a select component based on the given values. Uses the default select
    * component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @param value     The value of the option to be selected.
    */
   default void selectOption(SmartWebElement container, String value) {
      selectOption(DEFAULT_TYPE, container, value);
   }

   /**
    * Selects one or more options in a select component based on the given values.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @param value         The value of the option to be selected.
    */
   void selectOption(SelectComponentType componentType, SmartWebElement container, String value);

   /**
    * Selects one or more options in a select component based on the given values using the default select
    * component type.
    * The select component is identified by the provided container locator.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param value            The value of the option to be selected.
    */
   default void selectOption(By containerLocator, String value) {
      selectOption(DEFAULT_TYPE, containerLocator, value);
   }

   /**
    * Selects one or more options in a select component based on the given values.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param value            The value of the option to be selected.
    */
   void selectOption(SelectComponentType componentType, By containerLocator, String value);

   /**
    * Retrieves a list of available options in a select component. Uses the default select component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @return A List of Strings representing the available options.
    */
   default List<String> getAvailableOptions(SmartWebElement container) {
      return getAvailableOptions(DEFAULT_TYPE, container);
   }

   /**
    * Retrieves a list of available options in a select component.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @return A List of Strings representing the available options.
    */
   List<String> getAvailableOptions(SelectComponentType componentType, SmartWebElement container);

   /**
    * Retrieves a list of available options in a select component using the default select component type.
    * The select component is identified by the provided container locator.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @return A List of Strings representing the available options.
    */
   default List<String> getAvailableOptions(By containerLocator) {
      return getAvailableOptions(DEFAULT_TYPE, containerLocator);
   }

   /**
    * Retrieves a list of available options in a select component.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @return A List of Strings representing the available options.
    */
   List<String> getAvailableOptions(SelectComponentType componentType, By containerLocator);

   /**
    * Retrieves a list of currently selected options in a select component. Uses the default select component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @return A List of Strings representing the selected options.
    */
   default List<String> getSelectedOptions(SmartWebElement container) {
      return getSelectedOptions(DEFAULT_TYPE, container);
   }

   /**
    * Retrieves a list of currently selected options in a select component.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @return A List of Strings representing the selected options.
    */
   List<String> getSelectedOptions(SelectComponentType componentType, SmartWebElement container);

   /**
    * Retrieves a list of currently selected options in a select component.
    * The select component is identified by the provided container locator, using the default select component type.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @return A List of Strings representing the selected options.
    */
   default List<String> getSelectedOptions(By containerLocator) {
      return getSelectedOptions(DEFAULT_TYPE, containerLocator);
   }

   /**
    * Retrieves a list of currently selected options in a select component.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @return A List of Strings representing the selected options.
    */
   List<String> getSelectedOptions(SelectComponentType componentType, By containerLocator);

   /**
    * Checks if a specific option is visible in a select component. Uses the default select component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @param value     The value of the option to check.
    * @return true if the option is present, false otherwise.
    */
   default boolean isOptionVisible(SmartWebElement container, String value) {
      return isOptionVisible(DEFAULT_TYPE, container, value);
   }

   /**
    * Checks if a specific option is visible in a select component.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @param value         The value of the option to check.
    * @return true if the option is present, false otherwise.
    */
   boolean isOptionVisible(SelectComponentType componentType, SmartWebElement container, String value);

   /**
    * Checks if a specific option is visible in a select component.
    * The select component is identified by the provided container locator, using the default select component type.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param value            The value of the option to check.
    * @return true if the option is present, false otherwise.
    */
   default boolean isOptionVisible(By containerLocator, String value) {
      return isOptionVisible(DEFAULT_TYPE, containerLocator, value);
   }

   /**
    * Checks if a specific option is visible in a select component.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param value            The value of the option to check.
    * @return true if the option is present, false otherwise.
    */
   boolean isOptionVisible(SelectComponentType componentType, By containerLocator, String value);

   /**
    * Checks if a specific option is enabled in a select component. Uses the default select component type.
    *
    * @param container The SmartWebElement representing the container of the select component.
    * @param value     The value of the option to check.
    * @return true if the option is enabled, false otherwise.
    */
   default boolean isOptionEnabled(SmartWebElement container, String value) {
      return isOptionEnabled(DEFAULT_TYPE, container, value);
   }

   /**
    * Checks if a specific option is enabled in a select component.
    *
    * @param componentType The type of select component.
    * @param container     The SmartWebElement representing the container of the select component.
    * @param value         The value of the option to check.
    * @return true if the option is enabled, false otherwise.
    */
   boolean isOptionEnabled(SelectComponentType componentType, SmartWebElement container, String value);

   /**
    * Checks if a specific option is enabled in a select component.
    * The select component is identified by the provided container locator, using the default select component type.
    *
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param value            The value of the option to check.
    * @return true if the option is enabled, false otherwise.
    */
   default boolean isOptionEnabled(By containerLocator, String value) {
      return isOptionEnabled(DEFAULT_TYPE, containerLocator, value);
   }

   /**
    * Checks if a specific option is enabled in a select component.
    * The select component is identified by the provided container locator and the specified component type.
    *
    * @param componentType    The type of select component.
    * @param containerLocator The By locator that identifies the container of the select component.
    * @param value            The value of the option to check.
    * @return true if the option is enabled, false otherwise.
    */
   boolean isOptionEnabled(SelectComponentType componentType, By containerLocator, String value);
}