package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CellFilterComponent {

    private Class<? extends ComponentType> type;
    private String componentType;

}
