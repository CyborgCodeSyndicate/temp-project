package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.chain.FluentServiceDecorator;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.mock.MockFluentService;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
   @DisplayName("Should create Quest and register services correctly")
   void testCreateQuestRegistersServices() throws Exception {
      // Given
      List<FluentService> providers = List.of(mockFluentService);

      // Create the QuestFactory with the providers
      QuestFactory questFactory = new QuestFactory(providers);

      // Set the mock decoratorsFactory using reflection
      Field factoryField = QuestFactory.class.getDeclaredField("decoratorsFactory");
      factoryField.setAccessible(true);
      factoryField.set(questFactory, decoratorsFactory);

      when(decoratorsFactory.decorate(mockFluentService, FluentServiceDecorator.class))
            .thenAnswer(invocation -> new FluentServiceDecorator(mockFluentService));

      when(decoratorsFactory.decorate(any(), eq(SuperQuest.class)))
            .thenAnswer(invocation -> new SuperQuest(invocation.getArgument(0)));

      // When
      try (MockedStatic<LogTest> logMock = mockStatic(LogTest.class)) {
         Quest quest = questFactory.createQuest();
         SuperQuest superQuest = QuestHolder.get();

         // Then
         assertNotNull(quest, "Quest should not be null");
         assertNotNull(superQuest, "SuperQuest should not be null");
         assertSame(quest, superQuest.getOriginal(), "Quest should be the original inside SuperQuest");

         MockFluentService result = quest.enters(MockFluentService.class);
         assertSame(mockFluentService, result, "Entered FluentService should match the mock instance");

         logMock.verify(() -> LogTest.extended(anyString(), any()), times(1));
      }
   }
}