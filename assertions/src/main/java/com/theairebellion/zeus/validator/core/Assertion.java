package com.theairebellion.zeus.validator.core;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Assertion<T> {

    private final AssertionTarget target;
    @Setter
    private String key;
    private final AssertionType type;
    private final T expected;
    private final boolean soft;

}