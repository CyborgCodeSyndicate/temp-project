package com.theairebellion.zeus.framework.extension.mock;

import com.theairebellion.zeus.framework.data.DataProvider;
import java.util.Map;

public class MockDataProvider implements DataProvider {

   public Map<String, Object> testStaticData() {
      return Map.of("dummyKey", "dummyValue");
   }
}
