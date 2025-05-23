package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.annotations.CellFilter;
import com.theairebellion.zeus.ui.components.table.annotations.CellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.CustomCellFilter;
import com.theairebellion.zeus.ui.components.table.annotations.CustomCellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.exceptions.TableException;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterComponent;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionComponent;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.model.CellLocator;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.components.table.model.TableLocators;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Base implementation of the {@link Table} interface, providing core functionalities
 * for reading, filtering, sorting, and inserting values into table elements in the UI.
 *
 * <p>This class defines an abstraction over table interactions and supports table-specific
 * operations such as row retrieval, cell value insertion, and custom filtering.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings("java:S3011")
public abstract class TableImpl extends BaseComponent implements Table {

   public static final String NO_LOCATOR_FOR_FIELD_EXCEPTION = "No locator found for the provided field.";
   private static final String READING_CLASS = "Reading entire table as class '%s'";
   private static final String READING_CLASS_WITH_FIELDS = "Reading table as class '%s' with fields %s";
   private static final String READING_RANGE = "Reading table rows from %d to %d as class '%s'";
   private static final String READING_RANGE_WITH_FIELDS =
         "Reading table rows from %d to %d as class '%s' with fields %s";
   private static final String READING_ROW = "Reading row number %d as class '%s'";
   private static final String READING_ROW_WITH_FIELDS = "Reading row number %d as class '%s' with fields %s";
   private static final String READING_ROW_CRITERIA = "Reading row matching criteria %s as class '%s'";
   private static final String READING_ROW_CRITERIA_WITH_FIELDS =
         "Reading row matching criteria %s as class '%s' with fields %s";
   private static final String INSERT_CELL_ROW_CRITERIA =
         "Inserting cell value in row matching criteria %s for class '%s', field '%s', cell index %d";
   private static final String INSERT_CELL_ROW =
         "Inserting cell value in row %d for class '%s', field '%s', cell index %d";
   private static final String INSERT_CELL_FIELD_CRITERIA =
         "Inserting cell value(s) into field '%s' for row matching criteria %s for class '%s'";
   private static final String INSERT_CELL_FIELD_ROW =
         "Inserting cell value(s) into field '%s' for row %d for class '%s'";
   private static final String INSERT_CELL_DATA_CRITERIA =
         "Inserting cell value(s) using data for row matching criteria %s for class '%s'";
   private static final String INSERT_CELL_DATA_ROW = "Inserting cell value(s) using data for row %d for class '%s'";
   private static final String FILTERING =
         "Filtering table for class '%s' on column '%s' using strategy '%s' with values %s";
   private static final String SORTING = "Sorting table for class '%s' on column '%s' using sorting strategy '%s'";
   private static final String INVALID_FIELD_TYPE = "Some fields are not TableCell or List<TableCell>.";
   private final List<Object> acceptedValues;
   @Setter
   protected TableServiceRegistry serviceRegistry;

   /**
    * Constructs a {@code TableImpl} instance with the specified {@link SmartWebDriver}.
    *
    * @param smartWebDriver The WebDriver instance for interacting with the table.
    */
   protected TableImpl(final SmartWebDriver smartWebDriver) {
      super(smartWebDriver);
      this.acceptedValues = List.of(
            new TableCell(""),
            List.of()
      );
   }

   /**
    * Constructs a {@code TableImpl} instance with the specified {@link SmartWebDriver}
    * and {@link TableServiceRegistry}.
    *
    * @param smartWebDriver  The WebDriver instance for interacting with the table.
    * @param serviceRegistry The service registry for managing table insertions and filters.
    */
   protected TableImpl(final SmartWebDriver smartWebDriver,
                       final TableServiceRegistry serviceRegistry) {
      super(smartWebDriver);
      this.serviceRegistry = serviceRegistry;
      this.acceptedValues = List.of(
            new TableCell(""),
            List.of()
      );
   }

   /**
    * Reads all rows from the table and maps them to objects of the specified class type.
    *
    * @param clazz The class type representing the table rows.
    * @param <T>   The type of the row representation.
    * @return A list of objects representing the table rows.
    */
   @Override
   public final <T> List<T> readTable(final Class<T> clazz) {
      LogUi.step(String.format(READING_CLASS, clazz.getSimpleName()));
      return readTableInternal(clazz, null, null, null);
   }

   /**
    * Reads the table with only the specified fields.
    *
    * @param clazz  The class type representing the table rows.
    * @param fields The fields to be extracted from the table.
    * @param <T>    The type of the row representation.
    * @return A list of objects representing the table rows with selected fields.
    */
   @Override
   @SafeVarargs
   public final <T> List<T> readTable(final Class<T> clazz, final TableField<T>... fields) {
      LogUi.step(String.format(READING_CLASS_WITH_FIELDS, clazz.getSimpleName(), Arrays.toString(fields)));
      return readTableInternal(clazz, (fields == null) ? null : List.of(fields), null, null);
   }

   /**
    * Reads a specific range of rows from the table.
    *
    * @param start The starting row index (inclusive).
    * @param end   The ending row index (exclusive).
    * @param clazz The class type representing the table rows.
    * @param <T>   The type of the row representation.
    * @return A list of objects representing the selected rows.
    */
   @Override
   public final <T> List<T> readTable(final int start, final int end, final Class<T> clazz) {
      LogUi.step(String.format(READING_RANGE, start, end, clazz.getSimpleName()));
      return readTableInternal(clazz, null, start, end);
   }

