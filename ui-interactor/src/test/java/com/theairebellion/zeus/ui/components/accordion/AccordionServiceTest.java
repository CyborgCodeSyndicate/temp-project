package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccordionService Test")
class AccordionServiceTest extends BaseUnitUITest {

    private MockAccordionService service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        // Given
        service = new MockAccordionService();
        container = MockSmartWebElement.createMock();
        locator = By.id("test");
        strategy = Strategy.FIRST;
        service.reset();
    }

    @Nested
    @DisplayName("Default Expand Operations")
    class DefaultExpandOperations {

        @Test
        @DisplayName("default expand with container and text")
        void defaultExpandWithContainerAndText() {
            // Given - service and container are set up

            // When
            service.expand(container, "Panel1", "Panel2");

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastAccordionText).containsExactly("Panel1", "Panel2");
        }

        @Test
        @DisplayName("default expand with container and strategy")
        void defaultExpandWithContainerAndStrategy() {
            // Given - service, container, strategy are set up

            // When
            var result = service.expand(container, strategy);

            // Then
            assertThat(result).isEqualTo(MockAccordionService.EXPAND_STRATEGY_RESULT);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("default expand with text only")
        void defaultExpandWithTextOnly() {
            // Given - service is set up

            // When
            service.expand("Panel1");

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionText).containsExactly("Panel1");
        }

        @Test
        @DisplayName("default expand with By locator")
        void defaultExpandWithByLocator() {
            // Given - service and locator are set up

            // When
            service.expand(locator);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionLocators).containsExactly(locator);
        }
    }

    @Nested
    @DisplayName("Default Collapse Operations")
    class DefaultCollapseOperations {

        @Test
        @DisplayName("default collapse with container and text")
        void defaultCollapseWithContainerAndText() {
            // Given - setup in @BeforeEach

            // When
            service.collapse(container, "Panel1");

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastAccordionText).containsExactly("Panel1");
        }

        @Test
        @DisplayName("default collapse with container and strategy")
        void defaultCollapseWithContainerAndStrategy() {
            // Given - setup in @BeforeEach

            // When
            var result = service.collapse(container, strategy);

            // Then
            assertThat(result).isEqualTo(MockAccordionService.COLLAPSE_STRATEGY_RESULT);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("default collapse with text only")
        void defaultCollapseWithTextOnly() {
            // Given - setup in @BeforeEach

            // When
            service.collapse("Panel1", "Panel2");

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionText).containsExactly("Panel1", "Panel2");
        }

        @Test
        @DisplayName("default collapse with By locator")
        void defaultCollapseWithByLocator() {
            // Given - setup in @BeforeEach

            // When
            service.collapse(locator);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionLocators).containsExactly(locator);
        }
    }

    @Nested
    @DisplayName("Default areEnabled Operations")
    class DefaultAreEnabledOperations {

        @Test
        @DisplayName("default areEnabled with container and text")
        void defaultAreEnabledWithContainerAndText() {
            // Given - setup in @BeforeEach

            // When
            var result = service.areEnabled(container, "Panel1", "Panel2");

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastAccordionText).containsExactly("Panel1", "Panel2");
        }

        @Test
        @DisplayName("default areEnabled with text only")
        void defaultAreEnabledWithTextOnly() {
            // Given - setup in @BeforeEach

            // When
            var result = service.areEnabled("Panel1");

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionText).containsExactly("Panel1");
        }

        @Test
        @DisplayName("default areEnabled with By locator")
        void defaultAreEnabledWithByLocator() {
            // Given - setup in @BeforeEach

            // When
            var result = service.areEnabled(locator);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionLocators).containsExactly(locator);
        }
    }

    @Nested
    @DisplayName("Default Getter Operations")
    class DefaultGetterOperations {

        @Test
        @DisplayName("default getExpanded returns list of expanded panels")
        void defaultGetExpanded() {
            // Given - setup in @BeforeEach

            // When
            var list = service.getExpanded(container);

            // Then
            assertThat(list).isEqualTo(MockAccordionService.EXPANDED_LIST);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("default getCollapsed returns list of collapsed panels")
        void defaultGetCollapsed() {
            // Given - setup in @BeforeEach

            // When
            var list = service.getCollapsed(container);

            // Then
            assertThat(list).isEqualTo(MockAccordionService.COLLAPSED_LIST);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("default getAll returns list of all panels")
        void defaultGetAll() {
            // Given - setup in @BeforeEach

            // When
            var list = service.getAll(container);

            // Then
            assertThat(list).isEqualTo(MockAccordionService.ALL_LIST);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("default getTitle returns panel title")
        void defaultGetTitle() {
            // Given - setup in @BeforeEach

            // When
            var title = service.getTitle(locator);

            // Then
            assertThat(title).isEqualTo(MockAccordionService.TITLE);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionLocators).containsExactly(locator);
        }

        @Test
        @DisplayName("default getText returns panel text")
        void defaultGetText() {
            // Given - setup in @BeforeEach

            // When
            var text = service.getText(locator);

            // Then
            assertThat(text).isEqualTo(MockAccordionService.TEXT);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.explicitComponentType).isEqualTo(MockAccordionComponentType.DUMMY_ACCORDION);
            assertThat(service.lastAccordionLocators).containsExactly(locator);
        }
    }
}