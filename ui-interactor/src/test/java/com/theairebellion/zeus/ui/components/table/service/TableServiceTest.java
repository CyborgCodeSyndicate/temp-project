package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.mock.MockTableComponentType;
import com.theairebellion.zeus.ui.components.table.service.mock.MockTableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


@DisplayName("TableService Interface Default Methods Test")
@SuppressWarnings("all")
class TableServiceTest extends BaseUnitUITest {

   private static final Class<TestData> TEST_DATA_CLASS = TestData.class;
   private static final MockTableComponentType DEFAULT_TYPE = MockTableComponentType.DUMMY_TABLE;
   private static final List<String> SAMPLE_CRITERIA = List.of("search");
   private static final String[] SAMPLE_VALUES = {"val1", "val2"};
   private static final String SINGLE_VALUE = "val1";
   private static final String ROW_DATA_STRING = "rowData";
   private static final String SAMPLE_FILTER_VALUE = "filterVal";
   private static final List<TestData> SAMPLE_ROW_LIST = List.of(new TestData());
   private MockTableService service;
   @Mock
   private TableField<TestData> mockTableField;

   @BeforeEach
   void setUp() {
      // Given
      MockitoAnnotations.openMocks(this);
      service = new MockTableService();
      service.reset();
   }

   private static class TestData {
   }

   @Nested
   @DisplayName("ReadTable Default Method Tests")
   class ReadTableTests {
      @Test
      @DisplayName("readTable(Class) delegates correctly")
      void readTableDefault() {
         // Given
         service.returnList = SAMPLE_ROW_LIST;

         // When
         var result = service.readTable(TEST_DATA_CLASS);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
      }

      @Test
      @DisplayName("readTable(Class, fields...) delegates correctly")
      void readTableWithFieldsDefault() {
         // Given
         service.returnList = SAMPLE_ROW_LIST;

         // When
         var result = service.readTable(TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastFields).containsExactly(mockTableField);
      }

      @Test
      @DisplayName("readTable(start, end, Class) delegates correctly")
      void readTableRangeDefault() {
         // Given
         service.returnList = SAMPLE_ROW_LIST;

         // When
         var result = service.readTable(1, 2, TEST_DATA_CLASS);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastStart).isEqualTo(1);
         assertThat(service.lastEnd).isEqualTo(2);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
      }

