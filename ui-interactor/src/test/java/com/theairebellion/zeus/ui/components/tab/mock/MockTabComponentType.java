package com.theairebellion.zeus.ui.components.tab.mock;

import com.theairebellion.zeus.ui.components.tab.TabComponentType;

public enum MockTabComponentType implements TabComponentType {
   DUMMY_TAB;

   @Override
   public Enum<?> getType() {
      return this;
   }
}