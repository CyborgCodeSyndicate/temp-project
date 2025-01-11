package com.theairebellion.zeus.ui.util;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, K> {

    void accept(T t, U u, K k);

    @SuppressWarnings("unused")
    default TriConsumer<T, U, K> andThen(TriConsumer<? super T, ? super U, ? super K> after) {
        Objects.requireNonNull(after);

        return (l, r, m) -> {
            accept(l, r, m);
            after.accept(l, r, m);
        };
    }
}
