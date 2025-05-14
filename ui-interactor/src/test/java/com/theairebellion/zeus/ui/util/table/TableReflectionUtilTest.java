package com.theairebellion.zeus.ui.util.table;

import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableReflectionUtilTest {

   // Reuse or create minimal test data classes:

   @Test
   void extractTextsFromRow_WithSingleTableCell_ShouldReturnListOfText() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cellMock = mock(TableCell.class);
      when(cellMock.getText()).thenReturn("test text");
      testRow.cell = cellMock;

      // When
      List<String> result = TableReflectionUtil.extractTextsFromRow(testRow);

      // Then
      assertEquals(1, result.size());
      assertEquals("test text", result.get(0));
   }

   @Test
   void extractTextsFromRow_WithListOfTableCell_ShouldReturnListOfText() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cell1 = mock(TableCell.class);
      TableCell cell2 = mock(TableCell.class);
      when(cell1.getText()).thenReturn("text 1");
      when(cell2.getText()).thenReturn("text 2");

      testRow.cellList = Arrays.asList(cell1, cell2);

      // When
      List<String> result = TableReflectionUtil.extractTextsFromRow(testRow);

      // Then
      assertEquals(2, result.size());
      assertTrue(result.contains("text 1"));
      assertTrue(result.contains("text 2"));
   }

   @Test
   void extractTextsFromRow_WithBothTypes_ShouldReturnAllText() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cell1 = mock(TableCell.class);
      TableCell cell2 = mock(TableCell.class);
      TableCell cell3 = mock(TableCell.class);

      when(cell1.getText()).thenReturn("single cell");
      when(cell2.getText()).thenReturn("list cell 1");
      when(cell3.getText()).thenReturn("list cell 2");

      testRow.cell = cell1;
      testRow.cellList = Arrays.asList(cell2, cell3);

      // When
      List<String> result = TableReflectionUtil.extractTextsFromRow(testRow);

      // Then
      assertEquals(3, result.size());
      assertTrue(result.contains("single cell"));
      assertTrue(result.contains("list cell 1"));
      assertTrue(result.contains("list cell 2"));
   }

   @Test
   void extractElementsFromRow_WithSingleTableCell_ShouldReturnListOfElements() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cellMock = mock(TableCell.class);
      SmartWebElement elementMock = mock(SmartWebElement.class);
      when(cellMock.getElement()).thenReturn(elementMock);
      testRow.cell = cellMock;

      // When
      List<SmartWebElement> result = TableReflectionUtil.extractElementsFromRow(testRow);

      // Then
      assertEquals(1, result.size());
      assertEquals(elementMock, result.get(0));
   }

   @Test
   void extractElementsFromRow_WithListOfTableCell_ShouldReturnListOfElements() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cell1 = mock(TableCell.class);
      TableCell cell2 = mock(TableCell.class);
      SmartWebElement element1 = mock(SmartWebElement.class);
      SmartWebElement element2 = mock(SmartWebElement.class);

      when(cell1.getElement()).thenReturn(element1);
      when(cell2.getElement()).thenReturn(element2);

      testRow.cellList = Arrays.asList(cell1, cell2);

      // When
      List<SmartWebElement> result = TableReflectionUtil.extractElementsFromRow(testRow);

      // Then
      assertEquals(2, result.size());
      assertTrue(result.contains(element1));
      assertTrue(result.contains(element2));
   }

   @Test
   void extractElementsFromRow_WithBothTypes_ShouldReturnAllElements() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cell1 = mock(TableCell.class);
      TableCell cell2 = mock(TableCell.class);
      TableCell cell3 = mock(TableCell.class);

      SmartWebElement element1 = mock(SmartWebElement.class);
      SmartWebElement element2 = mock(SmartWebElement.class);
      SmartWebElement element3 = mock(SmartWebElement.class);

      when(cell1.getElement()).thenReturn(element1);
      when(cell2.getElement()).thenReturn(element2);
      when(cell3.getElement()).thenReturn(element3);

      testRow.cell = cell1;
      testRow.cellList = Arrays.asList(cell2, cell3);

      // When
      List<SmartWebElement> result = TableReflectionUtil.extractElementsFromRow(testRow);

      // Then
      assertEquals(3, result.size());
      assertTrue(result.contains(element1));
      assertTrue(result.contains(element2));
      assertTrue(result.contains(element3));
   }

   @Test
   void isListOfTableCell_WithListOfTableCell_ShouldReturnTrue() throws Exception {
      // Given
      Field field = TestTableRow.class.getDeclaredField("cellList");

      // When
      boolean result = TableReflectionUtil.isListOfTableCell(field);

      // Then
      assertTrue(result);
   }

   @Test
   void isListOfTableCell_WithNonList_ShouldReturnFalse() throws Exception {
      // Given
      Field field = TestTableRow.class.getDeclaredField("cell");

      // When
      boolean result = TableReflectionUtil.isListOfTableCell(field);

      // Then
      assertFalse(result);
   }

   @Test
   void isListOfTableCell_WithListOfNonTableCell_ShouldReturnFalse() throws Exception {
      // Given
      Field field = TestTableRow.class.getDeclaredField("stringList");

      // When
      boolean result = TableReflectionUtil.isListOfTableCell(field);

      // Then
      assertFalse(result);
   }

   @Test
   void isListOfTableCell_WithRawList_ShouldReturnFalse() throws Exception {
      // Given
      Field field = TestTableRow.class.getDeclaredField("rawList");

      // When
      boolean result = TableReflectionUtil.isListOfTableCell(field);

      // Then
      assertFalse(result);
   }

   @Test
   void lowerCaseAndTrim_ShouldProcessStrings() {
      // Given
      List<String> input = Arrays.asList("  HELLO ", " WorlD\t", "   tEst   ");

      // When
      List<String> result = TableReflectionUtil.lowerCaseAndTrim(input);

      // Then
      assertEquals(Arrays.asList("hello", "world", "test"), result);
   }

   static class TestTableRow {
      public TableCell cell;
      public List<TableCell> cellList;
      public List<String> stringList;
      public List<?> rawList;
   }
}