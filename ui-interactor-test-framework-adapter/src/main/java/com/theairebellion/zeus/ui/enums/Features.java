package com.theairebellion.zeus.ui.enums;

import lombok.Getter;

@Getter
public enum Features {

    INPUT_FIELDS("inputField"),
    TABLE("table"),
    REQUESTS_INTERCEPTOR("interceptor"),
    DATA_INSERTION("insertionService");

    private final String fieldName;


    Features(final String fieldName) {
        this.fieldName = fieldName;
    }
}
