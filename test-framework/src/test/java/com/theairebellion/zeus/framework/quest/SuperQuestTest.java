package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SuperQuestTest {

    @Test
    void testArtifact() {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        MockFluentService mockFluentService = new MockFluentService();
        quest.registerWorld(MockFluentService.class, mockFluentService);
        String value = superQuest.artifact(MockFluentService.class, String.class);
        assertEquals("mockValue", value);
    }

    @Test
    void testRegisterWorldAndCast() {
        Quest quest = new Quest();
        MockFluentService mockService = new MockFluentService();
        quest.registerWorld(MockFluentService.class, mockService);
        SuperQuest superQuest = new SuperQuest(quest);
        Map<?, ?> originalWorlds = (Map<?, ?>) ReflectionTestUtils.getField(quest, "worlds");
        ReflectionTestUtils.setField(superQuest, "worlds", originalWorlds);
        MockFluentService casted = superQuest.cast(MockFluentService.class);
        assertSame(mockService, casted);
    }

    @Test
    void testRemoveWorld() {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        MockFluentService mockFluentService = new MockFluentService();
        superQuest.registerWorld(MockFluentService.class, mockFluentService);
        superQuest.removeWorld(MockFluentService.class);
        assertThrows(IllegalArgumentException.class, () -> superQuest.cast(MockFluentService.class));
    }

    @Test
    void testGetStorage() {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        Storage storageFromQuest = quest.getStorage();
        Storage storageFromSuperQuest = superQuest.getStorage();
        assertSame(storageFromQuest, storageFromSuperQuest);
    }

    @Test
    void testGetSoftAssertions() {
        Quest quest = new Quest();
        SuperQuest superQuest = new SuperQuest(quest);
        CustomSoftAssertion assertionsFromQuest = quest.getSoftAssertions();
        CustomSoftAssertion assertionsFromSuperQuest = superQuest.getSoftAssertions();
        assertSame(assertionsFromQuest, assertionsFromSuperQuest);
    }
}