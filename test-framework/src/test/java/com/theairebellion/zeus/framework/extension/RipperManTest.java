package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.extension.mock.MockConfig;
import com.theairebellion.zeus.framework.extension.mock.MockNoRipper;
import com.theairebellion.zeus.framework.extension.mock.MockRipper;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RipperManTest {

    public static final String TEST_METHOD = "testMethod";
    public static final String MOCK_TARGET = "MOCK_TARGET";

    @Test
    void afterTestExecution_NoRipperAnnotation_DoesNothing() throws Exception {
        RipperMan ripperMan = new RipperMan();
        ExtensionContext context = mock(ExtensionContext.class);
        Method method = MockNoRipper.class.getMethod(TEST_METHOD);
        when(context.getTestMethod()).thenReturn(Optional.of(method));
        when(context.getRoot()).thenReturn(context);
        assertDoesNotThrow(() -> ripperMan.afterTestExecution(context));
    }

    @Test
    void afterTestExecution_QuestNull_ThrowsException() throws Exception {
        RipperMan ripperMan = new RipperMan();
        ExtensionContext context = mock(ExtensionContext.class);
        Method method = MockRipper.class.getMethod(TEST_METHOD);
        when(context.getTestMethod()).thenReturn(Optional.of(method));
        when(context.getRoot()).thenReturn(context);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(QUEST)).thenReturn(null);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (var springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appCtx);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            when(decoratorsFactory.decorate(null, SuperQuest.class)).thenReturn(null);
            assertThrows(IllegalStateException.class, () -> ripperMan.afterTestExecution(context));
        }
    }

    @Test
    void afterTestExecution_ProcessesRipperLogic() throws Exception {
        RipperMan ripperMan = new RipperMan();
        ExtensionContext context = mock(ExtensionContext.class);
        Method method = MockRipper.class.getMethod(TEST_METHOD);
        when(context.getTestMethod()).thenReturn(Optional.of(method));
        when(context.getRoot()).thenReturn(context);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        Quest quest = mock(Quest.class);
        when(store.get(QUEST)).thenReturn(quest);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (var springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appCtx);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            SuperQuest superQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
            Storage storage = mock(Storage.class);
            when(superQuest.getStorage()).thenReturn(storage);
            Storage subStorage = mock(Storage.class);
            when(storage.sub(ARGUMENTS)).thenReturn(subStorage);
            doNothing().when(subStorage).joinLateArguments();
            MockConfig mockConfig = new MockConfig();
            try (var configMock = mockStatic(FrameworkConfigHolder.class)) {
                configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);
                DataRipper dataRipper = mock(DataRipper.class);
                Consumer<SuperQuest> consumer = mock(Consumer.class);
                when(dataRipper.eliminate()).thenReturn(consumer);
                try (var reflectionMock = mockStatic(ReflectionUtil.class)) {
                    reflectionMock.when(() ->
                            ReflectionUtil.findEnumImplementationsOfInterface(eq(DataRipper.class), eq(MOCK_TARGET), eq(mockConfig.projectPackage()))
                    ).thenReturn(dataRipper);
                    try (var logTestMock = mockStatic(LogTest.class)) {
                        ripperMan.afterTestExecution(context);
                        verify(subStorage).joinLateArguments();
                        verify(consumer).accept(superQuest);
                        logTestMock.verify(() -> LogTest.info(eq("DataRipper processed target: '{}'."),
                                eq(MOCK_TARGET)));
                    }
                }
            }
        }
    }
}