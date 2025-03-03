package com.theairebellion.zeus.ui.util;

@FunctionalInterface
public interface FourFunction <T, U, V, K, R> {

    R apply(T t, U u, K k, V v);
}
