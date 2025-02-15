package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.toggle.ToggleComponentType;

public enum ToggleFieldTypes implements ToggleComponentType {

    MD_TOGGLE_TYPE;


    public static final String MD_TOGGLE = "MD_TOGGLE_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
