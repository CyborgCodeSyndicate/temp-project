package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import com.theairebellion.zeus.ui.service.tables.mock.MockRowClass;
import com.theairebellion.zeus.ui.service.tables.mock.MockTableElement;
import com.theairebellion.zeus.ui.service.tables.mock.MockTableService;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@DisplayName("TableServiceFluent Tests (with a fake TableService)")
class TableServiceFluentTest extends BaseUnitUITest {

   private MockTableService mockTableService;
   private UiServiceFluent<?> mockUiServiceFluent;
   private Storage mockStorage;
   private SmartWebDriver mockDriver;
   private TableServiceFluent<UiServiceFluent<?>> sut;
   static MockedStatic<ReflectionUtil> reflectionUtilMock;

   private static class MockRowClassPrivate {
      public void testField(String value) {
      }
   }

   @BeforeAll
   static void beforeAll() {
      reflectionUtilMock = mockStatic(ReflectionUtil.class);
      reflectionUtilMock.when(() ->
            ReflectionUtil.findEnumImplementationsOfInterface(
                  eq(TableComponentType.class),
                  anyString(),
                  anyString()
            )
      ).thenReturn(DefaultTableTypes.DEFAULT);
   }

   @BeforeEach
   void setUp() {
      mockTableService = spy(new MockTableService());
      mockUiServiceFluent = mock(UiServiceFluent.class);
      mockStorage = mock(Storage.class);
      mockDriver = mock(SmartWebDriver.class);

      sut = new TableServiceFluent<>(mockUiServiceFluent, mockStorage, mockTableService, mockDriver);
   }

   @Test
   @DisplayName("readTable should read entire table and store in storage")
   void readTableShouldReadAndStoreTable() {
      MockTableElement tableElement = new MockTableElement();
      List<MockRowClass> fakeRows = new ArrayList<>();
      mockTableService.setFakeReadTableResult(fakeRows);

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.readTable(tableElement);

      assertThat(result).isSameAs(mockUiServiceFluent);

      verify(subStorage).put(eq(tableElement.enumImpl()), eq(fakeRows));
      verify(mockTableService).readTable(DefaultTableTypes.DEFAULT, MockRowClass.class);
   }

