package com.theairebellion.zeus.ui.components.table.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;


@AllArgsConstructor
@Getter
public class TableLocators {

    private By tableContainerLocator;
    private By tableRowsLocator;
    private By headerRowLocator;

}
