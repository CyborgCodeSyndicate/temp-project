package com.theairebellion.zeus.ui.service.tables;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;

import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class TableServiceFluent<T extends UIServiceFluent<?>> {

    private final TableService tableService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public TableServiceFluent(T uiServiceFluent, Storage storage, TableService tableService,
                              SmartWebDriver webDriver) {
        this.tableService = tableService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public final <K> T readTable(TableElement tableElement) {
        tableElement.before().accept(driver);
        List<K> rows = tableService.readTable(tableElement.tableType(), tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;
    }


    @SafeVarargs
    public final <K> T readTable(TableElement tableElement, TableField<K>... fields) {
        validateArguments(fields[0], tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        List<K> rows = tableService.readTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;
    }


    public final <K> T readTable(TableElement tableElement, int start, int end) {
        tableElement.before().accept(driver);
        List<K> rows = tableService.readTable(tableElement.tableType(), start, end,
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
    public final <K> T readTable(TableElement tableElement, int start, int end,
                                               TableField<K>... fields) {
        validateArguments(fields[0], tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        List<K> rows = tableService.readTable(tableElement.tableType(), start, end,
                tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rows);
        return uiServiceFluent;

    }


    public final <K> T readRow(TableElement tableElement, int row) {
        tableElement.before().accept(driver);
        K rowEntry = tableService.readRow(tableElement.tableType(), row,
                tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rowEntry);
        return uiServiceFluent;
    }


    public final <K> T readRow(TableElement tableElement, List<String> searchCriteria) {
        tableElement.before().accept(driver);
        K row = tableService.readRow(tableElement.tableType(), searchCriteria,
                tableElement.rowsRepresentationClass());
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), row);
        return uiServiceFluent;
    }


    @SafeVarargs
    public final <K> T readRow(TableElement tableElement, int row,
                                             TableField<K>... fields) {
        tableElement.before().accept(driver);
        K rowEntry = tableService.readRow(tableElement.tableType(), row,
                tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rowEntry);
        return uiServiceFluent;
    }


    @SafeVarargs
    public final <K> T readRow(TableElement tableElement, List<String> searchCriteria,
                                             TableField<K>... fields) {
        validateArguments(fields[0], tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        K rowEntry = tableService.readRow(tableElement.tableType(), searchCriteria,
                tableElement.rowsRepresentationClass(), fields);
        tableElement.after().accept(driver);
        storage.sub(UI).put(tableElement.enumImpl(), rowEntry);
        return uiServiceFluent;
    }


    public final <K> T insertCellValue(TableElement tableElement, int row,
                                                     TableField<K> field, String... values) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), field, 1,
                values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <K> T insertCellValue(TableElement tableElement, int row,
                                                     TableField<K> field,
                                                     int index, String... value) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), field,
                index, value);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <K> T insertCellValue(TableElement tableElement, List<String> searchCriteria,
                                                     TableField<K> field,
                                                     String... values) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
                field, 1, values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <K> T insertCellValue(TableElement tableElement, List<String> searchCriteria,
                                                     TableField<K> field,
                                                     int index, String... values) {
        validateArguments(field, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), searchCriteria, tableElement.rowsRepresentationClass(),
                field, index, values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <K> T insertCellValue(TableElement tableElement, int row,
                                                     K data) {
        if (!tableElement.rowsRepresentationClass().equals(data.getClass())) {
            throw new IllegalArgumentException(
                    "The Data object must be from class: " + tableElement.rowsRepresentationClass());
        }
        tableElement.before().accept(driver);
        tableService.insertCellValue(tableElement.tableType(), row, tableElement.rowsRepresentationClass(), data);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <K> T insertCellValue(TableElement tableElement, List<String> searchCriteria, K data) {
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


    public final <K> T filterTable(TableElement tableElement,
                                                 TableField<K> column,
                                                 FilterStrategy filterStrategy, String... values) {
        validateArguments(column, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.filterTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), column,
                filterStrategy, values);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    public final <K> T sortTable(TableElement tableElement, TableField<K> column,
                                               SortingStrategy sortingStrategy) {
        validateArguments(column, tableElement.rowsRepresentationClass());
        tableElement.before().accept(driver);
        tableService.sortTable(tableElement.tableType(), tableElement.rowsRepresentationClass(), column,
                sortingStrategy);
        tableElement.after().accept(driver);
        return uiServiceFluent;
    }


    private <K> void validateArguments(TableField<K> field, Class<K> expectedClass) {
        try {
            K instance = expectedClass.getDeclaredConstructor().newInstance();
            field.invoke(instance, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("The TableField objects should be from class: " + expectedClass);
        }
    }

    public T validate(TableElement tableElement, Assertion<?>... assertions) {
        Object tableData = storage.sub(UI).get(tableElement.enumImpl(), Object.class);
        if (tableData == null) {
            throw new IllegalArgumentException("No table data found for key: " + tableElement.enumImpl());
        }

        final List<AssertionResult<Object>> results = tableService.validate(tableData, assertions);

        uiServiceFluent.validation(results);

        return uiServiceFluent;
    }

}


