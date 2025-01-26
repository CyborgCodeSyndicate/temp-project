package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface CheckboxService extends Insertion {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    CheckboxComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Selects checkboxes with their specified text inside a container, using the default checkbox component type.
     *
     * @param container    The WebElement container that contains the checkboxes.
     * @param checkBoxText The text of the checkbox to click.
     */
    default void select(WebElement container, String... checkBoxText) {
        select(DEFAULT_TYPE, container, checkBoxText);
    }

    /**
     * Selects checkboxes with the specified text inside a container, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkbox.
     * @param checkBoxText  The text of the checkbox to click.
     */
    void select(CheckboxComponentType componentType, WebElement container, String... checkBoxText);

    /**
     * Selects checkboxes inside a container based on a specified strategy, using the default checkbox component type.
     *
     * @param container The WebElement container that contains the checkboxes.
     * @param strategy  The strategy to determine which checkboxes to select.
     * @return A string indicating the result of the selection process.
     */
    default String select(WebElement container, Strategy strategy) {
        return select(DEFAULT_TYPE, container, strategy);
    }

    /**
     * Selects checkboxes inside a container based on a specified strategy, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkboxes.
     * @param strategy      The strategy to determine which checkboxes to select.
     * @return A string indicating the result of the selection process.
     */
    String select(CheckboxComponentType componentType, WebElement container, Strategy strategy);

    /**
     * Selects checkboxes with the specified text using the default checkbox component type.
     *
     * @param checkBoxText The text of the checkboxes to select.
     */
    default void select(String... checkBoxText) {
        select(DEFAULT_TYPE, checkBoxText);
    }

    /**
     * Selects checkboxes with the specified text using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param checkBoxText  The text of the checkboxes to select.
     */
    void select(CheckboxComponentType componentType, String... checkBoxText);

    /**
     * Selects checkboxes identified by specified locators, using the default checkbox component type.
     *
     * @param checkBoxLocator The locators for the checkboxes to select.
     */
    default void select(By... checkBoxLocator) {
        select(DEFAULT_TYPE, checkBoxLocator);
    }

    /**
     * Selects checkboxes identified by specified locators, using the given checkbox component type.
     *
     * @param componentType   The specific checkbox component type.
     * @param checkBoxLocator The locators for the checkboxes to select.
     */
    void select(CheckboxComponentType componentType, By... checkBoxLocator);

    /**
     * Deselects checkboxes with their specified text inside a container, using the default checkbox component type.
     *
     * @param container    The WebElement container that contains the checkboxes.
     * @param checkBoxText The text of the checkbox to click.
     */
    default void deSelect(WebElement container, String... checkBoxText) {
        deSelect(DEFAULT_TYPE, container, checkBoxText);
    }

    /**
     * Deselects checkboxes with the specified text inside a container, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkbox.
     * @param checkBoxText  The text of the checkbox to click.
     */
    void deSelect(CheckboxComponentType componentType, WebElement container, String... checkBoxText);

    /**
     * Deselects checkboxes inside a container based on a specified strategy, using the default checkbox component type.
     *
     * @param container The WebElement container that contains the checkboxes.
     * @param strategy  The strategy to determine which checkboxes to deselect.
     * @return A string indicating the result of the deselection process.
     */
    default String deSelect(WebElement container, Strategy strategy) {
        return deSelect(DEFAULT_TYPE, container, strategy);
    }

    /**
     * Deselects checkboxes inside a container based on a specified strategy, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkboxes.
     * @param strategy      The strategy to determine which checkboxes to deselect.
     * @return A string indicating the result of the deselection process.
     */
    String deSelect(CheckboxComponentType componentType, WebElement container, Strategy strategy);

    /**
     * Deselects checkboxes with the specified text using the default checkbox component type.
     *
     * @param checkBoxText The text of the checkboxes to deselect.
     */
    default void deSelect(String... checkBoxText) {
        deSelect(DEFAULT_TYPE, checkBoxText);
    }

    /**
     * Deselects checkboxes with the specified text using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param checkBoxText  The text of the checkboxes to deselect.
     */
    void deSelect(CheckboxComponentType componentType, String... checkBoxText);

    /**
     * Deselects checkboxes identified by specified locators, using the default checkbox component type.
     *
     * @param checkBoxLocator The locators for the checkboxes to deselect.
     */
    default void deSelect(By... checkBoxLocator) {
        deSelect(DEFAULT_TYPE, checkBoxLocator);
    }

    /**
     * Deselects checkboxes identified by specified locators, using the given checkbox component type.
     *
     * @param componentType   The specific checkbox component type.
     * @param checkBoxLocator The locators for the checkboxes to deselect.
     */
    void deSelect(CheckboxComponentType componentType, By... checkBoxLocator);

    /**
     * Checks if the checkboxes with the specified text inside a container are selected, using the default checkbox component type.
     *
     * @param container    The WebElement container that contains the checkboxes.
     * @param checkBoxText The text of the checkboxes to verify.
     * @return true if all specified checkboxes are selected, false otherwise.
     */
    default boolean areSelected(WebElement container, String... checkBoxText) {
        return areSelected(DEFAULT_TYPE, container, checkBoxText);
    }

    /**
     * Checks if the checkboxes with the specified text inside a container are selected, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkboxes.
     * @param checkBoxText  The text of the checkboxes to verify.
     * @return true if all specified checkboxes are selected, false otherwise.
     */
    boolean areSelected(CheckboxComponentType componentType, WebElement container, String... checkBoxText);

    /**
     * Checks if the checkboxes with the specified text are selected, using the default checkbox component type.
     *
     * @param checkBoxText The text of the checkboxes to verify.
     * @return true if all specified checkboxes are selected, false otherwise.
     */
    default boolean areSelected(String... checkBoxText) {
        return areSelected(DEFAULT_TYPE, checkBoxText);
    }

    /**
     * Checks if the checkboxes with the specified text are selected, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param checkBoxText  The text of the checkboxes to verify.
     * @return true if all specified checkboxes are selected, false otherwise.
     */
    boolean areSelected(CheckboxComponentType componentType, String... checkBoxText);

    /**
     * Checks if the checkboxes identified by the specified locators are selected, using the default checkbox component type.
     *
     * @param checkBoxLocator The locators for the checkboxes to verify.
     * @return true if all specified checkboxes are selected, false otherwise.
     */
    default boolean areSelected(By... checkBoxLocator) {
        return areSelected(DEFAULT_TYPE, checkBoxLocator);
    }

    /**
     * Checks if the checkboxes identified by the specified locators are selected, using the given checkbox component type.
     *
     * @param componentType   The specific checkbox component type.
     * @param checkBoxLocator The locators for the checkboxes to verify.
     * @return true if all specified checkboxes are selected, false otherwise.
     */
    boolean areSelected(CheckboxComponentType componentType, By... checkBoxLocator);


    /**
     * Checks if the checkbox with the specified text inside a container is selected, using the default checkbox component type.
     *
     * @param container    The WebElement container that contains the checkbox.
     * @param checkBoxText The text of the checkbox to verify.
     * @return true if the checkbox is selected, false otherwise.
     */
    default boolean isSelected(WebElement container, String checkBoxText) {
        return areSelected(DEFAULT_TYPE, container, checkBoxText);
    }

    /**
     * Checks if the checkbox with the specified text inside a container is selected, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkbox.
     * @param checkBoxText  The text of the checkbox to verify.
     * @return true if the checkbox is selected, false otherwise.
     */
    boolean isSelected(CheckboxComponentType componentType, WebElement container, String checkBoxText);

    /**
     * Checks if the checkbox with the specified text is selected, using the default checkbox component type.
     *
     * @param checkBoxText The text of the checkbox to verify.
     * @return true if the checkbox is selected, false otherwise.
     */
    default boolean isSelected(String checkBoxText) {
        return areSelected(DEFAULT_TYPE, checkBoxText);
    }

    /**
     * Checks if the checkbox with the specified text is selected, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param checkBoxText  The text of the checkbox to verify.
     * @return true if the checkbox is selected, false otherwise.
     */
    boolean isSelected(CheckboxComponentType componentType, String checkBoxText);

    /**
     * Checks if the checkbox identified by the specified locator is selected, using the default checkbox component type.
     *
     * @param checkBoxLocator The locator for the checkbox to verify.
     * @return true if the checkbox is selected, false otherwise.
     */
    default boolean isSelected(By checkBoxLocator) {
        return areSelected(DEFAULT_TYPE, checkBoxLocator);
    }

    /**
     * Checks if the checkbox identified by the specified locator is selected, using the given checkbox component type.
     *
     * @param componentType   The specific checkbox component type.
     * @param checkBoxLocator The locator for the checkbox to verify.
     * @return true if the checkbox is selected, false otherwise.
     */
    boolean isSelected(CheckboxComponentType componentType, By checkBoxLocator);



    /**
     * Checks if the checkboxes with the specified text inside a container are enabled, using the default checkbox component type.
     *
     * @param container    The WebElement container that contains the checkboxes.
     * @param checkBoxText The text of the checkboxes to verify.
     * @return true if all specified checkboxes are enabled, false otherwise.
     */
    default boolean areEnabled(WebElement container, String... checkBoxText) {
        return areEnabled(DEFAULT_TYPE, container, checkBoxText);
    }

    /**
     * Checks if the checkboxes with the specified text inside a container are enabled, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkboxes.
     * @param checkBoxText  The text of the checkboxes to verify.
     * @return true if all specified checkboxes are enabled, false otherwise.
     */
    boolean areEnabled(CheckboxComponentType componentType, WebElement container, String... checkBoxText);

    /**
     * Checks if the checkboxes with the specified text are enabled, using the default checkbox component type.
     *
     * @param checkBoxText The text of the checkboxes to verify.
     * @return true if all specified checkboxes are enabled, false otherwise.
     */
    default boolean areEnabled(String... checkBoxText) {
        return areEnabled(DEFAULT_TYPE, checkBoxText);
    }

    /**
     * Checks if the checkboxes with the specified text are enabled, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param checkBoxText  The text of the checkboxes to verify.
     * @return true if all specified checkboxes are enabled, false otherwise.
     */
    boolean areEnabled(CheckboxComponentType componentType, String... checkBoxText);

    /**
     * Checks if the checkboxes identified by the specified locators are enabled, using the default checkbox component type.
     *
     * @param checkBoxLocator The locators for the checkboxes to verify.
     * @return true if all specified checkboxes are enabled, false otherwise.
     */
    default boolean areEnabled(By... checkBoxLocator) {
        return areEnabled(DEFAULT_TYPE, checkBoxLocator);
    }

    /**
     * Checks if the checkboxes identified by the specified locators are enabled, using the given checkbox component type.
     *
     * @param componentType   The specific checkbox component type.
     * @param checkBoxLocator The locators for the checkboxes to verify.
     * @return true if all specified checkboxes are enabled, false otherwise.
     */
    boolean areEnabled(CheckboxComponentType componentType, By... checkBoxLocator);

    /**
     * Checks if the checkbox with the specified text inside a container is enabled, using the default checkbox component type.
     *
     * @param container    The WebElement container that contains the checkbox.
     * @param checkBoxText The text of the checkbox to verify.
     * @return true if the checkbox is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container, String checkBoxText) {
        return areEnabled(DEFAULT_TYPE, container, checkBoxText);
    }

    /**
     * Checks if the checkbox with the specified text inside a container is enabled, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkbox.
     * @param checkBoxText  The text of the checkbox to verify.
     * @return true if the checkbox is enabled, false otherwise.
     */
    boolean isEnabled(CheckboxComponentType componentType, WebElement container, String checkBoxText);

    /**
     * Checks if the checkbox with the specified text is enabled, using the default checkbox component type.
     *
     * @param checkBoxText The text of the checkbox to verify.
     * @return true if the checkbox is enabled, false otherwise.
     */
    default boolean isEnabled(String checkBoxText) {
        return areEnabled(DEFAULT_TYPE, checkBoxText);
    }

    /**
     * Checks if the checkbox with the specified text is enabled, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param checkBoxText  The text of the checkbox to verify.
     * @return true if the checkbox is enabled, false otherwise.
     */
    boolean isEnabled(CheckboxComponentType componentType, String checkBoxText);

    /**
     * Checks if the checkbox identified by the specified locator is enabled, using the default checkbox component type.
     *
     * @param checkBoxLocator The locator for the checkbox to verify.
     * @return true if the checkbox is enabled, false otherwise.
     */
    default boolean isEnabled(By checkBoxLocator) {
        return areEnabled(DEFAULT_TYPE, checkBoxLocator);
    }

    /**
     * Checks if the checkbox identified by the specified locator is enabled, using the given checkbox component type.
     *
     * @param componentType   The specific checkbox component type.
     * @param checkBoxLocator The locator for the checkbox to verify.
     * @return true if the checkbox is enabled, false otherwise.
     */
    boolean isEnabled(CheckboxComponentType componentType, By checkBoxLocator);

    /**
     * Retrieves the text of all selected checkboxes inside a container, using the default checkbox component type.
     *
     * @param container The WebElement container that contains the checkboxes.
     * @return A list of strings representing the text of all selected checkboxes.
     */
    default List<String> getSelected(WebElement container) {
        return getSelected(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the text of all selected checkboxes inside a container, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkboxes.
     * @return A list of strings representing the text of all selected checkboxes.
     */
    List<String> getSelected(CheckboxComponentType componentType, WebElement container);

    /**
     * Retrieves the text of all selected checkboxes inside a container identified by the specified locator, using the default checkbox component type.
     *
     * @param containerLocator The locator for the container that contains the checkboxes.
     * @return A list of strings representing the text of all selected checkboxes.
     */
    default List<String> getSelected(By containerLocator) {
        return getSelected(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves the text of all selected checkboxes inside a container identified by the specified locator, using the given checkbox component type.
     *
     * @param componentType    The specific checkbox component type.
     * @param containerLocator The locator for the container that contains the checkboxes.
     * @return A list of strings representing the text of all selected checkboxes.
     */
    List<String> getSelected(CheckboxComponentType componentType, By containerLocator);

    /**
     * Retrieves the text of all checkboxes inside a container, regardless of their state, using the default checkbox component type.
     *
     * @param container The WebElement container that contains the checkboxes.
     * @return A list of strings representing the text of all checkboxes.
     */
    default List<String> getAll(WebElement container) {
        return getAll(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the text of all checkboxes inside a container, regardless of their state, using the given checkbox component type.
     *
     * @param componentType The specific checkbox component type.
     * @param container     The WebElement container that contains the checkboxes.
     * @return A list of strings representing the text of all checkboxes.
     */
    List<String> getAll(CheckboxComponentType componentType, WebElement container);

    /**
     * Retrieves the text of all checkboxes inside a container identified by the specified locator, regardless of their state, using the default checkbox component type.
     *
     * @param containerLocator The locator for the container that contains the checkboxes.
     * @return A list of strings representing the text of all checkboxes.
     */
    default List<String> getAll(By containerLocator) {
        return getAll(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves the text of all checkboxes inside a container identified by the specified locator, regardless of their state, using the given checkbox component type.
     *
     * @param componentType    The specific checkbox component type.
     * @param containerLocator The locator for the container that contains the checkboxes.
     * @return A list of strings representing the text of all checkboxes.
     */
    List<String> getAll(CheckboxComponentType componentType, By containerLocator);

    /**
     * Retrieves the default checkbox component type from the configuration.
     *
     * @return The default CheckboxComponentType.
     */
    private static CheckboxComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(CheckboxComponentType.class,
                uiConfig.checkboxDefaultType(),
                uiConfig.projectPackage());
    }
}
