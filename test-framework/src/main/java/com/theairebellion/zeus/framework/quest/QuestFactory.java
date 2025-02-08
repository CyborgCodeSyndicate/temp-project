package com.theairebellion.zeus.framework.quest;

import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.log.LogTest;
import lombok.Getter;
import manifold.ext.rt.api.Jailbreak;
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

    private final Collection<FluentService> fluentServices;


    @Autowired
    public QuestFactory(Collection<FluentService> fluentServices) {
        this.fluentServices = fluentServices;
    }


    public Quest createQuest() {
        Quest quest = new Quest();
        registerServices(quest);
        QuestHolder.set(quest);
        return quest;
    }


    private void registerServices(final @Jailbreak Quest quest) {
        for (FluentService provider : fluentServices) {
            @Jailbreak FluentService fluentService = provider;
            fluentService.setQuest(quest);
            fluentService.postQuestSetupInitialization();
            quest.registerWorld(fluentService.getClass(), fluentService);
            LogTest.extended("Service: '{}' has been registered for the quest", fluentService.getClass().getName());
        }
    }

}
