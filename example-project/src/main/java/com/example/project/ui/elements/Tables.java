package com.example.project.ui.elements;

import com.example.project.model.CreditAccounts;
import com.example.project.model.OutFlow;
import com.example.project.model.TableEntry;
import com.example.project.model.TransactionEntry;
import com.example.project.ui.functions.SharedUI;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.tables.TableElement;

import java.util.function.Consumer;

import static com.example.project.ui.elements.TableTypes.SIMPLE;

public enum Tables implements TableElement {

    CAMPAIGNS(TableEntry.class),
    TRANSACTIONS(TransactionEntry.class),
    CREDIT_ACCOUNTS(CreditAccounts.class),
    OUTFLOW(OutFlow.class),
    ORDERS(TableEntry.class, SIMPLE);


    private final Class<?> rowRepresentationClass;
    private TableComponentType tableType;


    <T> Tables(final Class<T> rowRepresentationClass) {
        this.rowRepresentationClass = rowRepresentationClass;
    }


    <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType) {
        this.rowRepresentationClass = rowRepresentationClass;
        this.tableType = tableType;
    }


    @Override
    public <T extends TableComponentType> T tableType() {
        if (tableType != null) {
            return (T) tableType;
        } else {
            return TableElement.super.tableType();
        }
    }


    @Override
    public <T> Class<T> rowsRepresentationClass() {
        return (Class<T>) rowRepresentationClass;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }


    @Override
    public Consumer<SmartWebDriver> before() {
        return SharedUI.WAIT_FOR_LOADING;
    }


    @Override
    public Consumer<SmartWebDriver> after() {
        return TableElement.super.after();
    }


}
