package com.theairebellion.zeus.ui.service;

import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.insertion.BaseInsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.UiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

/**
 * Implementation of the {@link BaseInsertionService} that processes UI elements annotated with
 * {@link InsertionElement}.
 *
 * <p>This class provides functionality to:
 * <ul>
 *   <li>Retrieve UI element metadata from {@link InsertionElement} annotations.</li>
 *   <li>Identify the corresponding {@link ComponentType} for a UI element.</li>
 *   <li>Build Selenium locators for elements.</li>
 *   <li>Perform pre-insertion and post-insertion actions.</li>
 *   <li>Sort fields based on their insertion order.</li>
 * </ul>
 *
 * <p>It utilizes an {@link InsertionServiceRegistry} to manage different insertion strategies and relies on
 * a {@link SmartWebDriver} instance for interacting with web elements.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class InsertionServiceElementImpl extends BaseInsertionService<InsertionElement> {

   /**
    * WebDriver instance used for interacting with UI elements.
    */
   private final SmartWebDriver webDriver;

   /**
    * Constructs an {@code InsertionServiceElementImpl} instance.
    *
    * @param serviceRegistry The registry managing available insertion services.
    * @param webDriver       The WebDriver instance for element interaction.
    */
   public InsertionServiceElementImpl(final InsertionServiceRegistry serviceRegistry,
                                      final SmartWebDriver webDriver) {
      super(serviceRegistry);
      this.webDriver = webDriver;
   }

   /**
    * Returns the annotation class handled by this insertion service.
    *
    * @return {@code InsertionElement.class}.
    */
   @Override
   protected Class<InsertionElement> getAnnotationClass() {
      return InsertionElement.class;
   }

   /**
    * Extracts the insertion order defined in the {@link InsertionElement} annotation.
    *
    * @param annotation The annotation instance attached to the field.
    * @return The insertion order declared by the annotation.
    */
   @Override
   protected int getOrder(final InsertionElement annotation) {
      return annotation.order();
   }

   /**
    * Resolves the {@link ComponentType} enum class for the UI element described by the annotation.
    *
    * @param annotation The annotation instance attached to the field.
    * @return The enum class representing the component type.
    */
   @Override
   protected Class<? extends ComponentType> getComponentTypeEnumClass(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      return uiElement.componentType().getClass();
   }

   /**
    * Builds a Selenium {@link By} locator for the UI element specified by the annotation.
    *
    * @param annotation The annotation instance attached to the field.
    * @return A Selenium locator targeting the element.
    */
   @Override
   protected By buildLocator(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      return uiElement.locator();
   }

   /**
    * Retrieves the {@link ComponentType} of the UI element specified by the annotation.
    *
    * @param annotation The annotation instance attached to the field.
    * @return The component type for the element.
    */
   @Override
   protected ComponentType getType(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      return uiElement.componentType();
   }

   /**
    * Executes any pre-insertion action defined by the {@link UiElement#before()} consumer.
    *
    * @param annotation The annotation instance attached to the field.
    */
   @Override
   protected void beforeInsertion(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      uiElement.before().accept(webDriver);
   }

   /**
    * Executes any post-insertion action defined by the {@link UiElement#after()} consumer.
    *
    * @param annotation The annotation instance attached to the field.
    */
   @Override
   protected void afterInsertion(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      uiElement.after().accept(webDriver);
   }

}
