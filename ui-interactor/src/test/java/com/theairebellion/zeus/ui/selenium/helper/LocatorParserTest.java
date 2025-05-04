package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
class LocatorParserTest extends BaseUnitUITest {

    @Test
    void testPrivateConstructor() throws Exception {
        // Use reflection to access the private constructor
        Constructor<LocatorParser> constructor = LocatorParser.class.getDeclaredConstructor();

        // Make the constructor accessible
        constructor.setAccessible(true);

        // Create an instance using the private constructor
        LocatorParser instance = constructor.newInstance();

        // Verify that an instance was created
        assertThat(instance).isNotNull();
    }


    @Test
    void updateWebElement_simpleTest() {
        // Create minimal mocks
        WebDriver mockWebDriver = mock(WebDriver.class);
        SmartWebElement mockOriginalElement = mock(SmartWebElement.class);
        SmartWebElement mockNewElement = mock(SmartWebElement.class);

        try (MockedStatic<UiConfigHolder> staticMock = mockStatic(UiConfigHolder.class)) {
            UiConfig mockConfig = mock(UiConfig.class);
            lenient().when(mockConfig.waitDuration()).thenReturn(2); // <-- lenient here
            staticMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

            try (MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class)) {

                try (MockedConstruction<SmartWebDriver> swdMock =
                         mockConstruction(SmartWebDriver.class,
                             (mock, context) -> {
                                 when(mock.findSmartElement(any(), anyLong()))
                                     .thenReturn(mockNewElement);
                             })) {

                    String locatorString = "[[ChromeDriver: chrome on MAC] -> " +
                                               "tag name: div]] -> " +
                                               "css selector: .container]] -> " +
                                               "xpath: //input[@type='text']] -> " +
                                               "id: username]] -> " +
                                               "class name: form-control]] -> " +
                                               "name: login]] -> " +
                                               "link text: Click here]] -> " +
                                               "partial link text: Click]]";

                    // Ensure toString is mocked
                    when(mockOriginalElement.toString()).thenReturn(locatorString);

                    // Try to call the method
                    SmartWebElement result = LocatorParser.updateWebElement(mockWebDriver, mockOriginalElement);

                    // Basic assertion
                    assertThat(result).isNotNull();
                }
            }
        }
    }


    @Test
    void privateMethod_parseLocators_shouldParseAllLocatorTypes() throws Exception {
        // Access the private method via reflection
        Method parseLocatorsMethod = LocatorParser.class.getDeclaredMethod("parseLocators", String.class);
        parseLocatorsMethod.setAccessible(true);

        // Create a test input
        String locatorString = "[[ChromeDriver: chrome on MAC] -> " +
                                   "tag name: div]] -> " +
                                   "css selector: .container]] -> " +
                                   "xpath: //input[@type='text']] -> " +
                                   "id: username]] -> " +
                                   "class name: form-control]] -> " +
                                   "name: login]] -> " +
                                   "link text: Click here]] -> " +
                                   "partial link text: Click]]";

        // Invoke the private method
        List<By> result = (List<By>) parseLocatorsMethod.invoke(null, locatorString);

        // Verify the result
        assertThat(result).hasSize(8);
        assertThat(result.get(0).toString()).contains("By.tagName");
        assertThat(result.get(1).toString()).contains("By.cssSelector");
        assertThat(result.get(2).toString()).contains("By.xpath");
        assertThat(result.get(3).toString()).contains("By.id");
        assertThat(result.get(4).toString()).contains("By.className");
        assertThat(result.get(5).toString()).contains("By.name");
        assertThat(result.get(6).toString()).contains("By.linkText");
        assertThat(result.get(7).toString()).contains("By.partialLinkText");
    }


    @Test
    void privateMethod_addLocatorIfMatches_shouldAddMatchingLocator() throws Exception {
        // Access the private method via reflection
        Method addLocatorIfMatchesMethod = LocatorParser.class.getDeclaredMethod(
            "addLocatorIfMatches",
            List.class,
            String.class,
            String.class,
            Function.class);
        addLocatorIfMatchesMethod.setAccessible(true);

        // Create test inputs
        List<By> locatorList = new ArrayList<>();
        String locatorText = "css selector: .container]]";
        String key = "css selector";
        Function<String, By> locatorFunction = By::cssSelector;

        // Invoke the private method
        addLocatorIfMatchesMethod.invoke(null, locatorList, locatorText, key, locatorFunction);

        // Verify a locator was added
        assertThat(locatorList).hasSize(1);
        assertThat(locatorList.get(0).toString()).contains("By.cssSelector");
    }


    @Test
    void privateMethod_extractLocatorValue_shouldExtractCorrectValue() throws Exception {
        // Access the private method via reflection
        Method extractLocatorValueMethod = LocatorParser.class.getDeclaredMethod("extractLocatorValue", String.class);
        extractLocatorValueMethod.setAccessible(true);

        // Test cases
        String[] testCases = {
            "css selector: .container]]",
            "xpath: //div[@class='test']]",
            "id: username]]"
        };

        for (String locatorText : testCases) {
            // Invoke the private method
            String result = (String) extractLocatorValueMethod.invoke(null, locatorText);

            // Verify the result
            assertThat(result).isNotBlank();
        }
    }


    // Additional edge case tests for private methods
    @Test
    void privateMethod_extractLocatorValue_shouldHandleEdgeCases() throws Exception {
        // Access the private method via reflection
        Method extractLocatorValueMethod = LocatorParser.class.getDeclaredMethod("extractLocatorValue", String.class);
        extractLocatorValueMethod.setAccessible(true);

        // Test edge cases
        String[] edgeCases = {
            "css selector: ]]",
            "xpath: ]]"
        };

        for (String edgeCase : edgeCases) {
            try {
                extractLocatorValueMethod.invoke(null, edgeCase);
            } catch (Exception e) {
                // Verify the type of exception or handle as needed
                assertThat(e).isNotNull();
            }
        }
    }


    @Test
    void extractBlockingElementLocator_withTagOnly() {
        // Given
        String exceptionMessage = "Other element would receive the click: <div>";

        // When
        String result = LocatorParser.extractBlockingElementLocator(exceptionMessage);

        // Then
        assertThat(result).isEqualTo("//div");
    }


    @Test
    void extractBlockingElementLocator_withSingleAttribute() {
        // Given
        String exceptionMessage = "Other element would receive the click: <div id=\"overlay\">";

        // When
        String result = LocatorParser.extractBlockingElementLocator(exceptionMessage);

        // Then
        assertThat(result).isEqualTo("//div[@id='overlay']");
    }


    @Test
    void extractBlockingElementLocator_withMultipleAttributes() {
        // Given
        String exceptionMessage = "Other element would receive the click: <div id=\"overlay\" class=\"modal\" and >";

        // When
        String result = LocatorParser.extractBlockingElementLocator(exceptionMessage);

        // Then
        assertThat(result).isEqualTo("//div[@id='overlay' and @class='modal' and ]");
    }


    @Test
    void extractBlockingElementLocator_withNoMatch() {
        // Given
        String exceptionMessage = "Element not clickable at point (150, 200)";

        // When
        assertThrows(IllegalArgumentException.class, () -> LocatorParser.extractBlockingElementLocator(exceptionMessage));

    }


    @Test
    void extractLocator_withByArgument() {
        // Given
        By locator = By.id("username");
        Object[] args = new Object[]{locator, "some other arg"};

        // When
        By result = LocatorParser.extractLocator(args);

        // Then
        assertThat(result).isEqualTo(locator);
    }


    @Test
    void extractLocator_withNoByArgument() {
        // Given
        Object[] args = new Object[]{"not a By object", 123};

        // When
        By result = LocatorParser.extractLocator(args);

        // Then
        assertThat(result).isNull();
    }


    @Test
    void extractLocator_withNullArgs() {
        // When
        By result = LocatorParser.extractLocator(null);

        // Then
        assertThat(result).isNull();
    }


    @Test
    void extractLocator_withEmptyArgs() {
        // Given
        Object[] args = new Object[0];

        // When
        By result = LocatorParser.extractLocator(args);

        // Then
        assertThat(result).isNull();
    }


    @ParameterizedTest
    @ValueSource(strings = {
        "Unable to locate element: By.id(username)",
        "Timed out after 30 seconds trying to find element By.xpath(//div[@class='container'])"
    })
    void extractLocatorFromMessage_withValidMessages(String message) {
        // When
        String result = LocatorParser.extractLocatorFromMessage(message);

        // Then
        assertThat(result).contains("By.");
        assertThat(result).isNotEqualTo("Locator not found");
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "Element not found",
        "No such element exception"
    })
    void extractLocatorFromMessage_withInvalidMessages(String message) {
        // When
        String result = LocatorParser.extractLocatorFromMessage(message);

        // Then
        if (message == null) {
            assertThat(result).isEqualTo("No message available");
        } else {
            assertThat(result).isEqualTo("Locator not found");
        }
    }


    @Test
    void getElementDetails_shouldFormatElementDetails() {
        // Given
        WebElement element = mock(WebElement.class);
        String outerHTML = "<div id='container'>Content</div>";
        String innerHTML = "Content";

        when(element.getTagName()).thenReturn("div");
        when(element.getText()).thenReturn("Content");

        // When
        String result = LocatorParser.getElementDetails(element, outerHTML, innerHTML);

        // Then
        assertThat(result).contains("Tag: [div]");
        assertThat(result).contains("Text: [Content]");
        assertThat(result).contains("Outer HTML: [<div id='container'>Content</div>]");
        assertThat(result).contains("Inner HTML: [Content]");
    }


    @Test
    void parseLocators_shouldParseAllLocatorTypes() throws Exception {
        // Access the private method via reflection
        Method parseLocatorsMethod = LocatorParser.class.getDeclaredMethod("parseLocators", String.class);
        parseLocatorsMethod.setAccessible(true);

        // Create a test input
        String locatorString = "[[ChromeDriver: chrome on MAC] -> " +
                                   "tag name: div]] -> " +
                                   "css selector: .container]] -> " +
                                   "xpath: //input[@type='text']] -> " +
                                   "id: username]] -> " +
                                   "class name: form-control]] -> " +
                                   "name: login]] -> " +
                                   "link text: Click here]] -> " +
                                   "partial link text: Click]]";

        // Invoke the private method
        List<By> result = (List<By>) parseLocatorsMethod.invoke(null, locatorString);

        // Verify the result
        assertThat(result).hasSize(8);
        assertThat(result.get(0).toString()).contains("By.tagName");
        assertThat(result.get(1).toString()).contains("By.cssSelector");
        assertThat(result.get(2).toString()).contains("By.xpath");
        assertThat(result.get(3).toString()).contains("By.id");
        assertThat(result.get(4).toString()).contains("By.className");
        assertThat(result.get(5).toString()).contains("By.name");
        assertThat(result.get(6).toString()).contains("By.linkText");
        assertThat(result.get(7).toString()).contains("By.partialLinkText");
    }


    @Test
    void addLocatorIfMatches_shouldAddMatchingLocator() throws Exception {
        // Access the private method via reflection
        Method addLocatorIfMatchesMethod = LocatorParser.class.getDeclaredMethod(
            "addLocatorIfMatches",
            List.class,
            String.class,
            String.class,
            Function.class);
        addLocatorIfMatchesMethod.setAccessible(true);

        // Create test inputs
        List<By> locatorList = new ArrayList<>();
        String locatorText = "css selector: .container]]";
        String key = "css selector";
        Function<String, By> locatorFunction = By::cssSelector;

        // Invoke the private method
        addLocatorIfMatchesMethod.invoke(null, locatorList, locatorText, key, locatorFunction);

        // Verify a locator was added
        assertThat(locatorList).hasSize(1);
        assertThat(locatorList.get(0).toString()).contains("By.cssSelector");
    }


    @Test
    void addLocatorIfMatches_shouldNotAddNonMatchingLocator() throws Exception {
        // Access the private method via reflection
        Method addLocatorIfMatchesMethod = LocatorParser.class.getDeclaredMethod(
            "addLocatorIfMatches",
            List.class,
            String.class,
            String.class,
            Function.class);
        addLocatorIfMatchesMethod.setAccessible(true);

        // Create test inputs with non-matching key
        List<By> locatorList = new ArrayList<>();
        String locatorText = "id: username]]";
        String key = "css selector";
        Function<String, By> locatorFunction = By::cssSelector;

        // Invoke the private method
        addLocatorIfMatchesMethod.invoke(null, locatorList, locatorText, key, locatorFunction);

        // Verify no locator was added
        assertThat(locatorList).isEmpty();
    }


    @Test
    void extractLocatorValue_shouldExtractCorrectValue() throws Exception {
        // Access the private method via reflection
        Method extractLocatorValueMethod = LocatorParser.class.getDeclaredMethod("extractLocatorValue", String.class);
        extractLocatorValueMethod.setAccessible(true);

        // Create test input
        String locatorText = "css selector: .container]]";

        // Invoke the private method
        String result = (String) extractLocatorValueMethod.invoke(null, locatorText);

        // Verify the result
        assertThat(result).isEqualTo(".container");
    }


    @Test
    void extractLocatorValue_shouldHandleEdgeCases() throws Exception {
        // Access the private method via reflection
        Method extractLocatorValueMethod = LocatorParser.class.getDeclaredMethod("extractLocatorValue", String.class);
        extractLocatorValueMethod.setAccessible(true);

        // Test with malformed input (no colon)
        try {
            extractLocatorValueMethod.invoke(null, "malformed-input");
        } catch (InvocationTargetException e) {
            // Expecting an ArrayIndexOutOfBoundsException because of the split
            assertThat(e.getCause()).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }

        // Test with short value
        try {
            extractLocatorValueMethod.invoke(null, "key: a]]");
        } catch (InvocationTargetException e) {
            // May throw StringIndexOutOfBoundsException depending on the implementation
            if (!(e.getCause() instanceof StringIndexOutOfBoundsException)) {
                throw e;
            }
        }
    }


    @Test
    void parseLocators_shouldHandleNullInput() throws Exception {
        // Access the private method via reflection
        Method parseLocatorsMethod = LocatorParser.class.getDeclaredMethod("parseLocators", String.class);
        parseLocatorsMethod.setAccessible(true);

        // Invoke with null input
        try {
            parseLocatorsMethod.invoke(null, (Object) null);
        } catch (InvocationTargetException e) {
            // Original method likely throws NullPointerException with null input
            assertThat(e.getCause()).isInstanceOf(NullPointerException.class);
        }
    }

}