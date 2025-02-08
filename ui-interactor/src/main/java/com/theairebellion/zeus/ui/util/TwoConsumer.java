package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface TwoConsumer<T, U> {

    void accept(T t, U u);

}