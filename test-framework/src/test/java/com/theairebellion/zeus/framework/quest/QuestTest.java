package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import com.theairebellion.zeus.framework.quest.mock.TestableQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestTest {

    @Test
    void testEntersSuccess() {
        TestableQuest quest = new TestableQuest();
        MockFluentService mockFluentService = new MockFluentService();
        quest.exposeRegisterWorld(MockFluentService.class, mockFluentService);
        try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
            MockFluentService result = quest.enters(MockFluentService.class);
            assertSame(mockFluentService, result);
            logMock.verify(() -> LogTest.info("The quest has undertaken a journey through: 'MockWorld'"));
        }
    }

    @Test
    void testEntersNoWorld() {
        TestableQuest quest = new TestableQuest();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> quest.enters(MockFluentService.class));
        assertEquals("World not initialized: " + MockFluentService.class.getName(), exception.getMessage());
    }

    @Test
    void testComplete() throws Exception {
        TestableQuest quest = new TestableQuest();
        CustomSoftAssertion softSpy = Mockito.spy(new CustomSoftAssertion());
        Field softField = Quest.class.getDeclaredField("softAssertions");
        softField.setAccessible(true);
        softField.set(quest, softSpy);
        try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class);
             MockedStatic<QuestHolder> holderMock = mockStatic(QuestHolder.class)) {
            quest.complete();
            logMock.verify(() -> LogTest.info("The quest has reached his end"));
            holderMock.verify(QuestHolder::clear);
            verify(softSpy, times(1)).assertAll();
        }
    }

    @Test
    void testArtifactSuccess() {
        TestableQuest quest = new TestableQuest();
        MockFluentService mockFluentService = new MockFluentService();
        quest.exposeRegisterWorld(MockFluentService.class, mockFluentService);
        String value = quest.exposeArtifact(MockFluentService.class, String.class);
        assertEquals("mockValue", value);
    }

    @Test
    void testArtifactNullParameters() {
        TestableQuest quest = new TestableQuest();
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> quest.exposeArtifact(null, String.class));
        assertEquals("Parameters worldType and artifactType must not be null.", exception1.getMessage());
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> quest.exposeArtifact(MockFluentService.class, null));
        assertEquals("Parameters worldType and artifactType must not be null.", exception2.getMessage());
    }

    @Test
    void testCastSuccess() {
        TestableQuest quest = new TestableQuest();
        MockFluentService mockFluentService = new MockFluentService();
        quest.exposeRegisterWorld(MockFluentService.class, mockFluentService);
        MockFluentService result = quest.exposeCast(MockFluentService.class);
        assertSame(mockFluentService, result);
    }

    @Test
    void testRemoveWorld() {
        TestableQuest quest = new TestableQuest();
        MockFluentService mockFluentService = new MockFluentService();
        quest.exposeRegisterWorld(MockFluentService.class, mockFluentService);
        quest.exposeRemoveWorld(MockFluentService.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> quest.enters(MockFluentService.class));
        assertEquals("World not initialized: " + MockFluentService.class.getName(), exception.getMessage());
    }

    @Test
    void testGetStorageAndSoftAssertions() {
        TestableQuest quest = new TestableQuest();
        Storage storage = quest.getStorage();
        assertNotNull(storage);
        CustomSoftAssertion softAssertions = quest.getSoftAssertions();
        assertNotNull(softAssertions);
    }
}