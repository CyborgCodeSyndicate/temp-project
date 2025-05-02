package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.annotation.Odyssey;
import com.theairebellion.zeus.framework.config.TestConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;

import static com.theairebellion.zeus.framework.util.PropertiesUtil.addSystemProperties;

/**
 * Base test class providing foundational test setup and utilities.
 *
 * <p>This class serves as the base for all test classes, handling global test configurations,
 * logging setup, and utility methods for retrieving stored test data.
 * It ensures consistent test initialization and provides convenience methods
 * for accessing stored test artifacts.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Odyssey
@SpringBootTest(
      classes = {TestConfig.class},
      webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Tag("exclude-from-verify")
public class BaseTest {

   private static final String RETRIEVAL_LOG_TEMPLATE = "Fetching data from storage by key: '{}' and type: '{}'";

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
   protected <T> T retrieve(Enum<?> key, Class<T> clazz) {
      SuperQuest quest = QuestHolder.get();
      LogTest.extended(RETRIEVAL_LOG_TEMPLATE, key.name(), clazz.getName());
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
   protected <T> T retrieve(Enum<?> subKey, Enum<?> key, Class<T> clazz) {
      SuperQuest quest = QuestHolder.get();
      LogTest.extended(RETRIEVAL_LOG_TEMPLATE, key.name(), clazz.getName());
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
   protected <T> T retrieve(DataExtractor<T> extractor, Class<T> clazz) {
      SuperQuest quest = QuestHolder.get();
      LogTest.extended(RETRIEVAL_LOG_TEMPLATE, extractor.getKey().name(),
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
   protected <T> T retrieve(DataExtractor<T> extractor, int index, Class<T> clazz) {
      SuperQuest quest = QuestHolder.get();
      LogTest.extended(RETRIEVAL_LOG_TEMPLATE, extractor.getKey().name(),
            clazz.getName());
      return quest.getStorage().get(extractor, clazz, index);
   }

   //todo: JavaDocs
   protected <T> T hookData(Object value, Class<T> clazz) {
      SuperQuest quest = QuestHolder.get();
      LogTest.extended(RETRIEVAL_LOG_TEMPLATE, value,
            clazz.getName());
      return quest.getStorage().getHookData(value, clazz);
   }

   /**
    * Provides static utility methods for retrieving stored test data.
    *
    * @author Cyborg Code Syndicate 💍👨💻
    */
   public static final class DefaultStorage {


      private DefaultStorage() {
      }

      /**
       * Retrieves stored test data by key within a sub-storage context.
       *
       * @param key   The key identifying the stored data.
       * @param clazz The expected type of the retrieved object.
       * @param <T>   The type parameter corresponding to the retrieved object.
       * @return The stored test data of the specified type.
       */
      public static <T> T retrieve(Enum<?> key, Class<T> clazz) {
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
      public static <T> T retrieve(DataExtractor<T> extractor, Class<T> clazz) {
         SuperQuest quest = QuestHolder.get();
         return quest.getStorage().sub().get(extractor, clazz);
      }

   }

}
