package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.annotations.CustomCellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

@Setter
@Getter
@TableInfo(
        tableContainerLocator = @FindBy(xpath = "//table"),
        rowsLocator = @FindBy(xpath = ".//tr"),
        headerRowLocator = @FindBy(xpath = ".//thead/tr")
)
public class MockRowCustom {

    @TableCellLocator(cellLocator = @FindBy(xpath = "//td"), tableSection = "custom")
    @CustomCellInsertion(insertionFunction = FailingCellInsertionFunction.class, order = 1)
    private TableCell cell;
}
