package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.config.UiFrameworkConfigHolder;
import com.theairebellion.zeus.ui.parameters.DataIntercept;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUiServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.Event;
import org.openqa.selenium.devtools.v133.network.Network;
import org.openqa.selenium.devtools.v133.network.model.Request;
import org.openqa.selenium.devtools.v133.network.model.RequestId;
import org.openqa.selenium.devtools.v133.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v133.network.model.Response;
import org.openqa.selenium.devtools.v133.network.model.ResponseReceived;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.RESPONSES;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

class UiTestExtensionTest extends BaseUnitUITest {

   @Mock
   private ExtensionContext context;

   private UiTestExtension extension;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      extension = new UiTestExtension();
   }

   @Nested
   @DisplayName("beforeTestExecution registration")
   class BeforeTestExecutionTests {
      @Mock
      ExtensionContext context;
      @Mock
      ExtensionContext.Store store;

      Method interceptStub;
      MockedStatic<ReflectionUtil> reflectionMock;

      @InterceptRequests(requestUrlSubStrings = "foo")
      void interceptRequestMethod() { /* no-op */ }

      @BeforeEach
      void setup() throws NoSuchMethodException {
         MockitoAnnotations.openMocks(this);

         // pull in the real stub
         interceptStub = BeforeTestExecutionTests.class
               .getDeclaredMethod("interceptRequestMethod");
         assertNotNull(
               interceptStub.getAnnotation(InterceptRequests.class),
               "sanity: our stub really has @InterceptRequests"
         );

         // now return THAT stub from getTestMethod()
         when(context.getTestMethod()).thenReturn(Optional.of(interceptStub));

         // a fresh list for your consumers
         when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
         when(store.getOrComputeIfAbsent(
               eq(StoreKeys.QUEST_CONSUMERS), any()))
               .thenReturn(new ArrayList<Consumer<SuperQuest>>());

         // your existing ReflectionUtil mock
         reflectionMock = mockStatic(ReflectionUtil.class);
         reflectionMock
               .when(() -> ReflectionUtil.findImplementationsOfInterface(
                     eq(UiServiceFluent.class), anyString()))
               .thenReturn(List.of());
      }

      @AfterEach
      void tearDown() {
         reflectionMock.close();
      }

      @Test
      @DisplayName("Process Intercept Requests Annotation")
      void testProcessInterceptRequestsAnnotation() throws Exception {
         // 1. grab the private method
         Method m = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         m.setAccessible(true);

         // 2. pick a real Method that has @InterceptRequests on it
         Method stub = getClass().getDeclaredMethod("interceptRequestMethod");
         assertNotNull(stub.getAnnotation(InterceptRequests.class),
               "sanity-check: our stub must be annotated!");

         // 3. mock context & store so that addQuestConsumer will find a List
         ExtensionContext ctx = mock(ExtensionContext.class);
         ExtensionContext.Store store = mock(ExtensionContext.Store.class);
         when(ctx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
         when(store.getOrComputeIfAbsent(
               eq(StoreKeys.QUEST_CONSUMERS), any()))
               .thenReturn(new ArrayList<Consumer<SuperQuest>>());

         // 4. invoke only the one method under test
         UiTestExtension ext = new UiTestExtension();
         m.invoke(ext, ctx, stub);

         // 5. verify that we actually grabbed the GLOBAL store
         verify(ctx).getStore(ExtensionContext.Namespace.GLOBAL);
         verify(store)
               .getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any());
      }

      @Test
      @DisplayName("Test registerAssertionConsumer")
      void testRegisterAssertionConsumer() throws Exception {
         // Get the method using reflection
         Method registerMethod = UiTestExtension.class.getDeclaredMethod(
               "registerAssertionConsumer", ExtensionContext.class);
         registerMethod.setAccessible(true);

         // Create mocks
         ExtensionContext mockContext = mock(ExtensionContext.class);
         ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
         List<Consumer<SuperQuest>> consumers = new ArrayList<>();

         // Setup behavior
         when(mockContext.getStore(any())).thenReturn(mockStore);
         when(mockStore.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumers);
         when(mockContext.getDisplayName()).thenReturn("Test Display Name");

         // Execute the method
         registerMethod.invoke(extension, mockContext);

         // Verify a consumer was added
         assertEquals(1, consumers.size());

         // Execute the consumer with a mock quest to verify it works
         SuperQuest quest = mock(SuperQuest.class);
         SmartWebDriver smartWebDriver = mock(SmartWebDriver.class);
         CustomSoftAssertion softAssertion = mock(CustomSoftAssertion.class);

         when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
         when(quest.getSoftAssertions()).thenReturn(softAssertion);

         consumers.get(0).accept(quest);

         verify(quest).artifact(UiServiceFluent.class, SmartWebDriver.class);
         verify(quest).getSoftAssertions();
         verify(softAssertion).registerObjectForPostErrorHandling(eq(SmartWebDriver.class), eq(smartWebDriver));
      }

      @Test
      @DisplayName("Test processAuthenticateViaUiAsAnnotation")
      void testProcessAuthenticateViaUiAsAnnotation() throws Exception {
         // Get the method using reflection
         Method processMethod = UiTestExtension.class.getDeclaredMethod(
               "processAuthenticateViaUiAsAnnotation", ExtensionContext.class, Method.class);
         processMethod.setAccessible(true);

         // Create an actual annotated method for testing
         Method testMethod = UiTestExtensionTest.class
               .getDeclaredMethod("authenticateMethod");

         // Create mocks
         ExtensionContext mockContext = mock(ExtensionContext.class);
         ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);

         // Setup mocks
         when(mockContext.getStore(any())).thenReturn(mockStore);
         when(mockStore.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(new ArrayList<>());

         // Stub the needed application context
         try (var springExtensionMock = mockStatic(SpringExtension.class)) {
            ApplicationContext mockAppContext = mock(ApplicationContext.class);
            DecoratorsFactory mockFactory = mock(DecoratorsFactory.class);

            springExtensionMock.when(() -> SpringExtension.getApplicationContext(mockContext))
                  .thenReturn(mockAppContext);
            when(mockAppContext.getBean(DecoratorsFactory.class)).thenReturn(mockFactory);

            // Execute method
            processMethod.invoke(new UiTestExtension(), mockContext, testMethod);

            // Verify interactions
            verify(mockContext).getStore(any());
            verify(mockStore).getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any());
         }
      }

      @Test
      @DisplayName("Register Custom Services")
      void testRegisterCustomServices() throws Exception {
         // Get the method using reflection
         Method registerCustomServicesMethod = UiTestExtension.class.getDeclaredMethod(
               "registerCustomServices", ExtensionContext.class);
         registerCustomServicesMethod.setAccessible(true);

         // Create mocks
         ExtensionContext mockContext = mock(ExtensionContext.class);
         ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
         List<Consumer<SuperQuest>> consumers = new ArrayList<>();

         // Setup mocks
         when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
         when(mockStore.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumers);

         // Execute the method
         registerCustomServicesMethod.invoke(extension, mockContext);

         // Verify that a consumer was added
         assertEquals(1, consumers.size());
      }

      @Test
      @DisplayName("each registered consumer actually runs")
      void eachConsumerExecutes() {
         // grab the list you already populated in your register test:
         @SuppressWarnings("unchecked")
         var consumers = (List<Consumer<SuperQuest>>)
               store.getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, k -> null);

         // make a dummy SuperQuest with just enough stubbing to avoid NPE
         SuperQuest dummy = mock(SuperQuest.class);
         when(dummy.artifact(any(), any())).thenReturn(mock(SmartWebDriver.class));
         when(dummy.getStorage()).thenReturn(mock(Storage.class));
         when(dummy.getSoftAssertions()).thenReturn(mock(CustomSoftAssertion.class));
         // etc—whatever each consumer expects

         // invoke each
         for (Consumer<SuperQuest> c : consumers) {
            assertDoesNotThrow(() -> c.accept(dummy),
                  "consumer " + c + " should not blow up");
         }
      }

      @Test
      @DisplayName("– no enums found → use raw substrings")
      void interceptRequests_NoEnums_UsesRaw() throws Exception {
         Method m = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         m.setAccessible(true);

         // our stub method that has @InterceptRequests("foo")
         Method stub = getClass().getDeclaredMethod("interceptRequestMethod");
         String[] raw = stub.getAnnotation(InterceptRequests.class)
               .requestUrlSubStrings();
         assertArrayEquals(new String[] {"foo"}, raw);  // sanity-check

         // make ReflectionUtil return zero implementations
         reflectionMock
               .when(() -> ReflectionUtil
                     .findEnumClassImplementationsOfInterface(
                           eq(DataIntercept.class), anyString()))
               .thenReturn(List.of());

         // invoke
         m.invoke(extension, context, stub);

         @SuppressWarnings("unchecked")
         var consumers =
               (List<Consumer<SuperQuest>>) store.getOrComputeIfAbsent(
                     StoreKeys.QUEST_CONSUMERS, k -> null);
         assertEquals(1, consumers.size());
         Consumer<SuperQuest> c = consumers.get(0);

         // grab the captured URLs
         Field urlsField = c.getClass().getDeclaredFields()[0];
         urlsField.setAccessible(true);
         String[] got = (String[]) urlsField.get(c);

         // now assert we really fell back to the raw annotation...
         assertArrayEquals(raw, got);
      }

      @Test
      @DisplayName("– one enum, no match → fallback to raw")
      void interceptRequests_OneEnum_NoMatch_UsesRaw() throws Exception {
         Method m = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         m.setAccessible(true);

         // same stub as above
         Method stub = getClass().getDeclaredMethod("interceptRequestMethod");
         String[] raw = stub.getAnnotation(InterceptRequests.class)
               .requestUrlSubStrings();

         // make ReflectionUtil return exactly one enum class
         reflectionMock
               .when(() -> ReflectionUtil
                     .findEnumClassImplementationsOfInterface(
                           eq(DataIntercept.class), anyString()))
               .thenReturn(List.of(TestInterceptEnum.class));

         // invoke
         m.invoke(extension, context, stub);

         @SuppressWarnings("unchecked")
         var consumers =
               (List<Consumer<SuperQuest>>) store.getOrComputeIfAbsent(
                     StoreKeys.QUEST_CONSUMERS, k -> null);
         Consumer<SuperQuest> c = consumers.get(0);

         Field urlsField = c.getClass().getDeclaredFields()[0];
         urlsField.setAccessible(true);
         String[] got = (String[]) urlsField.get(c);

         // again, since "foo" ≠ "ONE", resolvedEndpoints is empty → raw
         assertArrayEquals(raw, got);
      }

      @Test
      @DisplayName("– one enum, match found → use resolved endpoints")
      void interceptRequests_OneEnum_Match_UsesResolved() throws Exception {
         Method m = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation", ExtensionContext.class, Method.class);
         m.setAccessible(true);

         Method stub = getClass().getDeclaredMethod("interceptRequestOneEnumMatch");

         // stub to return our single enum
         reflectionMock
               .when(() -> ReflectionUtil.findEnumClassImplementationsOfInterface(
                     eq(DataIntercept.class), anyString()))
               .thenReturn(List.of(TestInterceptEnum.class));

         // call
         m.invoke(extension, context, stub);

         @SuppressWarnings("unchecked")
         var consumers = (List<Consumer<SuperQuest>>)
               store.getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, k -> null);
         Consumer<SuperQuest> c = consumers.get(0);

         Field f = c.getClass().getDeclaredFields()[0];
         f.setAccessible(true);
         String[] got = (String[]) f.get(c);

         // since the annotation was {"ONE"}, resolvedEndpoints == List.of("/bar")
         assertArrayEquals(new String[] {"/bar"}, got);
      }

      @Test
      @DisplayName("– reflection throws → use raw substrings")
      void interceptRequests_ReflectionThrows_UsesRaw() throws Exception {
         // 1) grab the private method
         Method process = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         process.setAccessible(true);

         // 2) pick your annotated stub; it has @InterceptRequests("foo")
         Method stub = getClass().getDeclaredMethod("interceptRequestMethod");
         String[] raw = stub.getAnnotation(InterceptRequests.class)
               .requestUrlSubStrings();
         // raw == ["foo"]

         // 3) make ReflectionUtil throw
         reflectionMock
               .when(() -> ReflectionUtil.findEnumClassImplementationsOfInterface(
                     eq(DataIntercept.class), anyString()))
               .thenThrow(new RuntimeException("boom"));

         // 4) invoke
         process.invoke(extension, context, stub);

         // 5) pull out the only consumer that was registered
         @SuppressWarnings("unchecked")
         var consumers = (List<Consumer<SuperQuest>>)
               store.getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, k -> null);
         assertEquals(1, consumers.size());

         // 6) reflectively extract its captured urlsForIntercepting field
         Consumer<SuperQuest> c = consumers.get(0);
         Field urlsField = c.getClass().getDeclaredFields()[0];
         urlsField.setAccessible(true);
         String[] got = (String[]) urlsField.get(c);

         // 7) assert we fell back to the *actual* raw annotation values
         assertArrayEquals(raw, got);
      }

      @Test
      @DisplayName("– multiple enums → fallback to raw annotation values")
      void interceptRequests_MultipleEnums_FallbackToRaw() throws Exception {
         // 1) grab the private method
         Method process = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         process.setAccessible(true);

         // 2) pick your stub method (has @InterceptRequests("foo"))
         Method stub = getClass().getDeclaredMethod("interceptRequestMethod");
         String[] raw = stub.getAnnotation(InterceptRequests.class)
               .requestUrlSubStrings();
         assertArrayEquals(new String[] {"foo"}, raw, "sanity: raw substrings");

         // 3) make ReflectionUtil return TWO enums → triggers that throw-then-catch branch
         reflectionMock
               .when(() -> ReflectionUtil.findEnumClassImplementationsOfInterface(
                     eq(DataIntercept.class), anyString()))
               .thenReturn(List.of(TestInterceptEnum.class, AnotherInterceptEnum.class));

         // 4) invoke
         process.invoke(extension, context, stub);

         // 5) grab the single consumer that was registered
         @SuppressWarnings("unchecked")
         List<Consumer<SuperQuest>> consumers =
               (List<Consumer<SuperQuest>>)
                     store.getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, k -> null);
         assertEquals(1, consumers.size(), "Still exactly one consumer");

         // 6) reflectively inspect its captured urlsForIntercepting field
         Consumer<SuperQuest> c = consumers.get(0);
         Field urlsField = c.getClass().getDeclaredFields()[0];
         urlsField.setAccessible(true);
         String[] got = (String[]) urlsField.get(c);

         // 7) must have fallen back to the raw annotation
         assertArrayEquals(raw, got);
      }

      @Test
      @DisplayName("– multiple enum impls → fallback to raw substrings")
      void interceptRequests_MultipleEnums_UsesRaw() throws Exception {
         // 1) reflectively grab the private method
         Method proc = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         proc.setAccessible(true);

         // 2) pick the stub annotated @InterceptRequests("foo")
         Method stub = getClass().getDeclaredMethod("interceptRequestMethod");
         String[] raw = stub.getAnnotation(InterceptRequests.class)
               .requestUrlSubStrings();
         assertArrayEquals(new String[] {"foo"}, raw, "sanity: annotation must be [\"foo\"]");

         // 3) make the ReflectionUtil return TWO enum classes
         reflectionMock
               .when(() -> ReflectionUtil.findEnumClassImplementationsOfInterface(
                     eq(DataIntercept.class), anyString()))
               .thenReturn(List.of(TestInterceptEnum.class, AnotherTestInterceptEnum.class));

         // 4) invoke — it should swallow the IllegalStateException internally
         assertDoesNotThrow(() -> proc.invoke(extension, context, stub),
               "multiple‐enum branch should be caught and not rethrown");

         // 5) grab the one‐and‐only consumer that was registered
         @SuppressWarnings("unchecked")
         List<Consumer<SuperQuest>> list = (List<Consumer<SuperQuest>>)
               store.getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, k -> null);
         assertEquals(1, list.size(), "exactly one consumer must still be registered");

         // 6) extract its captured urlsForIntercepting field
         Consumer<SuperQuest> c = list.get(0);
         Field f = c.getClass().getDeclaredFields()[0];
         f.setAccessible(true);
         String[] got = (String[]) f.get(c);

         // 7) should have fallen back to the raw annotation
         assertArrayEquals(raw, got);
      }

      @Test
      @DisplayName("– one enum, empty annotation → fallback to raw substrings")
      void interceptRequests_OneEnum_EmptyAnnotation_UsesRaw() throws Exception {
         Method proc = UiTestExtension.class
               .getDeclaredMethod("processInterceptRequestsAnnotation",
                     ExtensionContext.class, Method.class);
         proc.setAccessible(true);

         // stub with an *empty* requestUrlSubStrings
         Method emptyStub = getClass().getDeclaredMethod("interceptRequestEmpty");
         InterceptRequests ann = emptyStub.getAnnotation(InterceptRequests.class);
         String[] rawEmpty = ann.requestUrlSubStrings();
         assertEquals(0, rawEmpty.length);

         // have ReflectionUtil pretend there's exactly one enum
         reflectionMock
               .when(() -> ReflectionUtil.findEnumClassImplementationsOfInterface(
                     eq(DataIntercept.class), anyString()))
               .thenReturn(List.of(TestInterceptEnum.class));

         // invoke — no exception
         proc.invoke(extension, context, emptyStub);

         @SuppressWarnings("unchecked")
         List<Consumer<SuperQuest>> consumers = (List<Consumer<SuperQuest>>)
               store.getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, k -> null);
         assertEquals(1, consumers.size());

         // pull out its captured URLs
         Consumer<SuperQuest> c = consumers.get(0);
         Field urlsField = c.getClass().getDeclaredFields()[0];
         urlsField.setAccessible(true);
         String[] got = (String[]) urlsField.get(c);

         // must have fallen back to the raw (empty) annotation array
         assertArrayEquals(rawEmpty, got);
      }

      @Test
      @DisplayName("beforeTestExecution registers exactly one intercept consumer among all")
      void beforeTestExecutionRegistersOneInterceptConsumer() throws Exception {
         // Arrange: make sure our real stub method is the one returned by getTestMethod()
         Method realStub = getClass().getDeclaredMethod("interceptRequestMethod");
         when(context.getTestMethod()).thenReturn(Optional.of(realStub));

         // Act
         extension.beforeTestExecution(context);

         @SuppressWarnings("unchecked")
         List<Consumer<SuperQuest>> consumers =
               (List<Consumer<SuperQuest>>) store.getOrComputeIfAbsent(
                     StoreKeys.QUEST_CONSUMERS, k -> null);

         // Sanity: raw annotation value
         String[] raw = realStub.getAnnotation(InterceptRequests.class)
               .requestUrlSubStrings();
         assertArrayEquals(new String[] {"foo"}, raw, "sanity-check raw annotation");

         // Count how many consumers captured exactly that raw array
         int matchCount = 0;
         for (Consumer<SuperQuest> c : consumers) {
            for (Field f : c.getClass().getDeclaredFields()) {
               if (f.getType().equals(String[].class)) {
                  f.setAccessible(true);
                  String[] captured = (String[]) f.get(c);
                  if (Arrays.equals(raw, captured)) {
                     matchCount++;
                  }
               }
            }
         }

         assertEquals(1, matchCount,
               "Exactly one of the registered consumers should be the intercept-consumer");
      }

      @Test
      @DisplayName("postQuestCreationIntercept — full Chrome path executes without error")
      void postQuestCreationIntercept_fullChromePath_noException() throws Exception {
         // 1) grab the private helper
         Method helper = UiTestExtension.class
               .getDeclaredMethod("postQuestCreationIntercept", SuperQuest.class, String[].class);
         helper.setAccessible(true);

         // 2) mock DevTools + ChromeDriver
         DevTools devTools = mock(DevTools.class);
         ChromeDriver chrome = mock(ChromeDriver.class);
         when(chrome.getDevTools()).thenReturn(devTools);

         // 3) wrap into HasOriginal so unwrapDriver(...) yields our ChromeDriver
         HasOriginal proxy = mock(HasOriginal.class);
         when(proxy.getOriginal()).thenReturn(chrome);

         // 4) wrap that proxy in SmartWebDriver
         SmartWebDriver smart = mock(SmartWebDriver.class);
         when(smart.getOriginal()).thenReturn(proxy);

         // 5) stub SuperQuest.artifact(...) to return our SmartWebDriver
         SuperQuest quest = mock(SuperQuest.class);
         when(quest.artifact(eq(UiServiceFluent.class), eq(SmartWebDriver.class)))
               .thenReturn(smart);

         // 6) stub out its Storage so no NPEs (we don't actually verify puts here)
         Storage top = mock(Storage.class);
         Storage ui = mock(Storage.class);
         when(quest.getStorage()).thenReturn(top);
         when(top.sub(UI)).thenReturn(ui);

         // 7) invoke every line of that private method—
         //    createSession(), send(...), both addListener(...),
         //    the Consumer lambda construction, etc.
         assertDoesNotThrow(() ->
               helper.invoke(null, quest, new String[]{"foo", "bar"})
         );
      }

      @Test
      void postQuestCreationIntercept_nonChromeDriver_throws() throws Exception {
         Method helper = UiTestExtension.class
               .getDeclaredMethod("postQuestCreationIntercept", SuperQuest.class, String[].class);
         helper.setAccessible(true);

         // build your non-Chrome SmartWebDriver as before…
         interface ProxyDriver extends WebDriver { WebDriver getOriginal(); }
         ProxyDriver nonChromeProxy = mock(ProxyDriver.class);
         WebDriver underlying = mock(WebDriver.class);
         when(nonChromeProxy.getOriginal()).thenReturn(underlying);

         SmartWebDriver smart = mock(SmartWebDriver.class);
         when(smart.getOriginal()).thenReturn(nonChromeProxy);

         SuperQuest quest = mock(SuperQuest.class);
         when(quest.artifact(eq(UiServiceFluent.class), eq(SmartWebDriver.class)))
               .thenReturn(smart);

         // here we *unwrap* the InvocationTargetException*:
         Executable call = () -> {
            try {
               helper.invoke(null, quest, new String[]{ "foo" });
            } catch (InvocationTargetException ite) {
               // re-throw the *real* IllegalArgumentException cause:
               throw ite.getCause();
            }
         };

         IllegalArgumentException iae = assertThrows(
               IllegalArgumentException.class,
               call,
               "Should reject non-ChromeDriver"
         );
         assertEquals(
               "Intercepting Backend Requests is only acceptable with Chrome browser",
               iae.getMessage()
         );
      }


      // helper second enum to simulate "size()>1"
      public enum AnotherInterceptEnum implements DataIntercept<AnotherInterceptEnum> {
         X;

         @Override
         public String getEndpointSubString() {
            return "/x";
         }

         @Override
         public AnotherInterceptEnum enumImpl() {
            return X;
         }
      }

      // -----------------------------------------------------------------
      // helper enum used in the two “one enum” tests:
      public enum TestInterceptEnum implements DataIntercept<TestInterceptEnum> {
         ONE;

         @Override
         public String getEndpointSubString() {
            return "/bar";
         }

         @Override
         public TestInterceptEnum enumImpl() {
            return ONE;
         }
      }

      public enum AnotherTestInterceptEnum implements DataIntercept<AnotherTestInterceptEnum> {
         ALPHA;

         @Override
         public String getEndpointSubString() {
            return "/alpha";
         }

         @Override
         public AnotherTestInterceptEnum enumImpl() {
            return ALPHA;
         }
      }

      // dummy stub method with a single name that matches ONE
      @InterceptRequests(requestUrlSubStrings = "ONE")
      void interceptRequestOneEnumMatch() { /* no-op */ }

      @InterceptRequests(requestUrlSubStrings = {})
      void interceptRequestEmpty() { /* no-op */ }

      interface HasOriginal extends WebDriver {
         ChromeDriver getOriginal();
      }

      interface ProxyDriver extends WebDriver {
         WebDriver getOriginal();
      }
   }

   @Test
   @DisplayName("Check URL Method")
   void testCheckUrl() throws Exception {
      // Use reflection to access private static method
      Method checkUrlMethod = UiTestExtension.class.getDeclaredMethod("checkUrl", String[].class, String.class);
      checkUrlMethod.setAccessible(true);

      // Test cases
      String[] urlSubstrings = {"test", "example"};

      // Scenario 1: URL contains substring
      boolean result1 = (boolean) checkUrlMethod.invoke(null, urlSubstrings, "https://test.com/path");
      assertTrue(result1);

      // Scenario 2: URL does not contain substring
      boolean result2 = (boolean) checkUrlMethod.invoke(null, urlSubstrings, "https://other.com/path");
      assertFalse(result2);
   }

   @Test
   @DisplayName("Add Response In Storage")
   @Disabled
   void testAddResponseInStorage() throws Exception {
      // Use reflection to access private static method
      Method addResponseMethod = UiTestExtension.class.getDeclaredMethod(
            "addResponseInStorage", Storage.class, ApiResponse.class
      );
      addResponseMethod.setAccessible(true);

      // Prepare test data
      ApiResponse apiResponse = new ApiResponse("http://test.url", "GET", 200);
      apiResponse.setBody("Test response body");

      // Create a mock storage
      Storage mockStorage = mock(Storage.class);
      Storage uiStorage = mock(Storage.class);

      // Setup storage behavior
      when(mockStorage.sub(UI)).thenReturn(uiStorage);
      when(uiStorage.get(
            eq(RESPONSES),
            any(ParameterizedTypeReference.class)
      )).thenReturn(null);

      // Invoke method
      addResponseMethod.invoke(null, mockStorage, apiResponse);

      // Verify storage interaction
      verify(uiStorage).put(eq(RESPONSES), argThat(list ->
            list instanceof List &&
                  ((List<?>) list).size() == 1 &&
                  ((List<?>) list).get(0).equals(apiResponse)
      ));
   }

   @Test
   @DisplayName("Unwrap Driver")
   void testUnwrapDriver() throws Exception {
      // Use reflection to access private static method
      Method unwrapMethod = UiTestExtension.class.getDeclaredMethod("unwrapDriver", WebDriver.class);
      unwrapMethod.setAccessible(true);

      // Create a mock SmartWebDriver
      SmartWebDriver mockSmartWebDriver = mock(SmartWebDriver.class);
      WebDriver originalDriver = mock(WebDriver.class);

      // Setup the getOriginal method behavior
      when(mockSmartWebDriver.getOriginal()).thenReturn(originalDriver);

      // Invoke method
      WebDriver result = (WebDriver) unwrapMethod.invoke(null, mockSmartWebDriver);

      // Assertions
      assertNotNull(result);
      assertEquals(originalDriver, result);
   }

   @Test
   @DisplayName("Unwrap Driver Exception")
   void testUnwrapDriverException() throws Exception {
      // Use reflection to access private static method
      Method unwrapMethod = UiTestExtension.class.getDeclaredMethod("unwrapDriver", WebDriver.class);
      unwrapMethod.setAccessible(true);

      // Create a mock WebDriver without getOriginal method
      WebDriver mockDriver = mock(WebDriver.class);

      // Invoke method and expect exception
      try {
         unwrapMethod.invoke(null, mockDriver);
         fail("Expected InvocationTargetException");
      } catch (InvocationTargetException e) {
         assertInstanceOf(RuntimeException.class, e.getCause());
         assertEquals("Failed to unwrap WebDriver", e.getCause().getMessage());
      }
   }

   @Test
   @DisplayName("Static Assertion Registry Initialization")
   void testStaticAssertionRegistryInitialization() {
      // This test verifies that the static initializer block runs without exceptions
      // The actual registration happens in the static block
      assertDoesNotThrow(() -> {
         // Trigger class loading
         Class.forName("com.theairebellion.zeus.ui.extensions.UiTestExtension");
      });
   }

   @Test
   @DisplayName("Take Screenshot Handles Exceptions")
   void testTakeScreenshot() throws Exception {
      // Use reflection to access private static method
      Method takeScreenshotMethod = UiTestExtension.class.getDeclaredMethod(
            "takeScreenshot", WebDriver.class, String.class
      );
      takeScreenshotMethod.setAccessible(true);

      // Create mock WebDriver that fails screenshot
      WebDriver mockDriver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));

      // Simulate screenshot failure
      when(((TakesScreenshot) mockDriver).getScreenshotAs(OutputType.BYTES))
            .thenThrow(new RuntimeException("Screenshot failed"));

      // Invoke method
      assertDoesNotThrow(() ->
            takeScreenshotMethod.invoke(null, mockDriver, "testScreenshot")
      );
   }

   @Test
   @DisplayName("Take Screenshot Handles Exceptions")
   void testTakeScreenshotHandlesExceptions() throws Exception {
      // Use reflection to access private static method
      Method takeScreenshotMethod = UiTestExtension.class.getDeclaredMethod(
            "takeScreenshot", WebDriver.class, String.class
      );
      takeScreenshotMethod.setAccessible(true);

      // Create mock WebDriver that fails screenshot
      WebDriver mockDriver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));

      // Simulate screenshot failure
      when(((TakesScreenshot) mockDriver).getScreenshotAs(OutputType.BYTES))
            .thenThrow(new RuntimeException("Screenshot failed"));

      // Invoke method
      assertDoesNotThrow(() ->
            takeScreenshotMethod.invoke(null, mockDriver, "testScreenshot")
      );
   }

   @Test
   @DisplayName("Stack Trace Checking")
   void testStackTraceChecking() throws Exception {
      // Access the lambda method for stack trace checking
      Method lambdaMethod = Arrays.stream(UiTestExtension.class.getDeclaredMethods())
            .filter(m -> m.getName().contains("lambda$postQuestCreationAssertion"))
            .filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(StackTraceElement.class))
            .findFirst()
            .orElseThrow();
      lambdaMethod.setAccessible(true);

      // Test selenium package
      StackTraceElement seleniumElement = new StackTraceElement(
            "org.openqa.selenium.WebDriver", "findElement", "WebDriver.java", 100);
      boolean seleniumResult = (boolean) lambdaMethod.invoke(null, seleniumElement);
      assertTrue(seleniumResult);

      // Test UI package
      StackTraceElement uiElement = new StackTraceElement(
            "com.theairebellion.zeus.ui.SomeClass", "method", "SomeClass.java", 100);
      boolean uiResult = (boolean) lambdaMethod.invoke(null, uiElement);
      assertTrue(uiResult);

      // Test unrelated package
      StackTraceElement otherElement = new StackTraceElement(
            "java.lang.Object", "toString", "Object.java", 100);
      boolean otherResult = (boolean) lambdaMethod.invoke(null, otherElement);
      assertFalse(otherResult);
   }

   @Test
   @DisplayName("Post Quest Creation Assertion")
   void testPostQuestCreationAssertion() throws Exception {
      // Use reflection to access private static method
      Method postQuestCreationAssertionMethod = UiTestExtension.class.getDeclaredMethod(
            "postQuestCreationAssertion", SuperQuest.class, String.class
      );
      postQuestCreationAssertionMethod.setAccessible(true);

      // Create mocks
      SuperQuest quest = mock(SuperQuest.class);
      SmartWebDriver smartWebDriver = mock(SmartWebDriver.class);
      CustomSoftAssertion softAssertion = mock(CustomSoftAssertion.class);

      // Setup behavior
      when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
      when(quest.getSoftAssertions()).thenReturn(softAssertion);

      // Execute method
      postQuestCreationAssertionMethod.invoke(null, quest, "TestMethod");

      // Verify method calls
      verify(quest).artifact(UiServiceFluent.class, SmartWebDriver.class);
      verify(quest).getSoftAssertions();
      verify(softAssertion).registerObjectForPostErrorHandling(
            eq(SmartWebDriver.class), eq(smartWebDriver));
   }

   @Test
   @DisplayName("Post Quest Creation Login")
   @Disabled
   void testPostQuestCreationLogin() throws Exception {
      // Use reflection to access private static method
      Method postQuestCreationLoginMethod = UiTestExtension.class.getDeclaredMethod(
            "postQuestCreationLogin",
            SuperQuest.class,
            DecoratorsFactory.class,
            String.class,
            String.class,
            Class.class,
            boolean.class
      );
      postQuestCreationLoginMethod.setAccessible(true);

      // Create mocks
      SuperQuest quest = mock(SuperQuest.class);
      DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
      UiServiceFluent<?> uiServiceFluent = mock(UiServiceFluent.class);
      SuperUiServiceFluent<?> superUIServiceFluent = mock(SuperUiServiceFluent.class);
      Storage storage = mock(Storage.class);
      Storage uiStorage = mock(Storage.class);

      // Setup mocks
      when(quest.getStorage()).thenReturn(storage);
      when(storage.sub(UI)).thenReturn(uiStorage);
      when(quest.enters(UiServiceFluent.class)).thenReturn(uiServiceFluent);
      when(decoratorsFactory.decorate(uiServiceFluent, SuperUiServiceFluent.class))
            .thenReturn(superUIServiceFluent);

      // The login process throws an exception because the mock WebDriver can't find elements
      // Let's verify interactions up to the point of exception
      try {
         postQuestCreationLoginMethod.invoke(
               null,
               quest,
               decoratorsFactory,
               "username",
               "password",
               TestLoginClient.class,
               true
         );
      } catch (InvocationTargetException e) {
         // Expected exception - the login fails due to missing WebDriver elements
         // This is okay for our test - we're just verifying the method was called correctly
         // In a real scenario, the WebDriver would find the elements and login
         assertNotNull(e.getCause());
         assertEquals("Logging in was not successful", e.getCause().getMessage());
      }

      // Verify the interactions that should have occurred before the exception
      verify(uiStorage).put(StorageKeysUi.USERNAME, "username");
      verify(uiStorage).put(StorageKeysUi.PASSWORD, "password");
      verify(quest).enters(UiServiceFluent.class);
      verify(decoratorsFactory).decorate(uiServiceFluent, SuperUiServiceFluent.class);
   }

   @Test
   @DisplayName("After Test Execution")
   @Disabled
   void testAfterTestExecution() {
      // Create mocks for the dependencies
      ApplicationContext appContext = mock(ApplicationContext.class);
      DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
      WebDriver driver = mock(WebDriver.class);
      ExtensionContext.Store store = mock(ExtensionContext.Store.class);
      Quest quest = mock(Quest.class);
      SuperQuest superQuest = mock(SuperQuest.class);
      SmartWebDriver smartWebDriver = mock(SmartWebDriver.class);

      // Configure extension context
      when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
      when(context.getExecutionException()).thenReturn(Optional.empty());
      when(store.get(StoreKeys.QUEST)).thenReturn(quest);

      // Setup the chain of calls that getWebDriver would make
      when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
      when(superQuest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
      when(smartWebDriver.getOriginal()).thenReturn(driver);

      // Mock the WebDriver unwrapping
      try (var unwrapDriverMock = mockStatic(UiTestExtension.class, invocation -> {
         if (invocation.getMethod().getName().equals("unwrapDriver") &&
               invocation.getArgument(0) == driver) {
            return driver;
         }
         return invocation.callRealMethod();
      })) {

         // Mock static methods
         try (var springExtensionMock = mockStatic(SpringExtension.class);
              var configMock = mockStatic(com.theairebellion.zeus.ui.config.UiFrameworkConfigHolder.class)) {

            // Configure mocks
            springExtensionMock.when(() -> SpringExtension.getApplicationContext(context))
                  .thenReturn(appContext);
            when(appContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            // Mock the framework config
            var frameworkConfig = mock(com.theairebellion.zeus.ui.config.UiFrameworkConfig.class);
            configMock.when(UiFrameworkConfigHolder::getUiFrameworkConfig)
                  .thenReturn(frameworkConfig);
            when(frameworkConfig.makeScreenshotOnPassedTest()).thenReturn(true);

            // Call the method
            extension.afterTestExecution(context);

            // Verify WebDriver was closed and quit
            verify(driver).close();
            verify(driver).quit();
         }
      }
   }

   @Test
   @DisplayName("Handle Test Execution Exception")
   void testHandleTestExecutionException() throws Exception {
      // Create mocks for the dependencies
      ApplicationContext appContext = mock(ApplicationContext.class);
      DecoratorsFactory decoratorsFactory = mock(DecoratorsFactory.class);
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));

      // Setup screenshot behavior - screenshot is taken before exception is thrown
      byte[] screenshotBytes = {1, 2, 3};
      when(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES))
            .thenReturn(screenshotBytes);

      // Use reflection to access the takeScreenshot method directly
      Method takeScreenshotMethod = UiTestExtension.class.getDeclaredMethod(
            "takeScreenshot", WebDriver.class, String.class
      );
      takeScreenshotMethod.setAccessible(true);

      // Test the takeScreenshot method directly
      try (var allureMock = mockStatic(Allure.class)) {
         takeScreenshotMethod.invoke(null, driver, "Test Display Name");

         // Verify screenshot was taken
         allureMock.verify(() ->
               Allure.addAttachment(eq("Test Display Name"), any(ByteArrayInputStream.class))
         );
      }

      // Simply test that the method throws the exception it receives
      RuntimeException testException = new RuntimeException("Test exception");
      assertThrows(RuntimeException.class, () ->
            extension.handleTestExecutionException(context, testException)
      );
   }

   @Test
   @DisplayName("Get Web Driver")
   @Disabled
   void testGetWebDriver() throws Exception {
      // Use reflection to access private method
      Method getWebDriverMethod = UiTestExtension.class.getDeclaredMethod(
            "getWebDriver", DecoratorsFactory.class, ExtensionContext.class
      );
      getWebDriverMethod.setAccessible(true);

      // Create an instance of UiTestExtension
      UiTestExtension uiTestExtension = new UiTestExtension();

      // Prepare mocks
      DecoratorsFactory mockFactory = mock(DecoratorsFactory.class);
      ExtensionContext mockContext = mock(ExtensionContext.class);
      ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
      Quest mockQuest = mock(Quest.class);
      SuperQuest mockSuperQuest = mock(SuperQuest.class);
      SmartWebDriver mockSmartWebDriver = mock(SmartWebDriver.class);
      WebDriver mockOriginalDriver = mock(WebDriver.class);

      // Setup method interactions
      when(mockContext.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
      when(mockStore.get(StoreKeys.QUEST)).thenReturn(mockQuest);
      when(mockFactory.decorate(mockQuest, SuperQuest.class)).thenReturn(mockSuperQuest);
      when(mockSuperQuest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(mockSmartWebDriver);
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockOriginalDriver);

      try {
         // Invoke method
         getWebDriverMethod.invoke(uiTestExtension, mockFactory, mockContext);
         fail("Expected an InvocationTargetException to be thrown");
      } catch (InvocationTargetException e) {
         // Assert that the cause of the exception is a RuntimeException
         assertInstanceOf(RuntimeException.class, e.getCause());
         assertEquals("Failed to unwrap WebDriver", e.getCause().getMessage());
      }

      // Verify interactions
      verify(mockContext).getStore(ExtensionContext.Namespace.GLOBAL);
      verify(mockStore).get(StoreKeys.QUEST);
      verify(mockFactory).decorate(mockQuest, SuperQuest.class);
      verify(mockSuperQuest).artifact(UiServiceFluent.class, SmartWebDriver.class);
      verify(mockSmartWebDriver).getOriginal();
   }

   @Test
   @DisplayName("Test postQuestCreationRegisterCustomServices")
   void testPostQuestCreationRegisterCustomServices() throws Exception {
      // Get the method using reflection
      Method registerMethod = UiTestExtension.class.getDeclaredMethod(
            "postQuestCreationRegisterCustomServices", SuperQuest.class);
      registerMethod.setAccessible(true);

      // Create mocks
      SuperQuest quest = mock(SuperQuest.class);
      SmartWebDriver smartWebDriver = mock(SmartWebDriver.class);

      // Setup behavior
      when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);

      // Mock the ReflectionUtil to return null (no custom class found)
      try (var reflectionUtilMock = mockStatic(ReflectionUtil.class)) {
         reflectionUtilMock.when(() ->
                     ReflectionUtil.findImplementationsOfInterface(eq(UiServiceFluent.class), anyString()))
               .thenReturn(List.of());

         // Execute the method
         registerMethod.invoke(null, quest);

         // Verify the lookup was attempted
         reflectionUtilMock.verify(() ->
               ReflectionUtil.findImplementationsOfInterface(eq(UiServiceFluent.class), nullable(String.class)));
      }
   }

   @Test
   @DisplayName("Test lambda$processInterceptRequestsAnnotation$1")
   @Disabled
   void testProcessInterceptRequestsAnnotationLambda1() throws Exception {
      // Find the lambda method
      Method[] methods = UiTestExtension.class.getDeclaredMethods();
      Method lambdaMethod = null;

      for (Method method : methods) {
         if (method.getName().contains("lambda$processInterceptRequestsAnnotation$1")) {
            lambdaMethod = method;
            break;
         }
      }

      // Execute the test if we found the method
      if (lambdaMethod != null) {
         lambdaMethod.setAccessible(true);

         // Create mocks
         SuperQuest quest = mock(SuperQuest.class);
         SmartWebDriver smartWebDriver = mock(SmartWebDriver.class);
         WebDriver regularDriver = mock(WebDriver.class);
         String[] urlSubstrings = new String[] {"test"};

         // Setup behavior
         when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
         when(smartWebDriver.getOriginal()).thenReturn(regularDriver);

         // Execute the lambda
         try {
            lambdaMethod.invoke(null, urlSubstrings, quest);
         } catch (InvocationTargetException e) {
            // Expected exception since driver is not ChromeDriver
            assertInstanceOf(RuntimeException.class, e.getCause());
            assertTrue(e.getCause().getMessage().contains("Failed to unwrap WebDriver"));
         }

         // Verify the artifact retrieval
         verify(quest).artifact(UiServiceFluent.class, SmartWebDriver.class);
      }
   }

   // Annotation method for testing
   @AuthenticateViaUiAs(
         credentials = TestLoginCredentials.class,
         type = TestLoginClient.class,
         cacheCredentials = true
   )
   void authenticateMethod() {
   }

   @InterceptRequests(requestUrlSubStrings = "test/url")
   void interceptRequestMethod() {
      // Dummy method to support the annotation test
   }

   // Dummy implementations for test classes
   public static class TestLoginCredentials implements LoginCredentials {
      @Override
      public String username() {
         return "testuser";
      }

      @Override
      public String password() {
         return "testpassword";
      }
   }

   public static class TestLoginClient extends BaseLoginClient {
      @Override
      protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
      }

      @Override
      protected By successfulLoginElementLocator() {
         return By.id("login-success");
      }
   }
}