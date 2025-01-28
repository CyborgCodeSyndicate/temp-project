package com.petstore.test.framework;

public enum JsonPathLocatorsApi {

    PET_ID_BY_NAME("$.pet[?(@.name == '%s')].id");

    private final String locator;

    JsonPathLocatorsApi(final String locator) {
        this.locator = locator;
    }

    public String format(String... args) {
        return String.format(locator, args);
    }
}
