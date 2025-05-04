package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;


import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("ButtonService Test")
class ButtonServiceTest extends BaseUnitUITest {

    private MockButtonService service;
    private SmartWebElement container;
    private By locator;
    private static final String BUTTON_TEXT = "ClickMe";

    @BeforeEach
    void setUp() {
        // Given
        service = new MockButtonService();
        container = MockSmartWebElement.createMock();
        locator = By.id("testButton");
        service.reset();
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - Click")
    class DefaultMethodDelegationClickTests {

        @Test
        @DisplayName("click with container and text delegates to implementation")
        void testDefaultClickWithContainerAndText() {
            // Given - setup in @BeforeEach

            // When
            service.click(container, BUTTON_TEXT);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT);
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("click with container delegates to implementation")
        void testDefaultClickWithContainer() {
            // Given - setup in @BeforeEach

            // When
            service.click(container);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isNull();
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("click with text delegates to implementation")
        void testDefaultClickWithString() {
            // Given - setup in @BeforeEach

            // When
            service.click(BUTTON_TEXT);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("click with locator delegates to implementation")
        void testDefaultClickWithLocator() {
            // Given - setup in @BeforeEach

            // When
            service.click(locator);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastButtonText).isNull();
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsEnabled")
    class DefaultMethodDelegationIsEnabledTests {

        @Test
        @DisplayName("isEnabled with container and text delegates to implementation")
        void testDefaultIsEnabledWithContainerAndText() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.isEnabled(container, BUTTON_TEXT);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT);
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("isEnabled with container delegates to implementation")
        void testDefaultIsEnabledWithContainer() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.isEnabled(container);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isNull();
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("isEnabled with text delegates to implementation")
        void testDefaultIsEnabledWithString() {
            // Given
            service.returnEnabled = true;

            // When
            var enabled = service.isEnabled(BUTTON_TEXT);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastLocator).isNull();
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
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastButtonText).isNull();
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsVisible")
    class DefaultMethodDelegationIsVisibleTests {

        @Test
        @DisplayName("isVisible with container and text delegates to implementation")
        void testDefaultIsVisibleWithContainerAndText() {
            // Given
            service.returnVisible = true;

            // When
            var visible = service.isVisible(container, BUTTON_TEXT);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT);
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("isVisible with container delegates to implementation")
        void testDefaultIsVisibleWithContainer() {
            // Given
            service.returnVisible = true;

            // When
            var visible = service.isVisible(container);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isNull();
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("isVisible with text delegates to implementation")
        void testDefaultIsVisibleWithString() {
            // Given
            service.returnVisible = true;

            // When
            var visible = service.isVisible(BUTTON_TEXT);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastLocator).isNull();
        }

        @Test
        @DisplayName("isVisible with locator delegates to implementation")
        void testDefaultIsVisibleWithLocator() {
            // Given
            service.returnVisible = true;

            // When
            var visible = service.isVisible(locator);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.explicitComponentType).isEqualTo(MockButtonComponentType.DUMMY_BUTTON);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastButtonText).isNull();
        }
    }
}