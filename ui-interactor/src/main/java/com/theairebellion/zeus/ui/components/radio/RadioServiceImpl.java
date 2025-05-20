package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link RadioService} interface, providing methods to select radio buttons,
 * verify their state (enabled, selected, visible), and retrieve their text values. Extends
 * {@link AbstractComponentService} to manage creation and retrieval of {@link Radio} instances.
 *
 * <p>This class delegates core functionality to an internally retrieved {@link Radio} instance,
 * created for each specified {@link RadioComponentType}. Methods conform to the {@link RadioService}
 * contract, ensuring a uniform approach to handling radio buttons across various UI designs.
 *
 * <p>All public methods handle operations such as checking button states, locating elements by
 * container or locator, and selecting them by text or strategy. The {@code insertion} method also
 * implements the {@link Insertion} interface for data
 * insertion scenarios.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class RadioServiceImpl extends AbstractComponentService<RadioComponentType, Radio> implements RadioService {

   private static final String SELECT_RADIO_WITH_TEXT_CONTAINER =
         "Selecting radio button with text %s in container %s for radio component %s";
   private static final String SELECT_RADIO_WITH_STRATEGY =
         "Selecting radio button with strategy %s in container %s for radio component %s";
   private static final String SELECT_RADIO_WITH_TEXT = "Selecting radio button with text %s for radio component %s";
   private static final String SELECT_RADIO_WITH_LOCATOR =
         "Selecting radio button with locator %s for radio component %s";

   private static final String CHECK_RADIO_ENABLED_CONTAINER =
         "Checking if radio button with text %s is enabled in container %s for radio component %s";
   private static final String CHECK_RADIO_ENABLED_TEXT =
         "Checking if radio button with text %s is enabled for radio component %s";
   private static final String CHECK_RADIO_ENABLED_LOCATOR =
         "Checking if radio button with locator %s is enabled for radio component %s";

   private static final String CHECK_RADIO_SELECTED_CONTAINER =
         "Checking if radio button with text %s is selected in container %s for radio component %s";
   private static final String CHECK_RADIO_SELECTED_TEXT =
         "Checking if radio button with text %s is selected for radio component %s";
   private static final String CHECK_RADIO_SELECTED_LOCATOR =
         "Checking if radio button with locator %s is selected for radio component %s";

   private static final String CHECK_RADIO_VISIBLE_CONTAINER =
         "Checking if radio button with text %s is visible in container %s for radio component %s";
   private static final String CHECK_RADIO_VISIBLE_TEXT =
         "Checking if radio button with text %s is visible for radio component %s";
   private static final String CHECK_RADIO_VISIBLE_LOCATOR =
         "Checking if radio button with locator %s is visible for radio component %s";

   private static final String GET_SELECTED_RADIO_CONTAINER =
         "Getting selected radio button text from container %s for radio component %s";
   private static final String GET_SELECTED_RADIO_LOCATOR =
         "Getting selected radio button text from locator %s for radio component %s";

   private static final String GET_ALL_RADIOS_CONTAINER =
         "Getting all radio buttons from container %s for radio component %s";
   private static final String GET_ALL_RADIOS_LOCATOR =
         "Getting all radio buttons from locator %s for radio component %s";

   private static final String INSERT_RADIO_VALUES_LOCATOR =
         "Inserting values %s for radio component %s using locator %s";

   /**
    * Constructs a new {@code RadioServiceImpl} with the provided {@link SmartWebDriver}.
    *
    * @param driver the smart web driver for interacting with browser elements.
    */
   public RadioServiceImpl(SmartWebDriver driver) {
      super(driver);
   }

   /**
    * Creates and returns a {@link Radio} component based on the given {@link RadioComponentType}.
    *
    * @param componentType the type of the radio component.
    * @return a new or existing {@link Radio} instance for the specified component type.
    */
   @Override
   protected Radio createComponent(final RadioComponentType componentType) {
      return ComponentFactory.getRadioComponent(componentType, driver);
   }

   /**
    * Selects a radio button by text within a given container, using the specified {@link RadioComponentType}.
    *
    * @param componentType   the radio component type.
    * @param container       the container holding the radio buttons.
    * @param radioButtonText the text of the radio button to select.
    */
   @Override
   public void select(final RadioComponentType componentType, final SmartWebElement container,
                      final String radioButtonText) {
      LogUi.step(String.format(SELECT_RADIO_WITH_TEXT_CONTAINER, radioButtonText, container, componentType));
      radioComponent(componentType).select(container, radioButtonText);
   }

   /**
    * Selects a radio button within a given container using a custom {@link Strategy}, returning
    * the text of the selected button.
    *
    * @param componentType the radio component type.
    * @param container     the container holding the radio buttons.
    * @param strategy      the selection strategy.
    * @return the text of the selected radio button.
    */
   @Override
   public String select(final RadioComponentType componentType, final SmartWebElement container,
                        final Strategy strategy) {
      LogUi.step(String.format(SELECT_RADIO_WITH_STRATEGY, strategy, container, componentType));
      return radioComponent(componentType).select(container, strategy);
   }

   /**
    * Selects a radio button by text without specifying a container, using the given {@link RadioComponentType}.
    *
    * @param componentType   the radio component type.
    * @param radioButtonText the text of the radio button to select.
    */
   @Override
   public void select(final RadioComponentType componentType, final String radioButtonText) {
      LogUi.step(String.format(SELECT_RADIO_WITH_TEXT, radioButtonText, componentType));
      radioComponent(componentType).select(radioButtonText);
   }

   /**
    * Selects a radio button identified by a locator, using the specified {@link RadioComponentType}.
    *
    * @param componentType      the radio component type.
    * @param radioButtonLocator the locator referencing the radio button.
    */
   @Override
   public void select(final RadioComponentType componentType, final By radioButtonLocator) {
      LogUi.step(String.format(SELECT_RADIO_WITH_LOCATOR, radioButtonLocator, componentType));
      radioComponent(componentType).select(radioButtonLocator);
   }

   /**
    * Checks if a radio button is enabled within the specified container, identified by text.
    *
    * @param componentType   the radio component type.
    * @param container       the container holding the radio button.
    * @param radioButtonText the text of the radio button to check.
    * @return true if enabled, otherwise false.
    */
   @Override
   public boolean isEnabled(final RadioComponentType componentType, final SmartWebElement container,
                            final String radioButtonText) {
      LogUi.step(String.format(CHECK_RADIO_ENABLED_CONTAINER, radioButtonText, container, componentType));
      return radioComponent(componentType).isEnabled(container, radioButtonText);
   }

   /**
    * Checks if a radio button, identified by text, is enabled without specifying a container.
    *
    * @param componentType   the radio component type.
    * @param radioButtonText the text of the radio button to check.
    * @return true if enabled, otherwise false.
    */
   @Override
   public boolean isEnabled(final RadioComponentType componentType, final String radioButtonText) {
      LogUi.step(String.format(CHECK_RADIO_ENABLED_TEXT, radioButtonText, componentType));
      return radioComponent(componentType).isEnabled(radioButtonText);
   }

   /**
    * Checks if a radio button identified by a locator is enabled.
    *
    * @param componentType      the radio component type.
    * @param radioButtonLocator the locator referencing the radio button.
    * @return true if enabled, otherwise false.
    */
   @Override
   public boolean isEnabled(final RadioComponentType componentType, final By radioButtonLocator) {
      LogUi.step(String.format(CHECK_RADIO_ENABLED_LOCATOR, radioButtonLocator, componentType));
      return radioComponent(componentType).isEnabled(radioButtonLocator);
   }

   /**
    * Checks if a radio button is selected within the specified container, identified by text.
    *
    * @param componentType   the radio component type.
    * @param container       the container holding the radio button.
    * @param radioButtonText the text of the radio button to check.
    * @return true if selected, otherwise false.
    */
   @Override
   public boolean isSelected(final RadioComponentType componentType, final SmartWebElement container,
                             final String radioButtonText) {
      LogUi.step(String.format(CHECK_RADIO_SELECTED_CONTAINER, radioButtonText, container, componentType));
      return radioComponent(componentType).isSelected(container, radioButtonText);
   }

   /**
    * Checks if a radio button, identified by text, is selected without specifying a container.
    *
    * @param componentType   the radio component type.
    * @param radioButtonText the text of the radio button to check.
    * @return true if selected, otherwise false.
    */
   @Override
   public boolean isSelected(final RadioComponentType componentType, final String radioButtonText) {
      LogUi.step(String.format(CHECK_RADIO_SELECTED_TEXT, radioButtonText, componentType));
      return radioComponent(componentType).isSelected(radioButtonText);
   }

   /**
    * Checks if a radio button identified by a locator is selected.
    *
    * @param componentType      the radio component type.
    * @param radioButtonLocator the locator referencing the radio button.
    * @return true if selected, otherwise false.
    */
   @Override
   public boolean isSelected(final RadioComponentType componentType, final By radioButtonLocator) {
      LogUi.step(String.format(CHECK_RADIO_SELECTED_LOCATOR, radioButtonLocator, componentType));
      return radioComponent(componentType).isSelected(radioButtonLocator);
   }

   /**
    * Checks if a radio button is visible within the specified container, identified by text.
    *
    * @param componentType   the radio component type.
    * @param container       the container holding the radio button.
    * @param radioButtonText the text of the radio button to check.
    * @return true if visible, otherwise false.
    */
   @Override
   public boolean isVisible(final RadioComponentType componentType, final SmartWebElement container,
                            final String radioButtonText) {
      LogUi.step(String.format(CHECK_RADIO_VISIBLE_CONTAINER, radioButtonText, container, componentType));
      return radioComponent(componentType).isVisible(container, radioButtonText);
   }

   /**
    * Checks if a radio button, identified by text, is visible without specifying a container.
    *
    * @param componentType   the radio component type.
    * @param radioButtonText the text of the radio button to check.
    * @return true if visible, otherwise false.
    */
   @Override
   public boolean isVisible(final RadioComponentType componentType, final String radioButtonText) {
      LogUi.step(String.format(CHECK_RADIO_VISIBLE_TEXT, radioButtonText, componentType));
      return radioComponent(componentType).isVisible(radioButtonText);
   }

   /**
    * Checks if a radio button identified by a locator is visible.
    *
    * @param componentType      the radio component type.
    * @param radioButtonLocator the locator referencing the radio button.
    * @return true if visible, otherwise false.
    */
   @Override
   public boolean isVisible(final RadioComponentType componentType, final By radioButtonLocator) {
      LogUi.step(String.format(CHECK_RADIO_VISIBLE_LOCATOR, radioButtonLocator, componentType));
      return radioComponent(componentType).isVisible(radioButtonLocator);
   }

   /**
    * Retrieves the text of the currently selected radio button within a container.
    *
    * @param componentType the radio component type.
    * @param container     the container holding the radio buttons.
    * @return the text of the selected radio button, or an empty string if none is selected.
    */
   @Override
   public String getSelected(final RadioComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(GET_SELECTED_RADIO_CONTAINER, container, componentType));
      return radioComponent(componentType).getSelected(container);
   }

   /**
    * Retrieves the text of the currently selected radio button within a container specified by a locator.
    *
    * @param componentType    the radio component type.
    * @param containerLocator the locator referencing the container holding the radio buttons.
    * @return the text of the selected radio button, or an empty string if none is selected.
    */
   @Override
   public String getSelected(final RadioComponentType componentType, final By containerLocator) {
      LogUi.step(String.format(GET_SELECTED_RADIO_LOCATOR, containerLocator, componentType));
      return radioComponent(componentType).getSelected(containerLocator);
   }

   /**
    * Retrieves a list of all available radio button texts in the specified container.
    *
    * @param componentType the radio component type.
    * @param container     the container holding the radio buttons.
    * @return a list of all radio button texts.
    */
   @Override
   public List<String> getAll(final RadioComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(GET_ALL_RADIOS_CONTAINER, container, componentType));
      return radioComponent(componentType).getAll(container);
   }

   /**
    * Retrieves a list of all available radio button texts in a container specified by a locator.
    *
    * @param componentType    the radio component type.
    * @param containerLocator the locator referencing the container holding the radio buttons.
    * @return a list of all radio button texts.
    */
   @Override
   public List<String> getAll(final RadioComponentType componentType, final By containerLocator) {
      LogUi.step(String.format(GET_ALL_RADIOS_LOCATOR, containerLocator, componentType));
      return radioComponent(componentType).getAll(containerLocator);
   }

    /**
     * Inserts (selects) a radio button based on the first passed value. Used by the
     * {@link com.theairebellion.zeus.ui.insertion.Insertion} interface.
     *
     * @param componentType the {@link ComponentType} identifying the radio component.
     * @param locator       the locator for the container or element.
     * @param values        an array of objects, expected to contain at least one string representing the radio text.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        if (!(componentType instanceof RadioComponentType radioType)) {
            throw new IllegalArgumentException("Component type needs to be from: RadioComponentType.");
        }
        LogUi.step(String.format(INSERT_RADIO_VALUES_LOCATOR, Arrays.toString(values), componentType, locator));
        select(radioType, String.valueOf(values[0]));
    }

   /**
    * Retrieves or creates the {@link Radio} instance for the specified component type.
    *
    * @param componentType the radio component type.
    * @return the {@link Radio} instance associated with the given component type.
    */
   private Radio radioComponent(final RadioComponentType componentType) {
      return getOrCreateComponent(componentType);
   }

}
