package com.theairebellion.zeus.ui.components.table.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;

@SuppressWarnings("all")
public class TableAnnotationsTest {

    public enum DummyComponentType implements ComponentType {
        DUMMY, FAIL, NON_EXISTENT;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @Test
    public void testCellFilterAnnotation() throws NoSuchFieldException {
        class Dummy {
            @CellFilter(type = DummyComponentType.class, componentType = "TEST_FILTER")
            private String field = "dummy";
        }
        Field field = Dummy.class.getDeclaredField("field");
        CellFilter annotation = field.getAnnotation(CellFilter.class);
        assertEquals(DummyComponentType.class, annotation.type());
        assertEquals("TEST_FILTER", annotation.componentType());
    }

    @Test
    public void testCellInsertionAnnotation() throws NoSuchFieldException {
        class Dummy {
            @CellInsertion(type = DummyComponentType.class, componentType = "TEST_INSERT", order = 5)
            private String field = "dummy";
        }
        Field field = Dummy.class.getDeclaredField("field");
        CellInsertion annotation = field.getAnnotation(CellInsertion.class);
        assertEquals(DummyComponentType.class, annotation.type());
        assertEquals("TEST_INSERT", annotation.componentType());
        assertEquals(5, annotation.order());
    }

    public static class DummyCellFilterFunction implements CellFilterFunction {
        @Override
        public void cellFilterFunction(com.theairebellion.zeus.ui.selenium.smart.SmartWebElement cellElement,
                                       FilterStrategy filterStrategy, String... values) {
        }
    }

    @Test
    public void testCustomCellFilterAnnotation() throws NoSuchFieldException {
        class Dummy {
            @CustomCellFilter(cellFilterFunction = DummyCellFilterFunction.class)
            private String field = "dummy";
        }
        Field field = Dummy.class.getDeclaredField("field");
        CustomCellFilter annotation = field.getAnnotation(CustomCellFilter.class);
        assertEquals(DummyCellFilterFunction.class, annotation.cellFilterFunction());
    }

    public static class DummyCellInsertionFunction implements CellInsertionFunction {
        @Override
        public void cellInsertionFunction(com.theairebellion.zeus.ui.selenium.smart.SmartWebElement cellElement, String... values) {
        }
    }

    @Test
    public void testCustomCellInsertionAnnotation() throws NoSuchFieldException {
        class Dummy {
            @CustomCellInsertion(insertionFunction = DummyCellInsertionFunction.class, order = 3)
            private String field = "dummy";
        }
        Field field = Dummy.class.getDeclaredField("field");
        CustomCellInsertion annotation = field.getAnnotation(CustomCellInsertion.class);
        assertEquals(DummyCellInsertionFunction.class, annotation.insertionFunction());
        assertEquals(3, annotation.order());
    }

    @Test
    public void testTableCellLocatorAnnotationDefaults() throws NoSuchFieldException {
        class Dummy {
            @TableCellLocator(cellLocator = @FindBy(xpath = "//td"))
            private String field = "dummy";
        }
        Field field = Dummy.class.getDeclaredField("field");
        TableCellLocator annotation = field.getAnnotation(TableCellLocator.class);
        assertEquals("//td", annotation.cellLocator().xpath());
        assertEquals("", annotation.tableSection());
        assertEquals(".", annotation.cellTextLocator().xpath());
        assertEquals(".", annotation.headerCellLocator().xpath());
    }

    @Test
    public void testTableInfoAnnotation() {
        @TableInfo(
                tableContainerLocator = @FindBy(xpath = "//table"),
                rowsLocator = @FindBy(xpath = ".//tr"),
                headerRowLocator = @FindBy(xpath = ".//thead/tr")
        )
        class DummyTable {}
        TableInfo annotation = DummyTable.class.getAnnotation(TableInfo.class);
        assertEquals("//table", annotation.tableContainerLocator().xpath());
        assertEquals(".//tr", annotation.rowsLocator().xpath());
        assertEquals(".//thead/tr", annotation.headerRowLocator().xpath());
    }
}