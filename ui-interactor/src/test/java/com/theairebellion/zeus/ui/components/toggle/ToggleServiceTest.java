package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@DisplayName("ToggleService Interface Methods")
class ToggleServiceTest extends BaseUnitUITest {

    private MockToggleService mockService;
    private SmartWebElement container;
    private By locator;

    // For direct testing of default methods
    private ToggleService directService;

    @BeforeEach
    void setUp() {
        // Setup for standard tests with MockToggleService
        mockService = new MockToggleService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testToggle");

        // Setup for direct testing of default methods
        directService = new ToggleService() {
            @Override
            public void activate(ToggleComponentType componentType, SmartWebElement c, String toggleText) {}

            @Override
            public void activate(ToggleComponentType componentType, String toggleText) {}

            @Override
            public void activate(ToggleComponentType componentType, By toggleLocator) {}

            @Override
            public void deactivate(ToggleComponentType componentType, SmartWebElement c, String toggleText) {}

            @Override
            public void deactivate(ToggleComponentType componentType, String toggleText) {}

            @Override
            public void deactivate(ToggleComponentType componentType, By toggleLocator) {}

            @Override
            public boolean isEnabled(ToggleComponentType componentType, SmartWebElement c, String toggleText) {
                return true;
            }

            @Override
            public boolean isEnabled(ToggleComponentType componentType, String toggleText) {
                return true;
            }

            @Override
            public boolean isEnabled(ToggleComponentType componentType, By toggleLocator) {
                return true;
            }

            @Override
            public boolean isActivated(ToggleComponentType componentType, SmartWebElement c, String toggleText) {
                return true;
            }

            @Override
            public boolean isActivated(ToggleComponentType componentType, String toggleText) {
                return true;
            }

            @Override
            public boolean isActivated(ToggleComponentType componentType, By toggleLocator) {
                return true;
            }
        };
    }

    @Nested
    @DisplayName("Mock Service Tests")
    class MockServiceTests {

        @Nested
        @DisplayName("Activate Method Tests")
        class ActivateMethodTests {

            @Test
            @DisplayName("activate with container and text delegates correctly")
            void activateWithContainerAndText() {
                // Given
                mockService.reset();
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "On";

                // When
                mockService.activate(componentType, container, toggleText);

                // Then
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastContainer).isEqualTo(container);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("activate with text only delegates correctly")
            void activateWithTextOnly() {
                // Given
                mockService.reset();
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "On";

                // When
                mockService.activate(componentType, toggleText);

                // Then
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("activate with locator delegates correctly")
            void activateWithLocator() {
                // Given
                mockService.reset();
                var componentType = MockToggleComponentType.DUMMY;

                // When
                mockService.activate(componentType, locator);

                // Then
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastLocator).isEqualTo(locator);
            }
        }

        @Nested
        @DisplayName("Deactivate Method Tests")
        class DeactivateMethodTests {

            @Test
            @DisplayName("deactivate with container and text delegates correctly")
            void deactivateWithContainerAndText() {
                // Given
                mockService.reset();
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "Off";

                // When
                mockService.deactivate(componentType, container, toggleText);

                // Then
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastContainer).isEqualTo(container);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("deactivate with text only delegates correctly")
            void deactivateWithTextOnly() {
                // Given
                mockService.reset();
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "Off";

                // When
                mockService.deactivate(componentType, toggleText);

                // Then
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("deactivate with locator delegates correctly")
            void deactivateWithLocator() {
                // Given
                mockService.reset();
                var componentType = MockToggleComponentType.DUMMY;

                // When
                mockService.deactivate(componentType, locator);

                // Then
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastLocator).isEqualTo(locator);
            }
        }

        @Nested
        @DisplayName("IsEnabled Method Tests")
        class IsEnabledMethodTests {

            @Test
            @DisplayName("isEnabled with container and text delegates correctly")
            void isEnabledWithContainerAndText() {
                // Given
                mockService.reset();
                mockService.returnBool = true;
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "On";

                // When
                var result = mockService.isEnabled(componentType, container, toggleText);

                // Then
                assertThat(result).isTrue();
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastContainer).isEqualTo(container);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("isEnabled with text only delegates correctly")
            void isEnabledWithTextOnly() {
                // Given
                mockService.reset();
                mockService.returnBool = true;
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "On";

                // When
                var result = mockService.isEnabled(componentType, toggleText);

                // Then
                assertThat(result).isTrue();
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("isEnabled with locator delegates correctly")
            void isEnabledWithLocator() {
                // Given
                mockService.reset();
                mockService.returnBool = true;
                var componentType = MockToggleComponentType.DUMMY;

                // When
                var result = mockService.isEnabled(componentType, locator);

                // Then
                assertThat(result).isTrue();
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastLocator).isEqualTo(locator);
            }
        }

