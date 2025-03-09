package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Implementation of the {@link RadioService} interface, providing methods to select radio buttons,
 * verify their state (enabled, selected, visible), and retrieve their text values. Extends
 * {@link AbstractComponentService} to manage creation and retrieval of {@link Radio} instances.
 *
 * <p>This class delegates core functionality to an internally retrieved {@link Radio} instance,
 * created for each specified {@link RadioComponentType}. Methods conform to the {@link RadioService}
 * contract, ensuring a uniform approach to handling radio buttons across various UI designs.</p>
 *
 * <p>All public methods handle operations such as checking button states, locating elements by
 * container or locator, and selecting them by text or strategy. The {@code insertion} method also
 * implements the {@link com.theairebellion.zeus.ui.insertion.Insertion} interface for data
 * insertion scenarios.</p>
 *
 * <p>
 * @author Cyborg Code Syndicate
 */
public class RadioServiceImpl extends AbstractComponentService<RadioComponentType, Radio> implements RadioService {

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
    public String select(final RadioComponentType componentType, final SmartWebElement container, final Strategy strategy) {
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
        select((RadioComponentType) componentType, (String) values[0]);
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
