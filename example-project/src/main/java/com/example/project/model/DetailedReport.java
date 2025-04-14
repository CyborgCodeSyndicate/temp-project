package com.example.project.model;

import com.theairebellion.zeus.ui.components.table.annotations.TableCellLocator;
import com.theairebellion.zeus.ui.components.table.annotations.TableInfo;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.support.FindBy;

@TableInfo(
      tableContainerLocator = @FindBy(id = "detailedreport-1041"),
      rowsLocator = @FindBy(xpath = ".//tbody//tr//td/.."),
      headerRowLocator = @FindBy(id = "headercontainer-1042"))
@NoArgsConstructor
@Getter
@Setter
public class DetailedReport {

   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(1)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1042-targetEl > div:nth-child(1) span"))
   private TableCell date;


   @TableCellLocator(cellLocator = @FindBy(css = "td:nth-of-type(2)"),
         headerCellLocator = @FindBy(css = "#headercontainer-1042-targetEl > div:nth-child(2) span"))
   private TableCell amount;
}