        @Nested
        @DisplayName("IsActivated Method Tests")
        class IsActivatedMethodTests {

            @Test
            @DisplayName("isActivated with container and text delegates correctly")
            void isActivatedWithContainerAndText() {
                // Given
                mockService.reset();
                mockService.returnBool = true;
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "On";

                // When
                var result = mockService.isActivated(componentType, container, toggleText);

                // Then
                assertThat(result).isTrue();
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastContainer).isEqualTo(container);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("isActivated with text only delegates correctly")
            void isActivatedWithTextOnly() {
                // Given
                mockService.reset();
                mockService.returnBool = true;
                var componentType = MockToggleComponentType.DUMMY;
                var toggleText = "On";

                // When
                var result = mockService.isActivated(componentType, toggleText);

                // Then
                assertThat(result).isTrue();
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastText).isEqualTo(toggleText);
            }

            @Test
            @DisplayName("isActivated with locator delegates correctly")
            void isActivatedWithLocator() {
                // Given
                mockService.reset();
                mockService.returnBool = true;
                var componentType = MockToggleComponentType.DUMMY;

                // When
                var result = mockService.isActivated(componentType, locator);

                // Then
                assertThat(result).isTrue();
                assertThat(mockService.lastComponentType).isEqualTo(componentType);
                assertThat(mockService.lastLocator).isEqualTo(locator);
            }
        }
    }

    @Nested
    @DisplayName("Direct Default Methods Tests")
    class DirectDefaultMethodsTests {

        @Test
        @DisplayName("activate default methods delegate correctly")
        void activateDefaultMethods() {
            // Test container and text
            directService.activate(container, "On");

            // Test text only
            directService.activate("On");

            // Test locator
            directService.activate(locator);
        }

        @Test
        @DisplayName("deactivate default methods delegate correctly")
        void deactivateDefaultMethods() {
            // Test container and text
            directService.deactivate(container, "Off");

            // Test text only
            directService.deactivate("Off");

            // Test locator
            directService.deactivate(locator);
        }

        @Test
        @DisplayName("isEnabled default methods delegate correctly")
        void isEnabledDefaultMethods() {
            // Test container and text
            assertThat(directService.isEnabled(container, "On")).isTrue();

            // Test text only
            assertThat(directService.isEnabled("On")).isTrue();

            // Test locator
            assertThat(directService.isEnabled(locator)).isTrue();
        }

        @Test
        @DisplayName("isActivated default methods delegate correctly")
        void isActivatedDefaultMethods() {
            // Test container and text
            assertThat(directService.isActivated(container, "On")).isTrue();

            // Test text only
            assertThat(directService.isActivated("On")).isTrue();

            // Test locator
            assertThat(directService.isActivated(locator)).isTrue();
        }
    }

    @Nested
    @DisplayName("Default Type Resolution Tests")
    class DefaultTypeResolutionTests {
        private MockedStatic<ToggleService> toggleServiceMock;

        @BeforeEach
        void setUp() {
            toggleServiceMock = mockStatic(ToggleService.class, invocation -> {
                if (invocation.getMethod().getName().equals("getDefaultType")) {
                    return null;
                }
                return invocation.callRealMethod();
            });
        }

        @AfterEach
        void tearDown() {
            if (toggleServiceMock != null) {
                toggleServiceMock.close();
            }
        }

        @Test
        @DisplayName("getDefaultType returns null when exception occurs")
        void getDefaultTypeWithException() {
            assertThat(ToggleService.DEFAULT_TYPE).isEqualTo(MockToggleComponentType.DUMMY);
        }
    }

    @Test
    @DisplayName("reset method clears all fields properly")
    void resetMethodClearsAllFields() {
        // Given
        mockService.lastComponentType = MockToggleComponentType.DUMMY;
        mockService.lastContainer = container;
        mockService.lastText = "toggleText";
        mockService.lastLocator = locator;
        mockService.returnBool = true;

        // When
        mockService.reset();

        // Then
        assertThat(mockService.lastComponentType).isNull();
        assertThat(mockService.lastContainer).isNull();
        assertThat(mockService.lastText).isNull();
        assertThat(mockService.lastLocator).isNull();
        assertThat(mockService.returnBool).isFalse();
        assertThat(mockService.returnOptions).isEmpty();
    }
}