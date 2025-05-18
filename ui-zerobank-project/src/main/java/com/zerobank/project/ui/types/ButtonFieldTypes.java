package com.zerobank.project.ui.types;

import com.theairebellion.zeus.ui.components.button.ButtonComponentType;

public enum ButtonFieldTypes implements ButtonComponentType {

   BOOTSTRAP_INPUT_TYPE;


   public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
