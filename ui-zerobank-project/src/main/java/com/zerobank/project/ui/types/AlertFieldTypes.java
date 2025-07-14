package com.zerobank.project.ui.types;

import com.theairebellion.zeus.ui.components.alert.AlertComponentType;

public enum AlertFieldTypes implements AlertComponentType {

   BOOTSTRAP_ALERT_TYPE;


   public static final String BOOTSTRAP_ALERT = "BOOTSTRAP_ALERT_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
