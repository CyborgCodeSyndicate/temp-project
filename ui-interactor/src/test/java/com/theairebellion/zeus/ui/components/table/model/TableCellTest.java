package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.model.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class TableCellTest extends BaseUnitUITest {

    WebElement webElement;
    WebDriver driver;

    @BeforeEach
    public void setUp() {

        webElement = mock(WebElement.class);
        driver = mock(WebDriver.class);
    }

    @Test
    void testAllArgsConstructor() {
        SmartWebElement element = new MockSmartWebElement(webElement, driver);
        String text = "sampleText";
        TableCell cell = new TableCell(element, text);
        assertEquals(element, cell.getElement());
        assertEquals(text, cell.getText());
    }

    @Test
    void testStringConstructorAndSetter() {
        String text = "onlyText";
        TableCell cell = new TableCell(text);
        assertEquals(text, cell.getText());
        cell.setText("newText");
        assertEquals("newText", cell.getText());
    }
}