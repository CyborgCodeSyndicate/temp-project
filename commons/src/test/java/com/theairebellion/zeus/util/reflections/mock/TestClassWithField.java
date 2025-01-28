package com.theairebellion.zeus.util.reflections.mock;

public class TestClassWithField {
    private String hiddenField;

    public TestClassWithField(String hiddenField) {
        this.hiddenField = hiddenField;
    }

    public String getHiddenField() {
        return hiddenField;
    }

    public void setHiddenField(String hiddenField) {
        this.hiddenField = hiddenField;
    }
}
