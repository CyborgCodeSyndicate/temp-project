package com.theairebellion.zeus.db.hooks;

import com.theairebellion.zeus.db.service.DatabaseService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

/**
 * Defines a single database hook implementation to run custom logic
 * before or after all tests in a class
 *
 * <p>Each {@code DbHookFlow} provides:
 * <ul>
 *   <li>a {@linkplain #flow() flow consumer} that accepts the {@link DatabaseService},
 *       a shared parameters map, and the hook arguments, and</li>
 *   <li>an {@linkplain #enumImpl() associated enum constant} identifying this hook.</li>
 * </ul>
 *
 * @param <T> the enum type used to identify hook implementations
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DbHookFlow<T extends Enum<T>> {

   /**
    * Returns the executable hook logic.
    *
    * <p>The returned {@link TriConsumer} is invoked for each hook with:
    * <ul>
    *   <li>a {@link DatabaseService} instance for database operations,</li>
    *   <li>a mutable {@link Map} into which the hook may write outputs, and</li>
    *   <li>an array of {@link String} arguments defined on the {@code @DbHook} annotation.</li>
    * </ul>
    *
    * @return a three-argument consumer executing the hook flow
    */
   TriConsumer<DatabaseService, Map<Object, Object>, String[]> flow();

   /**
    * Returns the enum constant that identifies this hook implementation.
    *
    * <p>This value is matched against the {@code type} attribute of the {@code @DbHook} annotation
    * during reflective lookup to find the correct hook flow.
    *
    * @return the enum identifying this hook
    */
   T enumImpl();

}
