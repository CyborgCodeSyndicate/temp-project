package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.annotations.mock.MockCellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.annotations.mock.MockComponentType;
import com.theairebellion.zeus.ui.components.table.annotations.mock.MockCellFilterFunction;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("all")
class TableAnnotationsTest extends BaseUnitUITest {

    @Test
    public void testCellFilterAnnotation() throws NoSuchFieldException {
        class Mock {
            @CellFilter(type = MockComponentType.class, componentType = "DUMMY")
            private String field = "dummy";
        }
        Field field = Mock.class.getDeclaredField("field");
        CellFilter annotation = field.getAnnotation(CellFilter.class);
        assertEquals(MockComponentType.class, annotation.type());
        assertEquals("DUMMY", annotation.componentType());
    }

    @Test
    public void testCellInsertionAnnotation() throws NoSuchFieldException {
        class Mock {
            @CellInsertion(type = MockComponentType.class, componentType = "DUMMY", order = 5)
            private String field = "dummy";
        }
        Field field = Mock.class.getDeclaredField("field");
        CellInsertion annotation = field.getAnnotation(CellInsertion.class);
        assertEquals(MockComponentType.class, annotation.type());
        assertEquals("DUMMY", annotation.componentType());
        assertEquals(5, annotation.order());
    }

    @Test
    public void testCustomCellFilterAnnotation() throws NoSuchFieldException {
        class Mock {
            @CustomCellFilter(cellFilterFunction = MockCellFilterFunction.class)
            private String field = "dummy";
        }
        Field field = Mock.class.getDeclaredField("field");
        CustomCellFilter annotation = field.getAnnotation(CustomCellFilter.class);
        assertEquals(MockCellFilterFunction.class, annotation.cellFilterFunction());
    }

    @Test
    public void testCustomCellInsertionAnnotation() throws NoSuchFieldException {
        class Mock {
            @CustomCellInsertion(insertionFunction = MockCellInsertionFunction.class, order = 3)
            private String field = "dummy";
        }
        Field field = Mock.class.getDeclaredField("field");
        CustomCellInsertion annotation = field.getAnnotation(CustomCellInsertion.class);
        assertEquals(MockCellInsertionFunction.class, annotation.insertionFunction());
        assertEquals(3, annotation.order());
    }

    @Test
    public void testTableCellLocatorAnnotationDefaults() throws NoSuchFieldException {
        class Mock {
            @TableCellLocator(cellLocator = @FindBy(xpath = "//td"))
            private String field = "dummy";
        }
        Field field = Mock.class.getDeclaredField("field");
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
        class MockTable {}
        TableInfo annotation = MockTable.class.getAnnotation(TableInfo.class);
        assertEquals("//table", annotation.tableContainerLocator().xpath());
        assertEquals(".//tr", annotation.rowsLocator().xpath());
        assertEquals(".//thead/tr", annotation.headerRowLocator().xpath());
    }
}