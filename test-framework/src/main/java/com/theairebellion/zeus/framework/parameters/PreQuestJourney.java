package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import java.util.function.BiConsumer;

/**
 * Defines a contract for pre-test setup operations that need to be executed before a test method runs.
 *
 * <p>Implementations of this interface should define specific preconditions that must be fulfilled
 * before the test begins. These preconditions are executed as part of the {@code @PreQuest} setup
 * and typically involve initializing test data or configuring the test environment dynamically.
 *
 * <p>Each implementation is expected to define a unique execution logic that is invoked through
 * the {@link #journey()} method, which accepts the {@code SuperQuest} instance and additional parameters.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface PreQuestJourney<T extends Enum<T>> {

   /**
    * Defines the precondition execution logic.
    *
    * <p>This method accepts a {@link SuperQuest} instance and an array of objects representing
    * dynamic parameters required for precondition execution. The implementation should
    * process these inputs accordingly and modify the test state as needed.
    *
    * @return A {@link BiConsumer} that takes a {@link SuperQuest} instance and an array of parameters.
    */
   BiConsumer<SuperQuest, Object[]> journey();

   /**
    * Returns the enumeration representation of the implementing precondition.
    *
    * <p>Each implementation should provide an enum representation to facilitate
    * easy lookup and execution of preconditions.
    *
    * @return An {@code Enum} representing the current precondition implementation.
    */
   T enumImpl();

}
