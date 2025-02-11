package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface BiConsumer<T, U> {

    void accept(T t, U u);

}