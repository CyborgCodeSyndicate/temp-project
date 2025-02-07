package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;

import java.util.List;

public interface Table {

    <T> List<T> readTable(Class<T> clazz);

    <T> List<T> readTable(Class<T> clazz, TableField<T>... fields);

    <T> List<T> readTable(int start, int end, Class<T> clazz);

    <T> List<T> readTable(int start, int end, Class<T> clazz,
                          TableField<T>... fields);

    <T> T readRow(int row, Class<T> clazz);

    <T> T readRow(List<String> searchCriteria, Class<T> clazz);

    <T> T readRow(int row, Class<T> clazz, TableField<T>... fields);

    <T> T readRow(List<String> searchCriteria, Class<T> clazz,
                  TableField<T>... fields);

    <T> void insertCellValue(int row, Class<T> tClass, T data);


    default <T> void insertCellValue(int row, Class<T> tClass, TableField<T> field,
                                     String... values) {
        insertCellValue(row, tClass, field, 1, values);
    }

    <T> void insertCellValue(int row, Class<T> tClass, TableField<T> field, int index,
                             String... value);

    default <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass,
                                     TableField<T> field,
                                     String... values) {
        insertCellValue(searchCriteria, tClass, field, 1, values);
    }

    <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, TableField<T> field,
                             int index, String... values);


    <T> void insertCellValue(List<String> searchCriteria, Class<T> tClass, T data);


    <T> void filterTable(Class<T> tclass, TableField<T> column, FilterStrategy filterStrategy,
                         String... values);

    <T> void sortTable(Class<T> tclass, TableField<T> column, SortingStrategy sortingStrategy);


}