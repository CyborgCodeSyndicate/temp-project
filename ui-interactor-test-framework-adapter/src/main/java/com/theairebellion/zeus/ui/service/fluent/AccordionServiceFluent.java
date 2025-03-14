package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;
import com.theairebellion.zeus.ui.components.accordion.AccordionService;
import com.theairebellion.zeus.ui.selenium.AccordionUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

/**
 * Provides fluent API methods for interacting with Accordion UI components.
 * <p>
 * This class encapsulates interactions with Accordion elements, allowing actions such as expansion,
 * collapsing, retrieving states, and verifying their properties. It integrates with
 * {@link AccordionService} to perform operations in a structured manner.
 * </p>
 *
 * <p>
 * The generic type {@code T} represents the main UI service fluent class that this service extends,
 * allowing method chaining within the fluent API structure.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class AccordionServiceFluent<T extends UIServiceFluent<?>> {

    private final AccordionService accordionService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs an instance of {@link AccordionServiceFluent}.
     *
     * @param uiServiceFluent  The main UI service fluent instance.
     * @param storage          The storage object for maintaining test state.
     * @param accordionService The service handling accordion interactions.
     * @param webDriver        The instance of {@link SmartWebDriver}.
     */
    public AccordionServiceFluent(T uiServiceFluent, Storage storage, AccordionService accordionService,
                                  SmartWebDriver webDriver) {
        this.accordionService = accordionService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        this.driver = webDriver;
    }

    /**
     * Expands the specified accordion panel.
     *
     * @param element The accordion UI element to expand.
     * @return The current fluent service instance for method chaining.
     */
    public T expand(final AccordionUIElement element) {
        element.before().accept(driver);
        accordionService.expand((AccordionComponentType) element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Collapses the specified accordion panel.
     *
     * @param element The accordion UI element to collapse.
     * @return The current fluent service instance for method chaining.
     */
    public T collapse(final AccordionUIElement element) {
        element.before().accept(driver);
        accordionService.collapse((AccordionComponentType) element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Checks if the specified accordion element is enabled.
     *
     * @param element The accordion UI element to check.
     * @return The current fluent service instance for method chaining.
     */
    public T areEnabled(final AccordionUIElement element) {
        element.before().accept(driver);
        boolean enabled = accordionService.areEnabled((AccordionComponentType) element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Retrieves a list of expanded accordion sections.
     *
     * @param element The accordion UI element to check.
     * @return The current fluent service instance for method chaining.
     */
    public T getExpanded(final AccordionUIElement element) {
        element.before().accept(driver);
        List<String> expanded = accordionService.getExpanded(element.componentType()); // TODO: Implement further validation
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), expanded);
        return uiServiceFluent;
    }

    /**
     * Retrieves a list of collapsed accordion sections.
     *
     * @param element The accordion UI element to check.
     * @return The current fluent service instance for method chaining.
     */
    public T getCollapsed(final AccordionUIElement element) {
        element.before().accept(driver);
        List<String> collapsed = accordionService.getCollapsed(element.componentType()); // TODO: Implement further validation
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), collapsed);
        return uiServiceFluent;
    }

    /**
     * Retrieves a list of all accordion sections.
     *
     * @param element The accordion UI element to check.
     * @return The current fluent service instance for method chaining.
     */
    public T getAll(final AccordionUIElement element) {
        element.before().accept(driver);
        List<String> all = accordionService.getAll(element.componentType()); // TODO: Implement further validation
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), all);
        return uiServiceFluent;
    }

    /**
     * Retrieves the title of the specified accordion element.
     *
     * @param element The accordion UI element.
     * @return The current fluent service instance for method chaining.
     */
    public T getTitle(final AccordionUIElement element) {
        element.before().accept(driver);
        String title = accordionService.getTitle(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), title);
        return uiServiceFluent;
    }

    /**
     * Retrieves the text content of the specified accordion element.
     *
     * @param element The accordion UI element.
     * @return The current fluent service instance for method chaining.
     */
    public T getText(final AccordionUIElement element) {
        element.before().accept(driver);
        String text = accordionService.getText(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), text);
        return uiServiceFluent;
    }

    // TODO: Implement validation functions
}
