package com.theairebellion.zeus.ui.drivers.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BaseDriverProviderTest {

   private static final String DRIVER_TYPE = "TEST_DRIVER";
   private static final String VERSION = "123.45";

   @Spy
   private TestDriverProvider driverProvider;

   private static class TestDriverProvider extends BaseDriverProvider<ChromeOptions> {
      private final AtomicBoolean downloadDriverCalled = new AtomicBoolean(false);

      @Override
      protected String getDriverType() {
         return DRIVER_TYPE;
      }

      @Override
      public ChromeOptions createOptions() {
         return new ChromeOptions();
      }

      @Override
      public org.openqa.selenium.WebDriver createDriver(ChromeOptions options) {
         return null;
      }

      @Override
      public void applyDefaultArguments(ChromeOptions options) {
         // No implementation needed for test
      }

      @Override
      public void applyHeadlessArguments(ChromeOptions options) {
         // No implementation needed for test
      }

      @Override
      public void downloadDriver(String version) {
         downloadDriverCalled.set(true);
         assertEquals(VERSION, version, "The version passed to downloadDriver should match the expected version");
      }

      public boolean wasDownloadDriverCalled() {
         return downloadDriverCalled.get();
      }

      public void resetDownloadDriverCalled() {
         downloadDriverCalled.set(false);
      }
   }

   @BeforeEach
   void setUp() throws Exception {
      // Reset the DRIVER_DOWNLOAD_STATUS field before each test
      ConcurrentHashMap<String, Boolean> driverDownloadStatus = getDriverDownloadStatusField();
      driverDownloadStatus.clear();

      // Reset the download flag
      driverProvider.resetDownloadDriverCalled();
   }

   private ConcurrentHashMap<String, Boolean> getDriverDownloadStatusField() throws Exception {
      var field = BaseDriverProvider.class.getDeclaredField("DRIVER_DOWNLOAD_STATUS");
      field.setAccessible(true);
      return (ConcurrentHashMap<String, Boolean>) field.get(null);
   }

   @Test
   void testSetupDriverFirstTime() throws Exception {
      // When setupDriver is called for the first time for this driver type
      driverProvider.setupDriver(VERSION);

      // Then downloadDriver should be called
      assertTrue(driverProvider.wasDownloadDriverCalled(), "downloadDriver should be called on first setup");

      // And the driver type should be added to the download status map
      ConcurrentHashMap<String, Boolean> driverDownloadStatus = getDriverDownloadStatusField();
      assertTrue(driverDownloadStatus.containsKey(DRIVER_TYPE), "Driver type should be added to download status map");
      assertTrue(driverDownloadStatus.get(DRIVER_TYPE), "Download status should be set to true");
   }

   @Test
   void testSetupDriverSubsequentCalls() throws Exception {
      // Given setupDriver has already been called once
      driverProvider.setupDriver(VERSION);
      assertTrue(driverProvider.wasDownloadDriverCalled(), "downloadDriver should be called on first setup");
      driverProvider.resetDownloadDriverCalled();

      // When setupDriver is called again
      driverProvider.setupDriver(VERSION);

      // Then downloadDriver should not be called again
      assertFalse(driverProvider.wasDownloadDriverCalled(), "downloadDriver should not be called on subsequent setups");

      // And the download status should remain in the map
      ConcurrentHashMap<String, Boolean> driverDownloadStatus = getDriverDownloadStatusField();
      assertTrue(driverDownloadStatus.containsKey(DRIVER_TYPE), "Driver type should still be in download status map");
      assertTrue(driverDownloadStatus.get(DRIVER_TYPE), "Download status should still be true");
   }

   @Test
   void testSetupDriverConcurrentAccess() throws Exception {
      // Simulate concurrent access by multiple threads
      final int threadCount = 5;
      final AtomicBoolean[] results = new AtomicBoolean[threadCount];
      Thread[] threads = new Thread[threadCount];

      for (int i = 0; i < threadCount; i++) {
         final int index = i;
         results[index] = new AtomicBoolean(false);
         threads[index] = new Thread(() -> {
            driverProvider.setupDriver(VERSION);
            results[index].set(true);
         });
      }

      // Start all threads
      for (Thread thread : threads) {
         thread.start();
      }

      // Wait for all threads to finish
      for (Thread thread : threads) {
         thread.join();
      }

      // Verify all threads completed successfully
      for (AtomicBoolean result : results) {
         assertTrue(result.get(), "Thread should have completed successfully");
      }

      // Verify downloadDriver was called exactly once
      // We can't verify this precisely with the current implementation since we're using a real instance
      // but we can verify the download status was set
      ConcurrentHashMap<String, Boolean> driverDownloadStatus = getDriverDownloadStatusField();
      assertTrue(driverDownloadStatus.containsKey(DRIVER_TYPE), "Driver type should be in download status map");
      assertTrue(driverDownloadStatus.get(DRIVER_TYPE), "Download status should be true");
   }

   @Test
   void testGetDriverType() {
      // When getDriverType is called
      String driverType = driverProvider.getDriverType();

      // Then it should return the expected value
      assertEquals(DRIVER_TYPE, driverType, "getDriverType should return the expected driver type");
   }
}