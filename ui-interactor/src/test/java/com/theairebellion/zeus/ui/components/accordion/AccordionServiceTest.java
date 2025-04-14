package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionService;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("AccordionService test")
class AccordionServiceTest extends BaseUnitUITest {

   private MockAccordionService service;
   private SmartWebElement container;
   private By locator;
   private Strategy strategy;

   @BeforeEach
   void setUp() {
      service = new MockAccordionService();
      // Continue using MockSmartWebElement but initialize it properly
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      container = new MockSmartWebElement(webElement, driver);
      locator = By.id("test");
      strategy = Strategy.FIRST;
   }

   @Nested
   @DisplayName("Default expand operations")
   class DefaultExpandOperations {

      @Test
      @DisplayName("default expand with container and text")
      void defaultExpandWithContainerAndText() {
         // Given
         service.reset();

         // When
         service.expand(container, "Panel1", "Panel2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastAccordionText).containsExactly("Panel1", "Panel2");
      }

      @Test
      @DisplayName("default expand with container and strategy")
      void defaultExpandWithContainerAndStrategy() {
         // Given
         service.reset();

         // When
         String result = service.expand(container, strategy);

         // Then
         assertThat(result).isEqualTo(MockAccordionService.EXPAND_STRATEGY_RESULT);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("default expand with text only")
      void defaultExpandWithTextOnly() {
         // Given
         service.reset();

         // When
         service.expand("Panel1");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionText).containsExactly("Panel1");
      }

      @Test
      @DisplayName("default expand with By locator")
      void defaultExpandWithByLocator() {
         // Given
         service.reset();

         // When
         service.expand(locator);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionLocators).containsExactly(locator);
      }
   }

   @Nested
   @DisplayName("Default collapse operations")
   class DefaultCollapseOperations {

      @Test
      @DisplayName("default collapse with container and text")
      void defaultCollapseWithContainerAndText() {
         // Given
         service.reset();

         // When
         service.collapse(container, "Panel1");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastAccordionText).containsExactly("Panel1");
      }

      @Test
      @DisplayName("default collapse with container and strategy")
      void defaultCollapseWithContainerAndStrategy() {
         // Given
         service.reset();

         // When
         String result = service.collapse(container, strategy);

         // Then
         assertThat(result).isEqualTo(MockAccordionService.COLLAPSE_STRATEGY_RESULT);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("default collapse with text only")
      void defaultCollapseWithTextOnly() {
         // Given
         service.reset();

         // When
         service.collapse("Panel1", "Panel2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionText).containsExactly("Panel1", "Panel2");
      }

      @Test
      @DisplayName("default collapse with By locator")
      void defaultCollapseWithByLocator() {
         // Given
         service.reset();

         // When
         service.collapse(locator);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionLocators).containsExactly(locator);
      }
   }

   @Nested
   @DisplayName("Default areEnabled operations")
   class DefaultAreEnabledOperations {

      @Test
      @DisplayName("default areEnabled with container and text")
      void defaultAreEnabledWithContainerAndText() {
         // Given
         service.reset();

         // When
         boolean result = service.areEnabled(container, "Panel1", "Panel2");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastAccordionText).containsExactly("Panel1", "Panel2");
      }

      @Test
      @DisplayName("default areEnabled with text only")
      void defaultAreEnabledWithTextOnly() {
         // Given
         service.reset();

         // When
         boolean result = service.areEnabled("Panel1");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionText).containsExactly("Panel1");
      }

      @Test
      @DisplayName("default areEnabled with By locator")
      void defaultAreEnabledWithByLocator() {
         // Given
         service.reset();

         // When
         boolean result = service.areEnabled(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionLocators).containsExactly(locator);
      }
   }

   @Nested
   @DisplayName("Default getter operations")
   class DefaultGetterOperations {

      @Test
      @DisplayName("default getExpanded returns list of expanded panels")
      void defaultGetExpanded() {
         // Given
         service.reset();

         // When
         List<String> list = service.getExpanded(container);

         // Then
         assertThat(list).isEqualTo(MockAccordionService.EXPANDED_LIST);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("default getCollapsed returns list of collapsed panels")
      void defaultGetCollapsed() {
         // Given
         service.reset();

         // When
         List<String> list = service.getCollapsed(container);

         // Then
         assertThat(list).isEqualTo(MockAccordionService.COLLAPSED_LIST);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("default getAll returns list of all panels")
      void defaultGetAll() {
         // Given
         service.reset();

         // When
         List<String> list = service.getAll(container);

         // Then
         assertThat(list).isEqualTo(MockAccordionService.ALL_LIST);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("default getTitle returns panel title")
      void defaultGetTitle() {
         // Given
         service.reset();

         // When
         String title = service.getTitle(locator);

         // Then
         assertThat(title).isEqualTo(MockAccordionService.TITLE);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionLocators).containsExactly(locator);
      }

      @Test
      @DisplayName("default getText returns panel text")
      void defaultGetText() {
         // Given
         service.reset();

         // When
         String text = service.getText(locator);

         // Then
         assertThat(text).isEqualTo(MockAccordionService.TEXT);
         assertThat(service.lastComponentType).isEqualTo(MockAccordionComponentType.DUMMY);
         assertThat(service.lastAccordionLocators).containsExactly(locator);
      }
   }
}