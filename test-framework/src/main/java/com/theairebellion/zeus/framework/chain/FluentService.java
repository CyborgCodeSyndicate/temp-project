package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.util.reflections.RetryUtils;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import java.time.Duration;
import java.util.List;

public class FluentService implements FluentChain {

    protected SuperQuest quest;


    @Override
    public Quest then() {
        LogTest.info("The quest has left the journey.");
        return quest.getOriginal();
    }


    protected <T> FluentService retryUntil(RetryCondition<T> retryCondition, Duration maxWait,
                                           Duration retryInterval, Object service) {
        RetryUtils.retryUntil(maxWait, retryInterval, () -> retryCondition.function().apply(service),
                retryCondition.condition());
        return this;
    }


    protected void setQuest(final SuperQuest quest) {
        this.quest = quest;
    }


    protected void validation(List<AssertionResult<Object>> assertionResults) {
        assertionResults.forEach(assertionResult -> {
            String message = assertionResult.toString();
            LogTest.validation(message);
            Allure.step(message);

            boolean isPassed = assertionResult.isPassed();
            if (assertionResult.isSoft()) {
                quest.getSoftAssertions()
                        .assertThat(isPassed)
                        .as(message)
                        .isTrue();
            } else {
                Assertions.assertThat(isPassed)
                        .as(message)
                        .isTrue();
            }
        });
    }


    protected void postQuestSetupInitialization() {

    }

}
