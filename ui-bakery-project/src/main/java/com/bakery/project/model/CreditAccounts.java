package com.bakery.project.model;

import com.bakery.project.ui.types.LinkFieldTypes;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.components.table.annotations.CellInsertion;
import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

@TableInfo(
        tableContainerLocator = @FindBy(xpath = "(//div[@class='board-content'])[3]"),
        rowsLocator = @FindBy(css = "tbody tr"),
        headerRowLocator = @FindBy(css = "thead tr"))
@NoArgsConstructor
@Getter
@Setter
public class CreditAccounts {

    @CellInsertion(type = LinkComponentType.class, componentType = LinkFieldTypes.Data.BOOTSTRAP_LINK)
    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(1)"))
    private TableCell account;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(2)"))
    private TableCell creditCard;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(3)"),
            headerCellLocator = @FindBy(css = "th:nth-of-type(3)"))
    private TableCell balance;

}
