package com.example.project.data;

import com.example.project.data.test.TestData;
import org.aeonbits.owner.ConfigCache;

public class Data {

   public static TestData testData() {
      return getTestDataConfig();
   }

   private static TestData getTestDataConfig() {
      return ConfigCache.getOrCreate(TestData.class);
   }

}
