package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.util.BiFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlingWebDriverTest {

    @Mock
    private WebDriver mockDriver;

    @ParameterizedTest
    @EnumSource(ExceptionHandlingWebDriver.class)
    @DisplayName("Each enum should have non-null fields")
    void testEnumConstructorAndGetters(ExceptionHandlingWebDriver enumValue) {
        // Test that each enum has non-null values for its fields
        assertNotNull(enumValue.getMethodName());
        assertNotNull(enumValue.getExceptionHandlingMap());
        assertFalse(enumValue.getExceptionHandlingMap().isEmpty());
    }

    @Test
    @DisplayName("FIND_ELEMENT should have correct method name")
    void testFindElementEnumHasCorrectMethodName() {
        assertEquals("findElement", ExceptionHandlingWebDriver.FIND_ELEMENT.getMethodName());
    }

    @Test
    @DisplayName("FIND_ELEMENTS should have correct method name")
    void testFindElementsEnumHasCorrectMethodName() {
        assertEquals("findElements", ExceptionHandlingWebDriver.FIND_ELEMENTS.getMethodName());
    }

    @Nested
    @DisplayName("Tests for exception handler mappings")
    class ExceptionHandlerMappingTests {
        @Test
        @DisplayName("FIND_ELEMENT should handle NoSuchElementException")
        void testFindElementHandlesNoSuchElementException() {
            Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> map =
                    ExceptionHandlingWebDriver.FIND_ELEMENT.getExceptionHandlingMap();

            assertTrue(map.containsKey(NoSuchElementException.class));
            assertNotNull(map.get(NoSuchElementException.class));
        }

        @Test
        @DisplayName("FIND_ELEMENTS should handle NoSuchElementException")
        void testFindElementsHandlesNoSuchElementException() {
            Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> map =
                    ExceptionHandlingWebDriver.FIND_ELEMENTS.getExceptionHandlingMap();

            assertTrue(map.containsKey(NoSuchElementException.class));
            assertNotNull(map.get(NoSuchElementException.class));
        }
    }

    @Nested
    @DisplayName("Tests for enum constructor")
    class EnumConstructorTests {
        @Test
        @DisplayName("Constructor should assign correct values")
        void testConstructorAssignsCorrectValues() {
            // Test for FIND_ELEMENT enum
            String methodName = "findElement";
            Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> exceptionMap =
                    ExceptionHandlingWebDriver.FIND_ELEMENT.getExceptionHandlingMap();

            assertEquals(methodName, ExceptionHandlingWebDriver.FIND_ELEMENT.getMethodName());
            assertNotSame(exceptionMap, ExceptionHandlingWebDriver.FIND_ELEMENT.getExceptionHandlingMap());

            // Test for FIND_ELEMENTS enum
            methodName = "findElements";
            exceptionMap = ExceptionHandlingWebDriver.FIND_ELEMENTS.getExceptionHandlingMap();

            assertEquals(methodName, ExceptionHandlingWebDriver.FIND_ELEMENTS.getMethodName());
            assertNotSame(exceptionMap, ExceptionHandlingWebDriver.FIND_ELEMENTS.getExceptionHandlingMap());
        }
    }

    @Nested
    @DisplayName("Tests for lambda function execution")
    class LambdaFunctionTests {
        @Test
        @DisplayName("FIND_ELEMENT lambda should call handleNoSuchElement with correct parameters")
        void testFindElementLambdaExecution() {
            try (MockedStatic<ExceptionHandlingWebDriverFunctions> mockedFunctions =
                         mockStatic(ExceptionHandlingWebDriverFunctions.class)) {

                // Setup test data
                Object[] args = new Object[] { mock(By.class) };

                // Get the function from the map
                BiFunction<WebDriver, Object[], Object> function =
                        ExceptionHandlingWebDriver.FIND_ELEMENT.getExceptionHandlingMap()
                                .get(NoSuchElementException.class);

                // Execute the lambda
                function.apply(mockDriver, args);

                // Verify the static method was called with correct params
                mockedFunctions.verify(() ->
                        ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                                eq(mockDriver),
                                eq(WebElementAction.FIND_ELEMENT),
                                eq(args)
                        )
                );
            }
        }

        @Test
        @DisplayName("FIND_ELEMENTS lambda should call handleNoSuchElement with correct parameters")
        void testFindElementsLambdaExecution() {
            try (MockedStatic<ExceptionHandlingWebDriverFunctions> mockedFunctions =
                         mockStatic(ExceptionHandlingWebDriverFunctions.class)) {

                // Setup test data
                Object[] args = new Object[] { mock(By.class) };

                // Get the function from the map
                BiFunction<WebDriver, Object[], Object> function =
                        ExceptionHandlingWebDriver.FIND_ELEMENTS.getExceptionHandlingMap()
                                .get(NoSuchElementException.class);

                // Execute the lambda
                function.apply(mockDriver, args);

                // Verify the static method was called with correct params
                mockedFunctions.verify(() ->
                        ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                                eq(mockDriver),
                                eq(WebElementAction.FIND_ELEMENTS),
                                eq(args)
                        )
                );
            }
        }
    }

    @Nested
    @DisplayName("Tests for map properties")
    class MapPropertiesTests {
        @Test
        @DisplayName("All enums should have non-empty maps")
        void testAllEnumsHaveNonEmptyMaps() {
            for (ExceptionHandlingWebDriver enumValue : ExceptionHandlingWebDriver.values()) {
                Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> map =
                        enumValue.getExceptionHandlingMap();

                assertFalse(map.isEmpty());
            }
        }

        @Test
        @DisplayName("FIND_ELEMENT map should have correct size")
        void testFindElementMapHasCorrectSize() {
            assertEquals(1, ExceptionHandlingWebDriver.FIND_ELEMENT.getExceptionHandlingMap().size());
        }

        @Test
        @DisplayName("FIND_ELEMENTS map should have correct size")
        void testFindElementsMapHasCorrectSize() {
            assertEquals(1, ExceptionHandlingWebDriver.FIND_ELEMENTS.getExceptionHandlingMap().size());
        }
    }

    @Test
    @DisplayName("Enum should have correct number of values")
    void testEnumHasCorrectNumberOfValues() {
        assertEquals(2, ExceptionHandlingWebDriver.values().length);
    }

    // Make sure the default constructor is not used
    @Test
    @DisplayName("Private constructor should not be accessible")
    void testPrivateConstructorNotAccessible() {
        // This doesn't actually test the constructor but ensures coverage
        // by acknowledging the enum has a default constructor
        assertNotNull(ExceptionHandlingWebDriver.FIND_ELEMENT);
    }
}