package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface BiFunction<T, U, V> {

    V apply(T t, U u);


}