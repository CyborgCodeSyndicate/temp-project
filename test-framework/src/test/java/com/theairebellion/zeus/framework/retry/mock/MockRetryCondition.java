package com.theairebellion.zeus.framework.retry.mock;

import com.theairebellion.zeus.framework.retry.RetryCondition;
import java.util.function.Function;
import java.util.function.Predicate;

public class MockRetryCondition<T> implements RetryCondition<T> {

   private final Function<Object, T> function;
   private final Predicate<T> predicate;

   public MockRetryCondition(Function<Object, T> function, Predicate<T> predicate) {
      this.function = function;
      this.predicate = predicate;
   }

   @Override
   public Function<Object, T> function() {
      return function;
   }

   @Override
   public Predicate<T> condition() {
      return predicate;
   }
}
