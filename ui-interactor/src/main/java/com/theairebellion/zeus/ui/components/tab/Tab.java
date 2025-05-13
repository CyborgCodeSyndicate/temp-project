package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Represents a specialized tab component that also behaves like a {@link Button}, enabling
 * clicking actions and selection checks in a UI automation framework. Provides methods
 * to verify if a tab is selected by text or locator, optionally within a container.
 *
 * <p>Classes implementing this interface typically utilize Selenium-based approaches to
 * locate and interact with tab elements, whether identified by text, a container element,
 * or direct locators.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface Tab extends Button {

   /**
    * Checks if the tab is selected within the specified container, identified by the tab's text.
    *
    * @param container  the container holding the tab elements.
    * @param buttonText the text of the tab to check.
    * @return true if the tab is selected, otherwise false.
    */
   boolean isSelected(SmartWebElement container, String buttonText);

   /**
    * Checks if the tab is selected within the specified container, without specifying the tab text.
    *
    * @param container the container holding the tab elements.
    * @return true if the tab is selected, otherwise false.
    */
   boolean isSelected(SmartWebElement container);

   /**
    * Checks if the tab, identified by text, is selected without referencing a container.
    *
    * @param buttonText the text of the tab to check.
    * @return true if the tab is selected, otherwise false.
    */
   boolean isSelected(String buttonText);

   /**
    * Checks if the tab, identified by a locator, is selected.
    *
    * @param buttonLocator the locator referencing the tab.
    * @return true if the tab is selected, otherwise false.
    */
   boolean isSelected(By buttonLocator);
}
