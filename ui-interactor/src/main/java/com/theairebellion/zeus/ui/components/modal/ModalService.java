package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Interface defining operations for interacting with modal elements within a web interface.
 */
public interface ModalService {

    ModalComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Checks if the modal is currently opened, using the default modal component type.
     *
     * @return true if the modal is opened, false otherwise.
     */
    default boolean isOpened() {
        return isOpened(DEFAULT_TYPE);
    }

    /**
     * Checks if the modal is currently opened, using the given modal component type.
     *
     * @param componentType The specific modal component type.
     * @return true if the modal is opened, false otherwise.
     */
    boolean isOpened(ModalComponentType componentType);

    /**
     * Clicks a button inside the modal, identified by its text, using the default modal component type.
     *
     * @param container       The modal container.
     * @param modalButtonText The text of the button to click.
     */
    default void clickButton(SmartWebElement container, String modalButtonText) {
        clickButton(DEFAULT_TYPE, container, modalButtonText);
    }

    /**
     * Clicks a button inside the modal, identified by its text, using the given modal component type.
     *
     * @param componentType   The specific modal component type.
     * @param container       The modal container.
     * @param modalButtonText The text of the button to click.
     */
    void clickButton(ModalComponentType componentType, SmartWebElement container, String modalButtonText);

    /**
     * Clicks a button inside the modal, identified by its text, using the default modal component type.
     *
     * @param modalButtonText The text of the button to click.
     */
    default void clickButton(String modalButtonText) {
        clickButton(DEFAULT_TYPE, modalButtonText);
    }

    /**
     * Clicks a button inside the modal, identified by its text, using the given modal component type.
     *
     * @param componentType   The specific modal component type.
     * @param modalButtonText The text of the button to click.
     */
    void clickButton(ModalComponentType componentType, String modalButtonText);

    /**
     * Clicks a button inside the modal, identified by its locator, using the default modal component type.
     *
     * @param modalButtonLocator The locator of the button to click.
     */
    default void clickButton(By modalButtonLocator) {
        clickButton(DEFAULT_TYPE, modalButtonLocator);
    }

    /**
     * Clicks a button inside the modal, identified by its locator, using the given modal component type.
     *
     * @param componentType The specific modal component type.
     * @param modalButtonLocator The locator of the button to click.
     */
    void clickButton(ModalComponentType componentType, By modalButtonLocator);

    /**
     * Retrieves the title of the modal, using the default modal component type.
     *
     * @return The title of the modal.
     */
    default String getTitle() {
        return getTitle(DEFAULT_TYPE);
    }

    /**
     * Retrieves the title of the modal, using the given modal component type.
     *
     * @param componentType The specific modal component type.
     * @return The title of the modal.
     */
    String getTitle(ModalComponentType componentType);

    /**
     * Retrieves the body text of the modal, using the default modal component type.
     *
     * @return The body text of the modal.
     */
    default String getBodyText() {
        return getBodyText(DEFAULT_TYPE);
    }

    /**
     * Retrieves the body text of the modal, using the given modal component type.
     *
     * @param componentType The specific modal component type.
     * @return The body text of the modal.
     */
    String getBodyText(ModalComponentType componentType);

    /**
     * Retrieves the content title of the modal, using the default modal component type.
     *
     * @return The content title of the modal.
     */
    default String getContentTitle() {
        return getContentTitle(DEFAULT_TYPE);
    }

    /**
     * Retrieves the content title of the modal, using the given modal component type.
     *
     * @param componentType The specific modal component type.
     * @return The content title of the modal.
     */
    String getContentTitle(ModalComponentType componentType);

    /**
     * Closes the modal, using the default modal component type.
     */
    default void close() {
        close(DEFAULT_TYPE);
    }

    /**
     * Closes the modal, using the given modal component type.
     *
     * @param componentType The specific modal component type.
     */
    void close(ModalComponentType componentType);

    /**
     * Retrieves the default modal component type.
     *
     * @return The default modal component type.
     */
    private static ModalComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(ModalComponentType.class,
                    getUiConfig().modalDefaultType(),
                    getUiConfig().projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}
