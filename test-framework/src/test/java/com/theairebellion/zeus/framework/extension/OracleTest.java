package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.extension.mock.MockTestClass;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.STATIC_DATA;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST_CONSUMERS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class OracleTest {

    @Test
    void supportsParameter_ReturnsTrue_ForQuestType() throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod("methodWithQuestParam", Quest.class);
        Parameter param = method.getParameters()[0];

        ParameterContext paramCtx = mock(ParameterContext.class);
        when(paramCtx.getParameter()).thenReturn(param);

        ExtensionContext extCtx = mock(ExtensionContext.class);
        Oracle oracle = new Oracle();

        boolean result = oracle.supportsParameter(paramCtx, extCtx);
        assertTrue(result);
    }

    @Test
    void supportsParameter_ReturnsFalse_ForNonQuestType() throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod("methodWithStringParam", String.class);
        Parameter param = method.getParameters()[0];

        ParameterContext paramCtx = mock(ParameterContext.class);
        when(paramCtx.getParameter()).thenReturn(param);

        ExtensionContext extCtx = mock(ExtensionContext.class);
        Oracle oracle = new Oracle();

        boolean result = oracle.supportsParameter(paramCtx, extCtx);
        assertFalse(result);
    }

    // Dummy methods to use for parameter reflection
    private void methodWithQuestParam(Quest quest) {}
    private void methodWithStringParam(String value) {}


    @Test
    void resolveParameter_NoStaticData_NoConsumers() {
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);
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
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    void resolveParameter_WithStaticData() throws Exception {
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);
        Method testMethod = MockTestClass.class.getMethod("mockTestMethod");
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
            verify(storage).put(STATIC_DATA, Map.of("dummyKey", "dummyValue"));
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    void resolveParameter_WithMethodButNoAnnotation() throws Exception {
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);

        // Create a method with no TestStaticData annotation
        Method testMethod = MockTestClass.class.getMethod("toString");
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

            // Verify that null static data is put (this is what the actual code does)
            verify(storage).put(STATIC_DATA, null);
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }

    @Test
    void resolveParameter_WithConsumers() {
        Oracle oracle = new Oracle();
        ExtensionContext extCtx = mock(ExtensionContext.class);
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
            verify(consumer).accept(superQuest);
            verify(store).put(QUEST, quest);
            assertSame(quest, result);
        }
    }
}