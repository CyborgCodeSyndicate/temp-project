package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TableCell {

    private SmartWebElement element;
    private String text;


    public TableCell(final String value) {
        this.text = value;
    }

}
