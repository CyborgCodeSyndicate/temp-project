package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.chain.FluentServiceDecorator;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import java.util.Collection;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating and managing test execution instances.
 *
 * <p>This class provides a mechanism to create and configure new {@code Quest} instances,
 * registering the necessary test services and setting up the execution environment.
 * It ensures that each test scenario has a properly initialized execution context.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
@Component
@Lazy
@Scope("prototype")
public class QuestFactory {

   /**
    * Factory responsible for creating and applying decorators to objects.
    */
   @Autowired
   private DecoratorsFactory decoratorsFactory;

   /**
    * Collection of available {@code FluentService} instances to be registered in quests.
    */
   private final Collection<FluentService> fluentServices;

   /**
    * Constructs a new {@code QuestFactory} instance and initializes available fluent services.
    *
    * @param fluentServices The collection of fluent services to be used in quests.
    */
   @Autowired
   public QuestFactory(Collection<FluentService> fluentServices) {
      this.fluentServices = fluentServices;
   }

   /**
    * Creates a new {@code Quest} instance and sets up the test execution context.
    *
    * <p>A new {@code Quest} is instantiated, registered with the required services, and assigned
    * to the {@code QuestHolder} for retrieval in the test lifecycle.
    *
    * @return The newly created {@code Quest} instance.
    */
   public Quest createQuest() {
      Quest quest = new Quest();
      registerServices(quest);
      QuestHolder.set(new SuperQuest(quest));
      return quest;
   }

   /**
    * Registers the available test services into the provided {@code Quest} instance.
    *
    * <p>Each service is decorated, linked to the quest execution context, and initialized
    * before being registered into the quest's service mapping.
    *
    * @param quest The quest instance where services will be registered.
    */
   private void registerServices(final Quest quest) {
      for (FluentService provider : fluentServices) {
         FluentServiceDecorator fluentServiceDecorator =
               decoratorsFactory.decorate(provider, FluentServiceDecorator.class);
         fluentServiceDecorator.setQuest(decoratorsFactory.decorate(quest, SuperQuest.class));
         fluentServiceDecorator.postQuestSetupInitialization();
         quest.registerWorld(provider.getClass(), provider);
         LogTest.extended("Service: '{}' has been registered for the quest",
               fluentServiceDecorator.getClass().getName());
      }
   }

}
