package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.radio.RadioComponentType;

public enum SelectFieldTypes implements RadioComponentType {

    MD_SELECT_TYPE;


    public static final String MD_SELECT = "MD_SELECT_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
