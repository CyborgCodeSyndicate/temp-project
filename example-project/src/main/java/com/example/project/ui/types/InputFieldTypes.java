package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.input.InputComponentType;

public enum InputFieldTypes implements InputComponentType {

    MD_INPUT;

    public static final class Data {

        public static final String MD_INPUT = "MD_INPUT";

    }



    @Override
    public Enum getType() {
        return this;
    }
}
