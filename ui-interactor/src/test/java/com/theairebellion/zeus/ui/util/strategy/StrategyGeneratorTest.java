package com.theairebellion.zeus.ui.util.strategy;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StrategyGenerator Tests")
class StrategyGeneratorTest {

    @Mock
    private SmartWebElement element1;

    @Mock
    private SmartWebElement element2;

    @Mock
    private SmartWebElement element3;

    @Test
    @DisplayName("getRandomElementFromElements should return an element")
    void getRandomElementFromElementsReturnsRandomElement() {
        // Given
        List<SmartWebElement> elements = Arrays.asList(element1, element2, element3);

        // When
        SmartWebElement result = StrategyGenerator.getRandomElementFromElements(elements);

        // Then
        assertTrue(elements.contains(result), "Result should be one of the elements in the list");
    }

    @Test
    @DisplayName("getFirstElementFromElements should return the first element")
    void getFirstElementFromElementsReturnsFirstElement() {
        // Given
        List<SmartWebElement> elements = Arrays.asList(element1, element2, element3);

        // When
        SmartWebElement result = StrategyGenerator.getFirstElementFromElements(elements);

        // Then
        assertSame(element1, result);
    }

    @Test
    @DisplayName("getLastElementFromElements should return the last element")
    void getLastElementFromElementsReturnsLastElement() {
        // Given
        List<SmartWebElement> elements = Arrays.asList(element1, element2, element3);

        // When
        SmartWebElement result = StrategyGenerator.getLastElementFromElements(elements);

        // Then
        assertSame(element3, result);
    }

    @Test
    @DisplayName("getFirstElementFromElements should throw exception with empty list")
    void firstElementThrowsExceptionWithEmptyList() {
        // Given
        List<SmartWebElement> emptyList = new ArrayList<>();

        // Then
        assertThrows(IndexOutOfBoundsException.class,
                () -> StrategyGenerator.getFirstElementFromElements(emptyList));
    }

    @Test
    @DisplayName("getLastElementFromElements should throw exception with empty list")
    void lastElementThrowsExceptionWithEmptyList() {
        // Given
        List<SmartWebElement> emptyList = new ArrayList<>();

        // Then
        assertThrows(IndexOutOfBoundsException.class,
                () -> StrategyGenerator.getLastElementFromElements(emptyList));
    }

    @Test
    @DisplayName("getRandomElementFromElements should throw IllegalArgumentException with empty list")
    void randomElementThrowsExceptionWithEmptyList() {
        // Given
        List<SmartWebElement> emptyList = new ArrayList<>();

        // Then
        assertThrows(IllegalArgumentException.class,
                () -> StrategyGenerator.getRandomElementFromElements(emptyList));
    }
}