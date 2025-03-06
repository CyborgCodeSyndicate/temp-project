package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.List;

public class TableServiceImpl extends AbstractComponentService<TableComponentType, Table> implements TableService {

    private final TableServiceRegistry tableServiceRegistry;
    private final UiTableValidator uiTableValidator;

    public TableServiceImpl(final SmartWebDriver driver, final TableServiceRegistry tableServiceRegistry, UiTableValidator uiTableValidator) {
        super(driver);
        this.tableServiceRegistry = tableServiceRegistry;
        this.uiTableValidator = uiTableValidator;
    }


    @Override
    protected Table createComponent(final TableComponentType componentType) {
        Table tableComponent = ComponentFactory.getTableComponent(componentType, driver);
        TableImpl table = (TableImpl) tableComponent;
        table.setServiceRegistry(tableServiceRegistry);
        return tableComponent;
    }


    @Step("[UI - Table] Reading table for component type '{tableComponentType}' as class '{clazz}'")
    @Override
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final Class<T> clazz) {
        LogUI.step("Reading table for component type '" + tableComponentType + "' as class '" + clazz + "'");
        return getOrCreateComponent(tableComponentType).readTable(clazz);
    }


    @Step("[UI - Table] Reading table for component type '{tableComponentType}' as class '{clazz}' with fields {fields}")
    @Override
    @SafeVarargs
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final Class<T> clazz,
                                       final TableField<T>... fields) {
        LogUI.step("Reading table for component type '" + tableComponentType + "' as class '" + clazz + "' with fields " + Arrays.toString(fields));
        return getOrCreateComponent(tableComponentType).readTable(clazz, fields);
    }


    @Step("[UI - Table] Reading table rows {start} to {end} for component type '{tableComponentType}' as class '{clazz}'")
    @Override
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final int start, final int end,
                                       final Class<T> clazz) {
        LogUI.step("Reading table rows " + start + " to " + end + " for component type '" + tableComponentType + "' as class '" + clazz + "'");
        return getOrCreateComponent(tableComponentType).readTable(start, end, clazz);
    }


    @Step("[UI - Table] Reading table rows {start} to {end} for component type '{tableComponentType}' as class '{clazz}' with fields {fields}")
    @Override
    @SafeVarargs
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final int start, final int end,
                                       final Class<T> clazz,
                                       final TableField<T>... fields) {
        LogUI.step("Reading table rows " + start + " to " + end + " for component type '" + tableComponentType + "' as class '" + clazz + "' with fields " + fields);
        return getOrCreateComponent(tableComponentType).readTable(start, end, clazz, fields);
    }


    @Step("[UI - Table] Reading row {row} for component type '{tableComponentType}' as class '{clazz}'")
    @Override
    public final <T> T readRow(final TableComponentType tableComponentType, final int row, final Class<T> clazz) {
        LogUI.step("Reading row " + row + " for component type '" + tableComponentType + "' as class '" + clazz + "'");
        return getOrCreateComponent(tableComponentType).readRow(row, clazz);
    }


    @Step("[UI - Table] Reading row matching criteria {searchCriteria} for component type '{tableComponentType}' as class '{clazz}'")
    @Override
    public final <T> T readRow(final TableComponentType tableComponentType, final List<String> searchCriteria,
                               final Class<T> clazz) {
        LogUI.step("Reading row matching criteria " + searchCriteria + " for component type '" + tableComponentType + "' as class '" + clazz + "'");
        return getOrCreateComponent(tableComponentType).readRow(searchCriteria, clazz);
    }


    @Step("[UI - Table] Reading row {row} for component type '{tableComponentType}' as class '{clazz}' with fields {fields}")
    @Override
    @SafeVarargs
    public final <T> T readRow(final TableComponentType tableComponentType, final int row, final Class<T> clazz,
                               final TableField<T>... fields) {
        LogUI.step("Reading row " + row + " for component type '" + tableComponentType + "' as class '" + clazz + "' with fields " + fields);
        return getOrCreateComponent(tableComponentType).readRow(row, clazz, fields);
    }


    @Step("[UI - Table] Reading row matching criteria {searchCriteria} for component type '{tableComponentType}' as class '{clazz}' with fields {fields}")
    @Override
    @SafeVarargs
    public final <T> T readRow(final TableComponentType tableComponentType, final List<String> searchCriteria,
                               final Class<T> clazz,
                               final TableField<T>... fields) {
        LogUI.step("Reading row matching criteria " + searchCriteria + " for component type '" + tableComponentType + "' as class '" + clazz + "' with fields " + fields);
        return getOrCreateComponent(tableComponentType).readRow(searchCriteria, clazz, fields);
    }


    @Step("[UI - Table] Inserting cell value for row {row} for component type '{tableComponentType}' with data as class '{tClass}'")
    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType, final int row,
                                          final Class<T> tClass,
                                          final T data) {
        LogUI.step("Inserting cell value for row " + row + " for component type '" + tableComponentType + "' with data as class '" + tClass + "'");
        getOrCreateComponent(tableComponentType).insertCellValue(row, tClass, data);
    }


    @Step("[UI - Table] Inserting cell value for row {row} for component type '{tableComponentType}', field '{field}', index {index}")
    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType, final int row,
                                          final Class<T> tClass,
                                          final TableField<T> field, final int index, final String... value) {
        LogUI.step("Inserting cell value for row " + row + " for component type '" + tableComponentType + "', field '" + field + "', index " + index);
        getOrCreateComponent(tableComponentType).insertCellValue(row, tClass, field, index, value);
    }


    @Step("[UI - Table] Inserting cell value for row matching criteria {searchCriteria} for component type '{tableComponentType}', field '{field}', index {index}")
    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType,
                                          final List<String> searchCriteria,
                                          final Class<T> tClass,
                                          final TableField<T> field, final int index, final String... values) {
        LogUI.step("Inserting cell value for row matching criteria " + searchCriteria + " for component type '" + tableComponentType + "', field '" + field + "', index " + index);
        getOrCreateComponent(tableComponentType).insertCellValue(searchCriteria, tClass, field, index, values);
    }


    @Step("[UI - Table] Inserting cell value for row matching criteria {searchCriteria} for component type '{tableComponentType}' with data as class '{tClass}'")
    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType,
                                          final List<String> searchCriteria,
                                          final Class<T> tClass,
                                          final T data) {
        LogUI.step("Inserting cell value for row matching criteria " + searchCriteria + " for component type '" + tableComponentType + "' with data as class '" + tClass + "'");
        getOrCreateComponent(tableComponentType).insertCellValue(searchCriteria, tClass, data);
    }


    @Step("[UI - Table] Filtering table for component type '{tableComponentType}', class '{tclass}', column '{column}', using strategy '{filterStrategy}' with values {values}")
    @Override
    public final <T> void filterTable(final TableComponentType tableComponentType, final Class<T> tclass,
                                      final TableField<T> column,
                                      final FilterStrategy filterStrategy, final String... values) {
        LogUI.step("Filtering table for component type '" + tableComponentType + "', class '" + tclass + "', column '" + column + "', using strategy '" + filterStrategy + "' with values " + values);
        getOrCreateComponent(tableComponentType).filterTable(tclass, column, filterStrategy, values);
    }


    @Step("[UI - Table] Sorting table for component type '{tableComponentType}', class '{tclass}', column '{column}', using sorting strategy '{sortingStrategy}'")
    @Override
    final public <T> void sortTable(final TableComponentType tableComponentType, final Class<T> tclass,
                                    final TableField<T> column,
                                    final SortingStrategy sortingStrategy) {
        LogUI.step("Sorting table for component type '" + tableComponentType + "', class '" + tclass + "', column '" + column + "', using sorting strategy '" + sortingStrategy + "'");
        getOrCreateComponent(tableComponentType).sortTable(tclass, column, sortingStrategy);

    }

    @Override
    public <T> List<AssertionResult<T>> validate(final Object table, final Assertion<?>... assertions) {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null for validation.");
        }
        if (assertions == null || assertions.length == 0) {
            throw new IllegalArgumentException("At least one assertion must be provided.");
        }
        return uiTableValidator.validateTable(table, assertions);
    }

}
