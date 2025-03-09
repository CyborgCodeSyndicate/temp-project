package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Represents a toggle switch in a UI automation context.
 * Provides methods to activate or deactivate the toggle, and to check if it is enabled
 * or currently activated.
 *
 * <p>Implementations typically use Selenium-based interactions with container elements
 * ({@link SmartWebElement}) or direct locators ({@link By}). Whether toggles are styled
 * like classic checkboxes or custom toggle switches, this interface provides a standard
 * contract for testing or simulating user interactions.</p>
 *
 * <p>This interface can handle toggles identified by text, container references, or direct
 * locators, making it flexible for a wide range of UI designs.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Toggle {

    /**
     * Activates the toggle identified by text within the specified container.
     *
     * @param container  the container holding the toggle.
     * @param toggleText the text identifying the toggle to activate.
     */
    void activate(SmartWebElement container, String toggleText);

    /**
     * Activates the toggle identified by text without referencing a container.
     *
     * @param toggleText the text identifying the toggle to activate.
     */
    void activate(String toggleText);

    /**
     * Activates the toggle identified by a locator.
     *
     * @param toggleLocator the locator referencing the toggle to activate.
     */
    void activate(By toggleLocator);

    /**
     * Deactivates the toggle identified by text within the specified container.
     *
     * @param container  the container holding the toggle.
     * @param toggleText the text identifying the toggle to deactivate.
     */
    void deactivate(SmartWebElement container, String toggleText);

    /**
     * Deactivates the toggle identified by text without referencing a container.
     *
     * @param toggleText the text identifying the toggle to deactivate.
     */
    void deactivate(String toggleText);

    /**
     * Deactivates the toggle identified by a locator.
     *
     * @param toggleLocator the locator referencing the toggle to deactivate.
     */
    void deactivate(By toggleLocator);

    /**
     * Checks if the toggle, identified by text within a container, is enabled.
     *
     * @param container  the container holding the toggle.
     * @param toggleText the text identifying the toggle to check.
     * @return true if the toggle is enabled, otherwise false.
     */
    boolean isEnabled(SmartWebElement container, String toggleText);

    /**
     * Checks if the toggle, identified by text, is enabled without referencing a container.
     *
     * @param toggleText the text identifying the toggle to check.
     * @return true if the toggle is enabled, otherwise false.
     */
    boolean isEnabled(String toggleText);

    /**
     * Checks if the toggle, identified by a locator, is enabled.
     *
     * @param toggleLocator the locator referencing the toggle to check.
     * @return true if the toggle is enabled, otherwise false.
     */
    boolean isEnabled(By toggleLocator);

    /**
     * Checks if the toggle, identified by text within a container, is currently activated.
     *
     * @param container  the container holding the toggle.
     * @param toggleText the text identifying the toggle to check.
     * @return true if the toggle is activated, otherwise false.
     */
    boolean isActivated(SmartWebElement container, String toggleText);

    /**
     * Checks if the toggle, identified by text, is currently activated without referencing a container.
     *
     * @param toggleText the text identifying the toggle to check.
     * @return true if the toggle is activated, otherwise false.
     */
    boolean isActivated(String toggleText);

    /**
     * Checks if the toggle, identified by a locator, is currently activated.
     *
     * @param toggleLocator the locator referencing the toggle to check.
     * @return true if the toggle is activated, otherwise false.
     */
    boolean isActivated(By toggleLocator);

}
