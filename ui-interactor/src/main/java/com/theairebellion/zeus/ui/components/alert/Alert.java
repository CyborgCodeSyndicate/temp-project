package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Represents a UI alert component, typically used to display messages such as
 * success, error, or informational notifications.
 * <p>
 * Provides methods to retrieve the alert’s text and check its visibility within
 * a specified container or through a direct {@link By} locator.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Alert {

    /**
     * Retrieves the alert’s text value within a container element.
     *
     * @param container The container element enclosing the alert.
     * @return The visible text of the alert.
     */
    String getValue(SmartWebElement container);

    /**
     * Retrieves the alert’s text value identified by a locator.
     *
     * @param containerLocator The {@link By} locator for the alert element.
     * @return The visible text of the alert.
     */
    String getValue(By containerLocator);

    /**
     * Checks whether the alert is visible within a container element.
     *
     * @param container The container element enclosing the alert.
     * @return {@code true} if the alert is visible; {@code false} otherwise.
     */
    boolean isVisible(SmartWebElement container);

    /**
     * Checks whether the alert is visible based on a locator.
     *
     * @param containerLocator The {@link By} locator for the alert element.
     * @return {@code true} if the alert is visible; {@code false} otherwise.
     */
    boolean isVisible(By containerLocator);

}
