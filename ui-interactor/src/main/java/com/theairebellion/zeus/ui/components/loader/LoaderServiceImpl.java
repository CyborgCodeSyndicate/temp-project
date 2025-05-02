package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link LoaderService} interface providing operations for checking the visibility
 * of loader elements, as well as waiting for their appearance or removal. Extends
 * {@link AbstractComponentService} to manage creation and retrieval of {@link Loader} instances.
 *
 * <p>This class uses {@link LoaderComponentType} to identify loader components within a UI automation
 * framework, ensuring consistent interactions whether loaders are located via a container or a direct
 * locator.
 *
 * <p>All methods delegate to an internal {@link Loader} instance, retrieved or created on-demand,
 * to execute the underlying logic of showing or removing loaders within specified timeframes.
 *
 * <p>Any uncaught timeout or unexpected loading behavior typically results in a runtime exception.
 *
 * <p>Example usage within a test:
 * <ul>
 *   <li>Instantiating this class with a {@link SmartWebDriver} instance.</li>
 *   <li>Calling <code>isVisible</code> or <code>waitToBeShown</code> / <code>waitToBeRemoved</code> methods.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class LoaderServiceImpl extends AbstractComponentService<LoaderComponentType, Loader> implements LoaderService {

   private static final String CHECK_VISIBILITY_USING_CONTAINER = "Checking visibility of loader %s using container";
   private static final String CHECK_VISIBILITY_USING_LOCATOR = "Checking visibility of loader %s using locator %s";

   private static final String WAIT_TO_BE_SHOWN_USING_CONTAINER =
         "Waiting for loader %s to be shown using container for %d seconds";
   private static final String WAIT_TO_BE_SHOWN = "Waiting for loader %s to be shown for %d seconds";
   private static final String WAIT_TO_BE_SHOWN_USING_LOCATOR =
         "Waiting for loader %s to be shown using locator %s for %d seconds";

   private static final String WAIT_TO_BE_REMOVED_USING_CONTAINER =
         "Waiting for loader %s to be removed using container for %d seconds";
   private static final String WAIT_TO_BE_REMOVED = "Waiting for loader %s to be removed for %d seconds";
   private static final String WAIT_TO_BE_REMOVED_USING_LOCATOR =
         "Waiting for loader %s to be removed using locator %s for %d seconds";

   /**
    * Constructs a {@code LoaderServiceImpl} with the provided {@link SmartWebDriver}.
    *
    * @param driver the {@link SmartWebDriver} used to interact with web elements.
    */
   public LoaderServiceImpl(SmartWebDriver driver) {
      super(driver);
   }

   /**
    * Creates and returns a {@link Loader} component instance based on the provided
    * {@link LoaderComponentType}.
    *
    * @param componentType the specific type of loader component.
    * @return a new {@link Loader} instance for the given component type.
    */
   @Override
   protected Loader createComponent(final LoaderComponentType componentType) {
      return ComponentFactory.getLoaderComponent(componentType, driver);
   }

   /**
    * Checks if the loader identified by the given component type is visible within the specified container.
    *
    * @param componentType the loader component type.
    * @param container     the container to check for the loader.
    * @return true if the loader is visible, otherwise false.
    */
   @Override
   public boolean isVisible(final LoaderComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(CHECK_VISIBILITY_USING_CONTAINER, componentType));
      return loaderComponent(componentType).isVisible(container);
   }

   /**
    * Checks if the loader identified by the given component type is visible using the provided locator.
    *
    * @param componentType the loader component type.
    * @param loaderLocator the locator identifying the loader.
    * @return true if the loader is visible, otherwise false.
    */
   @Override
   public boolean isVisible(final LoaderComponentType componentType, final By loaderLocator) {
      LogUi.step(String.format(CHECK_VISIBILITY_USING_LOCATOR, componentType, loaderLocator));
      return loaderComponent(componentType).isVisible(loaderLocator);
   }

   /**
    * Waits for the loader to be shown within the specified container, using the given component type.
    *
    * @param componentType the loader component type.
    * @param container     the container where the loader is expected.
    * @param secondsShown  how many seconds to wait for the loader to appear.
    */
   @Override
   public void waitToBeShown(final LoaderComponentType componentType, final SmartWebElement container,
                             final int secondsShown) {
      LogUi.step(String.format(WAIT_TO_BE_SHOWN_USING_CONTAINER, componentType, secondsShown));
      loaderComponent(componentType).waitToBeShown(container, secondsShown);
   }

   /**
    * Waits for the loader to be shown, using the provided component type, within the given time.
    *
    * @param componentType the loader component type.
    * @param secondsShown  how many seconds to wait for the loader to appear.
    */
   @Override
   public void waitToBeShown(final LoaderComponentType componentType, final int secondsShown) {
      LogUi.step(String.format(WAIT_TO_BE_SHOWN, componentType, secondsShown));
      loaderComponent(componentType).waitToBeShown(secondsShown);
   }

   /**
    * Waits for the loader to be shown, identified by a specific locator, using the given component type.
    *
    * @param componentType the loader component type.
    * @param loaderLocator the locator identifying the loader.
    * @param secondsShown  how many seconds to wait for the loader to appear.
    */
   @Override
   public void waitToBeShown(final LoaderComponentType componentType, final By loaderLocator, final int secondsShown) {
      LogUi.step(String.format(WAIT_TO_BE_SHOWN_USING_LOCATOR, componentType, loaderLocator, secondsShown));
      loaderComponent(componentType).waitToBeShown(loaderLocator, secondsShown);
   }

   /**
    * Waits for the loader to be removed within the specified container, using the given component type.
    *
    * @param componentType  the loader component type.
    * @param container      the container where the loader is expected to disappear.
    * @param secondsRemoved how many seconds to wait for the loader to be removed.
    */
   @Override
   public void waitToBeRemoved(final LoaderComponentType componentType, final SmartWebElement container,
                               final int secondsRemoved) {
      LogUi.step(String.format(WAIT_TO_BE_REMOVED_USING_CONTAINER, componentType, secondsRemoved));
      loaderComponent(componentType).waitToBeRemoved(container, secondsRemoved);
   }

   /**
    * Waits for the loader to be removed, using the provided component type, within the given time.
    *
    * @param componentType  the loader component type.
    * @param secondsRemoved how many seconds to wait for the loader to be removed.
    */
   @Override
   public void waitToBeRemoved(final LoaderComponentType componentType, final int secondsRemoved) {
      LogUi.step(String.format(WAIT_TO_BE_REMOVED, componentType, secondsRemoved));
      loaderComponent(componentType).waitToBeRemoved(secondsRemoved);
   }

   /**
    * Waits for the loader to be removed, identified by a specific locator, using the given component type.
    *
    * @param componentType  the loader component type.
    * @param loaderLocator  the locator identifying the loader.
    * @param secondsRemoved how many seconds to wait for the loader to be removed.
    */
   @Override
   public void waitToBeRemoved(final LoaderComponentType componentType, final By loaderLocator,
                               final int secondsRemoved) {
      LogUi.step(String.format(WAIT_TO_BE_REMOVED_USING_LOCATOR, componentType, loaderLocator, secondsRemoved));
      loaderComponent(componentType).waitToBeRemoved(loaderLocator, secondsRemoved);
   }

   /**
    * Retrieves or creates a loader for the specified loader component type.
    *
    * @param componentType the loader component type.
    * @return an existing or newly created {@link Loader} instance.
    */
   private Loader loaderComponent(final LoaderComponentType componentType) {
      return getOrCreateComponent(componentType);
   }

}
