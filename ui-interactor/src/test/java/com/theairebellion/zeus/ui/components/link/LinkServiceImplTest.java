package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("LinkServiceImpl Unit Tests")
class LinkServiceImplTest extends BaseUnitUITest {

   private SmartWebDriver driver;
   private LinkServiceImpl service;
   private SmartWebElement container;
   private SmartWebElement cellElement;
   private Link linkMock;
   private MockLinkComponentType componentType;
   private By locator;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      driver = mock(SmartWebDriver.class);
      service = new LinkServiceImpl(driver);
      container = MockSmartWebElement.createMock();
      cellElement = MockSmartWebElement.createMock();
      linkMock = mock(Link.class);
      componentType = MockLinkComponentType.DUMMY_LINK;
      locator = By.id("link");

      // Configure static mock for ComponentFactory
      factoryMock = mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getLinkComponent(eq(componentType), eq(driver)))
            .thenReturn(linkMock);
   }

   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }

   @Nested
   @DisplayName("Click Method Tests")
   class ClickMethodTests {

      @Test
      @DisplayName("click with container and text delegates correctly")
      void clickWithContainerAndText() {
         // Given
         var linkText = "LinkText";

         // When
         service.click(componentType, container, linkText);

         // Then
         verify(linkMock).click(container, linkText);
      }

      @Test
      @DisplayName("click with container delegates correctly")
      void clickWithContainer() {
         // When
         service.click(componentType, container);

         // Then
         verify(linkMock).click(container);
      }

      @Test
      @DisplayName("click with text only delegates correctly")
      void clickWithTextOnly() {
         // Given
         var linkText = "LinkText";

         // When
         service.click(componentType, linkText);

         // Then
         verify(linkMock).click(linkText);
      }

      @Test
      @DisplayName("click with locator delegates correctly")
      void clickWithLocator() {
         // When
         service.click(componentType, locator);

         // Then
         verify(linkMock).click(locator);
      }
   }

   @Nested
   @DisplayName("Double Click Method Tests")
   class DoubleClickMethodTests {

      @Test
      @DisplayName("doubleClick with container and text delegates correctly")
      void doubleClickWithContainerAndText() {
         // Given
         var linkText = "LinkText";

         // When
         service.doubleClick(componentType, container, linkText);

         // Then
         verify(linkMock).doubleClick(container, linkText);
      }

      @Test
      @DisplayName("doubleClick with container delegates correctly")
      void doubleClickWithContainer() {
         // When
         service.doubleClick(componentType, container);

         // Then
         verify(linkMock).doubleClick(container);
      }

      @Test
      @DisplayName("doubleClick with text only delegates correctly")
      void doubleClickWithTextOnly() {
         // Given
         var linkText = "LinkText";

         // When
         service.doubleClick(componentType, linkText);

         // Then
         verify(linkMock).doubleClick(linkText);
      }

      @Test
      @DisplayName("doubleClick with locator delegates correctly")
      void doubleClickWithLocator() {
         // When
         service.doubleClick(componentType, locator);

         // Then
         verify(linkMock).doubleClick(locator);
      }
   }

   @Nested
   @DisplayName("IsEnabled Method Tests")
   class IsEnabledMethodTests {

      @Test
      @DisplayName("isEnabled with container and text delegates correctly")
      void isEnabledWithContainerAndText() {
         // Given
         var linkText = "LinkText";
         when(linkMock.isEnabled(container, linkText)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, container, linkText);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isEnabled(container, linkText);
      }

      @Test
      @DisplayName("isEnabled with container delegates correctly")
      void isEnabledWithContainer() {
         // Given
         when(linkMock.isEnabled(container)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, container);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isEnabled(container);
      }

      @Test
      @DisplayName("isEnabled with text only delegates correctly")
      void isEnabledWithTextOnly() {
         // Given
         var linkText = "LinkText";
         when(linkMock.isEnabled(linkText)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, linkText);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isEnabled(linkText);
      }

      @Test
      @DisplayName("isEnabled with locator delegates correctly")
      void isEnabledWithLocator() {
         // Given
         when(linkMock.isEnabled(locator)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isEnabled(locator);
      }
   }

   @Nested
   @DisplayName("IsVisible Method Tests")
   class IsVisibleMethodTests {

      @Test
      @DisplayName("isVisible with container and text delegates correctly")
      void isVisibleWithContainerAndText() {
         // Given
         var linkText = "LinkText";
         when(linkMock.isVisible(container, linkText)).thenReturn(true);

         // When
         var result = service.isVisible(componentType, container, linkText);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isVisible(container, linkText);
      }

      @Test
      @DisplayName("isVisible with container delegates correctly")
      void isVisibleWithContainer() {
         // Given
         when(linkMock.isVisible(container)).thenReturn(true);

         // When
         var result = service.isVisible(componentType, container);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isVisible(container);
      }

      @Test
      @DisplayName("isVisible with text only delegates correctly")
      void isVisibleWithTextOnly() {
         // Given
         var linkText = "LinkText";
         when(linkMock.isVisible(linkText)).thenReturn(true);

         // When
         var result = service.isVisible(componentType, linkText);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isVisible(linkText);
      }

      @Test
      @DisplayName("isVisible with locator delegates correctly")
      void isVisibleWithLocator() {
         // Given
         when(linkMock.isVisible(locator)).thenReturn(true);

         // When
         var result = service.isVisible(componentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(linkMock).isVisible(locator);
      }
   }

   @Test
   @DisplayName("tableInsertion delegates to clickElementInCell correctly")
   void tableInsertion() {
      // When
      service.tableInsertion(cellElement, componentType, "value1", "value2");

      // Then
      verify(linkMock).clickElementInCell(cellElement);
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused")
      void componentCaching() {
         // When
         service.click(componentType, container, "ClickMe");
         service.isEnabled(componentType, container, "EnableMe");
         service.isVisible(componentType, container, "VisibleMe");

         // Then
         factoryMock.verify(() -> ComponentFactory.getLinkComponent(eq(componentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Different component types create different instances")
      void differentComponentTypes() {
         // Setup mock component types
         var type1 = MockLinkComponentType.DUMMY_LINK;
         var type2 = MockLinkComponentType.TEST;

         // Create mock components
         var link1 = mock(Link.class);
         var link2 = mock(Link.class);

         // Configure behavior
         when(link1.isVisible(container)).thenReturn(false);
         when(link2.isVisible(container)).thenReturn(true);

         // Configure factory mock
         factoryMock.reset();
         factoryMock.when(() -> ComponentFactory.getLinkComponent(eq(type1), eq(driver)))
               .thenReturn(link1);
         factoryMock.when(() -> ComponentFactory.getLinkComponent(eq(type2), eq(driver)))
               .thenReturn(link2);

         // First component type operation
         var result1 = service.isVisible(type1, container);
         assertThat(result1).isFalse();
         verify(link1).isVisible(container);

         // Second component type operation
         var result2 = service.isVisible(type2, container);
         assertThat(result2).isTrue();
         verify(link2).isVisible(container);

         // Verify factory calls
         factoryMock.verify(() -> ComponentFactory.getLinkComponent(eq(type1), eq(driver)), times(1));
         factoryMock.verify(() -> ComponentFactory.getLinkComponent(eq(type2), eq(driver)), times(1));
      }
   }
}