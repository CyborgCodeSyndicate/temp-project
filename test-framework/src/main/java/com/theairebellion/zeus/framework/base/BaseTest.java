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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

import java.util.List;

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
