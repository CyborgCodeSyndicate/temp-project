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

   //todo: javaDocs
   @Override
   protected Class<InsertionElement> getAnnotationClass() {
      return InsertionElement.class;
   }

   //todo: javaDocs
   @Override
   protected int getOrder(final InsertionElement annotation) {
      return annotation.order();
   }

   //todo: javaDocs
   @Override
   protected Class<? extends ComponentType> getComponentTypeEnumClass(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      return uiElement.componentType().getClass();
   }

   //todo: javaDocs
   @Override
   protected By buildLocator(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      return uiElement.locator();
   }

   //todo: javaDocs
   @Override
   protected ComponentType getType(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      return uiElement.componentType();
   }

   //todo: javaDocs
   @Override
   protected void beforeInsertion(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      uiElement.before().accept(webDriver);
   }

   //todo: javaDocs
   @Override
   protected void afterInsertion(final InsertionElement annotation) {
      final UiElement uiElement = (UiElement) Enum.valueOf(
            (Class<? extends Enum>) annotation.locatorClass(),
            annotation.elementEnum()
      );
      uiElement.after().accept(webDriver);
   }

}
