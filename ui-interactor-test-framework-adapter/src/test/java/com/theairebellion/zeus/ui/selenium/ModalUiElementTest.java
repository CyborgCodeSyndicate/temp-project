package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.modal.ModalComponentType;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.service.fluent.mock.MockModalComponentType;
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

class ModalUiElementTest {

   static class DummyModalElement implements ModalUiElement {
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
         when(mockConfig.modalDefaultType()).thenReturn("DUMMY");
         when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");

         configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         // Mock ReflectionUtil.findEnumImplementationsOfInterface
         reflectionUtilMock.when(() ->
               ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(ModalComponentType.class),
                     anyString(),
                     anyString()
               )
         ).thenReturn(MockModalComponentType.DUMMY);

         // Create and test the dummy element
         ModalUiElement element = new DummyModalElement();
         ComponentType result = element.componentType();

         assertNotNull(result);
         assertInstanceOf(ModalComponentType.class, result);
         assertEquals(MockModalComponentType.DUMMY, result);
         assertEquals(MockModalComponentType.DUMMY, result.getType());
      }
   }
}