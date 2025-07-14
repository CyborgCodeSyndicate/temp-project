package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link SelectService} interface, providing methods to select options,
 * retrieve available and selected options, and determine if certain options are visible or enabled.
 * Extends {@link AbstractComponentService} to handle the creation and retrieval of {@link Select}
 * instances.
 *
 * <p>This class relies on the {@link SelectComponentType} to identify the correct component for
 * select-type elements, allowing test automation to manage dropdowns, multi-select fields, or
 * similar widgets in a consistent manner.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class SelectServiceImpl extends AbstractComponentService<SelectComponentType, Select> implements SelectService {

   private static final String SELECT_OPTIONS_CONTAINER =
         "Selecting options %s in container %s for select component %s";
   private static final String SELECT_OPTION_CONTAINER = "Selecting option %s in container %s for select component %s";
   private static final String SELECT_OPTIONS_LOCATOR =
         "Selecting options %s in container with locator %s for select component %s";
   private static final String SELECT_OPTION_LOCATOR =
         "Selecting option %s in container with locator %s for select component %s";
   private static final String SELECT_OPTIONS_STRATEGY_CONTAINER =
         "Selecting options with strategy %s in container %s for select component %s";
   private static final String SELECT_OPTIONS_STRATEGY_LOCATOR =
         "Selecting options with strategy %s in container with locator %s for select component %s";

   private static final String GET_AVAILABLE_OPTIONS_CONTAINER =
         "Getting available options in container %s for select component %s";
   private static final String GET_AVAILABLE_OPTIONS_LOCATOR =
         "Getting available options in container with locator %s for select component %s";
   private static final String GET_SELECTED_OPTIONS_CONTAINER =
         "Getting selected options in container %s for select component %s";
   private static final String GET_SELECTED_OPTIONS_LOCATOR =
         "Getting selected options in container with locator %s for select component %s";

   private static final String CHECK_OPTION_VISIBLE_CONTAINER =
         "Checking if option %s is visible in container %s for select component %s";
   private static final String CHECK_OPTION_VISIBLE_LOCATOR =
         "Checking if option %s is visible in container with locator %s for select component %s";

   private static final String CHECK_OPTION_ENABLED_CONTAINER =
         "Checking if option %s is enabled in container %s for select component %s";
   private static final String CHECK_OPTION_ENABLED_LOCATOR =
         "Checking if option %s is enabled in container with locator %s for select component %s";

   private static final String INSERT_SELECT_VALUES_LOCATOR =
         "Inserting values %s for select component %s using locator %s";

   /**
    * Constructs a new {@code SelectServiceImpl} with the specified {@link SmartWebDriver}.
    *
    * @param driver the smart web driver for interacting with browser elements.
    */
   public SelectServiceImpl(SmartWebDriver driver) {
      super(driver);
   }

   /**
    * Creates and returns a {@link Select} component instance based on the provided
    * {@link SelectComponentType}.
    *
    * @param componentType the select component type.
    * @return a new or existing {@link Select} instance.
    */
   @Override
   protected Select createComponent(final SelectComponentType componentType) {
      return ComponentFactory.getSelectComponent(componentType, driver);
   }

   /**
    * Selects one or more options by text or value within a container.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @param values        one or more option values to select.
    */
   @Override
   public void selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                             final String... values) {
      LogUi.step(String.format(SELECT_OPTIONS_CONTAINER, Arrays.toString(values), container, componentType));
      selectComponent(componentType).selectOptions(container, values);
   }

   /**
    * Selects one or more options within a container identified by a locator.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @param values           one or more option values to select.
    */
   @Override
   public void selectOptions(final SelectComponentType componentType, final By containerLocator,
                             final String... values) {
      LogUi.step(String.format(SELECT_OPTIONS_LOCATOR, Arrays.toString(values), containerLocator, componentType));
      selectComponent(componentType).selectOptions(containerLocator, values);
   }

   /**
    * Selects options using a custom {@link Strategy}, returning the list of newly selected
    * option texts.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @param strategy      the strategy to apply.
    * @return a list of texts representing the selected options.
    */
   @Override
   public List<String> selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                                     final Strategy strategy) {
      LogUi.step(String.format(SELECT_OPTIONS_STRATEGY_CONTAINER, strategy, container, componentType));
      return selectComponent(componentType).selectOptions(container, strategy);
   }

   /**
    * Selects options using a custom {@link Strategy} within a container identified by a locator,
    * returning the list of newly selected option texts.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @param strategy         the strategy to apply.
    * @return a list of texts representing the selected options.
    */
   @Override
   public List<String> selectOptions(final SelectComponentType componentType, final By containerLocator,
                                     final Strategy strategy) {
      LogUi.step(String.format(SELECT_OPTIONS_STRATEGY_LOCATOR, strategy, containerLocator, componentType));
      return selectComponent(componentType).selectOptions(containerLocator, strategy);
   }

   /**
    * Selects a single option by text or value within a container. Delegates to
    * {@link #selectOptions(SelectComponentType, SmartWebElement, String...)}.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @param value         the value of the option to select.
    */
   @Override
   public void selectOption(final SelectComponentType componentType, final SmartWebElement container,
                            final String value) {
      LogUi.step(String.format(SELECT_OPTION_CONTAINER, value, container, componentType));
      selectOptions(componentType, container, value);
   }

   /**
    * Selects a single option by text or value within a container identified by a locator.
    * Delegates to {@link #selectOptions(SelectComponentType, By, String...)}.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @param value            the value of the option to select.
    */
   @Override
   public void selectOption(final SelectComponentType componentType, final By containerLocator,
                            final String value) {
      LogUi.step(String.format(SELECT_OPTION_LOCATOR, value, containerLocator, componentType));
      selectOptions(componentType, containerLocator, value);
   }

   /**
    * Retrieves all available options in a select component within the specified container.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @return a list of all available option texts.
    */
   @Override
   public List<String> getAvailableOptions(final SelectComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(GET_AVAILABLE_OPTIONS_CONTAINER, container, componentType));
      return selectComponent(componentType).getAvailableOptions(container);
   }

   /**
    * Retrieves all available options in a select component, identified by a container locator.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @return a list of all available option texts.
    */
   @Override
   public List<String> getAvailableOptions(final SelectComponentType componentType, final By containerLocator) {
      LogUi.step(String.format(GET_AVAILABLE_OPTIONS_LOCATOR, containerLocator, componentType));
      return selectComponent(componentType).getAvailableOptions(containerLocator);
   }

   /**
    * Retrieves all currently selected options within a container holding a select component.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @return a list of texts representing the selected options.
    */
   @Override
   public List<String> getSelectedOptions(final SelectComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(GET_SELECTED_OPTIONS_CONTAINER, container, componentType));
      return selectComponent(componentType).getSelectedOptions(container);
   }

   /**
    * Retrieves all currently selected options from a select component, identified by a container locator.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @return a list of texts representing the selected options.
    */
   @Override
   public List<String> getSelectedOptions(final SelectComponentType componentType, final By containerLocator) {
      LogUi.step(String.format(GET_SELECTED_OPTIONS_LOCATOR, containerLocator, componentType));
      return selectComponent(componentType).getSelectedOptions(containerLocator);
   }

   /**
    * Checks if a specific option is visible in the select component within the given container.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @param value         the text or value of the option.
    * @return true if the option is visible, otherwise false.
    */
   @Override
   public boolean isOptionVisible(final SelectComponentType componentType, final SmartWebElement container,
                                  final String value) {
      LogUi.step(String.format(CHECK_OPTION_VISIBLE_CONTAINER, value, container, componentType));
      return selectComponent(componentType).isOptionVisible(container, value);
   }

   /**
    * Checks if a specific option is visible in the select component identified by a container locator.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @param value            the text or value of the option.
    * @return true if the option is visible, otherwise false.
    */
   @Override
   public boolean isOptionVisible(final SelectComponentType componentType, final By containerLocator,
                                  final String value) {
      LogUi.step(String.format(CHECK_OPTION_VISIBLE_LOCATOR, value, containerLocator, componentType));
      return selectComponent(componentType).isOptionVisible(containerLocator, value);
   }

   /**
    * Checks if a specific option is enabled in the select component within the given container.
    *
    * @param componentType the select component type.
    * @param container     the container holding the select element.
    * @param value         the text or value of the option.
    * @return true if the option is enabled, otherwise false.
    */
   @Override
   public boolean isOptionEnabled(final SelectComponentType componentType, final SmartWebElement container,
                                  final String value) {
      LogUi.step(String.format(CHECK_OPTION_ENABLED_CONTAINER, value, container, componentType));
      return selectComponent(componentType).isOptionEnabled(container, value);
   }

   /**
    * Checks if a specific option is enabled in the select component identified by a container locator.
    *
    * @param componentType    the select component type.
    * @param containerLocator the locator for the container.
    * @param value            the text or value of the option.
    * @return true if the option is enabled, otherwise false.
    */
   @Override
   public boolean isOptionEnabled(final SelectComponentType componentType, final By containerLocator,
                                  final String value) {
      LogUi.step(String.format(CHECK_OPTION_ENABLED_LOCATOR, value, containerLocator, componentType));
      return selectComponent(componentType).isOptionEnabled(containerLocator, value);
   }

   /**
    * Inserts data by selecting one or more options based on the provided array of values.
    * This supports the {@link com.theairebellion.zeus.ui.insertion.Insertion} contract.
    *
    * @param componentType the component type (expected to be a {@link SelectComponentType}).
    * @param locator       the locator identifying the container.
    * @param values        the values to be selected.
    */
   @Override
   public void insertion(final ComponentType componentType, final By locator, final Object... values) {
      if (!(componentType instanceof SelectComponentType selectType)) {
         throw new IllegalArgumentException("Component type needs to be from: SelectComponentType.");
      }
      LogUi.step(String.format(INSERT_SELECT_VALUES_LOCATOR, Arrays.toString(values), componentType, locator));
      String[] stringValues = Arrays.stream(values)
            .map(String::valueOf)
            .toArray(String[]::new);
      selectOptions(selectType, locator, stringValues);
   }

   /**
    * Retrieves the underlying {@link Select} instance for the specified component type.
    *
    * @param componentType the select component type.
    * @return the {@link Select} instance handling operations on this component type.
    */
   private Select selectComponent(final SelectComponentType componentType) {
      return getOrCreateComponent(componentType);
   }
}
