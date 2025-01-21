package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;
import org.assertj.core.api.SoftAssertions;

import java.util.function.Consumer;

public interface FluentChain {

    Quest then();

    default FluentChain validate(Consumer<SoftAssertions> assertion) {
        @Jailbreak Quest quest = then();
        LogTest.validation("Starting soft validation.");
        assertion.accept(quest.getSoftAssertions());
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

}
