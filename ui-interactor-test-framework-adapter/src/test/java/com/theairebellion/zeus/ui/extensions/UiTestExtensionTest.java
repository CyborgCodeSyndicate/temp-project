package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.allure.StepType;
import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.exceptions.ServiceInitializationException;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.framework.util.TestContextManager;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.config.UiFrameworkConfig;
import com.theairebellion.zeus.ui.config.UiFrameworkConfigHolder;
import com.theairebellion.zeus.ui.exceptions.AuthenticationUiException;
import com.theairebellion.zeus.ui.parameters.DataIntercept;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUiServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import com.theairebellion.zeus.ui.util.ResponseFormatter;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.launcher.LauncherSession;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.RequestId;
import org.openqa.selenium.devtools.v85.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v85.network.model.ResponseReceived;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST_CONSUMERS;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.RESPONSES;
import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

class UiTestExtensionTest extends BaseUnitUITest {

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
               eq(QUEST_CONSUMERS), any()))
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

         // 3. mockStore context & mockStore so that addQuestConsumer will find a List
         ExtensionContext ctx = mock(ExtensionContext.class);
         ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
         when(ctx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
         when(mockStore.getOrComputeIfAbsent(
               eq(QUEST_CONSUMERS), any()))
               .thenReturn(new ArrayList<Consumer<SuperQuest>>());

         // 4. invoke only the one method under test
         UiTestExtension ext = new UiTestExtension();
         m.invoke(ext, ctx, stub);

         // 5. verify that we actually grabbed the GLOBAL mockStore
         verify(ctx).getStore(ExtensionContext.Namespace.GLOBAL);
         verify(mockStore)
               .getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any());
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
         when(mockStore.getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any())).thenReturn(consumers);
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
         verify(softAssertion)
               .registerObjectForPostErrorHandling(
                     SmartWebDriver.class,
                     smartWebDriver);
      }

      @Test
      @DisplayName("Test processAuthenticateViaUiAsAnnotation")
      void testProcessAuthenticateViaUiAsAnnotation() throws Exception {
         // Get the method using reflection
         Method processMethod = UiTestExtension.class.getDeclaredMethod(
               "processAuthenticateViaUiAsAnnotation", ExtensionContext.class, Method.class);
         processMethod.setAccessible(true);

         // Create an actual annotated method for testing
         Method testMethod = BeforeTestExecutionTests.class
               .getDeclaredMethod("authenticateMethod");

         // Create mocks
         ExtensionContext mockContext = mock(ExtensionContext.class);
         ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);

         // Setup mocks
         when(mockContext.getStore(any())).thenReturn(mockStore);
         when(mockStore.getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any())).thenReturn(new ArrayList<>());

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
            verify(mockStore).getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any());
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
         when(mockStore.getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any())).thenReturn(consumers);

         // Execute the method
         registerCustomServicesMethod.invoke(extension, mockContext);

         // Verify that a consumer was added
         assertEquals(1, consumers.size());
      }

      @Test
      @DisplayName("each registered consumer actually runs")
      void eachConsumerExecutes() {
         // grab the list you already populated in your register test:
         var consumers = (List<Consumer<SuperQuest>>)
               store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);

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
                     QUEST_CONSUMERS, k -> null);
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
                     QUEST_CONSUMERS, k -> null);
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
               store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);
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
               store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);
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
                     store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);
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
               store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);
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
               store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);
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
                     QUEST_CONSUMERS, k -> null);

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
               helper.invoke(null, quest, new String[] {"foo", "bar"})
         );
      }

      @Test
      void postQuestCreationIntercept_nonChromeDriver_throws() throws Exception {
         Method helper = UiTestExtension.class
               .getDeclaredMethod("postQuestCreationIntercept", SuperQuest.class, String[].class);
         helper.setAccessible(true);

         // build your non-Chrome SmartWebDriver as before…
         interface ProxyDriver extends WebDriver {
            WebDriver getOriginal();
         }
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
               helper.invoke(null, quest, new String[] {"foo"});
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

      @Test
      @DisplayName("postQuestCreationAssertion registers custom assertion with correct predicate")
      void testPostQuestCreationAssertionRegistersCustomAssertion() throws Exception {
         // 1) mock the static CustomSoftAssertion
         try (var csa = mockStatic(CustomSoftAssertion.class)) {
            // 2) prepare a fake SuperQuest
            SuperQuest quest = mock(SuperQuest.class);
            SmartWebDriver smartWebDriver = mock(SmartWebDriver.class);
            when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class))
                  .thenReturn(smartWebDriver);
            CustomSoftAssertion soft = mock(CustomSoftAssertion.class);
            when(quest.getSoftAssertions()).thenReturn(soft);

            // 3) invoke registerAssertionConsumer so that a Consumer<SuperQuest>
            //    gets added to the GLOBAL store
            Method reg = UiTestExtension.class
                  .getDeclaredMethod("registerAssertionConsumer", ExtensionContext.class);
            reg.setAccessible(true);
            reg.invoke(extension, context);

            @SuppressWarnings("unchecked")
            var consumers = (List<Consumer<SuperQuest>>)
                  store.getOrComputeIfAbsent(QUEST_CONSUMERS, k -> null);
            assertEquals(1, consumers.size(), "one assertion-consumer must be registered");

            // 4) fire the consumer
            consumers.get(0).accept(quest);

            // 5) verify we wired up the soft-assert handler
            verify(soft).registerObjectForPostErrorHandling(
                  SmartWebDriver.class,
                  smartWebDriver
            );

            // 6) capture the predicate
            @SuppressWarnings("unchecked")
            ArgumentCaptor<Predicate<StackTraceElement[]>> predCap =
                  ArgumentCaptor.forClass((Class) Predicate.class);

            csa.verify(() -> CustomSoftAssertion.registerCustomAssertion(
                  eq(SmartWebDriver.class), any(BiConsumer.class), predCap.capture()
            ));

            Predicate<StackTraceElement[]> stackPred = predCap.getValue();

            // 7) pull the two package constants out of the real class
            Field selField = UiTestExtension.class.getDeclaredField("SELENIUM_PACKAGE");
            selField.setAccessible(true);
            String selPkg = (String) selField.get(null);

            Field uiField = UiTestExtension.class.getDeclaredField("UI_MODULE_PACKAGE");
            uiField.setAccessible(true);
            String uiPkg = (String) uiField.get(null);

            // 8) build three synthetic stack traces
            StackTraceElement seleniumHit =
                  new StackTraceElement(selPkg + ".Foo", "m", "F.java", 1);
            StackTraceElement uiHit =
                  new StackTraceElement(uiPkg + ".Bar", "m", "B.java", 2);
            StackTraceElement miss =
                  new StackTraceElement("java.lang.String", "s", "String.java", 3);

            // 9) assert predicate behavior
            assertTrue(
                  stackPred.test(new StackTraceElement[] {seleniumHit}),
                  "should match when className contains SELENIUM_PACKAGE"
            );
            assertTrue(
                  stackPred.test(new StackTraceElement[] {uiHit}),
                  "should match when className contains UI_MODULE_PACKAGE"
            );
            assertFalse(
                  stackPred.test(new StackTraceElement[] {miss}),
                  "should not match unrelated classes"
            );

         }
      }

      @Test
      @DisplayName("postQuestCreationAssertion’s custom‐assertion lambda actually takes a screenshot")
      void testPostQuestCreationAssertionCustomAssertionLambda() throws Exception {
         // 1) set up quest + smartWebDriver + softAssertions
         SuperQuest quest = mock(SuperQuest.class);
         SmartWebDriver smart = mock(SmartWebDriver.class);
         when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class))
               .thenReturn(smart);

         CustomSoftAssertion softAssertions = mock(CustomSoftAssertion.class);
         when(quest.getSoftAssertions()).thenReturn(softAssertions);

         // 2) make the “underlying” driver that can take a screenshot
         WebDriver underlying = mock(WebDriver.class,
               withSettings().extraInterfaces(TakesScreenshot.class));
         when(((TakesScreenshot) underlying)
               .getScreenshotAs(OutputType.BYTES))
               .thenReturn(new byte[] {0x11, 0x22, 0x33});

         // 3) wrap it in a ProxyDriver so it has getOriginal()
         ProxyDriver proxy = mock(ProxyDriver.class);
         when(proxy.getOriginal()).thenReturn(underlying);

         // 4) stub smart.getOriginal() to hand back that proxy
         when(smart.getOriginal()).thenReturn(proxy);

         String testName = "myTestName";

         // 5) grab the private helper
         Method helper = UiTestExtension.class
               .getDeclaredMethod("postQuestCreationAssertion", SuperQuest.class, String.class);
         helper.setAccessible(true);

         // 6) static‐mock the registration so we can intercept the lambda
         try (var cs = mockStatic(CustomSoftAssertion.class);
              var as = mockStatic(Allure.class)) {

            cs.when(() -> CustomSoftAssertion.registerCustomAssertion(
                        eq(SmartWebDriver.class),
                        any(BiConsumer.class),
                        any(Predicate.class)))
                  .thenAnswer(inv -> {
                     @SuppressWarnings("unchecked")
                     BiConsumer<AssertionError, SmartWebDriver> lambda =
                           inv.getArgument(1);

                     // invoke it as if a soft‐assert failed
                     lambda.accept(new AssertionError("boom"), smart);

                     // verify that takeScreenshot → Allure.addAttachment(...)
                     as.verify(() -> Allure.addAttachment(
                           eq("soft_assert_failure_" + testName),
                           any(ByteArrayInputStream.class))
                     );
                     return null;
                  });

            // 7) run the helper, which hits our stub above
            helper.invoke(null, quest, testName);

            // sanity‐check the other registration call
            verify(softAssertions).registerObjectForPostErrorHandling(
                  SmartWebDriver.class,
                  smart
            );
            cs.verify(() -> CustomSoftAssertion.registerCustomAssertion(
                        eq(SmartWebDriver.class), any(BiConsumer.class), any(Predicate.class)),
                  times(1));
         }
      }

      @Test
      @DisplayName("processAuthenticateViaUiAsAnnotation consumer actually runs the postQuestCreationLogin lambda")
      void testProcessAuthenticateViaUiAsAnnotationConsumerInvocation() throws Exception {
         // 1) reflectively grab the private helper
         Method process = UiTestExtension.class
               .getDeclaredMethod("processAuthenticateViaUiAsAnnotation",
                     ExtensionContext.class, Method.class);
         process.setAccessible(true);

         // 2) prepare a mock ExtensionContext + GLOBAL mockStore
         ExtensionContext ctx = mock(ExtensionContext.class);
         ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);
         when(ctx.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(mockStore);
         @SuppressWarnings("unchecked")
         List<Consumer<SuperQuest>> consumers = new ArrayList<>();
         when(mockStore.getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any()))
               .thenReturn(consumers);

         // 3) identify the @AuthenticateViaUiAs stub method in *this* test class
         Method stub = BeforeTestExecutionTests.class
               .getDeclaredMethod("authenticateGood");
         assertNotNull(stub.getAnnotation(AuthenticateViaUiAs.class),
               "sanity–check: our stub must have @AuthenticateViaUiAs");

         // 4) stub SpringExtension → DecoratorsFactory → DummyLoginClient
         try (var spring = mockStatic(SpringExtension.class)) {
            ApplicationContext app = mock(ApplicationContext.class);
            spring.when(() -> SpringExtension.getApplicationContext(ctx))
                  .thenReturn(app);

            DecoratorsFactory df = mock(DecoratorsFactory.class);
            when(app.getBean(DecoratorsFactory.class)).thenReturn(df);

            // whenever decorate(...) is called with DummyLoginClient.class, return a new instance
            when(df.decorate(any(SuperQuest.class), eq(DummyLoginClient.class)))
                  .thenAnswer(i -> new DummyLoginClient());

            // 5) invoke ONLY the helper under test
            UiTestExtension ext = new UiTestExtension();
            process.invoke(ext, ctx, stub);

            // 6) now one consumer must have been registered
            assertEquals(1, consumers.size(),
                  "should have exactly one login consumer");
            Consumer<SuperQuest> consumer = consumers.get(0);

            // 7) fire the consumer; it will run the red‐line lambda, then inevitably NPE inside performLoginAndCache
            SuperQuest quest = mock(SuperQuest.class);
            // stub just enough of quest so that registerQuestConsumer() doesn't NPE before our lambda:
            when(quest.getStorage()).thenReturn(mock(Storage.class));

            // the key point: this assertThrows ensures the lambda *did* execute its body
            assertThrows(Throwable.class,
                  () -> consumer.accept(quest),
                  "invoking the login‐consumer must at least execute the postQuestCreationLogin lambda");
         }
      }

      @Test
      @DisplayName("– bad credentials ctor → AuthenticationUiException thrown")
      void testProcessAuthenticateViaUiAsAnnotationBad() throws Exception {
         Method helper = UiTestExtension.class
               .getDeclaredMethod("processAuthenticateViaUiAsAnnotation", ExtensionContext.class, Method.class);
         helper.setAccessible(true);

         // stub getTestMethod → our bad‐ctor method
         Method bad = getClass().getDeclaredMethod("authenticateBad");
         when(context.getTestMethod()).thenReturn(Optional.of(bad));
         when(context.getStore(any())).thenReturn(store);
         when(store.getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any()))
               .thenReturn(new ArrayList<>());

         try (var spring = mockStatic(SpringExtension.class)) {
            ApplicationContext app = mock(ApplicationContext.class);
            DecoratorsFactory df = mock(DecoratorsFactory.class);
            spring.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(app);
            when(app.getBean(DecoratorsFactory.class)).thenReturn(df);

            InvocationTargetException ive = assertThrows(
                  InvocationTargetException.class,
                  () -> helper.invoke(new UiTestExtension(), context, bad)
            );
            // unwrap the real cause
            Throwable real = ive.getCause();
            assertTrue(real instanceof AuthenticationUiException);
            assertTrue(real.getMessage().contains("Failed to instantiate login credentials"));
         }
      }

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

      @InterceptRequests(requestUrlSubStrings = "ONE")
      void interceptRequestOneEnumMatch() { /* no-op */ }

      @InterceptRequests(requestUrlSubStrings = {})
      void interceptRequestEmpty() { /* no-op */ }

      @InterceptRequests(requestUrlSubStrings = "foo")
      void interceptRequestMethod() { /* no-op */ }

      interface HasOriginal extends WebDriver {
         ChromeDriver getOriginal();
      }

      interface ProxyDriver extends WebDriver {
         WebDriver getOriginal();
      }

      // a) a dummy credentials impl whose ctor always succeeds
      static class DummyLoginCredentials implements LoginCredentials {
         @Override
         public String username() {
            return "dummyUser";
         }

         @Override
         public String password() {
            return "dummyPass";
         }
      }

      // b) a “bad” credentials impl whose ctor always blows up
      static class BadLoginCredentials implements LoginCredentials {
         public BadLoginCredentials() {
            throw new RuntimeException("bad ctor");
         }

         @Override
         public String username() {
            return null;
         }

         @Override
         public String password() {
            return null;
         }
      }

      // c) a dummy BaseLoginClient so that we can pass its Class literal
      static class DummyLoginClient extends BaseLoginClient {
         @Override
         protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String user, String pass) {
            // no‐op
         }

         @Override
         protected By successfulLoginElementLocator() {
            return By.tagName("body");
         }
      }

      @AuthenticateViaUiAs(
            credentials = TestLoginCredentials.class,
            type = TestLoginClient.class,
            cacheCredentials = true
      )
      void authenticateMethod() { /* no‐op */}

      @AuthenticateViaUiAs(
            credentials = DummyLoginCredentials.class,
            type = DummyLoginClient.class,    // ← must be a Class<? extends BaseLoginClient>
            cacheCredentials = false
      )
      void authenticateGood() { /* no‐op */ }

      @AuthenticateViaUiAs(
            credentials = BadLoginCredentials.class,
            type = DummyLoginClient.class,    // ← same here
            cacheCredentials = false
      )
      void authenticateBad() { /* no‐op */ }

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
            // no-op stub: we don't perform a real UI login in this test client
         }

         @Override
         protected By successfulLoginElementLocator() {
            return By.id("login-success");
         }
      }
   }

   @Nested
   @DisplayName("postQuestCreationIntercept helper")
   class PostQuestCreationInterceptTests {
      @Mock
      SuperQuest quest;
      @Mock
      SmartWebDriver smartWebDriver;
      @Mock
      ChromeDriver chromeDriver;
      @Mock
      DevTools devTools;
      @Mock
      Storage rootStorage;
      @Mock
      Storage uiStorage;

      private Method helper;

      @BeforeEach
      void setUp() throws Exception {
         MockitoAnnotations.openMocks(this);

         // 1) grab the private static helper
         helper = UiTestExtension.class
               .getDeclaredMethod("postQuestCreationIntercept",
                     SuperQuest.class, String[].class);
         helper.setAccessible(true);

         // 2a) create a little proxy that _does_ have getOriginal()
         interface ProxyDriver extends WebDriver {
            ChromeDriver getOriginal();
         }
         ProxyDriver proxy = mock(ProxyDriver.class);

         // 2b) chain them: quest→smart→proxy→chrome→devTools
         when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class))
               .thenReturn(smartWebDriver);
         when(smartWebDriver.getOriginal()).thenReturn(proxy);
         when(proxy.getOriginal()).thenReturn(chromeDriver);
         when(chromeDriver.getDevTools()).thenReturn(devTools);

         // 3) wire storage
         when(quest.getStorage()).thenReturn(rootStorage);
         when(rootStorage.sub(UI)).thenReturn(uiStorage);
         // first get(...) returns null so that addResponseInStorage will create a new list
         when(uiStorage.get(eq(RESPONSES), any(ParameterizedTypeReference.class)))
               .thenReturn(null);
      }

      @Test
      @DisplayName("normal body under 10k → stored verbatim")
      void normalBodyGetsStored() throws Exception {
         // arrange: capture what ends up in storage
         List<ApiResponse> captured = new ArrayList<>();
         doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<ApiResponse> list = (List<ApiResponse>) inv.getArgument(1);
            captured.addAll(list);
            return null;
         }).when(uiStorage).put(eq(RESPONSES), anyList());

         // act: run the helper
         helper.invoke(null, quest, new String[] {"foo"});

         // now verify that addListener was called exactly twice,
         // and capture the two Consumers in order:
         @SuppressWarnings("unchecked")
         ArgumentCaptor<Consumer<?>> listenerCaptor =
               ArgumentCaptor.forClass((Class) Consumer.class);

         verify(devTools, times(2)).addListener(any(), listenerCaptor.capture());

         // the first listener is the RequestWillBeSent consumer:
         @SuppressWarnings("unchecked")
         Consumer<RequestWillBeSent> reqListener =
               (Consumer<RequestWillBeSent>) listenerCaptor.getAllValues().get(0);

         // the second is the ResponseReceived consumer:
         @SuppressWarnings("unchecked")
         Consumer<ResponseReceived> resListener =
               (Consumer<ResponseReceived>) listenerCaptor.getAllValues().get(1);

         // now simulate a request event
         var fakeReqEvent = mock(RequestWillBeSent.class);
         var fakeReq      = mock(org.openqa.selenium.devtools.v85.network.model.Request.class);
         when(fakeReq.getMethod()).thenReturn("GET");
         when(fakeReqEvent.getRequest()).thenReturn(fakeReq);
         RequestId rid = new RequestId("rid-1");
         when(fakeReqEvent.getRequestId()).thenReturn(rid);

         reqListener.accept(fakeReqEvent);

         // simulate a matching response event
         var fakeRespEvent = mock(ResponseReceived.class);
         var fakeResp = mock(org.openqa.selenium.devtools.v85.network.model.Response.class);
         when(fakeResp.getStatus()).thenReturn(200);
         when(fakeResp.getUrl()).thenReturn("https://example.com/foo");
         when(fakeRespEvent.getResponse()).thenReturn(fakeResp);
         when(fakeRespEvent.getRequestId()).thenReturn(rid);

         // stub the body to “hello”
         var bodyResponse = mock(Network.GetResponseBodyResponse.class);
         when(bodyResponse.getBody()).thenReturn("hello");
         when(devTools.send(any())).thenReturn(bodyResponse);

         resListener.accept(fakeRespEvent);

         // assert that exactly one ApiResponse landed in storage
         assertEquals(1, captured.size());
         ApiResponse r = captured.get(0);
         assertEquals("GET",            r.getMethod());
         assertEquals(200,              r.getStatus());
         assertEquals("https://example.com/foo", r.getUrl());
         assertEquals("hello",          r.getBody());
      }

      @Test
      @DisplayName("body >10k → truncated message")
      void longBodyGetsTruncated() throws Exception {
         // arrange: capture whatever ends up in storage
         List<ApiResponse> captured = new ArrayList<>();
         doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<ApiResponse> list = (List<ApiResponse>) inv.getArgument(1);
            captured.addAll(list);
            return null;
         }).when(uiStorage).put(eq(RESPONSES), anyList());

         // act: invoke the helper with "foo" as our intercept substring
         helper.invoke(null, quest, new String[] {"foo"});

         // capture both addListener calls (request & response)
         @SuppressWarnings("unchecked")
         ArgumentCaptor<Consumer<?>> listenerCaptor =
               ArgumentCaptor.forClass((Class) Consumer.class);
         verify(devTools, times(2)).addListener(any(), listenerCaptor.capture());

         // first listener is the RequestWillBeSent consumer
         @SuppressWarnings("unchecked")
         Consumer<RequestWillBeSent> reqListener =
               (Consumer<RequestWillBeSent>) listenerCaptor.getAllValues().get(0);

         // second listener is the ResponseReceived consumer
         @SuppressWarnings("unchecked")
         Consumer<ResponseReceived> resListener =
               (Consumer<ResponseReceived>) listenerCaptor.getAllValues().get(1);

         // fire a fake request event so we record the rid→method mapping
         var fakeReqEvent = mock(RequestWillBeSent.class);
         var fakeReq     = mock(org.openqa.selenium.devtools.v85.network.model.Request.class);
         when(fakeReq.getMethod()).thenReturn("POST");
         when(fakeReqEvent.getRequest()).thenReturn(fakeReq);
         RequestId rid = new RequestId("rid-2");
         when(fakeReqEvent.getRequestId()).thenReturn(rid);
         reqListener.accept(fakeReqEvent);

         // now fire a fake response event whose URL contains "foo"
         var fakeRespEvent = mock(ResponseReceived.class);
         var fakeResp      = mock(org.openqa.selenium.devtools.v85.network.model.Response.class);
         when(fakeResp.getStatus()).thenReturn(201);
         when(fakeResp.getUrl()).thenReturn("https://big/foo");   // <-- ensure it matches "foo"
         when(fakeRespEvent.getResponse()).thenReturn(fakeResp);
         when(fakeRespEvent.getRequestId()).thenReturn(rid);

         // stub a huge body (>10k chars)
         String big = "x".repeat(10_001);
         var bigBodyResp = mock(Network.GetResponseBodyResponse.class);
         when(bigBodyResp.getBody()).thenReturn(big);
         when(devTools.send(any())).thenReturn(bigBodyResp);

         // fire the response listener
         resListener.accept(fakeRespEvent);

         // assert that exactly one ApiResponse was stored and that its body was truncated
         assertEquals(1, captured.size(), "should have stored exactly one truncated response");
         String body = captured.get(0).getBody();
         assertTrue(
               body.startsWith("Response body truncated. Original length: 10001 characters."),
               "truncation prefix"
         );
         assertTrue(
               body.contains(big.substring(0, 100)),
               "should include the first 100 chars of the original body"
         );
      }

      @Test
      @DisplayName("exception fetching body → error stored")
      void exceptionFetchingBody() throws Exception {
         // arrange: capture whatever ends up in storage
         List<ApiResponse> captured = new ArrayList<>();
         doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<ApiResponse> list = (List<ApiResponse>) inv.getArgument(1);
            captured.addAll(list);
            return null;
         }).when(uiStorage).put(eq(RESPONSES), anyList());

         // act: invoke the helper
         helper.invoke(null, quest, new String[] {"foo"});

         // capture both addListener calls (request & response)
         @SuppressWarnings("unchecked")
         ArgumentCaptor<Consumer<?>> listenerCaptor =
               ArgumentCaptor.forClass((Class) Consumer.class);
         verify(devTools, times(2)).addListener(any(), listenerCaptor.capture());

         // extract the two listeners
         @SuppressWarnings("unchecked")
         Consumer<RequestWillBeSent> reqListener =
               (Consumer<RequestWillBeSent>) listenerCaptor.getAllValues().get(0);
         @SuppressWarnings("unchecked")
         Consumer<ResponseReceived> resListener =
               (Consumer<ResponseReceived>) listenerCaptor.getAllValues().get(1);

         // fire a fake request so we map rid → method
         var fakeReq = mock(org.openqa.selenium.devtools.v85.network.model.Request.class);
         when(fakeReq.getMethod()).thenReturn("DELETE");
         var fakeReqEvent = mock(RequestWillBeSent.class);
         when(fakeReqEvent.getRequest()).thenReturn(fakeReq);
         RequestId rid = new RequestId("rid-3");
         when(fakeReqEvent.getRequestId()).thenReturn(rid);
         reqListener.accept(fakeReqEvent);

         // now fire a fake response whose URL contains "foo"
         var fakeResp = mock(org.openqa.selenium.devtools.v85.network.model.Response.class);
         when(fakeResp.getStatus()).thenReturn(500);
         when(fakeResp.getUrl()).thenReturn("https://error/foo");
         var fakeRespEvent = mock(ResponseReceived.class);
         when(fakeRespEvent.getResponse()).thenReturn(fakeResp);
         when(fakeRespEvent.getRequestId()).thenReturn(rid);

         // stub send(...) to throw
         when(devTools.send(any()))
               .thenThrow(new RuntimeException("boom!"));

         // invoke the response listener
         resListener.accept(fakeRespEvent);

         // assert we recorded exactly one response, with the error message
         assertEquals(1, captured.size(), "should have recorded exactly one ApiResponse");
         assertEquals(
               "Error retrieving response body: boom!",
               captured.get(0).getBody(),
               "body should contain the caught exception message"
         );
      }

      @Test
      @DisplayName("non-matching URL → skips body retrieval and leaves body null")
      void nonMatchingUrlSkipsBodyRetrieval() throws Exception {
         // arrange: capture whatever ends up in storage
         List<ApiResponse> captured = new ArrayList<>();
         doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<ApiResponse> list = (List<ApiResponse>) inv.getArgument(1);
            captured.addAll(list);
            return null;
         }).when(uiStorage).put(eq(RESPONSES), anyList());

         // act: invoke with a pattern that will NOT match the response URL
         helper.invoke(null, quest, new String[]{"nomatch"});

         // grab both listeners at once
         @SuppressWarnings("unchecked")
         ArgumentCaptor<Consumer<?>> cap = ArgumentCaptor.forClass((Class) Consumer.class);
         verify(devTools, times(2)).addListener(any(), cap.capture());

         // unpack them
         @SuppressWarnings("unchecked")
         Consumer<RequestWillBeSent> onReq = (Consumer<RequestWillBeSent>) cap.getAllValues().get(0);
         @SuppressWarnings("unchecked")
         Consumer<ResponseReceived> onRes = (Consumer<ResponseReceived>) cap.getAllValues().get(1);

         // simulate the request
         var reqEvent = mock(RequestWillBeSent.class);
         var req = mock(org.openqa.selenium.devtools.v85.network.model.Request.class);
         when(req.getMethod()).thenReturn("PATCH");
         when(reqEvent.getRequest()).thenReturn(req);
         RequestId rid = new RequestId("x");
         when(reqEvent.getRequestId()).thenReturn(rid);
         onReq.accept(reqEvent);

         // simulate a response whose URL does *not* contain "nomatch"
         var resEvent = mock(ResponseReceived.class);
         var res = mock(org.openqa.selenium.devtools.v85.network.model.Response.class);
         when(res.getStatus()).thenReturn(418);
         when(res.getUrl()).thenReturn("https://example.com/foo");
         when(resEvent.getResponse()).thenReturn(res);
         when(resEvent.getRequestId()).thenReturn(rid);
         onRes.accept(resEvent);

         // assert we got one entry, with method & status, but body stayed null
         assertEquals(1, captured.size());
         ApiResponse out = captured.get(0);
         assertEquals("PATCH", out.getMethod());
         assertEquals(418, out.getStatus());
         assertEquals("https://example.com/foo", out.getUrl());
         assertNull(out.getBody(), "body should remain null when URL doesn’t match");
      }

      @Test
      @DisplayName("matching URL with null body → stored as null")
      void matchingUrlWithNullBodyRecorded() throws Exception {
         // arrange: capture storage
         List<ApiResponse> captured = new ArrayList<>();
         doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<ApiResponse> list = (List<ApiResponse>) inv.getArgument(1);
            captured.addAll(list);
            return null;
         }).when(uiStorage).put(eq(RESPONSES), anyList());

         // act: invoke with pattern “example” so we match the URL
         helper.invoke(null, quest, new String[]{"example"});

         // grab both listeners
         @SuppressWarnings("unchecked")
         ArgumentCaptor<Consumer<?>> cap = ArgumentCaptor.forClass((Class) Consumer.class);
         verify(devTools, times(2)).addListener(any(), cap.capture());

         @SuppressWarnings("unchecked")
         Consumer<RequestWillBeSent> onReq = (Consumer<RequestWillBeSent>) cap.getAllValues().get(0);
         @SuppressWarnings("unchecked")
         Consumer<ResponseReceived> onRes = (Consumer<ResponseReceived>) cap.getAllValues().get(1);

         // fire the request → method mapping
         var reqEvent = mock(RequestWillBeSent.class);
         var req = mock(org.openqa.selenium.devtools.v85.network.model.Request.class);
         when(req.getMethod()).thenReturn("HEAD");
         when(reqEvent.getRequest()).thenReturn(req);
         RequestId rid = new RequestId("y");
         when(reqEvent.getRequestId()).thenReturn(rid);
         onReq.accept(reqEvent);

         // fire a matching response
         var resEvent = mock(ResponseReceived.class);
         var res = mock(org.openqa.selenium.devtools.v85.network.model.Response.class);
         when(res.getStatus()).thenReturn(307);
         when(res.getUrl()).thenReturn("https://example.com/null");
         when(resEvent.getResponse()).thenReturn(res);
         when(resEvent.getRequestId()).thenReturn(rid);

         // stub send(...) to return a body‐response whose getBody() is null
         var bodyResp = mock(Network.GetResponseBodyResponse.class);
         when(bodyResp.getBody()).thenReturn(null);
         when(devTools.send(any())).thenReturn(bodyResp);

         onRes.accept(resEvent);

         // assert that we still recorded one response, but its body is null
         assertEquals(1, captured.size());
         assertNull(captured.get(0).getBody(), "body should be recorded as null when getBody() returns null");
      }
   }

   @Nested
   @ExtendWith(MockitoExtension.class)
   @MockitoSettings(strictness = Strictness.LENIENT)
   class AfterTestExecutionTests {

      @Mock
      ExtensionContext context;
      @Mock
      ExtensionContext.Store globalStore;
      @Mock
      ApplicationContext appCtx;
      @Mock
      DecoratorsFactory decoratorsFactory;
      @Mock
      Quest quest;
      @Mock
      SuperQuest superQuest;
      @Mock
      SmartWebDriver smartWebDriver;
      WebDriver originalDriver;

      @BeforeEach
      void setUp() {
         // TestContextManager.getSuperQuest(context) ends up returning superQuest
         when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(globalStore);
         when(globalStore.get(StoreKeys.QUEST)).thenReturn(quest);

         // decorate quest → superQuest → smartWebDriver → originalDriver
         when(decoratorsFactory.decorate(quest, SuperQuest.class)).thenReturn(superQuest);
         when(superQuest.artifact(UiServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);

         // give smartWebDriver an unwrap-able driver
         originalDriver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
         when(smartWebDriver.getOriginal()).thenReturn(originalDriver);

         when(context.getDisplayName()).thenReturn("myTest");
      }

      @Test
      void afterTestExecution_noResponses_takesScreenshotAndCloses() {
         // 1) test passed → no exception
         when(context.getExecutionException()).thenReturn(Optional.empty());

         // 2) stub out storage on superQuest
         Storage root = mock(Storage.class), uiSub = mock(Storage.class);
         when(superQuest.getStorage()).thenReturn(root);
         when(root.sub(UI)).thenReturn(uiSub);
         when(uiSub.getAllByClass(RESPONSES, ApiResponse.class)).thenReturn(List.of());

         // 3) static‐mock SpringExtension + UiFrameworkConfigHolder
         try (var spring = mockStatic(SpringExtension.class);
              var cfgHolder = mockStatic(UiFrameworkConfigHolder.class)) {

            spring.when(() -> SpringExtension.getApplicationContext(context))
                  .thenReturn(appCtx);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            UiFrameworkConfig cfg = mock(UiFrameworkConfig.class);
            cfgHolder.when(UiFrameworkConfigHolder::getUiFrameworkConfig)
                  .thenReturn(cfg);
            when(cfg.makeScreenshotOnPassedTest()).thenReturn(true);

            // invoke the extension
            new UiTestExtension().afterTestExecution(context);
         }

         // verify screenshot was attempted
         verify((TakesScreenshot) originalDriver).getScreenshotAs(OutputType.BYTES);
         // verify driver closed & quit
         verify(originalDriver).close();
         verify(originalDriver).quit();
      }

      @Test
      @DisplayName("when exception present → skip screenshot, still close/quit")
      void afterTestExecution_withException_skipsScreenshot_butCloses() {
         // 1) executionException.isPresent()
         when(context.getExecutionException()).thenReturn(Optional.of(new RuntimeException("boom")));

         // 2) stub out storage as before (no responses)
         Storage root = mock(Storage.class), uiSub = mock(Storage.class);
         when(superQuest.getStorage()).thenReturn(root);
         when(root.sub(UI)).thenReturn(uiSub);
         when(uiSub.getAllByClass(RESPONSES, ApiResponse.class)).thenReturn(List.of());

         // 3) static‐mock SpringExtension & UiFrameworkConfigHolder
         try (var spring = mockStatic(SpringExtension.class);
              var cfgHolder = mockStatic(UiFrameworkConfigHolder.class)) {

            spring.when(() -> SpringExtension.getApplicationContext(context))
                  .thenReturn(appCtx);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            UiFrameworkConfig cfg = mock(UiFrameworkConfig.class);
            cfgHolder.when(UiFrameworkConfigHolder::getUiFrameworkConfig)
                  .thenReturn(cfg);
            // even though makeScreenshotOnPassedTest() → true, screenshot block is guarded by exception
            when(cfg.makeScreenshotOnPassedTest()).thenReturn(true);

            new UiTestExtension().afterTestExecution(context);
         }

         // verify we never tried to takeScreenshot
         verify((TakesScreenshot) originalDriver, never())
               .getScreenshotAs(any());

         // but we still close & quit
         verify(originalDriver).close();
         verify(originalDriver).quit();
      }

      @Test
      @DisplayName("when keepDriverForSession=true → skip close/quit entirely")
      void afterTestExecution_keepDriverForSession_skipsCloseAndQuit() {
         // 1) no exception
         when(context.getExecutionException()).thenReturn(Optional.empty());

         // 2) stub out storage as before
         Storage root = mock(Storage.class), uiSub = mock(Storage.class);
         when(superQuest.getStorage()).thenReturn(root);
         when(root.sub(UI)).thenReturn(uiSub);
         when(uiSub.getAllByClass(RESPONSES, ApiResponse.class)).thenReturn(List.of());

         // 3) static‐mock SpringExtension & UiFrameworkConfigHolder
         try (var spring = mockStatic(SpringExtension.class);
              var cfgHolder = mockStatic(UiFrameworkConfigHolder.class)) {

            spring.when(() -> SpringExtension.getApplicationContext(context))
                  .thenReturn(appCtx);
            when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            UiFrameworkConfig cfg = mock(UiFrameworkConfig.class);
            cfgHolder.when(UiFrameworkConfigHolder::getUiFrameworkConfig)
                  .thenReturn(cfg);
            // disable screenshot branch
            when(cfg.makeScreenshotOnPassedTest()).thenReturn(false);

            // 4) now stub keepDriverForSession = true
            when(smartWebDriver.isKeepDriverForSession()).thenReturn(true);

            new UiTestExtension().afterTestExecution(context);
         }

         // since keepDriverForSession==true, we never close or quit
         verify(originalDriver, never()).close();
         verify(originalDriver, never()).quit();
      }
   }

   @Nested
   @DisplayName("afterTestExecution – non-empty responses")
   class AfterTestExecution_NonEmptyResponses {

      @Mock
      ExtensionContext context;
      @Mock
      ExtensionContext.Store globalStore;
      @Mock
      Quest quest;

      @Mock
      ApplicationContext appCtx;
      @Mock
      DecoratorsFactory decoratorsFactory;
      @Mock
      SuperQuest superQuest;
      @Mock
      SmartWebDriver smartDriver;

      // unwrapDriver(smartDriver) → wrappedProxy
      WebDriver wrappedProxy;

      @Mock
      Storage rootStorage;
      @Mock
      Storage uiStorage;
      @Mock
      UiFrameworkConfig uiCfg;

      MockedStatic<SpringExtension> springExt;
      MockedStatic<UiFrameworkConfigHolder> cfgHolder;
      MockedStatic<TestContextManager> ctxMgr;
      MockedStatic<ResponseFormatter> respFmt;
      MockedStatic<Allure> allure;

      public interface HasOriginal extends WebDriver {
         WebDriver getOriginal();
      }

      @BeforeEach
      void setup() {
         MockitoAnnotations.openMocks(this);

         // — stub GLOBAL store → quest for getSmartWebDriver()
         when(context.getStore(ExtensionContext.Namespace.GLOBAL))
               .thenReturn(globalStore);
         when(globalStore.get(StoreKeys.QUEST)).thenReturn(quest);

         // — SpringExtension → DecoratorsFactory
         springExt = mockStatic(SpringExtension.class);
         springExt.when(() -> SpringExtension.getApplicationContext(context))
               .thenReturn(appCtx);
         when(appCtx.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

         // — getSmartWebDriver(...) chain
         when(decoratorsFactory.decorate(quest, SuperQuest.class))
               .thenReturn(superQuest);
         when(superQuest.artifact(UiServiceFluent.class, SmartWebDriver.class))
               .thenReturn(smartDriver);

         // — unwrapDriver(smartDriver) → wrappedProxy
         wrappedProxy = mock(WebDriver.class, withSettings().extraInterfaces(HasOriginal.class));
         when(smartDriver.getOriginal()).thenReturn(wrappedProxy);

         // — skip screenshot branch
         cfgHolder = mockStatic(UiFrameworkConfigHolder.class);
         cfgHolder.when(UiFrameworkConfigHolder::getUiFrameworkConfig)
               .thenReturn(uiCfg);
         when(uiCfg.makeScreenshotOnPassedTest()).thenReturn(false);

         // — getSuperQuest(...) → storage
         ctxMgr = mockStatic(TestContextManager.class);
         ctxMgr.when(() -> TestContextManager.getSuperQuest(context))
               .thenReturn(superQuest);

         // — storage stub with one ApiResponse
         when(superQuest.getStorage()).thenReturn(rootStorage);
         when(rootStorage.sub(UI)).thenReturn(uiStorage);
         ApiResponse dummy = new ApiResponse("u", "M", 200);
         when(uiStorage.getAllByClass(RESPONSES, ApiResponse.class))
               .thenReturn(List.of(dummy));

         // — ResponseFormatter stub
         respFmt = mockStatic(ResponseFormatter.class);
         respFmt.when(() -> ResponseFormatter.formatResponses(List.of(dummy)))
               .thenReturn("<html/>");

         // — Allure stub
         allure = mockStatic(Allure.class);
      }

      @AfterEach
      void tearDown() {
         springExt.close();
         cfgHolder.close();
         ctxMgr.close();
         respFmt.close();
         allure.close();
      }

      @Test
      @DisplayName("should attach intercepted-requests when storage is non-empty")
      void afterTestExecution_withNonEmptyResponses_attaches() {
         new UiTestExtension().afterTestExecution(context);

         // verify that we attached the HTML
         allure.verify(() ->
               Allure.addAttachment(
                     eq("Intercepted Requests"),
                     eq("text/html"),
                     any(ByteArrayInputStream.class),
                     eq(".html")
               )
         );

         // now verify we closed & quit the unwrapped driver
         verify(wrappedProxy).close();
         verify(wrappedProxy).quit();
      }
   }

   @Nested
   @ExtendWith(MockitoExtension.class)
   @DisplayName("handleTestExecutionException")
   class HandleTestExecutionExceptionTests {

      @Mock
      ExtensionContext context;
      @Mock
      ExtensionContext.Store globalStore;
      @Mock
      ApplicationContext appCtx;
      @Mock
      DecoratorsFactory decoratorsFactory;
      @Mock
      Quest quest;
      @Mock
      SuperQuest superQuest;
      @Mock
      SmartWebDriver smartWebDriver;

      WebDriver originalDriver;
      private MockedStatic<SpringExtension> springExt;

      @BeforeEach
      void setUp() {
         // only one static‐mock of SpringExtension per test class
         springExt = Mockito.mockStatic(SpringExtension.class);
         springExt
               .when(() -> SpringExtension.getApplicationContext(context))
               .thenReturn(appCtx);

         // wire up the DecoratorsFactory lookup
         when(appCtx.getBean(DecoratorsFactory.class))
               .thenReturn(decoratorsFactory);

         // build the getSmartWebDriver chain
         when(context.getStore(ExtensionContext.Namespace.GLOBAL))
               .thenReturn(globalStore);
         when(globalStore.get(StoreKeys.QUEST))
               .thenReturn(quest);
         when(decoratorsFactory.decorate(quest, SuperQuest.class))
               .thenReturn(superQuest);
         when(superQuest.artifact(UiServiceFluent.class, SmartWebDriver.class))
               .thenReturn(smartWebDriver);

         // unwrapDriver(...) → originalDriver
         originalDriver = mock(WebDriver.class, withSettings()
               .extraInterfaces(TakesScreenshot.class));
         when(smartWebDriver.getOriginal())
               .thenReturn(originalDriver);

         // screenshot always returns some bytes
         when(((TakesScreenshot) originalDriver)
               .getScreenshotAs(OutputType.BYTES))
               .thenReturn(new byte[] {1, 2, 3});

         // displayName used for the Allure attachment
         when(context.getDisplayName())
               .thenReturn("myDisplayName");
      }

      @AfterEach
      void tearDown() {
         // close the single static‐mock
         springExt.close();
      }

      @Test
      @DisplayName("captures screenshot, closes+quits driver, and rethrows")
      void handleTestExecutionException_closesAndQuits_whenNotKeepingDriver() {
         // branch: driver should NOT be kept
         when(smartWebDriver.isKeepDriverForSession()).thenReturn(false);

         RuntimeException toThrow = new RuntimeException("boom!");

         try (var allure = Mockito.mockStatic(Allure.class)) {
            UiTestExtension ext = new UiTestExtension();

            // exercise and verify it bubbles our exception
            RuntimeException thrown = assertThrows(RuntimeException.class,
                  () -> ext.handleTestExecutionException(context, toThrow)
            );
            assertSame(toThrow, thrown);

            // verify that we attached the screenshot under the right name
            allure.verify(() ->
                  Allure.addAttachment(
                        eq("myDisplayName"),
                        any(ByteArrayInputStream.class)
                  )
            );
         }

         // and finally, driver cleanup must have occurred
         verify(originalDriver).close();
         verify(originalDriver).quit();
      }

      @Test
      @DisplayName("when keepDriverForSession=true → skip close/quit but still rethrow")
      void handleTestExecutionException_keepsDriver_skipsCloseQuit() {
         // branch: driver should be kept for session
         when(smartWebDriver.isKeepDriverForSession()).thenReturn(true);

         RuntimeException toThrow = new RuntimeException("stay alive");

         try (var allure = Mockito.mockStatic(Allure.class)) {
            UiTestExtension ext = new UiTestExtension();

            // should rethrow our exact exception
            RuntimeException thrown = assertThrows(RuntimeException.class,
                  () -> ext.handleTestExecutionException(context, toThrow)
            );
            assertSame(toThrow, thrown);

            // screenshot still attached under displayName
            allure.verify(() ->
                  Allure.addAttachment(
                        eq("myDisplayName"),
                        any(ByteArrayInputStream.class)
                  )
            );
         }

         // but since we are keeping the driver, neither close() nor quit() should happen:
         verify(originalDriver, never()).close();
         verify(originalDriver, never()).quit();
      }

      @Test
      @DisplayName("when parent TEST_EXECUTION step is active it stops it and starts TEAR_DOWN")
      void handleTestExecutionException_switchesParentStep_whenParentStepActive() throws Throwable {
         when(smartWebDriver.isKeepDriverForSession()).thenReturn(false);
         RuntimeException boom = new RuntimeException("boom!");

         try (
               var allureMock   = mockStatic(Allure.class);
               var listenerMock = mockStatic(CustomAllureListener.class)
         ) {
            // stub the new predicate
            listenerMock
                  .when(() -> CustomAllureListener.isStepActive(
                        StepType.TEST_EXECUTION.getDisplayName()))
                  .thenReturn(true);

            UiTestExtension ext = new UiTestExtension();

            RuntimeException thrown = assertThrows(RuntimeException.class,
                  () -> ext.handleTestExecutionException(context, boom)
            );
            assertSame(boom, thrown);

            // now verify the NEW methods
            listenerMock.verify(CustomAllureListener::stopStep);
            listenerMock.verify(() ->
                  CustomAllureListener.startStep(StepType.TEAR_DOWN)
            );

            // screenshot still gets attached
            allureMock.verify(() ->
                  Allure.addAttachment(
                        eq("myDisplayName"),
                        any(ByteArrayInputStream.class)
                  )
            );
         }

         // and driver cleanup still runs
         verify(originalDriver).close();
         verify(originalDriver).quit();
      }

      @Test
      @DisplayName("Handle Test Execution Exception")
      void testHandleTestExecutionException() throws Exception {
         WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));

         byte[] screenshotBytes = {1, 2, 3};
         when(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES))
               .thenReturn(screenshotBytes);

         Method takeScreenshotMethod = UiTestExtension.class.getDeclaredMethod(
               "takeScreenshot", WebDriver.class, String.class
         );
         takeScreenshotMethod.setAccessible(true);

         try (var allureMock = mockStatic(Allure.class)) {
            takeScreenshotMethod.invoke(null, driver, "Test Display Name");

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
   }

   @Nested
   @ExtendWith(MockitoExtension.class)
   @DisplayName("launcherSessionClosed")
   class LauncherSessionClosedTests {

      @Mock
      LauncherSession session;
      @Mock
      SmartWebDriver keptDriver1;
      @Mock
      SmartWebDriver keptDriver2;

      private WebDriver underlying1;
      private WebDriver underlying2;

      @BeforeEach
      void setUp() {
         // start clean
         BaseLoginClient.getDriverToKeep().clear();

         // create two underlying WebDriver mocks
         underlying1 = mock(WebDriver.class);
         underlying2 = mock(WebDriver.class);

         // stub unwrap() to return them
         when(keptDriver1.getOriginal()).thenReturn(underlying1);
         when(keptDriver2.getOriginal()).thenReturn(underlying2);

         // register both in BaseLoginClient’s keep list
         BaseLoginClient.getDriverToKeep().add(keptDriver1);
         BaseLoginClient.getDriverToKeep().add(keptDriver2);
      }

      @AfterEach
      void tearDown() {
         BaseLoginClient.getDriverToKeep().clear();
      }

      @Test
      @DisplayName("closes and quits all kept drivers")
      void launcherSessionClosed_closesAndQuitsAllKeptDrivers() {
         UiTestExtension ext = new UiTestExtension();
         ext.launcherSessionClosed(session);

         // each underlying must be closed & quit
         verify(underlying1).close();
         verify(underlying1).quit();
         verify(underlying2).close();
         verify(underlying2).quit();
      }
   }

   @Nested
   @DisplayName("postQuestCreationLogin helper")
   class PostQuestCreationLoginTests {
      @Mock
      SuperQuest quest;
      @Mock
      DecoratorsFactory decoratorsFactory;
      @Mock
      Storage uiStorage;
      @Mock
      SuperUiServiceFluent<?> superSvc;

      SmartWebDriver smartDriver;
      WebDriverWait wait;

      @BeforeEach
      void init() {
         MockitoAnnotations.openMocks(this);

         // storage stubs
         when(quest.getStorage()).thenReturn(uiStorage);
         when(uiStorage.sub(UI)).thenReturn(uiStorage);

         // decorating stub
         when(decoratorsFactory.decorate(any(UiServiceFluent.class),
               eq(SuperUiServiceFluent.class)))
               .thenReturn(superSvc);

         // prevent NPE in BaseLoginClient.performLoginAndCache(...)
         smartDriver = mock(SmartWebDriver.class);
         when(superSvc.getDriver()).thenReturn(smartDriver);
         wait = mock(WebDriverWait.class);
         when(smartDriver.getWait()).thenReturn(wait);
         // succeed immediately
         when(wait.until(any())).thenReturn(true);
      }

      @Test
      @DisplayName("bad‐ctor path → AuthenticationUiException")
      void failurePath() throws Exception {
         // a client whose ctor blows up
         class BadClient extends BaseLoginClient {
            public BadClient() {
               throw new RuntimeException("boom");
            }

            @Override
            protected <T extends UiServiceFluent<?>> void loginImpl(
                  T uiService, String username, String password
            ) { /*no-op*/ }

            @Override
            protected By successfulLoginElementLocator() {
               return By.tagName("body");
            }
         }

         Method helper = UiTestExtension.class.getDeclaredMethod(
               "postQuestCreationLogin",
               SuperQuest.class,
               DecoratorsFactory.class,
               String.class,
               String.class,
               Class.class,
               boolean.class
         );
         helper.setAccessible(true);

         InvocationTargetException ite = assertThrows(
               InvocationTargetException.class,
               () -> helper.invoke(
                     /* static */ null,
                     quest,
                     decoratorsFactory,
                     "u",
                     "p",
                     BadClient.class,
                     false
               )
         );
         assertTrue(
               ite.getCause() instanceof AuthenticationUiException,
               "should wrap ctor‐failure in AuthenticationUiException"
         );
      }
   }

   @Nested
   @DisplayName("postQuestCreationRegisterCustomServices helper")
   class PostQuestCreationRegisterCustomServicesTests {
      @Mock
      SuperQuest quest;
      @Mock
      SmartWebDriver smartDriver;

      @BeforeEach
      void init() {
         MockitoAnnotations.openMocks(this);
         when(quest.artifact(UiServiceFluent.class, SmartWebDriver.class))
               .thenReturn(smartDriver);
      }

      @Test
      @DisplayName("no implementations → does nothing")
      void noImpls() throws Exception {
         try (var ref = mockStatic(ReflectionUtil.class)) {
            ref.when(() ->
                  ReflectionUtil.findImplementationsOfInterface(eq(UiServiceFluent.class), anyString())
            ).thenReturn(List.of());

            Method m = UiTestExtension.class
                  .getDeclaredMethod("postQuestCreationRegisterCustomServices", SuperQuest.class);
            m.setAccessible(true);

            assertDoesNotThrow(() -> m.invoke(null, quest));
            verifyNoInteractions(quest);
         }
      }

      @Test
      @DisplayName("multiple implementations → IllegalStateException")
      void multipleImpls() throws Exception {
         try (var ref = mockStatic(ReflectionUtil.class)) {
            ref.when(() ->
                  ReflectionUtil.findImplementationsOfInterface(eq(UiServiceFluent.class), anyString())
            ).thenReturn(List.of(DummyService.class, AnotherService.class));

            Method m = UiTestExtension.class
                  .getDeclaredMethod("postQuestCreationRegisterCustomServices", SuperQuest.class);
            m.setAccessible(true);

            InvocationTargetException ite = assertThrows(
                  InvocationTargetException.class,
                  () -> m.invoke(null, quest)
            );
            assertTrue(ite.getCause() instanceof IllegalStateException);
         }
      }

      @Test
      @DisplayName("ctor throws → ServiceInitializationException")
      void ctorBlowsUp() throws Exception {
         try (var ref = mockStatic(ReflectionUtil.class)) {
            ref.when(() ->
                  ReflectionUtil.findImplementationsOfInterface(eq(UiServiceFluent.class), anyString())
            ).thenReturn(List.of(BadService.class));

            Method m = UiTestExtension.class
                  .getDeclaredMethod("postQuestCreationRegisterCustomServices", SuperQuest.class);
            m.setAccessible(true);

            InvocationTargetException ite = assertThrows(
                  InvocationTargetException.class,
                  () -> m.invoke(null, quest)
            );
            assertTrue(ite.getCause() instanceof ServiceInitializationException);
         }
      }

      @Test
      @DisplayName("single implementation → registers and removes UI service")
      void singleImpl() throws Exception {
         try (var ref = mockStatic(ReflectionUtil.class)) {
            ref.when(() ->
                  ReflectionUtil.findImplementationsOfInterface(eq(UiServiceFluent.class), anyString())
            ).thenReturn(List.of(DummyService.class));

            Method m = UiTestExtension.class
                  .getDeclaredMethod("postQuestCreationRegisterCustomServices", SuperQuest.class);
            m.setAccessible(true);

            // this must not throw…
            assertDoesNotThrow(() -> m.invoke(null, quest));

            // …and it must have registered and then removed the world
            verify(quest).registerWorld(eq(DummyService.class), isA(DummyService.class));
            verify(quest).removeWorld(eq(UiServiceFluent.class));
         }
      }

      // give your dummy services exactly the two‐arg constructor the extension expects:
      static class DummyService extends UiServiceFluent<DummyService> {
         public DummyService(SmartWebDriver driver) {
            super(driver);
         }

         public DummyService(SmartWebDriver driver, SuperQuest q) {
            super(driver);
         }
      }

      static class AnotherService extends UiServiceFluent<AnotherService> {
         public AnotherService(SmartWebDriver driver) {
            super(driver);
         }
      }

      static class BadService extends UiServiceFluent<BadService> {
         public BadService(SmartWebDriver driver) {
            super(driver);
            throw new RuntimeException("boom");
         }
      }
   }

   @Nested
   @DisplayName("addResponseInStorage helper")
   class AddResponseInStorageTests {

      @Mock
      Storage rootStorage;
      @Mock
      Storage uiStorage;
      @Captor
      ArgumentCaptor<List<ApiResponse>> listCaptor;

      Method helper;
      ApiResponse resp1, resp2;

      @BeforeEach
      void setUp() throws Exception {
         MockitoAnnotations.openMocks(this);

         // when code does storage.sub(UI) → our uiStorage mock
         when(rootStorage.sub(UI)).thenReturn(uiStorage);

         // grab the private static helper
         helper = UiTestExtension.class
               .getDeclaredMethod("addResponseInStorage", Storage.class, ApiResponse.class);
         helper.setAccessible(true);

         resp1 = new ApiResponse("https://foo.example", "GET", 200);
         resp2 = new ApiResponse("https://bar.example", "POST", 201);
      }

      @Test
      @DisplayName("when storage empty → creates new list and adds one entry")
      void firstResponse() throws Exception {
         // simulate no existing list
         when(uiStorage.get(eq(RESPONSES), any(ParameterizedTypeReference.class)))
               .thenReturn(null);

         // invoke helper
         helper.invoke(null, rootStorage, resp1);

         // verify we put exactly one‐element list
         verify(uiStorage).put(eq(RESPONSES), listCaptor.capture());
         List<ApiResponse> stored = listCaptor.getValue();

         assertNotNull(stored, "the RESPONSES entry should exist");
         assertEquals(1, stored.size(), "should have created one‐element list");
         assertSame(resp1, stored.get(0), "must store exactly the passed‐in object");
      }

      @Test
      @DisplayName("when list already present → appends to existing list")
      void subsequentResponse() throws Exception {
         // simulate an existing single‐element list
         when(uiStorage.get(eq(RESPONSES), any(ParameterizedTypeReference.class)))
               .thenReturn(new ArrayList<>(List.of(resp1)));

         // invoke helper again
         helper.invoke(null, rootStorage, resp2);

         // verify we put back a two‐element list
         verify(uiStorage).put(eq(RESPONSES), listCaptor.capture());
         List<ApiResponse> stored = listCaptor.getValue();

         assertNotNull(stored, "the RESPONSES entry should still exist");
         assertEquals(2, stored.size(), "should have two elements now");
         assertIterableEquals(List.of(resp1, resp2), stored, "must preserve insertion order");
      }
   }

   @Nested
   @DisplayName("getOrCreateQuestConsumers helper")
   class GetOrCreateQuestConsumersTests {

      @Mock
      ExtensionContext context;
      @Mock
      ExtensionContext.Store store;
      UiTestExtension extension = new UiTestExtension();

      // we'll capture the very List that the lambda produces
      List<Consumer<SuperQuest>> lambdaList;

      Method helper;

      @BeforeEach
      void setUp() throws Exception {
         MockitoAnnotations.openMocks(this);
         // whenever context.getStore(GLOBAL) is called, return our mock store
         when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

         // stub getOrComputeIfAbsent so it actually runs the "key -> new ArrayList<>()" lambda
         doAnswer(inv -> {
            // key is arg0, mappingFunction is arg1
            Object key = inv.getArgument(0);
            @SuppressWarnings("unchecked")
            Function<Object, List<Consumer<SuperQuest>>> mapping = inv.getArgument(1);
            // invoke the lambda once, capture the list it returns
            lambdaList = mapping.apply(key);
            return lambdaList;
         }).when(store).getOrComputeIfAbsent(eq(QUEST_CONSUMERS), any(Function.class));

         // grab the private instance method by reflection
         helper = UiTestExtension.class
               .getDeclaredMethod("getOrCreateQuestConsumers", ExtensionContext.class);
         helper.setAccessible(true);
      }

      @Test
      @DisplayName("when no existing entry → invokes the ArrayList‐producing lambda")
      void createsEmptyList() throws Exception {
         // call the private helper
         @SuppressWarnings("unchecked")
         List<Consumer<SuperQuest>> returned =
               (List<Consumer<SuperQuest>>) helper.invoke(extension, context);

         // it must be exactly the instance the mapping function created
         assertSame(lambdaList, returned,
               "should hand back exactly the ArrayList created by the lambda");

         // and that new ArrayList must start out empty
         assertTrue(returned.isEmpty(), "new list should be empty");
      }
   }

   @Nested
   @DisplayName("checkUrl helper")
   class CheckUrlTests {

      @Test
      @DisplayName("Check URL Method")
      void testCheckUrl() throws Exception {
         Method checkUrlMethod = UiTestExtension.class.getDeclaredMethod("checkUrl", String[].class, String.class);
         checkUrlMethod.setAccessible(true);

         String[] urlSubstrings = {"test", "example"};

         boolean result1 = (boolean) checkUrlMethod.invoke(null, urlSubstrings, "https://test.com/path");
         assertTrue(result1);

         boolean result2 = (boolean) checkUrlMethod.invoke(null, urlSubstrings, "https://other.com/path");
         assertFalse(result2);
      }
   }

   @Nested
   @DisplayName("unwrapDriver helper")
   class UnwrapDriverTests {

      @Test
      @DisplayName("Unwrap Driver")
      void testUnwrapDriver() throws Exception {
         Method unwrapMethod = UiTestExtension.class.getDeclaredMethod("unwrapDriver", WebDriver.class);
         unwrapMethod.setAccessible(true);

         SmartWebDriver mockSmartWebDriver = mock(SmartWebDriver.class);
         WebDriver originalDriver = mock(WebDriver.class);

         when(mockSmartWebDriver.getOriginal()).thenReturn(originalDriver);

         WebDriver result = (WebDriver) unwrapMethod.invoke(null, mockSmartWebDriver);

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
   }

   @Nested
   @DisplayName("static initialization")
   class StaticInitializationTests {

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
   }

   @Nested
   @DisplayName("takeScreenshot helper")
   class TakeScreenshotTests {

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
   }

   @Nested
   @DisplayName("postQuestCreationAssertion helper")
   class PostQuestCreationAssertionTests {

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
               SmartWebDriver.class,
               smartWebDriver
         );
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
   }
}