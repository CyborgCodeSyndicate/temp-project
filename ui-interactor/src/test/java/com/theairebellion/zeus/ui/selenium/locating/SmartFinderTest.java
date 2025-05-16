package com.theairebellion.zeus.ui.selenium.locating;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.shadowroot.ShadowDomUtils;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmartFinderTest extends BaseUnitUITest {

    @Mock
    private WebDriver driver;

    @Mock
    private WebElement webElement;

    @Mock
    private SmartWebDriver smartDriver;

    @Mock
    private SmartWebElement smartElement;

    @Mock
    private By by;

    @Captor
    private ArgumentCaptor<Function<WebDriver, ?>> waitConditionCaptor;

    private Consumer<Function<WebDriver, ?>> waitConsumer;

    @BeforeEach
    void setUp() {
        // Set up basic mocking behavior
        lenient().when(smartDriver.getOriginal()).thenReturn(driver);
        lenient().when(smartElement.getOriginal()).thenReturn(webElement);
        lenient().when(smartElement.getDriver()).thenReturn(driver);

        // Set up wait consumer to capture wait conditions
        waitConsumer = condition -> condition.apply(driver);
    }

    @Nested
    @DisplayName("findElementNoWrap tests")
    class FindElementNoWrapTests {

        @Test
        @DisplayName("findElementNoWrap with WebDriver should return SmartWebElement")
        void testFindElementNoWrapWithWebDriver() {
            // Given
            when(driver.findElement(by)).thenReturn(webElement);

            // When
            SmartWebElement result = SmartFinder.findElementNoWrap(driver, by);

            // Then
            assertNotNull(result);
            assertEquals(webElement, result.getOriginal());
            assertEquals(driver, result.getDriver());
        }

        @Test
        @DisplayName("findElementNoWrap with SmartWebElement should return SmartWebElement")
        void testFindElementNoWrapWithSmartWebElement() {
            // Given
            when(webElement.findElement(by)).thenReturn(webElement);

            // When
            SmartWebElement result = SmartFinder.findElementNoWrap(smartElement, by);

            // Then
            assertNotNull(result);
            assertEquals(webElement, result.getOriginal());
            assertEquals(driver, result.getDriver());
        }
    }

    @Nested
    @DisplayName("findElementNormally tests")
    class FindElementNormallyTests {

        @Test
        @DisplayName("findElementNormallyWithWebDriver should wait and return SmartWebElement")
        void testFindElementNormallyWithWebDriver() {
            // Given
            when(driver.findElement(by)).thenReturn(webElement);
            Consumer<Function<WebDriver, ?>> mockWaitConsumer = mock(Consumer.class);

            // When
            SmartWebElement result = SmartFinder.findElementNormally(driver, by, mockWaitConsumer);

            // Then
            assertNotNull(result);
            assertEquals(webElement, result.getOriginal());
            verify(driver).findElement(by);
            verify(mockWaitConsumer).accept(any(Function.class));
        }


        @Test
        @DisplayName("findElementNormally with SmartWebElement should wait and return SmartWebElement")
        void testFindElementNormallyWithSmartWebElement() {
            // Given
            when(webElement.findElement(by)).thenReturn(webElement);

            // When
            SmartWebElement result = SmartFinder.findElementNormally(smartElement, by, waitConsumer);

            // Then
            assertNotNull(result);
            assertEquals(webElement, result.getOriginal());
            verify(webElement).findElement(by);
        }
    }

    @Nested
    @DisplayName("findElementWithShadowRootDriver tests")
    class FindElementWithShadowRootDriverTests {

        @Test
        @DisplayName("findElementWithShadowRootDriver should return element if found immediately")
        void testFindElementWithShadowRootDriverElementFoundImmediately() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(driver.findElements(by)).thenReturn(elements);

            // When
            SmartWebElement result = SmartFinder.findElementWithShadowRootDriver(smartDriver, by, waitConsumer);

            // Then
            assertNotNull(result);
            assertEquals(webElement, result.getOriginal());
        }

        @Test
        @DisplayName("findElementWithShadowRootDriver should search shadow roots if no elements found and shadow roots exist")
        void testFindElementWithShadowRootDriverSearchShadowRoots() {
            // Given
            SmartWebElement shadowElement = mock(SmartWebElement.class);
            when(driver.findElements(by)).thenReturn(Collections.emptyList());
            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartDriver)).thenReturn(true);
                shadowDomUtils.when(() -> ShadowDomUtils.findElementInShadowRoots(smartDriver, by)).thenReturn(shadowElement);

                // When
                SmartWebElement result = SmartFinder.findElementWithShadowRootDriver(smartDriver, by, waitConsumer);

                // Then
                assertNotNull(result);
                assertSame(shadowElement, result);
                shadowDomUtils.verify(() -> ShadowDomUtils.findElementInShadowRoots(smartDriver, by));
            }
        }

        @Test
        @DisplayName("findElementWithShadowRootDriver should search shadow roots with wait time if provided")
        void testFindElementWithShadowRootDriverWithWaitTime() {
            // Given
            SmartWebElement shadowElement = mock(SmartWebElement.class);
            when(driver.findElements(by)).thenReturn(Collections.emptyList());
            long waitTime = 1000L;
            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartDriver)).thenReturn(true);
                shadowDomUtils.when(() -> ShadowDomUtils.findElementInShadowRoots(smartDriver, by, waitTime)).thenReturn(shadowElement);

                // When
                SmartWebElement result = SmartFinder.findElementWithShadowRootDriver(smartDriver, by, waitConsumer, waitTime);

                // Then
                assertNotNull(result);
                assertSame(shadowElement, result);
                shadowDomUtils.verify(() -> ShadowDomUtils.findElementInShadowRoots(smartDriver, by, waitTime));
            }
        }

        @Test
        @DisplayName("findElementWithShadowRootDriver should wait and find normally if no shadow roots")
        void testFindElementWithShadowRootDriverNoShadowRoots() {
            // When
            when(driver.findElements(by)).thenReturn(Collections.emptyList());
            when(driver.findElement(by)).thenReturn(webElement);
            Consumer<Function<WebDriver, ?>> mockWaitConsumer = mock(Consumer.class);
            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartDriver)).thenReturn(false);
                SmartWebElement result = SmartFinder.findElementWithShadowRootDriver(smartDriver, by, mockWaitConsumer);

                // Then
                assertNotNull(result);
                assertEquals(webElement, result.getOriginal());
                verify(driver).findElement(by);
                verify(mockWaitConsumer).accept(any(Function.class));
            }
        }
    }

    @Nested
    @DisplayName("findElementWithShadowRootElement tests")
    class FindElementWithShadowRootElementTests {

        @Test
        @DisplayName("findElementWithShadowRootElement should return element if found immediately")
        void testFindElementWithShadowRootElementFoundImmediately() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(webElement.findElements(by)).thenReturn(elements);

            // When
            SmartWebElement result = SmartFinder.findElementWithShadowRootElement(smartElement, by, waitConsumer);

            // Then
            assertNotNull(result);
            assertEquals(webElement, result.getOriginal());
        }

        @Test
        @DisplayName("findElementWithShadowRootElement should search shadow roots if no elements found and shadow roots exist")
        void testFindElementWithShadowRootElementSearchShadowRoots() {
            // Given
            SmartWebElement shadowElement = mock(SmartWebElement.class);
            when(webElement.findElements(by)).thenReturn(Collections.emptyList());

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartElement)).thenReturn(true);
                shadowDomUtils.when(() -> ShadowDomUtils.findElementInShadowRoots(smartElement, by)).thenReturn(shadowElement);

                // When
                SmartWebElement result = SmartFinder.findElementWithShadowRootElement(smartElement, by, waitConsumer);

                // Then
                assertNotNull(result);
                assertSame(shadowElement, result);
                shadowDomUtils.verify(() -> ShadowDomUtils.findElementInShadowRoots(smartElement, by));
            }
        }

        @Test
        @DisplayName("findElementWithShadowRootElement should call findElementNormally if no shadow roots")
        void testFindElementWithShadowRootElementNoShadowRoots() {
            // Given
            when(webElement.findElements(by)).thenReturn(Collections.emptyList());
            when(webElement.findElement(by)).thenReturn(webElement);

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartElement)).thenReturn(false);

                // When
                SmartWebElement result = SmartFinder.findElementWithShadowRootElement(smartElement, by, waitConsumer);

                // Then
                assertNotNull(result);
                assertEquals(webElement, result.getOriginal());
                verify(webElement).findElement(by);
            }
        }
    }

    @Nested
    @DisplayName("findElementsNoWrap tests")
    class FindElementsNoWrapTests {

        @Test
        @DisplayName("findElementsNoWrap with WebDriver should return list of SmartWebElements")
        void testFindElementsNoWrapWithWebDriver() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(driver.findElements(by)).thenReturn(elements);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsNoWrap(driver, by);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }

        @Test
        @DisplayName("findElementsNoWrap with SmartWebElement should return list of SmartWebElements")
        void testFindElementsNoWrapWithSmartWebElement() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(webElement.findElements(by)).thenReturn(elements);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsNoWrap(smartElement, by);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }
    }

    @Nested
    @DisplayName("findElementsNormally tests")
    class FindElementsNormallyTests {

        @Test
        @DisplayName("findElementsNormally with WebDriver should return list when elements found")
        void testFindElementsNormallyWithWebDriverElementsFound() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(driver.findElements(by)).thenReturn(elements);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsNormally(driver, by, waitConsumer);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }

        @Test
        @DisplayName("findElementsNormally with WebDriver should find single element when list is empty")
        void testFindElementsNormallyWithWebDriverEmptyList() {
            // Given
            when(driver.findElements(by)).thenReturn(Collections.emptyList());
            when(driver.findElement(by)).thenReturn(webElement);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsNormally(driver, by, waitConsumer);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }

        @Test
        @DisplayName("findElementsNormally with SmartWebElement should return list when elements found")
        void testFindElementsNormallyWithSmartWebElementElementsFound() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(webElement.findElements(by)).thenReturn(elements);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsNormally(smartElement, by, waitConsumer);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }

        @Test
        @DisplayName("findElementsNormally with SmartWebElement should find single element when list is empty")
        void testFindElementsNormallyWithSmartWebElementEmptyList() {
            // Given
            when(webElement.findElements(by)).thenReturn(Collections.emptyList());
            when(webElement.findElement(by)).thenReturn(webElement);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsNormally(smartElement, by, waitConsumer);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }
    }

    @Nested
    @DisplayName("findElementsWithShadowRootDriver tests")
    class FindElementsWithShadowRootDriverTests {

        @Test
        @DisplayName("findElementsWithShadowRootDriver should return list if found immediately")
        void testFindElementsWithShadowRootDriverElementsFoundImmediately() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(driver.findElements(by)).thenReturn(elements);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootDriver(smartDriver, by, waitConsumer);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }

        @Test
        @DisplayName("findElementsWithShadowRootDriver should search shadow roots if no elements found and shadow roots exist")
        void testFindElementsWithShadowRootDriverSearchShadowRoots() {
            // Given
            List<SmartWebElement> shadowElements = List.of(mock(SmartWebElement.class));
            when(driver.findElements(by)).thenReturn(Collections.emptyList());

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartDriver)).thenReturn(true);
                shadowDomUtils.when(() -> ShadowDomUtils.findElementsInShadowRoots(smartDriver, by)).thenReturn(shadowElements);

                // When
                List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootDriver(smartDriver, by, waitConsumer);

                // Then
                assertNotNull(results);
                assertSame(shadowElements, results);
                shadowDomUtils.verify(() -> ShadowDomUtils.findElementsInShadowRoots(smartDriver, by));
            }
        }

        @Test
        @DisplayName("findElementsWithShadowRootDriver should wait and find list if no shadow roots")
        void testFindElementsWithShadowRootDriverNoShadowRootsFoundList() {
            // Given
            List<WebElement> emptyList = Collections.emptyList();
            List<WebElement> foundList = List.of(webElement);

            when(driver.findElements(by))
                    .thenReturn(emptyList)
                    .thenReturn(foundList);

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartDriver)).thenReturn(false);

                // When
                List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootDriver(smartDriver, by, waitConsumer);

                // Then
                assertNotNull(results);
                assertEquals(1, results.size());
                assertEquals(webElement, results.get(0).getOriginal());
                verify(driver, times(2)).findElements(by);
            }
        }

        @Test
        @DisplayName("findElementsWithShadowRootDriver should wait and find single element if lists are empty")
        void testFindElementsWithShadowRootDriverNoShadowRootsFoundElement() {
            // Given
            when(driver.findElements(by)).thenReturn(Collections.emptyList());
            when(driver.findElement(by)).thenReturn(webElement);

            // Create a mock wait consumer that doesn't actually execute the function
            Consumer<Function<WebDriver, ?>> mockWaitConsumer = mock(Consumer.class);

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartDriver)).thenReturn(false);

                // When
                List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootDriver(smartDriver, by, mockWaitConsumer);

                // Then
                assertNotNull(results);
                assertEquals(1, results.size());
                assertEquals(webElement, results.get(0).getOriginal());
                verify(driver).findElement(by);
                verify(mockWaitConsumer).accept(any(Function.class));
            }
        }
    }

    @Nested
    @DisplayName("findElementsWithShadowRootElement tests")
    class FindElementsWithShadowRootElementTests {

        @Test
        @DisplayName("findElementsWithShadowRootElement should return list if found immediately")
        void testFindElementsWithShadowRootElementFoundImmediately() {
            // Given
            List<WebElement> elements = List.of(webElement);
            when(webElement.findElements(by)).thenReturn(elements);

            // When
            List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootElement(smartElement, by, waitConsumer);

            // Then
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals(webElement, results.get(0).getOriginal());
        }

        @Test
        @DisplayName("findElementsWithShadowRootElement should search shadow roots if no elements found and shadow roots exist")
        void testFindElementsWithShadowRootElementSearchShadowRoots() {
            // Given
            List<SmartWebElement> shadowElements = List.of(mock(SmartWebElement.class));
            when(webElement.findElements(by)).thenReturn(Collections.emptyList());

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartElement)).thenReturn(true);
                shadowDomUtils.when(() -> ShadowDomUtils.findElementsInShadowRoots(smartElement, by)).thenReturn(shadowElements);

                // When
                List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootElement(smartElement, by, waitConsumer);

                // Then
                assertNotNull(results);
                assertSame(shadowElements, results);
                shadowDomUtils.verify(() -> ShadowDomUtils.findElementsInShadowRoots(smartElement, by));
            }
        }

        @Test
        @DisplayName("findElementsWithShadowRootElement should call findElementsNormally if no shadow roots")
        void testFindElementsWithShadowRootElementNoShadowRoots() {
            // Given
            when(webElement.findElements(by))
                    .thenReturn(Collections.emptyList())
                    .thenReturn(List.of(webElement));

            try (MockedStatic<ShadowDomUtils> shadowDomUtils = mockStatic(ShadowDomUtils.class)) {
                shadowDomUtils.when(() -> ShadowDomUtils.shadowRootElementsPresent(smartElement)).thenReturn(false);

                // When
                List<SmartWebElement> results = SmartFinder.findElementsWithShadowRootElement(smartElement, by, waitConsumer);

                // Then
                assertNotNull(results);
                assertEquals(1, results.size());
                assertEquals(webElement, results.get(0).getOriginal());
                verify(webElement, times(2)).findElements(by);
            }
        }
    }

    @Nested
    @DisplayName("Wait function tests")
    class WaitFunctionTests {

        @Test
        @DisplayName("Wait consumer should pass expected condition to ExpectedConditions")
        void testWaitConsumerPassesExpectedCondition() {
            // Given
            ArgumentCaptor<Function<WebDriver, ?>> captor = ArgumentCaptor.forClass(Function.class);
            Consumer<Function<WebDriver, ?>> mockConsumer = mock(Consumer.class);
            lenient().when(driver.findElement(by)).thenReturn(webElement);


            // When
            SmartFinder.findElementNormally(driver, by, mockConsumer);

            // Then
            verify(mockConsumer).accept(captor.capture());
            Function<WebDriver, ?> capturedFunction = captor.getValue();
            capturedFunction.apply(driver);
        }
    }
}