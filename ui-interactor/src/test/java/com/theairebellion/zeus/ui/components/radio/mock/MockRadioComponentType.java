package com.theairebellion.zeus.ui.components.radio.mock;

import com.theairebellion.zeus.ui.components.radio.RadioComponentType;

public enum MockRadioComponentType implements RadioComponentType {
   DUMMY_RADIO;

   @Override
   public Enum<?> getType() {
      return this;
   }
}