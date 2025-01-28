package com.theairebellion.zeus.util.reflections.mock;

public class TestClassWithPrivateField {
    private String hiddenField = "secret";

    public String getHiddenField() {
        return hiddenField;
    }

    public void setHiddenField(String hiddenField) {
        this.hiddenField = hiddenField;
    }
}
