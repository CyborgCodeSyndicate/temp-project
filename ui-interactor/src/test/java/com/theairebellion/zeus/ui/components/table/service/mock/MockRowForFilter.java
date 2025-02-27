package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.annotations.CellFilter;
import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import org.openqa.selenium.support.FindBy;

@TableInfo(
        tableContainerLocator = @FindBy(xpath = "//table"),
        rowsLocator = @FindBy(xpath = ".//tr"),
        headerRowLocator = @FindBy(xpath = ".//thead/tr")
)
public class MockRowForFilter {
    @TableCellLocator(cellLocator = @FindBy(xpath = "//td"), tableSection = "filterSection")
    @CellFilter(type = MockComponentType.class, componentType = "DUMMY")
    private TableCell cell;

    public void setCell(TableCell cell) {
        this.cell = cell;
    }
}
