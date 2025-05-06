package com.theairebellion.zeus.framework.extension.mock;

import com.theairebellion.zeus.framework.parameters.Late;

public class MockLate implements Late<Object> {

   @Override
   public Object join() {
      return "joinedValue";
   }
}
