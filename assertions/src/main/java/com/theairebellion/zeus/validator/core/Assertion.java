package com.theairebellion.zeus.validator.core;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderMethodName = "hiddenBuilder")
public class Assertion<T> {

    private final AssertionTarget target;
    private final String key;
    private final AssertionType type;
    private final T expected;
    private final boolean soft;


    public static <T> AssertionBuilder<T> builder(Class<T> expectedType) {
        return hiddenBuilder();
    }

}
