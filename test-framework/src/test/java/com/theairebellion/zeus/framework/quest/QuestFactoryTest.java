package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.chain.FluentServiceDecorator;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestFactoryTest {

    @Mock
    private DecoratorsFactory decoratorsFactory;
    private final MockFluentService mockFluentService = new MockFluentService();

    @AfterEach
    void tearDown() {
        QuestHolder.clear();
    }

    @Test
    @DisplayName("Should create Quest with registered services when fluent services provided")
    void createQuest_registersServices() throws Exception {
        // Given
        List<FluentService> providers = List.of(mockFluentService);
        QuestFactory questFactory = createFactory(providers);

        FluentServiceDecorator mockDecorator = mock(FluentServiceDecorator.class);
        when(decoratorsFactory.decorate(mockFluentService, FluentServiceDecorator.class))
                .thenReturn(mockDecorator);
        when(decoratorsFactory.decorate(any(Quest.class), eq(SuperQuest.class)))
                .thenAnswer(inv -> new SuperQuest(inv.getArgument(0)));

        // When
        try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
            Quest quest = questFactory.createQuest();

            // Then
            assertNotNull(quest);
            SuperQuest superQuest = QuestHolder.get();
            assertSame(quest, superQuest.getOriginal());

            MockFluentService result = quest.enters(MockFluentService.class);
            assertSame(mockFluentService, result);

            verify(mockDecorator).setQuest(any(SuperQuest.class));
            verify(mockDecorator).postQuestSetupInitialization();
            logMock.verify(() -> LogTest.extended(anyString(), any()), times(1));
        }
    }

    @Test
    @DisplayName("Should handle empty services collection without errors")
    void createQuest_withEmptyServices() {
        // Given
        QuestFactory questFactory = createFactory(List.of());

        // When
        Quest quest = questFactory.createQuest();

        // Then
        assertNotNull(quest);
        assertNotNull(QuestHolder.get());
    }

    @Test
    @DisplayName("Should register multiple services when provided")
    void createQuest_withMultipleServices() {
        // Given
        MockFluentService service2 = new MockFluentService();
        List<FluentService> providers = List.of(mockFluentService, service2);
        QuestFactory questFactory = createFactory(providers);

        // Configure mocks for both services
        FluentServiceDecorator decorator1 = mock(FluentServiceDecorator.class);
        FluentServiceDecorator decorator2 = mock(FluentServiceDecorator.class);

        when(decoratorsFactory.decorate(mockFluentService, FluentServiceDecorator.class))
                .thenReturn(decorator1);
        when(decoratorsFactory.decorate(service2, FluentServiceDecorator.class))
                .thenReturn(decorator2);
        when(decoratorsFactory.decorate(any(Quest.class), eq(SuperQuest.class)))
                .thenAnswer(inv -> new SuperQuest(inv.getArgument(0)));

        // When
        try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
            Quest quest = questFactory.createQuest();

            // Then - Verify services are accessible (without instance equality check)
            assertNotNull(quest.enters(MockFluentService.class));
            assertNotNull(quest.enters(MockFluentService.class));

            // Verify decorator interactions
            verify(decorator1).setQuest(any(SuperQuest.class));
            verify(decorator1).postQuestSetupInitialization();
            verify(decorator2).setQuest(any(SuperQuest.class));
            verify(decorator2).postQuestSetupInitialization();

            logMock.verify(() -> LogTest.extended(anyString(), any()), times(2));
        }
    }

    private QuestFactory createFactory(List<FluentService> services) {
        QuestFactory factory = new QuestFactory(services);
        factory.setDecoratorsFactory(decoratorsFactory);
        return factory;
    }
}