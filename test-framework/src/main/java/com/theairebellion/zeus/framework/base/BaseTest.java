package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.ai.metadata.model.classes.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.framework.annotation.Odyssey;
import com.theairebellion.zeus.framework.config.TestConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Base test class providing foundational test setup and utilities.
 * <p>
 * This class serves as the base for all test classes, handling global test configurations,
 * logging setup, and utility methods for retrieving stored test data.
 * It ensures consistent test initialization and provides convenience methods
 * for accessing stored test artifacts.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Odyssey
@SpringBootTest(
        classes = {TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@InfoAIClass(
        level = Level.LAST,
        description = "Base test class that all test classes extend from. Provides global test configurations, " +
                "logging setup, and utility methods for retrieving stored test data. Each test method has access to a quest object for interacting with the test storage system."
)
public class BaseTest {

    static {
        synchronized (BaseTest.class) {
            addSystemProperties();
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.reconfigure();
        }
    }

    /**
     * Retrieves stored test data by key.
     *
     * @param key   The key identifying the stored data.
     * @param clazz The expected type of the retrieved object.
     * @param <T>   The type parameter corresponding to the retrieved object.
     * @return The stored test data of the specified type.
     */
    @InfoAI(description = "Fetches stored test data by key from the test storage system. " +
            "The key must be an enum value representing a stored entity, and the returned object will be of the specified type.")
    public <T> T retrieve(@InfoAI(description = "The key representing the stored data in the test storage system.") Enum<?> key,
                             @InfoAI(description = "The expected class type of the retrieved object.") Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", key.name(), clazz.getName());
        return quest.getStorage().get(key, clazz);
    }

    /**
     * Retrieves stored test data from a sub-key.
     *
     * @param subKey The sub-key identifying the data subset.
     * @param key    The key identifying the stored data.
     * @param clazz  The expected type of the retrieved object.
     * @param <T>    The type parameter corresponding to the retrieved object.
     * @return The stored test data from the specified sub-key.
     */
    @InfoAI(description = "Fetches stored test data from a nested storage structure using a sub-key and a main key. " +
            "The sub-key represents a subset of stored data, and the main key identifies the specific entity within that subset.")
    public <T> T retrieve(@InfoAI(description = "The sub-key representing a specific subset of stored data.") Enum<?> subKey,
                             @InfoAI(description = "The key representing the stored data entity within the sub-key subset.") Enum<?> key,
                             @InfoAI(description = "The expected class type of the retrieved object.") Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", key.name(), clazz.getName());
        return quest.getStorage().sub(subKey).get(key, clazz);
    }

    /**
     * Retrieves test data using a {@code DataExtractor}.
     *
     * @param extractor The data extractor to retrieve test data.
     * @param clazz     The expected type of the retrieved object.
     * @param <T>       The type parameter corresponding to the retrieved object.
     * @return The extracted test data of the specified type.
     */
    @InfoAI(description = "Fetches stored test data using a DataExtractor. " +
            "The extractor defines how the data is retrieved, and the returned object is of the specified type.")
    public <T> T retrieve(@InfoAI(description = "The data extractor defining the retrieval logic for stored test data.") DataExtractor<T> extractor,
                             @InfoAI(description = "The expected class type of the retrieved object.") Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", extractor.getKey().name(),
                clazz.getName());
        return quest.getStorage().get(extractor, clazz);
    }

    /**
     * Retrieves test data using a {@code DataExtractor} at a specified index.
     *
     * @param extractor The data extractor to retrieve test data.
     * @param index     The index of the extracted data.
     * @param clazz     The expected type of the retrieved object.
     * @param <T>       The type parameter corresponding to the retrieved object.
     * @return The extracted test data of the specified type at the given index.
     */
    @InfoAI(description = "Fetches stored test data using a DataExtractor at a specified index. " +
            "The extractor defines how the data is retrieved, and the index determines which specific entry is returned.")
    public <T> T retrieve(@InfoAI(description = "The data extractor defining the retrieval logic for stored test data.") DataExtractor<T> extractor,
                             @InfoAI(description = "The index position of the extracted data entry.") int index,
                             @InfoAI(description = "The expected class type of the retrieved object.") Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", extractor.getKey().name(),
                clazz.getName());
        return quest.getStorage().get(extractor, clazz, index);
    }

    /**
     * Loads and sets system properties from the {@code system.properties} file if present.
     *
     * <p>
     * If a property is already set in the system, it is not overridden.
     * </p>
     */
    private static void addSystemProperties() {
        Resource resource = new ClassPathResource("system.properties");
        if (resource.exists()) {
            try {
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                for (String propName : props.stringPropertyNames()) {
                    String propValue = props.getProperty(propName);
                    if (System.getProperty(propName) == null) {
                        System.setProperty(propName, propValue);
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to load system.properties", e);
            }
        }
    }

    /**
     * Provides static utility methods for retrieving stored test data.
     */
    @InfoAIClass(
            level = Level.LAST,
            description = "Provides utility methods for retrieving stored test data within a sub-storage context, making it accessible in test cases.")
    public static final class DefaultStorage {

        /**
         * Retrieves stored test data by key within a sub-storage context.
         *
         * @param key   The key identifying the stored data.
         * @param clazz The expected type of the retrieved object.
         * @param <T>   The type parameter corresponding to the retrieved object.
         * @return The stored test data of the specified type.
         */
        @InfoAI(description = "Retrieves stored test data from a sub-storage system using a key.")
        public static <T> T retrieve(@InfoAI(description = "The key representing the stored data entity.") Enum<?> key,
                                     @InfoAI(description = "The expected class type of the retrieved object.") Class<T> clazz) {
            SuperQuest quest = QuestHolder.get();
            return quest.getStorage().sub().get(key, clazz);
        }

        /**
         * Retrieves test data using a {@code DataExtractor} within a sub-storage context.
         *
         * @param extractor The data extractor to retrieve test data.
         * @param clazz     The expected type of the retrieved object.
         * @param <T>       The type parameter corresponding to the retrieved object.
         * @return The extracted test data of the specified type.
         */
        @InfoAI(description = "Fetches stored test data from a sub-storage context using a DataExtractor. " +
                "The extractor defines how the data is retrieved, and the returned object is of the specified type.")
        public static <T> T retrieve(@InfoAI(description = "The data extractor defining the retrieval logic for stored test data.") DataExtractor<T> extractor,
                                     @InfoAI(description = "The expected class type of the retrieved object.") Class<T> clazz) {
            SuperQuest quest = QuestHolder.get();
            return quest.getStorage().sub().get(extractor, clazz);
        }

    }

}
