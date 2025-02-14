package com.theairebellion.zeus.framework.storage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.function.Function;

import com.theairebellion.zeus.framework.storage.mock.MockEnum;
import org.junit.jupiter.api.Test;

public class DataExtractorImplUnitTest {

    @Test
    void testConstructorWithSubKey() {
        Function<Object, String> extractionLogic = raw -> ((String) raw).toUpperCase();
        DataExtractorImpl<String> extractor = new DataExtractorImpl<>(MockEnum.KEY2, MockEnum.KEY1, extractionLogic);
        assertEquals(MockEnum.KEY2, extractor.getSubKey());
        assertEquals(MockEnum.KEY1, extractor.getKey());
        String result = extractor.extract("hello");
        assertEquals("HELLO", result);
    }

    @Test
    void testConstructorWithoutSubKey() {
        Function<Object, Integer> extractionLogic = raw -> ((String) raw).length();
        DataExtractorImpl<Integer> extractor = new DataExtractorImpl<>(MockEnum.KEY1, extractionLogic);
        assertNull(extractor.getSubKey());
        assertEquals(MockEnum.KEY1, extractor.getKey());
        Integer result = extractor.extract("test");
        assertEquals(4, result);
    }
}
