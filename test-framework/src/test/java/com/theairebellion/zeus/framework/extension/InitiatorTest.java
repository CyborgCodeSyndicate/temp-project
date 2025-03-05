package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.extension.mock.*;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class InitiatorTest {

    @Test
    void interceptTestMethod_NoPreQuestAnnotation_Proceeds() throws Throwable {
        Initiator initiator = new Initiator();
        Method method = MockTest.class.getMethod("nonAnnotatedMethod");
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);
        when(invocationContext.getTargetClass()).thenReturn((Class) MockTest.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));
        MockInvocation invocation = new MockInvocation();
        assertDoesNotThrow(() -> initiator.interceptTestMethod(invocation, invocationContext, extensionContext));
    }

    @Test
    void interceptTestMethod_QuestNotFound_ThrowsException() throws Throwable {
        Initiator initiator = new Initiator();
        Method method = MockTest.class.getMethod("annotatedMethod");
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);
        when(invocationContext.getTargetClass()).thenReturn((Class) MockTest.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(QUEST)).thenReturn(null);
        MockInvocation invocation = new MockInvocation();
        assertThrows(IllegalStateException.class, () -> initiator.interceptTestMethod(invocation, invocationContext, extensionContext));
    }

    @Test
    void interceptTestMethod_WithPreQuest_ProcessesJourney() throws Throwable {
        Initiator initiator = new Initiator();
        Method method = MockTest.class.getMethod("annotatedMethod");
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);
        when(invocationContext.getTargetClass()).thenReturn((Class) MockTest.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));
        Quest dummyQuest = mock(Quest.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(QUEST)).thenReturn(dummyQuest);
        ApplicationContext appCtx = mock(ApplicationContext.class);
        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extensionContext)).thenReturn(appCtx);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            SuperQuest dummySuperQuest = mock(SuperQuest.class);
            when(decoratorsFactory.decorate(dummyQuest, SuperQuest.class)).thenReturn(dummySuperQuest);
            Storage dummyStorage = mock(Storage.class);
            when(dummySuperQuest.getStorage()).thenReturn(dummyStorage);
            MockPreQuestJourney mockPreQuestJourney = new MockPreQuestJourney();
            try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class)) {
                MockConfig mockConfig = new MockConfig();
                configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);
                try (MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {
                    reflectionMock.when(() ->
                            ReflectionUtil.findEnumImplementationsOfInterface(eq(PreQuestJourney.class), eq("mockJourney"), eq(mockConfig.projectPackage()))
                    ).thenReturn(mockPreQuestJourney);
                    MockLate mockLate = new MockLate();
                    MockDataForge mockDataForge = new MockDataForge(mockLate);
                    reflectionMock.when(() ->
                            ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"), eq(mockConfig.projectPackage()))
                    ).thenReturn(mockDataForge);
                    Storage subStorage = mock(Storage.class);
                    when(dummyStorage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);
                    MockInvocation invocation = new MockInvocation();
                    initiator.interceptTestMethod(invocation, invocationContext, extensionContext);
                    verify(subStorage).put(mockDataForge.enumImpl(), mockLate.join());
                    assert (mockPreQuestJourney.invoked.get());
                    assert (invocation.proceeded.get());
                }
            }
        }
    }

    @Test
    void processJourneyData_WhenLateIsTrue() throws Exception {
        // Setup
        Initiator initiator = new Initiator();
        JourneyData journeyData = mock(JourneyData.class);
        when(journeyData.value()).thenReturn("mockData");
        when(journeyData.late()).thenReturn(true);

        SuperQuest quest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);
        when(quest.getStorage()).thenReturn(storage);
        Storage subStorage = mock(Storage.class);
        when(storage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);

        // Create a spy of MockLate that will be used when late=true
        // This allows us to verify the join() method is not called
        MockLate spyLate = spy(new MockLate());
        MockDataForge mockDataForge = new MockDataForge(spyLate);

        try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {

            MockConfig mockConfig = new MockConfig();
            configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            reflectionMock.when(() ->
                    ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"), eq(mockConfig.projectPackage()))
            ).thenReturn(mockDataForge);

            // Test the method using reflection to access private method
            Method processJourneyDataMethod = Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
            processJourneyDataMethod.setAccessible(true);
            Object result = processJourneyDataMethod.invoke(initiator, journeyData, quest);

            // Verify
            verify(journeyData).late();
            verify(spyLate, never()).join();
            verify(subStorage).put(eq(MockEnum.VALUE), any());
        }
    }

    @Test
    void processJourneyData_WhenLateIsFalse() throws Exception {
        // Setup
        Initiator initiator = new Initiator();
        JourneyData journeyData = mock(JourneyData.class);
        when(journeyData.value()).thenReturn("mockData");
        when(journeyData.late()).thenReturn(false);  // Set late to false

        SuperQuest quest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);
        when(quest.getStorage()).thenReturn(storage);
        Storage subStorage = mock(Storage.class);
        when(storage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);

        // Create a spy of MockLate that will be used when late=false
        // This allows us to verify the join() method is called
        MockLate spyLate = spy(new MockLate());
        MockDataForge mockDataForge = new MockDataForge(spyLate);

        try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {

            MockConfig mockConfig = new MockConfig();
            configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            reflectionMock.when(() ->
                    ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"), eq(mockConfig.projectPackage()))
            ).thenReturn(mockDataForge);

            // Test the method using reflection to access private method
            Method processJourneyDataMethod = Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
            processJourneyDataMethod.setAccessible(true);
            Object result = processJourneyDataMethod.invoke(initiator, journeyData, quest);

            // Verify
            verify(journeyData).late();
            verify(spyLate).join();
            verify(subStorage).put(eq(MockEnum.VALUE), eq("joinedValue"));
        }
    }

    @Test
    void interceptTestMethod_WithNoTestMethod() throws Throwable {
        // Setup
        Initiator initiator = new Initiator();
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.empty());

        MockInvocation invocation = new MockInvocation();

        // Execute
        initiator.interceptTestMethod(invocation, invocationContext, extensionContext);

        // Verify
        assert(invocation.proceeded.get());
    }
}