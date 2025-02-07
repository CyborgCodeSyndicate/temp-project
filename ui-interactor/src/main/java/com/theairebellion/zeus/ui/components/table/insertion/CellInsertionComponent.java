package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CellInsertionComponent {

    private Class<? extends ComponentType> type;
    private String componentType;
    int order;

}
