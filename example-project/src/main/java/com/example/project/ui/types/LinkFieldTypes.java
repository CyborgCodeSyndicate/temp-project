package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.link.LinkComponentType;

public enum LinkFieldTypes implements LinkComponentType {

    MD_LINK,
    BOOTSTRAP_LINK;


    public static final class Data {

        public static final String MD_LINK = "MD_LINK";
        public static final String BOOTSTRAP_LINK = "BOOTSTRAP_LINK";

    }

    @Override
    public Enum getType() {
        return this;
    }
}
