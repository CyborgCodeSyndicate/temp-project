package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterComponent;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionComponent;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.By;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CellLocator {

    private String fieldName;
    private By cellLocator;
    private By cellTextLocator;
    private By headerCellLocator;
    private boolean collection;
    private String tableSection;
    private CellInsertionComponent cellInsertionComponent;
    private Class<? extends CellInsertionFunction> customCellInsertion;
    private CellFilterComponent cellFilterComponent;
    private Class<? extends CellFilterFunction> customCellFilter;



}
