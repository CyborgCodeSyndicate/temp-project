package com.theairebellion.zeus.ui.components.link.mock;

import com.theairebellion.zeus.ui.components.link.LinkComponentType;

public enum MockLinkComponentType implements LinkComponentType {
   DUMMY_LINK;

   @Override
   public Enum<?> getType() {
      return this;
   }
}