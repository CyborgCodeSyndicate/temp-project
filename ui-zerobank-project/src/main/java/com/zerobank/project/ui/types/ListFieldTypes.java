package com.zerobank.project.ui.types;

import com.theairebellion.zeus.ui.components.list.ItemListComponentType;

public enum ListFieldTypes implements ItemListComponentType {

   BOOTSTRAP_LIST_TYPE;


   public static final String BOOTSTRAP_LIST = "BOOTSTRAP_LIST_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
