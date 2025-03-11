package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.modal.ModalService;
import com.theairebellion.zeus.ui.selenium.ModalUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

/**
 * A fluent service class for interacting with modal UI elements in test automation.
 * Provides methods for clicking buttons within modals, retrieving modal details, and closing modals.
 *
 * @param <T> The type of the UI service fluent interface.
 *
 * @author Cyborg Code Syndicate
 */
public class ModalServiceFluent<T extends UIServiceFluent<?>> {

    private final ModalService modalService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a new {@code ModalServiceFluent} instance.
     *
     * @param uiServiceFluent The parent fluent UI service instance.
     * @param storage         The storage instance for storing validation results.
     * @param modalService    The modal service responsible for interacting with modals.
     * @param webDriver       The smart web driver used for interactions.
     */
    public ModalServiceFluent(T uiServiceFluent, Storage storage, ModalService modalService,
                              SmartWebDriver webDriver) {
        this.modalService = modalService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        this.driver = webDriver;
    }

    /**
     * Clicks a button inside the specified modal UI element.
     *
     * @param element The {@link ModalUIElement} representing the modal UI component.
     * @return The fluent UI service instance.
     */
    public T click(final ModalUIElement element) {
        Allure.step(String.format("Clicking button with locator: '%s' from button component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        modalService.clickButton(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Retrieves the title of the specified modal UI element.
     *
     * @param element The {@link ModalUIElement} representing the modal.
     * @return The fluent UI service instance.
     */
    public T getTitle(final ModalUIElement element) {
        element.before().accept(driver);
        String modalTitle = modalService.getTitle(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalTitle);
        return uiServiceFluent;
    }

    /**
     * Retrieves the content title of the specified modal UI element.
     *
     * @param element The {@link ModalUIElement} representing the modal.
     * @return The fluent UI service instance.
     */
    public T getContentTitle(final ModalUIElement element) {
        element.before().accept(driver);
        String modalContentTitle = modalService.getContentTitle(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalContentTitle);
        return uiServiceFluent;
    }

    /**
     * Retrieves the body text of the specified modal UI element.
     *
     * @param element The {@link ModalUIElement} representing the modal.
     * @return The fluent UI service instance.
     */
    public T getBodyText(final ModalUIElement element) {
        element.before().accept(driver);
        String modalBodyText = modalService.getBodyText(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalBodyText);
        return uiServiceFluent;
    }

    /**
     * Closes the specified modal UI element.
     *
     * @param element The {@link ModalUIElement} representing the modal.
     * @return The fluent UI service instance.
     */
    public T close(final ModalUIElement element) {
        element.before().accept(driver);
        modalService.close(element.componentType());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    // TODO: Implement validation functions
}
