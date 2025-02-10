package com.theairebellion.zeus.framework.quest;


import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.storage.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.theairebellion.zeus.util.reflections.ReflectionUtil.getFieldValue;

public class Quest {

    private final Map<Class<? extends FluentService>, FluentService> worlds = new HashMap<>();
    private final Storage storage;
    private final CustomSoftAssertion softAssertions = new CustomSoftAssertion();


    public Quest() {
        this.storage = new Storage();
    }


    @SuppressWarnings("unchecked")
    public <T extends FluentService> T enters(Class<T> worldType) {
        Optional<Class<? extends FluentService>> match =
                worlds.keySet().stream().filter(worldType::isAssignableFrom).findFirst();
        if (match.isEmpty()) {
            throw new IllegalArgumentException("World not initialized: " + worldType.getName());
        }
        TestService worldName = worldType.getAnnotation(TestService.class);
        if (worldName == null) {
            worldName = worldType.getAnnotation(TestService.class);
        }
        String message = worldName == null
                ? "The quest has undertaken a journey through: '" + worldType.getName() + "'"
                : "The quest has undertaken a journey through: '" + worldName.value() + "'";

        LogTest.info(message);
        return (T) worlds.get(match.get());
    }

    @SuppressWarnings("unchecked")
    protected <T extends FluentService> T cast(Class<T> worldType) {
        Optional<Class<? extends FluentService>> match =
                worlds.keySet().stream().filter(worldType::isAssignableFrom).findFirst();
        if (match.isEmpty()) {
            throw new IllegalArgumentException("World not initialized: " + worldType.getName());
        }
        return (T) worlds.get(match.get());
    }


    public void complete() {
        LogTest.info("The quest has reached his end");
        QuestHolder.clear();
        softAssertions.assertAll();
    }


    protected <T extends FluentService, K> K artifact(Class<T> worldType, Class<K> artifactType) {
        if (worldType == null || artifactType == null) {
            throw new IllegalArgumentException("Parameters worldType and artifactType must not be null.");
        }

        T world = cast(worldType);
        if (world == null) {
            throw new IllegalStateException(
                    "Could not retrieve an instance of the specified worldType: " + worldType.getName());
        }

        return getFieldValue(world, artifactType);
    }


    protected void registerWorld(Class<? extends FluentService> worldType, FluentService world) {
        worlds.put(worldType, world);
    }

    protected void removeWorld(Class<? extends FluentService> worldType) {
        worlds.remove(worldType);
    }


    protected Storage getStorage() {
        return storage;
    }


    protected CustomSoftAssertion getSoftAssertions() {
        return softAssertions;
    }


}

