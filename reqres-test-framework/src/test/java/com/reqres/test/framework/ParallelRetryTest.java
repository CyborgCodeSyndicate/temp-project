package com.reqres.test.framework;

import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.framework.retry.RetryConditionImpl;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_OK;

@API
public class ParallelRetryTest extends BaseTest {

    private static final AtomicBoolean conditionMet = new AtomicBoolean(false);

    @Test
    public void testUpdateCondition(Quest quest) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        conditionMet.set(true);
    }

    @Test
    public void testWaitForCondition(Quest quest) {
        quest.enters(OLYMPYS)
                .retryUntil(
                        sharedFlagIsTrue(),
                        Duration.ofSeconds(20),
                        Duration.ofSeconds(1)
                )
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
                );
    }

    private RetryCondition<Boolean> sharedFlagIsTrue() {
        return new RetryConditionImpl<>(
                service -> ParallelRetryTest.conditionMet.get(),
                result -> result
        );
    }

}
