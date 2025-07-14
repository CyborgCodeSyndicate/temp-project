package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;

public enum MockAccordionComponentType implements AccordionComponentType {
   DUMMY;

   @Override
   public Enum<?> getType() {
      return this;
   }
}
