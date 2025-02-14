package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.parameters.mock.MockDataRipper;
import com.theairebellion.zeus.framework.parameters.mock.MockEnum;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DataRipperTest {

    @Test
    void testEliminate() {
        AtomicBoolean executed = new AtomicBoolean(false);
        Consumer<SuperQuest> consumer = quest -> executed.set(true);
        DataRipper dataRipper = new MockDataRipper(consumer);
        SuperQuest superQuest = mock(SuperQuest.class);
        dataRipper.eliminate().accept(superQuest);
        assertTrue(executed.get());
    }

    @Test
    void testEnumImpl() {
        DataRipper dataRipper = new MockDataRipper(q -> {
        });
        assertNotNull(dataRipper.enumImpl());
        assertEquals(MockEnum.RIPPED, dataRipper.enumImpl());
    }
}
