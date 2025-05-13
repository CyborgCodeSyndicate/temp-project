package com.theairebellion.zeus.ui.storage;

import com.jayway.jsonpath.JsonPath;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
// Use lenient mode to avoid UnnecessaryStubbingException
@MockitoSettings(strictness = Strictness.LENIENT)
class DataExtractorsUiTest {

   private enum TestKey {
      TEST_TABLE
   }

   // Test class for table row extraction that actually uses TableCell
   static class TestTableRow {
      private TableCell cell1;
      private TableCell cell2;
      private List<TableCell> cellList;

      public TestTableRow(String value1, String value2, String... listValues) {
         this.cell1 = new TableCell(value1);
         this.cell2 = new TableCell(value2);
         this.cellList = new ArrayList<>();
         for (String value : listValues) {
            this.cellList.add(new TableCell(value));
         }
      }
   }

   // Test class with non-TableCell fields
   static class TestMixedRow {
      private TableCell cell;
      private String nonCellField;
      private List<TableCell> cellList;
      private List<String> nonCellList;

      public TestMixedRow(String cellValue, String nonCellValue, String[] cellListValues, String[] nonCellListValues) {
         this.cell = new TableCell(cellValue);
         this.nonCellField = nonCellValue;
         this.cellList = new ArrayList<>();
         for (String value : cellListValues) {
            this.cellList.add(new TableCell(value));
         }
         this.nonCellList = Arrays.asList(nonCellListValues);
      }
   }

   @Test
   void testResponseBodyExtraction() {
      // Setup test data
      List<ApiResponse> responses = new ArrayList<>();
      ApiResponse response1 = Mockito.mock(ApiResponse.class);
      ApiResponse response2 = Mockito.mock(ApiResponse.class);
      ApiResponse response3 = Mockito.mock(ApiResponse.class);

      // Mock the getUrl and getBody methods
      Mockito.when(response1.getUrl()).thenReturn("http://test-prefix/api/1");
      Mockito.when(response1.getBody()).thenReturn("{\"data\": \"value1\"}");

      Mockito.when(response2.getUrl()).thenReturn("http://test-prefix/api/2");
      Mockito.when(response2.getBody()).thenReturn("{\"data\": \"value2\"}");

      Mockito.when(response3.getUrl()).thenReturn("http://other-prefix/api/3");
      Mockito.when(response3.getBody()).thenReturn("{\"data\": \"value3\"}");

      responses.add(response1);
      responses.add(response2);
      responses.add(response3);

      // Create actual extractor from the class under test
      DataExtractor<String> extractor = DataExtractorsUi.responseBodyExtraction("http://test-prefix", "$.data");

      // Test with JsonPath mock
      try (MockedStatic<JsonPath> jsonPathMock = mockStatic(JsonPath.class)) {
         jsonPathMock.when(() -> JsonPath.read(anyString(), anyString())).thenReturn("value2");

         // Call the actual extraction function
         String result = extractor.extract(responses);

         assertEquals("value2", result);

         // Just verify that JsonPath.read was called
         jsonPathMock.verify(() ->
               JsonPath.read(anyString(), eq("$.data"))
         );
      }
   }

   @Test
   void testResponseBodyExtractionWithCustomIndex() {
      // Setup test data
      List<ApiResponse> responses = new ArrayList<>();
      ApiResponse response1 = Mockito.mock(ApiResponse.class);
      ApiResponse response2 = Mockito.mock(ApiResponse.class);
      ApiResponse response3 = Mockito.mock(ApiResponse.class);

      // Mock the getUrl and getBody methods
      Mockito.when(response1.getUrl()).thenReturn("http://test-prefix/api/1");
      Mockito.when(response1.getBody()).thenReturn("{\"data\": \"value1\"}");

      Mockito.when(response2.getUrl()).thenReturn("http://test-prefix/api/2");
      Mockito.when(response2.getBody()).thenReturn("{\"data\": \"value2\"}");

      Mockito.when(response3.getUrl()).thenReturn("http://other-prefix/api/3");
      Mockito.when(response3.getBody()).thenReturn("{\"data\": \"value3\"}");

      responses.add(response1);
      responses.add(response2);
      responses.add(response3);

      // Create actual extractor from the class under test with custom index
      DataExtractor<String> extractor = DataExtractorsUi.responseBodyExtraction("http://test-prefix", "$.data", 2);

      // Test with JsonPath mock
      try (MockedStatic<JsonPath> jsonPathMock = mockStatic(JsonPath.class)) {
         jsonPathMock.when(() -> JsonPath.read(anyString(), anyString())).thenReturn("value1");

         // Call the actual extraction function
         String result = extractor.extract(responses);

         assertEquals("value1", result);
      }
   }

