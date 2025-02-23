
package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.model.CellLocator;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.model.TableLocators;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.annotations.CellFilter;
import com.theairebellion.zeus.ui.components.table.annotations.CellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.CustomCellFilter;
import com.theairebellion.zeus.ui.components.table.annotations.CustomCellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterComponent;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionComponent;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.*;
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

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public abstract class TableImpl extends BaseComponent implements Table {

    @Setter
    protected TableServiceRegistry serviceRegistry;
    private final List<Object> acceptedValues;


    protected TableImpl(final SmartWebDriver smartWebDriver) {
        super(smartWebDriver);
        this.acceptedValues = List.of(
            new TableCell(""),
            List.of()
        );
    }


    protected TableImpl(final SmartWebDriver smartWebDriver,
                        final TableServiceRegistry serviceRegistry) {
        super(smartWebDriver);
        this.serviceRegistry = serviceRegistry;
        this.acceptedValues = List.of(
            new TableCell(""),
            List.of()
        );
    }


    @Override
    public final <T> List<T> readTable(final Class<T> clazz) {
        return readTableInternal(clazz, null, null, null);
    }


    @Override
    @SafeVarargs
    public final <T> List<T> readTable(final Class<T> clazz, final TableField<T>... fields) {
        return readTableInternal(clazz, (fields == null) ? null : List.of(fields), null, null);
    }


    @Override
    public final <T> List<T> readTable(final int start, final int end, final Class<T> clazz) {
        return readTableInternal(clazz, null, start, end);
    }


    @Override
    @SafeVarargs
    public final <T> List<T> readTable(final int start,
                                       final int end,
                                       final Class<T> clazz,
                                       final TableField<T>... fields) {
        return readTableInternal(clazz, (fields == null) ? null : Arrays.asList(fields), start, end);
    }


    @Override
    public final <T> T readRow(final int row, final Class<T> clazz) {
        return readRowInternal(row - 1, clazz, null);
    }


    @Override
    public final <T> T readRow(final List<String> searchCriteria, final Class<T> clazz) {
        return readRowInternal(searchCriteria, clazz, null);
    }


    @Override
    @SafeVarargs
    public final <T> T readRow(final int row, final Class<T> clazz, final TableField<T>... fields) {
        return readRowInternal(row - 1, clazz, fields);
    }


    @Override
    @SafeVarargs
    public final <T> T readRow(final List<String> searchCriteria,
                               final Class<T> clazz,
                               final TableField<T>... fields) {
        return readRowInternal(searchCriteria, clazz, fields);
    }


    @Override
    public final <T> void insertCellValue(final List<String> searchCriteria,
                                          final Class<T> rowClass,
                                          final TableField<T> field,
                                          final int cellIndex,
                                          final String... values) {
        insertCellValueInternal(searchCriteria, rowClass, field, cellIndex, values);
    }


    @Override
    public final <T> void insertCellValue(final int row,
                                          final Class<T> rowClass,
                                          final TableField<T> field,
                                          final int cellIndex,
                                          final String... values) {
        insertCellValueInternal(row - 1, rowClass, field, cellIndex, values);
    }


    @Override
    public final <T> void insertCellValue(final List<String> searchCriteria, final Class<T> tClass, final T data) {
        processInsertCellValue((fieldInvoker, strings) -> {
            if (strings.length == 1) {
                insertCellValue(searchCriteria, tClass, fieldInvoker, 1, strings);
            } else {
                for (int i = 0; i < strings.length; i++) {
                    insertCellValue(searchCriteria, tClass, fieldInvoker, i + 1, strings[i]);
                }
            }
        }, tClass, data);
    }


    @Override
    public final <T> void insertCellValue(final List<String> searchCriteria, final Class<T> tClass,
                                          final TableField<T> field,
                                          final String... values) {
        Table.super.insertCellValue(searchCriteria, tClass, field, values);
    }


    @Override
    public final <T> void insertCellValue(final int row, final Class<T> tClass, final TableField<T> field,
                                          final String... values) {
        Table.super.insertCellValue(row, tClass, field, values);
    }


    @Override
    public final <T> void insertCellValue(final int row, final Class<T> tClass, final T data) {
        processInsertCellValue((fieldInvoker, strings) -> {
            if (strings.length == 1) {
                insertCellValue(row, tClass, fieldInvoker, 1, strings);
            } else {
                for (int i = 0; i < strings.length; i++) {
                    insertCellValue(row, tClass, fieldInvoker, i + 1, strings[i]);
                }
            }
        }, tClass, data);
    }


    @Override
    public final <T> void filterTable(final Class<T> tclass, final TableField<T> column,
                                      final FilterStrategy filterStrategy,
                                      final String... values) {
        final Map<String, List<CellLocator>> tableSectionLocatorsMap =
            getTableSectionLocatorsMap(tclass, List.of(column));

        final Map.Entry<String, List<CellLocator>> firstEntry = tableSectionLocatorsMap.entrySet().stream()
                                                                    .findFirst()
                                                                    .orElseThrow(() -> new IllegalStateException(
                                                                        "No locator found for the provided field."));

        final String tableSection = firstEntry.getKey();
        final CellLocator cellLocator = firstEntry.getValue().get(0);

        TableLocators tableLocators = getTableLocators(tclass);
        SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());
        SmartWebElement headerRow = getHeaderRow(tableContainer, tableLocators.getHeaderRowLocator(), tableSection);
        filterCells(cellLocator, headerRow, filterStrategy, values);
    }


    @Override
    public final <T> void sortTable(final Class<T> tclass, final TableField<T> column,
                                    final SortingStrategy sortingStrategy) {

        final Map<String, List<CellLocator>> tableSectionLocatorsMap = getTableSectionLocatorsMap(tclass,
            List.of(column));

        final Map.Entry<String, List<CellLocator>> firstEntry = tableSectionLocatorsMap.entrySet().stream()
                                                                    .findFirst()
                                                                    .orElseThrow(() -> new IllegalStateException(
                                                                        "No locator found for the provided field."));

        final String tableSection = firstEntry.getKey();
        final CellLocator cellLocator = firstEntry.getValue().get(0);

        TableLocators tableLocators = getTableLocators(tclass);
        SmartWebElement tableContainer = getTableContainer(tableLocators.getTableContainerLocator());
        SmartWebElement headerRow = getHeaderRow(tableContainer, tableLocators.getHeaderRowLocator(), tableSection);
        sortTable(headerRow.findSmartElement(cellLocator.getHeaderCellLocator()), sortingStrategy);
    }


    protected SmartWebElement getTableContainer(By tableContainerLocator) {
        return driver.findSmartElement(tableContainerLocator);
    }


    protected List<SmartWebElement> getRows(SmartWebElement tableContainer, By tableRowsLocator, String section) {
        return tableContainer.findSmartElements(tableRowsLocator);
    }


    protected SmartWebElement getHeaderRow(SmartWebElement tableContainer, By headerRowLocator, String tableSection) {
        return tableContainer.findSmartElement(headerRowLocator);
    }


    protected void sortTable(SmartWebElement headerCell, SortingStrategy sortingStrategy) {

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
                    section -> readRowsInRange(tableContainer, tableLocators.getTableRowsLocator(), section, start, end)
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
                LogUI.warn("Requested start/end range is invalid: {}-{}. Returning empty list.", start, end);
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
        SmartWebElement tableContainer = getTableContainer(tableLocators.getTableRowsLocator());
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
        final By locator = cellLocator.getCellLocator();
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
                                                   .collect(Collectors.toList());
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
            LogUI.error(message, e);
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
                LogUI.error(message, e);
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
            LogUI.error(message, e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            final String message = String.format(
                "Failed to invoke setter: %s on %s",
                setterName,
                targetObject.getClass().getName()
            );
            LogUI.error(message, e);
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
                                      LogUI.error("Cannot access field: {}", f.getName(), ex);
                                  }
                                  return hasValue;
                              })
                              .toList();
        }

        final boolean validSyntax = validFields.stream()
                                        .allMatch(
                                            f -> isListOfTableCell(f) || TableCell.class.isAssignableFrom(f.getType()));
        if (!validSyntax) {
            LogUI.error("Some fields are not TableCell or List<TableCell>.");
            throw new RuntimeException("Invalid field type for table cell usage.");
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
                LogUI.error(msg);
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
                                                                        "No locator found for the provided field."));

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
            throw new RuntimeException(
                "No table cell insertion method provided for field: " + cellLocator.getFieldName()
            );
        }

        final List<SmartWebElement> cells = rowElement.findSmartElements(cellLocator.getCellLocator());
        if (cells.isEmpty() || cellIndex <= 0 || cellIndex > cells.size()) {
            throw new RuntimeException(String.format(
                "Invalid cell index: %d for locator: %s", cellIndex, cellLocator.getCellLocator()
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
            final Class<? extends Enum> enumClass = ReflectionUtil.findEnumClassImplementationsOfInterface(
                type, getUiConfig().projectPackage()
            );
            final Enum<?> componentInstance = Enum.valueOf(enumClass, component.getComponentType());
            final TableInsertion service = serviceRegistry.getTableService(type);
            service.tableInsertion(targetCell, (ComponentType) componentInstance, values);
        } catch (Exception e) {
            throw new RuntimeException(
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
            throw new RuntimeException(
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
            throw new RuntimeException(
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
            final Class<? extends Enum> enumClass = ReflectionUtil.findEnumClassImplementationsOfInterface(
                type, getUiConfig().projectPackage()
            );
            final Enum<?> componentInstance = Enum.valueOf(enumClass, component.getComponentType());
            final TableFilter service = serviceRegistry.getFilterService(type);
            service.tableFilter(targetCell, (ComponentType) componentInstance, filterStrategy, values);
        } catch (Exception e) {
            throw new RuntimeException(
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
            throw new RuntimeException(
                "Failed to instantiate custom cell filter function: " + customFunction.getName(), e
            );
        }
    }


    private <T> void processInsertCellValue(BiConsumer<TableField<T>, String[]> action, Class<T> tClass, T data) {
        Map<TableField<T>, String[]> invokersMap = prepareFieldInvokersMap(tClass, data);
        invokersMap.forEach(action);
    }


    private <T> Map<TableField<T>, String[]> prepareFieldInvokersMap(Class<T> tClass, T data) {
        return Arrays.stream(tClass.getDeclaredFields())
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
                return cell.getText() == null ? new String[0] : new String[]{cell.getText()};
            } else if (value instanceof List<?> list) {
                return list.stream()
                           .filter(TableCell.class::isInstance)
                           .map(TableCell.class::cast)
                           .map(TableCell::getText)
                           .filter(Objects::nonNull)
                           .toArray(String[]::new);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access field: " + field.getName(), e);
        }
        return new String[0];
    }


    private record OrderedFieldInvokerAndValues<T>(
        TableField<T> fieldInvoker,
        int order,
        String[] stringValues
    ) {

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


}