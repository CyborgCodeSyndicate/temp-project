package com.theairebellion.zeus.framework.extension.mock;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.extension.InvocationInterceptor;

public class MockInvocation implements InvocationInterceptor.Invocation<Void> {

   public final AtomicBoolean proceeded = new AtomicBoolean(false);

   @Override
   public Void proceed() throws Throwable {
      proceeded.set(true);
      return null;
   }
}