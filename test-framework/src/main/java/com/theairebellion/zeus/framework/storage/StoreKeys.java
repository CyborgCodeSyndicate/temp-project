package com.theairebellion.zeus.framework.storage;

import lombok.Getter;

@Getter
public enum StoreKeys {

    QUEST("quest"),
    QUEST_CONSUMERS("quest-consumers"),
    PRE_QUEST_JOURNEYS("prequest-journeys"),
    START_TIME("startTime");

    private final String key;


    StoreKeys(final String key) {
        this.key = key;
    }
}
