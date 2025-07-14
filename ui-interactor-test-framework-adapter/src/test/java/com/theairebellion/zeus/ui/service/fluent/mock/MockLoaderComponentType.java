package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.loader.LoaderComponentType;

public enum MockLoaderComponentType implements LoaderComponentType {
   DUMMY;

   @Override
   public Enum<?> getType() {
      return this;
   }
}