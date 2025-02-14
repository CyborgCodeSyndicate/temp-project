package com.theairebellion.zeus.framework.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class DataExtractorsTestUnitTest {

    @Test
    void testStaticTestDataExtractionReturnsValue() {
        Map<String, Object> testData = new HashMap<>();
        testData.put("myKey", "expectedValue");
        DataExtractor<String> extractor = DataExtractorsTest.staticTestData("myKey");
        assertNull(extractor.getSubKey());
        assertEquals(StorageKeysTest.STATIC_DATA, extractor.getKey());
        String result = extractor.extract(testData);
        assertEquals("expectedValue", result);
    }

    @Test
    void testStaticTestDataExtractionReturnsNullWhenMissing() {
        Map<String, Object> testData = new HashMap<>();
        DataExtractor<String> extractor = DataExtractorsTest.staticTestData("nonExistentKey");
        String result = extractor.extract(testData);
        assertNull(result);
    }
}