package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class InitiatorTest {

    @Test
    @DisplayName("Should intercept test method when there is no pre-quest annotation on the method")
    void interceptTestMethod_NoPreQuestAnnotation_Proceeds() throws Throwable {
        // Given
        Initiator initiator = new Initiator();
        Method method = MockTest.class.getMethod("nonAnnotatedMethod");
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);

        // When
        when(invocationContext.getTargetClass()).thenReturn((Class) MockTest.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));
        MockInvocation invocation = new MockInvocation();

        // Then
        assertDoesNotThrow(() -> initiator.interceptTestMethod(invocation, invocationContext, extensionContext));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when quest is not found")
    void interceptTestMethod_QuestNotFound_ThrowsException() throws Throwable {
        // Given
        Initiator initiator = new Initiator();
        Method method = MockTest.class.getMethod("annotatedMethod");
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);

        // When
        when(invocationContext.getTargetClass()).thenReturn((Class) MockTest.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        when(extensionContext.getTestMethod()).thenReturn(Optional.of(method));
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
        when(store.get(QUEST)).thenReturn(null);
        MockInvocation invocation = new MockInvocation();

        // Then
        assertThrows(IllegalStateException.class, () -> initiator.interceptTestMethod(invocation, invocationContext, extensionContext));
    }

    @Test
    @DisplayName("Should intercept test method when there is pre-quest annotation on the method")
    void interceptTestMethod_WithPreQuest_ProcessesJourney() throws Throwable {
        // Given
        Initiator initiator = new Initiator();
        Method method = MockTest.class.getMethod("annotatedMethod");
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);

        // When
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

                    // Then
                    verify(subStorage).put(mockDataForge.enumImpl(), mockLate.join());
                    assertTrue(mockPreQuestJourney.invoked.get());
                    assertTrue(invocation.proceeded.get());
                }
            }
        }
    }

    @Test
    @DisplayName("Should process journey data when late is true")
    void processJourneyData_WhenLateIsTrue() throws Exception {
        // Given
        Initiator initiator = new Initiator();

        Method method = this.getClass().getDeclaredMethod("annotatedLateTrue");
        TestWrapper wrapper = method.getAnnotation(TestWrapper.class);
        JourneyData journeyData = wrapper.value();

        SuperQuest quest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);

        // When
        when(quest.getStorage()).thenReturn(storage);
        Storage subStorage = mock(Storage.class);
        when(storage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);

        MockLate spyLate = spy(new MockLate());
        MockDataForge mockDataForge = new MockDataForge(spyLate);

        try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {

            MockConfig mockConfig = new MockConfig();
            configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            reflectionMock.when(() ->
                                    ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"), eq(mockConfig.projectPackage()))
            ).thenReturn(mockDataForge);

            Method processJourneyDataMethod = Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
            processJourneyDataMethod.setAccessible(true);
            processJourneyDataMethod.invoke(initiator, journeyData, quest);

            // Then
            verify(spyLate, never()).join();
            verify(subStorage).put(eq(MockEnum.VALUE), any());
        }
    }

    @Test
    @DisplayName("Should process journey data when late is false")
    void processJourneyData_WhenLateIsFalse() throws Exception {
        // Given
        Initiator initiator = new Initiator();

        Method method = this.getClass().getDeclaredMethod("annotatedLateFalse");
        TestWrapper wrapper = method.getAnnotation(TestWrapper.class);
        JourneyData journeyData = wrapper.value();

        SuperQuest quest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);

        // When
        when(quest.getStorage()).thenReturn(storage);
        Storage subStorage = mock(Storage.class);
        when(storage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);

        MockLate spyLate = spy(new MockLate());
        MockDataForge mockDataForge = new MockDataForge(spyLate);

        try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {

            MockConfig mockConfig = new MockConfig();
            configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            reflectionMock.when(() ->
                                    ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"), eq(mockConfig.projectPackage()))
            ).thenReturn(mockDataForge);

            Method processJourneyDataMethod = Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
            processJourneyDataMethod.setAccessible(true);
            processJourneyDataMethod.invoke(initiator, journeyData, quest);

            // Then
            verify(spyLate).join();
            verify(subStorage).put(eq(MockEnum.VALUE), eq("joinedValue"));
        }
    }

    @Test
    @DisplayName("Should intercept test method when there is no method")
    void interceptTestMethod_WithNoTestMethod() throws Throwable {
        // Given
        Initiator initiator = new Initiator();
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        // When
        when(extensionContext.getTestMethod()).thenReturn(Optional.empty());

        MockInvocation invocation = new MockInvocation();

        initiator.interceptTestMethod(invocation, invocationContext, extensionContext);

        // Then
        assertTrue(invocation.proceeded.get());
    }

    @Test
    @DisplayName("Should call join method when late is false")
    void processJourneyData_WhenLateIsFalse_CallsJoin() throws Exception {
        // Given
        Initiator initiator = new Initiator();

        Method method = this.getClass().getDeclaredMethod("annotatedLateFalse");
        TestWrapper wrapper = method.getAnnotation(TestWrapper.class);
        JourneyData journeyData = wrapper.value();

        SuperQuest quest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);
        Storage subStorage = mock(Storage.class);

        // When
        when(quest.getStorage()).thenReturn(storage);
        when(storage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);

        MockLate spyLate = spy(new MockLate());
        MockDataForge mockDataForge = new MockDataForge(spyLate);

        try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class);
             MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {

            MockConfig mockConfig = new MockConfig();
            configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

            reflectionMock.when(() ->
                    ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"), eq(mockConfig.projectPackage()))
            ).thenReturn(mockDataForge);

            Method processJourneyDataMethod = Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
            processJourneyDataMethod.setAccessible(true);
            processJourneyDataMethod.invoke(initiator, journeyData, quest);

            // Then
            verify(spyLate).join();
            verify(subStorage).put(eq(MockEnum.VALUE), any());
        }
    }

    @Test
    @DisplayName("Should get journeys sorted")
    void getSortedJourneys_ReturnsSortedList() throws Exception {
        // Given
        Initiator initiator = new Initiator();
        Method method = this.getClass().getDeclaredMethod("methodWithMultipleJourneys");

        // When
        Method getSortedJourneys = Initiator.class.getDeclaredMethod("getSortedJourneys", Method.class);
        getSortedJourneys.setAccessible(true);
        List<?> journeys = (List<?>) getSortedJourneys.invoke(initiator, method);

        // Then
        assertEquals(2, journeys.size());
        assertEquals(1, ((Journey) journeys.get(0)).order());
        assertEquals(2, ((Journey) journeys.get(1)).order());
    }

    @Test
    @DisplayName("Should intercept test method when test method is null")
    void interceptTestMethod_NoTestMethodPresent_Proceeds() throws Throwable {
        // Given
        Initiator initiator = new Initiator();
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);

        // When
        when(extensionContext.getTestMethod()).thenReturn(Optional.empty());

        MockInvocation invocation = new MockInvocation();

        // Then
        assertDoesNotThrow(() -> initiator.interceptTestMethod(invocation, invocationContext, extensionContext));
        assertTrue(invocation.proceeded.get());
    }
    // --- Annotation definitions and dummy annotated methods below ---

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface TestWrapper {
        JourneyData value();
    }

    @TestWrapper(@JourneyData(value = "mockData", late = true))
    public void annotatedLateTrue() {}

    @TestWrapper(@JourneyData(value = "mockData", late = false))
    public void annotatedLateFalse() {}

    @Journey(order = 2, value = "j2")
    @Journey(order = 1, value = "j1")
    public void methodWithMultipleJourneys() {}
}