package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.annotation.Odyssey;
import com.theairebellion.zeus.framework.config.TestConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.boot.test.context.SpringBootTest;

import static com.theairebellion.zeus.framework.util.PropertiesUtil.addSystemProperties;

@Odyssey
@SpringBootTest(
    classes = {TestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class BaseTest {

    static {
        synchronized (BaseTest.class) {
            addSystemProperties();
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.reconfigure();
        }
    }


    protected <T> T retrieve(Enum<?> key, Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", key.name(), clazz.getName());
        return quest.getStorage().get(key, clazz);
    }


    protected <T> T retrieve(Enum<?> subKey, Enum<?> key, Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", key.name(), clazz.getName());
        return quest.getStorage().sub(subKey).get(key, clazz);
    }


    protected <T> T retrieve(DataExtractor<T> extractor, Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", extractor.getKey().name(),
            clazz.getName());
        return quest.getStorage().get(extractor, clazz);
    }


    protected <T> T retrieve(DataExtractor<T> extractor, int index, Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", extractor.getKey().name(),
            clazz.getName());
        return quest.getStorage().get(extractor, clazz, index);
    }


    protected <T> T hookData(Object value, Class<T> clazz) {
        SuperQuest quest = QuestHolder.get();
        LogTest.extended("Fetching data from before hooks storage by key: '{}' and type: '{}'", value,
            clazz.getName());
        return quest.getStorage().getHookData(value, clazz);
    }


    public static final class DefaultStorage {

        public static <T> T retrieve(Enum<?> key, Class<T> clazz) {
            SuperQuest quest = QuestHolder.get();
            return quest.getStorage().sub().get(key, clazz);
        }


        public static <T> T retrieve(DataExtractor<T> extractor, Class<T> clazz) {
            SuperQuest quest = QuestHolder.get();
            return quest.getStorage().sub().get(extractor, clazz);
        }

    }

}
