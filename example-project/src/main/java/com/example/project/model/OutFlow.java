package com.example.project.model;

import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

@TableInfo(
        tableContainerLocator = @FindBy(id = "report-1016"),
        rowsLocator = @FindBy(xpath = "(//tbody)[4]//tr//td/.."),
        headerRowLocator = @FindBy(id = "headercontainer-1017"))
@NoArgsConstructor
@Getter
@Setter
public class OutFlow {

    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
            headerCellLocator = @FindBy(css = "#headercontainer-1017-targetEl > div:nth-child(1) span"))
    private TableCell category;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
            headerCellLocator = @FindBy(css = "#headercontainer-1017-targetEl > div:nth-child(2) span"))
    private TableCell amount;


    @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(3)"),
            headerCellLocator = @FindBy(css = "#headercontainer-1017-targetEl > div:nth-child(3) span"))
    private TableCell details;

}
