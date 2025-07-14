package com.theairebellion.zeus.ui.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogUiTest {

   @Test
   void shouldCallTraceLog() {
      // Don't use mockStatic for this test
      LogUi.trace("Test message", "arg1", "arg2");
      // Coverage test only
   }

   @Test
   void shouldCallValidationLog() {
      // Don't use mockStatic for this test
      LogUi.validation("Test message", "arg1", "arg2");
      // Coverage test only
   }
}