package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.alert.AlertComponentType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAlertComponentType;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class AlertUiElementTest {

   static class DummyAlertElement implements AlertUiElement {
      @Override
      public By locator() {
         return By.id("dummy-id");
      }

      @Override
      public Enum<?> enumImpl() {
         return null;
      }
   }

   @Test
   void testComponentTypeReturnsDefaultType() {
      try (
            MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class)
      ) {
         // Mock UiConfigHolder.getUiConfig()
         UiConfig mockConfig = mock(UiConfig.class);
         when(mockConfig.alertDefaultType()).thenReturn("DUMMY");
         when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");

         configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         // Mock ReflectionUtil.findEnumImplementationsOfInterface
         reflectionUtilMock.when(() ->
               ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(AlertComponentType.class),
                     anyString(),
                     anyString()
               )
         ).thenReturn(MockAlertComponentType.DUMMY);

         // Create and test the dummy element
         AlertUiElement element = new DummyAlertElement();
         ComponentType result = element.componentType();

         assertNotNull(result);
         assertInstanceOf(AlertComponentType.class, result);
         assertEquals(MockAlertComponentType.DUMMY, result);
         assertEquals(MockAlertComponentType.DUMMY, result.getType());
      }
   }
}