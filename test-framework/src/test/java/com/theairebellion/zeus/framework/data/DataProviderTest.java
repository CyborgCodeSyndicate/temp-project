package com.theairebellion.zeus.framework.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataProvider Interface Tests")
class DataProviderTest {

    @Test
    @DisplayName("testStaticData() should return map of test data")
    void testTestStaticData() {
        // Given
        DataProvider dataProvider = mock(DataProvider.class);
        when(dataProvider.testStaticData()).thenReturn(Map.of("key", "value"));

        // When
        Map<String, Object> result = dataProvider.testStaticData();

        // Then
        assertEquals(Map.of("key", "value"), result);
        verify(dataProvider).testStaticData();
        verifyNoMoreInteractions(dataProvider);
    }

    @Test
    @DisplayName("testStaticData() should handle empty map")
    void testTestStaticDataWithEmptyMap() {
        // Given
        DataProvider dataProvider = mock(DataProvider.class);
        when(dataProvider.testStaticData()).thenReturn(Collections.emptyMap());

        // When
        Map<String, Object> result = dataProvider.testStaticData();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(dataProvider).testStaticData();
    }

    @Test
    @DisplayName("testStaticData() should handle null values in map")
    void testTestStaticDataWithNullValues() {
        // Given
        DataProvider dataProvider = mock(DataProvider.class);
        Map<String, Object> testData = new HashMap<>();
        testData.put("key1", "value1");
        testData.put("key2", null);

        when(dataProvider.testStaticData()).thenReturn(testData);

        // When
        Map<String, Object> result = dataProvider.testStaticData();

        // Then
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertNull(result.get("key2"));
        verify(dataProvider).testStaticData();
    }

    @Test
    @DisplayName("Concrete implementation should work correctly")
    void testConcreteImplementation() {
        // Given
        DataProvider dataProvider = new ConcreteDataProvider();

        // When
        Map<String, Object> result = dataProvider.testStaticData();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("concrete", result.get("test"));
    }

    // Simple concrete implementation for testing
    private static class ConcreteDataProvider implements DataProvider {
        @Override
        public Map<String, Object> testStaticData() {
            return Map.of("test", "concrete");
        }
    }
}