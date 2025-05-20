package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.components.table.filters.mock.TestCellFilterFunction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


@DisplayName("CellFilterFunction Interface Test")
@SuppressWarnings("all")
class CellFilterFunctionTest extends BaseUnitUITest {

   @Test
   @DisplayName("Default accept method should call cellFilterFunction")
   void testAcceptCallsCellFilterFunction() {
      // Given
      var function = new TestCellFilterFunction();
      SmartWebElement element = MockSmartWebElement.createMock();
      var strategy = FilterStrategy.SELECT;
      var values = new String[] {"val1", "val2"};

      // When
      function.accept(element, strategy, values);

      // Then
      assertSame(element, function.capturedElement);
      assertSame(strategy, function.capturedStrategy);
      assertArrayEquals(values, function.capturedValues);
   }
}