   /**
    * Reads a specific range of rows from the table with only the specified fields.
    *
    * <p>This method allows extracting a subset of rows from the table between the given indices,
    * and retrieves only the specified fields from each row.
    *
    * @param start  The starting row index (inclusive, 1-based index).
    * @param end    The ending row index (exclusive, 1-based index).
    * @param clazz  The class type representing the table rows.
    * @param fields The fields to be extracted from the table. If not provided, all fields are retrieved.
    * @param <T>    The type of the row representation.
    * @return A list of objects representing the selected rows with chosen fields.
    * @throws IndexOutOfBoundsException if the start or end indices are invalid.
    */
   @Override
   @SafeVarargs
   public final <T> List<T> readTable(final int start, final int end, final Class<T> clazz,
                                      final TableField<T>... fields) {
      LogUi.step(String.format(READING_RANGE_WITH_FIELDS, start, end, clazz.getSimpleName(), Arrays.toString(fields)));
      return readTableInternal(clazz, (fields == null) ? null : Arrays.asList(fields), start, end);
   }

   /**
    * Reads a single row from the table based on the row index.
    *
    * @param row   The row index (1-based index).
    * @param clazz The class type representing the table row.
    * @param <T>   The type of the row representation.
    * @return An object representing the row.
    */
   @Override
   public final <T> T readRow(final int row, final Class<T> clazz) {
      LogUi.step(String.format(READING_ROW, row, clazz.getSimpleName()));
      return readRowInternal(row - 1, clazz, null);
   }

   /**
    * Reads a row that matches the provided search criteria.
    *
    * @param searchCriteria A list of values to match in the row.
    * @param clazz          The class type representing the table row.
    * @param <T>            The type of the row representation.
    * @return The first matching row.
    */
   @Override
   public final <T> T readRow(final List<String> searchCriteria, final Class<T> clazz) {
      LogUi.step(String.format(READING_ROW_CRITERIA, searchCriteria, clazz.getSimpleName()));
      return readRowInternal(searchCriteria, clazz, null);
   }

   /**
    * Reads a specific row from the table, retrieving only the specified fields.
    *
    * <p>This method fetches a single row from the table based on its index (1-based),
    * and extracts only the specified fields if provided. If no fields are provided,
    * all fields of the row are retrieved.
    *
    * @param row    The row index to be read (1-based index).
    * @param clazz  The class type representing the table row structure.
    * @param fields The specific fields to be extracted from the row. If not provided, all fields are retrieved.
    * @param <T>    The type representing the table row.
    * @return An object representing the row with the selected fields.
    * @throws IndexOutOfBoundsException if the row index is out of range.
    */
   @Override
   @SafeVarargs
   public final <T> T readRow(final int row, final Class<T> clazz, final TableField<T>... fields) {
      LogUi.step(String.format(READING_ROW_WITH_FIELDS, row, clazz.getSimpleName(), Arrays.toString(fields)));
      return readRowInternal(row - 1, clazz, fields);
   }

   /**
    * Reads a row from the table that matches the provided search criteria, retrieving only specified fields.
    *
    * <p>This method searches for a row in the table that contains all the specified search criteria.
    * It then extracts only the specified fields if provided. If no fields are provided,
    * all fields of the row are retrieved.
    *
    * @param searchCriteria A list of strings representing the criteria that must be matched within the row.
    * @param clazz          The class type representing the table row structure.
    * @param fields         The specific fields to be extracted from the matched row.
    * @param <T>            The type representing the table row.
    * @return An object representing the matching row with the selected fields.
    * @throws NotFoundException if no row matches the search criteria.
    */
   @Override
   @SafeVarargs
   public final <T> T readRow(final List<String> searchCriteria, final Class<T> clazz, final TableField<T>... fields) {
      LogUi.step(String.format(READING_ROW_CRITERIA_WITH_FIELDS, searchCriteria, clazz.getSimpleName(),
            Arrays.toString(fields)));
      return readRowInternal(searchCriteria, clazz, fields);
   }

   /**
    * Inserts a value into a specific cell in the table row that matches the given search criteria.
    *
    * <p>This method locates a row in the table based on the provided search criteria and
    * inserts the specified values into a particular field in that row.
    *
    * @param searchCriteria A list of strings used to identify the target row.
    * @param rowClass       The class type representing the table row structure.
    * @param field          The specific field within the row where the value should be inserted.
    * @param cellIndex      The index of the cell (1-based) where the value should be inserted.
    * @param values         The values to be inserted into the specified cell.
    * @param <T>            The type representing the table row.
    * @throws NotFoundException         if no row matches the search criteria.
    * @throws IndexOutOfBoundsException if the cell index is out of range.
    */
   @Override
   public final <T> void insertCellValue(final List<String> searchCriteria, final Class<T> rowClass,
                                         final TableField<T> field, final int cellIndex, final String... values) {
      LogUi.step(String.format(INSERT_CELL_ROW_CRITERIA, searchCriteria, rowClass.getSimpleName(), field, cellIndex));
      insertCellValueInternal(searchCriteria, rowClass, field, cellIndex, values);
   }

