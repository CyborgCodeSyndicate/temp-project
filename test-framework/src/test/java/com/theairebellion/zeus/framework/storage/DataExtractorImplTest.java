package com.theairebellion.zeus.framework.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.theairebellion.zeus.framework.storage.mock.MockEnum;
import org.junit.jupiter.api.Test;

public class DataExtractorImplTest {


    @Test
    void testExtractorWithSubKey() {
        DataExtractorImpl<String> extractor = new DataExtractorImpl<>(MockEnum.KEY2, MockEnum.KEY1, raw -> raw.toString().toUpperCase());
        assertEquals(MockEnum.KEY2, extractor.getSubKey());
        assertEquals(MockEnum.KEY1, extractor.getKey());
        String result = extractor.extract("hello");
        assertEquals("HELLO", result);
    }

    @Test
    void testExtractorWithoutSubKey() {
        DataExtractorImpl<Integer> extractor = new DataExtractorImpl<>(MockEnum.KEY1, raw -> ((String) raw).length());
        assertNull(extractor.getSubKey());
        assertEquals(MockEnum.KEY1, extractor.getKey());
        Integer result = extractor.extract("abcd");
        assertEquals(4, result);
    }
}