package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("ModalServiceImpl Unit Tests")
class ModalServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private ModalServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Modal modalMock;
    private MockModalComponentType componentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        driver = mock(SmartWebDriver.class);
        service = new ModalServiceImpl(driver);
        container = MockSmartWebElement.createMock();
        locator = By.id("modal");
        modalMock = mock(Modal.class);
        componentType = MockModalComponentType.DUMMY;

        // Configure static mock for ComponentFactory
        factoryMock = mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getModalComponent(eq(componentType), eq(driver)))
                .thenReturn(modalMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Method Delegation Tests")
    class MethodDelegationTests {

        @Test
        @DisplayName("isOpened delegates correctly")
        void isOpenedDelegates() {
            // Given
            when(modalMock.isOpened()).thenReturn(true);

            // When
            var result = service.isOpened(componentType);

            // Then
            assertThat(result).isTrue();
            verify(modalMock).isOpened();
        }

        @Test
        @DisplayName("clickButton with container and text delegates correctly")
        void clickButtonWithContainerAndText() {
            // Given
            var buttonText = "OK";

            // When
            service.clickButton(componentType, container, buttonText);

            // Then
            verify(modalMock).clickButton(container, buttonText);
        }

        @Test
        @DisplayName("clickButton with text only delegates correctly")
        void clickButtonWithTextOnly() {
            // Given
            var buttonText = "Submit";

            // When
            service.clickButton(componentType, buttonText);

            // Then
            verify(modalMock).clickButton(buttonText);
        }

        @Test
        @DisplayName("clickButton with locator delegates correctly")
        void clickButtonWithLocator() {
            // When
            service.clickButton(componentType, locator);

            // Then
            verify(modalMock).clickButton(locator);
        }

        @Test
        @DisplayName("getTitle delegates correctly")
        void getTitleDelegates() {
            // Given
            var expectedTitle = "Modal Title";
            when(modalMock.getTitle()).thenReturn(expectedTitle);

            // When
            var result = service.getTitle(componentType);

            // Then
            assertThat(result).isEqualTo(expectedTitle);
            verify(modalMock).getTitle();
        }

        @Test
        @DisplayName("getBodyText delegates correctly")
        void getBodyTextDelegates() {
            // Given
            var expectedText = "Body Content";
            when(modalMock.getBodyText()).thenReturn(expectedText);

            // When
            var result = service.getBodyText(componentType);

            // Then
            assertThat(result).isEqualTo(expectedText);
            verify(modalMock).getBodyText();
        }

        @Test
        @DisplayName("getContentTitle delegates correctly")
        void getContentTitleDelegates() {
            // Given
            var expectedTitle = "Content Title";
            when(modalMock.getContentTitle()).thenReturn(expectedTitle);

            // When
            var result = service.getContentTitle(componentType);

            // Then
            assertThat(result).isEqualTo(expectedTitle);
            verify(modalMock).getContentTitle();
        }

        @Test
        @DisplayName("close delegates correctly")
        void closeDelegates() {
            // When
            service.close(componentType);

            // Then
            verify(modalMock).close();
        }
    }

    @Nested
    @DisplayName("Default Method Overloads Tests")
    class DefaultMethodOverloadsTests {

        @BeforeEach
        void setUpDefaultTypeMock() {
            // Additional setup for testing default method overloads
            factoryMock.when(() -> ComponentFactory.getModalComponent(any(ModalComponentType.class), eq(driver)))
                    .thenReturn(modalMock);
        }

        @Test
        @DisplayName("isOpened default overload delegates correctly")
        void isOpenedDefaultOverload() {
            // Given
            when(modalMock.isOpened()).thenReturn(true);

            // When
            var result = service.isOpened();

            // Then
            assertThat(result).isTrue();
            verify(modalMock).isOpened();
        }

        @Test
        @DisplayName("clickButton with container and text default overload delegates correctly")
        void clickButtonWithContainerAndTextDefaultOverload() {
            // Given
            var buttonText = "OK";

            // When
            service.clickButton(container, buttonText);

            // Then
            verify(modalMock).clickButton(container, buttonText);
        }

        @Test
        @DisplayName("clickButton with text only default overload delegates correctly")
        void clickButtonWithTextOnlyDefaultOverload() {
            // Given
            var buttonText = "Submit";

            // When
            service.clickButton(buttonText);

            // Then
            verify(modalMock).clickButton(buttonText);
        }

        @Test
        @DisplayName("clickButton with locator default overload delegates correctly")
        void clickButtonWithLocatorDefaultOverload() {
            // When
            service.clickButton(locator);

            // Then
            verify(modalMock).clickButton(locator);
        }

        @Test
        @DisplayName("getTitle default overload delegates correctly")
        void getTitleDefaultOverload() {
            // Given
            var expectedTitle = "Modal Title";
            when(modalMock.getTitle()).thenReturn(expectedTitle);

            // When
            var result = service.getTitle();

            // Then
            assertThat(result).isEqualTo(expectedTitle);
            verify(modalMock).getTitle();
        }

        @Test
        @DisplayName("getBodyText default overload delegates correctly")
        void getBodyTextDefaultOverload() {
            // Given
            var expectedText = "Body Content";
            when(modalMock.getBodyText()).thenReturn(expectedText);

            // When
            var result = service.getBodyText();

            // Then
            assertThat(result).isEqualTo(expectedText);
            verify(modalMock).getBodyText();
        }

        @Test
        @DisplayName("getContentTitle default overload delegates correctly")
        void getContentTitleDefaultOverload() {
            // Given
            var expectedTitle = "Content Title";
            when(modalMock.getContentTitle()).thenReturn(expectedTitle);

            // When
            var result = service.getContentTitle();

            // Then
            assertThat(result).isEqualTo(expectedTitle);
            verify(modalMock).getContentTitle();
        }

        @Test
        @DisplayName("close default overload delegates correctly")
        void closeDefaultOverload() {
            // When
            service.close();

            // Then
            verify(modalMock).close();
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused")
        void componentCaching() {
            // When
            service.isOpened(componentType);
            service.getTitle(componentType);
            service.getBodyText(componentType);

            // Then
            factoryMock.verify(() -> ComponentFactory.getModalComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("Different component types create different instances")
        void differentComponentTypes() {
            var type1 = MockModalComponentType.DUMMY;

            var type2 = MockModalComponentType.TEST;

            // Create two modal component mocks
            Modal modal1 = mock(Modal.class);
            Modal modal2 = mock(Modal.class);

            // Configure behavior for the mocks
            when(modal1.isOpened()).thenReturn(false);
            when(modal2.isOpened()).thenReturn(true);

            // Configure factory mock to return different components for different types
            factoryMock.reset();
            factoryMock.when(() -> ComponentFactory.getModalComponent(eq(type1), eq(driver)))
                    .thenReturn(modal1);
            factoryMock.when(() -> ComponentFactory.getModalComponent(eq(type2), eq(driver)))
                    .thenReturn(modal2);

            // Execute first operation
            service.isOpened(type1);

            // Verify first modal was called
            verify(modal1).isOpened();

            // Execute second operation with different type
            boolean result = service.isOpened(type2);

            // Verify second operation and result
            assertThat(result).isTrue();
            verify(modal2).isOpened();

            // Verify factory was called for both types
            factoryMock.verify(() -> ComponentFactory.getModalComponent(eq(type1), eq(driver)), times(1));
            factoryMock.verify(() -> ComponentFactory.getModalComponent(eq(type2), eq(driver)), times(1));
        }
    }
}