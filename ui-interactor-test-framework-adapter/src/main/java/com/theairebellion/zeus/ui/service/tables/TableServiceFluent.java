package com.theairebellion.zeus.ui.service.tables;


import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUiServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import com.theairebellion.zeus.validator.core.Assertion;
import io.qameta.allure.Allure;
import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent methods for interacting with table components in the UI.
 *
 * <p>This class offers functionality for reading, inserting, filtering, and sorting table data,
 * as well as validating table contents.
 *
 * @param <T> The type of {@link UiServiceFluent} for chaining fluent methods.
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class TableServiceFluent<T extends UiServiceFluent<?>> {

   private static final String UI_TABLE_CLICKING_ELEMENT_IN_CELL_AT_ROW =
         "[UI - Table] Clicking element in cell at row: ";
   private static final String FIELD = ", field: ";
   private static final String INDEX = ", index: ";
   private static final String USING_DATA = " using data: ";
   private static final String UI_TABLE_CLICKING_ELEMENT_IN_CELL_FOR_SEARCH_CRITERIA =
         "[UI - Table] Clicking element in cell for search criteria: ";

   private final TableService tableService;
   private final T uiServiceFluent;
   private final Storage storage;
   private final SmartWebDriver driver;
   private final DecoratorsFactory decoratorsFactory = new DecoratorsFactory();

   /**
    * Constructs a new {@code TableServiceFluent} instance.
    *
    * @param uiServiceFluent The UI service instance for fluent chaining.
    * @param storage         The storage instance for persisting table data.
    * @param tableService    The table service handling table interactions.
    * @param webDriver       The smart WebDriver instance.
    */
   public TableServiceFluent(T uiServiceFluent, Storage storage, TableService tableService,
                             SmartWebDriver webDriver) {
      this.tableService = tableService;
      this.uiServiceFluent = uiServiceFluent;
      this.storage = storage;
      driver = webDriver;
   }

   /**
    * Reads the entire table and stores the result in storage.
    *
    * @param <K>          The type representing table rows.
    * @param tableElement The table element to be read.
    * @return The fluent UI service instance.
    */
   public final <K> T readTable(TableElement<?> tableElement) {
      Allure.step("[UI - Table] Reading the table: " + tableElement);
      tableElement.before().accept(driver);
      List<K> rows = tableService.readTable(tableElement.tableType(), tableElement.rowsRepresentationClass());
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), rows);
      return uiServiceFluent;
   }

   /**
    * Reads specific fields from the table and stores the result in storage.
    *
    * @param <K>          The type representing table rows.
    * @param tableElement The table element to be read.
    * @param fields       The specific fields to read from the table.
    * @return The fluent UI service instance.
    */
   @SafeVarargs
   public final <K> T readTable(TableElement<?> tableElement, TableField<K>... fields) {
      Allure.step("[UI - Table] Reading the table with specific fields: " + tableElement);
      validateArguments(fields[0], tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(),
            tableService.readTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), fields));
      return uiServiceFluent;
   }

   /**
    * Reads a range of rows from the table and stores the result in storage.
    *
    * @param tableElement The table element to be read.
    * @param start        The starting row index (inclusive).
    * @param end          The ending row index (exclusive).
    * @return The fluent UI service instance.
    */
   public final T readTable(TableElement<?> tableElement, int start, int end) {
      Allure.step("[UI - Table] Reading a range of rows from the table: " + tableElement + " from "
            + start + " to " + end);
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), tableService.readTable(tableElement.tableType(), start, end,
            tableElement.rowsRepresentationClass()));
      return uiServiceFluent;
   }

   /**
    * Reads a specific range of rows from the table with only the specified fields and stores the result in storage.
    *
    * @param <K>          The type representing table rows.
    * @param tableElement The table element to be read.
    * @param start        The starting row index (inclusive).
    * @param end          The ending row index (exclusive).
    * @param fields       The fields to be extracted from the table. If not provided, all fields are retrieved.
    * @return The fluent UI service instance for method chaining.
    */
   @SafeVarargs
   public final <K> T readTable(TableElement<?> tableElement, int start, int end,
                                TableField<K>... fields) {
      Allure.step("[UI - Table] Reading a range of rows with specific fields from the table: " + tableElement
            + " from " + start + " to " + end);
      validateArguments(fields[0], tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), tableService.readTable(tableElement.tableType(), start, end,
            tableElement.rowsRepresentationClass(), fields));
      return uiServiceFluent;
   }

   /**
    * Reads a specific row from the table and stores the result in storage.
    *
    * @param tableElement The table element to be read.
    * @param row          The index of the row to read.
    * @return The fluent UI service instance.
    */
   public final T readRow(TableElement<?> tableElement, int row) {
      Allure.step("[UI - Table] Reading a specific row from the table: " + tableElement + " at row index: " + row);
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), tableService.readRow(tableElement.tableType(), row,
            tableElement.rowsRepresentationClass()));
      return uiServiceFluent;
   }

   /**
    * Reads a single row from the table based on the provided search criteria and stores the result in storage.
    *
    * @param tableElement   The table element to be read.
    * @param searchCriteria A list of values that must be matched within the row.
    * @return The fluent UI service instance for method chaining.
    */
   public final T readRow(TableElement<?> tableElement, List<String> searchCriteria) {
      Allure.step("[UI - Table] Reading a specific row from the table by search criteria: " + tableElement
            + " with criteria: " + searchCriteria);
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), tableService.readRow(tableElement.tableType(), searchCriteria,
            tableElement.rowsRepresentationClass()));
      return uiServiceFluent;
   }

   /**
    * Reads a single row from the table based on the row index and stores the result in storage.
    *
    * @param <K>          The type representing the table row.
    * @param tableElement The table element to be read.
    * @param row          The row index (1-based index).
    * @param fields       The fields to be extracted from the row. If not provided, all fields are retrieved.
    * @return The fluent UI service instance for method chaining.
    */
   @SafeVarargs
   public final <K> T readRow(TableElement<?> tableElement, int row,
                              TableField<K>... fields) {
      Allure.step("[UI - Table] Reading a specific row from the table with specific fields: " + tableElement
            + " at row index: " + row);
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), tableService.readRow(tableElement.tableType(), row,
            tableElement.rowsRepresentationClass(), fields));
      return uiServiceFluent;
   }

   /**
    * Reads a row from the table that matches the provided search criteria, retrieving only the specified fields.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element to be read.
    * @param searchCriteria A list of values that must be matched within the row.
    * @param fields         The specific fields to be extracted from the matched row.
    * @return The fluent UI service instance for method chaining.
    */
   @SafeVarargs
   public final <K> T readRow(TableElement<?> tableElement, List<String> searchCriteria,
                              TableField<K>... fields) {
      Allure.step("[UI - Table] Reading a row with search criteria and specific fields: " + tableElement
            + " with criteria: " + searchCriteria);
      validateArguments(fields[0], tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableElement.after().accept(driver);
      storage.sub(UI).put(tableElement.enumImpl(), tableService.readRow(tableElement.tableType(), searchCriteria,
            tableElement.rowsRepresentationClass(), fields));
      return uiServiceFluent;
   }

   /**
    * Inserts a value into a table cell based on the row index.
    *
    * @param <K>          The type representing table rows.
    * @param tableElement The table element to insert data into.
    * @param row          The row index.
    * @param field        The table field to insert data into.
    * @param values       The values to insert.
    * @return The fluent UI service instance.
    */
   public final <K> T insertCellValue(TableElement<?> tableElement, int row,
                                      TableField<K> field, String... values) {
      Allure.step("[UI - Table] Inserting value into cell in row: " + row + " for field: " + field);
      validateArguments(field, tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), field, 1,
            values);
      tableElement.after().accept(driver);
      return uiServiceFluent;
   }

   /**
    * Inserts values into a specific cell within a row of the table at the given index.
    *
    * @param <K>          The type representing the table row.
    * @param tableElement The table element where the value will be inserted.
    * @param row          The row index (1-based index).
    * @param field        The field within the row where the value should be inserted.
    * @param index        The index of the cell within the row (1-based index).
    * @param value        The values to be inserted into the specified cell.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T insertCellValue(TableElement<?> tableElement, int row,
                                      TableField<K> field,
                                      int index, String... value) {
      Allure.step("[UI - Table] Inserting cell value into row: " + row + FIELD + field + INDEX + index);
      validateArguments(field, tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), field,
            index, value);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully inserted value into cell");
      return uiServiceFluent;
   }

   /**
    * Inserts values into a specific cell in the row that matches the given search criteria.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element where the value will be inserted.
    * @param searchCriteria A list of values used to locate the row.
    * @param field          The field within the row where the values should be inserted.
    * @param values         The values to be inserted into the specified field.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T insertCellValue(TableElement<?> tableElement, List<String> searchCriteria,
                                      TableField<K> field,
                                      String... values) {
      Allure.step("[UI - Table] Inserting cell value for search criteria: " + searchCriteria + FIELD + field);
      validateArguments(field, tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
            field, 1, values);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully inserted value for search criteria");
      return uiServiceFluent;
   }

   /**
    * Inserts values into a specific cell in the row that matches the given search criteria, at the given index.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element where the value will be inserted.
    * @param searchCriteria A list of values used to locate the row.
    * @param field          The field within the row where the values should be inserted.
    * @param index          The index of the cell within the row (1-based index).
    * @param values         The values to be inserted into the specified field.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T insertCellValue(TableElement<?> tableElement, List<String> searchCriteria,
                                      TableField<K> field,
                                      int index, String... values) {
      Allure.step("[UI - Table] Inserting cell value at index: " + index + " for search criteria: "
            + searchCriteria + FIELD + field);
      validateArguments(field, tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
            field, index, values);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully inserted value at index");
      return uiServiceFluent;
   }

   /**
    * Inserts a full data object into a specific row in the table at the given index.
    *
    * @param <K>          The type representing the table row.
    * @param tableElement The table element where the data will be inserted.
    * @param row          The row index (1-based index).
    * @param data         The object containing values to be inserted into the row.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T insertCellValueAsData(TableElement<?> tableElement, int row, K data) {
      Allure.step("[UI - Table] Inserting data into row: " + row + USING_DATA + data);
      if (!tableElement.rowsRepresentationClass().equals(data.getClass())) {
         throw new IllegalArgumentException(
               "The Data object must be from class: " + tableElement.rowsRepresentationClass());
      }
      tableElement.before().accept(driver);
      tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), data);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully inserted data into row");
      return uiServiceFluent;
   }

   /**
    * Inserts a full data object into a row that matches the given search criteria.
    *
    * <p>This method finds a row in the table that matches the search criteria and inserts values
    * into multiple fields based on the provided data object.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element where the data will be inserted.
    * @param searchCriteria A list of values used to locate the row.
    * @param data           The object containing values to be inserted into the row.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T insertCellValueAsData(TableElement<?> tableElement, List<String> searchCriteria, K data) {
      Allure.step("[UI - Table] Inserting data for search criteria: " + searchCriteria + USING_DATA + data);
      if (!tableElement.rowsRepresentationClass().equals(data.getClass())) {
         throw new IllegalArgumentException(
               "The Data object must be from class: " + tableElement.rowsRepresentationClass());
      }
      tableElement.before().accept(driver);
      tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
            data);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully inserted data for search criteria");
      return uiServiceFluent;
   }

   /**
    * Filters a table column based on the specified filtering strategy.
    *
    * @param <K>            The type representing table rows.
    * @param tableElement   The table element to filter.
    * @param column         The table column field to filter.
    * @param filterStrategy The filtering strategy to apply.
    * @param values         The values to filter by.
    * @return The fluent UI service instance.
    */
   public final <K> T filterTable(TableElement<?> tableElement,
                                  TableField<K> column,
                                  FilterStrategy filterStrategy, String... values) {
      Allure.step("[UI - Table] Filtering table using column: " + column + " with strategy: " + filterStrategy
            + " and values: " + values);
      validateArguments(column, tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableService.filterTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), column,
            filterStrategy, values);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully filtered table");
      return uiServiceFluent;
   }

   /**
    * Sorts a table column based on the specified sorting strategy.
    *
    * @param <K>             The type representing table rows.
    * @param tableElement    The table element to sort.
    * @param column          The table column field to sort.
    * @param sortingStrategy The sorting strategy to apply.
    * @return The fluent UI service instance.
    */
   public final <K> T sortTable(TableElement<?> tableElement, TableField<K> column,
                                SortingStrategy sortingStrategy) {
      Allure.step("[UI - Table] Sorting table using column: " + column + " with strategy: " + sortingStrategy);
      validateArguments(column, tableElement.rowsRepresentationClass());
      tableElement.before().accept(driver);
      tableService.sortTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), column,
            sortingStrategy);
      tableElement.after().accept(driver);
      Allure.step("[UI - Table] Successfully sorted table");
      return uiServiceFluent;
   }

   /**
    * Clicks an element inside a specific cell within a row of the table at the given index.
    *
    * @param <K>          The type representing the table row.
    * @param tableElement The table element where the action will be performed.
    * @param row          The row index (1-based index).
    * @param field        The field within the row that should be clicked.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T clickElementInCell(TableElement<?> tableElement, int row,
                                         TableField<K> field) {
      Allure.step(UI_TABLE_CLICKING_ELEMENT_IN_CELL_AT_ROW + row + FIELD + field);
      return insertCellValue(tableElement, row, field);
   }

   /**
    * Clicks an element inside a specific cell within a row of the table at the given cell index.
    *
    * @param <K>          The type representing the table row.
    * @param tableElement The table element where the action will be performed.
    * @param row          The row index (1-based index).
    * @param field        The field within the row that should be clicked.
    * @param index        The index of the cell within the row (1-based index).
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T clickElementInCell(TableElement<?> tableElement, int row, TableField<K> field, int index) {
      Allure.step(UI_TABLE_CLICKING_ELEMENT_IN_CELL_AT_ROW + row + FIELD + field + INDEX + index);
      return insertCellValue(tableElement, row, field, index);
   }

   /**
    * Clicks an element inside a specific cell in the row that matches the given search criteria.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element where the action will be performed.
    * @param searchCriteria A list of values used to locate the row.
    * @param field          The field within the row that should be clicked.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T clickElementInCell(TableElement<?> tableElement, List<String> searchCriteria,
                                         TableField<K> field) {
      Allure.step(
            UI_TABLE_CLICKING_ELEMENT_IN_CELL_FOR_SEARCH_CRITERIA + searchCriteria + FIELD + field);
      return insertCellValue(tableElement, searchCriteria, field);
   }

   /**
    * Clicks an element inside a specific cell in the row that matches the given search criteria at the given index.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element where the action will be performed.
    * @param searchCriteria A list of values used to locate the row.
    * @param field          The field within the row that should be clicked.
    * @param index          The index of the cell within the row (1-based index).
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T clickElementInCell(TableElement<?> tableElement, List<String> searchCriteria,
                                         TableField<K> field, int index) {
      Allure.step(UI_TABLE_CLICKING_ELEMENT_IN_CELL_FOR_SEARCH_CRITERIA + searchCriteria + FIELD + field
            + INDEX + index);
      return insertCellValue(tableElement, searchCriteria, field, index);
   }

   /**
    * Clicks an element inside a specific row in the table based on the given data object.
    *
    * @param <K>          The type representing the table row.
    * @param tableElement The table element where the action will be performed.
    * @param row          The row index (1-based index).
    * @param data         The object containing values to locate and interact with elements inside the row.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T clickElementInCell(TableElement<?> tableElement, int row, K data) {
      Allure.step(UI_TABLE_CLICKING_ELEMENT_IN_CELL_AT_ROW + row + USING_DATA + data);
      return insertCellValueAsData(tableElement, row, data);
   }

   /**
    * Clicks an element inside a specific row in the table based on the given search criteria and data object.
    *
    * @param <K>            The type representing the table row.
    * @param tableElement   The table element where the action will be performed.
    * @param searchCriteria A list of values used to locate the row.
    * @param data           The object containing values to locate and interact with elements inside the row.
    * @return The fluent UI service instance for method chaining.
    */
   public final <K> T clickElementInCell(TableElement<?> tableElement, List<String> searchCriteria, K data) {
      Allure.step(UI_TABLE_CLICKING_ELEMENT_IN_CELL_FOR_SEARCH_CRITERIA + searchCriteria + USING_DATA + data);
      return insertCellValueAsData(tableElement, searchCriteria, data);
   }

   private <K> void validateArguments(TableField<K> field, Class<K> expectedClass) {
      try {
         K instance = expectedClass.getDeclaredConstructor().newInstance();
         field.invoke(instance, null);
      } catch (Exception e) {
         throw new IllegalArgumentException("The TableField objects should be from class: " + expectedClass);
      }
   }

   /**
    * Validates the table contents against the given assertions.
    *
    * @param tableElement The table element to validate.
    * @param assertions   The assertions to verify.
    * @return The fluent UI service instance.
    */
   @SuppressWarnings("java:S1854")
   public T validate(TableElement<?> tableElement, Assertion... assertions) {
      Allure.step("[UI - Table] Validating table element: " + tableElement + " with assertions: "
            + Arrays.toString(assertions));
      Object tableData = storage.sub(UI).get(tableElement.enumImpl(), Object.class);
      if (tableData == null) {
         throw new IllegalArgumentException("No table data found for key: " + tableElement.enumImpl());
      }

      var validationResults = tableService.validate(tableData, assertions);
      decoratorsFactory.decorate(uiServiceFluent, SuperUiServiceFluent.class)
            .validation(validationResults);

      Allure.step("[UI - Table] Validation completed for table element");
      return uiServiceFluent;
   }

}


