package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.chain.FluentServiceDecorator;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import com.theairebellion.zeus.framework.quest.mock.MockFluentServiceDecorator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static com.theairebellion.zeus.framework.quest.QuestHolder.clear;
import static com.theairebellion.zeus.framework.quest.QuestHolder.get;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class QuestFactoryTest {

    @AfterEach
    void tearDown() {
        clear();
    }

    @Test
    void testCreateQuestRegistersServices() throws Exception {
        DecoratorsFactory decoratorsFactory = Mockito.mock(DecoratorsFactory.class);
        MockFluentService mockFluentService = new MockFluentService();
        Collection<FluentService> providers = List.of(mockFluentService);
        Mockito.when(decoratorsFactory.decorate(mockFluentService, FluentServiceDecorator.class))
                .thenAnswer(invocation -> new MockFluentServiceDecorator(mockFluentService));
        Mockito.when(decoratorsFactory.decorate(any(), Mockito.eq(SuperQuest.class)))
                .thenAnswer(invocation -> new SuperQuest(invocation.getArgument(0)));
        QuestFactory questFactory = new QuestFactory(providers);
        Field factoryField = QuestFactory.class.getDeclaredField("decoratorsFactory");
        factoryField.setAccessible(true);
        factoryField.set(questFactory, decoratorsFactory);
        try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
            Quest quest = questFactory.createQuest();
            SuperQuest superQuest = get();
            assertNotNull(quest);
            assertNotNull(superQuest);
            assertSame(quest, superQuest.getOriginal());
            MockFluentService result = quest.enters(MockFluentService.class);
            assertSame(mockFluentService, result);
            logMock.verify(() -> LogTest.extended(anyString(), any()), times(1));
        }
    }
}