package com.theairebellion.zeus.ui.service.tables;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import manifold.ext.rt.api.Jailbreak;

import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class TableServiceFluent {

    private final TableService tableService;
    private final @Jailbreak UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public TableServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, TableService tableService,
                              SmartWebDriver webDriver) {
        this.tableService = tableService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public final <T> UIServiceFluent readTable(TableElement tableElement) {
        tableElement.before().accept(driver);
        List<T> rows = tableService.readTable(tableElement.tableType(), tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;
    }


    @SafeVarargs
    public final <T> UIServiceFluent readTable(TableElement tableElement, TableField<T>... fields) {
        validateArguments(fields[0], tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        List<T> rows = tableService.readTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent readTable(TableElement tableElement, int start, int end) {
        tableElement.before().accept(driver);
        List<T> rows = tableService.readTable(tableElement.tableType(), start, end,
            tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;
    }

    // public final <T> UIServiceFluent validateTable(TableElement tableElement, Assertion<?>...assertions) {
    //     List list = storage.sub(UI).get(tableElement.enumImpl(), List.class);
    //     uiServiceFluent.validation();
    //     return uiServiceFluent;
    // }
    //
    // public final <T> UIServiceFluent validateRow(TableElement tableElement, Assertion<?>...assertions) {
    //     List list = storage.sub(UI).get(tableElement.enumImpl(), List.class);
    //     uiServiceFluent.validation();
    //     return uiServiceFluent;
    // }


    @SafeVarargs
    public final <T> UIServiceFluent readTable(TableElement tableElement, int start, int end,
                                               TableField<T>... fields) {
        validateArguments(fields[0], tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        List<T> rows = tableService.readTable(tableElement.tableType(), start, end,
            tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;

    }


    public final <T> UIServiceFluent readRow(TableElement tableElement, int row) {
        tableElement.before().accept(driver);
        T rowEntry = tableService.readRow(tableElement.tableType(), row,
            tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rowEntry);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent readRow(TableElement tableElement, List<String> searchCriteria) {
        tableElement.before().accept(driver);
        T row = tableService.readRow(tableElement.tableType(), searchCriteria,
            tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), row);
        return uiServiceFluent;
    }


    @SafeVarargs
    public final <T> UIServiceFluent readRow(TableElement tableElement, int row,
                                             TableField<T>... fields) {
        tableElement.before().accept(driver);
        T rowEntry = tableService.readRow(tableElement.tableType(), row,
            tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rowEntry);
        return uiServiceFluent;
    }


    @SafeVarargs
    public final <T> UIServiceFluent readRow(TableElement tableElement, List<String> searchCriteria,
                                             TableField<T>... fields) {
        validateArguments(fields[0], tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        T rowEntry = tableService.readRow(tableElement.tableType(), searchCriteria,
            tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rowEntry);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent insertCellValue(TableElement tableElement, int row,
                                                     TableField<T> field, String... values) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), field, 1,
            values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent insertCellValue(TableElement tableElement, int row,
                                                     TableField<T> field,
                                                     int index, String... value) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), field,
            index, value);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent insertCellValue(TableElement tableElement, List<String> searchCriteria,
                                                     TableField<T> field,
                                                     String... values) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
            field, 1, values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent insertCellValue(TableElement tableElement, List<String> searchCriteria,
                                                     TableField<T> field,
                                                     int index, String... values) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
            field, index, values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent insertCellValue(TableElement tableElement, int row,
                                                     T data) {
        if (!tableElement.rowsRepresentationClass().equals(data.getClass())) {
            throw new IllegalArgumentException(
                "The Data object must be from class: " + tableElement.rowsRepresentationClass());
        }
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), data);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent insertCellValue(TableElement tableElement, List<String> searchCriteria, T data) {
        if (!tableElement.rowsRepresentationClass().equals(data.getClass())) {
            throw new IllegalArgumentException(
                "The Data object must be from class: " + tableElement.rowsRepresentationClass());
        }
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
            data);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent filterTable(TableElement tableElement,
                                                 TableField<T> column,
                                                 FilterStrategy filterStrategy, String... values) {
        validateArguments(column, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.filterTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), column,
            filterStrategy, values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <T> UIServiceFluent sortTable(TableElement tableElement, TableField<T> column,
                                               SortingStrategy sortingStrategy) {
        validateArguments(column, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.sortTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), column,
            sortingStrategy);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    private <T> void validateArguments(TableField<T> field, Class<T> expectedClass) {
        try {
            T instance = expectedClass.getDeclaredConstructor().newInstance();
            field.invoke(instance, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("The TableField objects should be from class: " + expectedClass);
        }
    }

}


