package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

@SuppressWarnings("all")
public class MockTableService implements TableService {

    @Override
    public List readTable(TableComponentType tableComponentType, Class clazz) {
        return List.of("default");
    }

    @Override
    public List readTable(TableComponentType tableComponentType, Class clazz, TableField... fields) {
        return List.of("default");
    }

    @Override
    public List readTable(TableComponentType tableComponentType, int start, int end, Class clazz) {
        return List.of("default");
    }

    @Override
    public List readTable(TableComponentType tableComponentType, int start, int end, Class clazz, TableField... fields) {
        return List.of("default");
    }

    @Override
    public Object readRow(TableComponentType tableComponentType, int row, Class clazz) {
        return "default";
    }

    @Override
    public Object readRow(TableComponentType tableComponentType, List searchCriteria, Class clazz) {
        return "default";
    }

    @Override
    public Object readRow(TableComponentType tableComponentType, int row, Class clazz, TableField... fields) {
        return "default";
    }

    @Override
    public Object readRow(TableComponentType tableComponentType, List searchCriteria, Class clazz, TableField... fields) {
        return "default";
    }

    @Override
    public void insertCellValue(TableComponentType tableComponentType, int row, Class tClass, Object data) {
    }

    @Override
    public void insertCellValue(TableComponentType tableComponentType, int row, Class tClass, TableField field, int index, String... value) {
    }

    @Override
    public void insertCellValue(TableComponentType tableComponentType, List searchCriteria, Class tClass, TableField field, int index, String... values) {
    }

    @Override
    public void insertCellValue(TableComponentType tableComponentType, List searchCriteria, Class tClass, Object data) {
    }

    @Override
    public void filterTable(TableComponentType tableComponentType, Class tclass, TableField column, FilterStrategy filterStrategy, String... values) {
    }

    @Override
    public void sortTable(TableComponentType tableComponentType, Class tclass, TableField column, SortingStrategy sortingStrategy) {
    }

    @Override
    public <T> List<AssertionResult<T>> validate(Object table, Assertion<?>... assertions) {
        return List.of();
    }
}
