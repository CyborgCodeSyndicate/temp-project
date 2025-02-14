package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.parameters.mock.MockEnum;
import com.theairebellion.zeus.framework.parameters.mock.MockPreQuestJourney;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreQuestJourneyTest {

    public static final String ARG_1 = "arg1";
    public static final String ARG_2 = "arg2";

    @Test
    void testJourneyExecution() {
        AtomicBoolean executed = new AtomicBoolean(false);
        BiConsumer<SuperQuest, Object[]> consumer = (quest, args) -> executed.set(true);
        PreQuestJourney preQuestJourney = new MockPreQuestJourney(consumer);
        SuperQuest dummyQuest = Mockito.mock(SuperQuest.class);
        preQuestJourney.journey().accept(dummyQuest, new Object[]{ARG_1, ARG_2});
        assertTrue(executed.get());
    }

    @Test
    void testEnumImpl() {
        BiConsumer<SuperQuest, Object[]> consumer = (quest, args) -> {
        };
        PreQuestJourney preQuestJourney = new MockPreQuestJourney(consumer);
        assertEquals(MockEnum.INSTANCE, preQuestJourney.enumImpl());
    }
}