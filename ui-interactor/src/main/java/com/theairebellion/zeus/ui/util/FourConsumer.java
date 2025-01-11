package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface FourConsumer<T, U, V, K> {

    void accept(T t, U u, V v, K k);

}
