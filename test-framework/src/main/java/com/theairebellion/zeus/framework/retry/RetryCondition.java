package com.theairebellion.zeus.framework.retry;

import java.util.function.Function;
import java.util.function.Predicate;

public interface RetryCondition<T> {

    Function<Object, T> function();

    Predicate<T> condition();


}
