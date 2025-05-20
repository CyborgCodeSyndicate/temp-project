package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.table.insertion.mock.TestCellInsertionFunction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


@DisplayName("CellInsertionFunction Interface Test")
@SuppressWarnings("all")
class CellInsertionFunctionTest extends BaseUnitUITest {

   private static final String[] SAMPLE_VALUES = {"val1", "val2"};

   @Test
   @DisplayName("Default accept method should call cellInsertionFunction")
   void testAcceptMethod() {
      // Given
      var function = new TestCellInsertionFunction();
      SmartWebElement element = MockSmartWebElement.createMock();

      // When
      function.accept(element, SAMPLE_VALUES);

      // Then
      assertSame(element, function.capturedElement);
      assertArrayEquals(SAMPLE_VALUES, function.capturedValues);
   }
}