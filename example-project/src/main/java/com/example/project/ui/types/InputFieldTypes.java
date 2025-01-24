package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.input.InputComponentType;

public enum InputFieldTypes implements InputComponentType {

    MD_INPUT_TYPE,
    BOOTSTRAP_INPUT_TYPE;


    public static final String MD_INPUT = "MD_INPUT_TYPE";
    public static final String BOOTSTRAP_INPUT = "BOOTSTRAP_INPUT_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
