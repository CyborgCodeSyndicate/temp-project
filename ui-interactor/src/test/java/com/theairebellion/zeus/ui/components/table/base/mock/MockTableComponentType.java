package com.theairebellion.zeus.ui.components.table.base.mock;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;

public enum MockTableComponentType implements TableComponentType {
   VALUE;

   @Override
   public Enum<?> getType() {
      return this;
   }
}