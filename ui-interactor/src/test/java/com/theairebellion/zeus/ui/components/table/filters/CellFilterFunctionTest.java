package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.filters.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.table.filters.mock.TestCellFilterFunction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@SuppressWarnings("all")
public class CellFilterFunctionTest extends BaseUnitUITest {

   @Test
   void testAcceptCallsCellFilterFunction() {
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      var function = new TestCellFilterFunction();
      SmartWebElement element = new MockSmartWebElement(webElement, driver);
      FilterStrategy strategy = FilterStrategy.SELECT;
      String[] values = {"val1", "val2"};
      function.accept(element, strategy, values);
      assertSame(element, function.capturedElement);
      assertSame(strategy, function.capturedStrategy);
      assertArrayEquals(values, function.capturedValues);
   }
}
