package com.bakery.project.ui.types;

import com.theairebellion.zeus.ui.components.modal.ModalComponentType;

public enum ModalFieldTypes implements ModalComponentType {

    MD_MODAL_TYPE;


    public static final String MD_MODAL = "MD_MODAL_TYPE";


    @Override
    public Enum getType() {
        return this;
    }
}
