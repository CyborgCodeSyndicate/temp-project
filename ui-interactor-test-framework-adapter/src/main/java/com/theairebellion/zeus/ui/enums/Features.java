package com.theairebellion.zeus.ui.enums;

import lombok.Getter;

public enum Features {

    INPUT_FIELDS("inputField"),
    REQUESTS_INTERCEPTOR("interceptor"),
    DATA_INSERTION("insertionService");

    @Getter
    private final String fieldName;


    Features(final String fieldName) {
        this.fieldName = fieldName;
    }
}
