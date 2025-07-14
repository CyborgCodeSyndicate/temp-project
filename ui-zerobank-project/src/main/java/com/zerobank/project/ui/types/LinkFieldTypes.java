package com.zerobank.project.ui.types;

import com.theairebellion.zeus.ui.components.link.LinkComponentType;

public enum LinkFieldTypes implements LinkComponentType {

   BOOTSTRAP_LINK_TYPE;

   public static final class Data {

      public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK_TYPE";
   }


   @Override
   public Enum getType() {
      return this;
   }
}
