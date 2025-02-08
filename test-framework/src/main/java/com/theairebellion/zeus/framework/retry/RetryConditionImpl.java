package com.theairebellion.zeus.framework.retry;

import java.util.function.Function;
import java.util.function.Predicate;

public class RetryConditionImpl<T> implements RetryCondition<T> {

    private final Function<Object, T> function;
    private final Predicate<T> condition;


    public RetryConditionImpl(final Function<Object, T> function, final Predicate<T> condition) {
        this.function = function;
        this.condition = condition;
    }


    @Override
    public Function<Object, T> function() {
        return function;
    }


    @Override
    public Predicate<T> condition() {
        return condition;
    }

}
