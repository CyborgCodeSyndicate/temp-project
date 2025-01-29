package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.annotation.Odyssey;
import com.theairebellion.zeus.framework.config.TestConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

@Odyssey
@SpringBootTest(
    classes = {TestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class BaseTest {

    static {
        synchronized (BaseTest.class){
            addSystemProperties();
        }
    }


    protected <T> T retrieve(Enum<?> key, Class<T> clazz) {
        @Jailbreak Quest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", key.name(), clazz.getName());
        return quest.getStorage().get(key, clazz);
    }


    protected <T> T retrieve(Enum<?> subKey, Enum<?> key, Class<T> clazz) {
        @Jailbreak Quest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", key.name(), clazz.getName());
        return quest.getStorage().sub(subKey).get(key, clazz);
    }


    protected <T> T retrieve(DataExtractor<T> extractor, Class<T> clazz) {
        @Jailbreak Quest quest = QuestHolder.get();
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", extractor.getKey().name(),
            clazz.getName());
        return quest.getStorage().get(extractor, clazz);
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
            @Jailbreak Quest quest = QuestHolder.get();
            return quest.getStorage().sub().get(key, clazz);
        }


        public static <T> T retrieve(DataExtractor<T> extractor, Class<T> clazz) {
            @Jailbreak Quest quest = QuestHolder.get();
            return quest.getStorage().sub().get(extractor, clazz);
        }

    }


}
