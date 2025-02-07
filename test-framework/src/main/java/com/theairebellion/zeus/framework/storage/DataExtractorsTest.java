package com.theairebellion.zeus.framework.storage;


import java.util.Map;

public class DataExtractorsTest {

    public static <T> DataExtractor<T> staticTestData(String key) {
        return new DataExtractorImpl<>(
            StorageKeysTest.STATIC_DATA,
            raw -> {
                Map<String, Object> staticTestData = (Map<String, Object>) raw;
                return (T) staticTestData.get(key);
            }
        );
    }

}
