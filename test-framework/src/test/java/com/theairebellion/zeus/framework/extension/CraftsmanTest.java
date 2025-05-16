package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.extension.mock.MockEnum;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.framework.util.TestContextManager;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CraftsmanTest {

    private static final String DOG_PET = "DOG_PET";
    private static final String COM_EXAMPLE = "com.example";

    private Craftsman craftsman;

    @Mock private ParameterContext parameterContext;
    @Mock private ExtensionContext extensionContext;
    @Mock private ExtensionContext.Store globalStore;
    @Mock private ExtensionContext.Store parametersStore;
    @Mock private DecoratorsFactory decoratorsFactory;
    @Mock private Quest quest;
    @Mock private SuperQuest superQuest;
    @Mock private Craft craft;
    @Mock private DataForge<?> dataForge;
    @Mock private Late<Object> late;
    @Mock private FrameworkConfig frameworkConfig;
    @Mock private Storage storage;
    @Mock private Storage subStorage;
    @Mock private Parameter parameter;

    private Method testMethod;
    private Class<?> paramType;

    @BeforeEach
    void setup() throws NoSuchMethodException {
        craftsman = new Craftsman();
        paramType = String.class;

        // Use a real method with a parameter for reflection metadata
        testMethod = this.getClass().getDeclaredMethod("dummyMethod", String.class);

        // Setup mocks
        when(parameterContext.getParameter()).thenReturn(parameter);
        doReturn(String.class).when(parameter).getType();
        doReturn(testMethod).when(parameter).getDeclaringExecutable(); // Ensure parameterTypes isn't null

        when(extensionContext.getRequiredTestMethod()).thenReturn(testMethod);
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(globalStore);
        when(globalStore.get(StoreKeys.QUEST)).thenReturn(quest);

        when(extensionContext.getUniqueId()).thenReturn("test-unique-id");
        ExtensionContext.Namespace customNamespace = ExtensionContext.Namespace.create(TestContextManager.class, "test-unique-id");
        when(extensionContext.getStore(customNamespace)).thenReturn(parametersStore);
        when(extensionContext.getStore(any(ExtensionContext.Namespace.class))).thenReturn(parametersStore);

        when(decoratorsFactory.decorate(any(Quest.class), eq(SuperQuest.class))).thenReturn(superQuest);
        when(superQuest.getStorage()).thenReturn(storage);
        when(storage.sub(StorageKeysTest.ARGUMENTS)).thenReturn(subStorage);

        doReturn(MockEnum.VALUE).when(dataForge).enumImpl();
        when(dataForge.dataCreator()).thenReturn(late);
        when(frameworkConfig.projectPackage()).thenReturn(COM_EXAMPLE);
    }

    // Dummy method used for reflection in test setup
    private void dummyMethod(String input) {
    }

    @Nested
    @DisplayName("supportsParameter tests")
    class SupportsParameterTests {

        @Test
        @DisplayName("Should return true when parameter is annotated with @Craft")
        void supportsParameterReturnsTrueWhenAnnotatedWithCraft() {
            // When
            when(parameterContext.isAnnotated(Craft.class)).thenReturn(true);

            boolean result = craftsman.supportsParameter(parameterContext, extensionContext);

            // Then
            assertThat(result).isTrue();
            verify(parameterContext).isAnnotated(Craft.class);
        }

        @Test
        @DisplayName("Should return false when parameter is not annotated with @Craft")
        void supportsParameterReturnsFalseWhenNotAnnotatedWithCraft() {
            // When
            when(parameterContext.isAnnotated(Craft.class)).thenReturn(false);

            boolean result = craftsman.supportsParameter(parameterContext, extensionContext);

            // Then
            assertThat(result).isFalse();
            verify(parameterContext).isAnnotated(Craft.class);
        }
    }

    @Nested
    @DisplayName("resolveParameter tests")
    class ResolveParameterTests {

        @BeforeEach
        void setup() {
            when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.of(craft));
            when(craft.model()).thenReturn(DOG_PET);
        }

        @Test
        @DisplayName("Should throw exception when @Craft annotation is not present")
        void shouldThrowExceptionWhenCraftAnnotationNotPresent() {
            // When
            when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.empty());
            when(parameter.getName()).thenReturn("testParam");

            // Then
            assertThatThrownBy(() -> craftsman.resolveParameter(parameterContext, extensionContext))
                .isInstanceOf(ParameterResolutionException.class)
                .hasMessageContaining("Failed to resolve parameter")
                .cause()
                .hasMessageContaining("Missing @Craft annotation");
        }

        @Test
        @DisplayName("Should throw exception when Quest is not present in the store")
        void shouldThrowExceptionWhenQuestNotPresent() {
            // When
            when(globalStore.get(StoreKeys.QUEST)).thenReturn(null);

            // Then
            assertThatThrownBy(() -> craftsman.resolveParameter(parameterContext, extensionContext))
                .isInstanceOf(ParameterResolutionException.class)
                .hasMessageContaining("Failed to resolve parameter")
                .cause()
                .hasMessageContaining("Quest not found");
        }

        @Test
        @DisplayName("Should return Late instance when parameter type is Late")
        void shouldReturnLateInstanceWhenParameterTypeIsLate() {
            // Given
            paramType = Late.class;
            doReturn(Late.class).when(parameter).getType();

            try (MockedStatic<TestContextManager> testContextManagerMock = mockStatic(TestContextManager.class);
                 MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 MockedStatic<FrameworkConfigHolder> frameworkConfigHolderMock = mockStatic(FrameworkConfigHolder.class)) {

                // When
                testContextManagerMock.when(() -> TestContextManager.getSuperQuest(extensionContext)).thenReturn(superQuest);
                testContextManagerMock.when(() -> TestContextManager.initializeParameterTracking(extensionContext)).thenCallRealMethod();
                testContextManagerMock.when(() -> TestContextManager.storeArgument(superQuest, dataForge, late, extensionContext)).thenCallRealMethod();

                frameworkConfigHolderMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(frameworkConfig);
                reflectionUtilMock.when(() ->
                                            ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq(DOG_PET), eq(COM_EXAMPLE))
                ).thenReturn(dataForge);

                Object result = craftsman.resolveParameter(parameterContext, extensionContext);

                // Then
                assertThat(result).isSameAs(late);
            }
        }

        @Test
        @DisplayName("Should return joined object when parameter type is not Late")
        void shouldReturnJoinedObjectWhenParameterTypeIsNotLate() {
            // Given
            Object joinedObject = new Object();
            paramType = String.class;
            doReturn(String.class).when(parameter).getType();
            when(late.join()).thenReturn(joinedObject);

            try (MockedStatic<TestContextManager> testContextManagerMock = mockStatic(TestContextManager.class);
                 MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 MockedStatic<FrameworkConfigHolder> frameworkConfigHolderMock = mockStatic(FrameworkConfigHolder.class)) {

                // When
                testContextManagerMock.when(() -> TestContextManager.getSuperQuest(extensionContext)).thenReturn(superQuest);
                frameworkConfigHolderMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(frameworkConfig);
                reflectionUtilMock.when(() ->
                                            ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq(DOG_PET), eq(COM_EXAMPLE))
                ).thenReturn(dataForge);

                Object result = craftsman.resolveParameter(parameterContext, extensionContext);

                // Then
                assertThat(result).isSameAs(joinedObject);
                verify(late).join();
            }
        }
    }
}