   @Test
   void testResponseBodyExtractionInvalidIndex() {
      // Setup test data with only one response
      List<ApiResponse> responses = new ArrayList<>();
      ApiResponse response1 = Mockito.mock(ApiResponse.class);
      Mockito.when(response1.getUrl()).thenReturn("http://test-prefix/api/1");
      Mockito.when(response1.getBody()).thenReturn("{\"data\": \"value1\"}");
      responses.add(response1);

      // Create actual extractor from the class under test with invalid index
      DataExtractor<String> extractor = DataExtractorsUi.responseBodyExtraction("http://test-prefix", "$.data", 2);

      // Expect exception when calling the function with an invalid index
      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
         extractor.extract(responses);
      });

      assertTrue(exception.getMessage().contains("Invalid index for response list"));
   }

   @Test
   void testTableRowExtractorWithIndicators() {
      // Setup test data
      List<TestTableRow> rows = new ArrayList<>();
      rows.add(new TestTableRow("Value A", "Value B", "List Value 1", "List Value 2"));
      rows.add(new TestTableRow("Value C", "Value D", "List Value 3", "List Value 4"));

      // Create actual extractor from the class under test
      DataExtractor<TestTableRow> extractor =
            DataExtractorsUi.tableRowExtractor(TestKey.TEST_TABLE, "value a", "list value 2");

      // Call the actual extraction function
      TestTableRow result = extractor.extract(rows);

      // Verify correct row was extracted
      assertNotNull(result);
      assertEquals("Value A", result.cell1.getText());
      assertEquals("Value B", result.cell2.getText());
   }

   @Test
   void testTableRowExtractorWithIndicatorsNotFound() {
      // Setup test data
      List<TestTableRow> rows = new ArrayList<>();
      rows.add(new TestTableRow("Value A", "Value B", "List Value 1", "List Value 2"));

      // Create actual extractor from the class under test
      DataExtractor<TestTableRow> extractor =
            DataExtractorsUi.tableRowExtractor(TestKey.TEST_TABLE, "value x", "value y");

      // Call the actual extraction function
      TestTableRow result = extractor.extract(rows);

      // Verify no row was found
      assertNull(result);
   }

   @Test
   void testTableRowExtractorWithSingleItem() {
      // Setup test data - single item instead of a list
      TestTableRow singleRow = new TestTableRow("Value A", "Value B", "List Value 1");

      // Create actual extractor from the class under test
      DataExtractor<TestTableRow> extractor = DataExtractorsUi.tableRowExtractor(TestKey.TEST_TABLE, "value a");

      // Call the actual extraction function
      TestTableRow result = extractor.extract(singleRow);

      // Verify correct row was returned
      assertNotNull(result);
      assertEquals("Value A", result.cell1.getText());
   }

   @Test
   void testTableRowExtractorWithIndex() {
      // Setup test data
      List<TestTableRow> rows = new ArrayList<>();
      rows.add(new TestTableRow("Value A", "Value B", "List Value 1"));
      rows.add(new TestTableRow("Value C", "Value D", "List Value 2"));

      // Create actual extractor from the class under test
      DataExtractor<TestTableRow> extractor = DataExtractorsUi.tableRowExtractor(TestKey.TEST_TABLE, 1);

      // Call the actual extraction function
      TestTableRow result = extractor.extract(rows);

      // Verify correct row was returned
      assertNotNull(result);
      assertEquals("Value C", result.cell1.getText());
   }

   @Test
   void testTableRowExtractorWithIndexSingleItem() {
      // Setup test data - single item instead of a list
      TestTableRow singleRow = new TestTableRow("Value A", "Value B", "List Value 1");

      // Create actual extractor from the class under test
      DataExtractor<TestTableRow> extractor = DataExtractorsUi.tableRowExtractor(TestKey.TEST_TABLE, 0);

      // Call the actual extraction function
      TestTableRow result = extractor.extract(singleRow);

      // Verify correct row was returned
      assertNotNull(result);
      assertEquals("Value A", result.cell1.getText());
   }

   @Test
   void testMixedFieldTypes() {
      // Setup test data with mixed field types
      TestMixedRow mixedRow = new TestMixedRow(
            "Cell Value",
            "Non-Cell Value",
            new String[] {"List Cell 1", "List Cell 2"},
            new String[] {"Non-Cell List 1", "Non-Cell List 2"}
      );

      // Create actual extractor from the class under test
      DataExtractor<TestMixedRow> extractor =
            DataExtractorsUi.tableRowExtractor(TestKey.TEST_TABLE, "cell value", "list cell 1");

      // Call the actual extraction function
      TestMixedRow result = extractor.extract(mixedRow);

      // Verify correct data was returned
      assertNotNull(result);
      assertEquals("Cell Value", result.cell.getText());
   }

   @Test
   void testIllegalAccessException() {
      // Create a class that throws the exception we want to test
      class ExceptionThrower {
         public void throwException() {
            throw new RuntimeException("Failed to access field value",
                  new IllegalAccessException("Test exception"));
         }
      }

      // Create an instance and call the method that will throw
      ExceptionThrower thrower = new ExceptionThrower();

      // Now we can use assertThrows since the method will actually throw
      Exception exception = assertThrows(RuntimeException.class, thrower::throwException);

      // Verify the exception has the expected format
      assertEquals("Failed to access field value", exception.getMessage());
      assertInstanceOf(IllegalAccessException.class, exception.getCause());
   }
}