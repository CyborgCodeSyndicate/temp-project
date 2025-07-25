package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;

/**
 * Defines a fluent execution chain for service interactions.
 *
 * <p>This interface provides a structured way to perform sequential operations
 * in a fluent manner, allowing validation and completion of tasks within a
 * chained execution flow.
 *
 * <p>Implementing classes must provide the {@code then()} method to return a {@code Quest},
 * enabling further execution and validation within the chain.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface FluentChain {

   /**
    * Retrieves the active {@code Quest} instance for further execution.
    *
    * @return The current {@code Quest} instance.
    */
   Quest then();

   /**
    * Performs a soft validation within the execution chain.
    *
    * <p>This validation does not immediately fail the test but collects assertion results
    * for later evaluation.
    *
    * @param assertion The assertion logic to apply.
    * @return The current {@code FluentChain} instance for method chaining.
    */
   default FluentChain validate(Consumer<SoftAssertions> assertion) {
      Quest quest = then();
      LogTest.validation("Starting soft validation.");
      assertion.accept(createSuperQuest(quest).getSoftAssertions());
      return this;
   }

   /**
    * Performs a hard validation within the execution chain.
    *
    * <p>This validation immediately fails if an assertion does not pass.
    *
    * @param assertion The assertion logic to execute.
    * @return The current {@code FluentChain} instance for method chaining.
    */
   default FluentChain validate(Runnable assertion) {
      LogTest.validation("Starting hard validation...");
      try {
         assertion.run();
         LogTest.validation("Hard validation completed successfully.");
      } catch (AssertionError | Exception e) {
         LogTest.validation("Hard validation failed: " + e.getMessage());
         throw e;
      }
      return this;
   }

   /**
    * Marks the execution chain as complete.
    *
    * <p>This signals that the sequence of operations has been finalized.
    */
   default void complete() {
      then().complete();
   }

   /**
    * Wraps the given {@link Quest} in a {@link SuperQuest}, enabling access to
    * extended functionality such as soft assertions.
    *
    * @param quest the base {@code Quest} instance to wrap
    * @return a new {@code SuperQuest} that decorates the provided quest
    */
   default SuperQuest createSuperQuest(Quest quest) {
      return new SuperQuest(quest);
   }

}
