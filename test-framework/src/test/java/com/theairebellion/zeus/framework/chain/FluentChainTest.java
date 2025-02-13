package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.quest.Quest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class FluentChainTest {

    @Mock
    Quest quest;

    FluentChain fluentChain;

    @BeforeEach
    void setUp() {
        fluentChain = () -> quest;
    }

    @Test
    void testValidateWithConsumer() {
        fluentChain.validate(softAssertions -> {});
    }

    @Test
    void testValidateWithRunnable() {
        Runnable runnable = mock(Runnable.class);
        fluentChain.validate(runnable);
        verify(runnable).run();
        verifyNoMoreInteractions(quest);
    }

    @Test
    void testComplete() {
        fluentChain.complete();
        verify(quest).complete();
        verifyNoMoreInteractions(quest);
    }
}