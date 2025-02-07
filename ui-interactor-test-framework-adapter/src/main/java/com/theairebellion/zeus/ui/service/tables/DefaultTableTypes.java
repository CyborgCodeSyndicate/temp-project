package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;

public enum DefaultTableTypes implements TableComponentType {
    DEFAULT;


    @Override
    public Enum<?> getType() {
        return this;
    }
}
