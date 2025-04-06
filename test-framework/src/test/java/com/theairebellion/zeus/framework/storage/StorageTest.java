package com.theairebellion.zeus.framework.storage;

import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.config.FrameworkConfigHolder;
import com.theairebellion.zeus.framework.storage.mock.DummyLate;
import com.theairebellion.zeus.framework.storage.mock.MockEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageTest {

    // Test constants
    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE = "value";
    private static final String VALUE_X = "valueX";
    private static final String HELLO = "hello";
    private static final String FIRST = "first";
    private static final String SECOND = "second";
    private static final String ONLY = "only";
    private static final String STRING = "string";
    private static final String SUB_VALUE = "subValue";
    private static final String DATA = "data";
    private static final String JOINED = "joined";
    private static final String NORMAL = "normal";

    private Storage storage;

    @Mock
    private FrameworkConfig mockConfig;

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @Nested
    @DisplayName("Basic put and get operations")
    class BasicOperations {

        @Test
        @DisplayName("Should store and retrieve value with class")
        void testPutAndGet() {
            // Given
            storage.put(MockEnum.KEY1, VALUE_1);

            // When
            String retrieved = storage.get(MockEnum.KEY1, String.class);

            // Then
            assertEquals(VALUE_1, retrieved, "Retrieved value should match stored value");
        }

        @Test
        @DisplayName("Should store and retrieve value with ParameterizedTypeReference")
        void testGetWithParameterizedTypeReference() {
            // Given
            storage.put(MockEnum.KEY1, VALUE_2);

            // When
            String retrieved = storage.get(MockEnum.KEY1, new ParameterizedTypeReference<String>() {});

            // Then
            assertEquals(VALUE_2, retrieved, "Retrieved value should match stored value");
        }

        @Test
        @DisplayName("Should return null when getting non-existent key")
        void testGetLatestValueWhenEmpty() {
            // When/Then
            assertNull(storage.get(MockEnum.KEY2, String.class),
                    "Should return null for non-existent key");
        }
    }

    @Nested
    @DisplayName("DataExtractor operations")
    class DataExtractorOperations {

        @Test
        @DisplayName("Should extract data without sub-key")
        void testGetDataExtractorWithoutSubKey() {
            // Given
            DataExtractor<String> extractor = new DataExtractorImpl<>(MockEnum.KEY1, raw -> raw + "X");
            storage.put(MockEnum.KEY1, VALUE);

            // When
            String result = storage.get(extractor, String.class);

            // Then
            assertEquals(VALUE_X, result, "Extracted value should have transformation applied");
        }

        @Test
        @DisplayName("Should extract data with sub-key")
        void testGetDataExtractorWithSubKey() {
            // Given
            DataExtractor<Integer> extractor = new DataExtractorImpl<>(
                    MockEnum.SUB, MockEnum.KEY1, raw -> ((String) raw).length());
            Storage subStorage = storage.sub(MockEnum.SUB);
            subStorage.put(MockEnum.KEY1, HELLO);

            // When
            Integer result = storage.get(extractor, Integer.class);

            // Then
            assertEquals(5, result, "Extracted value should have transformation applied");
        }

        @Test
        @DisplayName("Should extract indexed data without sub-key")
        void testGetDataExtractorByIndexWithoutSubKey() {
            // Given
            DataExtractor<String> extractor = new DataExtractorImpl<>(
                    MockEnum.KEY1, raw -> raw + "_suffix");
            storage.put(MockEnum.KEY1, "val1");
            storage.put(MockEnum.KEY1, "val2");

            // When
            String result1 = storage.get(extractor, String.class, 1);
            String result2 = storage.get(extractor, String.class, 2);

            // Then
            assertEquals("val2_suffix", result1, "First extracted value should be transformed val2");
            assertEquals("val1_suffix", result2, "Second extracted value should be transformed val1");
        }

        @Test
        @DisplayName("Should extract indexed data with sub-key")
        void testGetDataExtractorByIndexWithSubKey() {
            // Given
            DataExtractor<Integer> extractor = new DataExtractorImpl<>(
                    MockEnum.SUB, MockEnum.KEY1, raw -> ((String) raw).length());
            Storage subStorage = storage.sub(MockEnum.SUB);
            subStorage.put(MockEnum.KEY1, "abc");
            subStorage.put(MockEnum.KEY1, "de");

            // When
            Integer result1 = storage.get(extractor, Integer.class, 1);
            Integer result2 = storage.get(extractor, Integer.class, 2);

            // Then
            assertEquals(2, result1, "First extracted value should be length of 'de'");
            assertEquals(3, result2, "Second extracted value should be length of 'abc'");
        }
    }

    @Nested
    @DisplayName("Indexed operations")
    class IndexedOperations {

        @Test
        @DisplayName("Should retrieve values by index")
        void testGetByIndex() {
            // Given
            storage.put(MockEnum.KEY1, FIRST);
            storage.put(MockEnum.KEY1, SECOND);

            // When
            String last = storage.getByIndex(MockEnum.KEY1, 1, String.class);
            String secondLast = storage.getByIndex(MockEnum.KEY1, 2, String.class);

            // Then
            assertEquals(SECOND, last, "Index 1 should return the most recent value");
            assertEquals(FIRST, secondLast, "Index 2 should return the second most recent value");
        }

        @Test
        @DisplayName("Should retrieve values by index using ParameterizedTypeReference")
        void testGetByIndexWithParameterizedTypeReference() {
            // Given
            storage.put(MockEnum.KEY1, FIRST);
            storage.put(MockEnum.KEY1, SECOND);

            // When
            String last = storage.getByIndex(MockEnum.KEY1, 1, new ParameterizedTypeReference<String>() {});

            // Then
            assertEquals(SECOND, last, "Index 1 should return the most recent value");
        }

        @Test
        @DisplayName("Should return null for out of range index")
        void testGetByIndexOutOfRange() {
            // Given
            storage.put(MockEnum.KEY1, ONLY);

            // When/Then
            assertNull(storage.getByIndex(MockEnum.KEY1, 2, String.class),
                    "Should return null for out of range index");
            assertNull(storage.getByIndex(MockEnum.KEY1, 0, String.class),
                    "Should return null for invalid index 0");
            assertNull(storage.getByIndex(MockEnum.KEY2, 1, String.class),
                    "Should return null for non-existent key");
        }

        @Test
        @DisplayName("Should return null for invalid index with ParameterizedTypeReference")
        void testGetByIndexWithTypeRefOutOfRange() {
            // Given
            storage.put(MockEnum.KEY1, ONLY);

            // When/Then
            assertNull(storage.getByIndex(MockEnum.KEY1, 2, new ParameterizedTypeReference<String>() {}),
                    "Should return null for out of range index");
        }
    }

    @Nested
    @DisplayName("Class-based retrieval operations")
    class ClassBasedRetrievalOperations {

        @Test
        @DisplayName("Should retrieve value by class")
        void testGetByClass() {
            // Given
            storage.put(MockEnum.KEY1, STRING);
            storage.put(MockEnum.KEY1, 123);

            // When
            Integer value = storage.getByClass(MockEnum.KEY1, Integer.class);
            String strValue = storage.getByClass(MockEnum.KEY1, String.class);

            // Then
            assertEquals(123, value, "Should retrieve the most recent value of Integer type");
            assertEquals(STRING, strValue, "Should retrieve the most recent value of String type");
        }

        @Test
        @DisplayName("Should retrieve value by class with ParameterizedTypeReference")
        void testGetByClassWithParameterizedTypeReference() {
            // Given
            storage.put(MockEnum.KEY1, HELLO);
            storage.put(MockEnum.KEY1, 99);

            // When
            String result = storage.getByClass(MockEnum.KEY1, new ParameterizedTypeReference<String>() {});
            Integer intResult = storage.getByClass(MockEnum.KEY1, new ParameterizedTypeReference<Integer>() {});

            // Then
            assertEquals(HELLO, result, "Should retrieve the most recent value of String type");
            assertEquals(99, intResult, "Should retrieve the most recent value of Integer type");
        }

        @Test
        @DisplayName("Should return null when class not found")
        void testGetByClassWhenNotFound() {
            // Given
            storage.put(MockEnum.KEY1, "string only");

            // When/Then
            assertNull(storage.getByClass(MockEnum.KEY1, Integer.class),
                    "Should return null when no value of the requested type exists");
            assertNull(storage.getByClass(MockEnum.KEY2, String.class),
                    "Should return null for non-existent key");
        }

        @Test
        @DisplayName("Should return null when type reference not matched")
        void testGetByClassWithTypeRefWhenNotFound() {
            // Given
            storage.put(MockEnum.KEY1, "string only");

            // When/Then
            assertNull(storage.getByClass(MockEnum.KEY1, new ParameterizedTypeReference<Integer>() {}),
                    "Should return null when no value of the requested type exists");
        }

        @Test
        @DisplayName("Should retrieve all values by class")
        void testGetAllByClass() {
            // Given
            storage.put(MockEnum.KEY1, "a");
            storage.put(MockEnum.KEY1, 1);
            storage.put(MockEnum.KEY1, "b");

            // When
            List<String> strings = storage.getAllByClass(MockEnum.KEY1, String.class);
            List<Integer> integers = storage.getAllByClass(MockEnum.KEY1, Integer.class);

            // Then
            assertEquals(2, strings.size(), "Should retrieve 2 String values");
            assertEquals("a", strings.get(0), "First String should be 'a'");
            assertEquals("b", strings.get(1), "Second String should be 'b'");

            assertEquals(1, integers.size(), "Should retrieve 1 Integer value");
            assertEquals(1, integers.get(0), "Integer value should be 1");
        }

        @Test
        @DisplayName("Should retrieve all values by class with ParameterizedTypeReference")
        void testGetAllByClassWithParameterizedTypeReference() {
            // Given
            storage.put(MockEnum.KEY1, "a");
            storage.put(MockEnum.KEY1, 1);
            storage.put(MockEnum.KEY1, "b");

            // When
            List<String> result = storage.getAllByClass(MockEnum.KEY1, new ParameterizedTypeReference<String>() {});

            // Then
            assertNotNull(result, "Result should not be null");
            assertEquals(2, result.size(), "Should retrieve 2 String values");
            assertEquals("a", result.get(0), "First String should be 'a'");
            assertEquals("b", result.get(1), "Second String should be 'b'");
        }

        @Test
        @DisplayName("Should return empty list when no values of class exist")
        void testGetAllByClassWhenNoneExist() {
            // Given
            storage.put(MockEnum.KEY1, "string only");

            // When
            List<Integer> result = storage.getAllByClass(MockEnum.KEY1, Integer.class);
            List<String> emptyKeyResult = storage.getAllByClass(MockEnum.KEY2, String.class);

            // Then
            assertTrue(result.isEmpty(), "Result should be empty when no values of type exist");
            assertTrue(emptyKeyResult.isEmpty(), "Result should be empty for non-existent key");
        }

        @Test
        @DisplayName("Should return empty list when no values of parameterized type exist")
        void testGetAllByClassWithTypeRefWhenNoneExist() {
            // Given
            storage.put(MockEnum.KEY1, "string only");

            // When
            List<Integer> result = storage.getAllByClass(MockEnum.KEY1, new ParameterizedTypeReference<Integer>() {});

            // Then
            assertTrue(result.isEmpty(), "Result should be empty when no values of type exist");
        }
    }

    @Nested
    @DisplayName("Sub-storage operations")
    class SubStorageOperations {

        @Test
        @DisplayName("Should create new sub-storage when missing")
        void testSubCreatesNewStorageWhenMissing() {
            // When
            Storage sub = storage.sub(MockEnum.SUB);

            // Then
            assertNotNull(sub, "Substorage should not be null");

            // Additional verification
            sub.put(MockEnum.KEY1, SUB_VALUE);
            String retrieved = sub.get(MockEnum.KEY1, String.class);
            assertEquals(SUB_VALUE, retrieved, "Value stored in substorage should be retrievable");
        }

        @Test
        @DisplayName("Should return existing sub-storage")
        void testSubReturnsExistingStorage() {
            // Given
            Storage sub1 = storage.sub(MockEnum.SUB);
            sub1.put(MockEnum.KEY1, DATA);

            // When
            Storage sub2 = storage.sub(MockEnum.SUB);
            String retrieved = sub2.get(MockEnum.KEY1, String.class);

            // Then
            assertEquals(DATA, retrieved, "Substorage should maintain stored values");
        }

        @Test
        @DisplayName("Should throw exception when last value is not storage")
        void testSubThrowsWhenLastValueIsNotStorage() {
            // Given
            storage.put(MockEnum.NON_STORAGE, "not a storage");

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> storage.sub(MockEnum.NON_STORAGE),
                    "Should throw IllegalStateException for non-storage value");

            assertTrue(ex.getMessage().contains("already used for a non-storage value"),
                    "Exception message should mention non-storage value");
        }

        @Test
        @DisplayName("Should create sub-storage with default key when config is available")
        void testSubWithDefaultKey() {
            // Given
            try (MockedStatic<FrameworkConfigHolder> configHolderMock = mockStatic(FrameworkConfigHolder.class)) {
                when(mockConfig.defaultStorage()).thenReturn("SUB");
                configHolderMock.when(FrameworkConfigHolder::getFrameworkConfig).thenReturn(mockConfig);

                // When
                Storage sub = storage.sub(MockEnum.SUB);

                // Then
                assertNotNull(sub, "Substorage should be created");

                // When - calling default sub()
                Storage defaultSub = storage.sub();

                // Then
                assertNotNull(defaultSub, "Default substorage should be created");
                assertSame(sub, defaultSub, "Default storage should be the same instance");
            }
        }

        @Test
        @DisplayName("Should throw exception when default storage not initialized")
        void testSubDefaultThrowsWhenNotInitialized() {
            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> storage.sub(),
                    "Should throw IllegalStateException when default storage not initialized");

            assertEquals("There is no default storage initialized", ex.getMessage(),
                    "Exception message should indicate default storage not initialized");
        }
    }

    @Nested
    @DisplayName("Late argument operations")
    class LateArgumentOperations {

        @Test
        @DisplayName("Should join late arguments")
        void testJoinLateArguments() {
            // Given
            DummyLate<String> lateValue = new DummyLate<>(JOINED);
            storage.put(MockEnum.KEY1, lateValue);
            storage.put(MockEnum.KEY1, NORMAL);

            // When
            storage.joinLateArguments();

            // Then
            String joined = storage.getByIndex(MockEnum.KEY1, 2, String.class);
            String normal = storage.getByIndex(MockEnum.KEY1, 1, String.class);
            assertEquals(JOINED, joined, "Late value should be joined");
            assertEquals(NORMAL, normal, "Normal value should remain unchanged");
        }

        @Test
        @DisplayName("Should handle exceptions during join")
        void testJoinLateArgumentsWithException() {
            // Given
            DummyLate<String> failingLate = mock(DummyLate.class);
            when(failingLate.join()).thenThrow(new RuntimeException("Join failed"));

            storage.put(MockEnum.KEY1, failingLate);
            storage.put(MockEnum.KEY2, "This should remain");

            // When - should not throw exception
            storage.joinLateArguments();

            // Then
            // The failing Late object's value is skipped in the current implementation
            assertNull(storage.get(MockEnum.KEY1, Object.class),
                    "Failed Late objects are skipped in the current implementation");

            // But other keys should be unaffected
            assertEquals("This should remain", storage.get(MockEnum.KEY2, String.class),
                    "Other values should remain in storage");
        }
    }
}