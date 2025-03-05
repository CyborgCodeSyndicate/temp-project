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
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @Mock
    private ParameterContext parameterContext;

    @Mock
    private ExtensionContext extensionContext;

    @Mock
    private ExtensionContext.Store store;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DecoratorsFactory decoratorsFactory;

    @Mock
    private Quest quest;

    @Mock
    private SuperQuest superQuest;

    @Mock
    private Craft craft;

    @Mock
    private DataForge dataForge;

    @Mock
    private Late<Object> late;

    @Mock
    private FrameworkConfig frameworkConfig;

    @Mock
    private Storage storage;

    @Mock
    private Storage subStorage;

    @Mock
    private Parameter parameter;

    @BeforeEach
    void setup() {
        craftsman = new Craftsman();

        when(parameterContext.getParameter()).thenReturn(parameter);

        // Common mocks setup
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(StoreKeys.QUEST)).thenReturn(quest);

        when(decoratorsFactory.decorate(any(Quest.class), eq(SuperQuest.class))).thenReturn(superQuest);
        when(superQuest.getStorage()).thenReturn(storage);
        when(storage.sub(StorageKeysTest.ARGUMENTS)).thenReturn(subStorage);

        doReturn(MockEnum.VALUE).when(dataForge).enumImpl();
        when(dataForge.dataCreator()).thenReturn(late);

        when(frameworkConfig.projectPackage()).thenReturn(COM_EXAMPLE);
    }

    @Nested
    @DisplayName("supportsParameter tests")
    class SupportsParameterTests {

        @Test
        @DisplayName("Should return true when parameter is annotated with @Craft")
        void supportsParameterReturnsTrueWhenAnnotatedWithCraft() {
            // Given
            when(parameterContext.isAnnotated(Craft.class)).thenReturn(true);

            // When
            boolean result = craftsman.supportsParameter(parameterContext, extensionContext);

            // Then
            assertThat(result).isTrue();
            verify(parameterContext).isAnnotated(Craft.class);
        }

        @Test
        @DisplayName("Should return false when parameter is not annotated with @Craft")
        void supportsParameterReturnsFalseWhenNotAnnotatedWithCraft() {
            // Given
            when(parameterContext.isAnnotated(Craft.class)).thenReturn(false);

            // When
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
            // Setup specific for resolveParameter tests
            when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.of(craft));
            when(craft.model()).thenReturn(DOG_PET);
        }

        @Test
        @DisplayName("Should throw exception when @Craft annotation is not present")
        void shouldThrowExceptionWhenCraftAnnotationNotPresent() {
            // Given
            when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> craftsman.resolveParameter(parameterContext, extensionContext))
                    .isInstanceOf(ParameterResolutionException.class)
                    .hasMessageContaining("@Craft annotation not found");
        }

        @Test
        @DisplayName("Should throw exception when Quest is not present in the store")
        void shouldThrowExceptionWhenQuestNotPresent() {
            // Given
            when(store.get(StoreKeys.QUEST)).thenReturn(null);

            // Setup static mocking just for this test
            try (MockedStatic<SpringExtension> springExtMock = mockStatic(SpringExtension.class)) {
                springExtMock.when(() -> SpringExtension.getApplicationContext(any())).thenReturn(applicationContext);
                when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

                // When/Then
                assertThatThrownBy(() -> craftsman.resolveParameter(parameterContext, extensionContext))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Quest not found in the global store");
            }
        }

        @Test
        @DisplayName("Should return Late instance when parameter type is Late")
        void shouldReturnLateInstanceWhenParameterTypeIsLate() {
            // Given
            doReturn(Late.class).when(parameter).getType();

            // Setup static mocking
            try (MockedStatic<SpringExtension> springExtMock = mockStatic(SpringExtension.class);
                 MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 MockedStatic<FrameworkConfigHolder> frameworkConfigHolderMock = mockStatic(FrameworkConfigHolder.class)) {

                springExtMock.when(() -> SpringExtension.getApplicationContext(any())).thenReturn(applicationContext);
                when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

                frameworkConfigHolderMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(frameworkConfig);

                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(DataForge.class), eq(DOG_PET), eq(COM_EXAMPLE)
                )).thenReturn(dataForge);

                // When
                Object result = craftsman.resolveParameter(parameterContext, extensionContext);

                // Then
                assertThat(result).isSameAs(late);
                verify(subStorage).put(eq(MockEnum.VALUE), eq(late));
            }
        }

        @Test
        @DisplayName("Should return joined object when parameter type is not Late")
        void shouldReturnJoinedObjectWhenParameterTypeIsNotLate() {
            // Given
            Object joinedObject = new Object();
            doReturn(String.class).when(parameter).getType();
            when(late.join()).thenReturn(joinedObject);

            // Setup static mocking
            try (MockedStatic<SpringExtension> springExtMock = mockStatic(SpringExtension.class);
                 MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 MockedStatic<FrameworkConfigHolder> frameworkConfigHolderMock = mockStatic(FrameworkConfigHolder.class)) {

                springExtMock.when(() -> SpringExtension.getApplicationContext(any())).thenReturn(applicationContext);
                when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

                frameworkConfigHolderMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(frameworkConfig);

                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(DataForge.class), eq(DOG_PET), eq(COM_EXAMPLE)
                )).thenReturn(dataForge);

                // When
                Object result = craftsman.resolveParameter(parameterContext, extensionContext);

                // Then
                assertThat(result).isSameAs(joinedObject);
                verify(late).join();
                verify(subStorage).put(eq(MockEnum.VALUE), eq(joinedObject));
            }
        }

        @Test
        @DisplayName("Should properly capture the lambda for exception throwing")
        void shouldProperlyCaptureLambdaForExceptionThrowing() {
            // Given
            when(parameterContext.findAnnotation(Craft.class)).thenReturn(Optional.empty());

            // When/Then
            // This will test the lambda function that throws the exception
            assertThatThrownBy(() -> craftsman.resolveParameter(parameterContext, extensionContext))
                    .isInstanceOf(ParameterResolutionException.class)
                    .hasMessageContaining("@Craft annotation not found");
        }

        @Test
        @DisplayName("Should verify all method calls and interactions")
        void shouldVerifyAllMethodCallsAndInteractions() {
            // Given
            Object joinedObject = new Object();
            doReturn(String.class).when(parameter).getType();
            when(late.join()).thenReturn(joinedObject);

            // Setup static mocking
            try (MockedStatic<SpringExtension> springExtMock = mockStatic(SpringExtension.class);
                 MockedStatic<ReflectionUtil> reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 MockedStatic<FrameworkConfigHolder> frameworkConfigHolderMock = mockStatic(FrameworkConfigHolder.class)) {

                springExtMock.when(() -> SpringExtension.getApplicationContext(any())).thenReturn(applicationContext);
                when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

                frameworkConfigHolderMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(frameworkConfig);

                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(DataForge.class), eq(DOG_PET), eq(COM_EXAMPLE)
                )).thenReturn(dataForge);

                // When
                craftsman.resolveParameter(parameterContext, extensionContext);

                // Then - verify the entire method execution flow
                verify(parameterContext).getParameter();
                verify(parameter).getType();
                verify(parameterContext).findAnnotation(Craft.class);
                verify(craft).model();
                verify(extensionContext).getStore(ExtensionContext.Namespace.GLOBAL);
                verify(store).get(StoreKeys.QUEST);
                verify(applicationContext).getBean(DecoratorsFactory.class);
                verify(decoratorsFactory).decorate(quest, SuperQuest.class);
                verify(superQuest).getStorage();
                verify(storage).sub(StorageKeysTest.ARGUMENTS);
                verify(dataForge, times(1)).dataCreator();
                verify(late).join();
                verify(subStorage).put(eq(MockEnum.VALUE), eq(joinedObject));

                // Verify static method interactions
                frameworkConfigHolderMock.verify(FrameworkConfigHolder::getFrameworkConfig);
                reflectionUtilMock.verify(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(DataForge.class), eq(DOG_PET), eq(COM_EXAMPLE)
                ));
            }
        }
    }
}