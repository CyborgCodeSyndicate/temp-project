package com.theairebellion.zeus.framework.parameters;

@FunctionalInterface
public interface Late<T> {

    T join();

}
