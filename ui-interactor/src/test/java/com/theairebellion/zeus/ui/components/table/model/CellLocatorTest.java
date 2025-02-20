package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterComponent;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionComponent;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.model.mock.MockCellFilterFunction;
import com.theairebellion.zeus.ui.components.table.model.mock.MockCellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.model.mock.MockComponentType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CellLocatorTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        String fieldName = "testField";
        By cellLocator = By.id("cell");
        By cellTextLocator = By.className("text");
        By headerCellLocator = By.tagName("th");
        boolean collection = true;
        String tableSection = "section1";
        CellInsertionComponent insertionComponent = new CellInsertionComponent(MockComponentType.class, "dummy", 1);
        Class<? extends CellInsertionFunction> customInsertion = MockCellInsertionFunction.class;
        CellFilterComponent filterComponent = new CellFilterComponent(MockComponentType.class, "dummyFilter");
        Class<? extends CellFilterFunction> customFilter = MockCellFilterFunction.class;
        CellLocator locator = new CellLocator(fieldName, cellLocator, cellTextLocator, headerCellLocator, collection, tableSection, insertionComponent, customInsertion, filterComponent, customFilter);
        assertEquals(fieldName, locator.getFieldName());
        assertEquals(cellLocator, locator.getCellLocator());
        assertEquals(cellTextLocator, locator.getCellTextLocator());
        assertEquals(headerCellLocator, locator.getHeaderCellLocator());
        assertEquals(collection, locator.isCollection());
        assertEquals(tableSection, locator.getTableSection());
        assertEquals(insertionComponent, locator.getCellInsertionComponent());
        assertEquals(customInsertion, locator.getCustomCellInsertion());
        assertEquals(filterComponent, locator.getCellFilterComponent());
        assertEquals(customFilter, locator.getCustomCellFilter());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CellLocator locator = new CellLocator();
        locator.setFieldName("field");
        locator.setCellLocator(By.name("cell"));
        locator.setCellTextLocator(By.xpath("//text"));
        locator.setHeaderCellLocator(By.cssSelector(".header"));
        locator.setCollection(false);
        locator.setTableSection("section2");
        CellInsertionComponent insertionComponent = new CellInsertionComponent(MockComponentType.class, "dummy", 2);
        locator.setCellInsertionComponent(insertionComponent);
        locator.setCustomCellInsertion(MockCellInsertionFunction.class);
        CellFilterComponent filterComponent = new CellFilterComponent(MockComponentType.class, "dummyFilter");
        locator.setCellFilterComponent(filterComponent);
        locator.setCustomCellFilter(MockCellFilterFunction.class);
        assertEquals("field", locator.getFieldName());
        assertEquals(By.name("cell"), locator.getCellLocator());
        assertEquals(By.xpath("//text"), locator.getCellTextLocator());
        assertEquals(By.cssSelector(".header"), locator.getHeaderCellLocator());
        assertEquals(false, locator.isCollection());
        assertEquals("section2", locator.getTableSection());
        assertEquals(insertionComponent, locator.getCellInsertionComponent());
        assertEquals(MockCellInsertionFunction.class, locator.getCustomCellInsertion());
        assertEquals(filterComponent, locator.getCellFilterComponent());
        assertEquals(MockCellFilterFunction.class, locator.getCustomCellFilter());
    }

    @Test
    void testEqualsAndHashCode() {
        String fieldName = "field";
        By cellLocator = By.id("id");
        By cellTextLocator = By.className("class");
        By headerCellLocator = By.tagName("th");
        boolean collection = false;
        String tableSection = "sec";
        CellInsertionComponent insertionComponent = new CellInsertionComponent(MockComponentType.class, "dummy", 1);
        CellFilterComponent filterComponent = new CellFilterComponent(MockComponentType.class, "dummyFilter");
        CellLocator locator1 = new CellLocator(fieldName, cellLocator, cellTextLocator, headerCellLocator, collection, tableSection, insertionComponent, MockCellInsertionFunction.class, filterComponent, MockCellFilterFunction.class);
        CellLocator locator2 = new CellLocator(fieldName, cellLocator, cellTextLocator, headerCellLocator, collection, tableSection, insertionComponent, MockCellInsertionFunction.class, filterComponent, MockCellFilterFunction.class);
        assertEquals(locator1, locator2);
        assertEquals(locator1.hashCode(), locator2.hashCode());
        locator2.setFieldName("different");
        assertNotEquals(locator1, locator2);
    }
}