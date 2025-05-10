package com.theairebellion.zeus.framework.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataProvider Interface Tests")
class DataProviderTest {

   @Test
   @DisplayName("testStaticData() should return map of test data")
   void testTestStaticData() {
      // Given: a fake implementation instead of a mock
      DataProvider dataProvider = () -> Map.of("key", "value");

      // When
      Map<String, Object> result = dataProvider.testStaticData();

      // Then
      assertEquals(Map.of("key", "value"), result);
   }

   @Test
   @DisplayName("testStaticData() should handle empty map")
   void testTestStaticDataWithEmptyMap() {
      // Given: a fake implementation returning empty map
      DataProvider dataProvider = Collections::emptyMap;

      // When
      Map<String, Object> result = dataProvider.testStaticData();

      // Then
      assertNotNull(result);
      assertEquals(0, result.size());
   }

   @Test
   @DisplayName("testStaticData() should handle null values in map")
   void testTestStaticDataWithNullValues() {
      // Given: a fake implementation returning a map with nulls
      DataProvider dataProvider = () -> {
         Map<String, Object> testData = new HashMap<>();
         testData.put("key1", "value1");
         testData.put("key2", null);
         return testData;
      };

      // When
      Map<String, Object> result = dataProvider.testStaticData();

      // Then
      assertEquals(2, result.size());
      assertEquals("value1", result.get("key1"));
      assertNull(result.get("key2"));
   }

   @Test
   @DisplayName("Concrete implementation should work correctly")
   void testConcreteImplementation() {
      // Given
      DataProvider dataProvider = new ConcreteDataProvider();

      // When
      Map<String, Object> result = dataProvider.testStaticData();

      // Then
      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals("concrete", result.get("test"));
   }

   // ✅ Concrete implementation for final test
   private static class ConcreteDataProvider implements DataProvider {
      @Override
      public Map<String, Object> testStaticData() {
         return Map.of("test", "concrete");
      }
   }
}
