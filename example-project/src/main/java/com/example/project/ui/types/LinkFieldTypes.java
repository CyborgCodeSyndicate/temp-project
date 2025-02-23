package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.link.LinkComponentType;

public enum LinkFieldTypes implements LinkComponentType {

    MD_LINK_TYPE,
    BOOTSTRAP_LINK_TYPE,
    VA_LINK_TYPE;


    public static final String MD_LINK = "MD_LINK_TYPE";
    public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK_TYPE";
    public static final String VA_LINK = "VA_LINK_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
