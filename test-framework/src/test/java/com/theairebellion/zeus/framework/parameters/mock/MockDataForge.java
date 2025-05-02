package com.theairebellion.zeus.framework.parameters.mock;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public class MockDataForge<T extends Enum<T>> implements DataForge<T> {

   final Object data;
   final T enumValue;

   public MockDataForge(Object data, T enumValue) {
      this.data = data;
      this.enumValue = enumValue;
   }

   @Override
   public Late<Object> dataCreator() {
      return () -> data;
   }

   @Override
   public T enumImpl() {
      return enumValue;
   }
}
