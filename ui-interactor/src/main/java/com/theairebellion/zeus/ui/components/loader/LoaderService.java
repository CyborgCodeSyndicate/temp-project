package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides an interface for interacting with loader elements in a UI automation framework,
 * enabling checks for visibility and waiting mechanisms for when loaders appear or disappear.
 * By referencing a {@link LoaderComponentType} and the {@link #DEFAULT_TYPE}, implementations
 * integrate seamlessly with broader test automation pipelines.
 *
 * <p>Classes implementing this interface typically rely on Selenium-based operations for
 * synchronization, ensuring loaders are properly shown or removed within expected timeframes.
 *
 * <p>All methods are designed to support both container-based and locator-based identification
 * of loader elements, offering a flexible approach to handle various UI designs.
 *
 * <p>Methods in this interface throw runtime exceptions if loaders do not appear or disappear
 * within the specified time, allowing test flows to handle unexpected loading behavior.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface LoaderService {

   LoaderComponentType DEFAULT_TYPE = getDefaultType();

   /**
    * Retrieves the default loader component type from the configuration.
    *
    * @return The default LoaderComponentType.
    */
   private static LoaderComponentType getDefaultType() {
      try {
         return ReflectionUtil.findEnumImplementationsOfInterface(LoaderComponentType.class,
               getUiConfig().loaderDefaultType(),
               getUiConfig().projectPackage());
      } catch (Exception ignored) {
         return null;
      }
   }

   /**
    * Checks if a loader is visible within the specified container using the default loader component type.
    *
    * @param container The SmartWebElement container to check for the presence of the loader.
    * @return true if the loader is visible, false otherwise.
    */
   default boolean isVisible(SmartWebElement container) {
      return isVisible(DEFAULT_TYPE, container);
   }

   /**
    * Checks if a loader is visible within the specified container using the given loader component type.
    *
    * @param componentType The specific loader component type.
    * @param container     The SmartWebElement container to check for the presence of the loader.
    * @return true if the loader is visible, false otherwise.
    */
   boolean isVisible(LoaderComponentType componentType, SmartWebElement container);

   /**
    * Checks if a loader is visible using the specified locator and the default loader component type.
    *
    * @param loaderLocator The By locator to check for the presence of the loader.
    * @return true if the loader is visible, false otherwise.
    */
   default boolean isVisible(By loaderLocator) {
      return isVisible(DEFAULT_TYPE, loaderLocator);
   }

   /**
    * Checks if a loader is visible using the specified locator and loader component type.
    *
    * @param componentType The specific loader component type.
    * @param loaderLocator The By locator to check for the presence of the loader.
    * @return true if the loader is visible, false otherwise.
    */
   boolean isVisible(LoaderComponentType componentType, By loaderLocator);

   /**
    * Waits for the loader to be shown within the specified container for the given duration, using the default loader
    * component type.
    *
    * @param container    The SmartWebElement container where the loader is expected to appear.
    * @param secondsShown The maximum time to wait for the loader to be shown, in seconds.
    */
   default void waitToBeShown(SmartWebElement container, int secondsShown) {
      waitToBeShown(DEFAULT_TYPE, container, secondsShown);
   }

   /**
    * Waits for the loader to be shown within the specified container for the given duration, using the given loader
    * component type.
    *
    * @param componentType The specific loader component type.
    * @param container     The SmartWebElement container where the loader is expected to appear.
    * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
    */
   void waitToBeShown(LoaderComponentType componentType, SmartWebElement container, int secondsShown);

   /**
    * Waits for the loader to be shown for the given duration, using the default loader component type.
    *
    * @param secondsShown The maximum time to wait for the loader to be shown, in seconds.
    */
   default void waitToBeShown(int secondsShown) {
      waitToBeShown(DEFAULT_TYPE, secondsShown);
   }

   /**
    * Waits for the loader to be shown for the given duration, using the given loader component type.
    *
    * @param componentType The specific loader component type.
    * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
    */
   void waitToBeShown(LoaderComponentType componentType, int secondsShown);

   /**
    * Waits for the loader to be shown using the specified locator for the given duration, using the default loader
    * component type.
    *
    * @param loaderLocator The By locator for the loader element.
    * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
    */
   default void waitToBeShown(By loaderLocator, int secondsShown) {
      waitToBeShown(DEFAULT_TYPE, loaderLocator, secondsShown);
   }

   /**
    * Waits for the loader to be shown using the specified locator for the given duration, using the given loader
    * component type.
    *
    * @param componentType The specific loader component type.
    * @param loaderLocator The By locator for the loader element.
    * @param secondsShown  The maximum time to wait for the loader to be shown, in seconds.
    */
   void waitToBeShown(LoaderComponentType componentType, By loaderLocator, int secondsShown);

   /**
    * Waits for the loader to be removed from the specified container within the given duration, using the default
    * loader component type.
    *
    * @param container      The SmartWebElement container where the loader is expected to be removed.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   default void waitToBeRemoved(SmartWebElement container, int secondsRemoved) {
      waitToBeRemoved(DEFAULT_TYPE, container, secondsRemoved);
   }

   /**
    * Waits for the loader to be removed from the specified container within the given duration, using the given
    * loader component type.
    *
    * @param componentType  The specific loader component type.
    * @param container      The SmartWebElement container where the loader is expected to be removed.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   void waitToBeRemoved(LoaderComponentType componentType, SmartWebElement container, int secondsRemoved);

   /**
    * Waits for the loader to be removed within the given duration, using the default loader component type.
    *
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   default void waitToBeRemoved(int secondsRemoved) {
      waitToBeRemoved(DEFAULT_TYPE, secondsRemoved);
   }

   /**
    * Waits for the loader to be removed within the given duration, using the given loader component type.
    *
    * @param componentType  The specific loader component type.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   void waitToBeRemoved(LoaderComponentType componentType, int secondsRemoved);

   /**
    * Waits for the loader to be removed using the specified locator within the given duration, using the default
    * loader component type.
    *
    * @param loaderLocator  The By locator for the loader element.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   default void waitToBeRemoved(By loaderLocator, int secondsRemoved) {
      waitToBeRemoved(DEFAULT_TYPE, loaderLocator, secondsRemoved);
   }

   /**
    * Waits for the loader to be removed using the specified locator within the given duration, using the given
    * loader component type.
    *
    * @param componentType  The specific loader component type.
    * @param loaderLocator  The By locator for the loader element.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   void waitToBeRemoved(LoaderComponentType componentType, By loaderLocator, int secondsRemoved);

   /**
    * Waits for the loader to be shown and then removed from the specified container within the given durations.
    *
    * @param componentType  The specific loader component type.
    * @param container      The SmartWebElement container.
    * @param secondsShown   The maximum time to wait for the loader to be shown, in seconds.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   default void waitToBeShownAndRemoved(LoaderComponentType componentType, SmartWebElement container,
                                        int secondsShown, int secondsRemoved) {
      waitToBeShown(componentType, container, secondsShown);
      waitToBeRemoved(componentType, container, secondsRemoved);
   }

   /**
    * Waits for the loader to be shown and then removed within the given durations, using the given loader component
    * type.
    *
    * @param componentType  The specific loader component type.
    * @param secondsShown   The maximum time to wait for the loader to be shown, in seconds.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   default void waitToBeShownAndRemoved(LoaderComponentType componentType, int secondsShown, int secondsRemoved) {
      waitToBeShown(componentType, secondsShown);
      waitToBeRemoved(componentType, secondsRemoved);
   }

   /**
    * Waits for the loader to be shown and then removed using the specified locator within the given durations, using
    * the given loader component type.
    *
    * @param componentType  The specific loader component type.
    * @param loaderLocator  The By locator for the loader element.
    * @param secondsShown   The maximum time to wait for the loader to be shown, in seconds.
    * @param secondsRemoved The maximum time to wait for the loader to be removed, in seconds.
    */
   default void waitToBeShownAndRemoved(LoaderComponentType componentType, By loaderLocator, int secondsShown,
                                        int secondsRemoved) {
      waitToBeShown(componentType, loaderLocator, secondsShown);
      waitToBeRemoved(componentType, loaderLocator, secondsRemoved);
   }
}
