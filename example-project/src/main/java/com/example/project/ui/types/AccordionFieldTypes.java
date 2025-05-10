package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;

public enum AccordionFieldTypes implements AccordionComponentType {

   MD_ACCORDION_TYPE;


   public static final String MD_ACCORDION = "MD_ACCORDION_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
