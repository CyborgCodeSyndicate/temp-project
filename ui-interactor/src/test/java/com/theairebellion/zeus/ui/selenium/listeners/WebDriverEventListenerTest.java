package com.theairebellion.zeus.ui.selenium.listeners;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.logging.ExceptionLogging;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElementInspector;
import com.theairebellion.zeus.ui.util.FourConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebDriverEventListenerTest {

    private WebDriverEventListener listener;

    @Mock
    private WebElement webElement;

    @BeforeEach
    void setUp() {
        listener = new WebDriverEventListener();
        // Use lenient to avoid unnecessary stubbing exceptions
        lenient().when(webElement.toString()).thenReturn("MockedWebElement");
    }

    @Test
    @DisplayName("Test beforeClick logs correctly")
    void testBeforeClick() {
        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {
            // Execute the method under test
            listener.beforeClick(webElement);

            // Verify logging happened correctly
            logUIMock.verify(() ->
                    LogUI.extended(eq("Element: '{}' is about to get clicked"), eq("MockedWebElement")));
        }
    }

    @Test
    @DisplayName("Test afterClick logs correctly")
    void testAfterClick() {
        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {
            // Execute the method under test
            listener.afterClick(webElement);

            // Verify logging happened correctly
            logUIMock.verify(() ->
                    LogUI.extended(eq("Element: '{}' was clicked"), eq("MockedWebElement")));
        }
    }

    @Test
    @DisplayName("Test onError when exception comes from wait")
    void testOnErrorWhenComingFromWait() throws Exception {
        // Create mocked objects
        Method clickMethod = WebElement.class.getMethod("click");
        NoSuchElementException exception = new NoSuchElementException("Element not found");
        InvocationTargetException invocationTargetException =
                new InvocationTargetException(exception);

        // Mock SmartWebElementInspector result for wait exception
        SmartWebElementInspector.Result result = new SmartWebElementInspector.Result(false, false, true);

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<SmartWebElementInspector> inspectorMock = mockStatic(SmartWebElementInspector.class)) {

            // Set up mocks
            inspectorMock.when(() -> SmartWebElementInspector.inspectStackTrace(exception))
                    .thenReturn(result);

            // Execute method under test
            listener.onError(webElement, clickMethod, new Object[]{}, invocationTargetException);

            // Verify logging and no further logging since we're returning early due to wait
            logUIMock.verify(() -> LogUI.extended(contains("Exception in method"), anyString(), anyString()));
            logUIMock.verify(() -> LogUI.error(anyString()), never());
            logUIMock.verify(() -> LogUI.info(anyString()), never());
        }
    }

    @Test
    @DisplayName("Test onError when exception is from an annotated method with handle exception")
    void testOnErrorWithAnnotatedMethodAndHandleException() throws Exception {
        // Create mocked objects
        Method clickMethod = WebElement.class.getMethod("click");
        NoSuchElementException exception = new NoSuchElementException("Element not found");
        InvocationTargetException invocationTargetException =
                new InvocationTargetException(exception);

        // Mock SmartWebElementInspector result for annotated method with handle exception
        SmartWebElementInspector.Result result = new SmartWebElementInspector.Result(true, true, false);

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<SmartWebElementInspector> inspectorMock = mockStatic(SmartWebElementInspector.class)) {

            // Set up mocks
            inspectorMock.when(() -> SmartWebElementInspector.inspectStackTrace(exception))
                    .thenReturn(result);

            // Execute method under test
            listener.onError(webElement, clickMethod, new Object[]{}, invocationTargetException);

            // Verify appropriate log messages
            logUIMock.verify(() -> LogUI.extended(contains("Exception in method"), anyString(), anyString()));
            logUIMock.verify(() -> LogUI.error("Exception was not handled"));
        }
    }

    @Test
    @DisplayName("Test onError when exception is from an annotated method without handle exception")
    void testOnErrorWithAnnotatedMethodWithoutHandleException() throws Exception {
        // Create mocked objects
        Method clickMethod = WebElement.class.getMethod("click");
        NoSuchElementException exception = new NoSuchElementException("Element not found");
        InvocationTargetException invocationTargetException =
                new InvocationTargetException(exception);

        // Mock SmartWebElementInspector result for annotated method without handle exception
        SmartWebElementInspector.Result result = new SmartWebElementInspector.Result(true, false, false);

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<SmartWebElementInspector> inspectorMock = mockStatic(SmartWebElementInspector.class)) {

            // Set up mocks
            inspectorMock.when(() -> SmartWebElementInspector.inspectStackTrace(exception))
                    .thenReturn(result);

            // Execute method under test
            listener.onError(webElement, clickMethod, new Object[]{}, invocationTargetException);

            // Verify appropriate log messages
            logUIMock.verify(() -> LogUI.extended(contains("Exception in method"), anyString(), anyString()));
            logUIMock.verify(() -> LogUI.info("Framework will try to handle the exception"));
        }
    }

    @Test
    @DisplayName("Test onError when no annotated method found in framework")
    void testOnErrorWithNoAnnotatedMethod() throws Exception {
        // Create mocked objects
        Method clickMethod = WebElement.class.getMethod("click");
        NoSuchElementException exception = new NoSuchElementException("Element not found");
        InvocationTargetException invocationTargetException =
                new InvocationTargetException(exception);

        // Mock SmartWebElementInspector result for no annotated method
        SmartWebElementInspector.Result result = new SmartWebElementInspector.Result(false, false, false);

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<SmartWebElementInspector> inspectorMock = mockStatic(SmartWebElementInspector.class)) {

            // Set up mocks
            inspectorMock.when(() -> SmartWebElementInspector.inspectStackTrace(exception))
                    .thenReturn(result);

            // Execute method under test
            listener.onError(webElement, clickMethod, new Object[]{}, invocationTargetException);

            // Verify appropriate log messages
            logUIMock.verify(() -> LogUI.extended(contains("Exception in method"), anyString(), anyString()));
            logUIMock.verify(() -> LogUI.info("No implementation in framework for exception handling in this method"));
        }
    }

    @Test
    @DisplayName("Test exceptionLogging with matching criteria for WebElement click exception")
    void testExceptionLoggingWithMatchingCriteriaForClickException() throws Exception {
        // Create mocked objects
        Method clickMethod = WebElement.class.getMethod("click");
        Object[] args = new Object[]{};
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");
        InvocationTargetException invocationTargetException =
                new InvocationTargetException(noSuchElementException);

        // Create a real ExceptionLogging enum value
        ExceptionLogging clickLogging = ExceptionLogging.CLICK;

        // Create a test consumer that we can verify
        @SuppressWarnings("unchecked")
        FourConsumer<Object, WebElementAction, Object[], InvocationTargetException> consumer =
                mock(FourConsumer.class);

        // Use reflection to replace the exception logging map for testing
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, consumer);
        setExceptionLoggingMap(clickLogging, testMap);

        try (MockedStatic<ExceptionLogging> exceptionLoggingMock = mockStatic(ExceptionLogging.class)) {
            // Return our test enum value
            exceptionLoggingMock.when(ExceptionLogging::values)
                    .thenReturn(new ExceptionLogging[]{clickLogging});

            // Use reflection to call private static method
            Method exceptionLoggingMethod = WebDriverEventListener.class.getDeclaredMethod(
                    "exceptionLogging", Object.class, Method.class, Object[].class,
                    InvocationTargetException.class, Throwable.class);
            exceptionLoggingMethod.setAccessible(true);

            // Execute method under test
            exceptionLoggingMethod.invoke(null, webElement, clickMethod, args,
                    invocationTargetException, noSuchElementException);

            // Verify the consumer was called with the right parameters
            verify(consumer).accept(eq(webElement), eq(WebElementAction.CLICK), eq(args), eq(invocationTargetException));
        }
    }

    @Test
    @DisplayName("Test exceptionLogging with no matching criteria")
    void testExceptionLoggingWithNoMatchingCriteria() throws Exception {
        // Create mocked objects
        Method clickMethod = WebElement.class.getMethod("click");
        Object[] args = new Object[]{};
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");
        InvocationTargetException invocationTargetException =
                new InvocationTargetException(noSuchElementException);

        // Create a mock enum that won't match
        ExceptionLogging mockEnum = ExceptionLogging.FIND_ELEMENT_FROM_ROOT; // Different from WebElement and click

        try (MockedStatic<ExceptionLogging> exceptionLoggingMock = mockStatic(ExceptionLogging.class)) {
            // Return our test enum value that won't match
            exceptionLoggingMock.when(ExceptionLogging::values)
                    .thenReturn(new ExceptionLogging[]{mockEnum});

            // Use reflection to call private static method
            Method exceptionLoggingMethod = WebDriverEventListener.class.getDeclaredMethod(
                    "exceptionLogging", Object.class, Method.class, Object[].class,
                    InvocationTargetException.class, Throwable.class);
            exceptionLoggingMethod.setAccessible(true);

            // Execute method under test - should not throw exception even with no matches
            exceptionLoggingMethod.invoke(null, webElement, clickMethod, args,
                    invocationTargetException, noSuchElementException);

            // Nothing to verify as we're just making sure it completes without error
        }
    }

    @Test
    @DisplayName("Test matchesLogCriteria with all matching conditions")
    void testMatchesLogCriteriaWithAllMatching() throws Exception {
        // Create test objects
        Method clickMethod = WebElement.class.getMethod("click");
        Object[] args = new Object[]{};
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use a real enum value and modify it for testing
        ExceptionLogging clickLogging = ExceptionLogging.CLICK;

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        setExceptionLoggingMap(clickLogging, testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                clickLogging, webElement, clickMethod, args, noSuchElementException);

        // Verify result
        assertTrue(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with non-matching target class")
    void testMatchesLogCriteriaWithNonMatchingTargetClass() throws Exception {
        // Create test objects
        Method clickMethod = WebElement.class.getMethod("click");
        Object[] args = new Object[]{};
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use a non-matching enum value (WebDriver instead of WebElement)
        ExceptionLogging findElementEnum = ExceptionLogging.FIND_ELEMENT_FROM_ROOT; // Uses WebDriver class

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                findElementEnum, webElement, clickMethod, args, noSuchElementException);

        // Verify result is false due to non-matching target class
        assertFalse(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with non-matching method name")
    void testMatchesLogCriteriaWithNonMatchingMethodName() throws Exception {
        // Create test objects
        Method clickMethod = WebElement.class.getMethod("click");
        Object[] args = new Object[]{};
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use a non-matching enum value (different method name)
        ExceptionLogging submitEnum = ExceptionLogging.SUBMIT; // Uses "submit" method instead of "click"

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                submitEnum, webElement, clickMethod, args, noSuchElementException);

        // Verify result is false due to non-matching method name
        assertFalse(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with non-matching exception type")
    void testMatchesLogCriteriaWithNonMatchingExceptionType() throws Exception {
        // Create test objects
        Method clickMethod = WebElement.class.getMethod("click");
        Object[] args = new Object[]{};
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use a real enum value and modify it for testing
        ExceptionLogging clickLogging = ExceptionLogging.CLICK;

        // Create a test consumer map with different exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(ElementNotInteractableException.class, (t, a, args1, e) -> {}); // Different exception
        setExceptionLoggingMap(clickLogging, testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                clickLogging, webElement, clickMethod, args, noSuchElementException);

        // Verify result is false due to non-matching exception type
        assertFalse(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with different parameter and argument lengths")
    void testMatchesLogCriteriaWithDifferentParameterAndArgumentLengths() throws Exception {
        // Create test objects with mismatched parameter counts
        Method sendKeysMethod = WebElement.class.getMethod("sendKeys", CharSequence[].class);
        Object[] emptyArgs = new Object[]{}; // No args, but method expects args
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use the SEND_KEYS enum
        ExceptionLogging sendKeysEnum = ExceptionLogging.SEND_KEYS;

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                sendKeysEnum, webElement, sendKeysMethod, emptyArgs, noSuchElementException);

        // Verify result is false due to different parameter and argument lengths
        assertFalse(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with null argument for non-primitive parameter")
    void testMatchesLogCriteriaWithNullArgumentForNonPrimitiveParameter() throws Exception {
        // Create test objects with null argument for non-primitive parameter
        WebDriver driver = mock(WebDriver.class);
        Method findElementMethod = WebDriver.class.getMethod("findElement", By.class);
        Object[] args = new Object[]{null}; // Null argument for By parameter
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use a real enum value and modify it for testing
        ExceptionLogging findElementEnum = ExceptionLogging.FIND_ELEMENT_FROM_ROOT;

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        setExceptionLoggingMap(findElementEnum, testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                findElementEnum, driver, findElementMethod, args, noSuchElementException);

        // Verify result is true because null is valid for a non-primitive parameter (By)
        assertTrue(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with null argument for primitive parameter")
    void testMatchesLogCriteriaWithNullArgumentForPrimitiveParameter() throws Exception {
        // Use a class with primitive parameter for testing
        class TestClass {
            public void testMethod(int primitive) {}
        }

        Method testMethod = TestClass.class.getMethod("testMethod", int.class);
        Object[] args = new Object[]{null}; // Null argument for primitive int
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");
        TestClass testObj = new TestClass();

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Create a mock ExceptionLogging enum for this special case
        ExceptionLogging mockLogging = mock(ExceptionLogging.class);

        // Use lenient for all the stubbings to avoid UnnecessaryStubbingException
        lenient().when(mockLogging.getTargetClass()).thenReturn((Class) TestClass.class);
        lenient().when(mockLogging.getAction()).thenReturn(WebElementAction.CLICK);

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        lenient().when(mockLogging.getExceptionLoggingMap()).thenReturn(testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                mockLogging, testObj, testMethod, args, noSuchElementException);

        // Verify result is false because null is not valid for a primitive parameter
        assertFalse(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with non-null arguments that don't match parameter types")
    void testMatchesLogCriteriaWithNonMatchingParameterTypes() throws Exception {
        // Create test objects with mismatched parameter types
        Method findElementMethod = WebElement.class.getMethod("findElement", By.class);
        Object[] args = new Object[]{"string"}; // String arg, but method expects By
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Use a real enum value
        ExceptionLogging findElementEnum = ExceptionLogging.FIND_ELEMENT_FROM_ELEMENT;

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        setExceptionLoggingMap(findElementEnum, testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                findElementEnum, webElement, findElementMethod, args, noSuchElementException);

        // Verify result is false due to non-matching parameter and argument types
        assertFalse(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with mixed null and non-null arguments")
    void testMatchesLogCriteriaArrayStreamMapping() throws Exception {
        // Create a method with multiple parameters of different types
        class TestClass {
            public void click(String s, Integer i, Double d) {}  // Named "click" to match WebElementAction.CLICK
        }

        Method testMethod = TestClass.class.getMethod("click", String.class, Integer.class, Double.class);

        // Create args with mix of null and non-null values
        Object[] args = new Object[]{"test", null, 3.14};

        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");
        TestClass testObj = new TestClass();

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Create a mock ExceptionLogging enum for this special case
        ExceptionLogging mockLogging = mock(ExceptionLogging.class);

        // Use lenient for all the stubbings
        lenient().when(mockLogging.getTargetClass()).thenReturn((Class) TestClass.class);
        lenient().when(mockLogging.getAction()).thenReturn(WebElementAction.CLICK); // CLICK has method name "click"

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        lenient().when(mockLogging.getExceptionLoggingMap()).thenReturn(testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                mockLogging, testObj, testMethod, args, noSuchElementException);

        // Since everything matches, result should be true
        assertTrue(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with subclass parameter type")
    void testMatchesLogCriteriaWithSubclassParameterType() throws Exception {
        // Create test objects with subclass parameter/argument relationship
        class BaseClass {}
        class SubClass extends BaseClass {}

        class TestClass {
            public void click(BaseClass param) {}  // Named "click" to match WebElementAction.CLICK
        }

        Method testMethod = TestClass.class.getMethod("click", BaseClass.class);
        Object[] args = new Object[]{new SubClass()}; // SubClass instance where BaseClass is expected - should be compatible
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");
        TestClass testObj = new TestClass();

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Create a mock ExceptionLogging enum for this special case
        ExceptionLogging mockLogging = mock(ExceptionLogging.class);

        // Use lenient for all the stubbings
        lenient().when(mockLogging.getTargetClass()).thenReturn((Class) TestClass.class);
        lenient().when(mockLogging.getAction()).thenReturn(WebElementAction.CLICK); // CLICK has method name "click"

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        lenient().when(mockLogging.getExceptionLoggingMap()).thenReturn(testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                mockLogging, testObj, testMethod, args, noSuchElementException);

        // Should be true since SubClass is assignable to BaseClass
        assertTrue(result);
    }

    @Test
    @DisplayName("Test matchesLogCriteria with incompatible class parameter type")
    void testMatchesLogCriteriaWithIncompatibleClassParameterType() throws Exception {
        // Create test objects with incompatible parameter/argument relationship
        class ClassA {}
        class ClassB {} // Not related to ClassA

        class TestClass {
            public void testMethod(ClassA param) {}
        }

        Method testMethod = TestClass.class.getMethod("testMethod", ClassA.class);
        Object[] args = new Object[]{new ClassB()}; // ClassB instance where ClassA is expected - incompatible
        NoSuchElementException noSuchElementException = new NoSuchElementException("Element not found");
        TestClass testObj = new TestClass();

        // Use reflection to access the private method
        Method matchesLogCriteriaMethod = WebDriverEventListener.class.getDeclaredMethod(
                "matchesLogCriteria", ExceptionLogging.class, Object.class, Method.class,
                Object[].class, Throwable.class);
        matchesLogCriteriaMethod.setAccessible(true);

        // Create a mock ExceptionLogging enum for this special case
        ExceptionLogging mockLogging = mock(ExceptionLogging.class);

        // Use lenient for all the stubbings
        lenient().when(mockLogging.getTargetClass()).thenReturn((Class) TestClass.class);
        lenient().when(mockLogging.getAction()).thenReturn(WebElementAction.CLICK);

        // Create a test consumer map with our exception
        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
                testMap = new HashMap<>();
        testMap.put(NoSuchElementException.class, (t, a, args1, e) -> {});
        lenient().when(mockLogging.getExceptionLoggingMap()).thenReturn(testMap);

        // Execute method under test
        Boolean result = (Boolean) matchesLogCriteriaMethod.invoke(null,
                mockLogging, testObj, testMethod, args, noSuchElementException);

        // Should be false since ClassB is NOT assignable to ClassA
        assertFalse(result);
    }

    // Helper method to modify ExceptionLogging enum maps for testing
    private void setExceptionLoggingMap(ExceptionLogging enumValue,
                                        Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> testMap)
            throws Exception {
        Field exceptionLoggingMapField = ExceptionLogging.class.getDeclaredField("exceptionLoggingMap");
        exceptionLoggingMapField.setAccessible(true);
        exceptionLoggingMapField.set(enumValue, testMap);
    }
}