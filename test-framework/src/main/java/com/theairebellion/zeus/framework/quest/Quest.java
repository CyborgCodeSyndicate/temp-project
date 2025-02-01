package com.theairebellion.zeus.framework.quest;


import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.storage.Storage;
import org.assertj.core.api.SoftAssertions;

import java.util.HashMap;
import java.util.Map;

import static com.theairebellion.zeus.util.reflections.ReflectionUtil.getFieldValue;

public class Quest {

    private final Map<Class<? extends FluentService>, FluentService> worlds = new HashMap<>();
    private Storage storage;
    private final SoftAssertions softAssertions = new SoftAssertions();


    public Quest() {
        this.storage = new Storage();
    }


    @SuppressWarnings("unchecked")
    public <T extends FluentService> T enters(Class<T> worldType) {
        FluentService world = worlds.get(worldType);
        if (world == null) {
            throw new IllegalArgumentException("World not initialized: " + worldType.getName());
        }
        WorldName worldName = worldType.getAnnotation(WorldName.class);
        if (worldName == null) {
            LogTest.info("The quest has undertaken a journey through: '{}'", worldType.getName());

        } else {
            LogTest.info("The quest has undertaken a journey through: '{}'", worldName.value());

        }
        return (T) world;
    }


    public void complete() {
        LogTest.info("The quest has reached his end");
        QuestHolder.clear();
        softAssertions.assertAll();
    }


    private <T extends FluentService, K> K artifact(Class<T> worldType, Class<K> artifactType) {
        if (worldType == null || artifactType == null) {
            throw new IllegalArgumentException("Parameters worldType and artifactType must not be null.");
        }

        T world = enters(worldType);
        if (world == null) {
            throw new IllegalStateException(
                "Could not retrieve an instance of the specified worldType: " + worldType.getName());
        }

        return getFieldValue(world, artifactType);
    }


    private void registerWorld(Class<? extends FluentService> worldType, FluentService world) {
        worlds.put(worldType, world);
    }


    private Storage getStorage() {
        return storage;
    }


    private SoftAssertions getSoftAssertions() {
        return softAssertions;
    }


}

