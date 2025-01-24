package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.alert.AlertComponentType;

public enum AlertFieldTypes implements AlertComponentType {

    MD_ALERT_TYPE;


    public static final String MD_ALERT = "MD_ALERT_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
