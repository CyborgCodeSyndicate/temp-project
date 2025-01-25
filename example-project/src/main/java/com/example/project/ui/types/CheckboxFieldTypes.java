package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;

public enum CheckboxFieldTypes implements CheckboxComponentType {

    MD_CHECKBOX_TYPE;


    public static final String MD_CHECKBOX = "MD_CHECKBOX_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
