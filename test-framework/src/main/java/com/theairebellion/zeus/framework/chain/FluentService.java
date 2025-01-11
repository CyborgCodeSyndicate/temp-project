package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Allure;
import manifold.ext.rt.api.Jailbreak;
import org.assertj.core.api.Assertions;

import java.util.List;

public class FluentService implements FluentChain {

    protected @Jailbreak Quest quest;


    @Override
    public Quest then() {
        LogTest.info("The quest has leaves the journey.");
        return quest;
    }


    protected void setQuest(final Quest quest) {
        this.quest = quest;
    }


    protected void validation(List<AssertionResult<Object>> assertionResults) {
        assertionResults.forEach(assertionResult -> {
            String message = assertionResult.toString();
            LogTest.validation(message);
            Allure.step(message);
            if (assertionResult.isSoft()) {
                quest.getSoftAssertions().assertThat(assertionResult.isPassed()).as(message)
                    .isTrue();
            } else {
                Assertions.assertThat(assertionResult.isPassed()).as(message).isTrue();
            }
        });
    }

    protected void postQuestSetupInitialization(){

    }

}
