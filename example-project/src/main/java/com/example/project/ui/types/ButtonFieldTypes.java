package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.button.ButtonComponentType;

public enum ButtonFieldTypes implements ButtonComponentType {

    MD_BUTTON_TYPE;


    public static final String MD_BUTTON = "MD_BUTTON_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
