package com.zerobank.project.ui.elements;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;

public enum TableTypes implements TableComponentType {

   SIMPLE;


   @Override
   public Enum<?> getType() {
      return this;
   }
}
