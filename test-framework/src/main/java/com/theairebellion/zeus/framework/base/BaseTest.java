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
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@Odyssey
@SpringBootTest(
    classes = {TestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)

@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseTest {

    @Autowired
    @Lazy
    private Services services;


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
        //todo change log
        LogTest.extended("Fetching data from storage by key: '{}' and type: '{}'", extractor.getKey().name(),
            clazz.getName());
        Object result = quest.getStorage().sub(extractor.getSubKey()).get(extractor.getKey(), Object.class);
        return clazz.cast(extractor.extract(result));
    }


    @AfterAll
    protected final void beforeAll() {
        beforeAll(services);
    }


    protected void beforeAll(Services services) {
    }


    @AfterAll
    protected final void afterAll() {
        afterAll(services);
    }


    protected void afterAll(Services services) {
    }


}
