package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link TabService} interface, offering functionality for tab
 * interactions such as clicking, verifying if tabs are enabled, visible, or selected.
 * Extends {@link AbstractComponentService} to manage creation and retrieval of
 * {@link Tab} instances.
 *
 * <p>This class references {@link TabComponentType} to determine the specific tab component,
 * ensuring consistent handling of tab-based elements across different UI designs.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class TabServiceImpl extends AbstractComponentService<TabComponentType, Tab> implements TabService {

    /**
     * Constructs a new {@code TabServiceImpl} with the given {@link SmartWebDriver}.
     *
     * @param driver the smart web driver used to interact with browser elements.
     */
    public TabServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates a new {@link Tab} component for the specified {@link TabComponentType}.
     *
     * @param componentType the tab component type.
     * @return a new or existing {@link Tab} instance.
     */
    @Override
    protected Tab createComponent(final TabComponentType componentType) {
        return ComponentFactory.getTabComponent(componentType, driver);
    }

    /**
     * Clicks a tab, identified by text, within the specified container.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param tabText       the text identifying the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     */
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container,
                                                      final String tabText) {
        tabComponent((TabComponentType) componentType).click(container, tabText);
    }

    /**
     * Clicks a tab within a specified container, without referring to the tab text.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     */
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
        tabComponent((TabComponentType) componentType).click(container);
    }

    /**
     * Clicks a tab identified by text, without referencing a container.
     *
     * @param componentType the type of the tab component.
     * @param tabText       the text identifying the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     */
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String tabText) {
        tabComponent((TabComponentType) componentType).click(tabText);
    }

    /**
     * Clicks a tab identified by a locator.
     *
     * @param componentType the type of the tab component.
     * @param tabLocator    the locator for the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     */
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By tabLocator) {
        tabComponent((TabComponentType) componentType).click(tabLocator);
    }

    /**
     * Checks if a tab, identified by text, is enabled within a specified container.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param tabText       the text identifying the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is enabled, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        return tabComponent((TabComponentType) componentType).isEnabled(container, tabText);
    }

    /**
     * Checks if a tab is enabled within a specified container, without referring to tab text.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is enabled, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
        return tabComponent((TabComponentType) componentType).isEnabled(container);
    }

    /**
     * Checks if a tab, identified by text, is enabled without referencing a container.
     *
     * @param componentType the type of the tab component.
     * @param tabText       the text identifying the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is enabled, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String tabText) {
        return tabComponent((TabComponentType) componentType).isEnabled(tabText);
    }

    /**
     * Checks if a tab, identified by a locator, is enabled.
     *
     * @param componentType the type of the tab component.
     * @param tabLocator    the locator for the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is enabled, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By tabLocator) {
        return tabComponent((TabComponentType) componentType).isEnabled(tabLocator);
    }

    /**
     * Checks if a tab, identified by text, is visible within a specified container.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param tabText       the text identifying the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is visible, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        return tabComponent((TabComponentType) componentType).isVisible(container, tabText);
    }

    /**
     * Checks if a tab is visible within a specified container, without referencing tab text.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is visible, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
        return tabComponent((TabComponentType) componentType).isVisible(container);
    }

    /**
     * Checks if a tab, identified by text, is visible without referencing a container.
     *
     * @param componentType the type of the tab component.
     * @param tabText       the text identifying the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is visible, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String tabText) {
        return tabComponent((TabComponentType) componentType).isVisible(tabText);
    }

    /**
     * Checks if a tab, identified by a locator, is visible.
     *
     * @param componentType the type of the tab component.
     * @param tabLocator    the locator for the tab.
     * @param <T>           a type extending {@link ButtonComponentType}.
     * @return true if the tab is visible, otherwise false.
     */
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By tabLocator) {
        return tabComponent((TabComponentType) componentType).isVisible(tabLocator);
    }

    /**
     * Checks if a tab, identified by text, is selected within the specified container.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @param tabText       the text identifying the tab.
     * @return true if the tab is selected, otherwise false.
     */
    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container, final String tabText) {
        return tabComponent(componentType).isSelected(container, tabText);
    }

    /**
     * Checks if a tab is selected within the specified container, without referring to tab text.
     *
     * @param componentType the type of the tab component.
     * @param container     the container element holding the tab.
     * @return true if the tab is selected, otherwise false.
     */
    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container) {
        return tabComponent(componentType).isSelected(container);
    }

    /**
     * Checks if a tab, identified by text, is selected without referencing a container.
     *
     * @param componentType the type of the tab component.
     * @param tabText       the text identifying the tab.
     * @return true if the tab is selected, otherwise false.
     */
    @Override
    public boolean isSelected(final TabComponentType componentType, final String tabText) {
        return tabComponent(componentType).isSelected(tabText);
    }

    /**
     * Checks if a tab, identified by a locator, is selected.
     *
     * @param componentType the type of the tab component.
     * @param tabLocator    the locator for the tab.
     * @return true if the tab is selected, otherwise false.
     */
    @Override
    public boolean isSelected(final TabComponentType componentType, final By tabLocator) {
        return tabComponent(componentType).isSelected(tabLocator);
    }

    /**
     * Performs a table insertion action by clicking a tab within a cell element.
     *
     * @param cellElement   the table cell element containing the tab.
     * @param componentType the component type, expected to be a {@link TabComponentType}.
     * @param values        additional values (currently unused).
     */
    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        if (!(componentType instanceof TabComponentType tabType)) {
            throw new IllegalArgumentException("Component type needs to be from: TabComponentType.");
        }
        tabComponent(tabType).clickElementInCell(cellElement);
    }

    /**
     * Retrieves the {@link Tab} component for the specified {@link TabComponentType}.
     *
     * @param componentType the tab component type.
     * @return a {@link Tab} instance for managing tab interactions.
     */
    private Tab tabComponent(final TabComponentType componentType) {
        return getOrCreateComponent(componentType);
    }

}