   @Test
   @DisplayName("readTable with fields should call readTable(...) with the fields")
   void readTableWithFields() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.readTable(tableElement, field);
      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).readTable(eq(DefaultTableTypes.DEFAULT), eq(MockRowClass.class), eq(field));
   }

   @Test
   @DisplayName("readTable with start-end range should read table and store")
   void readTableWithRangeShouldReadAndStore() {
      MockTableElement tableElement = new MockTableElement();
      List<MockRowClass> fakeRows = new ArrayList<>();
      mockTableService.setFakeReadTableResult(fakeRows);

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.readTable(tableElement, 5, 10);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(subStorage).put(eq(tableElement.enumImpl()), eq(fakeRows));
      verify(mockTableService).readTable(DefaultTableTypes.DEFAULT, 5, 10, MockRowClass.class);
   }

   @Test
   @DisplayName("readTable with start-end range and fields should read table and store")
   void readTableWithRangeAndFieldsShouldReadAndStore() {
      MockTableElement tableElement = new MockTableElement();
      List<MockRowClass> fakeRows = new ArrayList<>();
      mockTableService.setFakeReadTableResult(fakeRows);
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.readTable(tableElement, 5, 10, field);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(subStorage).put(eq(tableElement.enumImpl()), eq(fakeRows));
      verify(mockTableService).readTable(DefaultTableTypes.DEFAULT, 5, 10, MockRowClass.class, field);
   }

   @Test
   @DisplayName("readRow should read a specific row and store it")
   void readSpecificRow() {
      MockTableElement tableElement = new MockTableElement();
      var rowObj = new MockRowClass();
      mockTableService.setFakeReadRowResult(rowObj);

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.readRow(tableElement, 42);
      assertThat(result).isSameAs(mockUiServiceFluent);

      verify(mockTableService).readRow(eq(DefaultTableTypes.DEFAULT), eq(42), eq(MockRowClass.class));
      verify(subStorage).put(eq(tableElement.enumImpl()), eq(rowObj));
   }

   @Test
   @DisplayName("readRow with searchCriteria should read row and store")
   void readRowWithSearchCriteriaShouldReadAndStore() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClass rowObj = new MockRowClass();
      mockTableService.setFakeReadRowResult(rowObj);

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      List<String> criteria = List.of("criteria1", "criteria2");
      var result = sut.readRow(tableElement, criteria);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).readRow(DefaultTableTypes.DEFAULT, criteria, MockRowClass.class);
      verify(subStorage).put(eq(tableElement.enumImpl()), eq(rowObj));
   }

   @Test
   @DisplayName("readRow should read a specific row with table fields and store it")
   void readSpecificRowWithTableFields() {
      MockTableElement tableElement = new MockTableElement();
      var rowObj = new MockRowClass();
      mockTableService.setFakeReadRowResult(rowObj);
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.readRow(tableElement, 42, field);
      assertThat(result).isSameAs(mockUiServiceFluent);

      verify(mockTableService).readRow(eq(DefaultTableTypes.DEFAULT), eq(42), eq(MockRowClass.class), eq(field));
      verify(subStorage).put(eq(tableElement.enumImpl()), eq(rowObj));
   }

   @Test
   @DisplayName("readRow with searchCriteria should read row and store")
   void readRowWithSearchCriteriaAndTableFieldsShouldReadAndStore() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClass rowObj = new MockRowClass();
      mockTableService.setFakeReadRowResult(rowObj);
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      List<String> criteria = List.of("criteria1", "criteria2");
      var result = sut.readRow(tableElement, criteria, field);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).readRow(DefaultTableTypes.DEFAULT, criteria, MockRowClass.class, field);
      verify(subStorage).put(eq(tableElement.enumImpl()), eq(rowObj));
   }

   @Test
   @DisplayName("insertCellValue with row and fields should call tableService with the correct arguments")
   void insertCellValueWithRowAndFieldsShouldInsertValue() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.insertCellValue(tableElement, 1, field, "someVal");
      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(1),
            eq(MockRowClass.class),
            eq(field),
            eq(1),
            eq("someVal")
      );
   }

   @Test
   @DisplayName("insertCellValue with row, field and index should call tableService with the correct arguments")
   void insertCellValueWithRowFieldAndIndexShouldInsertValue() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.insertCellValue(tableElement, 1, field, 2, "someVal");
      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(1),
            eq(MockRowClass.class),
            eq(field),
            eq(2),
            eq("someVal")
      );
   }

   @Test
   @DisplayName("insertCellValue with searchCriteria and field should call tableService with the correct arguments")
   void insertCellValueWithSearchCriteriaAndFieldsShouldInsertValue() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      List<String> criteria = List.of("search1", "search2");

      var result = sut.insertCellValue(tableElement, criteria, field, "value");
      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(criteria),
            eq(MockRowClass.class),
            eq(field),
            eq(1),
            eq("value")
      );
   }

   @Test
   @DisplayName("insertCellValue with searchCriteria field and index should call tableService with correct args")
   void insertCellValueWithSearchCriteriaFieldAndIndexShouldInsertValue() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      List<String> criteria = List.of("search1", "search2");

      var result = sut.insertCellValue(tableElement, criteria, field, 1, "value");
      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(criteria),
            eq(MockRowClass.class),
            eq(field),
            eq(1),
            eq("value")
      );
   }

   @Test
   @DisplayName("insertCellValueAsData with row and data should call tableService with correct args")
   void insertCellValueWithRowAndDataShouldInsertValue() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClass data = new MockRowClass();

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.insertCellValueAsData(tableElement, 7, data);
      assertThat(result).isSameAs(mockUiServiceFluent);

      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(7),
            eq(MockRowClass.class),
            eq(data)
      );
   }

   @Test
   @DisplayName("insertCellValueAsData with row and wrong data object should throw IllegalArgumentException")
   void insertCellValueWithRowAndDataWrongDataObject() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClassPrivate data = new MockRowClassPrivate();

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      assertThatThrownBy(() -> sut.insertCellValueAsData(tableElement, 7, data))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The Data object must be from class: " + tableElement.rowsRepresentationClass());
   }

   @Test
   @DisplayName("insertCellValueAsData with searchCriteria and data should call tableService with correct args")
   void insertCellValueWithSearchCriteriaAndDataShouldInsertValue() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClass data = new MockRowClass();

      List<String> criteria = List.of("search1", "search2");

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.insertCellValueAsData(tableElement, criteria, data);
      assertThat(result).isSameAs(mockUiServiceFluent);

      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(criteria),
            eq(MockRowClass.class),
            eq(data)
      );
   }

   @Test
   @DisplayName("insertCellValueAsData with searchCriteria and wrong data object should throw IllegalArgumentException")
   void insertCellValueWithSearchCriteriaAndDataWrongDataObject() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClassPrivate data = new MockRowClassPrivate();

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      List<String> criteria = List.of("search1", "search2");

      assertThatThrownBy(() -> sut.insertCellValueAsData(tableElement, criteria, data))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The Data object must be from class: " + tableElement.rowsRepresentationClass());
   }

   @Test
   @DisplayName("filterTable with column and filterStrategy by values should call tableService with correct args")
   void filterTableWithColumnAndStrategyByValues() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> column = TableField.of((MockRowClass t, String v) -> t.testField(v));
      FilterStrategy filterStrategy = FilterStrategy.SELECT;
      String[] filterValues = {"value1", "value2"};

      var result = sut.filterTable(tableElement, column, filterStrategy, filterValues);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).filterTable(
            eq(DefaultTableTypes.DEFAULT),
            eq(MockRowClass.class),
            eq(column),
            eq(filterStrategy),
            eq(filterValues)
      );
   }

   @Test
   @DisplayName("clickElementInCell with row and field should call tableService with the correct arguments")
   void clickElementInCellWithRowAndFieldShouldClickCellElement() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.clickElementInCell(tableElement, 3, field);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(3),
            eq(MockRowClass.class),
            eq(field),
            eq(1)
      );
   }

   @Test
   @DisplayName("clickElementInCell with row, field and index should call tableService with the correct arguments")
   void clickElementInCellWithRowFieldsAndIndexShouldClickCellElement() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.clickElementInCell(tableElement, 3, field, 2);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(3),
            eq(MockRowClass.class),
            eq(field),
            eq(2)
      );
   }

   @Test
   @DisplayName("clickElementInCell with searchCriteria and field should call tableService with the correct arguments")
   void clickElementInCellWithSearchCriteriaAndFieldShouldClickCellElement() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      List<String> criteria = List.of("search1", "search2");

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.clickElementInCell(tableElement, criteria, field);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(criteria),
            eq(MockRowClass.class),
            eq(field),
            eq(1)
      );
   }

   @Test
   @DisplayName("clickElementInCell with searchCriteria field and index should call tableService with the "
         + "correct arguments")
   void clickElementInCellWithSearchCriteriaFieldAndIndexShouldClickCellElement() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> field = TableField.of((MockRowClass t, String v) -> t.testField(v));

      List<String> criteria = List.of("search1", "search2");

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.clickElementInCell(tableElement, criteria, field, 2);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(criteria),
            eq(MockRowClass.class),
            eq(field),
            eq(2)
      );
   }

   @Test
   @DisplayName("clickElementInCell with row and data should call tableService with the correct arguments")
   void clickElementInCellWithRowAndDataShouldClickCellElement() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClass data = new MockRowClass();

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.clickElementInCell(tableElement, 3, data);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(3),
            eq(MockRowClass.class),
            eq(data)
      );
   }

   @Test
   @DisplayName("clickElementInCell with searchCriteria and data should call tableService with the "
         + "correct arguments")
   void clickElementInCellWithSearchCriteriaAndDataShouldClickCellElement() {
      MockTableElement tableElement = new MockTableElement();
      MockRowClass data = new MockRowClass();

      List<String> criteria = List.of("search1", "search2");

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var result = sut.clickElementInCell(tableElement, criteria, data);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).insertCellValue(
            eq(DefaultTableTypes.DEFAULT),
            eq(criteria),
            eq(MockRowClass.class),
            eq(data)
      );
   }

   @Test
   @DisplayName("sortTable with column and sortingStrategy should call tableService with correct args")
   void sortTableWithColumnAndStrategy() {
      MockTableElement tableElement = new MockTableElement();
      TableField<MockRowClass> column = TableField.of((MockRowClass t, String v) -> t.testField(v));
      SortingStrategy sortingStrategy = SortingStrategy.ASC;

      var result = sut.sortTable(tableElement, column, sortingStrategy);

      assertThat(result).isSameAs(mockUiServiceFluent);
      verify(mockTableService).sortTable(
            eq(DefaultTableTypes.DEFAULT),
            eq(MockRowClass.class),
            eq(column),
            eq(sortingStrategy)
      );
   }

   @Test
   @DisplayName("validate should retrieve table data from storage and call tableService.validate")
   void validateShouldCallTableServiceValidate() {
      MockTableElement tableElement = new MockTableElement();
      Assertion assertion = mock(Assertion.class);
      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);

      var fakeData = new MockRowClass();
      when(subStorage.get(eq(tableElement.enumImpl()), eq(Object.class))).thenReturn(fakeData);

      List<AssertionResult<Object>> fakeResults = List.of(new AssertionResult<>(true, "desc", null, null, false));
      mockTableService.setFakeValidateResults(fakeResults);

      var result = sut.validate(tableElement, assertion);
      assertThat(result).isSameAs(mockUiServiceFluent);

      verify(mockTableService).validate(eq(fakeData), eq(assertion));
   }

   @Test
   @DisplayName("validate should throw if no table data found")
   void validateThrowsWhenNoData() {
      MockTableElement tableElement = new MockTableElement();
      Assertion assertion = mock(Assertion.class);

      Storage subStorage = mock(Storage.class);
      when(mockStorage.sub(UI)).thenReturn(subStorage);
      when(subStorage.get(eq(tableElement.enumImpl()), eq(Object.class))).thenReturn(null);

      assertThatThrownBy(() -> sut.validate(tableElement, assertion))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("No table data found for key");
   }

   @Test
   @DisplayName("readTable with private TableField class should throw exception")
   void validateArgumentsWithPrivateTableFieldClassCatchException() {
      MockTableElement tableElement = new MockTableElement();
      List<MockRowClassPrivate> fakeRows = new ArrayList<>();
      mockTableService.setFakeReadTableResult(fakeRows);
      TableField<MockRowClassPrivate> field = TableField.of((MockRowClassPrivate t, String v) -> t.testField(v));
      Class<?> expectedRowClass = MockRowClass.class;

      assertThatThrownBy(() -> sut.readTable(tableElement, field))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The TableField objects should be from class: " + expectedRowClass);
   }
}
