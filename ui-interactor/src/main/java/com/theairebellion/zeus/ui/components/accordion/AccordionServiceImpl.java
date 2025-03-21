package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Default implementation of {@link AccordionService} that delegates accordion actions
 * to a cached or newly created {@link Accordion} instance.
 * <p>
 * This class extends {@link AbstractComponentService}, enabling the framework
 * to maintain and reuse accordion implementations across multiple test interactions.
 * Each method resolves the appropriate {@link AccordionComponentType} to retrieve
 * the correct implementation, then calls the matching methods on the retrieved
 * {@link Accordion}.
 * </p>
 *
 * <p>
 * Typical usage involves specifying which accordion component type to interact with
 * (e.g., different Material Design or Bootstrap variants), and then calling methods
 * like {@code expand()}, {@code collapse()}, or {@code getExpanded()} to operate on
 * those panels.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class AccordionServiceImpl extends AbstractComponentService<AccordionComponentType, Accordion>
        implements AccordionService {

    /**
     * Constructs a new {@code AccordionServiceImpl} with the given driver.
     *
     * @param driver The {@link SmartWebDriver} instance used for UI interactions.
     */
    public AccordionServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates a new {@link Accordion} instance for the specified type using the {@link ComponentFactory}.
     *
     * @param componentType The enum constant representing the accordion type.
     * @return A new or cached {@link Accordion} instance for the given type.
     */
    @Override
    protected Accordion createComponent(final AccordionComponentType componentType) {
        return ComponentFactory.getAccordionComponent(componentType, driver);
    }

    /**
     * Expands specified panels inside the accordion container for a given type.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @param accordionText The text of the panels to expand.
     */
    @Override
    public void expand(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        LogUI.step("Expanding accordion {} inside container with text: {}", componentType, accordionText);
        accordionComponent(componentType).expand(container, accordionText);
    }

    /**
     * Expands an accordion panel using a strategy for panel selection.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @param strategy      The selection strategy (e.g. random, first).
     * @return The text or identifier of the panel that was expanded.
     */
    @Override
    public String expand(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Expanding accordion {} inside container using strategy: {}", componentType, strategy);
        return accordionComponent(componentType).expand(container, strategy);
    }

    /**
     * Expands panels identified by text labels for a given accordion type.
     *
     * @param componentType The specific accordion component type.
     * @param accordionText The text of the panels to expand.
     */
    @Override
    public void expand(final AccordionComponentType componentType, final String... accordionText) {
        LogUI.step("Expanding accordion {} with text: {}", componentType, accordionText);
        accordionComponent(componentType).expand(accordionText);
    }

    /**
     * Expands accordion panels identified by locators for a given type.
     *
     * @param componentType    The specific accordion component type.
     * @param accordionLocator One or more locators identifying the panels to expand.
     */
    @Override
    public void expand(final AccordionComponentType componentType, final By... accordionLocator) {
        LogUI.step("Expanding accordion {} using locators", componentType);
        accordionComponent(componentType).expand(accordionLocator);
    }

    /**
     * Collapses specified panels inside the accordion container for a given type.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @param accordionText The text of the panels to collapse.
     */
    @Override
    public void collapse(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        LogUI.step("Collapsing accordion {} inside container with text: {}", componentType, accordionText);
        accordionComponent(componentType).collapse(container, accordionText);
    }

    /**
     * Collapses an accordion panel using a strategy for panel selection.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @param strategy      The selection strategy (e.g. random, first).
     * @return The text or identifier of the panel that was collapsed.
     */
    @Override
    public String collapse(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Collapsing accordion {} inside container using strategy: {}", componentType, strategy);
        return accordionComponent(componentType).collapse(container, strategy);
    }

    /**
     * Collapses panels identified by text labels for a given accordion type.
     *
     * @param componentType The specific accordion component type.
     * @param accordionText The text of the panels to collapse.
     */
    @Override
    public void collapse(final AccordionComponentType componentType, final String... accordionText) {
        LogUI.step("Collapsing accordion {} with text: {}", componentType, accordionText);
        accordionComponent(componentType).collapse(accordionText);
    }

    /**
     * Collapses accordion panels identified by locators for a given type.
     *
     * @param componentType    The specific accordion component type.
     * @param accordionLocator One or more locators identifying the panels to collapse.
     */
    @Override
    public void collapse(final AccordionComponentType componentType, final By... accordionLocator) {
        LogUI.step("Collapsing accordion {} using locators", componentType);
        accordionComponent(componentType).collapse(accordionLocator);
    }

    /**
     * Checks if specified accordion panels are enabled for a given type, within a specific container.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @param accordionText The text of the panels to check.
     * @return {@code true} if all specified panels are enabled, otherwise {@code false}.
     */
    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        LogUI.step("Checking if accordion {} is enabled inside container with text: {}", componentType, accordionText);
        return accordionComponent(componentType).areEnabled(container, accordionText);
    }

    /**
     * Checks if specified accordion panels are enabled for a given type, identified by text.
     *
     * @param componentType The specific accordion component type.
     * @param accordionText The text of the panels to check.
     * @return {@code true} if all specified panels are enabled, otherwise {@code false}.
     */
    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final String... accordionText) {
        LogUI.step("Checking if accordion {} is enabled with text: {}", componentType, accordionText);
        return accordionComponent(componentType).areEnabled(accordionText);
    }

    /**
     * Checks if accordion panels identified by locators are enabled for a given type.
     *
     * @param componentType    The specific accordion component type.
     * @param accordionLocator One or more locators identifying the panels to check.
     * @return {@code true} if all specified panels are enabled, otherwise {@code false}.
     */
    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final By... accordionLocator) {
        LogUI.step("Checking if accordion {} is enabled using locators", componentType);
        return accordionComponent(componentType).areEnabled(accordionLocator);
    }

    /**
     * Retrieves a list of expanded panels for a given type, within a specific container.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @return A list of expanded panel titles.
     */
    @Override
    public List<String> getExpanded(final AccordionComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting expanded accordions for {}", componentType);
        return accordionComponent(componentType).getExpanded(container);
    }

    /**
     * Retrieves a list of collapsed panels for a given type, within a specific container.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @return A list of collapsed panel titles.
     */
    @Override
    public List<String> getCollapsed(final AccordionComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting collapsed accordions for {}", componentType);
        return accordionComponent(componentType).getCollapsed(container);
    }

    /**
     * Retrieves a list of all panel texts (both expanded and collapsed) for a given type
     * within a specific container.
     *
     * @param componentType The specific accordion component type.
     * @param container     The container holding the accordion.
     * @return A list of all panel texts.
     */
    @Override
    public List<String> getAll(final AccordionComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting all accordions for {}", componentType);
        return accordionComponent(componentType).getAll(container);
    }

    /**
     * Retrieves the title of an accordion panel for a given type, identified by a locator.
     *
     * @param componentType    The specific accordion component type.
     * @param accordionLocator The locator of the panel.
     * @return The panel's title text.
     */
    @Override
    public String getTitle(final AccordionComponentType componentType, final By accordionLocator) {
        LogUI.step("Getting title for accordion {} using locator", componentType);
        return accordionComponent(componentType).getTitle(accordionLocator);
    }

    /**
     * Retrieves the text content of an accordion panel for a given type, identified by a locator.
     *
     * @param componentType    The specific accordion component type.
     * @param accordionLocator The locator of the panel.
     * @return The panel's text content.
     */
    @Override
    public String getText(final AccordionComponentType componentType, final By accordionLocator) {
        LogUI.step("Getting text for accordion {} using locator", componentType);
        return accordionComponent(componentType).getText(accordionLocator);
    }

    /**
     * Retrieves or creates (caches) the {@link Accordion} implementation for the specified type.
     *
     * @param componentType The enum constant representing the accordion type.
     * @return The accordion implementation associated with the given type.
     */
    private Accordion accordionComponent(final AccordionComponentType componentType) {
        return getOrCreateComponent(componentType);
    }

}
