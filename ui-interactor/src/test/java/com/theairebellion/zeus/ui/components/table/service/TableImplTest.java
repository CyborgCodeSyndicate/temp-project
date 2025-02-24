package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.components.table.model.TableLocators;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.mock.*;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
public class TableImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private SmartWebElement container;
    private SmartWebElement rowElement;
    private TableServiceRegistry registry;
    private TableLocators locators;
    private TestTableImpl tableImpl;

    @BeforeEach
    void setUp() {
        driver = mock(SmartWebDriver.class);
        container = mock(SmartWebElement.class);
        rowElement = mock(SmartWebElement.class);
        registry = new TableServiceRegistry();
        locators = new TableLocators(By.xpath("//table"), By.xpath(".//tr"), By.xpath(".//thead/tr"));
        List<SmartWebElement> rows = new ArrayList<>();
        rows.add(rowElement);
        tableImpl = new TestTableImpl(driver, registry, locators, container, rows);
    }

    @Test
    void testReadTableWithoutFields() {
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        SmartWebElement cellElement = mock(SmartWebElement.class);
        when(rowElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn("cellValue");
        List<MockRow> result = tableImpl.readTable(MockRow.class);
        assertNotNull(result);
        assertEquals(1, result.size());
        MockRow row = result.get(0);
        assertNotNull(row.getCell());
        assertEquals("cellValue", row.getCell().getText());
    }

    @Test
    void testReadRowByIndex() {
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        SmartWebElement cellElement = mock(SmartWebElement.class);
        when(rowElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn("rowValue");
        MockRow row = tableImpl.readRow(1, MockRow.class);
        assertNotNull(row);
        assertNotNull(row.getCell());
        assertEquals("rowValue", row.getCell().getText());
    }

    @Test
    void testReadRowByCriteria() {
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(rowElement.getAttribute("innerText")).thenReturn("criteria match");
        SmartWebElement cellElement = mock(SmartWebElement.class);
        when(rowElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn("criteriaValue");
        MockRow row = tableImpl.readRow(List.of("criteria"), MockRow.class);
        assertNotNull(row);
        assertNotNull(row.getCell());
        assertEquals("criteriaValue", row.getCell().getText());
    }

    @Test
    void testInsertCellValueByRowIndexInvalidIndex() {
        when(container.findSmartElements(any(By.class))).thenReturn(Collections.emptyList());
        Exception ex = assertThrows(RuntimeException.class, () ->
                tableImpl.insertCellValue(1, MockRow.class, new MockTableField("cell"), 1, "value"));
        assertTrue(ex.getMessage().contains("Invalid cell index: 1 for locator"));
    }

    @Test
    void testInsertCellValueUsingCustomFunction() {
        TestTableImpl localTable = new TestTableImpl(driver, registry, locators, container, List.of(rowElement));
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        SmartWebElement cellElement = mock(SmartWebElement.class);
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        when(cellElement.getText()).thenReturn("initial");
        Exception ex = assertThrows(RuntimeException.class, () ->
                localTable.insertCellValue(1, MockRowCustom.class, (instance, value) -> instance.setCell((TableCell) value), 1, "newValue"));
        assertTrue(ex.getMessage().contains("failed")
                && (ex.getMessage().contains("instantiat") || ex.getMessage().contains("Custom insertion failed")));
    }

    @Test
    void testInsertCellValueUsingComponentNoRegistry() {
        TestTableImpl localTable = new TestTableImpl(driver, null, locators, container, List.of(rowElement));
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        SmartWebElement cellElement = mock(SmartWebElement.class);
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        MockTableField field = new MockTableField("cell");
        Exception ex = assertThrows(IllegalStateException.class, () ->
                localTable.insertCellValue(1, MockRow.class, field, 1, "value"));
        assertTrue(ex.getMessage().contains("Your instance of table is not having registered services"));
    }

    @Test
    void testFilterTableUsingComponent() {
        registry.registerService(MockComponentType.class, new TableFilter() {
            @Override
            public void tableFilter(SmartWebElement cellElement, ComponentType componentType, FilterStrategy filterStrategy, String... values) {
                cellElement.sendKeys("filtered");
            }
        });
        TestTableImpl filterTable = new TestTableImpl(driver, registry, locators, container, List.of(rowElement));
        MockTableFieldForFilter field = new MockTableFieldForFilter();
        Exception ex = assertThrows(RuntimeException.class, () ->
                filterTable.filterTable(MockRowForFilter.class, field, FilterStrategy.SELECT, "filtered"));
        assertTrue(ex.getMessage().contains("Failed to filter using component"));
    }

    @Test
    void testSortTableDelegation() {
        MockTableField field = new MockTableField("cell");
        assertDoesNotThrow(() ->
                tableImpl.sortTable(MockRow.class, field, SortingStrategy.ASC));
    }
}