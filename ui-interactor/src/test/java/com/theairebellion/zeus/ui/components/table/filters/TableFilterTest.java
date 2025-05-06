package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.filters.mock.MockComponentType;
import com.theairebellion.zeus.ui.components.table.filters.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.table.filters.mock.TestTableFilter;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

public class TableFilterTest extends BaseUnitUITest {


   @Test
   void testTableFilter() {
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      TestTableFilter filter = new TestTableFilter();
      SmartWebElement element = new MockSmartWebElement(webElement, driver);
      MockComponentType compType = MockComponentType.DUMMY;
      FilterStrategy strategy = FilterStrategy.UNSELECT;
      String[] values = {"a", "b"};
      filter.tableFilter(element, compType, strategy, values);
      assertSame(element, filter.capturedElement);
      assertSame(compType, filter.capturedComponent);
      assertSame(strategy, filter.capturedStrategy);
      assertArrayEquals(values, filter.capturedValues);
   }
}