      @Test
      @DisplayName("readTable(start, end, Class, fields...) delegates correctly")
      void readTableRangeWithFieldsDefault() {
         // Given
         service.returnList = SAMPLE_ROW_LIST;

         // When
         var result = service.readTable(1, 2, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastStart).isEqualTo(1);
         assertThat(service.lastEnd).isEqualTo(2);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastFields).containsExactly(mockTableField);
      }
   }

   @Nested
   @DisplayName("ReadRow Default Method Tests")
   class ReadRowTests {

      // Define a TestData instance or mock to be returned
      private TestData expectedTestData;

      @BeforeEach
      void setupReadRowTests() {
         // Given: Set the mock service to return a TestData object for readRow calls
         expectedTestData = mock(TestData.class);
         service.returnObject = expectedTestData;
      }

      @Test
      @DisplayName("readRow(index, Class) delegates correctly")
      void readRowByIndexDefault() {
         // Given - service.returnObject set in nested @BeforeEach

         // When
         var result = service.readRow(3, TEST_DATA_CLASS);

         // Then
         assertThat(result).isInstanceOf(TestData.class);
         assertThat(result).isSameAs(expectedTestData);
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastRow).isEqualTo(3);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
      }

      @Test
      @DisplayName("readRow(criteria, Class) delegates correctly")
      void readRowByCriteriaDefault() {
         // Given - service.returnObject set in nested @BeforeEach

         // When
         var result = service.readRow(SAMPLE_CRITERIA, TEST_DATA_CLASS);

         // Then
         assertThat(result).isInstanceOf(TestData.class);
         assertThat(result).isSameAs(expectedTestData);
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastSearchCriteria).isEqualTo(SAMPLE_CRITERIA);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
      }

      @Test
      @DisplayName("readRow(index, Class, fields...) delegates correctly")
      void readRowByIndexWithFieldsDefault() {
         // Given - service.returnObject set in nested @BeforeEach

         // When
         var result = service.readRow(2, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(result).isInstanceOf(TestData.class);
         assertThat(result).isSameAs(expectedTestData);
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastRow).isEqualTo(2);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastFields).containsExactly(mockTableField);
      }

      @Test
      @DisplayName("readRow(criteria, Class, fields...) delegates correctly")
      void readRowByCriteriaWithFieldsDefault() {
         // Given - service.returnObject set in nested @BeforeEach

         // When
         var result = service.readRow(SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(result).isInstanceOf(TestData.class);
         assertThat(result).isSameAs(expectedTestData);
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastSearchCriteria).isEqualTo(SAMPLE_CRITERIA);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastFields).containsExactly(mockTableField);
      }
   }

   @Nested
   @DisplayName("InsertCellValue Default Method Tests")
   class InsertCellValueTests {
      @Test
      @DisplayName("insertCellValue(row, class, data) delegates correctly")
      void insertCellValueByRowDefault() {
         // Given
         var data = new TestData();

         // When
         service.insertCellValue(4, TEST_DATA_CLASS, data);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastRow).isEqualTo(4);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastDataObject).isEqualTo(data);
      }

      @Test
      @DisplayName("insertCellValue(row, class, field, values...) delegates correctly")
      void insertCellValueByRowAndFieldDefault() {
         // Given

         // When
         service.insertCellValue(4, TEST_DATA_CLASS, mockTableField, SAMPLE_VALUES);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastRow).isEqualTo(4);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastColumn).isEqualTo(mockTableField);
         assertThat(service.lastValues).isEqualTo(SAMPLE_VALUES);
      }

      @Test
      @DisplayName("insertCellValue(row, class, field, index, values...) delegates correctly")
      void insertCellValueByRowAndFieldIndexDefault() {
         // Given

         // When
         service.insertCellValue(3, TEST_DATA_CLASS, mockTableField, 2, SINGLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastRow).isEqualTo(3);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastColumn).isEqualTo(mockTableField);
         assertThat(service.lastValues).containsExactly(SINGLE_VALUE);
      }

      @Test
      @DisplayName("insertCellValue(criteria, class, field, values...) delegates correctly")
      void insertCellValueByCriteriaAndFieldDefault() {
         // Given

         // When
         service.insertCellValue(SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField, SAMPLE_VALUES);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastSearchCriteria).isEqualTo(SAMPLE_CRITERIA);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastColumn).isEqualTo(mockTableField);
         assertThat(service.lastValues).isEqualTo(SAMPLE_VALUES);
      }

      @Test
      @DisplayName("insertCellValue(criteria, class, field, index, values...) delegates correctly")
      void insertCellValueByCriteriaAndFieldIndexDefault() {
         // Given

         // When
         service.insertCellValue(SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField, 2, SINGLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastSearchCriteria).isEqualTo(SAMPLE_CRITERIA);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastColumn).isEqualTo(mockTableField);
         assertThat(service.lastValues).containsExactly(SINGLE_VALUE);
      }

      @Test
      @DisplayName("insertCellValue(criteria, class, data) delegates correctly")
      void insertCellValueByCriteriaObjectDefault() {
         // Given
         var data = new TestData();

         // When
         service.insertCellValue(SAMPLE_CRITERIA, TEST_DATA_CLASS, data);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastSearchCriteria).isEqualTo(SAMPLE_CRITERIA);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastDataObject).isEqualTo(data);
      }
   }

   @Nested
   @DisplayName("Table Operation Default Method Tests")
   class TableOperationTests {
      @Test
      @DisplayName("filterTable delegates correctly")
      void filterTableDefault() {
         // Given

         // When
         service.filterTable(TEST_DATA_CLASS, mockTableField, FilterStrategy.SELECT, SAMPLE_FILTER_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastColumn).isEqualTo(mockTableField);
         assertThat(service.lastFilterStrategy).isEqualTo(FilterStrategy.SELECT);
         assertThat(service.lastValues).containsExactly(SAMPLE_FILTER_VALUE);
      }

      @Test
      @DisplayName("sortTable delegates correctly")
      void sortTableDefault() {
         // Given

         // When
         service.sortTable(TEST_DATA_CLASS, mockTableField, SortingStrategy.ASC);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastClazz).isEqualTo(TEST_DATA_CLASS);
         assertThat(service.lastColumn).isEqualTo(mockTableField);
         assertThat(service.lastSortingStrategy).isEqualTo(SortingStrategy.ASC);
      }
   }
}