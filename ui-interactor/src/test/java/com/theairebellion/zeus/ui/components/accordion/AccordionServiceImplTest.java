package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("AccordionServiceImpl Unit Test")
class AccordionServiceImplTest extends BaseUnitUITest {

   private final MockAccordionComponentType componentType = MockAccordionComponentType.DUMMY_ACCORDION;
   private final String[] sampleText = {"Panel1", "Panel2"};
   private final By sampleLocator = By.id("accordion");
   private final List<String> sampleList = Arrays.asList("Item1", "Item2");
   private final String sampleResult = "Result";
   private final String sampleTitle = "Title";
   private final String sampleContentText = "Text";
   @Mock
   private SmartWebDriver driver;
   @Mock
   private SmartWebElement container;
   @Mock
   private Accordion accordion;
   @Mock
   private Strategy strategy;
   private MockedStatic<ComponentFactory> factoryMock;
   private AccordionServiceImpl service;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      service = new AccordionServiceImpl(driver);

      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getAccordionComponent(eq(componentType), eq(driver)))
            .thenReturn(accordion);
   }

   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }

   @Test
   @DisplayName("should cache and reuse component instance")
   void componentIsCachedAndReused() {
      // Given - setup in @BeforeEach

      // When
      service.expand(componentType, container, sampleText);
      service.collapse(componentType, container, sampleText);
      service.areEnabled(componentType, container, sampleText);

      // Then
      factoryMock.verify(() -> ComponentFactory.getAccordionComponent(eq(componentType), eq(driver)), times(1));
      verify(accordion).expand(container, sampleText);
      verify(accordion).collapse(container, sampleText);
      verify(accordion).areEnabled(container, sampleText);
      verifyNoMoreInteractions(accordion);
   }

   @Nested
   @DisplayName("Expand Operations Delegation")
   class ExpandOperations {

      @Test
      @DisplayName("should delegate expand with container and text")
      void expandWithContainerAndText() {
         // Given - service and mocks are set up in @BeforeEach

         // When
         service.expand(componentType, container, sampleText);

         // Then
         verify(accordion).expand(container, sampleText);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate expand with container and strategy")
      void expandWithContainerAndStrategy() {
         // Given
         when(accordion.expand(container, strategy)).thenReturn(sampleResult);

         // When
         var result = service.expand(componentType, container, strategy);

         // Then
         assertThat(result).isEqualTo(sampleResult);
         verify(accordion).expand(container, strategy);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate expand with text only")
      void expandWithTextOnly() {
         // Given - setup in @BeforeEach

         // When
         service.expand(componentType, sampleText);

         // Then
         verify(accordion).expand(sampleText);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate expand with By locator")
      void expandWithByLocator() {
         // Given - setup in @BeforeEach

         // When
         service.expand(componentType, sampleLocator);

         // Then
         verify(accordion).expand(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }
   }

   @Nested
   @DisplayName("Collapse Operations Delegation")
   class CollapseOperations {

      @Test
      @DisplayName("should delegate collapse with container and text")
      void collapseWithContainerAndText() {
         // Given - setup in @BeforeEach

         // When
         service.collapse(componentType, container, sampleText);

         // Then
         verify(accordion).collapse(container, sampleText);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate collapse with container and strategy")
      void collapseWithContainerAndStrategy() {
         // Given
         when(accordion.collapse(container, strategy)).thenReturn(sampleResult);

         // When
         var result = service.collapse(componentType, container, strategy);

         // Then
         assertThat(result).isEqualTo(sampleResult);
         verify(accordion).collapse(container, strategy);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate collapse with text only")
      void collapseWithTextOnly() {
         // Given - setup in @BeforeEach

         // When
         service.collapse(componentType, sampleText);

         // Then
         verify(accordion).collapse(sampleText);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate collapse with By locator")
      void collapseWithByLocator() {
         // Given - setup in @BeforeEach

         // When
         service.collapse(componentType, sampleLocator);

         // Then
         verify(accordion).collapse(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }
   }

   @Nested
   @DisplayName("Status Check Operations Delegation")
   class StatusCheckOperations {

      @Test
      @DisplayName("should delegate areEnabled with container and text")
      void areEnabledWithContainerAndText() {
         // Given
         when(accordion.areEnabled(container, sampleText)).thenReturn(true);

         // When
         var result = service.areEnabled(componentType, container, sampleText);

         // Then
         assertThat(result).isTrue();
         verify(accordion).areEnabled(container, sampleText);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate areEnabled with text only")
      void areEnabledWithTextOnly() {
         // Given
         when(accordion.areEnabled(sampleText)).thenReturn(true);

         // When
         var result = service.areEnabled(componentType, sampleText);

         // Then
         assertThat(result).isTrue();
         verify(accordion).areEnabled(sampleText);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate areEnabled with By locator")
      void areEnabledWithByLocator() {
         // Given
         when(accordion.areEnabled(sampleLocator)).thenReturn(true);

         // When
         var result = service.areEnabled(componentType, sampleLocator);

         // Then
         assertThat(result).isTrue();
         verify(accordion).areEnabled(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate isEnabled with By locator")
      void isEnabledWithByLocator() {
         // Given
         when(accordion.areEnabled(sampleLocator)).thenReturn(true);

         // When
         boolean result = service.isEnabled(componentType, sampleLocator);

         // Then
         assertThat(result).isTrue();
         verify(accordion).areEnabled(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }
   }

   @Nested
   @DisplayName("Get Information Operations Delegation")
   class GetAccordionInformation {

      @Test
      @DisplayName("should delegate getExpanded")
      void getExpanded() {
         // Given
         when(accordion.getExpanded(container)).thenReturn(sampleList);

         // When
         var result = service.getExpanded(componentType, container);

         // Then
         assertThat(result).isEqualTo(sampleList);
         verify(accordion).getExpanded(container);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getExpanded by locator")
      void getExpandedByLocator() {
         // Given
         when(accordion.getExpanded(sampleLocator)).thenReturn(sampleList);

         // When
         var result = service.getExpanded(componentType, sampleLocator);

         // Then
         assertThat(result).isEqualTo(sampleList);
         verify(accordion).getExpanded(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getCollapsed")
      void getCollapsed() {
         // Given
         when(accordion.getCollapsed(container)).thenReturn(sampleList);

         // When
         var result = service.getCollapsed(componentType, container);

         // Then
         assertThat(result).isEqualTo(sampleList);
         verify(accordion).getCollapsed(container);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getCollapsed by locator")
      void getCollapsedByLocator() {
         // Given
         when(accordion.getCollapsed(sampleLocator)).thenReturn(sampleList);

         // When
         var result = service.getCollapsed(componentType, sampleLocator);

         // Then
         assertThat(result).isEqualTo(sampleList);
         verify(accordion).getCollapsed(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getAll")
      void getAll() {
         // Given
         when(accordion.getAll(container)).thenReturn(sampleList);

         // When
         var result = service.getAll(componentType, container);

         // Then
         assertThat(result).isEqualTo(sampleList);
         verify(accordion).getAll(container);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getAll by locator")
      void getAllByLocator() {
         // Given
         when(accordion.getAll(sampleLocator)).thenReturn(sampleList);

         // When
         var result = service.getAll(componentType, sampleLocator);

         // Then
         assertThat(result).isEqualTo(sampleList);
         verify(accordion).getAll(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getTitle")
      void getTitle() {
         // Given
         when(accordion.getTitle(sampleLocator)).thenReturn(sampleTitle);

         // When
         var result = service.getTitle(componentType, sampleLocator);

         // Then
         assertThat(result).isEqualTo(sampleTitle);
         verify(accordion).getTitle(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }

      @Test
      @DisplayName("should delegate getText")
      void getText() {
         // Given
         when(accordion.getText(sampleLocator)).thenReturn(sampleContentText);

         // When
         var result = service.getText(componentType, sampleLocator);

         // Then
         assertThat(result).isEqualTo(sampleContentText);
         verify(accordion).getText(sampleLocator);
         verifyNoMoreInteractions(accordion);
      }
   }
}