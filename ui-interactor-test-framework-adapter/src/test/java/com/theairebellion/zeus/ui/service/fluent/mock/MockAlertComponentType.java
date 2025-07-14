package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.alert.AlertComponentType;

public enum MockAlertComponentType implements AlertComponentType {

   DUMMY;

   @Override
   public Enum<?> getType() {
      return this;
   }
}
