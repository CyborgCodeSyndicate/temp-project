package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface TwoFunction<T, U, V> {

    V apply(T t, U u);


}