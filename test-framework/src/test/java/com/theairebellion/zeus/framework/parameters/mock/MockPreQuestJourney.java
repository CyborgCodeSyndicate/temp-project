package com.theairebellion.zeus.framework.parameters.mock;

import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import java.util.function.BiConsumer;

public class MockPreQuestJourney implements PreQuestJourney {

   private final BiConsumer<SuperQuest, Object[]> consumer;

   public MockPreQuestJourney(BiConsumer<SuperQuest, Object[]> consumer) {
      this.consumer = consumer;
   }

   @Override
   public BiConsumer<SuperQuest, Object[]> journey() {
      return consumer;
   }

   @Override
   public Enum<?> enumImpl() {
      return MockEnum.INSTANCE;
   }
}
