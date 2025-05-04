package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("ModalServiceImpl Unit Tests")
class ModalServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private By locator;
    @Mock private Modal modalMock;

    private ModalServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockModalComponentType componentType = MockModalComponentType.DUMMY_MODAL;

    private static final String BUTTON_TEXT_OK = "OK";
    private static final String BUTTON_TEXT_SUBMIT = "Submit";
    private static final String MODAL_TITLE = "Modal Title";
    private static final String MODAL_BODY = "Body Content";
    private static final String MODAL_CONTENT_TITLE = "Content Title";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ModalServiceImpl(driver);
        locator = By.id("modal");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getModalComponent(any(ModalComponentType.class), eq(driver)))
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
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("clickButton with container and text delegates correctly")
        void clickButtonWithContainerAndText() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.clickButton(componentType, container, BUTTON_TEXT_OK);

            // Then
            verify(modalMock).clickButton(container, BUTTON_TEXT_OK);
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("clickButton with text only delegates correctly")
        void clickButtonWithTextOnly() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.clickButton(componentType, BUTTON_TEXT_SUBMIT);

            // Then
            verify(modalMock).clickButton(BUTTON_TEXT_SUBMIT);
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("clickButton with locator delegates correctly")
        void clickButtonWithLocator() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.clickButton(componentType, locator);

            // Then
            verify(modalMock).clickButton(locator);
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("getTitle delegates correctly")
        void getTitleDelegates() {
            // Given
            when(modalMock.getTitle()).thenReturn(MODAL_TITLE);

            // When
            var result = service.getTitle(componentType);

            // Then
            assertThat(result).isEqualTo(MODAL_TITLE);
            verify(modalMock).getTitle();
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("getBodyText delegates correctly")
        void getBodyTextDelegates() {
            // Given
            when(modalMock.getBodyText()).thenReturn(MODAL_BODY);

            // When
            var result = service.getBodyText(componentType);

            // Then
            assertThat(result).isEqualTo(MODAL_BODY);
            verify(modalMock).getBodyText();
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("getContentTitle delegates correctly")
        void getContentTitleDelegates() {
            // Given
            when(modalMock.getContentTitle()).thenReturn(MODAL_CONTENT_TITLE);

            // When
            var result = service.getContentTitle(componentType);

            // Then
            assertThat(result).isEqualTo(MODAL_CONTENT_TITLE);
            verify(modalMock).getContentTitle();
            verifyNoMoreInteractions(modalMock);
        }

        @Test
        @DisplayName("close delegates correctly")
        void closeDelegates() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.close(componentType);

            // Then
            verify(modalMock).close();
            verifyNoMoreInteractions(modalMock);
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused")
        void componentCaching() {
            // Given - setup in @BeforeEach

            // When
            service.isOpened(componentType);
            service.getTitle(componentType);

            // Then
            factoryMock.verify(() -> ComponentFactory.getModalComponent(eq(componentType), eq(driver)), times(1));
        }
    }
}