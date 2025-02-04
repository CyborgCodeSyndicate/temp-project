package com.theairebellion.zeus.framework.annotation.mock;

import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.PreQuest;

public class PreQuestTestDummy {
    @PreQuest({@Journey(value = "prequest", order = 5)})
    public void dummyPreQuestMethod() {
    }
}
