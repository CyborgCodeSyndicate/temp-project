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
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("RipperMan Extension Tests")
class RipperManTest {

   private static final String TEST_METHOD = "testMethod";
   private static final String MOCK_TARGET = "MOCK_TARGET";

   @Nested
   @DisplayName("RipperMan execution tests")
   class RipperManExecutionTests {

      @Test
      @DisplayName("Should do nothing when no Ripper annotation is present")
      void afterTestExecution_NoRipperAnnotation_DoesNothing() throws Exception {
         RipperMan ripperMan = new RipperMan();
         ExtensionContext context = mock(ExtensionContext.class);
         Method method = MockNoRipper.class.getMethod(TEST_METHOD);

         when(context.getTestMethod()).thenReturn(Optional.of(method));

         assertDoesNotThrow(() -> ripperMan.afterTestExecution(context));
         verify(context).getTestMethod();
         verifyNoMoreInteractions(context);
      }

      @Test
      @DisplayName("Should throw exception when Quest is not found in global store")
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

         try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appCtx);
            DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);
            when(decoratorsFactory.decorate(null, SuperQuest.class)).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> ripperMan.afterTestExecution(context));
         }
      }

      @Test
      @DisplayName("Should process ripper logic successfully with valid inputs")
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
         try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
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
            try (MockedStatic<FrameworkConfigHolder> configMock = mockStatic(FrameworkConfigHolder.class)) {
               configMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

               @SuppressWarnings("unchecked")
               Consumer<SuperQuest> consumer = mock(Consumer.class);

               // ✅ Use real implementation instead of mocking DataRipper
               DataRipper dataRipper = new FakeDataRipper(consumer);

               try (MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)) {
                  reflectionMock.when(() ->
                        ReflectionUtil.findEnumImplementationsOfInterface(
                              eq(DataRipper.class),
                              eq(MOCK_TARGET),
                              eq(mockConfig.projectPackage())
                        )
                  ).thenReturn(dataRipper);

                  try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
                     ripperMan.afterTestExecution(context);

                     verify(subStorage).joinLateArguments();
                     verify(consumer).accept(superQuest);

                     logTestMock.verify(() -> LogTest.info(
                           eq("DataRipper processed target: '{}'."), eq(MOCK_TARGET)));
                  }
               }
            }
         }
      }
   }

   // ✅ Helper test implementation instead of mocking DataRipper
   static class FakeDataRipper implements DataRipper {
      private final Consumer<SuperQuest> consumer;

      public FakeDataRipper(Consumer<SuperQuest> consumer) {
         this.consumer = consumer;
      }

      @Override
      public Consumer<SuperQuest> eliminate() {
         return consumer;
      }


      @Override
      public Enum<?> enumImpl() {
         return null;
      }


   }
}