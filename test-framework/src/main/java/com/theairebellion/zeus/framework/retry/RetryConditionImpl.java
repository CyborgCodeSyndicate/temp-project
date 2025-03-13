package com.theairebellion.zeus.framework.retry;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A concrete implementation of {@link RetryCondition} for flexible retry logic.
 * <p>
 * This class defines how a particular retry condition is formed by pairing
 * a {@link Function} (producing the value to be tested) with a {@link Predicate}
 * (evaluating whether the produced value meets the desired condition).
 * </p>
 *
 * <p>
 * Instances of this class are typically created with references to the actual
 * service or context that will be invoked repeatedly, along with the success
 * criteria that must be satisfied for the retry to end.
 * </p>
 *
 * <p>
 * @param <T> The type of value produced by the {@code function} and evaluated by the {@code condition}.
 * </p>
 *
 * <p>
 * @author Cyborg Code Syndicate
 * </p>
 */
public class RetryConditionImpl<T> implements RetryCondition<T> {

    private final Function<Object, T> function;
    private final Predicate<T> condition;

    /**
     * Constructs a new {@code RetryConditionImpl} with the specified function and condition.
     *
     * @param function  The function that produces a value of type {@code T} for evaluation.
     * @param condition The predicate that tests the produced value for success.
     */
    public RetryConditionImpl(final Function<Object, T> function, final Predicate<T> condition) {
        this.function = function;
        this.condition = condition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Object, T> function() {
        return function;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate<T> condition() {
        return condition;
    }

}
