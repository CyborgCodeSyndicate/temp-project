package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;

import java.util.List;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public interface TableService {

    TableComponentType DEFAULT_TYPE = getDefaultType();


    default <T> List<T> readTable(Class<T> clazz) {
        return readTable(DEFAULT_TYPE, clazz);
    }

    <T> List<T> readTable(TableComponentType tableComponentType, Class<T> clazz);

    default <T> List<T> readTable(Class<T> clazz, TableField<T>... fields) {
        return readTable(DEFAULT_TYPE, clazz, fields);
    }

    <T> List<T> readTable(TableComponentType tableComponentType, Class<T> clazz, TableField<T>... fields);

    default <T> List<T> readTable(int start, int end, Class<T> clazz) {
        return readTable(DEFAULT_TYPE, start, end, clazz);
    }

    <T> List<T> readTable(TableComponentType tableComponentType, int start, int end, Class<T> clazz);

    default <T> List<T> readTable(int start, int end, Class<T> clazz,
                                  TableField<T>... fields) {
        return readTable(DEFAULT_TYPE, start, end, clazz, fields);
    }

    <T> List<T> readTable(TableComponentType tableComponentType, int start, int end, Class<T> clazz,
                          TableField<T>... fields);

    default <T> T readRow(int row, Class<T> clazz) {
        return readRow(DEFAULT_TYPE, row, clazz);
    }

    <T> T readRow(TableComponentType tableComponentType, int row, Class<T> clazz);

    default <T> T readRow(List<String> searchCriteria, Class<T> clazz) {
        return readRow(DEFAULT_TYPE, searchCriteria, clazz);
    }

    <T> T readRow(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> clazz);

    default <T> T readRow(int row, Class<T> clazz, TableField<T>... fields) {
        return readRow(DEFAULT_TYPE, row, clazz, fields);
    }

    <T> T readRow(TableComponentType tableComponentType, int row, Class<T> clazz, TableField<T>... fields);

    default <T> T readRow(List<String> searchCriteria, Class<T> clazz,
                          TableField<T>... fields) {
        return readRow(DEFAULT_TYPE, searchCriteria, clazz, fields);
    }

    <T> T readRow(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> clazz,
                  TableField<T>... fields);

    default <T> void insertCellValue(int row, Class<T> tClass, T data) {
        insertCellValue(DEFAULT_TYPE, row, tClass, data);
    }

    <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> tClass, T data);

    default <T> void insertCellValue(int row, Class<T> tClass, TableField<T> field, String... values) {
        insertCellValue(DEFAULT_TYPE, row, tClass, field, 1, values);
    }


    default <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> tClass,
                                     TableField<T> field, String... values) {
        insertCellValue(tableComponentType, row, tClass, field, 1, values);
    }


    default <T> void insertCellValue(int row, Class<T> tClass, TableField<T> field, int index, String... value) {
        insertCellValue(DEFAULT_TYPE, row, tClass, field, index, value);
    }

    <T> void insertCellValue(TableComponentType tableComponentType, int row, Class<T> tClass, TableField<T> field,
                             int index, String... value);


    default <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, TableField<T> field,
                                     String... values) {
        insertCellValue(DEFAULT_TYPE, searchCriteria, tClass, field, 1, values);
    }


    default <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria,
                                     Class<T> tClass, TableField<T> field,
                                     String... values) {
        insertCellValue(tableComponentType, searchCriteria, tClass, field, 1, values);
    }

    default <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, TableField<T> field,
                                     int index, String... values) {
        insertCellValue(DEFAULT_TYPE, searchCriteria, tClass, field, index, values);
    }

    <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria, Class<T> tClass,
                             TableField<T> field,
                             int index, String... values);


    default <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, T data) {
        insertCellValue(DEFAULT_TYPE, searchCriteria, tClass, data);
    }


    <T> void insertCellValue(TableComponentType tableComponentType, List<String> searchCriteria,
                             Class<T> tClass, T data);


    default <T> void filterTable(Class<T> tclass, TableField<T> column, FilterStrategy filterStrategy,
                                 String... values) {
        filterTable(DEFAULT_TYPE, tclass, column, filterStrategy, values);
    }

    <T> void filterTable(TableComponentType tableComponentType, Class<T> tclass, TableField<T> column,
                         FilterStrategy filterStrategy, String... values);

    default <T> void sortTable(Class<T> tclass, TableField<T> column, SortingStrategy sortingStrategy) {
        sortTable(DEFAULT_TYPE, tclass, column, sortingStrategy);
    }

    <T> void sortTable(TableComponentType tableComponentType, Class<T> tclass, TableField<T> column,
                       SortingStrategy sortingStrategy);


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

}
