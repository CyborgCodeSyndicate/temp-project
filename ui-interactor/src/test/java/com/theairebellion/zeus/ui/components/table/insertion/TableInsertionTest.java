package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.insertion.mock.MockComponentType;
import com.theairebellion.zeus.ui.components.table.insertion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.table.insertion.mock.TestTableInsertion;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@SuppressWarnings("all")
public class TableInsertionTest extends BaseUnitUITest {

    @Test
    void testTableInsertion() {
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        TestTableInsertion insertion = new TestTableInsertion();
        SmartWebElement element = new MockSmartWebElement(webElement, driver);
        MockComponentType compType = MockComponentType.DUMMY;
        String[] values = {"insert1", "insert2"};
        insertion.tableInsertion(element, compType, values);
        assertSame(element, insertion.capturedElement);
        assertSame(compType, insertion.capturedComponent);
        assertArrayEquals(values, insertion.capturedValues);
    }
}
