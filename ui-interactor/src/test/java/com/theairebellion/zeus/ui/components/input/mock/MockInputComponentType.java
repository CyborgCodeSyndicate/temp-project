package com.theairebellion.zeus.ui.components.input.mock;

import com.theairebellion.zeus.ui.components.input.InputComponentType;

public enum MockInputComponentType implements InputComponentType {
   DUMMY_INPUT;

   @Override
   public Enum<?> getType() {
      return this;
   }
}