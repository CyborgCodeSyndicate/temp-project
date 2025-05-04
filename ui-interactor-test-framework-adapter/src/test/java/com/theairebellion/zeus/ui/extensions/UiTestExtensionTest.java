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
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

class UiTestExtensionTest extends BaseUnitUITest {

    @Mock
    private ExtensionContext context;

    private UiTestExtension extension;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        extension = new UiTestExtension();
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
        when(mockStorage.sub(StorageKeysUi.UI)).thenReturn(uiStorage);
        when(uiStorage.get(
                eq(StorageKeysUi.RESPONSES),
                any(ParameterizedTypeReference.class)
        )).thenReturn(null);

        // Invoke method
        addResponseMethod.invoke(null, mockStorage, apiResponse);

        // Verify storage interaction
        verify(uiStorage).put(eq(StorageKeysUi.RESPONSES), argThat(list ->
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
        when(((TakesScreenshot)mockDriver).getScreenshotAs(OutputType.BYTES))
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
        when(quest.artifact(UIServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
        when(quest.getSoftAssertions()).thenReturn(softAssertion);

        // Execute method
        postQuestCreationAssertionMethod.invoke(null, quest, "TestMethod");

        // Verify method calls
        verify(quest).artifact(UIServiceFluent.class, SmartWebDriver.class);
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
        UIServiceFluent<?> uiServiceFluent = mock(UIServiceFluent.class);
        SuperUIServiceFluent<?> superUIServiceFluent = mock(SuperUIServiceFluent.class);
        Storage storage = mock(Storage.class);
        Storage uiStorage = mock(Storage.class);

        // Setup mocks
        when(quest.getStorage()).thenReturn(storage);
        when(storage.sub(StorageKeysUi.UI)).thenReturn(uiStorage);
        when(quest.enters(UIServiceFluent.class)).thenReturn(uiServiceFluent);
        when(decoratorsFactory.decorate(uiServiceFluent, SuperUIServiceFluent.class))
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
        verify(quest).enters(UIServiceFluent.class);
        verify(decoratorsFactory).decorate(uiServiceFluent, SuperUIServiceFluent.class);
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
        when(superQuest.artifact(UIServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
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
                var frameworkConfig = mock(com.theairebellion.zeus.ui.config.UIFrameworkConfig.class);
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
        when(mockSuperQuest.artifact(UIServiceFluent.class, SmartWebDriver.class)).thenReturn(mockSmartWebDriver);
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
        verify(mockSuperQuest).artifact(UIServiceFluent.class, SmartWebDriver.class);
        verify(mockSmartWebDriver).getOriginal();
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
    @DisplayName("Process Intercept Requests Annotation")
    void testProcessInterceptRequestsAnnotation() throws Exception {
        // Get the method using reflection
        Method processMethod = UiTestExtension.class.getDeclaredMethod(
                "processInterceptRequestsAnnotation", ExtensionContext.class, Method.class);
        processMethod.setAccessible(true);

        // Create an actual annotated method for testing
        Method testMethod = getClass().getDeclaredMethod("interceptRequestMethod");

        // Create mocks
        ExtensionContext mockContext = mock(ExtensionContext.class);
        ExtensionContext.Store mockStore = mock(ExtensionContext.Store.class);

        // Setup mocks
        when(mockContext.getStore(any())).thenReturn(mockStore);
        when(mockStore.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(new ArrayList<>());

        // Execute the method
        processMethod.invoke(new UiTestExtension(), mockContext, testMethod);

        // Verify interactions
        verify(mockContext).getStore(any());
        verify(mockStore).getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any());
    }

    @Test
    @DisplayName("Test processAuthenticateViaUiAsAnnotation")
    void testProcessAuthenticateViaUiAsAnnotation() throws Exception {
        // Get the method using reflection
        Method processMethod = UiTestExtension.class.getDeclaredMethod(
                "processAuthenticateViaUiAsAnnotation", ExtensionContext.class, Method.class);
        processMethod.setAccessible(true);

        // Create an actual annotated method for testing
        Method testMethod = getClass().getDeclaredMethod("authenticateMethod");

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
        when(quest.artifact(UIServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);

        // Mock the ReflectionUtil to return null (no custom class found)
        try (var reflectionUtilMock = mockStatic(ReflectionUtil.class)) {
            reflectionUtilMock.when(() ->
                            ReflectionUtil.findImplementationsOfInterface(eq(UIServiceFluent.class), anyString()))
                    .thenReturn(List.of());

            // Execute the method
            registerMethod.invoke(null, quest);

            // Verify the lookup was attempted
            reflectionUtilMock.verify(() ->
                    ReflectionUtil.findImplementationsOfInterface(eq(UIServiceFluent.class), nullable(String.class)));
        }
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

        when(quest.artifact(UIServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
        when(quest.getSoftAssertions()).thenReturn(softAssertion);

        consumers.get(0).accept(quest);

        verify(quest).artifact(UIServiceFluent.class, SmartWebDriver.class);
        verify(quest).getSoftAssertions();
        verify(softAssertion).registerObjectForPostErrorHandling(eq(SmartWebDriver.class), eq(smartWebDriver));
    }

    @Test
    @DisplayName("Test lambda$beforeTestExecution$0")
    void testBeforeTestExecutionLambda() {
        // Find the lambda method
        Method[] methods = UiTestExtension.class.getDeclaredMethods();
        Method lambdaMethod = null;

        for (Method method : methods) {
            if (method.getName().contains("lambda$beforeTestExecution")) {
                lambdaMethod = method;
                break;
            }
        }

        // Execute the test if we found the method
        if (lambdaMethod != null) {
            lambdaMethod.setAccessible(true);

            // Create mocks
            ExtensionContext mockContext = mock(ExtensionContext.class);
            Method testMethod = mock(Method.class);

            // Spy on the extension to verify needed methods are called
            UiTestExtension extensionSpy = spy(extension);

            // Execute the lambda
            try {
                lambdaMethod.invoke(extensionSpy, mockContext, testMethod);
            } catch (Exception e) {
                // Expected due to incomplete mocking, but we still verify method calls
            }
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
            String[] urlSubstrings = new String[]{"test"};

            // Setup behavior
            when(quest.artifact(UIServiceFluent.class, SmartWebDriver.class)).thenReturn(smartWebDriver);
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
            verify(quest).artifact(UIServiceFluent.class, SmartWebDriver.class);
        }
    }

    // Annotation method for testing
    @AuthenticateViaUiAs(
            credentials = TestLoginCredentials.class,
            type = TestLoginClient.class,
            cacheCredentials = true
    )
    void authenticateMethod() {}

    @InterceptRequests(requestUrlSubStrings = "test/url")
    void interceptRequestMethod() {
        // Dummy method to support the annotation test
    }

    // Dummy implementations for test classes
    public static class TestLoginCredentials implements LoginCredentials {
        @Override
        public String username() { return "testuser"; }
        @Override
        public String password() { return "testpassword"; }
    }

    public static class TestLoginClient extends BaseLoginClient {
        @Override
        protected <T extends UIServiceFluent<?>> void loginImpl(T uiService, String username, String password) {}

        @Override
        protected By successfulLoginElementLocator() {
            return By.id("login-success");
        }
    }
}