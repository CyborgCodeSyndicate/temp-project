package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.button.ButtonComponentType;

public enum LinkFieldTypes implements ButtonComponentType {

    MD_LINK_TYPE;


    public static final String MD_LINK = "MD_LINK_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
