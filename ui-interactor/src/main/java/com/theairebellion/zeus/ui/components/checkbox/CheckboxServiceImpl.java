package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.List;
import org.openqa.selenium.By;

/**
 * Provides service-level operations for interacting with checkbox components.
 *
 * <p>This class handles selection, deselection, verification of states (selected/enabled),
 * and retrieval of checkbox values, delegating the interactions to appropriate
 * {@link Checkbox} implementations based on the given {@link CheckboxComponentType}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class CheckboxServiceImpl extends AbstractComponentService<CheckboxComponentType, Checkbox>
      implements CheckboxService {


   private static final String SELECT_WITH_TEXT = "Selecting checkbox %s with text %s";
   private static final String SELECT_USING_STRATEGY = "Selecting checkbox %s using strategy %s";
   private static final String SELECT_BY_LOCATOR = "Selecting checkbox %s by locator";

   private static final String DESELECT_WITH_TEXT = "Deselecting checkbox %s with text %s";
   private static final String DESELECT_USING_STRATEGY = "Deselecting checkbox %s using strategy %s";
   private static final String DESELECT_BY_LOCATOR = "Deselecting checkbox %s by locator";

   private static final String CHECKBOXES_SELECTED = "Checking if checkboxes %s are selected";
   private static final String CHECKBOXES_SELECTED_BY_LOCATOR = "Checking if checkboxes %s are selected by locator";
   private static final String CHECKBOX_SELECTED = "Checking if checkbox %s is selected";
   private static final String CHECKBOX_SELECTED_BY_LOCATOR = "Checking if checkbox %s is selected by locator";

   private static final String CHECKBOXES_ENABLED = "Checking if checkboxes %s are enabled";
   private static final String CHECKBOXES_ENABLED_BY_LOCATOR = "Checking if checkboxes %s are enabled by locator";
   private static final String CHECKBOX_ENABLED = "Checking if checkbox %s is enabled";
   private static final String CHECKBOX_ENABLED_BY_LOCATOR = "Checking if checkbox %s is enabled by locator";

   private static final String GET_SELECTED_CHECKBOXES = "Getting selected checkboxes %s";
   private static final String GET_SELECTED_CHECKBOXES_BY_LOCATOR = "Getting selected checkboxes %s by locator";
   private static final String GET_ALL_CHECKBOXES = "Getting all checkboxes %s";
   private static final String GET_ALL_CHECKBOXES_BY_LOCATOR = "Getting all checkboxes %s by locator";

   private static final String INSERT_VALUE_INTO_CHECKBOX = "Inserting value into checkbox %s";

   /**
    * Constructs a new CheckboxServiceImpl using the specified SmartWebDriver.
    *
    * @param driver the SmartWebDriver used for UI interactions.
    */
   public CheckboxServiceImpl(SmartWebDriver driver) {
      super(driver);
   }

   /**
    * Creates a new Checkbox instance for the given component type.
    *
    * @param componentType the checkbox component type.
    * @return a new or cached Checkbox instance.
    */
   @Override
   protected Checkbox createComponent(CheckboxComponentType componentType) {
      return ComponentFactory.getCheckBoxComponent(componentType, driver);
   }

   /**
    * Selects checkboxes inside a container based on their text.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @param checkBoxText  the text of the checkboxes to select.
    */
   @Override
   public void select(final CheckboxComponentType componentType, final SmartWebElement container,
                      final String... checkBoxText) {
      String text = checkBoxText != null ? String.join(", ", checkBoxText) : "no text added";
      LogUi.step(String.format(SELECT_WITH_TEXT, componentType, text));
      checkboxComponent(componentType).select(container, checkBoxText);
   }

   /**
    * Selects a checkbox inside a container based on a strategy.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @param strategy      the selection strategy.
    * @return the selected checkbox text.
    */
   @Override
   public String select(final CheckboxComponentType componentType, final SmartWebElement container,
                        final Strategy strategy) {
      LogUi.step(String.format(SELECT_USING_STRATEGY, componentType, strategy));
      return checkboxComponent(componentType).select(container, strategy);
   }

   /**
    * Selects checkboxes based on their text.
    *
    * @param componentType the checkbox component type.
    * @param checkBoxText  the text of the checkboxes to select.
    */
   @Override
   public void select(final CheckboxComponentType componentType, final String... checkBoxText) {
      LogUi.step(String.format(SELECT_WITH_TEXT, componentType, String.join(", ", checkBoxText)));
      checkboxComponent(componentType).select(checkBoxText);
   }

   /**
    * Selects checkboxes identified by their locators.
    *
    * @param componentType   the checkbox component type.
    * @param checkBoxLocator the locators of the checkboxes to select.
    */
   @Override
   public void select(final CheckboxComponentType componentType, final By... checkBoxLocator) {
      LogUi.step(String.format(SELECT_BY_LOCATOR, componentType));
      checkboxComponent(componentType).select(checkBoxLocator);
   }

   /**
    * Deselects checkboxes inside a container based on their text.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @param checkBoxText  the text of the checkboxes to deselect.
    */
   @Override
   public void deSelect(final CheckboxComponentType componentType, final SmartWebElement container,
                        final String... checkBoxText) {
      LogUi.step(String.format(DESELECT_WITH_TEXT, componentType, String.join(", ", checkBoxText)));
      checkboxComponent(componentType).deSelect(container, checkBoxText);
   }

   /**
    * Deselects a checkbox inside a container based on a strategy.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @param strategy      the deselection strategy.
    * @return the deselected checkbox text.
    */
   @Override
   public String deSelect(final CheckboxComponentType componentType, final SmartWebElement container,
                          final Strategy strategy) {
      LogUi.step(String.format(DESELECT_USING_STRATEGY, componentType, strategy));
      return checkboxComponent(componentType).deSelect(container, strategy);
   }

   /**
    * Deselects checkboxes based on their text.
    *
    * @param componentType the checkbox component type.
    * @param checkBoxText  the text of the checkboxes to deselect.
    */
   @Override
   public void deSelect(final CheckboxComponentType componentType, final String... checkBoxText) {
      LogUi.step(String.format(DESELECT_WITH_TEXT, componentType, String.join(", ", checkBoxText)));
      checkboxComponent(componentType).deSelect(checkBoxText);
   }

   /**
    * Deselects checkboxes identified by their locators.
    *
    * @param componentType   the checkbox component type.
    * @param checkBoxLocator the locators of the checkboxes to deselect.
    */
   @Override
   public void deSelect(final CheckboxComponentType componentType, final By... checkBoxLocator) {
      LogUi.step(String.format(DESELECT_BY_LOCATOR, componentType));
      checkboxComponent(componentType).deSelect(checkBoxLocator);
   }

   /**
    * Checks if the specified checkboxes inside a container are selected.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @param checkBoxText  the text of the checkboxes to verify.
    * @return true if all specified checkboxes are selected; false otherwise.
    */
   @Override
   public boolean areSelected(final CheckboxComponentType componentType, final SmartWebElement container,
                              final String... checkBoxText) {
      LogUi.step(String.format(CHECKBOXES_SELECTED, componentType));
      return checkboxComponent(componentType).areSelected(container, checkBoxText);
   }

   /**
    * Checks if the checkboxes with the specified text are selected.
    *
    * @param componentType the checkbox component type.
    * @param checkBoxText  the text of the checkboxes to verify.
    * @return true if all specified checkboxes are selected, false otherwise.
    */
   @Override
   public boolean areSelected(final CheckboxComponentType componentType, final String... checkBoxText) {
      LogUi.step(String.format(CHECKBOXES_SELECTED, componentType));
      return checkboxComponent(componentType).areSelected(checkBoxText);
   }

   /**
    * Checks if the checkboxes with the specified text are selected.
    *
    * @param componentType   the checkbox component type.
    * @param checkBoxLocator the locators of the checkboxes to verify.
    * @return true if all specified checkboxes are selected, false otherwise.
    */
   @Override
   public boolean areSelected(final CheckboxComponentType componentType, final By... checkBoxLocator) {
      LogUi.step(String.format(CHECKBOXES_SELECTED_BY_LOCATOR, componentType));
      return checkboxComponent(componentType).areSelected(checkBoxLocator);
   }

   /**
    * Checks if a checkbox inside a container is selected.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkbox.
    * @param checkBoxText  the text of the checkbox to verify.
    * @return true if the checkbox is selected, false otherwise.
    */
   @Override
   public boolean isSelected(final CheckboxComponentType componentType, final SmartWebElement container,
                             final String checkBoxText) {
      LogUi.step(String.format(CHECKBOX_SELECTED, componentType));
      return checkboxComponent(componentType).areSelected(container, checkBoxText);
   }

   /**
    * Checks if a checkbox with the specified text is selected.
    *
    * @param componentType the checkbox component type.
    * @param checkBoxText  the text of the checkbox to verify.
    * @return true if the checkbox is selected, false otherwise.
    */
   @Override
   public boolean isSelected(final CheckboxComponentType componentType, final String checkBoxText) {
      LogUi.step(String.format(CHECKBOX_SELECTED, componentType));
      return checkboxComponent(componentType).areSelected(checkBoxText);
   }

   /**
    * Checks if a checkbox identified by a locator is selected.
    *
    * @param componentType   the checkbox component type.
    * @param checkBoxLocator the locator of the checkbox to verify.
    * @return true if the checkbox is selected, false otherwise.
    */
   @Override
   public boolean isSelected(final CheckboxComponentType componentType, final By checkBoxLocator) {
      LogUi.step(String.format(CHECKBOX_SELECTED_BY_LOCATOR, componentType));
      return checkboxComponent(componentType).areSelected(checkBoxLocator);
   }

   /**
    * Checks if the checkboxes with the specified text inside a container are enabled.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @param checkBoxText  the text of the checkboxes to verify.
    * @return true if all specified checkboxes are enabled, false otherwise.
    */
   @Override
   public boolean areEnabled(final CheckboxComponentType componentType, final SmartWebElement container,
                             final String... checkBoxText) {
      LogUi.step(String.format(CHECKBOXES_ENABLED, componentType));
      return checkboxComponent(componentType).areEnabled(container, checkBoxText);
   }

   /**
    * Checks if the checkboxes with the specified text are enabled.
    *
    * @param componentType the checkbox component type.
    * @param checkBoxText  the text of the checkboxes to verify.
    * @return true if all specified checkboxes are enabled, false otherwise.
    */
   @Override
   public boolean areEnabled(final CheckboxComponentType componentType, final String... checkBoxText) {
      LogUi.step(String.format(CHECKBOXES_ENABLED, componentType));
      return checkboxComponent(componentType).areEnabled(checkBoxText);
   }

   /**
    * Checks if the checkboxes identified by their locators are enabled.
    *
    * @param componentType   the checkbox component type.
    * @param checkBoxLocator the locators of the checkboxes to verify.
    * @return true if all specified checkboxes are enabled, false otherwise.
    */
   @Override
   public boolean areEnabled(final CheckboxComponentType componentType, final By... checkBoxLocator) {
      LogUi.step(String.format(CHECKBOXES_ENABLED_BY_LOCATOR, componentType));
      return checkboxComponent(componentType).areEnabled(checkBoxLocator);
   }

   /**
    * Checks if a checkbox inside a container is enabled.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkbox.
    * @param checkBoxText  the text of the checkbox to verify.
    * @return true if the checkbox is enabled, false otherwise.
    */
   @Override
   public boolean isEnabled(final CheckboxComponentType componentType, final SmartWebElement container,
                            final String checkBoxText) {
      LogUi.step(String.format(CHECKBOX_ENABLED, componentType));
      return checkboxComponent(componentType).areEnabled(container, checkBoxText);
   }

   /**
    * Checks if a checkbox with the specified text is enabled.
    *
    * @param componentType the checkbox component type.
    * @param checkBoxText  the text of the checkbox to verify.
    * @return true if the checkbox is enabled, false otherwise.
    */
   @Override
   public boolean isEnabled(final CheckboxComponentType componentType, final String checkBoxText) {
      LogUi.step(String.format(CHECKBOX_ENABLED, componentType));
      return checkboxComponent(componentType).areEnabled(checkBoxText);
   }

   /**
    * Checks if a checkbox identified by a locator is enabled.
    *
    * @param componentType   the checkbox component type.
    * @param checkBoxLocator the locator of the checkbox to verify.
    * @return true if the checkbox is enabled, false otherwise.
    */
   @Override
   public boolean isEnabled(final CheckboxComponentType componentType, final By checkBoxLocator) {
      LogUi.step(String.format(CHECKBOX_ENABLED_BY_LOCATOR, componentType));
      return checkboxComponent(componentType).areEnabled(checkBoxLocator);
   }

   /**
    * Retrieves a list of all selected checkboxes inside a container.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @return a list of selected checkbox texts.
    */
   @Override
   public List<String> getSelected(final CheckboxComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(GET_SELECTED_CHECKBOXES, componentType));
      return checkboxComponent(componentType).getSelected(container);
   }

   /**
    * Retrieves a list of all selected checkboxes inside a container identified by its locator.
    *
    * @param componentType    the checkbox component type.
    * @param containerLocator the locator of the container holding the checkboxes.
    * @return a list of selected checkbox texts.
    */
   @Override
   public List<String> getSelected(final CheckboxComponentType componentType, final By containerLocator) {
      LogUi.step(String.format(GET_SELECTED_CHECKBOXES_BY_LOCATOR, componentType));
      return checkboxComponent(componentType).getSelected(containerLocator);
   }

   /**
    * Retrieves a list of all checkboxes inside a container.
    *
    * @param componentType the checkbox component type.
    * @param container     the container holding the checkboxes.
    * @return a list of all checkbox texts.
    */
   @Override
   public List<String> getAll(final CheckboxComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(GET_ALL_CHECKBOXES, componentType));
      return checkboxComponent(componentType).getAll(container);
   }

   /**
    * Retrieves a list of all checkboxes inside a container identified by its locator.
    *
    * @param componentType    the checkbox component type.
    * @param containerLocator the locator of the container holding the checkboxes.
    * @return a list of all checkbox texts.
    */
   @Override
   public List<String> getAll(final CheckboxComponentType componentType, final By containerLocator) {
      LogUi.step(String.format(GET_ALL_CHECKBOXES_BY_LOCATOR, componentType));
      return checkboxComponent(componentType).getAll(containerLocator);
   }

    /**
     * Performs an insertion action on a checkbox component.
     *
     * @param componentType the checkbox component type.
     * @param locator       the locator for the checkbox.
     * @param values        optional values for the insertion action.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        if (!(componentType instanceof CheckboxComponentType checkboxType)) {
            throw new IllegalArgumentException("Component type needs to be from: CheckboxComponentType.");
        }
        LogUi.step(String.format(INSERT_VALUE_INTO_CHECKBOX, checkboxType));
        select(checkboxType, (String) values[0]);
    }

   /**
    * Retrieves the Checkbox instance for the given component type.
    *
    * @param componentType the checkbox component type.
    * @return the Checkbox instance.
    */
   private Checkbox checkboxComponent(CheckboxComponentType componentType) {
      return getOrCreateComponent(componentType);
   }

}
