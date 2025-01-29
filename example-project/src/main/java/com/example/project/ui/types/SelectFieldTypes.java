package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.select.SelectComponentType;

public enum SelectFieldTypes implements SelectComponentType {

    MD_SELECT_TYPE,
    BOOTSTRAP_SELECT_TYPE;


    public static final String MD_SELECT = "MD_SELECT_TYPE";
    public static final String BOOTSTRAP_SELECT = "BOOTSTRAP_SELECT_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
