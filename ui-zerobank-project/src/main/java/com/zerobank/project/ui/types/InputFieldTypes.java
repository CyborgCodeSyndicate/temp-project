package com.zerobank.project.ui.types;

import com.theairebellion.zeus.ui.components.input.InputComponentType;

public enum InputFieldTypes implements InputComponentType {

   BOOTSTRAP_INPUT_TYPE;


   public static final class Data {

      public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";
   }


   @Override
   public Enum<?> getType() {
      return this;
   }
}
