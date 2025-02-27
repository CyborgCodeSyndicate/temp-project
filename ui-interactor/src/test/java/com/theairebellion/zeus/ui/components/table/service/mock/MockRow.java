package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.annotations.CellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import org.openqa.selenium.support.FindBy;

@TableInfo(
        tableContainerLocator = @FindBy(xpath = "//table"),
        rowsLocator = @FindBy(xpath = ".//tr"),
        headerRowLocator = @FindBy(xpath = ".//thead/tr")
)
public class MockRow {
    @TableCellLocator(cellLocator = @FindBy(xpath = "//td"), tableSection = "default")
    @CellInsertion(type = ComponentType.class, componentType = "DUMMY", order = 1)
    private TableCell cell;

    public TableCell getCell() {
        return cell;
    }

    public void setCell(TableCell cell) {
        this.cell = cell;
    }
}
