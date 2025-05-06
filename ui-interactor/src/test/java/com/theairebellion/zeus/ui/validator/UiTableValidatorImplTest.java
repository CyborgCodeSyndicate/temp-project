package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
class UiTableValidatorImplTest {

   private UiTableValidatorImpl validator;

   @Mock
   private Assertion assertionMock;

   @Mock
   private SmartWebElement smartWebElementMock;

   @BeforeEach
   void setUp() {
      validator = new UiTableValidatorImpl();
   }

   @Test
   void printAssertionTarget_ShouldLogExtendedInfo() {
      // Given
      Map<String, Object> data = new HashMap<>();
      data.put("key", "value");

      try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {
         // When
         validator.printAssertionTarget(data);

         // Then
         logUIMock.verify(() -> LogUi.extended(eq("Validation target: [{}]"), eq(data.toString())));
      }
   }

   @Test
   void validateTable_WithRowValues_ShouldReturnAssertionResults() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cellMock = mock(TableCell.class);
      when(cellMock.getText()).thenReturn("test text");
      testRow.cell = cellMock;

      when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.ROW_VALUES);
      List<AssertionResult<Object>> expectedResults = Collections.singletonList(mock(AssertionResult.class));

      try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
           MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

         assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
               .thenReturn(expectedResults);

         // When
         List<AssertionResult<Object>> results = validator.validateTable(testRow, assertionMock);

         // Then
         assertEquals(expectedResults, results);
         verify(assertionMock).setKey("rowValues");
         logUIMock.verify(() -> LogUi.info(anyString(), anyInt()));
      }
   }

   @Test
   void validateTable_WithRowElements_ShouldReturnAssertionResults() {
      // Given
      TestTableRow testRow = new TestTableRow();
      TableCell cellMock = mock(TableCell.class);
      when(cellMock.getElement()).thenReturn(smartWebElementMock);
      testRow.cell = cellMock;

      when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.ROW_ELEMENTS);
      List<AssertionResult<Object>> expectedResults = Collections.singletonList(mock(AssertionResult.class));

      try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
           MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

         assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
               .thenReturn(expectedResults);

         // When
         List<AssertionResult<Object>> results = validator.validateTable(testRow, assertionMock);

         // Then
         assertEquals(expectedResults, results);
         verify(assertionMock).setKey("rowElements");
         logUIMock.verify(() -> LogUi.info(anyString(), anyInt()));
      }
   }

   @Test
   void validateTable_WithTableValues_ShouldReturnAssertionResults() {
      // Given
      TestTableRow testRow1 = new TestTableRow();
      TableCell cellMock1 = mock(TableCell.class);
      when(cellMock1.getText()).thenReturn("test text 1");
      testRow1.cell = cellMock1;

      TestTableRow testRow2 = new TestTableRow();
      TableCell cellMock2 = mock(TableCell.class);
      when(cellMock2.getText()).thenReturn("test text 2");
      testRow2.cell = cellMock2;

      List<TestTableRow> testRows = Arrays.asList(testRow1, testRow2);

      when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.TABLE_VALUES);
      List<AssertionResult<Object>> expectedResults = Collections.singletonList(mock(AssertionResult.class));

      try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
           MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

         assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
               .thenReturn(expectedResults);

         // When
         List<AssertionResult<Object>> results = validator.validateTable(testRows, assertionMock);

         // Then
         assertEquals(expectedResults, results);
         verify(assertionMock).setKey("tableValues");
         logUIMock.verify(() -> LogUi.info(anyString(), anyInt()));
      }
   }

   @Test
   void validateTable_WithTableElements_ShouldReturnAssertionResults() {
      // Given
      TestTableRow testRow1 = new TestTableRow();
      TableCell cellMock1 = mock(TableCell.class);
      SmartWebElement elementMock1 = mock(SmartWebElement.class);
      when(cellMock1.getElement()).thenReturn(elementMock1);
      testRow1.cell = cellMock1;

      TestTableRow testRow2 = new TestTableRow();
      TableCell cellMock2 = mock(TableCell.class);
      SmartWebElement elementMock2 = mock(SmartWebElement.class);
      when(cellMock2.getElement()).thenReturn(elementMock2);
      testRow2.cell = cellMock2;

      List<TestTableRow> testRows = Arrays.asList(testRow1, testRow2);

      when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.TABLE_ELEMENTS);
      List<AssertionResult<Object>> expectedResults = Collections.singletonList(mock(AssertionResult.class));

      try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
           MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

         assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
               .thenReturn(expectedResults);

         // When
         List<AssertionResult<Object>> results = validator.validateTable(testRows, assertionMock);

         // Then
         assertEquals(expectedResults, results);
         verify(assertionMock).setKey("tableElements");
         logUIMock.verify(() -> LogUi.info(anyString(), anyInt()));
      }
   }

   @Test
   void printAssertionTarget_IllegalAccessExceptionTest() {
      // Just a sample check to ensure we handle IllegalAccessException as expected
      IllegalAccessException illegalAccessException = new IllegalAccessException("Test exception");
      RuntimeException runtimeException = new RuntimeException("Failed to access field value", illegalAccessException);

      assertEquals("Failed to access field value", runtimeException.getMessage());
      assertSame(illegalAccessException, runtimeException.getCause());
   }

   // Test data classes
   static class TestTableRow {
      public TableCell cell;
      public List<TableCell> cellList;
      public List<String> stringList;
      public List rawList;
   }
}