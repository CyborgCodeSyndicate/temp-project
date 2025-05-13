package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.radio.RadioComponentType;

public enum RadioFieldTypes implements RadioComponentType {

   MD_RADIO_TYPE,
   BOOTSTRAP_RADIO_TYPE;


   public static final String MD_RADIO = "MD_RADIO_TYPE";
   public static final String BOOTSTRAP_RADIO = "BOOTSTRAP_RADIO_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
