package com.theairebellion.zeus.ui.components.base.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;

public enum MockType implements ComponentType {

   A, B;

   @Override
   public Enum<?> getType() {
      return this;
   }
}
