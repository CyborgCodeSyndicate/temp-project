package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FluentServiceTest {

    FluentService service;

    @Mock
    SuperQuest superQuest;

    @Mock
    Quest originalQuest;

    @Mock
    RetryCondition<Object> retryCondition;

    @Mock
    Function<Object, Object> function;

    @Mock
    Predicate<Object> predicate;

    @BeforeEach
    void setUp() {
        service = new FluentService();
    }

    @Test
    void testThen() {
        when(superQuest.getOriginal()).thenReturn(originalQuest);
        service.setQuest(superQuest);
        Quest result = service.then();
        verify(superQuest).getOriginal();
        assertSame(originalQuest, result);
    }

    @Test
    void testRetryUntil() {
        when(retryCondition.function()).thenReturn(function);
        when(function.apply(any())).thenReturn(true);
        when(retryCondition.condition()).thenReturn(predicate);
        when(predicate.test(true)).thenReturn(true);
        FluentService result = service.retryUntil(
                retryCondition,
                Duration.ofMillis(10),
                Duration.ofMillis(5),
                new Object()
        );
        assertNotNull(result);
    }

    @Test
    void testSetQuest() {
        service.setQuest(superQuest);
        assertSame(superQuest, service.quest);
    }

    @Test
    void testValidationWithSoftAndHardAssertions() {
        service.setQuest(superQuest);
        when(superQuest.getSoftAssertions()).thenReturn(new CustomSoftAssertion());
        AssertionResult<Object> softPass = new AssertionResult<>(true, "SoftPass", "x", "x", true);
        AssertionResult<Object> hardPass = new AssertionResult<>(true, "HardPass", "y", "y", false);
        service.validation(List.of(softPass, hardPass));
    }

    @Test
    void testPostQuestSetupInitialization() {
        service.postQuestSetupInitialization();
    }
}