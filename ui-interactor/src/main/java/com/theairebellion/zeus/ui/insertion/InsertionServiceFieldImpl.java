package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Service implementation for processing fields annotated with {@link InsertionField}.
 *
 * <p>This class inspects the {@code InsertionField} annotation on object fields,
 * builds the appropriate Selenium {@link By} locator, determines the target
 * {@link ComponentType} enum, and delegates to the registered insertion service
 * to insert values into the UI component.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class InsertionServiceFieldImpl extends BaseInsertionService<InsertionField> {

   /**
    * Constructs an instance of {@code InsertionServiceFieldImpl}.
    *
    * @param serviceRegistry The registry that maintains the mapping between component types and insertion services.
    */
   public InsertionServiceFieldImpl(final InsertionServiceRegistry serviceRegistry) {
      super(serviceRegistry);
   }

   /**
    * Returns the annotation class that this service handles ({@link InsertionField}).
    *
    * @return the {@link InsertionField} class
    */
   @Override
   protected Class<InsertionField> getAnnotationClass() {
      return InsertionField.class;
   }

   /**
    * Returns the processing order defined by the annotation.
    *
    * @param annotation the {@link InsertionField} annotation instance
    * @return the order value indicating insertion priority
    */
   @Override
   protected int getOrder(final InsertionField annotation) {
      return annotation.order();
   }

   /**
    * Retrieves the enum class implementing {@link ComponentType} from the annotation.
    *
    * @param annotation the {@link InsertionField} annotation instance
    * @return the {@code Class} object of the ComponentType enum specified in the annotation
    */
   @Override
   protected Class<? extends ComponentType> getComponentTypeEnumClass(final InsertionField annotation) {
      return annotation.type();
   }

   /**
    * Builds a Selenium {@link By} locator using the annotation's locator definition.
    *
    * @param annotation the {@link InsertionField} annotation containing locator info
    * @return a {@link By} locator for finding the UI element
    */
   @Override
   protected By buildLocator(final InsertionField annotation) {
      return new FindBy.FindByBuilder().buildIt(annotation.locator(), null);
   }

   /**
    * Resolves the {@link ComponentType} enum constant based on annotation's componentType name.
    *
    * <p>Uses reflection to find the enum constant within the specified type class and project package.
    *
    * @param annotation the {@link InsertionField} annotation instance
    * @return the {@link ComponentType} constant to use for insertion
    */
   @Override
   protected ComponentType getType(final InsertionField annotation) {
      final String componentTypeEnumName = annotation.componentType();
      final Class<? extends ComponentType> typeClass = annotation.type();
      return ReflectionUtil.findEnumImplementationsOfInterface(
            typeClass, componentTypeEnumName, getUiConfig().projectPackage()
      );
   }

}
