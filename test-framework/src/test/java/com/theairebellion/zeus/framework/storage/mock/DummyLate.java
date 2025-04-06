package com.theairebellion.zeus.framework.storage.mock;

import com.theairebellion.zeus.framework.parameters.Late;

public class DummyLate<T> implements Late<T> {

    private final T value;

    public DummyLate(T value) {
        this.value = value;
    }

    @Override
    public T join() {
        return value;
    }
}
