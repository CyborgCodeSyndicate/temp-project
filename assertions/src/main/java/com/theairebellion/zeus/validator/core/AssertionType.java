package com.theairebellion.zeus.validator.core;

public interface AssertionType {

    Enum<?> type();

    Class<?> getSupportedType();

}
