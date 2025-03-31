package com.bakery.project.ui.elements;

import com.bakery.project.model.TableEntry;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.tables.TableElement;

import java.util.function.Consumer;

import static com.bakery.project.ui.elements.TableTypes.SIMPLE;

public enum Tables implements TableElement {

    CAMPAIGNS(TableEntry.class),
    ORDERS(TableEntry.class, SIMPLE);


    private final Class<?> rowRepresentationClass;
    private final TableComponentType tableType;
    private final Consumer<SmartWebDriver> before;
    private final Consumer<SmartWebDriver> after;


    <T> Tables(final Class<T> rowRepresentationClass) {
        this(rowRepresentationClass, null, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType) {
        this(rowRepresentationClass, tableType, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
               Consumer<SmartWebDriver> before) {
        this(rowRepresentationClass, tableType, before, smartWebDriver -> {
        });
    }


    <T> Tables(final Class<T> rowRepresentationClass, TableComponentType tableType,
               Consumer<SmartWebDriver> before, Consumer<SmartWebDriver> after) {
        this.rowRepresentationClass = rowRepresentationClass;
        this.tableType = tableType;
        this.before = before;
        this.after = after;
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
        return before;
    }


    @Override
    public Consumer<SmartWebDriver> after() {
        return after;
    }

}
