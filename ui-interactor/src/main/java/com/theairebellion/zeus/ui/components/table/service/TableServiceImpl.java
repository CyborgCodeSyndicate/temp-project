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


    @Override
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final Class<T> clazz) {
        return getOrCreateComponent(tableComponentType).readTable(clazz);
    }


    @Override
    @SafeVarargs
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final Class<T> clazz,
                                       final TableField<T>... fields) {
        return getOrCreateComponent(tableComponentType).readTable(clazz, fields);
    }


    @Override
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final int start, final int end,
                                       final Class<T> clazz) {
        return getOrCreateComponent(tableComponentType).readTable(start, end, clazz);
    }


    @Override
    @SafeVarargs
    public final <T> List<T> readTable(final TableComponentType tableComponentType, final int start, final int end,
                                       final Class<T> clazz,
                                       final TableField<T>... fields) {
        return getOrCreateComponent(tableComponentType).readTable(start, end, clazz, fields);
    }


    @Override
    public final <T> T readRow(final TableComponentType tableComponentType, final int row, final Class<T> clazz) {
        return getOrCreateComponent(tableComponentType).readRow(row, clazz);
    }


    @Override
    public final <T> T readRow(final TableComponentType tableComponentType, final List<String> searchCriteria,
                               final Class<T> clazz) {
        return getOrCreateComponent(tableComponentType).readRow(searchCriteria, clazz);
    }


    @Override
    @SafeVarargs
    public final <T> T readRow(final TableComponentType tableComponentType, final int row, final Class<T> clazz,
                               final TableField<T>... fields) {
        return getOrCreateComponent(tableComponentType).readRow(row, clazz, fields);
    }


    @Override
    @SafeVarargs
    public final <T> T readRow(final TableComponentType tableComponentType, final List<String> searchCriteria,
                               final Class<T> clazz,
                               final TableField<T>... fields) {
        return getOrCreateComponent(tableComponentType).readRow(searchCriteria, clazz, fields);
    }


    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType, final int row,
                                          final Class<T> tClass,
                                          final T data) {
        getOrCreateComponent(tableComponentType).insertCellValue(row, tClass, data);
    }


    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType, final int row,
                                          final Class<T> tClass,
                                          final TableField<T> field, final int index, final String... value) {
        getOrCreateComponent(tableComponentType).insertCellValue(row, tClass, field, index, value);
    }


    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType,
                                          final List<String> searchCriteria,
                                          final Class<T> tClass,
                                          final TableField<T> field, final int index, final String... values) {
        getOrCreateComponent(tableComponentType).insertCellValue(searchCriteria, tClass, field, index, values);
    }


    @Override
    public final <T> void insertCellValue(final TableComponentType tableComponentType,
                                          final List<String> searchCriteria,
                                          final Class<T> tClass,
                                          final T data) {
        getOrCreateComponent(tableComponentType).insertCellValue(searchCriteria, tClass, data);
    }


    @Override
    public final <T> void filterTable(final TableComponentType tableComponentType, final Class<T> tclass,
                                      final TableField<T> column,
                                      final FilterStrategy filterStrategy, final String... values) {
        getOrCreateComponent(tableComponentType).filterTable(tclass, column, filterStrategy, values);
    }


    @Override
    final public <T> void sortTable(final TableComponentType tableComponentType, final Class<T> tclass,
                                    final TableField<T> column,
                                    final SortingStrategy sortingStrategy) {
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
