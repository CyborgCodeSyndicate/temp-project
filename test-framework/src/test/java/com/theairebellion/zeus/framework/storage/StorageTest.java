package com.theairebellion.zeus.framework.storage;

import com.theairebellion.zeus.framework.storage.mock.DummyLate;
import com.theairebellion.zeus.framework.storage.mock.MockEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    public static final String VALUE_1 = "value1";
    public static final String VALUE_2 = "value2";
    public static final String VALUE = "value";
    public static final String VALUE_X = "valueX";
    public static final String HELLO = "hello";
    public static final String FIRST = "first";
    public static final String SECOND = "second";
    public static final String ONLY = "only";
    public static final String STRING = "string";
    public static final String SUB_VALUE = "subValue";
    public static final String DATA = "data";
    public static final String JOINED = "joined";
    public static final String NORMAL = "normal";

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @Test
    void testPutAndGet() {
        storage.put(MockEnum.KEY1, VALUE_1);
        String retrieved = storage.get(MockEnum.KEY1, String.class);
        assertEquals(VALUE_1, retrieved);
    }

    @Test
    void testGetWithParameterizedTypeReference() {
        storage.put(MockEnum.KEY1, VALUE_2);
        String retrieved = storage.get(MockEnum.KEY1, new ParameterizedTypeReference<>() {
        });
        assertEquals(VALUE_2, retrieved);
    }

    @Test
    void testGetDataExtractorWithoutSubKey() {
        DataExtractor<String> extractor = new DataExtractorImpl<>(MockEnum.KEY1, raw -> raw + "X");
        storage.put(MockEnum.KEY1, VALUE);
        String result = storage.get(extractor, String.class);
        assertEquals(VALUE_X, result);
    }

    @Test
    void testGetDataExtractorWithSubKey() {
        DataExtractor<Integer> extractor = new DataExtractorImpl<>(MockEnum.SUB, MockEnum.KEY1, raw -> ((String) raw).length());
        Storage subStorage = storage.sub(MockEnum.SUB);
        subStorage.put(MockEnum.KEY1, HELLO);
        Integer result = storage.get(extractor, Integer.class);
        assertEquals(5, result);
    }

    @Test
    void testGetByIndex() {
        storage.put(MockEnum.KEY1, FIRST);
        storage.put(MockEnum.KEY1, SECOND);
        String last = storage.getByIndex(MockEnum.KEY1, 1, String.class);
        String secondLast = storage.getByIndex(MockEnum.KEY1, 2, String.class);
        assertEquals(SECOND, last);
        assertEquals(FIRST, secondLast);
    }

    @Test
    void testGetByIndexOutOfRange() {
        storage.put(MockEnum.KEY1, ONLY);
        assertNull(storage.getByIndex(MockEnum.KEY1, 2, String.class));
    }

    @Test
    void testGetByClass() {
        storage.put(MockEnum.KEY1, STRING);
        storage.put(MockEnum.KEY1, 123);
        Integer value = storage.getByClass(MockEnum.KEY1, Integer.class);
        assertEquals(123, value);
    }

    @Test
    void testGetAllByClass() {
        storage.put(MockEnum.KEY1, "a");
        storage.put(MockEnum.KEY1, 1);
        storage.put(MockEnum.KEY1, "b");
        List<String> strings = storage.getAllByClass(MockEnum.KEY1, String.class);
        assertEquals(2, strings.size());
        assertEquals("a", strings.get(0));
        assertEquals("b", strings.get(1));
    }

    @Test
    void testSubCreatesNewStorageWhenMissing() {
        Storage sub = storage.sub(MockEnum.SUB);
        assertNotNull(sub);
        sub.put(MockEnum.KEY1, SUB_VALUE);
        String retrieved = sub.get(MockEnum.KEY1, String.class);
        assertEquals(SUB_VALUE, retrieved);
    }

    @Test
    void testSubReturnsExistingStorage() {
        Storage sub1 = storage.sub(MockEnum.SUB);
        sub1.put(MockEnum.KEY1, DATA);
        Storage sub2 = storage.sub(MockEnum.SUB);
        String retrieved = sub2.get(MockEnum.KEY1, String.class);
        assertEquals(DATA, retrieved);
    }

    @Test
    void testSubThrowsWhenLastValueIsNotStorage() {
        storage.put(MockEnum.NON_STORAGE, "not a storage");
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> storage.sub(MockEnum.NON_STORAGE));
        assertTrue(ex.getMessage().contains("already used for a non-storage value"));
    }

    @Test
    void testSubDefaultThrowsWhenNotInitialized() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> storage.sub());
        assertEquals("There is no default storage initialized", ex.getMessage());
    }

    @Test
    void testJoinLateArguments() {
        DummyLate<String> lateValue = new DummyLate<>(JOINED);
        storage.put(MockEnum.KEY1, lateValue);
        storage.put(MockEnum.KEY1, NORMAL);
        storage.joinLateArguments();
        String joined = storage.getByIndex(MockEnum.KEY1, 2, String.class);
        String normal = storage.getByIndex(MockEnum.KEY1, 1, String.class);
        assertEquals(JOINED, joined);
        assertEquals(NORMAL, normal);
    }

    @Test
    void testGetLatestValueWhenEmpty() {
        assertNull(storage.get(MockEnum.KEY2, String.class));
    }

    @Test
    void testGetValueByIndexWhenEmpty() {
        assertNull(storage.getByIndex(MockEnum.KEY2, 1, String.class));
    }

    @Test
    void testGetDataExtractorByIndexWithoutSubKey() {
        DataExtractor<String> extractor = new DataExtractorImpl<>(MockEnum.KEY1, raw -> raw + "_suffix");
        storage.put(MockEnum.KEY1, "val1");
        storage.put(MockEnum.KEY1, "val2");
        String result1 = storage.get(extractor, String.class, 1);
        assertEquals("val2_suffix", result1);
        String result2 = storage.get(extractor, String.class, 2);
        assertEquals("val1_suffix", result2);
    }

    @Test
    void testGetDataExtractorByIndexWithSubKey() {
        DataExtractor<Integer> extractor = new DataExtractorImpl<>(MockEnum.SUB, MockEnum.KEY1, raw -> ((String) raw).length());
        Storage subStorage = storage.sub(MockEnum.SUB);
        subStorage.put(MockEnum.KEY1, "abc");
        subStorage.put(MockEnum.KEY1, "de");
        Integer result1 = storage.get(extractor, Integer.class, 1);
        assertEquals(2, result1);
        Integer result2 = storage.get(extractor, Integer.class, 2);
        assertEquals(3, result2);
    }

    @Test
    void testGetByClassWithParameterizedTypeReference() {
        storage.put(MockEnum.KEY1, HELLO);
        storage.put(MockEnum.KEY1, 99);
        String result = storage.getByClass(MockEnum.KEY1, new ParameterizedTypeReference<>() {
        });
        assertEquals(HELLO, result);
    }

    @Test
    void testGetAllByClassWithParameterizedTypeReference() {
        storage.put(MockEnum.KEY1, "a");
        storage.put(MockEnum.KEY1, 1);
        storage.put(MockEnum.KEY1, "b");
        List<String> result = storage.getAllByClass(MockEnum.KEY1, new ParameterizedTypeReference<>() {
        });
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
    }
}
