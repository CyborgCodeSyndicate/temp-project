package com.theairebellion.zeus.framework.quest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class QuestHolderTest {

    @AfterEach
    void cleanup() {
        QuestHolder.clear();
    }

    @Test
    @DisplayName("Should set and retrieve SuperQuest from ThreadLocal")
    void testSetAndGet() {
        // Given
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);

        // When
        QuestHolder.set(superQuest);
        SuperQuest retrieved = QuestHolder.get();

        // Then
        assertSame(superQuest, retrieved, "Retrieved SuperQuest should be the same as set");
    }

    @Test
    @DisplayName("Should clear ThreadLocal")
    void testClear() {
        // Given
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        QuestHolder.set(superQuest);

        // When
        QuestHolder.clear();

        // Then
        assertNull(QuestHolder.get(), "SuperQuest should be null after clearing");
    }

    @Test
    @DisplayName("Should maintain ThreadLocal isolation between threads")
    void testThreadLocalIsolation() throws ExecutionException, InterruptedException {
        // Given
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        QuestHolder.set(superQuest);

        // When - Execute in a separate thread
        CompletableFuture<SuperQuest> futureResult = CompletableFuture.supplyAsync(QuestHolder::get);
        SuperQuest threadResult = futureResult.get();

        // Then
        assertNull(threadResult, "SuperQuest should be null in a different thread");
        assertNotNull(QuestHolder.get(), "SuperQuest should be available in the original thread");
    }

    @Test
    @DisplayName("Should return null when getting before setting")
    void testGetBeforeSetting() {
        // Ensure clean state
        QuestHolder.clear();

        // When/Then
        assertNull(QuestHolder.get(), "Should return null when no SuperQuest has been set");
    }
}