package com.theairebellion.zeus.framework.storage;

import lombok.Getter;

/**
 * Defines keys used for storing and retrieving global test execution data.
 *
 * <p>This enum provides unique identifiers for managing test execution contexts,
 * metadata, and framework-level consumers within the storage system.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public enum StoreKeys {

   /**
    * Key for storing the current {@code SuperQuest} instance.
    */
   QUEST,

   /**
    * Key for storing a list of consumers that modify or interact with the quest instance.
    */
   QUEST_CONSUMERS,

   /**
    * Key for storing the start time of the test execution.
    */
   START_TIME,

   /**
    * Key for storing the rendered HTML output or fragments for use in reporting or visualization.
    */
   HTML,

   /**
    * Key for storing hook-related parameters, such as setup and teardown data,
    * to be shared between extensions and test logic.
    */
   HOOKS_PARAMS;

}
