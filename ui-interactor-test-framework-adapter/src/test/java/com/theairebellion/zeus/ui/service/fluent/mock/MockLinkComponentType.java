package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.link.LinkComponentType;

public enum MockLinkComponentType implements LinkComponentType {
   DUMMY;

   @Override
   public Enum<?> getType() {
      return this;
   }
}