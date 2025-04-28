package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.AssertionResult;
import lombok.experimental.Delegate;

import java.util.List;

/**
 * A decorator for {@code FluentService}, enabling extended functionality.
 * <p>
 * This class acts as a wrapper around an existing {@code FluentService} instance,
 * allowing additional behavior to be introduced without modifying the original implementation.
 * It delegates all method calls to the wrapped {@code FluentService} instance.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public final class FluentServiceDecorator extends FluentService {

    /**
     * The wrapped instance of {@code FluentService}.
     */
    @Delegate
    private final FluentService fluentService;

    /**
     * Constructs a new {@code FluentServiceDecorator} wrapping the specified fluent service.
     *
     * @param fluentService The {@code FluentService} instance to be wrapped.
     */
    public FluentServiceDecorator(FluentService fluentService) {
        this.fluentService = fluentService;
    }

    /**
     * Sets the associated {@code SuperQuest} instance.
     *
     * @param quest The {@code SuperQuest} instance to be assigned.
     */
    @Override
    public void setQuest(SuperQuest quest) {
        fluentService.setQuest(quest);
    }

    /**
     * Performs validation on a list of assertion results by delegating to the wrapped service.
     *
     * @param assertionResults The list of assertion results to be validated.
     */
    @Override
    public void validation(List<AssertionResult<Object>> assertionResults) {
        fluentService.validation(assertionResults);
    }

    /**
     * Executes any post-quest setup initialization logic by delegating to the wrapped service.
     */
    @Override
    public void postQuestSetupInitialization() {
        fluentService.postQuestSetupInitialization();
    }
}