   /**
    * Inserts a value into a specific cell within a row.
    *
    * @param row       The row index (1-based index).
    * @param rowClass  The class type representing the table row.
    * @param field     The table field to insert the value into.
    * @param cellIndex The index of the cell in the row (1-based index).
    * @param values    The values to be inserted.
    * @param <T>       The type of the row representation.
    */
   @Override
   public final <T> void insertCellValue(final int row, final Class<T> rowClass, final TableField<T> field,
                                         final int cellIndex, final String... values) {
      LogUi.step(String.format(INSERT_CELL_ROW, row, rowClass.getSimpleName(), field, cellIndex));
      insertCellValueInternal(row - 1, rowClass, field, cellIndex, values);
   }

   /**
    * Inserts values into multiple cells in the table row that matches the given search criteria.
    *
    * <p>This method locates a row in the table based on the provided search criteria and inserts values
    * into multiple fields of that row by iterating over the fields of the provided data object.
    *
    * @param searchCriteria A list of strings used to identify the target row.
    * @param clazz          The class type representing the table row structure.
    * @param data           The object containing the values to be inserted into the corresponding row fields.
    * @param <T>            The type representing the table row.
    * @throws NotFoundException if no row matches the search criteria.
    */
   @Override
   public final <T> void insertCellValue(final List<String> searchCriteria, final Class<T> clazz, final T data) {
      LogUi.step(String.format(INSERT_CELL_DATA_CRITERIA, searchCriteria, clazz.getSimpleName()));
      processInsertCellValue((fieldInvoker, strings) -> {
         if (strings.length == 1) {
            insertCellValue(searchCriteria, clazz, fieldInvoker, 1, strings);
         } else {
            for (int i = 0; i < strings.length; i++) {
               insertCellValue(searchCriteria, clazz, fieldInvoker, i + 1, strings[i]);
            }
         }
      }, clazz, data);
   }

   /**
    * Inserts values into a specific cell in the table row that matches the given search criteria.
    *
    * <p>This method finds a row in the table based on the provided search criteria and inserts values
    * into the specified field of that row.
    *
    * @param searchCriteria A list of strings used to identify the target row.
    * @param clazz          The class type representing the table row structure.
    * @param field          The specific field within the row where the values should be inserted.
    * @param values         The values to be inserted into the specified field.
    * @param <T>            The type representing the table row.
    * @throws NotFoundException if no row matches the search criteria.
    */
   @Override
   public final <T> void insertCellValue(final List<String> searchCriteria, final Class<T> clazz,
                                         final TableField<T> field, final String... values) {
      LogUi.step(String.format(INSERT_CELL_FIELD_CRITERIA, field, searchCriteria, clazz.getSimpleName()));
      Table.super.insertCellValue(searchCriteria, clazz, field, values);
   }

   /**
    * Inserts values into a specific cell in the table row at the given row index.
    *
    * <p>This method finds the row in the table based on the provided row index (1-based)
    * and inserts values into the specified field of that row.
    *
    * @param row    The row index where the values should be inserted (1-based index).
    * @param clazz  The class type representing the table row structure.
    * @param field  The specific field within the row where the values should be inserted.
    * @param values The values to be inserted into the specified field.
    * @param <T>    The type representing the table row.
    * @throws IndexOutOfBoundsException if the row index is out of range.
    */
   @Override
   public final <T> void insertCellValue(final int row, final Class<T> clazz, final TableField<T> field,
                                         final String... values) {
      LogUi.step(String.format(INSERT_CELL_FIELD_ROW, field, row, clazz.getSimpleName()));
      Table.super.insertCellValue(row, clazz, field, values);
   }

   /**
    * Inserts values into multiple cells in the table row at the given row index.
    *
    * <p>This method finds the row in the table based on the provided row index (1-based)
    * and inserts values into multiple fields of that row by iterating over the fields of the provided data object.
    *
    * @param row   The row index where the values should be inserted (1-based index).
    * @param clazz The class type representing the table row structure.
    * @param data  The object containing the values to be inserted into the corresponding row fields.
    * @param <T>   The type representing the table row.
    * @throws IndexOutOfBoundsException if the row index is out of range.
    */
   @Override
   public final <T> void insertCellValue(final int row, final Class<T> clazz, final T data) {
      LogUi.step(String.format(INSERT_CELL_DATA_ROW, row, clazz.getSimpleName()));
      processInsertCellValue((fieldInvoker, strings) -> {
         if (strings.length == 1) {
            insertCellValue(row, clazz, fieldInvoker, 1, strings);
         } else {
            for (int i = 0; i < strings.length; i++) {
               insertCellValue(row, clazz, fieldInvoker, i + 1, strings[i]);
            }
         }
      }, clazz, data);
   }

   /**
    * Filters a table column based on a specified filtering strategy.
    *
    * @param tclass         The class type representing the table row.
    * @param column         The table field representing the column to be filtered.
    * @param filterStrategy The filtering strategy to be applied.
    * @param values         The values used for filtering.
    * @param <T>            The type of the row representation.
    */
   @Override
   public final <T> void filterTable(final Class<T> tclass, final TableField<T> column,
                                     final FilterStrategy filterStrategy, final String... values) {
      LogUi.step(String.format(FILTERING, tclass.getSimpleName(), column, filterStrategy, Arrays.toString(values)));
      final Map<String, List<CellLocator>> tableSectionLocatorsMap =
            getTableSectionLocatorsMap(tclass, List.of(column));

      final Map.Entry<String, List<CellLocator>> firstEntry = tableSectionLocatorsMap.entrySet().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(NO_LOCATOR_FOR_FIELD_EXCEPTION));

