package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Represents a loader or spinner element in a UI, providing methods to check its visibility
 * and to wait for it to appear or disappear.
 *
 * <p>Typical usage scenarios include verifying if a loader is displayed on the screen
 * and waiting until it finishes loading (is removed). Implementations are expected
 * to handle the actual waiting logic, using container or locator references, potentially
 * with explicit or implicit waits.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface Loader {

   /**
    * Checks if the loader is visible within the specified container.
    *
    * @param container a {@link SmartWebElement} representing the container holding the loader.
    * @return true if the loader is visible, false otherwise.
    */
   boolean isVisible(SmartWebElement container);

   /**
    * Checks if the loader identified by the given locator is visible.
    *
    * @param loaderLocator a {@link By} locator used to find the loader element.
    * @return true if the loader is visible, false otherwise.
    */
   boolean isVisible(By loaderLocator);

   /**
    * Waits for the loader in the specified container to become visible within a given timeout.
    *
    * @param container    the container element holding the loader.
    * @param secondsShown the maximum number of seconds to wait for the loader to appear.
    * @throws RuntimeException if the loader is not shown within the specified timeout.
    */
   void waitToBeShown(SmartWebElement container, int secondsShown);

   /**
    * Waits for a loader (implicitly identified) to become visible within a given timeout.
    *
    * @param secondsShown the maximum number of seconds to wait for the loader to appear.
    * @throws RuntimeException if the loader is not shown within the specified timeout.
    */
   void waitToBeShown(int secondsShown);

   /**
    * Waits for the loader identified by the given locator to become visible within a given timeout.
    *
    * @param loaderLocator the {@link By} locator used to find the loader element.
    * @param secondsShown  the maximum number of seconds to wait for the loader to appear.
    * @throws RuntimeException if the loader is not shown within the specified timeout.
    */
   void waitToBeShown(By loaderLocator, int secondsShown);

   /**
    * Waits for the loader in the specified container to be removed within a given timeout.
    *
    * @param container      the container element holding the loader.
    * @param secondsRemoved the maximum number of seconds to wait for the loader to disappear.
    * @throws RuntimeException if the loader is not removed within the specified timeout.
    */
   void waitToBeRemoved(SmartWebElement container, int secondsRemoved);

   /**
    * Waits for a loader (implicitly identified) to be removed within a given timeout.
    *
    * @param secondsRemoved the maximum number of seconds to wait for the loader to disappear.
    * @throws RuntimeException if the loader is not removed within the specified timeout.
    */
   void waitToBeRemoved(int secondsRemoved);

   /**
    * Waits for the loader identified by the given locator to be removed within a given timeout.
    *
    * @param loaderLocator  the {@link By} locator used to find the loader element.
    * @param secondsRemoved the maximum number of seconds to wait for the loader to disappear.
    * @throws RuntimeException if the loader is not removed within the specified timeout.
    */
   void waitToBeRemoved(By loaderLocator, int secondsRemoved);
}

