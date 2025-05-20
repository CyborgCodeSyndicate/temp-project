package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.util.List;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides high-level service operations for accordion UI components.
 *
 * <p>This interface extends the functionality of {@link Accordion} by offering
 * default methods that rely on a {@link AccordionComponentType}—either a
 * user-specified type or a globally configured default. Service methods
 * include expanding and collapsing accordion panels, checking whether panels
 * are enabled, and retrieving their text content.
 *
 * <p>An implementation such as {@code AccordionServiceImpl} internally delegates
 * to the framework’s {@link com.theairebellion.zeus.ui.components.factory.ComponentFactory}
 * to create the correct {@code Accordion} instance based on the chosen
 * {@code AccordionComponentType}. This design enables easy swapping of
 * accordion implementations (e.g., Bootstrap-based, Material-based)
 * without changing test code.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface AccordionService {

   AccordionComponentType DEFAULT_TYPE = getDefaultType();

   /**
    * Retrieves the default accordion component type.
    *
    * @return The default accordion component type.
    */
   private static AccordionComponentType getDefaultType() {
      try {
         return ReflectionUtil.findEnumImplementationsOfInterface(AccordionComponentType.class,
               getUiConfig().accordionDefaultType(),
               getUiConfig().projectPackage());
      } catch (Exception ignored) {
         return null;
      }
   }

   /**
    * Expands specified panels inside the accordion, using the default accordion component type.
    *
    * @param container     The accordion container.
    * @param accordionText The text of the panels to expand.
    */
   default void expand(SmartWebElement container, String... accordionText) {
      expand(DEFAULT_TYPE, container, accordionText);
   }

   /**
    * Expands specified panels inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @param accordionText The text of the panels to expand.
    */
   void expand(AccordionComponentType componentType, SmartWebElement container, String... accordionText);

   /**
    * Expands a panel inside the accordion using a strategy, with the default accordion component type.
    *
    * @param container The accordion container.
    * @param strategy  The expansion strategy.
    * @return The expanded panel title.
    */
   default String expand(SmartWebElement container, Strategy strategy) {
      return expand(DEFAULT_TYPE, container, strategy);
   }

   /**
    * Expands a panel inside the accordion using a strategy, with the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @param strategy      The expansion strategy.
    * @return The expanded panel title.
    */
   String expand(AccordionComponentType componentType, SmartWebElement container, Strategy strategy);

   /**
    * Expands specified panels inside the accordion, using the default accordion component type.
    *
    * @param accordionText The text of the panels to expand.
    */
   default void expand(String... accordionText) {
      expand(DEFAULT_TYPE, accordionText);
   }

   /**
    * Expands specified panels inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param accordionText The text of the panels to expand.
    */
   void expand(AccordionComponentType componentType, String... accordionText);

   /**
    * Expands specified panels inside the accordion, identified by locators, using the default accordion component type.
    *
    * @param accordionLocator The locator of the panels to expand.
    */
   default void expand(By... accordionLocator) {
      expand(DEFAULT_TYPE, accordionLocator);
   }

   /**
    * Expands specified panels inside the accordion, identified by locators, using the given accordion component type.
    *
    * @param componentType    The specific accordion component type.
    * @param accordionLocator The locator of the panels to expand.
    */
   void expand(AccordionComponentType componentType, By... accordionLocator);

   /**
    * Collapses specified panels inside the accordion, using the default accordion component type.
    *
    * @param container     The accordion container.
    * @param accordionText The text of the panels to collapse.
    */
   default void collapse(SmartWebElement container, String... accordionText) {
      collapse(DEFAULT_TYPE, container, accordionText);
   }

   /**
    * Collapses specified panels inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @param accordionText The text of the panels to collapse.
    */
   void collapse(AccordionComponentType componentType, SmartWebElement container, String... accordionText);

   /**
    * Collapses a panel inside the accordion using a strategy, with the default accordion component type.
    *
    * @param container The accordion container.
    * @param strategy  The collapse strategy.
    * @return The collapsed panel title.
    */
   default String collapse(SmartWebElement container, Strategy strategy) {
      return collapse(DEFAULT_TYPE, container, strategy);
   }

   /**
    * Collapses a panel inside the accordion using a strategy, with the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @param strategy      The collapse strategy.
    * @return The collapsed panel title.
    */
   String collapse(AccordionComponentType componentType, SmartWebElement container, Strategy strategy);

   /**
    * Collapses specified panels inside the accordion, using the default accordion component type.
    *
    * @param accordionText The text of the panels to collapse.
    */
   default void collapse(String... accordionText) {
      collapse(DEFAULT_TYPE, accordionText);
   }

   /**
    * Collapses specified panels inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param accordionText The text of the panels to collapse.
    */
   void collapse(AccordionComponentType componentType, String... accordionText);

   /**
    * Collapses specified panels inside the accordion, identified by locators, using the default accordion
    * component type.
    *
    * @param accordionLocator The locator of the panels to collapse.
    */
   default void collapse(By... accordionLocator) {
      collapse(DEFAULT_TYPE, accordionLocator);
   }

   /**
    * Collapses specified panels inside the accordion, identified by locators, using the given accordion component type.
    *
    * @param componentType    The specific accordion component type.
    * @param accordionLocator The locator of the panels to collapse.
    */
   void collapse(AccordionComponentType componentType, By... accordionLocator);

   /**
    * Checks if specified panels inside the accordion are enabled, using the default accordion component type.
    *
    * @param container     The accordion container.
    * @param accordionText The text of the panels to check.
    * @return true if all specified panels are enabled, false otherwise.
    */
   default boolean areEnabled(SmartWebElement container, String... accordionText) {
      return areEnabled(DEFAULT_TYPE, container, accordionText);
   }

   /**
    * Checks if specified panels inside the accordion are enabled, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @param accordionText The text of the panels to check.
    * @return true if all specified panels are enabled, false otherwise.
    */
   boolean areEnabled(AccordionComponentType componentType, SmartWebElement container, String... accordionText);

   /**
    * Checks if specified panels inside the accordion are enabled, using the default accordion component type.
    *
    * @param accordionText The text of the panels to check.
    * @return true if all specified panels are enabled, false otherwise.
    */
   default boolean areEnabled(String... accordionText) {
      return areEnabled(DEFAULT_TYPE, accordionText);
   }

   /**
    * Checks if specified panels inside the accordion are enabled, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param accordionText The text of the panels to check.
    * @return true if all specified panels are enabled, false otherwise.
    */
   boolean areEnabled(AccordionComponentType componentType, String... accordionText);

   /**
    * Checks if specified panels inside the accordion are enabled, using the default accordion component type.
    *
    * @param accordionLocator The locator of the panels to check.
    * @return true if all specified panels are enabled, false otherwise.
    */
   default boolean areEnabled(By... accordionLocator) {
      return areEnabled(DEFAULT_TYPE, accordionLocator);
   }

   /**
    * Checks if specified panels inside the accordion are enabled, using the given accordion component type.
    *
    * @param componentType    The specific accordion component type.
    * @param accordionLocator The locator of the panels to check.
    * @return true if all specified panels are enabled, false otherwise.
    */
   boolean areEnabled(AccordionComponentType componentType, By... accordionLocator);

   /**
    * Retrieves a list of currently expanded panels inside the accordion, using the default accordion component type.
    *
    * @param container The accordion container.
    * @return A list of expanded panel titles.
    */
   default List<String> getExpanded(SmartWebElement container) {
      return getExpanded(DEFAULT_TYPE, container);
   }

   /**
    * Retrieves a list of currently expanded panels inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @return A list of expanded panel titles.
    */
   List<String> getExpanded(AccordionComponentType componentType, SmartWebElement container);

   /**
    * Retrieves a list of currently collapsed panels inside the accordion, using the default accordion component type.
    *
    * @param container The accordion container.
    * @return A list of collapsed panel titles.
    */
   default List<String> getCollapsed(SmartWebElement container) {
      return getCollapsed(DEFAULT_TYPE, container);
   }

   /**
    * Retrieves a list of currently collapsed panels inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @return A list of collapsed panel titles.
    */
   List<String> getCollapsed(AccordionComponentType componentType, SmartWebElement container);

   /**
    * Retrieves a list of all panel texts inside the accordion, using the default accordion component type.
    *
    * @param container The accordion container.
    * @return A list of all panel texts.
    */
   default List<String> getAll(SmartWebElement container) {
      return getAll(DEFAULT_TYPE, container);
   }

   /**
    * Retrieves a list of all panel texts inside the accordion, using the given accordion component type.
    *
    * @param componentType The specific accordion component type.
    * @param container     The accordion container.
    * @return A list of all panel texts.
    */
   List<String> getAll(AccordionComponentType componentType, SmartWebElement container);

   /**
    * Retrieves the title of a panel inside the accordion, using the default accordion component type.
    *
    * @param accordionLocator The locator of the panel.
    * @return The title of the panel.
    */
   default String getTitle(By accordionLocator) {
      return getTitle(DEFAULT_TYPE, accordionLocator);
   }

   /**
    * Retrieves the title of a panel inside the accordion, using the given accordion component type.
    *
    * @param componentType    The specific accordion component type.
    * @param accordionLocator The locator of the panel.
    * @return The title of the panel.
    */
   String getTitle(AccordionComponentType componentType, By accordionLocator);

   /**
    * Retrieves the text content of a panel inside the accordion, using the default accordion component type.
    *
    * @param accordionLocator The locator of the panel.
    * @return The text content of the panel.
    */
   default String getText(By accordionLocator) {
      return getText(DEFAULT_TYPE, accordionLocator);
   }

   /**
    * Retrieves the text content of a panel inside the accordion, using the given accordion component type.
    *
    * @param componentType    The specific accordion component type.
    * @param accordionLocator The locator of the panel.
    * @return The text content of the panel.
    */
   String getText(AccordionComponentType componentType, By accordionLocator);
}

