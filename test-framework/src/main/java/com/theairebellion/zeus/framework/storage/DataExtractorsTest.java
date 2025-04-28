package com.theairebellion.zeus.framework.storage;


import java.util.Map;

/**
 * Provides utility methods for creating {@link DataExtractor} instances that retrieve
 * static test data from the framework's storage.
 * <p>
 * This class focuses on accessing and returning values from the static test data map,
 * allowing tests to dynamically fetch pre-defined data by a string key.
 * </p>
 *
 * <p>
 * Generic Type {@code T}: the type of the data retrieved from the static test data map.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class DataExtractorsTest {

    private DataExtractorsTest() {
    }

    /**
     * Creates a {@link DataExtractor} for retrieving a specific value from the static test data map.
     * <p>
     * The returned extractor locates the static test data map within the storage and fetches
     * the object associated with the provided key.
     * </p>
     *
     * @param key the string key identifying the data in the static test data map
     * @param <T> the type of the data to be extracted
     * @return a {@code DataExtractor} that retrieves the specified value from static test data
     */
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
