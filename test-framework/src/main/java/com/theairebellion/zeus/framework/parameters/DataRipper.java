package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import java.util.function.Consumer;

/**
 * Defines a contract for cleaning up test data after execution.
 *
 * <p>Implementations of this interface specify cleanup logic for removing
 * test-generated data, ensuring that test environments remain in a consistent state.
 *
 * <p>The cleanup operation is executed via a {@link Consumer} that accepts a {@link SuperQuest} instance.
 * Each implementation also provides an enumeration reference for structured identification.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DataRipper {

   /**
    * Provides the cleanup operation for test-generated data.
    *
    * <p>Implementations return a {@link Consumer} that defines the cleanup logic,
    * allowing for structured and reusable test data removal.
    *
    * @return A {@link Consumer} responsible for executing the cleanup process.
    */
   Consumer<SuperQuest> eliminate();

   /**
    * Returns the enumeration instance associated with the cleanup operation.
    *
    * <p>This ensures that cleanup actions can be identified and referenced
    * in a structured manner within the framework.
    *
    * @return An {@link Enum} instance representing the cleanup operation.
    */
   Enum<?> enumImpl();

}
