package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides service-level methods for interacting with button UI components.
 *
 * <p>This interface defines operations to click buttons and to check their enabled
 * and visible states, delegating the actual interactions to specific implementations
 * based on the configured {@link ButtonComponentType}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface ButtonService extends TableInsertion {

   ButtonComponentType DEFAULT_TYPE = getDefaultType();

   /**
    * Retrieves the default button component type from the configuration.
    *
    * @return The default ButtonComponentType.
    */
   private static ButtonComponentType getDefaultType() {
      try {
         return ReflectionUtil.findEnumImplementationsOfInterface(ButtonComponentType.class,
               getUiConfig().buttonDefaultType(),
               getUiConfig().projectPackage());
      } catch (Exception ignored) {
         return null;
      }
   }

   /**
    * Clicks a button with the specified text inside a container, using the default button component type.
    *
    * @param container  The SmartWebElement container that contains the button.
    * @param buttonText The text of the button to click.
    */
   default void click(SmartWebElement container, String buttonText) {
      click(DEFAULT_TYPE, container, buttonText);
   }

   /**
    * Clicks a button with the specified text inside a container, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param container     The SmartWebElement container that contains the button.
    * @param buttonText    The text of the button to click.
    */
   <T extends ButtonComponentType> void click(T componentType, SmartWebElement container, String buttonText);

   /**
    * Clicks a button inside a container, using the default button component type.
    *
    * @param container The SmartWebElement container that contains the button.
    */
   default void click(SmartWebElement container) {
      click(DEFAULT_TYPE, container);
   }

   /**
    * Clicks a button inside a container, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param container     The SmartWebElement container that contains the button.
    */
   <T extends ButtonComponentType> void click(T componentType, SmartWebElement container);

   /**
    * Clicks a button with the specified text, using the default button component type.
    *
    * @param buttonText The text of the button to click.
    */
   default void click(String buttonText) {
      click(DEFAULT_TYPE, buttonText);
   }

   /**
    * Clicks a button with the specified text, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param buttonText    The text of the button to click.
    */
   <T extends ButtonComponentType> void click(T componentType, String buttonText);

   /**
    * Clicks a button located by the specified locator, using the default button component type.
    *
    * @param buttonLocator The By locator for the button to click.
    */
   default void click(By buttonLocator) {
      click(DEFAULT_TYPE, buttonLocator);
   }

   /**
    * Clicks a button located by the specified locator, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param buttonLocator The By locator for the button to click.
    */
   <T extends ButtonComponentType> void click(T componentType, By buttonLocator);

   /**
    * Checks if a button with the specified text inside a container is enabled, using the default button component type.
    *
    * @param container  The SmartWebElement container that contains the button.
    * @param buttonText The text of the button to check.
    * @return true if the button is enabled, false otherwise.
    */
   default boolean isEnabled(SmartWebElement container, String buttonText) {
      return isEnabled(DEFAULT_TYPE, container, buttonText);
   }

   /**
    * Checks if a button with the specified text inside a container is enabled, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param container     The SmartWebElement container that contains the button.
    * @param buttonText    The text of the button to check.
    * @return true if the button is enabled, false otherwise.
    */
   <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container, String buttonText);

   /**
    * Checks if a button inside a container is enabled, using the default button component type.
    *
    * @param container The SmartWebElement container that contains the button.
    * @return true if the button is enabled, false otherwise.
    */
   default boolean isEnabled(SmartWebElement container) {
      return isEnabled(DEFAULT_TYPE, container);
   }

   /**
    * Checks if a button inside a container is enabled, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param container     The SmartWebElement container that contains the button.
    * @return true if the button is enabled, false otherwise.
    */
   <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement container);

   /**
    * Checks if a button with the specified text is enabled, using the default button component type.
    *
    * @param buttonText The text of the button to check.
    * @return true if the button is enabled, false otherwise.
    */
   default boolean isEnabled(String buttonText) {
      return isEnabled(DEFAULT_TYPE, buttonText);
   }

   /**
    * Checks if a button with the specified text is enabled, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param buttonText    The text of the button to check.
    * @return true if the button is enabled, false otherwise.
    */
   <T extends ButtonComponentType> boolean isEnabled(T componentType, String buttonText);

   /**
    * Checks if a button located by the specified locator is enabled, using the default button component type.
    *
    * @param buttonLocator The By locator for the button to check.
    * @return true if the button is enabled, false otherwise.
    */
   default boolean isEnabled(By buttonLocator) {
      return isEnabled(DEFAULT_TYPE, buttonLocator);
   }

   /**
    * Checks if a button located by the specified locator is enabled, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param buttonLocator The By locator for the button to check.
    * @return true if the button is enabled, false otherwise.
    */
   <T extends ButtonComponentType> boolean isEnabled(T componentType, By buttonLocator);

   /**
    * Checks if a button with the specified text inside a container is visible, using the default button component type.
    *
    * @param container  The SmartWebElement container that contains the button.
    * @param buttonText The text of the button to check.
    * @return true if the button is visible, false otherwise.
    */
   default boolean isVisible(SmartWebElement container, String buttonText) {
      return isVisible(DEFAULT_TYPE, container, buttonText);
   }

   /**
    * Checks if a button with the specified text inside a container is visible, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param container     The SmartWebElement container that contains the button.
    * @param buttonText    The text of the button to check.
    * @return true if the button is visible, false otherwise.
    */
   <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container, String buttonText);

   /**
    * Checks if a button inside a container is visible, using the default button component type.
    *
    * @param container The SmartWebElement container that contains the button.
    * @return true if the button is visible, false otherwise.
    */
   default boolean isVisible(SmartWebElement container) {
      return isVisible(DEFAULT_TYPE, container);
   }

   /**
    * Checks if a button inside a container is visible, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param container     The SmartWebElement container that contains the button.
    * @return true if the button is visible, false otherwise.
    */
   <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement container);

   /**
    * Checks if a button with the specified text is visible, using the default button component type.
    *
    * @param buttonText The text of the button to check.
    * @return true if the button is visible, false otherwise.
    */
   default boolean isVisible(String buttonText) {
      return isVisible(DEFAULT_TYPE, buttonText);
   }

   /**
    * Checks if a button with the specified text is visible, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param buttonText    The text of the button to check.
    * @return true if the button is visible, false otherwise.
    */
   <T extends ButtonComponentType> boolean isVisible(T componentType, String buttonText);

   /**
    * Checks if a button located by the specified locator is visible, using the default button component type.
    *
    * @param buttonLocator The By locator for the button to check.
    * @return true if the button is visible, false otherwise.
    */
   default boolean isVisible(By buttonLocator) {
      return isVisible(DEFAULT_TYPE, buttonLocator);
   }

   /**
    * Checks if a button located by the specified locator is visible, using the given button component type.
    *
    * @param componentType The specific button component type.
    * @param buttonLocator The By locator for the button to check.
    * @return true if the button is visible, false otherwise.
    */
   <T extends ButtonComponentType> boolean isVisible(T componentType, By buttonLocator);
}