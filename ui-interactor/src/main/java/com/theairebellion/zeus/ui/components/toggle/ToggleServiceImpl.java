package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link ToggleService} interface, providing methods to activate or
 * deactivate toggles, as well as check if they are enabled or activated. Extends
 * {@link AbstractComponentService} to manage creation and retrieval of {@link Toggle} instances.
 *
 * <p>This class references {@link ToggleComponentType} to identify the correct toggle component,
 * ensuring consistent behavior for a variety of toggle-like elements (switches, checkboxes, etc.)
 * across different UI designs.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class ToggleServiceImpl extends AbstractComponentService<ToggleComponentType, Toggle> implements ToggleService {

    private static final String ACTIVATE_TEXT = "Activating toggle %s for toggle component %s";
    private static final String ACTIVATE_LOCATOR = "Activating toggle with locator %s for toggle component %s";
    private static final String ACTIVATE_CONTAINER = "Activating toggle %s in container %s for toggle component %s";

    private static final String DEACTIVATE_TEXT = "Deactivating toggle %s for toggle component %s";
    private static final String DEACTIVATE_LOCATOR = "Deactivating toggle with locator %s for toggle component %s";
    private static final String DEACTIVATE_CONTAINER = "Deactivating toggle %s in container %s for toggle component %s";

    private static final String IS_ENABLED_TEXT = "Checking if toggle %s is enabled for toggle component %s";
    private static final String IS_ENABLED_LOCATOR = "Checking if toggle with locator %s is enabled for toggle component %s";
    private static final String IS_ENABLED_CONTAINER = "Checking if toggle %s is enabled in container %s for toggle component %s";

    private static final String IS_ACTIVATED_TEXT = "Checking if toggle %s is activated for toggle component %s";
    private static final String IS_ACTIVATED_LOCATOR = "Checking if toggle with locator %s is activated for toggle component %s";
    private static final String IS_ACTIVATED_CONTAINER = "Checking if toggle %s is activated in container %s for toggle component %s";

    /**
     * Constructs a new {@code ToggleServiceImpl} with the specified {@link SmartWebDriver}.
     *
     * @param driver the smart web driver for interacting with browser elements.
     */
    public ToggleServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates a {@link Toggle} instance for the given {@link ToggleComponentType}.
     *
     * @param componentType the toggle component type.
     * @return a new or existing {@link Toggle} instance.
     */
    @Override
    protected Toggle createComponent(ToggleComponentType componentType) {
        return ComponentFactory.getToggleComponent(componentType, driver);
    }

    /**
     * Activates a toggle, identified by text, within a specified container.
     *
     * @param componentType the toggle component type.
     * @param container     the container element holding the toggle.
     * @param toggleText    the text identifying the toggle.
     */
    @Override
    public void activate(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step(String.format(ACTIVATE_CONTAINER, toggleText, container, componentType));
        toggleComponent(componentType).activate(container, toggleText);
    }

    /**
     * Activates a toggle, identified by text, without referencing a container.
     *
     * @param componentType the toggle component type.
     * @param toggleText    the text identifying the toggle.
     */
    @Override
    public void activate(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step(String.format(ACTIVATE_TEXT, toggleText, componentType));
        toggleComponent(componentType).activate(toggleText);
    }

    /**
     * Activates a toggle, identified by a locator.
     *
     * @param componentType the toggle component type.
     * @param toggleLocator the locator referencing the toggle.
     */
    @Override
    public void activate(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step(String.format(ACTIVATE_LOCATOR, toggleLocator, componentType));
        toggleComponent(componentType).activate(toggleLocator);
    }

    /**
     * Deactivates a toggle, identified by text, within a specified container.
     *
     * @param componentType the toggle component type.
     * @param container     the container element holding the toggle.
     * @param toggleText    the text identifying the toggle.
     */
    @Override
    public void deactivate(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step(String.format(DEACTIVATE_CONTAINER, toggleText, container, componentType));
        toggleComponent(componentType).deactivate(container, toggleText);
    }

    /**
     * Deactivates a toggle, identified by text, without referencing a container.
     *
     * @param componentType the toggle component type.
     * @param toggleText    the text identifying the toggle.
     */
    @Override
    public void deactivate(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step(String.format(DEACTIVATE_TEXT, toggleText, componentType));
        toggleComponent(componentType).deactivate(toggleText);
    }

    /**
     * Deactivates a toggle, identified by a locator.
     *
     * @param componentType the toggle component type.
     * @param toggleLocator the locator referencing the toggle.
     */
    @Override
    public void deactivate(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step(String.format(DEACTIVATE_LOCATOR, toggleLocator, componentType));
        toggleComponent(componentType).deactivate(toggleLocator);
    }

    /**
     * Checks if a toggle, identified by text within a container, is enabled.
     *
     * @param componentType the toggle component type.
     * @param container     the container holding the toggle.
     * @param toggleText    the text identifying the toggle.
     * @return true if enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step(String.format(IS_ENABLED_CONTAINER, toggleText, container, componentType));
        return toggleComponent(componentType).isEnabled(container, toggleText);
    }

    /**
     * Checks if a toggle, identified by text, is enabled without referencing a container.
     *
     * @param componentType the toggle component type.
     * @param toggleText    the text identifying the toggle.
     * @return true if enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step(String.format(IS_ENABLED_TEXT, toggleText, componentType));
        return toggleComponent(componentType).isEnabled(toggleText);
    }

    /**
     * Checks if a toggle, identified by a locator, is enabled.
     *
     * @param componentType the toggle component type.
     * @param toggleLocator the locator referencing the toggle.
     * @return true if enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step(String.format(IS_ENABLED_LOCATOR, toggleLocator, componentType));
        return toggleComponent(componentType).isEnabled(toggleLocator);
    }

    /**
     * Checks if a toggle, identified by text within a container, is currently activated.
     *
     * @param componentType the toggle component type.
     * @param container     the container holding the toggle.
     * @param toggleText    the text identifying the toggle.
     * @return true if activated, otherwise false.
     */
    @Override
    public boolean isActivated(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step(String.format(IS_ACTIVATED_CONTAINER, toggleText, container, componentType));
        return toggleComponent(componentType).isActivated(container, toggleText);
    }

    /**
     * Checks if a toggle, identified by text, is currently activated, without referencing a container.
     *
     * @param componentType the toggle component type.
     * @param toggleText    the text identifying the toggle.
     * @return true if activated, otherwise false.
     */
    @Override
    public boolean isActivated(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step(String.format(IS_ACTIVATED_TEXT, toggleText, componentType));
        return toggleComponent(componentType).isActivated(toggleText);
    }

    /**
     * Checks if a toggle, identified by a locator, is currently activated.
     *
     * @param componentType the toggle component type.
     * @param toggleLocator the locator referencing the toggle.
     * @return true if activated, otherwise false.
     */
    @Override
    public boolean isActivated(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step(String.format(IS_ACTIVATED_LOCATOR, toggleLocator, componentType));
        return toggleComponent(componentType).isActivated(toggleLocator);
    }

    /**
     * Retrieves or creates the {@link Toggle} instance for the specified component type.
     *
     * @param componentType the toggle component type.
     * @return a {@link Toggle} instance corresponding to the given type.
     */
    private Toggle toggleComponent(final ToggleComponentType componentType) {
        return getOrCreateComponent(componentType);
    }

}
