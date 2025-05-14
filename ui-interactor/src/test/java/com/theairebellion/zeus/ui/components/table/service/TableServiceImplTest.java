package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.mock.MockTableComponentType;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayName("TableServiceImpl Tests")
@SuppressWarnings("all")
class TableServiceImplTest extends BaseUnitUITest {

   private static final Class<TestData> TEST_DATA_CLASS = TestData.class;
   private static final List<String> SAMPLE_CRITERIA = List.of("search");
   private static final String SAMPLE_FILTER_VALUE = "filterVal";
   private static final String SAMPLE_TABLE_OBJECT = "table object";
   private static final List<TestData> SAMPLE_ROW_LIST = List.of(new TestData());
   private final MockTableComponentType componentType = MockTableComponentType.DUMMY_TABLE;
   @Mock
   private SmartWebDriver smartWebDriver;
   @Mock
   private TableServiceRegistry tableServiceRegistry;
   @Mock
   private UiTableValidator uiTableValidator;
   @Mock
   private Table tableMock;
   @Mock
   private TableImpl mockTableImpl;
   @Mock
   private TableField<TestData> mockTableField;
   @Mock
   private Assertion mockAssertion;
   @Mock
   private TestData mockTestData;
   private TableServiceImpl service;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      service = new TableServiceImpl(smartWebDriver, tableServiceRegistry, uiTableValidator);

      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getTableComponent(any(TableComponentType.class), eq(smartWebDriver)))
            .thenReturn(mockTableImpl);

      // Use doReturn/when for generics
      doReturn(SAMPLE_ROW_LIST).when(mockTableImpl).readTable(any(Class.class));
      doReturn(SAMPLE_ROW_LIST).when(mockTableImpl).readTable(any(Class.class), any(TableField[].class));
      doReturn(SAMPLE_ROW_LIST).when(mockTableImpl).readTable(anyInt(), anyInt(), any(Class.class));
      doReturn(SAMPLE_ROW_LIST).when(mockTableImpl).readTable(anyInt(), anyInt(), any(Class.class), any(TableField[].class));

      doReturn(mockTestData).when(mockTableImpl).readRow(anyInt(), any(Class.class));
      doReturn(mockTestData).when(mockTableImpl).readRow(any(List.class), any(Class.class));
      doReturn(mockTestData).when(mockTableImpl).readRow(anyInt(), any(Class.class), any(TableField[].class));
      doReturn(mockTestData).when(mockTableImpl).readRow(any(List.class), any(Class.class), any(TableField[].class));

      // Specify matchers precisely for overloaded methods
      doNothing().when(mockTableImpl).insertCellValue(anyInt(), any(Class.class), any(TestData.class));
      doNothing().when(mockTableImpl).insertCellValue(anyInt(), any(Class.class), any(TableField.class), anyInt(), any(String[].class));
      doNothing().when(mockTableImpl).insertCellValue(anyList(), any(Class.class), any(TableField.class), anyInt(), any(String[].class));
      doNothing().when(mockTableImpl).insertCellValue(anyList(), any(Class.class), any(TestData.class));

      doNothing().when(mockTableImpl).filterTable(any(Class.class), any(TableField.class), any(FilterStrategy.class), any(String[].class));
      doNothing().when(mockTableImpl).sortTable(any(Class.class), any(TableField.class), any(SortingStrategy.class));
   }

   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }

   private static class TestData {
   }

   @Nested
   @DisplayName("ReadTable Method Delegation")
   class ReadTableTests {
      @Test
      @DisplayName("readTable(type, class) delegates")
      void testReadTableNoFields() {
         // Given

         // When
         List<TestData> actualResult = service.readTable(componentType, TEST_DATA_CLASS);

         // Then
         assertThat(actualResult).isSameAs(SAMPLE_ROW_LIST);
         verify(mockTableImpl).readTable(TEST_DATA_CLASS);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock); // Verify interface mock wasn't used directly for these calls
      }

      @Test
      @DisplayName("readTable(type, class, fields) delegates")
      void testReadTableWithFields() {
         // Given

         // When
         var actualResult = service.readTable(componentType, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(actualResult).isSameAs(SAMPLE_ROW_LIST);
         verify(mockTableImpl).readTable(eq(TEST_DATA_CLASS), eq(mockTableField));
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }

      @Test
      @DisplayName("readTable(type, start, end, class) delegates")
      void testReadTableRangeNoFields() {
         // Given

         // When
         var actualResult = service.readTable(componentType, 1, 3, TEST_DATA_CLASS);

         // Then
         assertThat(actualResult).isSameAs(SAMPLE_ROW_LIST);
         verify(mockTableImpl).readTable(1, 3, TEST_DATA_CLASS);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }

      @Test
      @DisplayName("readTable(type, start, end, class, fields) delegates")
      void testReadTableRangeWithFields() {
         // Given

         // When
         var actualResult = service.readTable(componentType, 1, 3, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(actualResult).isSameAs(SAMPLE_ROW_LIST);
         verify(mockTableImpl).readTable(1, 3, TEST_DATA_CLASS, mockTableField);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }
   }

   @Nested
   @DisplayName("ReadRow Method Delegation")
   class ReadRowTests {
      @Test
      @DisplayName("readRow(type, index, class) delegates")
      void testReadRowByIndex() {
         // Given

         // When
         var result = service.readRow(componentType, 2, TEST_DATA_CLASS);

         // Then
         assertThat(result).isSameAs(mockTestData);
         verify(mockTableImpl).readRow(2, TEST_DATA_CLASS);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }

      @Test
      @DisplayName("readRow(type, criteria, class) delegates")
      void testReadRowByCriteria() {
         // Given

         // When
         var result = service.readRow(componentType, SAMPLE_CRITERIA, TEST_DATA_CLASS);

         // Then
         assertThat(result).isSameAs(mockTestData);
         verify(mockTableImpl).readRow(SAMPLE_CRITERIA, TEST_DATA_CLASS);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }

      @Test
      @DisplayName("readRow(type, index, class, fields) delegates")
      void testReadRowByIndexWithFields() {
         // Given

         // When
         var result = service.readRow(componentType, 2, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(result).isSameAs(mockTestData);
         verify(mockTableImpl).readRow(2, TEST_DATA_CLASS, mockTableField);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }

      @Test
      @DisplayName("readRow(type, criteria, class, fields) delegates")
      void testReadRowByCriteriaWithFields() {
         // Given

         // When
         var result = service.readRow(componentType, SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField);

         // Then
         assertThat(result).isSameAs(mockTestData);
         verify(mockTableImpl).readRow(SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
         verifyNoMoreInteractions(tableMock);
      }
   }

   @Nested
   @DisplayName("InsertCellValue Method Delegation")
   class InsertCellValueTests {
      @Test
      @DisplayName("insertCellValue(type, index, class, data) delegates")
      void testInsertCellValueDataByIndex() {
         // Given - mockTestData and other mocks setup in @BeforeEach

         // When
         service.insertCellValue(componentType, 2, TEST_DATA_CLASS, mockTestData);

         // Then
         verify(mockTableImpl).insertCellValue(2, TEST_DATA_CLASS, mockTestData);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
      }

      @Test
      @DisplayName("insertCellValue(type, index, class, field, cellIndex, value) delegates")
      void testInsertCellValueFieldByIndex() {
         // Given - setup in @BeforeEach

         // When
         service.insertCellValue(componentType, 2, TEST_DATA_CLASS, mockTableField, 1, SAMPLE_FILTER_VALUE);

         // Then
         verify(mockTableImpl).insertCellValue(2, TEST_DATA_CLASS, mockTableField, 1, SAMPLE_FILTER_VALUE);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
      }

      @Test
      @DisplayName("insertCellValue(type, criteria, class, data) delegates")
      void testInsertCellValueDataByCriteria() {
         // Given - setup in @BeforeEach

         // When
         service.insertCellValue(componentType, SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTestData);

         // Then
         verify(mockTableImpl).insertCellValue(SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTestData);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
      }

      @Test
      @DisplayName("insertCellValue(type, criteria, class, field, cellIndex, value) delegates")
      void testInsertCellValueFieldByCriteria() {
         // Given - setup in @BeforeEach

         // When
         service.insertCellValue(componentType, SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField, 1, SAMPLE_FILTER_VALUE);

         // Then
         verify(mockTableImpl).insertCellValue(SAMPLE_CRITERIA, TEST_DATA_CLASS, mockTableField, 1, SAMPLE_FILTER_VALUE);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
      }
   }

   @Nested
   @DisplayName("Table Operation Method Delegation")
   class TableOperationTests {
      @Test
      @DisplayName("filterTable delegates")
      void testFilterTable() {
         // Given - setup in @BeforeEach

         // When
         service.filterTable(componentType, TEST_DATA_CLASS, mockTableField, FilterStrategy.SELECT, SAMPLE_FILTER_VALUE);

         // Then
         verify(mockTableImpl).filterTable(TEST_DATA_CLASS, mockTableField, FilterStrategy.SELECT, SAMPLE_FILTER_VALUE);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
      }

      @Test
      @DisplayName("sortTable delegates")
      void testSortTable() {
         // Given - setup in @BeforeEach

         // When
         service.sortTable(componentType, TEST_DATA_CLASS, mockTableField, SortingStrategy.ASC);

         // Then
         verify(mockTableImpl).sortTable(TEST_DATA_CLASS, mockTableField, SortingStrategy.ASC);
         verify(mockTableImpl).setServiceRegistry(tableServiceRegistry);
      }
   }

   @Nested
   @DisplayName("Validation Method Tests")
   class ValidationTests {
      @Test
      @DisplayName("validate delegates to UiTableValidator")
      void testValidate_WithValidInput() {
         // Given
         List<AssertionResult<?>> expectedResults = List.of(mock(AssertionResult.class));
         doReturn(expectedResults).when(uiTableValidator).validateTable(eq(SAMPLE_TABLE_OBJECT), eq(mockAssertion));

         // When
         var results = service.validate(SAMPLE_TABLE_OBJECT, mockAssertion);

         // Then
         assertThat(results).isSameAs(expectedResults);
         verify(uiTableValidator).validateTable(SAMPLE_TABLE_OBJECT, mockAssertion);
         factoryMock.verifyNoInteractions();
         verifyNoInteractions(tableMock);
         verify(mockTableImpl, never()).setServiceRegistry(any());
      }

      @Test
      @DisplayName("validate throws exception for null table")
      void testValidate_WithNullTable() {
         // Given

         // When / Then
         assertThatThrownBy(() -> service.validate(null, mockAssertion))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage("Table cannot be null for validation.");
         verifyNoInteractions(uiTableValidator);
      }

      @Test
      @DisplayName("validate throws exception for null assertions")
      void testValidate_WithNullAssertions() {
         // Given

         // When / Then
         assertThatThrownBy(() -> service.validate(SAMPLE_TABLE_OBJECT, (Assertion[]) null))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage("At least one assertion must be provided.");
         verifyNoInteractions(uiTableValidator);
      }

      @Test
      @DisplayName("validate throws exception for empty assertions")
      void testValidate_WithEmptyAssertions() {
         // Given

         // When / Then
         assertThatThrownBy(() -> service.validate(SAMPLE_TABLE_OBJECT))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage("At least one assertion must be provided.");
         verifyNoInteractions(uiTableValidator);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {
      @Test
      @DisplayName("Component is cached and reused")
      void testComponentCaching() {
         // Given

         // When
         service.readTable(componentType, TEST_DATA_CLASS);
         service.readRow(componentType, 1, TEST_DATA_CLASS);

         // Then
         factoryMock.verify(() -> ComponentFactory.getTableComponent(eq(componentType), eq(smartWebDriver)), times(1));
         verify(mockTableImpl, times(1)).setServiceRegistry(tableServiceRegistry);
      }
   }
}