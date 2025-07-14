package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Service interface defining operations for interacting with table components.
 *
 * <p>This interface provides methods for reading tables, retrieving rows, inserting values,
 * filtering, and sorting table data dynamically.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface TableService {

   /**
    * Default table component type used when none is specified.
    */
   TableComponentType DEFAULT_TYPE = getDefaultType();

   private static TableComponentType getDefaultType() {
      try {
         return ReflectionUtil.findEnumImplementationsOfInterface(TableComponentType.class,
               getUiConfig().tableDefaultType(),
               getUiConfig().projectPackage());
      } catch (ReflectionException e) {
         return ReflectionUtil.findEnumImplementationsOfInterface(TableComponentType.class,
               getUiConfig().tableDefaultType(),
               "com.theairebellion.zeus");
      }
   }

   /**
    * Reads all rows from the table.
    *
    * @param clazz The class type representing the table rows.
    * @param <T>   The type of the row representation.
    * @return A list of objects representing table rows.
    */
   default <T> List<T> readTable(Class<T> clazz) {
      return readTable(DEFAULT_TYPE, clazz);
   }

   /**
    * Reads all rows from the table using a specific table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param clazz              The class type representing the table rows.
    * @param <T>                The type of the row representation.
    * @return A list of objects representing table rows.
    */
   <T> List<T> readTable(TableComponentType tableComponentType, Class<T> clazz);

   /**
    * Reads all rows from the table with only the specified fields.
    *
    * @param clazz  The class type representing the table rows.
    * @param fields The fields to be extracted from the table.
    * @param <T>    The type of the row representation.
    * @return A list of objects representing table rows with selected fields.
    */
   default <T> List<T> readTable(Class<T> clazz, TableField<T>... fields) {
      return readTable(DEFAULT_TYPE, clazz, fields);
   }

   /**
    * Reads all rows from the table with only the specified fields, using a specific table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param clazz              The class type representing the table rows.
    * @param fields             The fields to be extracted from the table.
    * @param <T>                The type of the row representation.
    * @return A list of objects representing table rows with selected fields.
    */
   <T> List<T> readTable(TableComponentType tableComponentType, Class<T> clazz, TableField<T>... fields);

   /**
    * Reads a specific range of rows from the table.
    *
    * @param start The starting row index (inclusive, 1-based index).
    * @param end   The ending row index (exclusive, 1-based index).
    * @param clazz The class type representing the table rows.
    * @param <T>   The type of the row representation.
    * @return A list of objects representing the selected rows.
    */
   default <T> List<T> readTable(int start, int end, Class<T> clazz) {
      return readTable(DEFAULT_TYPE, start, end, clazz);
   }

   /**
    * Reads a specific range of rows from the table using a specific table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param start              The starting row index.
    * @param end                The ending row index.
    * @param clazz              The class type representing the table rows.
    * @param <T>                The type of the row representation.
    * @return A list of objects representing the selected rows.
    */
   <T> List<T> readTable(TableComponentType tableComponentType, int start, int end, Class<T> clazz);

   /**
    * Reads a specific range of rows from the table with only the specified fields.
    *
    * @param start  The starting row index (inclusive, 1-based index).
    * @param end    The ending row index (exclusive, 1-based index).
    * @param clazz  The class type representing the table rows.
    * @param fields The fields to be extracted from the table. If not provided, all fields are retrieved.
    * @param <T>    The type of the row representation.
    * @return A list of objects representing the selected rows with chosen fields.
    */
   default <T> List<T> readTable(int start, int end, Class<T> clazz,
                                 TableField<T>... fields) {
      return readTable(DEFAULT_TYPE, start, end, clazz, fields);
   }

   /**
    * Reads a specific range of rows from the table with only the specified fields using a specific table
    * component type.
    *
    * @param tableComponentType The type of the table component.
    * @param start              The starting row index (inclusive, 1-based index).
    * @param end                The ending row index (exclusive, 1-based index).
    * @param clazz              The class type representing the table rows.
    * @param fields             The fields to be extracted from the table. If not provided, all fields are retrieved.
    * @param <T>                The type of the row representation.
    * @return A list of objects representing the selected rows with chosen fields.
    */
   <T> List<T> readTable(TableComponentType tableComponentType, int start, int end, Class<T> clazz,
                         TableField<T>... fields);

   /**
    * Reads a specific row from the table by index.
    *
    * @param row   The row index (1-based index).
    * @param clazz The class type representing the table row.
    * @param <T>   The type of the row representation.
    * @return An object representing the selected row.
    */
   default <T> T readRow(int row, Class<T> clazz) {
      return readRow(DEFAULT_TYPE, row, clazz);
   }

   /**
    * Reads a specific row from the table by index using a specific table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The row index.
    * @param clazz              The class type representing the table row.
    * @param <T>                The type of the row representation.
    * @return An object representing the selected row.
    */
   <T> T readRow(TableComponentType tableComponentType, int row, Class<T> clazz);

   /**
    * Reads a single row from the table based on search criteria.
    *
    * <p>This method searches for a row that matches the given criteria and returns it as an object
    * of the specified class.
    *
    * @param searchCriteria A list of string values used to identify the row.
    * @param clazz          The class type representing the table row.
    * @param <T>            The type of the row representation.
    * @return An object representing the row matching the search criteria.
    */
   default <T> T readRow(List<String> searchCriteria, Class<T> clazz) {
      return readRow(DEFAULT_TYPE, searchCriteria, clazz);
   }

   /**
    * Reads a single row from the table based on search criteria for a specific table component type.
    *
    * <p>This method searches for a row that matches the given criteria within the specified table component
    * and returns it as an object of the provided class.
    *
    * @param tableComponentType The type of the table component.
    * @param searchCriteria     A list of string values used to identify the row.
    * @param clazz              The class type representing the table row.
    * @param <T>                The type of the row representation.
    * @return An object representing the row matching the search criteria.
    */
   <T> T readRow(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> clazz);

   /**
    * Reads a single row from the table using a 1-based index and retrieves only the specified fields.
    *
    * <p>This method extracts a row from the table using the given row index and retrieves
    * only the specified fields from the row.
    *
    * @param row    The 1-based index of the row to retrieve.
    * @param clazz  The class type representing the table row.
    * @param fields The fields to extract from the row. If not provided, all fields are retrieved.
    * @param <T>    The type of the row representation.
    * @return An object representing the row with the chosen fields.
    */
   default <T> T readRow(int row, Class<T> clazz, TableField<T>... fields) {
      return readRow(DEFAULT_TYPE, row, clazz, fields);
   }

   /**
    * Reads a single row from the table using a 1-based index with a specified table component type.
    *
    * <p>This method extracts a row from a table component using the given row index and retrieves
    * only the specified fields.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The 1-based index of the row to retrieve.
    * @param clazz              The class type representing the table row.
    * @param fields             The fields to extract from the row. If not provided, all fields are retrieved.
    * @param <T>                The type of the row representation.
    * @return An object representing the row with the chosen fields.
    */
   <T> T readRow(TableComponentType tableComponentType, int row, Class<T> clazz, TableField<T>... fields);

   /**
    * Reads a single row from the table based on search criteria and retrieves only the specified fields.
    *
    * <p>This method searches for a row that matches the given criteria and extracts only
    * the specified fields.
    *
    * @param searchCriteria A list of string values used to identify the row.
    * @param clazz          The class type representing the table row.
    * @param fields         The fields to extract from the row. If not provided, all fields are retrieved.
    * @param <T>            The type of the row representation.
    * @return An object representing the row matching the search criteria with chosen fields.
    */
   default <T> T readRow(List<String> searchCriteria, Class<T> clazz,
                         TableField<T>... fields) {
      return readRow(DEFAULT_TYPE, searchCriteria, clazz, fields);
   }

   /**
    * Reads a single row from the table based on search criteria using a specified table component type.
    *
    * <p>This method searches for a row that matches the given criteria within the specified table component
    * and extracts only the specified fields.
    *
    * @param tableComponentType The type of the table component.
    * @param searchCriteria     A list of string values used to identify the row.
    * @param clazz              The class type representing the table row.
    * @param fields             The fields to extract from the row. If not provided, all fields are retrieved.
    * @param <T>                The type of the row representation.
    * @return An object representing the row matching the search criteria with chosen fields.
    */
   <T> T readRow(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> clazz,
                 TableField<T>... fields);

   /**
    * Inserts values into a specific row in the table.
    *
    * <p>This method inserts values into the fields of a specified row in the table using the default table
    * component type.
    *
    * @param row       The 1-based index of the row where values should be inserted.
    * @param classType The class type representing the table row.
    * @param data      The object containing the values to insert.
    * @param <T>       The type of the row representation.
    */
   default <T> void insertCellValue(int row, Class<T> classType, T data) {
      insertCellValue(DEFAULT_TYPE, row, classType, data);
   }

   /**
    * Inserts values into a specific row in the table using a specified table component type.
    *
    * <p>This method inserts values into the fields of a specified row in the table for a given table component
    * type.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The 1-based index of the row where values should be inserted.
    * @param classType          The class type representing the table row.
    * @param data               The object containing the values to insert.
    * @param <T>                The type of the row representation.
    */
   <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> classType, T data);

   /**
    * Inserts values into a specific cell in the table.
    *
    * <p>This method inserts values into a particular field of a specified row.
    *
    * @param row       The 1-based index of the row where the values should be inserted.
    * @param classType The class type representing the table row.
    * @param field     The field in which values should be inserted.
    * @param values    The values to insert into the field.
    * @param <T>       The type of the row representation.
    */
   default <T> void insertCellValue(int row, Class<T> classType, TableField<T> field, String... values) {
      insertCellValue(DEFAULT_TYPE, row, classType, field, 1, values);
   }

   /**
    * Inserts values into a specific cell in the table using a specified table component type.
    *
    * <p>This method inserts values into a particular field of a specified row for a given table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The 1-based index of the row where the values should be inserted.
    * @param classType          The class type representing the table row.
    * @param field              The field in which values should be inserted.
    * @param values             The values to insert into the field.
    * @param <T>                The type of the row representation.
    */
   default <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> classType,
                                    TableField<T> field, String... values) {
      insertCellValue(tableComponentType, row, classType, field, 1, values);
   }

   /**
    * Inserts values into a specific indexed cell within a row in the table.
    *
    * <p>This method inserts values into a specified field of a row at a particular index position.
    *
    * @param row       The 1-based index of the row where the values should be inserted.
    * @param classType The class type representing the table row.
    * @param field     The field in which values should be inserted.
    * @param index     The 1-based index of the cell within the field.
    * @param value     The values to insert into the field at the given index.
    * @param <T>       The type of the row representation.
    */
   default <T> void insertCellValue(int row, Class<T> classType, TableField<T> field, int index, String... value) {
      insertCellValue(DEFAULT_TYPE, row, classType, field, index, value);
   }

   /**
    * Inserts values into a specific indexed cell within a row in the table using a specified table component type.
    *
    * <p>This method inserts values into a specified field of a row at a particular index position
    * for a given table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param row                The 1-based index of the row where the values should be inserted.
    * @param classType          The class type representing the table row.
    * @param field              The field in which values should be inserted.
    * @param index              The 1-based index of the cell within the field.
    * @param value              The values to insert into the field at the given index.
    * @param <T>                The type of the row representation.
    */
   <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> classType, TableField<T> field,
                            int index, String... value);

   /**
    * Inserts a value into a specific cell in the table based on search criteria.
    *
    * @param searchCriteria The criteria used to locate the row.
    * @param classType      The class type representing the table row.
    * @param field          The field representing the cell.
    * @param values         The values to be inserted into the cell.
    * @param <T>            The type of the row representation.
    */
   default <T> void insertCellValue(List<String> searchCriteria, Class<T> classType, TableField<T> field,
                                    String... values) {
      insertCellValue(DEFAULT_TYPE, searchCriteria, classType, field, 1, values);
   }

   /**
    * Inserts values into a specific cell in a row that matches the search criteria.
    *
    * <p>This method inserts values into a particular field of a row that matches the given search criteria
    * using a specified table component type. It defaults to inserting at the first available index.
    *
    * @param tableComponentType The type of the table component.
    * @param searchCriteria     A list of string values used to identify the row.
    * @param classType          The class type representing the table row.
    * @param field              The field in which values should be inserted.
    * @param values             The values to insert into the field.
    * @param <T>                The type of the row representation.
    */
   default <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria,
                                    Class<T> classType, TableField<T> field,
                                    String... values) {
      insertCellValue(tableComponentType, searchCriteria, classType, field, 1, values);
   }

   /**
    * Inserts values into a specific indexed cell within a row that matches the search criteria.
    *
    * <p>This method inserts values into a particular field of a row that matches the given search criteria
    * using the default table component type at a specified cell index.
    *
    * @param searchCriteria A list of string values used to identify the row.
    * @param classType      The class type representing the table row.
    * @param field          The field in which values should be inserted.
    * @param index          The 1-based index of the cell within the field.
    * @param values         The values to insert into the field at the given index.
    * @param <T>            The type of the row representation.
    */
   default <T> void insertCellValue(List<String> searchCriteria, Class<T> classType, TableField<T> field,
                                    int index, String... values) {
      insertCellValue(DEFAULT_TYPE, searchCriteria, classType, field, index, values);
   }

   /**
    * Inserts values into a specific indexed cell within a row that matches the search criteria using a specified
    * table component type.
    *
    * <p>This method inserts values into a particular field of a row that matches the given search criteria
    * at a specified cell index.
    *
    * @param tableComponentType The type of the table component.
    * @param searchCriteria     A list of string values used to identify the row.
    * @param classType          The class type representing the table row.
    * @param field              The field in which values should be inserted.
    * @param index              The 1-based index of the cell within the field.
    * @param values             The values to insert into the field at the given index.
    * @param <T>                The type of the row representation.
    */
   <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> classType,
                            TableField<T> field,
                            int index, String... values);

   /**
    * Inserts values into all applicable fields of a row that matches the search criteria.
    *
    * <p>This method inserts values into all fields of a row that matches the given search criteria
    * using the default table component type.
    *
    * @param searchCriteria A list of string values used to identify the row.
    * @param classType      The class type representing the table row.
    * @param data           The object containing the values to insert.
    * @param <T>            The type of the row representation.
    */
   default <T> void insertCellValue(List<String> searchCriteria, Class<T> classType, T data) {
      insertCellValue(DEFAULT_TYPE, searchCriteria, classType, data);
   }

   /**
    * Inserts values into all applicable fields of a row that matches the search criteria using a specified table
    * component type.
    *
    * <p>This method inserts values into all fields of a row that matches the given search criteria.
    *
    * @param tableComponentType The type of the table component.
    * @param searchCriteria     A list of string values used to identify the row.
    * @param classType          The class type representing the table row.
    * @param data               The object containing the values to insert.
    * @param <T>                The type of the row representation.
    */
   <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria,
                            Class<T> classType, T data);

   /**
    * Filters the table based on a specified field and filter strategy.
    *
    * @param tclass         The class type representing the table row.
    * @param column         The field representing the column to be filtered.
    * @param filterStrategy The strategy used for filtering (e.g., SELECT_ONLY, UNSELECT_ALL).
    * @param values         The values to filter.
    * @param <T>            The type of the row representation.
    */
   default <T> void filterTable(Class<T> tclass, TableField<T> column, FilterStrategy filterStrategy,
                                String... values) {
      filterTable(DEFAULT_TYPE, tclass, column, filterStrategy, values);
   }

   /**
    * Filters the table based on a specified field and filter strategy.
    *
    * <p>This method applies the provided {@link FilterStrategy} to the given {@link TableField}
    * for a table component of the specified type, including only rows that match one or more
    * of the supplied values.
    *
    * @param tableComponentType The type of the table component to filter.
    * @param tclass             The class type representing the table rows.
    * @param column             The field (column) on which to apply the filter.
    * @param filterStrategy     The filtering strategy to use (e.g., SELECT_ONLY, UNSELECT_ALL).
    * @param values             The values to filter by.
    * @param <T>                The type of the row representation.
    */
   <T> void filterTable(TableComponentType tableComponentType, Class<T> tclass, TableField<T> column,
                        FilterStrategy filterStrategy, String... values);

   /**
    * Sorts the table based on a specified field and sorting strategy.
    *
    * @param tclass          The class type representing the table row.
    * @param column          The field representing the column to be sorted.
    * @param sortingStrategy The sorting strategy (e.g., ascending or descending).
    * @param <T>             The type of the row representation.
    */
   default <T> void sortTable(Class<T> tclass, TableField<T> column, SortingStrategy sortingStrategy) {
      sortTable(DEFAULT_TYPE, tclass, column, sortingStrategy);
   }

   /**
    * Sorts the table based on the specified column and sorting strategy.
    *
    * <p>This method applies sorting to a table based on the given column and sorting strategy,
    * using the specified table component type.
    *
    * @param tableComponentType The type of the table component.
    * @param tclass             The class type representing the table rows.
    * @param column             The column to be sorted.
    * @param sortingStrategy    The sorting strategy to be applied (e.g., ascending or descending).
    * @param <T>                The type of the row representation.
    */
   <T> void sortTable(TableComponentType tableComponentType, Class<T> tclass, TableField<T> column,
                      SortingStrategy sortingStrategy);

   /**
    * Validates a table against a set of assertions.
    *
    * @param table      The table object to validate.
    * @param assertions The assertions to apply.
    * @param <T>        The type of the table rows.
    * @return A list of assertion results.
    */
   <T> List<AssertionResult<T>> validate(Object table, Assertion... assertions);

}
