package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestContextManagerTest {

    @Mock
    private ExtensionContext extensionContext;

    @Mock
    private ExtensionContext.Store globalStore;

    @Mock
    private ExtensionContext.Store parameterStore;

    @Mock
    private Quest quest;

    @Mock
    private SuperQuest superQuest;

    @Mock
    private DecoratorsFactory decoratorsFactory;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DataForge<?> dataForge;

    @Mock
    private Storage storage;

    @Mock
    private Storage subStorage;

    @Mock
    private Method testMethod;

    // Mock enum for testing
    private enum MockEnum {
        ENUM_VALUE
    }

    @Nested
    @DisplayName("getSuperQuest Tests")
    class GetSuperQuestTests {

        @Test
        @DisplayName("Should return decorated SuperQuest when Quest is present in store")
        void shouldReturnDecoratedSuperQuestWhenQuestIsPresent() {
            // Given
            when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(globalStore);
            when(globalStore.get(QUEST)).thenReturn(quest);

            try (MockedStatic<SpringExtension> springExtensionMock = mockStatic(SpringExtension.class)) {
                springExtensionMock.when(() -> SpringExtension.getApplicationContext(extensionContext))
                        .thenReturn(applicationContext);
                when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
                when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);

                // When
                SuperQuest result = TestContextManager.getSuperQuest(extensionContext);

                // Then
                assertThat(result).isSameAs(superQuest);
                verify(extensionContext).getStore(ExtensionContext.Namespace.GLOBAL);
                verify(globalStore).get(QUEST);
                verify(decoratorsFactory).decorate(quest, SuperQuest.class);
            }
        }

        @Test
        @DisplayName("Should throw IllegalStateException when Quest is not present in store")
        void shouldThrowWhenQuestIsNotPresent() {
            // Given
            when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(globalStore);
            when(globalStore.get(QUEST)).thenReturn(null);

            try (MockedStatic<SpringExtension> springExtensionMock = mockStatic(SpringExtension.class)) {
                springExtensionMock.when(() -> SpringExtension.getApplicationContext(extensionContext))
                        .thenReturn(applicationContext);

                // When/Then
                assertThatThrownBy(() -> TestContextManager.getSuperQuest(extensionContext))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("Quest not found in the global store.");
            }
        }
    }

    @Nested
    @DisplayName("storeArgument Tests")
    class StoreArgumentTests {

        @Test
        @DisplayName("Should store argument in SuperQuest storage and extension context")
        void shouldStoreArgumentInStorageAndContext() {
            // Given
            Object testArgument = new Object();
            doReturn(MockEnum.ENUM_VALUE).when(dataForge).enumImpl();
            when(superQuest.getStorage()).thenReturn(storage);
            when(storage.sub(ARGUMENTS)).thenReturn(subStorage);
            when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(globalStore);

            List<Object> existingArgs = new ArrayList<>();
            when(globalStore.get(ARGUMENTS)).thenReturn(existingArgs);

            // When
            TestContextManager.storeArgument(superQuest, dataForge, testArgument, extensionContext);

            // Then
            verify(subStorage).put(eq(MockEnum.ENUM_VALUE), eq(testArgument));
            verify(globalStore).put(eq(ARGUMENTS), any(List.class));

            // Verify the argument was added to the list
            verify(globalStore).get(ARGUMENTS);
            assertThat(existingArgs).hasSize(1).containsExactly(testArgument);
        }

        @Test
        @DisplayName("Should create new args list when none exists")
        void shouldCreateNewArgsListWhenNoneExists() {
            // Given
            Object testArgument = new Object();
            doReturn(MockEnum.ENUM_VALUE).when(dataForge).enumImpl();
            when(superQuest.getStorage()).thenReturn(storage);
            when(storage.sub(ARGUMENTS)).thenReturn(subStorage);
            when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(globalStore);
            when(globalStore.get(ARGUMENTS)).thenReturn(null);

            // When
            TestContextManager.storeArgument(superQuest, dataForge, testArgument, extensionContext);

            // Then
            verify(subStorage).put(eq(MockEnum.ENUM_VALUE), eq(testArgument));
            verify(globalStore).put(eq(ARGUMENTS), any(List.class));
        }
    }

    @Nested
    @DisplayName("initializeParameterTracking Tests")
    class InitializeParameterTrackingTests {

        @Test
        @DisplayName("Should store parameter count in context store")
        void shouldStoreParameterCountInStore() {
            // Given
            when(extensionContext.getUniqueId()).thenReturn("test-unique-id");

            // Create expected namespace
            ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(
                    TestContextManager.class, "test-unique-id");
            when(extensionContext.getStore(namespace)).thenReturn(parameterStore);

            // When
            TestContextManager.initializeParameterTracking(extensionContext);

            // Then
            verify(extensionContext).getUniqueId();
            verify(extensionContext).getStore(namespace);
            verify(parameterStore).getOrComputeIfAbsent(eq("totalParams"), any());
        }

        @DisplayName("initializeParameterTracking computes and stores number of test method parameters")
        @Test
        void initializeParameterTracking_shouldStoreNumberOfParameters() {
            // Given
            ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
            Method mockMethod = mock(Method.class);

            Parameter[] fakeParams = new Parameter[]{ mock(Parameter.class), mock(Parameter.class) };

            // When
            when(extensionContext.getUniqueId()).thenReturn("unique-id");
            when(extensionContext.getStore(any())).thenReturn(mockStore);
            when(extensionContext.getRequiredTestMethod()).thenReturn(mockMethod);
            when(mockMethod.getParameters()).thenReturn(fakeParams);

            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Function<String, Integer>> fnCaptor = ArgumentCaptor.forClass(Function.class);

            // Mock store behavior to call the actual function with the key
            when(mockStore.getOrComputeIfAbsent(keyCaptor.capture(), fnCaptor.capture()))
                    .thenAnswer(invocation -> {
                        String key = invocation.getArgument(0);
                        Function<String, Integer> fn = invocation.getArgument(1);
                        return fn.apply(key);
                    });

            TestContextManager.initializeParameterTracking(extensionContext);

            // Then
            assertEquals(2, fnCaptor.getValue().apply("anyKey"));
        }
    }
}