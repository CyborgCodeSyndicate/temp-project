package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.insertion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.table.insertion.mock.TestCellInsertionFunction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@SuppressWarnings("all")
public class CellInsertionFunctionTest extends BaseUnitUITest {

    @Test
    void testAcceptMethod() {
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        TestCellInsertionFunction function = new TestCellInsertionFunction();
        SmartWebElement element = new MockSmartWebElement(webElement, driver);
        String[] values = {"val1", "val2"};
        function.accept(element, values);
        assertSame(element, function.capturedElement);
        assertArrayEquals(values, function.capturedValues);
    }
}
