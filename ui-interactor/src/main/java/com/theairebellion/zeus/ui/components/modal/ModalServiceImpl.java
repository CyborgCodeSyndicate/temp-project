package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link ModalService} interface, providing functionality to check
 * if modals are open, click modal buttons, retrieve textual content, and close modals.
 * Extends {@link AbstractComponentService} to handle creation and retrieval of
 * {@link Modal} instances.
 *
 * <p>This class uses {@link ModalComponentType} to identify modals in a UI automation
 * framework, ensuring consistent interactions via containers and direct locators.</p>
 *
 * <p>Method implementations delegate to an internally retrieved {@link Modal} object
 * for each specified component type.</p>
 *
 * <p>All public methods align with the contract defined by {@link ModalService},
 * ensuring compatibility and standardization across different modal implementations.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class ModalServiceImpl extends AbstractComponentService<ModalComponentType, Modal>
        implements ModalService {

    private static final String CHECK_MODAL_OPENED                   = "Checking if modal %s is opened";
    private static final String CLICK_BUTTON_WITH_TEXT_CONTAINER     = "Clicking button with text %s in modal %s using container";
    private static final String CLICK_BUTTON_WITH_TEXT               = "Clicking button with text %s in modal %s";
    private static final String CLICK_BUTTON_WITH_LOCATOR            = "Clicking button with locator %s in modal %s";
    private static final String GET_MODAL_TITLE                      = "Getting title of modal %s";
    private static final String GET_MODAL_BODY_TEXT                  = "Getting body text of modal %s";
    private static final String GET_MODAL_CONTENT_TITLE              = "Getting content title of modal %s";
    private static final String CLOSE_MODAL                          = "Closing modal %s";

    /**
     * Constructs a new {@code ModalServiceImpl} with the provided {@link SmartWebDriver}.
     *
     * @param driver the {@link SmartWebDriver} used to interact with browser elements.
     */
    public ModalServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates and returns a {@link Modal} component instance based on the provided
     * {@link ModalComponentType}.
     *
     * @param componentType the specific modal component type.
     * @return a new {@link Modal} instance for the given component type.
     */
    @Override
    protected Modal createComponent(ModalComponentType componentType) {
        return ComponentFactory.getModalComponent(componentType, driver);
    }

    /**
     * Checks whether the modal identified by the provided component type is currently open.
     *
     * @param componentType the modal component type.
     * @return true if the modal is open, otherwise false.
     */
    @Override
    public boolean isOpened(final ModalComponentType componentType) {
        LogUI.step(String.format(CHECK_MODAL_OPENED, componentType));
        return modalComponent(componentType).isOpened();
    }

    /**
     * Clicks a button inside the modal, identified by its text, within the specified container.
     *
     * @param componentType   the modal component type.
     * @param container       the container in which the button is located.
     * @param modalButtonText the text of the button to click.
     */
    @Override
    public void clickButton(final ModalComponentType componentType, final SmartWebElement container, final String modalButtonText) {
        LogUI.step(String.format(CLICK_BUTTON_WITH_TEXT_CONTAINER, modalButtonText, componentType));
        modalComponent(componentType).clickButton(container, modalButtonText);
    }

    /**
     * Clicks a button inside the modal, identified by its text, without specifying a container.
     *
     * @param componentType   the modal component type.
     * @param modalButtonText the text of the button to click.
     */
    @Override
    public void clickButton(final ModalComponentType componentType, final String modalButtonText) {
        LogUI.step(String.format(CLICK_BUTTON_WITH_TEXT, modalButtonText, componentType));
        modalComponent(componentType).clickButton(modalButtonText);
    }

    /**
     * Clicks a button inside the modal, identified by a locator.
     *
     * @param componentType      the modal component type.
     * @param modalButtonLocator the locator of the button to click.
     */
    @Override
    public void clickButton(final ModalComponentType componentType, final By modalButtonLocator) {
        LogUI.step(String.format(CLICK_BUTTON_WITH_LOCATOR, modalButtonLocator, componentType));
        modalComponent(componentType).clickButton(modalButtonLocator);
    }

    /**
     * Retrieves the title text of the modal associated with the provided component type.
     *
     * @param componentType the modal component type.
     * @return the title text of the modal, or an empty string if no title is present.
     */
    @Override
    public String getTitle(final ModalComponentType componentType) {
        LogUI.step(String.format(GET_MODAL_TITLE, componentType));
        return modalComponent(componentType).getTitle();
    }

    /**
     * Retrieves the body text of the modal associated with the provided component type.
     *
     * @param componentType the modal component type.
     * @return the main body text of the modal, or an empty string if no body text is present.
     */
    @Override
    public String getBodyText(final ModalComponentType componentType) {
        LogUI.step(String.format(GET_MODAL_BODY_TEXT, componentType));
        return modalComponent(componentType).getBodyText();
    }

    /**
     * Retrieves the content title of the modal, if separate from the main title.
     *
     * @param componentType the modal component type.
     * @return the content title, or an empty string if not present.
     */
    @Override
    public String getContentTitle(final ModalComponentType componentType) {
        LogUI.step(String.format(GET_MODAL_CONTENT_TITLE, componentType));
        return modalComponent(componentType).getContentTitle();
    }

    /**
     * Closes the modal identified by the provided component type.
     *
     * @param componentType the modal component type.
     */
    @Override
    public void close(final ModalComponentType componentType) {
        LogUI.step(String.format(CLOSE_MODAL, componentType));
        modalComponent(componentType).close();
    }

    /**
     * Retrieves or creates a modal for the specified component type.
     *
     * @param componentType the modal component type.
     * @return an existing or newly created {@link Modal}.
     */
    private Modal modalComponent(final ModalComponentType componentType) {
        return getOrCreateComponent(componentType);
    }

}
