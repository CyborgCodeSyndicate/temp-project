package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.util.reflections.RetryUtils;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Allure;
import java.time.Duration;
import java.util.List;
import org.assertj.core.api.Assertions;

/**
 * Provides a base implementation for fluent service interactions.
 *
 * <p>This class serves as a foundation for fluent service execution,
 * offering retry mechanisms, validation handling, and structured
 * test execution flow.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class FluentService implements FluentChain {

   /**
    * The {@code SuperQuest} instance managing the test execution context.
    */
   protected SuperQuest quest;

   /**
    * Retrieves the original quest instance for further execution.
    *
    * @return The original {@code Quest} instance.
    */
   @Override
   public Quest then() {
      LogTest.info("The quest has left the journey.");
      return quest.getOriginal();
   }

   /**
    * Executes a retry mechanism until a specified condition is met.
    *
    * @param retryCondition The retry condition to be checked.
    * @param maxWait        The maximum duration to wait before giving up.
    * @param retryInterval  The interval between retries.
    * @param service        The service instance used in the retry condition.
    * @param <T>            The type used in the retry condition function.
    * @return The current {@code FluentService} instance for method chaining.
    */
   protected <T> FluentService retryUntil(RetryCondition<T> retryCondition, Duration maxWait,
                                          Duration retryInterval, Object service) {
      RetryUtils.retryUntil(maxWait, retryInterval, () -> retryCondition.function().apply(service),
            retryCondition.condition());
      return this;
   }

   /**
    * Assigns a {@code SuperQuest} instance to this service.
    *
    * @param quest The {@code SuperQuest} instance to be assigned.
    */
   protected void setQuest(final SuperQuest quest) {
      this.quest = quest;
   }

   /**
    * Performs validation on a list of assertion results.
    *
    * <p>This method logs, reports, and evaluates assertions, supporting both
    * soft and hard validations.
    *
    * @param assertionResults The list of assertion results to be validated.
    */
   protected void validation(List<AssertionResult<Object>> assertionResults) {
      assertionResults.forEach(assertionResult -> {
         String message = assertionResult.toString();
         LogTest.validation(message);
         Allure.step(message);

         boolean isPassed = assertionResult.isPassed();
         if (assertionResult.isSoft()) {
            quest.getSoftAssertions()
                  .assertThat(isPassed)
                  .as(message)
                  .isTrue();
         } else {
            Assertions.assertThat(isPassed)
                  .as(message)
                  .isTrue();
         }
      });
   }

   /**
    * Hook method for initializing any required setup after a quest is configured.
    *
    * <p>This method can be overridden by subclasses to provide custom setup logic.
    */
   protected void postQuestSetupInitialization() {

   }

}
