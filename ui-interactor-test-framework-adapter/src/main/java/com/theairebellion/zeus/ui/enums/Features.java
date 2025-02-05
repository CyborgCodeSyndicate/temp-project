package com.theairebellion.zeus.ui.enums;

import lombok.Getter;

public enum Features {

    INPUT_FIELDS("inputField"),
    BUTTON_FIELDS("buttonField"),
    RADIO_FIELDS("radioField"),
    CHECKBOX_FIELDS("checkboxField"),
    TOGGLE_FIELDS("toggleField"),
    SELECT_FIELDS("selectField"),
    LIST_FIELDS("listField"),
    LOADER_FIELDS("loaderField"),
    LINK_FIELDS("linkField"),
    ALERT_FIELDS("alertField"),
    TAB_FIELDS("tabField"),
    REQUESTS_INTERCEPTOR("interceptor"),
    DATA_INSERTION("insertionService");

    @Getter
    private final String fieldName;


    Features(final String fieldName) {
        this.fieldName = fieldName;
    }
}
