package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.chain.FluentServiceDecorator;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Getter
@Component
@Lazy
@Scope("prototype")
public class QuestFactory {

    @Autowired
    private DecoratorsFactory decoratorsFactory;

    private final Collection<FluentService> fluentServices;


    @Autowired
    public QuestFactory(Collection<FluentService> fluentServices) {
        this.fluentServices = fluentServices;
    }


    public Quest createQuest() {
        Quest quest = new Quest();
        registerServices(quest);
        QuestHolder.set(new SuperQuest(quest));
        return quest;
    }


    private void registerServices(final Quest quest) {
        for (FluentService provider : fluentServices) {
            FluentServiceDecorator fluentServiceDecorator = decoratorsFactory.decorate(provider, FluentServiceDecorator.class);
            fluentServiceDecorator.setQuest(decoratorsFactory.decorate(quest, SuperQuest.class));
            fluentServiceDecorator.postQuestSetupInitialization();
            quest.registerWorld(provider.getClass(), provider);
            LogTest.extended("Service: '{}' has been registered for the quest", fluentServiceDecorator.getClass().getName());
        }
    }

}
