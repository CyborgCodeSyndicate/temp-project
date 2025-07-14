package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.radio.RadioComponentType;

public enum MockRadioComponentType implements RadioComponentType {
   DUMMY;

   @Override
   public Enum<?> getType() {
      return this;
   }
}
