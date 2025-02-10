package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.storage.Storage;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public class SuperQuest extends Quest {

    @Delegate
    private final Quest original;

    public SuperQuest(Quest quest) {
        this.original = quest;
    }

    @Override
    public <T extends FluentService, K> K artifact(Class<T> worldType, Class<K> artifactType) {
        return original.artifact(worldType, artifactType);
    }

    @Override
    public void registerWorld(Class<? extends FluentService> worldType, FluentService world) {
        original.registerWorld(worldType, world);
    }

    @Override
    public <T extends FluentService> T cast(Class<T> worldType) {
        return super.cast(worldType);
    }

    @Override
    public void removeWorld(Class<? extends FluentService> worldType) {
        original.removeWorld(worldType);
    }

    @Override
    public Storage getStorage() {
        return original.getStorage();
    }

    @Override
    public CustomSoftAssertion getSoftAssertions() {
        return original.getSoftAssertions();
    }
}
