package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface TriConsumer<T, U, K> {

    void accept(T t, U u, K k);

}
