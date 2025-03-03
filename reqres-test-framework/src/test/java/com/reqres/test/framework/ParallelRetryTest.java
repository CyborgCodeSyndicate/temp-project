package com.reqres.test.framework;

import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.framework.retry.RetryConditionImpl;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class ParallelRetryTest extends BaseTest {

    private static final AtomicBoolean conditionMet = new AtomicBoolean(false);

    @Test
    public void testUpdateCondition() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        conditionMet.set(true);
        System.out.println("Shared flag set to true.");
    }

    @Test
    public void testWaitForCondition(Quest quest) {
        quest.enters(OLYMPYS)
                .retryUntil(
                        sharedFlagIsTrue(),
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(1)
                )
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
    }

    private RetryCondition<Boolean> sharedFlagIsTrue() {
        return new RetryConditionImpl<>(
                service -> ParallelRetryTest.conditionMet.get(),
                result -> result
        );
    }

}

