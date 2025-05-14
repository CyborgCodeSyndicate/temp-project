package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UiConfigHolderConcurrencyTest extends BaseUnitUITest {

   @BeforeEach
   void setUp() {
      // Ensure clean state before each test
      ConfigCache.clear();
      // Use reflection to reset the static config field if possible
      try {
         java.lang.reflect.Field configField = UiConfigHolder.class.getDeclaredField("config");
         configField.setAccessible(true);
         java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
         modifiersField.setAccessible(true);
         modifiersField.setInt(configField, configField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
         configField.set(null, null);
      } catch (Exception e) {
         // Ignore if reset fails
      }
   }

   @Test
   void testConcurrentConfigCreation() throws InterruptedException {
      int threadCount = 10;
      ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
      CountDownLatch latch = new CountDownLatch(threadCount);
      AtomicReference<UiConfig> firstConfig = new AtomicReference<>();
      AtomicReference<Boolean> allConfigsEqual = new AtomicReference<>(true);

      for (int i = 0; i < threadCount; i++) {
         threadCount = i;
         executorService.submit(() -> {
            try {
               UiConfig config = UiConfigHolder.getUiConfig();

               // Ensure first config is captured
               if (firstConfig.compareAndSet(null, config)) {
                  // First thread sets the config
               } else {
                  // Subsequent threads compare with first config
                  if (firstConfig.get() != config) {
                     allConfigsEqual.set(false);
                  }
               }
            } finally {
               latch.countDown();
            }
         });
      }

      // Wait for all threads to complete
      latch.await(5, TimeUnit.SECONDS);
      executorService.shutdown();

      // Verify that all threads got the same config instance
      assertTrue(allConfigsEqual.get(), "Not all config instances were the same");
      assertNotNull(firstConfig.get(), "No config was created");
   }

   @Test
   void testConfigHolderSingleton() {
      UiConfig firstConfig = UiConfigHolder.getUiConfig();
      UiConfig secondConfig = UiConfigHolder.getUiConfig();

      // Ensure the same instance is returned
      assertSame(firstConfig, secondConfig, "Multiple calls should return the same config instance");
   }

   @Test
   void testConfigHolderThreadSafety() throws InterruptedException {
      int iterations = 1000;
      ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      CountDownLatch latch = new CountDownLatch(iterations);

      for (int i = 0; i < iterations; i++) {
         executorService.submit(() -> {
            try {
               UiConfig config = UiConfigHolder.getUiConfig();
               assertNotNull(config);
            } finally {
               latch.countDown();
            }
         });
      }

      // Wait for all threads to complete
      boolean completed = latch.await(10, TimeUnit.SECONDS);
      executorService.shutdown();

      assertTrue(completed, "Not all threads completed within the timeout");
   }
}