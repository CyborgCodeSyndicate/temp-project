package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

//todo: javaDocs
public class InsertionServiceFieldImpl extends BaseInsertionService<InsertionField> {

   /**
    * Constructs an instance of {@code InsertionServiceFieldImpl}.
    *
    * @param serviceRegistry The registry that maintains the mapping between component types and insertion services.
    */
   public InsertionServiceFieldImpl(final InsertionServiceRegistry serviceRegistry) {
      super(serviceRegistry);
   }

   //todo: javaDocs
   @Override
   protected Class<InsertionField> getAnnotationClass() {
      return InsertionField.class;
   }

   //todo: javaDocs
   @Override
   protected int getOrder(final InsertionField annotation) {
      return annotation.order();
   }

   //todo: javaDocs
   @Override
   protected Class<? extends ComponentType> getComponentTypeEnumClass(final InsertionField annotation) {
      return annotation.type();
   }

   //todo: javaDocs
   @Override
   protected By buildLocator(final InsertionField annotation) {
      return new FindBy.FindByBuilder().buildIt(annotation.locator(), null);
   }

   //todo: javaDocs
   @Override
   protected ComponentType getType(final InsertionField annotation) {
      final String componentTypeEnumName = annotation.componentType();
      final Class<? extends ComponentType> typeClass = annotation.type();
      return ReflectionUtil.findEnumImplementationsOfInterface(
            typeClass, componentTypeEnumName, getUiConfig().projectPackage()
      );
   }

}
