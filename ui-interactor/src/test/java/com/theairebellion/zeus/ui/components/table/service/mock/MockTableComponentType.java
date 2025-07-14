package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;

public enum MockTableComponentType implements TableComponentType {
   DUMMY_TABLE;

   @Override
   public Enum<?> getType() {
      return this;
   }
}