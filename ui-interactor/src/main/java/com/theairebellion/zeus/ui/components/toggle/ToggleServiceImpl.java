package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

import java.util.Map;

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

    /**
     * Map storing references to toggle components by their type.
     * Currently unused, but declared for potential future caching or sharing of components.
     */
    private static Map<ToggleComponentType, Toggle> components;

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
        Allure.step(String.format("[UI - Toggle] Activating toggle %s in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Activating toggle " + toggleText + " in container " + container + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Activating toggle %s for toggle component %s", toggleText, componentType));
        LogUI.step("Activating toggle " + toggleText + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Activating toggle with locator %s for toggle component %s", toggleLocator, componentType));
        LogUI.step("Activating toggle with locator " + toggleLocator + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Deactivating toggle %s in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Deactivating toggle " + toggleText + " in container " + container + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Deactivating toggle %s for toggle component %s", toggleText, componentType));
        LogUI.step("Deactivating toggle " + toggleText + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Deactivating toggle with locator %s for toggle component %s", toggleLocator, componentType));
        LogUI.step("Deactivating toggle with locator " + toggleLocator + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is enabled in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is enabled in container " + container + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is enabled for toggle component %s", toggleText, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is enabled for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Checking if toggle with locator %s is enabled for toggle component %s", toggleLocator, componentType));
        LogUI.step("Checking if toggle with locator " + toggleLocator + " is enabled for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is activated in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is activated in container " + container + " for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is activated for toggle component %s", toggleText, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is activated for toggle component " + componentType);
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
        Allure.step(String.format("[UI - Toggle] Checking if toggle with locator %s is activated for toggle component %s", toggleLocator, componentType));
        LogUI.step("Checking if toggle with locator " + toggleLocator + " is activated for toggle component " + componentType);
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
