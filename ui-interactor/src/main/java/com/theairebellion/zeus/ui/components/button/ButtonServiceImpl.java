package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

/**
 * Provides service-level operations for interacting with button components.
 * <p>
 * This class manages button interactions (clicks, state checks, and table insertions)
 * by delegating to the appropriate {@link Button} implementation based on the
 * provided {@link ButtonComponentType}.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class ButtonServiceImpl extends AbstractComponentService<ButtonComponentType, Button> implements ButtonService {

    /**
     * Constructs a new ButtonServiceImpl using the specified SmartWebDriver.
     *
     * @param driver the SmartWebDriver used for UI interactions.
     */
    public ButtonServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates a new Button instance for the given component type.
     *
     * @param componentType the button component type.
     * @return a new or cached Button instance.
     */
    @Override
    protected Button createComponent(final ButtonComponentType componentType) {
        return ComponentFactory.getButtonComponent(componentType, driver);
    }

    /**
     * Clicks a button with the specified text inside a container.
     *
     * @param componentType the button component type.
     * @param container     the container holding the button.
     * @param buttonText    the text of the button to click.
     */
    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container,
                      final String buttonText) {
        Allure.step(String.format("[UI - Button] Clicking button: %s in container", buttonText));
        LogUI.step("Clicking button: " + buttonText + " in container");
        buttonComponent(componentType).click(container, buttonText);
    }

    /**
     * Clicks a button inside a container.
     *
     * @param componentType the button component type.
     * @param container     the container holding the button.
     */
    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container) {
        Allure.step("[UI - Button] Clicking button in container");
        LogUI.step("Clicking button in container");
        buttonComponent(componentType).click(container);
    }

    /**
     * Clicks a button identified by its text.
     *
     * @param componentType the button component type.
     * @param buttonText    the text of the button to click.
     */
    @Override
    public void click(final ButtonComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Button] Clicking button: %s", buttonText));
        LogUI.step("Clicking button: " + buttonText);
        buttonComponent(componentType).click(buttonText);
    }

    /**
     * Clicks a button identified by a locator.
     *
     * @param componentType the button component type.
     * @param buttonLocator the locator for the button.
     */
    @Override
    public void click(final ButtonComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Button] Clicking button using locator: %s", buttonLocator));
        LogUI.step("Clicking button using locator: " + buttonLocator);
        buttonComponent(componentType).click(buttonLocator);
    }

    /**
     * Checks if a button with the specified text inside a container is enabled.
     *
     * @param componentType the button component type.
     * @param container     the container holding the button.
     * @param buttonText    the text of the button to check.
     * @return true if the button is enabled; false otherwise.
     */
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled: %s", buttonText));
        LogUI.step("Checking if button is enabled: " + buttonText);
        return buttonComponent(componentType).isEnabled(container, buttonText);
    }

    /**
     * Checks if a button inside a container is enabled.
     *
     * @param componentType the button component type.
     * @param container     the container holding the button.
     * @return true if the button is enabled; false otherwise.
     */
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container) {
        Allure.step("[UI - Button] Checking if button is enabled in container");
        LogUI.step("Checking if button is enabled in container");
        return buttonComponent(componentType).isEnabled(container);
    }

    /**
     * Checks if a button identified by its text is enabled.
     *
     * @param componentType the button component type.
     * @param buttonText    the text of the button to check.
     * @return true if the button is enabled; false otherwise.
     */
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled: %s", buttonText));
        LogUI.step("Checking if button is enabled: " + buttonText);
        return buttonComponent(componentType).isEnabled(buttonText);
    }

    /**
     * Checks if a button identified by a locator is enabled.
     *
     * @param componentType the button component type.
     * @param buttonLocator the locator for the button.
     * @return true if the button is enabled; false otherwise.
     */
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled using locator: %s", buttonLocator));
        LogUI.step("Checking if button is enabled using locator: " + buttonLocator);
        return buttonComponent(componentType).isEnabled(buttonLocator);
    }

    /**
     * Checks if a button with the specified text inside a container is visible.
     *
     * @param componentType the button component type.
     * @param container     the container holding the button.
     * @param buttonText    the text of the button to check.
     * @return true if the button is visible; false otherwise.
     */
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is visible: %s", buttonText));
        LogUI.step("Checking if button is visible: " + buttonText);
        return buttonComponent(componentType).isVisible(container, buttonText);
    }

    /**
     * Checks if a button inside a container is visible.
     *
     * @param componentType the button component type.
     * @param container     the container holding the button.
     * @return true if the button is visible; false otherwise.
     */
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container) {
        Allure.step("[UI - Button] Checking if button is visible in container");
        LogUI.step("Checking if button is visible in container");
        return buttonComponent(componentType).isVisible(container);
    }

    /**
     * Checks if a button identified by its text is visible.
     *
     * @param componentType the button component type.
     * @param buttonText    the text of the button to check.
     * @return true if the button is visible; false otherwise.
     */
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is visible: %s", buttonText));
        LogUI.step("Checking if button is visible: " + buttonText);
        return buttonComponent(componentType).isVisible(buttonText);
    }

    /**
     * Checks if a button identified by a locator is visible.
     *
     * @param componentType the button component type.
     * @param buttonLocator the locator for the button.
     * @return true if the button is visible; false otherwise.
     */
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Button] Checking if button is visible using locator: %s", buttonLocator));
        LogUI.step("Checking if button is visible using locator: " + buttonLocator);
        return buttonComponent(componentType).isVisible(buttonLocator);
    }

    /**
     * Retrieves the Button instance for the given component type.
     *
     * @param componentType the button component type.
     * @return the Button instance.
     */
    private Button buttonComponent(final ButtonComponentType componentType) {
        return getOrCreateComponent(componentType);
    }

    /**
     * Performs a table insertion action on a cell element using the button component.
     *
     * @param cellElement   the cell element where the button is located.
     * @param componentType the button component type.
     * @param values        optional values for the insertion action.
     */
    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        Allure.step("[UI - Button] Performing table insertion");
        LogUI.step("Performing table insertion in cell element");
        buttonComponent((ButtonComponentType) componentType).clickElementInCell(cellElement);
    }

}
