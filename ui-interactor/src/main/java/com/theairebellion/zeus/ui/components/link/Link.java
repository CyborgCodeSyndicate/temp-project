package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Defines operations for interacting with link components.
 * <p>
 * This interface extends {@link Button} and provides additional functionality
 * for handling link elements, including support for double-click actions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Link extends Button {

    /**
     * Performs a double-click action on a link with the specified text within the given container.
     *
     * @param container  The container holding the link.
     * @param buttonText The text of the link to be double-clicked.
     */
    void doubleClick(SmartWebElement container, String buttonText);

    /**
     * Performs a double-click action on a link within the specified container.
     *
     * @param container The container holding the link.
     */
    void doubleClick(SmartWebElement container);

    /**
     * Performs a double-click action on a link identified by its text.
     *
     * @param buttonText The text of the link to be double-clicked.
     */
    void doubleClick(String buttonText);

    /**
     * Performs a double-click action on a link located using the specified locator.
     *
     * @param buttonLocator The locator identifying the link.
     */
    void doubleClick(By buttonLocator);
}
