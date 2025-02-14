package com.theairebellion.zeus.framework.quest;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class QuestHolderTest {

    @Test
    void testSetAndGet() {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        QuestHolder.set(superQuest);
        SuperQuest retrieved = QuestHolder.get();
        assertSame(superQuest, retrieved);
    }

    @Test
    void testClear() {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        QuestHolder.set(superQuest);
        QuestHolder.clear();
        assertNull(QuestHolder.get());
    }

    @Test
    void testThreadLocalIsolation() throws InterruptedException {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        QuestHolder.set(superQuest);
        final SuperQuest[] threadResult = new SuperQuest[1];
        Thread thread = new Thread(() -> threadResult[0] = QuestHolder.get());
        thread.start();
        thread.join();
        assertNull(threadResult[0]);
    }
}