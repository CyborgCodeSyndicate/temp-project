package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link LinkService} interface providing actions for interacting with link components.
 * Extends {@link AbstractComponentService} to handle operations on {@link Link} components.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class LinkServiceImpl extends AbstractComponentService<LinkComponentType, Link> implements LinkService {

   private static final String CLICK_WITH_TEXT = "Clicking link %s with text %s";
   private static final String CLICK = "Clicking link %s";
   private static final String CLICK_USING_LOCATOR = "Clicking link %s using locator %s";

   private static final String DOUBLE_CLICK_WITH_TEXT = "Double-clicking link %s with text %s";
   private static final String DOUBLE_CLICK = "Double-clicking link %s";
   private static final String DOUBLE_CLICK_USING_LOCATOR = "Double-clicking link %s using locator %s";

   private static final String CHECK_ENABLED_WITH_TEXT = "Checking if link %s is enabled with text %s";
   private static final String CHECK_ENABLED = "Checking if link %s is enabled";
   private static final String CHECK_ENABLED_USING_LOCATOR = "Checking if link %s is enabled using locator %s";

   private static final String CHECK_VISIBLE_WITH_TEXT = "Checking if link %s is visible with text %s";
   private static final String CHECK_VISIBLE = "Checking if link %s is visible";
   private static final String CHECK_VISIBLE_USING_LOCATOR = "Checking if link %s is visible using locator %s";

   private static final String INSERT_VALUES_IN_CELL = "Inserting values %s in cell for link %s";

   /**
    * Constructor initializing the LinkServiceImpl with a {@link SmartWebDriver} instance.
    *
    * @param driver the {@link SmartWebDriver} used to interact with browser elements.
    */
   public LinkServiceImpl(SmartWebDriver driver) {
      super(driver);
   }

   /**
    * Creates a {@link Link} component instance based on the provided {@link LinkComponentType}.
    *
    * @param componentType the specific type of link component to create.
    * @return a new instance of {@link Link} initialized with the provided component type.
    */
   @Override
   protected Link createComponent(final LinkComponentType componentType) {
      return ComponentFactory.getLinkComponent(componentType, driver);
   }

   /**
    * Clicks the link component with the specified type, within a container, matching the provided button text.
    *
    * @param componentType the type of the component to interact with.
    * @param container     the container element holding the link.
    * @param buttonText    the text of the button to click.
    */
   @Override
   public <T extends ButtonComponentType> void click(final T componentType, SmartWebElement container,
                                                     String buttonText) {
      LogUi.step(String.format(CLICK_WITH_TEXT, componentType, buttonText));
      linkComponent((LinkComponentType) componentType).click(container, buttonText);
   }

   /**
    * Clicks the link component with the specified type within the given container.
    *
    * @param componentType the type of the component to interact with.
    * @param container     the container element holding the link.
    */
   @Override
   public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
      LogUi.step(String.format(CLICK, componentType));
      linkComponent((LinkComponentType) componentType).click(container);
   }

   /**
    * Clicks the link component with the specified type identified by its button text.
    *
    * @param componentType the type of the component to interact with.
    * @param buttonText    the text of the button to click.
    */
   @Override
   public <T extends ButtonComponentType> void click(final T componentType, final String buttonText) {
      LogUi.step(String.format(CLICK_WITH_TEXT, componentType, buttonText));
      linkComponent((LinkComponentType) componentType).click(buttonText);
   }

   /**
    * Clicks the link component with the specified type located using the provided locator.
    *
    * @param componentType the type of the component to interact with.
    * @param buttonLocator the {@link By} locator of the button.
    */
   @Override
   public <T extends ButtonComponentType> void click(final T componentType, final By buttonLocator) {
      LogUi.step(String.format(CLICK_USING_LOCATOR, componentType, buttonLocator));
      linkComponent((LinkComponentType) componentType).click(buttonLocator);
   }

   /**
    * Double-clicks the link component with specified type, container, and button text.
    *
    * @param componentType the component type to double-click.
    * @param container     the container element.
    * @param buttonText    the text of the button to double-click.
    */
   @Override
   public void doubleClick(final LinkComponentType componentType, final SmartWebElement container,
                           final String buttonText) {
      LogUi.step(String.format(DOUBLE_CLICK_WITH_TEXT, componentType, buttonText));
      linkComponent(componentType).doubleClick(container, buttonText);
   }

   /**
    * Double-clicks the link component within the specified container.
    *
    * @param componentType the component type to double-click.
    * @param container     the container element.
    */
   @Override
   public void doubleClick(final LinkComponentType componentType, final SmartWebElement container) {
      LogUi.step(String.format(DOUBLE_CLICK, componentType));
      linkComponent(componentType).doubleClick(container);
   }

   /**
    * Double-clicks the link component identified by button text.
    *
    * @param componentType the component type to double-click.
    * @param buttonText    the text identifying the link to double-click.
    */
   @Override
   public void doubleClick(final LinkComponentType componentType, final String buttonText) {
      LogUi.step(String.format(DOUBLE_CLICK_WITH_TEXT, componentType, buttonText));
      linkComponent(componentType).doubleClick(buttonText);
   }

   /**
    * Double-clicks the link component identified by locator.
    *
    * @param componentType the component type to double-click.
    * @param buttonLocator the locator identifying the link to double-click.
    */
   @Override
   public void doubleClick(final LinkComponentType componentType, final By buttonLocator) {
      LogUi.step(String.format(DOUBLE_CLICK_USING_LOCATOR, componentType, buttonLocator));
      linkComponent(componentType).doubleClick(buttonLocator);
   }

   /**
    * Checks if the specified link component is enabled within the container, identified by button text.
    *
    * @return true if enabled, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                            final String buttonText) {
      LogUi.step(String.format(CHECK_ENABLED_WITH_TEXT, componentType, buttonText));
      return linkComponent((LinkComponentType) componentType).isEnabled(container, buttonText);
   }

   /**
    * Checks if the link component is enabled within the specified container.
    *
    * @param componentType the type of the component to check.
    * @param container     the container holding the link.
    * @return true if the component is enabled, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
      LogUi.step(String.format(CHECK_ENABLED, componentType));
      return linkComponent((LinkComponentType) componentType).isEnabled(container);
   }

   /**
    * Checks if the link component identified by the given button text is enabled.
    *
    * @param componentType the type of the component to check.
    * @param buttonText    the text identifying the button.
    * @return true if the component is enabled, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String buttonText) {
      LogUi.step(String.format(CHECK_ENABLED_WITH_TEXT, componentType, buttonText));
      return linkComponent((LinkComponentType) componentType).isEnabled(buttonText);
   }

   /**
    * Checks if the link component identified by the provided locator is enabled.
    *
    * @param componentType the type of the component to check.
    * @param buttonLocator the locator used to identify the button.
    * @return true if the component is enabled, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By buttonLocator) {
      LogUi.step(String.format(CHECK_ENABLED_USING_LOCATOR, componentType, buttonLocator));
      return linkComponent((LinkComponentType) componentType).isEnabled(buttonLocator);
   }

   /**
    * Checks if the link component within the specified container and matching the given button text is visible.
    *
    * @param componentType the type of the component to check.
    * @param container     the container holding the link.
    * @param buttonText    the text identifying the button.
    * @return true if the component is visible, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                            final String buttonText) {
      LogUi.step(String.format(CHECK_VISIBLE_WITH_TEXT, componentType, buttonText));
      return linkComponent((LinkComponentType) componentType).isVisible(container, buttonText);
   }

   /**
    * Checks if the link component within the specified container is visible.
    *
    * @param componentType the type of the component to check.
    * @param container     the container holding the link.
    * @return true if the component is visible, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
      LogUi.step(String.format(CHECK_VISIBLE, componentType));
      return linkComponent((LinkComponentType) componentType).isVisible(container);
   }

   /**
    * Checks if the link component identified by the given button text is visible.
    *
    * @param componentType the type of the component to check.
    * @param buttonText    the text identifying the button.
    * @return true if the component is visible, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String buttonText) {
      LogUi.step(String.format(CHECK_VISIBLE_WITH_TEXT, componentType, buttonText));
      return linkComponent((LinkComponentType) componentType).isVisible(buttonText);
   }

   /**
    * Checks if the link component identified by the provided locator is visible.
    *
    * @param componentType the type of the component to check.
    * @param buttonLocator the locator used to identify the button.
    * @return true if the component is visible, otherwise false.
    */
   @Override
   public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By buttonLocator) {
      LogUi.step(String.format(CHECK_VISIBLE_USING_LOCATOR, componentType, buttonLocator));
      return linkComponent((LinkComponentType) componentType).isVisible(buttonLocator);
   }

   /**
    * Performs an insertion operation on a table cell by clicking a link within the cell.
    *
    * @param cellElement   the table cell element.
    * @param componentType the component type within the cell.
    * @param values        additional values (currently unused).
    */
   @Override
   public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                              final String... values) {
      if (!(componentType instanceof LinkComponentType linkType)) {
         throw new IllegalArgumentException("Component type needs to be from: LinkComponentType.");
      }
      LogUi.step(String.format(INSERT_VALUES_IN_CELL, String.join(", ", values), componentType));
      linkComponent(linkType).clickElementInCell(cellElement);
   }

   /**
    * Retrieves or creates a link component based on the provided component type.
    *
    * @param componentType the type of the link component.
    * @return a {@link Link} component instance.
    */
   private Link linkComponent(final LinkComponentType componentType) {
      return getOrCreateComponent(componentType);
   }
}
