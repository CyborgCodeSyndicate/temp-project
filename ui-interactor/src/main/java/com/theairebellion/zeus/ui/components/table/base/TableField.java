package com.theairebellion.zeus.ui.components.table.base;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface TableField<T> {

    void invoke(T instance, Object o) throws IllegalAccessException, InvocationTargetException;

    static <T, P> TableField<T> of(BiConsumer<T, P> consumer) {
        return (t, obj) -> consumer.accept(t, (P) obj);
    }

}