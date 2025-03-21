package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Represents a modal or dialog window in a UI automation context. Provides methods for
 * checking whether the modal is open, interacting with contained buttons, retrieving
 * textual content, and closing the modal.
 *
 * @author Cyborg Code Syndicate
 */
public interface Modal {

    /**
     * Checks if the modal is currently open.
     *
     * @return {@code true} if the modal is visible/active, otherwise {@code false}.
     */
    boolean isOpened();

    /**
     * Clicks a button with the specified text inside a given container.
     *
     * @param container  the container element within which the button is located.
     * @param buttonText the visible text of the button to be clicked.
     */
    void clickButton(SmartWebElement container, String buttonText);

    /**
     * Clicks a button identified by its visible text, without specifying a container.
     *
     * @param buttonText the visible text of the button to be clicked.
     */
    void clickButton(String buttonText);

    /**
     * Clicks a button identified by a locator.
     *
     * @param buttonLocator the locator referencing the button.
     */
    void clickButton(By buttonLocator);

    /**
     * Retrieves the main title text of the modal.
     *
     * @return the modal's title text, or an empty string if not present.
     */
    String getTitle();

    /**
     * Retrieves the body text of the modal.
     *
     * @return the main textual content of the modal, or an empty string if not present.
     */
    String getBodyText();

    /**
     * Retrieves the content title, which may be separate from the main title, depending on the modal structure.
     *
     * @return the content's title, or an empty string if not present.
     */
    String getContentTitle();

    /**
     * Closes or dismisses the modal.
     *
     * <p>Typically performs an action such as clicking a close button or ESC key simulation,
     * depending on the specific modal implementation.</p>
     */
    void close();

}
