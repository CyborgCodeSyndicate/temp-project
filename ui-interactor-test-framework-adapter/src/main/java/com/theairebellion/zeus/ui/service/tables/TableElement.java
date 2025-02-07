package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

import java.util.function.Consumer;

public interface TableElement {

    @SuppressWarnings("unchecked")
    default <T extends TableComponentType> T tableType() {
        return (T) DefaultTableTypes.DEFAULT;
    }

    <T> Class<T> rowsRepresentationClass();

    Enum<?> enumImpl();

    default Consumer<SmartWebDriver> before() {
        return smartWebDriver -> {
        };
    }

    default Consumer<SmartWebDriver> after() {
        return smartWebDriver -> {
        };
    }


}
