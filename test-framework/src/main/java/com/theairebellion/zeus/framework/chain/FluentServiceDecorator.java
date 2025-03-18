package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.annotation.AIDisableUsage;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.AssertionResult;
import lombok.experimental.Delegate;

import java.util.List;

@AIDisableUsage
public class FluentServiceDecorator extends FluentService {

    @Delegate
    private final FluentService fluentService;

    public FluentServiceDecorator(FluentService fluentService) {
        this.fluentService = fluentService;
    }

    @Override
    public void setQuest(SuperQuest quest) {
        fluentService.setQuest(quest);
    }

    @Override
    public void validation(List<AssertionResult<Object>> assertionResults) {
        fluentService.validation(assertionResults);
    }

    @Override
    public void postQuestSetupInitialization() {
        fluentService.postQuestSetupInitialization();
    }
}
