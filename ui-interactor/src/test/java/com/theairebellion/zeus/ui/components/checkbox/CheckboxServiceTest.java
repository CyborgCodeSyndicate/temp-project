package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("CheckboxService Test")
class CheckboxServiceTest extends BaseUnitUITest {

    private MockCheckboxService service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;

    private static final String[] CHECKBOX_TEXT_ARRAY = {"A", "B"};
    private static final String SINGLE_CHECKBOX_TEXT = "A";


    @BeforeEach
    void setUp() {
        // Given
        service = new MockCheckboxService();
        container = MockSmartWebElement.createMock();
        locator = By.id("testCheckbox");
        strategy = Strategy.FIRST;
        service.reset();
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - Select")
    class DefaultMethodDelegationSelectTests {

        @Test
        @DisplayName("select with container and text delegates to implementation")
        void testDefaultSelectWithContainerAndText() {
            // Given - setup in @BeforeEach

            // When
            service.select(container, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(CHECKBOX_TEXT_ARRAY);
        }

        @Test
        @DisplayName("select with container and strategy delegates to implementation")
        void testDefaultSelectWithContainerAndStrategy() {
            // Given - setup in @BeforeEach

            // When
            var result = service.select(container, strategy);

            // Then
            assertThat(result).isEqualTo("selectStrategyMock");
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("select with text only delegates to implementation")
        void testDefaultSelectWithTextOnly() {
            // Given - setup in @BeforeEach

            // When
            service.select(SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("select with locator delegates to implementation")
        void testDefaultSelectWithLocator() {
            // Given - setup in @BeforeEach

            // When
            service.select(locator);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - DeSelect")
    class DefaultMethodDelegationDeSelectTests {

        @Test
        @DisplayName("deSelect with container and text delegates to implementation")
        void testDefaultDeSelectWithContainerAndText() {
            // Given - setup in @BeforeEach

            // When
            service.deSelect(container, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(CHECKBOX_TEXT_ARRAY);
        }

        @Test
        @DisplayName("deSelect with container and strategy delegates to implementation")
        void testDefaultDeSelectWithContainerAndStrategy() {
            // Given - setup in @BeforeEach

            // When
            var result = service.deSelect(container, strategy);

            // Then
            assertThat(result).isEqualTo("deSelectStrategyMock");
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("deSelect with text only delegates to implementation")
        void testDefaultDeSelectWithTextOnly() {
            // Given - setup in @BeforeEach

            // When
            service.deSelect(SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("deSelect with locator delegates to implementation")
        void testDefaultDeSelectWithLocator() {
            // Given - setup in @BeforeEach

            // When
            service.deSelect(locator);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - AreSelected")
    class DefaultMethodDelegationAreSelectedTests {

        @Test
        @DisplayName("areSelected with container and text delegates to implementation")
        void testDefaultAreSelectedWithContainerAndText() {
            // Given
            service.returnSelected = true;

            // When
            var selected = service.areSelected(container, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(CHECKBOX_TEXT_ARRAY);
        }

        @Test
        @DisplayName("areSelected with text only delegates to implementation")
        void testDefaultAreSelectedWithTextOnly() {
            // Given
            service.returnSelected = true;

            // When
            var selected = service.areSelected(SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("areSelected with locator delegates to implementation")
        void testDefaultAreSelectedWithLocator() {
            // Given
            service.returnSelected = true;

            // When
            var selected = service.areSelected(locator);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsSelected")
    class DefaultMethodDelegationIsSelectedTests {

        @Test
        @DisplayName("isSelected with container delegates to implementation")
        void testDefaultIsSelectedWithContainer() {
            // Given
            service.returnSelected = true;

            // When
            var selected = service.isSelected(container, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("isSelected with text only delegates to implementation")
        void testDefaultIsSelectedWithTextOnly() {
            // Given
            service.returnSelected = true;

            // When
            var selected = service.isSelected(SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("isSelected with locator delegates to implementation")
        void testDefaultIsSelectedWithLocator() {
            // Given
            service.returnSelected = true;

            // When
            var selected = service.isSelected(locator);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - AreEnabled")
    class DefaultMethodDelegationAreEnabledTests {

        @Test
        @DisplayName("areEnabled with container and text delegates to implementation")
        void testDefaultAreEnabledWithContainerAndText() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.areEnabled(container, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(CHECKBOX_TEXT_ARRAY);
        }

        @Test
        @DisplayName("areEnabled with text only delegates to implementation")
        void testDefaultAreEnabledWithTextOnly() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.areEnabled(SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("areEnabled with locator delegates to implementation")
        void testDefaultAreEnabledWithLocator() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.areEnabled(locator);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsEnabled")
    class DefaultMethodDelegationIsEnabledTests {

        @Test
        @DisplayName("isEnabled with container delegates to implementation")
        void testDefaultIsEnabledWithContainer() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.isEnabled(container, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("isEnabled with text only delegates to implementation")
        void testDefaultIsEnabledWithTextOnly() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.isEnabled(SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{SINGLE_CHECKBOX_TEXT});
        }

        @Test
        @DisplayName("isEnabled with locator delegates to implementation")
        void testDefaultIsEnabledWithLocator() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.isEnabled(locator);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - Get Methods")
    class DefaultMethodDelegationGetMethodsTests {

        @Test
        @DisplayName("getSelected with container delegates to implementation")
        void testDefaultGetSelectedWithContainer() {
            // Given
            var expectedList = List.of("A");
            service.returnSelectedList = expectedList;

            // When
            var result = service.getSelected(container);

            // Then
            assertThat(result).isEqualTo(expectedList);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getSelected with locator delegates to implementation")
        void testDefaultGetSelectedWithLocator() {
            // Given
            var expectedList = List.of("X", "Y");
            service.returnSelectedList = expectedList;

            // When
            var result = service.getSelected(locator);

            // Then
            assertThat(result).isEqualTo(expectedList);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }

        @Test
        @DisplayName("getAll with container delegates to implementation")
        void testDefaultGetAllWithContainer() {
            // Given
            var expectedList = List.of("A", "B", "C");
            service.returnAllList = expectedList;

            // When
            var result = service.getAll(container);

            // Then
            assertThat(result).isEqualTo(expectedList);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getAll with locator delegates to implementation")
        void testDefaultGetAllWithLocator() {
            // Given
            var expectedList = List.of("D", "E");
            service.returnAllList = expectedList;

            // When
            var result = service.getAll(locator);

            // Then
            assertThat(result).isEqualTo(expectedList);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.explicitComponentType).isEqualTo(MockCheckboxComponentType.DUMMY_CHECKBOX);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }
}