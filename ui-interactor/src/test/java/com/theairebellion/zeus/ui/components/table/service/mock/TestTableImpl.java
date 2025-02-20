package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.model.TableLocators;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.TableImpl;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

import java.util.List;

public class TestTableImpl extends TableImpl {

    private final TableLocators locators;
    private final SmartWebElement container;
    private final List<SmartWebElement> rows;

    public TestTableImpl(SmartWebDriver driver, TableServiceRegistry registry, TableLocators locators,
                         SmartWebElement container, List<SmartWebElement> rows) {
        super(driver, registry);
        this.locators = locators;
        this.container = container;
        this.rows = rows;
    }

    @Override
    protected SmartWebElement getTableContainer(By tableContainerLocator) {
        return container;
    }

    @Override
    protected List<SmartWebElement> getRows(SmartWebElement tableContainer, By tableRowsLocator, String section) {
        return rows;
    }

    @Override
    protected SmartWebElement getHeaderRow(SmartWebElement tableContainer, By headerRowLocator, String section) {
        return container;
    }

    @Override
    protected void sortTable(SmartWebElement headerCell, SortingStrategy sortingStrategy) {
        // No operation for testing.
    }
}
