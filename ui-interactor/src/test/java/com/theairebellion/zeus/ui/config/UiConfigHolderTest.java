package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class UiConfigHolderTest extends BaseUnitUITest {

   @Test
   void testGetUiConfigSubsequentCalls() {
      UiConfig firstConfig = UiConfigHolder.getUiConfig();
      UiConfig secondConfig = UiConfigHolder.getUiConfig();

      // Verify same instance is returned
      assertSame(firstConfig, secondConfig,
            "Subsequent calls should return the same config instance");
   }
}