package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.extension.mock.MockConfig;
import com.theairebellion.zeus.framework.extension.mock.MockDataForge;
import com.theairebellion.zeus.framework.extension.mock.MockEnum;
import com.theairebellion.zeus.framework.extension.mock.MockInvocation;
import com.theairebellion.zeus.framework.extension.mock.MockLate;
import com.theairebellion.zeus.framework.extension.mock.MockPreQuestJourney;
import com.theairebellion.zeus.framework.extension.mock.MockTest;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
      assertThrows(IllegalStateException.class,
            () -> initiator.interceptTestMethod(invocation, invocationContext, extensionContext));
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
                     ReflectionUtil.findEnumImplementationsOfInterface(eq(PreQuestJourney.class), eq("mockJourney"),
                           eq(mockConfig.projectPackage()))
               ).thenReturn(mockPreQuestJourney);
               MockLate mockLate = new MockLate();
               MockDataForge mockDataForge = new MockDataForge(mockLate);
               reflectionMock.when(() ->
                     ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"),
                           eq(mockConfig.projectPackage()))
               ).thenReturn(mockDataForge);
               Storage subStorage = mock(Storage.class);
               when(dummyStorage.sub(StorageKeysTest.PRE_ARGUMENTS)).thenReturn(subStorage);
               MockInvocation invocation = new MockInvocation();
               initiator.interceptTestMethod(invocation, invocationContext, extensionContext);
               verify(subStorage).put(mockDataForge.enumImpl(), mockLate.join());
               assertTrue(mockPreQuestJourney.invoked.get());
               assertTrue(invocation.proceeded.get());
            }
         }
      }
   }

   @Test
   void processJourneyData_WhenLateIsTrue() throws Exception {
      Initiator initiator = new Initiator();

      Method method = this.getClass().getDeclaredMethod("annotatedLateTrue");
      TestWrapper wrapper = method.getAnnotation(TestWrapper.class);
      JourneyData journeyData = wrapper.value();

      SuperQuest quest = mock(SuperQuest.class);
      Storage storage = mock(Storage.class);
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
               ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"),
                     eq(mockConfig.projectPackage()))
         ).thenReturn(mockDataForge);

         Method processJourneyDataMethod =
               Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
         processJourneyDataMethod.setAccessible(true);
         processJourneyDataMethod.invoke(initiator, journeyData, quest);

         verify(spyLate, never()).join();
         verify(subStorage).put(eq(MockEnum.VALUE), any());
      }
   }

   @Test
   void processJourneyData_WhenLateIsFalse() throws Exception {
      Initiator initiator = new Initiator();

      Method method = this.getClass().getDeclaredMethod("annotatedLateFalse");
      TestWrapper wrapper = method.getAnnotation(TestWrapper.class);
      JourneyData journeyData = wrapper.value();

      SuperQuest quest = mock(SuperQuest.class);
      Storage storage = mock(Storage.class);
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
               ReflectionUtil.findEnumImplementationsOfInterface(eq(DataForge.class), eq("mockData"),
                     eq(mockConfig.projectPackage()))
         ).thenReturn(mockDataForge);

         Method processJourneyDataMethod =
               Initiator.class.getDeclaredMethod("processJourneyData", JourneyData.class, SuperQuest.class);
         processJourneyDataMethod.setAccessible(true);
         processJourneyDataMethod.invoke(initiator, journeyData, quest);

         verify(spyLate).join();
         verify(subStorage).put(eq(MockEnum.VALUE), eq("joinedValue"));
      }
   }

   @Test
   void interceptTestMethod_WithNoTestMethod() throws Throwable {
      Initiator initiator = new Initiator();
      ReflectiveInvocationContext<Method> invocationContext = mock(ReflectiveInvocationContext.class);
      ExtensionContext extensionContext = mock(ExtensionContext.class);
      when(extensionContext.getTestMethod()).thenReturn(Optional.empty());

      MockInvocation invocation = new MockInvocation();

      initiator.interceptTestMethod(invocation, invocationContext, extensionContext);

      assertTrue(invocation.proceeded.get());
   }

   // --- Annotation definitions and dummy annotated methods below ---

   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.METHOD)
   public @interface TestWrapper {
      JourneyData value();
   }

   @TestWrapper(@JourneyData(value = "mockData", late = true))
   public void annotatedLateTrue() {
   }

   @TestWrapper(@JourneyData(value = "mockData", late = false))
   public void annotatedLateFalse() {
   }
}