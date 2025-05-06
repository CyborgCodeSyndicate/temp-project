package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;

/**
 * Implementation of the {@link TableService} interface, providing concrete logic
 * for interacting with table components.
 *
 * <p>This service is responsible for reading tables and rows, inserting values,
 * filtering, sorting, and validating tables. It utilizes a registry to manage
 * table-related services dynamically.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class TableServiceImpl extends AbstractComponentService<TableComponentType, Table> implements TableService {

   private final TableServiceRegistry tableServiceRegistry;
   private final UiTableValidator uiTableValidator;

   /**
    * Constructs a {@code TableServiceImpl} with the specified dependencies.
    *
    * @param driver               The {@link SmartWebDriver} instance for browser interactions.
    * @param tableServiceRegistry The registry managing table services.
    * @param uiTableValidator     The validator for performing table-related assertions.
    */
   public TableServiceImpl(final SmartWebDriver driver, final TableServiceRegistry tableServiceRegistry,
                           UiTableValidator uiTableValidator) {
      super(driver);
      this.tableServiceRegistry = tableServiceRegistry;
      this.uiTableValidator = uiTableValidator;
   }

   /**
    * Creates a table component based on the specified component type.
    *
    * @param componentType The type of table component to create.
    * @return The created table component instance.
    */
   @Override
   protected Table createComponent(final TableComponentType componentType) {
      Table tableComponent = ComponentFactory.getTableComponent(componentType, driver);
      TableImpl table = (TableImpl) tableComponent;
      table.setServiceRegistry(tableServiceRegistry);
      return tableComponent;
   }

   /**
    * Reads the entire table and maps its contents to the specified class type.
    *
    * @param tableComponentType The type of the table component.
    * @param clazz              The class type representing the table rows.
    * @param <T>                The row type.
    * @return A list of objects representing the table rows.
    */
   @Override
   public final <T> List<T> readTable(final TableComponentType tableComponentType, final Class<T> clazz) {
      return getOrCreateComponent(tableComponentType).readTable(clazz);
   }

   /**
    * Reads the table with only the specified fields.
    *
    * @param tableComponentType The type of the table component.
    * @param clazz              The class type representing the table rows.
    * @param fields             The fields to extract from the table.
    * @param <T>                The row type.
    * @return A list of objects representing the table rows with the selected fields.
    */
   @Override
   @SafeVarargs
   public final <T> List<T> readTable(final TableComponentType tableComponentType, final Class<T> clazz,
                                      final TableField<T>... fields) {
      return getOrCreateComponent(tableComponentType).readTable(clazz, fields);
   }

   /**
    * Reads a range of rows from the table.
    *
    * @param tableComponentType The type of the table component.
    * @param start              The starting row index (inclusive, 1-based index).
    * @param end                The ending row index (exclusive, 1-based index).
    * @param clazz              The class type representing the table rows.
    * @param <T>                The row type.
    * @return A list of objects representing the selected rows.
    */
   @Override
   public final <T> List<T> readTable(final TableComponentType tableComponentType, final int start, final int end,
                                      final Class<T> clazz) {
      return getOrCreateComponent(tableComponentType).readTable(start, end, clazz);
   }

   /**
    * Reads a specific range of rows from the table with only the specified fields.
    *
    * @param tableComponentType The type of table component being used.
    * @param start              The starting row index (inclusive, 1-based index).
    * @param end                The ending row index (exclusive, 1-based index).
    * @param clazz              The class type representing the table rows.
    * @param fields             The fields to be extracted from the table. If not provided, all fields are retrieved.
    * @param <T>                The type of the row representation.
    * @return A list of objects representing the selected rows with the specified fields.
    */
   @Override
   @SafeVarargs
   public final <T> List<T> readTable(final TableComponentType tableComponentType, final int start, final int end,
                                      final Class<T> clazz, final TableField<T>... fields) {
      return getOrCreateComponent(tableComponentType).readTable(start, end, clazz, fields);
   }

   /**
    * Reads a single row from the table based on the given row index.
    *
    * <p>The method retrieves the data of the specified row and maps it to the provided class type.
    *
    * @param tableComponentType The type of table component being used.
    * @param row                The index of the row to be read (1-based index).
    * @param clazz              The class type representing the row structure.
    * @param <T>                The type of the row representation.
    * @return An object representing the row data.
    */
   @Override
   public final <T> T readRow(final TableComponentType tableComponentType, final int row, final Class<T> clazz) {
      return getOrCreateComponent(tableComponentType).readRow(row, clazz);
   }

   /**
    * Reads a single row from the table based on specific search criteria.
    *
    * <p>This method searches for a row matching the given criteria and maps it to the provided class type.
    *
    * @param tableComponentType The type of table component being used.
    * @param searchCriteria     A list of string values used to locate the row.
    * @param clazz              The class type representing the row structure.
    * @param <T>                The type of the row representation.
    * @return An object representing the matched row.
    */
   @Override
   public final <T> T readRow(final TableComponentType tableComponentType, final List<String> searchCriteria,
                              final Class<T> clazz) {
      return getOrCreateComponent(tableComponentType).readRow(searchCriteria, clazz);
   }

   /**
    * Reads a specific row from the table based on the row index.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The row index (1-based index).
    * @param clazz              The class type representing the row.
    * @param <T>                The row type.
    * @return The object representing the selected row.
    */
   @Override
   @SafeVarargs
   public final <T> T readRow(final TableComponentType tableComponentType, final int row, final Class<T> clazz,
                              final TableField<T>... fields) {
      return getOrCreateComponent(tableComponentType).readRow(row, clazz, fields);
   }

   /**
    * Reads a single row from the table based on specific search criteria, retrieving only the specified fields.
    *
    * @param tableComponentType The type of table component being used.
    * @param searchCriteria     A list of string values used to locate the row.
    * @param clazz              The class type representing the row structure.
    * @param fields             The specific fields to retrieve from the row. If not provided, all fields will be
    *                           retrieved.
    * @param <T>                The type of the row representation.
    * @return An object representing the matched row with the specified fields.
    */
   @Override
   @SafeVarargs
   public final <T> T readRow(final TableComponentType tableComponentType, final List<String> searchCriteria,
                              final Class<T> clazz, final TableField<T>... fields) {
      return getOrCreateComponent(tableComponentType).readRow(searchCriteria, clazz, fields);
   }

   /**
    * Inserts a value into a specific cell in a table row identified by its row index.
    *
    * @param tableComponentType The type of table component being used.
    * @param row                The index of the row where the value should be inserted (1-based index).
    * @param clazz             The class type representing the table row.
    * @param data               The data to be inserted into the row.
    * @param <T>                The type of the row representation.
    */
   @Override
   public final <T> void insertCellValue(final TableComponentType tableComponentType, final int row,
                                         final Class<T> clazz, final T data) {
      getOrCreateComponent(tableComponentType).insertCellValue(row, clazz, data);
   }

   /**
    * Inserts a value into a specific cell in the table.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The row index (1-based index).
    * @param clazz             The class type representing the row.
    * @param field              The field to update.
    * @param index              The cell index within the row (1-based index).
    * @param value              The value to insert.
    * @param <T>                The row type.
    */
   @Override
   public final <T> void insertCellValue(final TableComponentType tableComponentType, final int row,
                                         final Class<T> clazz, final TableField<T> field, final int index,
                                         final String... value) {
      getOrCreateComponent(tableComponentType).insertCellValue(row, clazz, field, index, value);
   }

   /**
    * Inserts a value into a specific cell of a table row identified by search criteria.
    *
    * @param tableComponentType The type of table component being used.
    * @param searchCriteria     A list of string values used to locate the row.
    * @param clazz             The class type representing the table row.
    * @param field              The field where the value should be inserted.
    * @param index              The index within the field where the value should be inserted (1-based index).
    * @param values             The values to be inserted into the specified cell.
    * @param <T>                The type of the row representation.
    */
   @Override
   public final <T> void insertCellValue(final TableComponentType tableComponentType,
                                         final List<String> searchCriteria, final Class<T> clazz,
                                         final TableField<T> field, final int index, final String... values) {
      getOrCreateComponent(tableComponentType).insertCellValue(searchCriteria, clazz, field, index, values);
   }

   /**
    * Inserts an entire data object into a table row identified by search criteria.
    *
    * @param tableComponentType The type of table component being used.
    * @param searchCriteria     A list of string values used to locate the row.
    * @param clazz             The class type representing the table row.
    * @param data               The data object containing values to be inserted into the row.
    * @param <T>                The type of the row representation.
    */
   @Override
   public final <T> void insertCellValue(final TableComponentType tableComponentType,
                                         final List<String> searchCriteria, final Class<T> clazz, final T data) {
      getOrCreateComponent(tableComponentType).insertCellValue(searchCriteria, clazz, data);
   }

   /**
    * Applies a filter to a table column based on a specified filtering strategy.
    *
    * @param tableComponentType The type of table component being used.
    * @param tclass             The class type representing the table structure.
    * @param column             The column field on which the filter should be applied.
    * @param filterStrategy     The filtering strategy to be used.
    * @param values             The values used for filtering the table.
    * @param <T>                The type of the table row representation.
    */
   @Override
   public final <T> void filterTable(final TableComponentType tableComponentType, final Class<T> tclass,
                                     final TableField<T> column, final FilterStrategy filterStrategy,
                                     final String... values) {
      getOrCreateComponent(tableComponentType).filterTable(tclass, column, filterStrategy, values);
   }

   /**
    * Sorts the table based on the specified column and sorting strategy.
    *
    * @param tableComponentType The type of the table component.
    * @param tclass             The class type representing the table rows.
    * @param column             The column to be sorted.
    * @param sortingStrategy    The sorting strategy to be applied.
    * @param <T>                The row type.
    */
   @Override
   public final <T> void sortTable(final TableComponentType tableComponentType, final Class<T> tclass,
                                   final TableField<T> column, final SortingStrategy sortingStrategy) {
      getOrCreateComponent(tableComponentType).sortTable(tclass, column, sortingStrategy);
   }

   /**
    * Validates a table against the specified assertions.
    *
    * @param table      The table object to validate.
    * @param assertions The assertions to apply for validation.
    * @param <T>        The row type.
    * @return A list of assertion results indicating the validation outcome.
    */
   @Override
   public <T> List<AssertionResult<T>> validate(final Object table, final Assertion... assertions) {
      if (table == null) {
         throw new IllegalArgumentException("Table cannot be null for validation.");
      }
      if (assertions == null || assertions.length == 0) {
         throw new IllegalArgumentException("At least one assertion must be provided.");
      }
      return uiTableValidator.validateTable(table, assertions);
   }

}
