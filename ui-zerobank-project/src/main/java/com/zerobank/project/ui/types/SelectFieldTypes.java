package com.zerobank.project.ui.types;

import com.theairebellion.zeus.ui.components.select.SelectComponentType;

public enum SelectFieldTypes implements SelectComponentType {

   BOOTSTRAP_SELECT_TYPE;


   public static final String BOOTSTRAP_SELECT = "BOOTSTRAP_SELECT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
