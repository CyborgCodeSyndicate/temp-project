package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.assertj.core.api.SoftAssertions;

import java.util.function.Consumer;

public interface FluentChain {

    Quest then();

    default FluentChain validate(Consumer<SoftAssertions> assertion) {
        Quest quest = then();
        LogTest.validation("Starting soft validation.");
        assertion.accept(createSuperQuest(quest).getSoftAssertions());
        return this;
    }

    default FluentChain validate(Runnable assertion) {
        LogTest.validation("Starting hard validation.");
        assertion.run();
        LogTest.validation("Hard validation has passed.");
        return this;
    }

    default void complete() {
        then().complete();
    }

    default SuperQuest createSuperQuest(Quest quest) {
        return new SuperQuest(quest);
    }

}
