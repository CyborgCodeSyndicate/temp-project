package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("RadioService Interface Tests")
class RadioServiceTest extends BaseUnitUITest {

    private MockRadioService service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;

    private static final MockRadioComponentType DEFAULT_TYPE = MockRadioComponentType.DUMMY_RADIO;
    private static final String RADIO_TEXT = "RadioOption";
    private static final String SELECTED_TEXT = "SelectedRadio";
    private static final List<String> ALL_OPTIONS_LIST = List.of("Opt1", "Opt2");


    @BeforeEach
    void setUp() {
        // Given
        service = new MockRadioService();
        container = MockSmartWebElement.createMock();
        locator = By.id("testRadio");
        strategy = Strategy.FIRST;
        service.reset();
    }

    @Nested
    @DisplayName("Select Default Methods")
    class SelectDefaultMethodsTests {

        @Test
        @DisplayName("select(container, radioButtonText) delegates correctly")
        void selectContainerText() {
            // Given - setup in @BeforeEach

            // When
            service.select(container, RADIO_TEXT);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("select(container, strategy) delegates correctly")
        void selectContainerStrategy() {
            // Given
            service.returnSelected = SELECTED_TEXT;

            // When
            var result = service.select(container, strategy);

            // Then
            assertThat(result).isEqualTo(SELECTED_TEXT);
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("select(radioButtonText) delegates correctly")
        void selectRadioButtonText() {
            // Given - setup in @BeforeEach

            // When
            service.select(RADIO_TEXT);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("select(radioButtonLocator) delegates correctly")
        void selectRadioButtonLocator() {
            // Given - setup in @BeforeEach

            // When
            service.select(locator);

            // Then
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }

    @Nested
    @DisplayName("IsEnabled Default Methods")
    class IsEnabledDefaultMethodsTests {

        @Test
        @DisplayName("isEnabled(container, radioButtonText) delegates correctly")
        void isEnabledContainerText() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isEnabled(container, RADIO_TEXT);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("isEnabled(radioButtonText) delegates correctly")
        void isEnabledRadioButtonText() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isEnabled(RADIO_TEXT);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("isEnabled(radioButtonLocator) delegates correctly")
        void isEnabledRadioButtonLocator() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isEnabled(locator);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }

    @Nested
    @DisplayName("IsSelected Default Methods")
    class IsSelectedDefaultMethodsTests {

        @Test
        @DisplayName("isSelected(container, radioButtonText) delegates correctly")
        void isSelectedContainerText() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isSelected(container, RADIO_TEXT);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("isSelected(radioButtonText) delegates correctly")
        void isSelectedRadioButtonText() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isSelected(RADIO_TEXT);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("isSelected(radioButtonLocator) delegates correctly")
        void isSelectedRadioButtonLocator() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isSelected(locator);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }

    @Nested
    @DisplayName("IsVisible Default Methods")
    class IsVisibleDefaultMethodsTests {

        @Test
        @DisplayName("isVisible(container, radioButtonText) delegates correctly")
        void isVisibleContainerText() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isVisible(container, RADIO_TEXT);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("isVisible(radioButtonText) delegates correctly")
        void isVisibleRadioButtonText() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isVisible(RADIO_TEXT);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastText).isEqualTo(RADIO_TEXT);
        }

        @Test
        @DisplayName("isVisible(radioButtonLocator) delegates correctly")
        void isVisibleRadioButtonLocator() {
            // Given
            service.returnBool = true;

            // When
            var result = service.isVisible(locator);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }

    @Nested
    @DisplayName("Get Methods Default Delegation")
    class GetMethodsDefaultTests {

        @Test
        @DisplayName("getSelected(container) delegates correctly")
        void getSelectedContainer() {
            // Given
            service.returnSelected = SELECTED_TEXT;

            // When
            var result = service.getSelected(container);

            // Then
            assertThat(result).isEqualTo(SELECTED_TEXT);
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getSelected(containerLocator) delegates correctly")
        void getSelectedContainerLocator() {
            // Given
            service.returnSelected = SELECTED_TEXT;

            // When
            var result = service.getSelected(locator);

            // Then
            assertThat(result).isEqualTo(SELECTED_TEXT);
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastLocator).isEqualTo(locator);
        }

        @Test
        @DisplayName("getAll(container) delegates correctly")
        void getAllContainer() {
            // Given
            service.returnAll = ALL_OPTIONS_LIST;

            // When
            var result = service.getAll(container);

            // Then
            assertThat(result).isEqualTo(ALL_OPTIONS_LIST);
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getAll(containerLocator) delegates correctly")
        void getAllContainerLocator() {
            // Given
            service.returnAll = ALL_OPTIONS_LIST;

            // When
            var result = service.getAll(locator);

            // Then
            assertThat(result).isEqualTo(ALL_OPTIONS_LIST);
            assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
            assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }
}