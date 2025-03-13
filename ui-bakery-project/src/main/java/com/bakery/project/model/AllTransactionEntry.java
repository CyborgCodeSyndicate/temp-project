package com.bakery.project.model;

import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

@TableInfo(
        tableContainerLocator = @FindBy(id = "all_transactions_for_account"),
        rowsLocator = @FindBy(css = "tbody tr"),
        headerRowLocator = @FindBy(css = "thead tr"))
@NoArgsConstructor
@Getter
@Setter
public class AllTransactionEntry {


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(1)"))
    private TableCell date;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(2)"))
    private TableCell description;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(3)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(3)"))
    private TableCell deposit;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(4)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(4)"))
    private TableCell withdrawal;

}
