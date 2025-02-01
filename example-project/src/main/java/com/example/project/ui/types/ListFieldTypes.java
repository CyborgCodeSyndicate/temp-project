package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.list.ItemListComponentType;

public enum ListFieldTypes implements ItemListComponentType {

    MD_LIST_TYPE,
    BOOTSTRAP_LIST_TYPE;


    public static final String MD_LIST = "MD_LIST_TYPE";
    public static final String BOOTSTRAP_LIST = "BOOTSTRAP_LIST_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
