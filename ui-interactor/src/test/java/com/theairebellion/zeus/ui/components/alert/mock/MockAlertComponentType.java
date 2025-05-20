package com.theairebellion.zeus.ui.components.alert.mock;

import com.theairebellion.zeus.ui.components.alert.AlertComponentType;

public enum MockAlertComponentType implements AlertComponentType {

   DUMMY_ALERT;

   @Override
   public Enum<?> getType() {
      return this;
   }
}