      final String tableSection = firstEntry.getKey();
      final CellLocator cellLocator = firstEntry.getValue().get(0);

      TableLocators tableLocators = getTableLocators(tclass);
      SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());
      SmartWebElement headerRow = getHeaderRow(tableContainer, tableLocators.getHeaderRowLocator(), tableSection);
      filterCells(cellLocator, headerRow, filterStrategy, values);
   }

   /**
    * Sorts a table column based on a specified sorting strategy.
    *
    * @param tclass          The class type representing the table row.
    * @param column          The table field representing the column to be sorted.
    * @param sortingStrategy The sorting strategy to be applied.
    * @param <T>             The type of the row representation.
    */
   @Override
   public final <T> void sortTable(final Class<T> tclass, final TableField<T> column,
                                   final SortingStrategy sortingStrategy) {
      LogUi.step(String.format(SORTING, tclass.getSimpleName(), column, sortingStrategy));

      final Map<String, List<CellLocator>> tableSectionLocatorsMap =
            getTableSectionLocatorsMap(tclass, List.of(column));

      final Map.Entry<String, List<CellLocator>> firstEntry = tableSectionLocatorsMap.entrySet().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(NO_LOCATOR_FOR_FIELD_EXCEPTION));

      final String tableSection = firstEntry.getKey();
      final CellLocator cellLocator = firstEntry.getValue().get(0);

      TableLocators tableLocators = getTableLocators(tclass);
      SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());
      SmartWebElement headerRow = getHeaderRow(tableContainer, tableLocators.getHeaderRowLocator(), tableSection);
      sortTable(headerRow.findSmartElement(cellLocator.getHeaderCellLocator()), sortingStrategy);
   }

   /**
    * Sorts the table based on the given header cell and sorting strategy.
    *
    * <p>This method provides an extension point for subclasses to define how sorting is applied to a table.
    * It can be overridden to implement specific sorting logic.
    *
    * @param headerCell      The SmartWebElement representing the header cell to be clicked for sorting.
    * @param sortingStrategy The {@link SortingStrategy} defining the sorting direction (e.g., ascending or descending).
    */
   protected void sortTable(SmartWebElement headerCell, SortingStrategy sortingStrategy) {

   }

   /**
    * Retrieves the table container element using the specified locator.
    *
    * <p>This method is protected to allow subclasses to modify or override the way the table container is located.
    * It ensures that the SmartWebElement representing the table container is retrieved correctly.
    *
    * @param tableContainerLocator The locator used to find the table container.
    * @return A {@link SmartWebElement} representing the table container.
    */
   protected SmartWebElement getTableContainer(By tableContainerLocator) {
      return driver.findSmartElement(tableContainerLocator);
   }

   /**
    * Retrieves all rows from the specified table container.
    *
    * <p>This method waits until all rows are visible before retrieving them. It can be overridden by subclasses
    * to customize how table rows are retrieved.
    *
    * @param tableContainer   The SmartWebElement representing the table container.
    * @param tableRowsLocator The locator used to find the table rows.
    * @param section          The table section from which rows should be retrieved (optional).
    * @return A list of {@link SmartWebElement} representing the table rows.
    */
   protected List<SmartWebElement> getRows(SmartWebElement tableContainer, By tableRowsLocator, String section) {
      driver.getWait()
            .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(tableContainer, tableRowsLocator));
      return tableContainer.findSmartElements(tableRowsLocator);
   }

   /**
    * Retrieves the header row of a table.
    *
    * <p>This method locates and returns the header row within the given table container. It can be
    * overridden by subclasses to implement custom header retrieval logic.
    *
    * @param tableContainer   The SmartWebElement representing the table container.
    * @param headerRowLocator The locator used to find the header row.
    * @param tableSection     The section of the table where the header row is located (optional).
    * @return A {@link SmartWebElement} representing the header row.
    */
   protected SmartWebElement getHeaderRow(SmartWebElement tableContainer, By headerRowLocator, String tableSection) {
      return tableContainer.findSmartElement(headerRowLocator);
   }


   private <T> List<T> readTableInternal(final Class<T> rowClass,
                                         final List<TableField<T>> fields,
                                         final Integer start,
                                         final Integer end) {
      TableLocators tableLocators = getTableLocators(rowClass);
      SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());

      final Map<String, List<CellLocator>> tableSectionLocatorsMap =
            getTableSectionLocatorsMap(rowClass, fields);

      final Map<String, List<SmartWebElement>> rowsMap =
            tableSectionLocatorsMap.keySet().stream()
                  .collect(Collectors.toMap(
                        Function.identity(),
                        section -> readRowsInRange(tableContainer, tableLocators.getTableRowsLocator(), section,
                              start, end)
                  ));

      return mergeRowsAcrossSections(rowsMap, tableSectionLocatorsMap, rowClass);
   }


   private List<SmartWebElement> readRowsInRange(SmartWebElement tableContainer, By tableRowsLocator,
                                                 final String tableSection,
                                                 final Integer start,
                                                 final Integer end) {
      final List<SmartWebElement> allRows = getRows(tableContainer, tableRowsLocator, tableSection);

      if (start != null && end != null) {
         final int fromIndex = Math.max(0, start - 1);
         final int toIndex = Math.min(allRows.size(), end);
         if (fromIndex >= toIndex) {
            LogUi.warn("Requested start/end range is invalid: {}-{}. Returning empty list.", start, end);
            return Collections.emptyList();
         }
         return allRows.subList(fromIndex, toIndex);
      }
      return allRows;
   }


   private <T> List<T> mergeRowsAcrossSections(final Map<String, List<SmartWebElement>> rowsPerSection,
                                               final Map<String, List<CellLocator>> locatorsMap,
                                               final Class<T> rowClass) {
      final List<T> results = new ArrayList<>();

      rowsPerSection.values().stream().findFirst().ifPresent(rows -> {
         for (int i = 0; i < rows.size(); i++) {
            T mergedRow = null;
            for (Map.Entry<String, List<SmartWebElement>> entry : rowsPerSection.entrySet()) {
               final String section = entry.getKey();
               final SmartWebElement rowElement = entry.getValue().get(i);

               final T partialRow = readSingleRow(rowClass, locatorsMap.get(section), rowElement);
               mergedRow = mergeObjects(mergedRow, partialRow);
            }
            results.add(mergedRow);
         }
      });
      return results;
   }


   private <T> T readRowInternal(final Object rowIdentifier,
                                 final Class<T> rowClass,
                                 final TableField<T>[] fields) {
      TableLocators tableLocators = getTableLocators(rowClass);
      SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());
      final Map<String, List<CellLocator>> locatorsMap =
            getTableSectionLocatorsMap(rowClass, (fields == null) ? null : Arrays.asList(fields));

      final Map<String, SmartWebElement> rowElementMap = locatorsMap.keySet()
            .stream()
            .collect(Collectors.toMap(
                  Function.identity(),
                  section -> findRowElement(tableContainer,
                        tableLocators.getTableRowsLocator(),
                        rowIdentifier, section)
            ));

      T mergedRow = null;
      for (Map.Entry<String, SmartWebElement> entry : rowElementMap.entrySet()) {
         final T partialRow = readSingleRow(rowClass, locatorsMap.get(entry.getKey()), entry.getValue());
         mergedRow = mergeObjects(mergedRow, partialRow);
      }

      return mergedRow;
   }


   private SmartWebElement findRowElement(SmartWebElement tableContainer, By tableRowsLocator,
                                          final Object rowIdentifier,
                                          final String section) {
      final List<SmartWebElement> rows = getRows(tableContainer, tableRowsLocator, section);

      if (rowIdentifier instanceof Integer rowIndex) {
         if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException(String.format(
                  "Requested row index %d is out of valid range [1..%d]", rowIndex + 1, rows.size()
            ));
         }
         return rows.get(rowIndex);
      } else if (rowIdentifier instanceof List<?> criteria) {
         return findRowByCriteria(criteria, rows);
      } else {
         throw new IllegalArgumentException("Unsupported row identifier type: " + rowIdentifier);
      }
   }


   private SmartWebElement findRowByCriteria(final List<?> searchCriteria, final List<SmartWebElement> rows) {
      return rows.stream()
            .filter(row -> searchCriteria.stream().allMatch(
                  criterion -> Optional.ofNullable(row.getAttribute("innerText"))
                        .orElse("")
                        .contains(String.valueOf(criterion))
            ))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(
                  "No row found containing all criteria: " + searchCriteria
            ));
   }


   private <T> T readSingleRow(final Class<T> rowClass,
                               final List<CellLocator> cellLocators,
                               final SmartWebElement rowElement) {
      final T rowInstance = createInstance(rowClass);
      for (CellLocator cellLocator : cellLocators) {
         populateFieldValue(rowInstance, rowElement, cellLocator);
      }
      return rowInstance;
   }


   private <T> void populateFieldValue(final T rowInstance,
                                       final SmartWebElement rowElement,
                                       final CellLocator cellLocator) {
      final By locator = cellLocator.getLocator();
      final By textLocator = cellLocator.getCellTextLocator();
      final String fieldName = cellLocator.getFieldName();
      final boolean isCollection = cellLocator.isCollection();

      if (!isCollection) {
         final TableCell singleCell = buildTableCell(rowElement, locator, textLocator);
         invokeSetter(rowInstance, fieldName, singleCell);
      } else {
         final List<SmartWebElement> cellElements = rowElement.findSmartElements(locator);
         final List<TableCell> tableCells = cellElements.stream()
               .map(elem -> buildTableCell(elem, null, textLocator))
               .toList();
         invokeSetter(rowInstance, fieldName, tableCells);
      }
   }


   private TableCell buildTableCell(final SmartWebElement container,
                                    final By cellLocator,
                                    final By textLocator) {
      final SmartWebElement cellElement = (cellLocator == null)
            ? container
            : container.findSmartElement(cellLocator);

      final String text = cellElement.findSmartElement(textLocator).getText();
      return new TableCell(cellElement, text);
   }


   private <T> T createInstance(final Class<T> clazz) {
      try {
         return clazz.getDeclaredConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
         final String message = "Could not create a new instance of class: " + clazz.getName();
         LogUi.error(message, e);
         throw new IllegalStateException(message, e);
      }
   }


   private <T> T mergeObjects(final T t1, final T t2) {
      if (t1 == null) {
         return t2;
      }
      if (t2 == null) {
         return t1;
      }

      final Field[] fields = t1.getClass().getDeclaredFields();
      for (Field field : fields) {
         field.setAccessible(true);
         try {
            final Object value1 = field.get(t1);
            if (value1 == null) {
               final Object value2 = field.get(t2);
               if (value2 != null) {
                  field.set(t1, value2);
               }
            }
         } catch (IllegalAccessException e) {
            final String message = "Cannot access field: " + field.getName();
            LogUi.error(message, e);
         }
      }
      return t1;
   }


   private void invokeSetter(final Object targetObject, final String fieldName, final Object value) {
      if (targetObject == null || fieldName == null || value == null) {
         return;
      }

      Class<?> paramType = value.getClass();
      if (List.class.isAssignableFrom(paramType)) {
         paramType = List.class; // For generics consistency
      }

      final String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

      try {
         final Method setter = targetObject.getClass().getDeclaredMethod(setterName, paramType);
         setter.invoke(targetObject, value);
      } catch (NoSuchMethodException e) {
         final String message = String.format(
               "Setter not found: %s(%s) in class %s",
               setterName,
               paramType.getSimpleName(),
               targetObject.getClass().getName()
         );
         LogUi.error(message, e);
      } catch (IllegalAccessException | InvocationTargetException e) {
         final String message = String.format(
               "Failed to invoke setter: %s on %s",
               setterName,
               targetObject.getClass().getName()
         );
         LogUi.error(message, e);
      }
   }


   private <T> Map<String, List<CellLocator>> getTableSectionLocatorsMap(final Class<T> rowClass,
                                                                         final List<TableField<T>> fields) {
      final List<CellLocator> cellLocators;
      if (fields == null || fields.isEmpty()) {
         cellLocators = extractAnnotatedFields(rowClass, Collections.emptyList());
      } else {
         cellLocators = extractAnnotatedFields(rowClass, fields);
      }

      return cellLocators.stream()
            .collect(Collectors.groupingBy(CellLocator::getTableSection));
   }


   private <T> List<CellLocator> extractAnnotatedFields(final Class<T> clazz,
                                                        final List<TableField<T>> fields) {
      final T rowInstance = createInstance(clazz);

      if (!fields.isEmpty()) {
         validateFieldInvokers(rowInstance, fields);
      }

      final Field[] declaredFields = clazz.getDeclaredFields();
      final List<Field> validFields;
      if (fields.isEmpty()) {
         validFields = Arrays.stream(declaredFields)
               .filter(f -> f.isAnnotationPresent(TableCellLocator.class))
               .toList();
      } else {
         validFields = Arrays.stream(declaredFields)
               .filter(f -> {
                  boolean hasValue = false;
                  try {
                     f.setAccessible(true);
                     hasValue = (f.get(rowInstance) != null);
                     if (hasValue && !f.isAnnotationPresent(TableCellLocator.class)) {
                        throw new IllegalArgumentException(
                              "Field " + f.getName()
                                    + " is missing a @TableCellLocator annotation."
                        );
                     }
                  } catch (IllegalAccessException ex) {
                     LogUi.error("Cannot access field: {}", f.getName(), ex);
                  }
                  return hasValue;
               })
               .toList();
      }

      final boolean validSyntax = validFields.stream()
            .allMatch(
                  f -> isListOfTableCell(f) || TableCell.class.isAssignableFrom(f.getType()));
      if (!validSyntax) {
         LogUi.error(INVALID_FIELD_TYPE);
         throw new TableException("Invalid field type for table cell usage.");
      }

      return validFields.stream()
            .map(this::mapToCellLocator)
            .toList();
   }


   private boolean isListOfTableCell(final Field field) {
      if (!List.class.isAssignableFrom(field.getType())) {
         return false;
      }
      final Type genericType = field.getGenericType();
      if (genericType instanceof ParameterizedType parameterizedType) {
         final Type[] typeArguments = parameterizedType.getActualTypeArguments();
         return (typeArguments.length == 1) && (typeArguments[0] == TableCell.class);
      }
      return false;
   }


   private CellLocator mapToCellLocator(final Field field) {
      final TableCellLocator annotation = field.getAnnotation(TableCellLocator.class);
      final FindBy.FindByBuilder builder = new FindBy.FindByBuilder();

      final By cellBy = builder.buildIt(annotation.cellLocator(), null);
      final By cellTextBy = builder.buildIt(annotation.cellTextLocator(), null);
      final By cellHeaderBy = builder.buildIt(annotation.headerCellLocator(), null);

      final CellInsertionComponent cellInsertionComponent = Optional.ofNullable(
                  field.getAnnotation(CellInsertion.class)
            ).map(ci -> new CellInsertionComponent(ci.type(), ci.componentType(), ci.order()))
            .orElse(null);

      final Class<? extends CellInsertionFunction> customCellInsertion = Optional.ofNullable(
            field.getAnnotation(CustomCellInsertion.class)
      ).map(CustomCellInsertion::insertionFunction).orElse(null);


      final CellFilterComponent cellFilterComponent = Optional.ofNullable(
                  field.getAnnotation(CellFilter.class)
            ).map(ci -> new CellFilterComponent(ci.type(), ci.componentType()))
            .orElse(null);

      final Class<? extends CellFilterFunction> customCellFilter = Optional.ofNullable(
            field.getAnnotation(CustomCellFilter.class)
      ).map(CustomCellFilter::cellFilterFunction).orElse(null);

      final boolean isCollection = Collection.class.isAssignableFrom(field.getType());

      return new CellLocator(
            field.getName(),
            cellBy,
            cellTextBy,
            cellHeaderBy,
            isCollection,
            annotation.tableSection(),
            cellInsertionComponent,
            customCellInsertion,
            cellFilterComponent,
            customCellFilter
      );
   }


   private <T> void validateFieldInvokers(final T instance, final List<TableField<T>> fields) {
      for (TableField<T> setter : fields) {
         boolean success = false;
         for (Object testValue : acceptedValues) {
            try {
               setter.invoke(instance, testValue);
               success = true;
               break;
            } catch (InvocationTargetException | IllegalAccessException ex) {
               // Try next accepted value
            }
         }
         if (!success) {
            final String msg = "No accepted value could be applied via FieldInvoker. "
                  + "Possible illegal field or setter in " + instance.getClass().getName();
            LogUi.error(msg);
            throw new IllegalArgumentException(msg);
         }
      }
   }


   private <T> void insertCellValueInternal(final Object rowIdentifier,
                                            final Class<T> rowClass,
                                            final TableField<T> field,
                                            final int cellIndex,
                                            final String... values) {
      TableLocators tableLocators = getTableLocators(rowClass);
      SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());
      final Map<String, List<CellLocator>> tableSectionLocatorsMap =
            getTableSectionLocatorsMap(rowClass, List.of(field));
      final Map.Entry<String, List<CellLocator>> firstEntry = tableSectionLocatorsMap.entrySet().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                  NO_LOCATOR_FOR_FIELD_EXCEPTION));

      final String tableSection = firstEntry.getKey();
      final CellLocator cellLocator = firstEntry.getValue().get(0);

      final SmartWebElement rowElement;
      if (rowIdentifier instanceof List<?> criteria) {
         rowElement = findRowByCriteria(criteria,
               getRows(tableContainer, tableLocators.getTableRowsLocator(), tableSection));
      } else if (rowIdentifier instanceof Integer rowIndex) {
         final List<SmartWebElement> rows = getRows(tableContainer, tableLocators.getTableRowsLocator(),
               tableSection);
         if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException(String.format(
                  "Requested row index %d is out of valid range [1..%d]", rowIndex + 1, rows.size()
            ));
         }
         rowElement = rows.get(rowIndex);
      } else {
         throw new IllegalArgumentException("Unsupported row identifier type: " + rowIdentifier);
      }

      insertInCell(cellLocator, rowElement, values, cellIndex);
   }


   private void insertInCell(final CellLocator cellLocator,
                             final SmartWebElement rowElement,
                             final String[] values,
                             final int cellIndex) {
      final CellInsertionComponent component = cellLocator.getCellInsertionComponent();
      final Class<? extends CellInsertionFunction> customFunction = cellLocator.getCustomCellInsertion();

      if (component == null && customFunction == null) {
         throw new TableException(
               "No table cell insertion method provided for field: " + cellLocator.getFieldName()
         );
      }

      final List<SmartWebElement> cells = rowElement.findSmartElements(cellLocator.getLocator());
      if (cells.isEmpty() || cellIndex <= 0 || cellIndex > cells.size()) {
         throw new TableException(String.format(
               "Invalid cell index: %d for locator: %s", cellIndex, cellLocator.getLocator()
         ));
      }

      final SmartWebElement targetCell = cells.get(cellIndex - 1);

      if (component != null) {
         insertUsingComponent(component, targetCell, values);
      } else {
         insertUsingCustomFunction(customFunction, targetCell, values);
      }
   }


   private void insertUsingComponent(final CellInsertionComponent component,
                                     final SmartWebElement targetCell,
                                     final String[] values) {
      if (serviceRegistry == null) {
         throw new IllegalStateException(
               "Your instance of table is not having registered services. You can't use CellInsertion annotation.");
      }
      try {
         final Class<? extends ComponentType> type = component.getType();

         Enum<?> componentInstance =
               (Enum<?>) ReflectionUtil.findEnumImplementationsOfInterface(type, component.getComponentType(),
                     getUiConfig().projectPackage());
         final TableInsertion service = serviceRegistry.getTableService(type);
         service.tableInsertion(targetCell, (ComponentType) componentInstance, values);
      } catch (Exception e) {
         throw new TableException(
               "Failed to insert using component: " + component.getComponentType(), e
         );
      }
   }


   private void insertUsingCustomFunction(final Class<? extends CellInsertionFunction> customFunction,
                                          final SmartWebElement targetCell,
                                          final String[] values) {
      try {
         Constructor<? extends CellInsertionFunction> constructor = customFunction.getDeclaredConstructor();
         constructor.setAccessible(true);
         final CellInsertionFunction functionInstance = constructor.newInstance();
         functionInstance.accept(targetCell, values);
      } catch (ReflectiveOperationException e) {
         throw new TableException(
               "Failed to instantiate custom cell insertion function: " + customFunction.getName(), e
         );
      }
   }


   private void filterCells(final CellLocator cellLocator,
                            final SmartWebElement headerRowElement,
                            final FilterStrategy filterStrategy,
                            final String[] values) {
      final CellFilterComponent component = cellLocator.getCellFilterComponent();
      final Class<? extends CellFilterFunction> customFunction = cellLocator.getCustomCellFilter();

      if (component == null && customFunction == null) {
         throw new TableException(
               "No table cell insertion method provided for field: " + cellLocator.getFieldName()
         );
      }

      final SmartWebElement targetCell = headerRowElement.findSmartElement(cellLocator.getHeaderCellLocator());


      if (component != null) {
         filterCellsUsingComponent(component, targetCell, filterStrategy, values);
      } else {
         filterCellsUsingCustomFunction(customFunction, targetCell, filterStrategy, values);
      }
   }


   private void filterCellsUsingComponent(final CellFilterComponent component,
                                          final SmartWebElement targetCell, FilterStrategy filterStrategy,
                                          final String[] values) {
      if (serviceRegistry == null) {
         throw new IllegalStateException(
               "Your instance of table is not having registered services. You can't use CellFilter annotation.");
      }
      try {
         final Class<? extends ComponentType> type = component.getType();
         final Enum<?> componentInstance =
               (Enum<?>) ReflectionUtil.findEnumImplementationsOfInterface(type, component.getComponentType(),
                     getUiConfig().projectPackage());
         final TableFilter service = serviceRegistry.getFilterService(type);
         service.tableFilter(targetCell, (ComponentType) componentInstance, filterStrategy, values);
      } catch (Exception e) {
         throw new TableException(
               "Failed to filter using component: " + component.getComponentType(), e
         );
      }
   }


   private void filterCellsUsingCustomFunction(final Class<? extends CellFilterFunction> customFunction,
                                               final SmartWebElement targetCell, FilterStrategy filterStrategy,
                                               final String[] values) {
      try {
         final CellFilterFunction functionInstance = customFunction.getDeclaredConstructor().newInstance();
         functionInstance.accept(targetCell, filterStrategy, values);
      } catch (ReflectiveOperationException e) {
         throw new TableException(
               "Failed to instantiate custom cell filter function: " + customFunction.getName(), e
         );
      }
   }


   private <T> void processInsertCellValue(BiConsumer<TableField<T>, String[]> action, Class<T> classType, T data) {
      Map<TableField<T>, String[]> invokersMap = prepareFieldInvokersMap(classType, data);
      invokersMap.forEach(action);
   }


   private <T> Map<TableField<T>, String[]> prepareFieldInvokersMap(Class<T> classType, T data) {
      return Arrays.stream(classType.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(CellInsertion.class) || field.isAnnotationPresent(
                  CustomCellInsertion.class))
            .filter(field -> isListOfTableCell(field) || TableCell.class.isAssignableFrom(field.getType()))
            .map(field -> {
               int order = field.isAnnotationPresent(CellInsertion.class)
                     ? field.getAnnotation(CellInsertion.class).order()
                     : field.getAnnotation(CustomCellInsertion.class).order();

               TableField<T> fieldInvoker = (instance, value) -> {
                  field.setAccessible(true);
                  field.set(instance, value);
               };

               String[] stringValues = convertFieldValueToStrings(field, data);

               return new OrderedFieldInvokerAndValues<>(fieldInvoker, order, stringValues);
            })
            .sorted(Comparator.comparingInt(OrderedFieldInvokerAndValues::order))
            .collect(Collectors.toMap(
                  OrderedFieldInvokerAndValues::fieldInvoker,
                  OrderedFieldInvokerAndValues::stringValues,
                  (v1, v2) -> v1,
                  LinkedHashMap::new
            ));
   }


   private String[] convertFieldValueToStrings(Field field, Object data) {
      field.setAccessible(true);
      try {
         Object value = field.get(data);
         if (value instanceof TableCell cell) {
            return cell.getText() == null ? new String[0] : new String[] {cell.getText()};
         } else if (value instanceof List<?> list) {
            return list.stream()
                  .filter(TableCell.class::isInstance)
                  .map(TableCell.class::cast)
                  .map(TableCell::getText)
                  .filter(Objects::nonNull)
                  .toArray(String[]::new);
         }
      } catch (IllegalAccessException e) {
         throw new TableException("Unable to access field: " + field.getName(), e);
      }
      return new String[0];
   }

   private TableLocators getTableLocators(Class<?> clazz) {
      TableInfo annotation = clazz.getAnnotation(TableInfo.class);
      if (annotation == null) {
         throw new IllegalArgumentException(
               "Your class: " + clazz.getSimpleName() + "is missing @TableInfo annotation for the table container");
      }
      final FindBy.FindByBuilder builder = new FindBy.FindByBuilder();
      By tableContainerLocator = builder.buildIt(annotation.tableContainerLocator(), null);
      By tableRowsLocator = builder.buildIt(annotation.rowsLocator(), null);
      By headerRowLocator = builder.buildIt(annotation.headerRowLocator(), null);
      return new TableLocators(tableContainerLocator, tableRowsLocator, headerRowLocator);
   }

   private record OrderedFieldInvokerAndValues<T>(
         TableField<T> fieldInvoker,
         int order,
         String[] stringValues
   ) {
      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         }
         if (!(o instanceof OrderedFieldInvokerAndValues<?> that)) {
            return false;
         }
         return order == that.order
               && Objects.equals(fieldInvoker, that.fieldInvoker)
               && Arrays.equals(stringValues, that.stringValues);
      }

      @Override
      public int hashCode() {
         int result = Objects.hash(fieldInvoker, order);
         result = 31 * result + Arrays.hashCode(stringValues);
         return result;
      }

      @Override
      public String toString() {
         return "OrderedFieldInvokerAndValues["
               + "fieldInvoker=" + fieldInvoker
               + ", order=" + order
               + ", stringValues=" + Arrays.toString(stringValues)
               + ']';
      }
   }


}