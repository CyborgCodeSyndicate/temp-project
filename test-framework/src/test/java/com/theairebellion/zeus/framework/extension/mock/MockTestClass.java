package com.theairebellion.zeus.framework.extension.mock;

import com.theairebellion.zeus.framework.annotation.TestStaticData;

public class MockTestClass {

   @TestStaticData(MockDataProvider.class)
   public void mockTestMethod() {
   }
}
