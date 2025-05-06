package com.theairebellion.zeus.ui.components.button.mock;

import com.theairebellion.zeus.ui.components.button.ButtonComponentType;

public enum MockButtonComponentType implements ButtonComponentType {
   DUMMY,
   CUSTOM;

   @Override
   public Enum<?> getType() {
      return this;
   }
}