package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAccordionComponentType;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccordionUiElementTest {

   static class DummyAccordionElement implements AccordionUiElement {
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
   void testComponentTypeReturnsDefault() {
      try (
            MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class)
      ) {
         // Mock UiConfigHolder.getUiConfig()
         UiConfig mockConfig = mock(UiConfig.class);
         when(mockConfig.accordionDefaultType()).thenReturn("DUMMY");
         when(mockConfig.projectPackage()).thenReturn("com.theairebellion.zeus");

         configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         // Mock ReflectionUtil.findEnumImplementationsOfInterface
         reflectionUtilMock.when(() ->
               ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(AccordionComponentType.class),
                     anyString(),
                     anyString()
               )
         ).thenReturn(MockAccordionComponentType.DUMMY);

         // Create and test the dummy element
         AccordionUiElement element = new DummyAccordionElement();
         ComponentType result = element.componentType();

         assertNotNull(result);
         assertInstanceOf(AccordionComponentType.class, result);
         assertEquals(MockAccordionComponentType.DUMMY, result);
         assertEquals(MockAccordionComponentType.DUMMY, result.getType());
      }
   }
}