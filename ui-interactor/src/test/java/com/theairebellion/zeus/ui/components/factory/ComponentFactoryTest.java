package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.components.factory.mock.FailInputImpl;
import com.theairebellion.zeus.ui.components.factory.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.factory.mock.MockInputImpl;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComponentFactory Tests")
class ComponentFactoryTest extends BaseUnitUITest {

    @Mock private WebDriver webDriver;
    @Mock private UiConfig uiConfig;

    private SmartWebDriver smartWebDriver;
    private MockedStatic<ReflectionUtil> reflectionMock;
    private MockedStatic<UiConfigHolder> uiConfigHolderMock;

    private static final String TEST_PACKAGE = "test.package";
    private static final String FRAMEWORK_PACKAGE = "com.theairebellion.zeus";
    private static final String EXPECTED_CREATION_FAILURE_MSG = "Simulated constructor failure!";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smartWebDriver = new SmartWebDriver(webDriver);
        reflectionMock = mockStatic(ReflectionUtil.class);
        uiConfigHolderMock = mockStatic(UiConfigHolder.class);

        lenient().when(uiConfig.projectPackage()).thenReturn(TEST_PACKAGE);
        when(UiConfigHolder.getUiConfig()).thenReturn(uiConfig);
    }

    @AfterEach
    void tearDown() {
        reflectionMock.close();
        uiConfigHolderMock.close();
    }

    @Test
    @DisplayName("getComponent should return correct instance on success")
    void getComponent_Success_ReturnsCorrectInstance() {
        // Given
        var componentType = MockInputComponentType.DUMMY;
        List<Class<? extends Input>> implList = List.of(MockInputImpl.class);

        reflectionMock.when(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(TEST_PACKAGE)))
                .thenReturn(new ArrayList<>(implList));
        reflectionMock.when(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(FRAMEWORK_PACKAGE)))
                .thenReturn(new ArrayList<>());

        // When
        var result = ComponentFactory.getInputComponent(componentType, smartWebDriver);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(MockInputImpl.class);
        reflectionMock.verify(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(TEST_PACKAGE)));
        reflectionMock.verify(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(FRAMEWORK_PACKAGE)));
    }

    @Test
    @DisplayName("getComponent should throw ComponentNotFoundException when no implementation found")
    void getComponent_NotFound_ThrowsComponentNotFoundException() {
        // Given
        var componentType = MockInputComponentType.NON_EXISTENT;
        reflectionMock.when(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), anyString()))
                .thenReturn(Collections.emptyList());

        // When
        var thrown = catchThrowable(() -> ComponentFactory.getInputComponent(componentType, smartWebDriver));

        // Then
        assertThat(thrown)
                .isInstanceOf(ComponentFactory.ComponentNotFoundException.class)
                .hasMessageContaining("No implementation found for type: " + componentType.name());

        reflectionMock.verify(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(TEST_PACKAGE)));
        reflectionMock.verify(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(FRAMEWORK_PACKAGE)));
    }

    @Test
    @DisplayName("getComponent should throw ComponentCreationException when instantiation fails")
    void getComponent_CreationFailed_ThrowsComponentCreationException() {
        // Given
        var componentType = MockInputComponentType.FAIL;
        List<Class<? extends Input>> implList = List.of(FailInputImpl.class);

        reflectionMock.when(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(TEST_PACKAGE)))
                .thenReturn(new ArrayList<>(implList));
        reflectionMock.when(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(FRAMEWORK_PACKAGE)))
                .thenReturn(new ArrayList<>());

        // When
        var thrown = catchThrowable(() -> ComponentFactory.getInputComponent(componentType, smartWebDriver));

        // Then
        assertThat(thrown)
                .isInstanceOf(ComponentFactory.ComponentCreationException.class)
                .hasMessageContaining("Failed to create instance of " + FailInputImpl.class.getName());

        assertThat(thrown.getCause())
                .isInstanceOf(InvocationTargetException.class);

        assertThat(thrown.getCause().getCause())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(EXPECTED_CREATION_FAILURE_MSG);

        reflectionMock.verify(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(TEST_PACKAGE)));
        reflectionMock.verify(() -> ReflectionUtil.findImplementationsOfInterface(eq(Input.class), eq(FRAMEWORK_PACKAGE)));
    }
}