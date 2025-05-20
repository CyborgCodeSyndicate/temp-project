package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.exceptions.UiInteractionException;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("updateWebElement should successfully update element with complex locator string")
    void updateWebElement_simpleTest() {
        // Given
        WebDriver mockWebDriver = mock(WebDriver.class);
        SmartWebElement mockOriginalElement = mock(SmartWebElement.class);
        SmartWebElement mockNewElement = mock(SmartWebElement.class);

        // When
        try (MockedConstruction<SmartWebDriver> swdMock = mockConstruction(SmartWebDriver.class, (mock, context) -> {
            when(mock.findSmartElement(any(), anyLong())).thenReturn(mockNewElement);
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
            when(mockOriginalElement.toString()).thenReturn(locatorString);
            SmartWebElement result = LocatorParser.updateWebElement(mockWebDriver, mockOriginalElement);

            // Then
            assertThat(result).isNotNull();
        }
    }

    @Test
    @DisplayName("updateWebElement should throw UiInteractionException when no locators can be parsed")
    void updateWebElement_shouldThrowWhenNoLocatorsFound() {
        // Given
        WebDriver mockWebDriver = mock(WebDriver.class);
        SmartWebElement mockElement = mock(SmartWebElement.class);

        // When
        when(mockElement.toString()).thenReturn("Invalid locator string format");

        // Then
        assertThrows(UiInteractionException.class, () ->
                LocatorParser.updateWebElement(mockWebDriver, mockElement));
    }

    @Test
    @DisplayName("extractBlockingElementLocator should return basic xpath when only tag name is present")
    void extractBlockingElementLocator_withTagOnly() {
        // Given
        String exceptionMessage = "Other element would receive the click: <div>";

        // When
        String result = LocatorParser.extractBlockingElementLocator(exceptionMessage);

        // Then
        assertThat(result).isEqualTo("//div");
    }


    @Test
    @DisplayName("extractBlockingElementLocator should return xpath with single attribute when present")
    void extractBlockingElementLocator_withSingleAttribute() {
        // Given
        String exceptionMessage = "Other element would receive the click: <div id=\"overlay\">";

        // When
        String result = LocatorParser.extractBlockingElementLocator(exceptionMessage);

        // Then
        assertThat(result).isEqualTo("//div[@id='overlay']");
    }


    @Test
    @DisplayName("extractBlockingElementLocator should return xpath with multiple attributes when present")
    void extractBlockingElementLocator_withMultipleAttributes() {
        // Given
        String exceptionMessage = "Other element would receive the click: <div id=\"overlay\" class=\"modal\" and >";

        // When
        String result = LocatorParser.extractBlockingElementLocator(exceptionMessage);

        // Then
        assertThat(result).isEqualTo("//div[@id='overlay' and @class='modal' and ]");
    }


    @Test
    @DisplayName("extractBlockingElementLocator should throw exception when no matching pattern found")
    void extractBlockingElementLocator_withNoMatch() {
        // Given
        String exceptionMessage = "Element not clickable at point (150, 200)";

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> LocatorParser.extractBlockingElementLocator(exceptionMessage));
    }


    @Test
    @DisplayName("extractLocator should return By object when present in arguments")
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
    @DisplayName("extractLocator should return null when no By object in arguments")
    void extractLocator_withNoByArgument() {
        // Given
        Object[] args = new Object[]{"not a By object", 123};

        // When
        By result = LocatorParser.extractLocator(args);

        // Then
        assertThat(result).isNull();
    }


    @Test
    @DisplayName("extractLocator should return null when arguments array is null")
    void extractLocator_withNullArgs() {
        // When
        By result = LocatorParser.extractLocator(null);

        // Then
        assertThat(result).isNull();
    }


    @Test
    @DisplayName("extractLocator should return null when arguments array is empty")
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
    @DisplayName("extractLocatorFromMessage should successfully extract locator from valid error messages")
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
    @DisplayName("extractLocatorFromMessage should handle invalid or null error messages appropriately")
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
    @DisplayName("getElementDetails should format all element details including tag, text and HTML")
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

}