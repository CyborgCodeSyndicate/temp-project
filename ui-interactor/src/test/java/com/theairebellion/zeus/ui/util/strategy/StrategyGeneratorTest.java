package com.theairebellion.zeus.ui.util.strategy;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("StrategyGenerator Tests")
class StrategyGeneratorTest {

   @Mock
   private SmartWebElement element1;

   @Mock
   private SmartWebElement element2;

   @Mock
   private SmartWebElement element3;

   @Test
   @DisplayName("getRandomElementFromElements should return an element")
   void getRandomElementFromElementsReturnsRandomElement() {
      // Given
      List<SmartWebElement> elements = Arrays.asList(element1, element2, element3);

      // When
      SmartWebElement result = StrategyGenerator.getRandomElementFromElements(elements);

      // Then
      assertTrue(elements.contains(result), "Result should be one of the elements in the list");
   }

   @Test
   @DisplayName("getFirstElementFromElements should return the first element")
   void getFirstElementFromElementsReturnsFirstElement() {
      // Given
      List<SmartWebElement> elements = Arrays.asList(element1, element2, element3);

      // When
      SmartWebElement result = StrategyGenerator.getFirstElementFromElements(elements);

      // Then
      assertSame(element1, result);
   }

   @Test
   @DisplayName("getLastElementFromElements should return the last element")
   void getLastElementFromElementsReturnsLastElement() {
      // Given
      List<SmartWebElement> elements = Arrays.asList(element1, element2, element3);

      // When
      SmartWebElement result = StrategyGenerator.getLastElementFromElements(elements);

      // Then
      assertSame(element3, result);
   }

   @Test
   @DisplayName("getRandomElementFromElements should throw IllegalArgumentException with empty list")
   void randomElementThrowsExceptionWithEmptyList() {
      List<SmartWebElement> emptyList = new ArrayList<>();
      assertThrows(IllegalArgumentException.class,
            () -> StrategyGenerator.getRandomElementFromElements(emptyList));
   }

   @Test
   @DisplayName("getFirstElementFromElements should throw IllegalArgumentException with empty list")
   void firstElementThrowsExceptionWithEmptyList() {
      List<SmartWebElement> emptyList = new ArrayList<>();
      assertThrows(IllegalArgumentException.class,
            () -> StrategyGenerator.getFirstElementFromElements(emptyList));
   }

   @Test
   @DisplayName("getLastElementFromElements should throw IllegalArgumentException with empty list")
   void lastElementThrowsExceptionWithEmptyList() {
      List<SmartWebElement> emptyList = new ArrayList<>();
      assertThrows(IllegalArgumentException.class,
            () -> StrategyGenerator.getLastElementFromElements(emptyList));
   }

   @Test
   @DisplayName("getRandomElementFromElements should throw IllegalArgumentException with null list")
   void randomElementThrowsExceptionWithNullList() {
      assertThrows(IllegalArgumentException.class,
            () -> StrategyGenerator.getRandomElementFromElements(null));
   }

   @Test
   @DisplayName("getFirstElementFromElements should throw IllegalArgumentException with null list")
   void firstElementThrowsExceptionWithNullList() {
      assertThrows(IllegalArgumentException.class,
            () -> StrategyGenerator.getFirstElementFromElements(null));
   }

   @Test
   @DisplayName("getLastElementFromElements should throw IllegalArgumentException with null list")
   void lastElementThrowsExceptionWithNullList() {
      assertThrows(IllegalArgumentException.class,
            () -> StrategyGenerator.getLastElementFromElements(null));
   }
}