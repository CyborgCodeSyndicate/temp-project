package com.theairebellion.zeus.ui.components.accordion.mock;

import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;

public enum MockAccordionComponentType implements AccordionComponentType {

   DUMMY_ACCORDION;

   @Override
   public Enum<?> getType() {
      return this;
   }
}
