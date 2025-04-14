package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.api.service.fluent.SuperRestServiceFluent;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.API;
import static com.theairebellion.zeus.api.storage.StorageKeysApi.PASSWORD;
import static com.theairebellion.zeus.api.storage.StorageKeysApi.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiTestExtension Tests")
class ApiTestExtensionTest {

   @Mock
   private ExtensionContext context;

   @Mock
   private ExtensionContext.Store store;

   @Mock
   private ApplicationContext appContext;

   @Mock
   private DecoratorsFactory decoratorsFactory;

   private ApiTestExtension extension = new ApiTestExtension();

   @AuthenticateViaApiAs(credentials = TestCreds.class, type = TestAuthClient.class)
   void dummyTestMethod() {
      // Method used for testing annotation handling
   }

   // Define this method to reference in the test
   void noAnnotationMethod() {
      // Method without annotation for testing
   }

   @Nested
   @DisplayName("beforeTestExecution Tests")
   class BeforeTestExecutionTests {

      @Test
      @DisplayName("Should process annotation when present")
      void shouldProcessAnnotationWhenPresent() throws Exception {
         // Arrange
         Method testMethod = ApiTestExtensionTest.class.getDeclaredMethod("dummyTestMethod");
         when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

         List<Consumer<SuperQuest>> consumerList = new ArrayList<>();
         when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
         when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumerList);

         try (var springMock = mockStatic(SpringExtension.class)) {
            springMock.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appContext);
            when(appContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            // Act
            extension.beforeTestExecution(context);

            // Assert
            assertThat(consumerList).hasSize(1);
         }
      }

      @Test
      @DisplayName("Should not add consumer when annotation is missing")
      @MockitoSettings(strictness = Strictness.LENIENT)
      void shouldNotAddConsumerWhenAnnotationIsMissing() throws Exception {
         // Arrange
         Method noAnnotationMethod = ApiTestExtensionTest.class.getDeclaredMethod("noAnnotationMethod");
         when(context.getTestMethod()).thenReturn(Optional.of(noAnnotationMethod));

         List<Consumer<SuperQuest>> consumerList = new ArrayList<>();
         when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
         when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumerList);

         // Act
         extension.beforeTestExecution(context);

         // Assert
         assertThat(consumerList).isEmpty();
      }

      @Test
      @DisplayName("Should initialize store when empty")
      void shouldInitializeStoreWhenEmpty() throws Exception {
         // Arrange
         Method testMethod = ApiTestExtensionTest.class.getDeclaredMethod("dummyTestMethod");
         when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

         ArgumentCaptor<Function<StoreKeys, List<Consumer<SuperQuest>>>> captor =
               ArgumentCaptor.forClass(Function.class);

         final List<Consumer<SuperQuest>>[] actualConsumers = new List[1];

         when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
         when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), captor.capture())).thenAnswer(inv -> {
            actualConsumers[0] = captor.getValue().apply(StoreKeys.QUEST_CONSUMERS);
            return actualConsumers[0];
         });

         try (var springMock = mockStatic(SpringExtension.class)) {
            springMock.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appContext);
            when(appContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            // Act
            extension.beforeTestExecution(context);

            // Assert
            assertThat(actualConsumers[0]).isNotNull();
            assertThat(actualConsumers[0]).hasSize(1);
         }
      }
   }

   @Nested
   @DisplayName("Quest Consumer Tests")
   class QuestConsumerTests {

      @Test
      @DisplayName("Should properly configure quest storage and authenticate")
      void shouldConfigureQuestStorageAndAuthenticate() throws Exception {
         // Arrange
         String username = "testUser";
         String password = "testPass";
         boolean cacheCredentials = false;

         Method dummyMethod = ApiTestExtensionTest.class.getDeclaredMethod("dummyTestMethod");
         AuthenticateViaApiAs authAs = dummyMethod.getAnnotation(AuthenticateViaApiAs.class);

         // Create consumer using reflection to access private method
         Method createMethod = ApiTestExtension.class.getDeclaredMethod(
               "createQuestConsumer",
               DecoratorsFactory.class,
               String.class,
               String.class,
               Class.class,
               boolean.class
         );
         createMethod.setAccessible(true);

         @SuppressWarnings("unchecked")
         Consumer<SuperQuest> consumer = (Consumer<SuperQuest>) createMethod.invoke(
               extension,
               decoratorsFactory,
               username,
               password,
               authAs.type(),
               cacheCredentials
         );

         // Setup mocks for quest consumer execution
         SuperQuest quest = mock(SuperQuest.class);
         Storage storage = mock(Storage.class);
         Storage subStorage = mock(Storage.class);
         RestService restServiceMock = mock(RestService.class);
         RestServiceFluent fluentMock = mock(RestServiceFluent.class);
         SuperRestServiceFluent superFluentMock = mock(SuperRestServiceFluent.class);

         when(quest.getStorage()).thenReturn(storage);
         when(storage.sub(API)).thenReturn(subStorage);
         when(quest.enters(RestServiceFluent.class)).thenReturn(fluentMock);
         when(fluentMock.authenticate(any(), any(), any())).thenReturn(fluentMock);
         when(decoratorsFactory.decorate(fluentMock, SuperRestServiceFluent.class)).thenReturn(superFluentMock);
         when(superFluentMock.getRestService()).thenReturn(restServiceMock);

         // Use reflection to set private fields
         setPrivateField(fluentMock, "restService", restServiceMock);
         setPrivateField(superFluentMock, "original", fluentMock);

         // Act
         consumer.accept(quest);

         // Assert
         verify(subStorage).put(USERNAME, username);
         verify(subStorage).put(PASSWORD, password);
         verify(restServiceMock).setCacheAuthentication(cacheCredentials);
         verify(fluentMock).authenticate(eq(username), eq(password), eq(authAs.type()));
      }
   }

   private void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
      var field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
   }
}