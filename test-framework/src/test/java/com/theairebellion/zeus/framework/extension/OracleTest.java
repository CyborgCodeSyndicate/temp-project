package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.TestStaticData;
import com.theairebellion.zeus.framework.data.DataProvider;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.exceptions.StaticTestDataInitializationException;
import com.theairebellion.zeus.framework.extension.mock.MockTestClass;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.STATIC_DATA;
import static com.theairebellion.zeus.framework.storage.StoreKeys.*;
import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;
import static io.qameta.allure.util.ResultsUtils.createParameter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class OracleTest {

    @Test
    @DisplayName("supportParameter should return true for method with Quest parameter")
    void supportsParameter_ReturnsTrue_ForQuestType() throws NoSuchMethodException {
        // Given
        Method method = getClass().getDeclaredMethod("methodWithQuestParam", Quest.class);
        Parameter param = method.getParameters()[0];
        ParameterContext paramCtx = mock(ParameterContext.class);

        // When
        when(paramCtx.getParameter()).thenReturn(param);
        ExtensionContext extCtx = mock(ExtensionContext.class);
        Oracle oracle = new Oracle();
        boolean result = oracle.supportsParameter(paramCtx, extCtx);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("supportParameter should return false for method without Quest parameter")
    void supportsParameter_ReturnsFalse_ForNonQuestType() throws NoSuchMethodException {
        // Given
        Method method = getClass().getDeclaredMethod("methodWithStringParam", String.class);
        Parameter param = method.getParameters()[0];
        ParameterContext paramCtx = mock(ParameterContext.class);

        // When
        when(paramCtx.getParameter()).thenReturn(param);
        ExtensionContext extCtx = mock(ExtensionContext.class);
        Oracle oracle = new Oracle();
        boolean result = oracle.supportsParameter(paramCtx, extCtx);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("resolveParameter should initialize new Quest object when there is no static data")
    void resolveParameter_NoStaticData_NoConsumers() {
        // Given
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);

        // When
        when(extCtx.getTestMethod()).thenReturn(Optional.empty());
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extCtx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extCtx)).thenReturn(appCtx);
            QuestFactory questFactory = mock(QuestFactory.class);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(QuestFactory.class)).thenReturn(questFactory);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            Quest quest = mock(Quest.class);
            when(questFactory.createQuest()).thenReturn(quest);
            SuperQuest superQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
            Storage storage = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storage);
            when(store.get(QUEST_CONSUMERS)).thenReturn(null);
            Object result = oracle.resolveParameter(null, extCtx);

            // Then
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    @DisplayName("resolveParameter should initialize new Quest object when there is static data")
    void resolveParameter_WithStaticData() throws Exception {
        // Given
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);
        Method testMethod = MockTestClass.class.getMethod("mockTestMethod");

        // When
        when(extCtx.getTestMethod()).thenReturn(Optional.of(testMethod));
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extCtx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extCtx)).thenReturn(appCtx);
            QuestFactory questFactory = mock(QuestFactory.class);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(QuestFactory.class)).thenReturn(questFactory);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            Quest quest = mock(Quest.class);
            when(questFactory.createQuest()).thenReturn(quest);
            SuperQuest superQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
            Storage storage = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storage);
            when(store.get(QUEST_CONSUMERS)).thenReturn(null);
            Object result = oracle.resolveParameter(null, extCtx);

            // Then
            verify(storage).put(STATIC_DATA, Map.of("dummyKey", "dummyValue"));
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    @DisplayName("resolveParameter should initialize new Quest object when there is hooks parameters")
    void resolveParameter_WithHooksParams() throws Exception {
        // Given
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);
        Method testMethod = MockTestClass.class.getMethod("mockTestMethod");

        // When
        when(extCtx.getTestMethod()).thenReturn(Optional.of(testMethod));
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extCtx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extCtx)).thenReturn(appCtx);
            QuestFactory questFactory = mock(QuestFactory.class);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(QuestFactory.class)).thenReturn(questFactory);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            Quest quest = mock(Quest.class);
            when(questFactory.createQuest()).thenReturn(quest);
            SuperQuest superQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
            Storage storage = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storage);
            when(store.get(QUEST_CONSUMERS)).thenReturn(null);
            Map<Object, Object> hooksStorage = Map.of("TestKey", "TestValue");
            when(store.get(HOOKS_PARAMS, Map.class)).thenReturn(hooksStorage);
            Object result = oracle.resolveParameter(null, extCtx);

            // Then
            verify(storage).put(STATIC_DATA, Map.of("dummyKey", "dummyValue"));
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    @DisplayName("resolveParameter should initialize new Quest object when there is no method")
    void resolveParameter_WithMethodButNoAnnotation() throws Exception {
        // Given
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);

        // Create a method with no TestStaticData annotation
        Method testMethod = MockTestClass.class.getMethod("toString");

        // When
        when(extCtx.getTestMethod()).thenReturn(Optional.of(testMethod));

        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extCtx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extCtx)).thenReturn(appCtx);
            QuestFactory questFactory = mock(QuestFactory.class);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(QuestFactory.class)).thenReturn(questFactory);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            Quest quest = mock(Quest.class);
            when(questFactory.createQuest()).thenReturn(quest);
            SuperQuest superQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
            Storage storage = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storage);
            when(store.get(QUEST_CONSUMERS)).thenReturn(null);

            Object result = oracle.resolveParameter(null, extCtx);

            // Then
            verify(storage).put(STATIC_DATA, Map.of());
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    @DisplayName("resolveParameter should initialize new Quest object when there is quest consumers")
    void resolveParameter_WithConsumers() {
        // Given
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);

        // When
        when(extCtx.getTestMethod()).thenReturn(Optional.empty());
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extCtx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        Consumer<SuperQuest> consumer = mock(Consumer.class);
        when(store.get(QUEST_CONSUMERS)).thenReturn(List.of(consumer));
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extCtx)).thenReturn(appCtx);
            QuestFactory questFactory = mock(QuestFactory.class);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(QuestFactory.class)).thenReturn(questFactory);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            Quest quest = mock(Quest.class);
            when(questFactory.createQuest()).thenReturn(quest);
            SuperQuest superQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
            Storage storage = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storage);
            Object result = oracle.resolveParameter(null, extCtx);

            // Then
            verify(consumer).accept(superQuest);
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    @DisplayName("resolveParameter should throw StaticTestDataInitializationException when prvider instantiation fails")
    void resolveParameter_ThrowsStaticTestDataInitializationException_WhenProviderInstantiationFails() throws Exception {
        // Given
        Oracle oracle = new Oracle();
        ExtensionContext context = mock(ExtensionContext.class);

        // Use a method that has the @TestStaticData(FaultyProvider.class)
        Method method = FaultyStaticDataTestClass.class.getMethod("testMethod");

        // When
        when(context.getTestMethod()).thenReturn(Optional.of(method));
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        QuestFactory questFactory = mock(QuestFactory.class);
        DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
        Quest quest = mock(Quest.class);
        SuperQuest superQuest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);

        when(applicationContext.getBean(QuestFactory.class)).thenReturn(questFactory);
        when(applicationContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
        when(questFactory.createQuest()).thenReturn(quest);
        when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
        when(superQuest.getStorage()).thenReturn(storage);
        when(store.get(QUEST_CONSUMERS)).thenReturn(null);

        try (MockedStatic<SpringExtension> springExtension = mockStatic(SpringExtension.class)) {
            springExtension.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(applicationContext);

            // Act + Assert
            StaticTestDataInitializationException ex = assertThrows(
                    StaticTestDataInitializationException.class,
                    () -> oracle.resolveParameter(null, context)
            );
            Throwable rootCause = ex.getCause();
            while (rootCause.getCause() != null) {
                rootCause = rootCause.getCause();
            }

            // Then
            assertTrue(rootCause instanceof RuntimeException);
            assertEquals("Boom", rootCause.getMessage());
        }
    }

    public static class FaultyProvider implements DataProvider {
        public FaultyProvider() {
            throw new RuntimeException("Boom");
        }

        @Override
        public Map<String, Object> testStaticData() {
            return Map.of();
        }
    }

    public static class FaultyStaticDataTestClass {
        @TestStaticData(FaultyProvider.class)
        public void testMethod() {
            // Just a dummy method to attach the annotation
        }
    }

    // Dummy methods to use for parameter reflection
    private void methodWithQuestParam(Quest quest) {
    }

    private void methodWithStringParam(String value) {
    }

}