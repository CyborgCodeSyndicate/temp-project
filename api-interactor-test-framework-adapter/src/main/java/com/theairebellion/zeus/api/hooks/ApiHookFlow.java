package com.theairebellion.zeus.api.hooks;

import com.theairebellion.zeus.api.service.RestService;
import java.util.Map;
import org.apache.logging.log4j.util.TriConsumer;

/**
 * Represents a single API hook implementation, providing the logic to execute
 * before or after tests run.
 *
 * <p>Each {@code ApiHookFlow} must supply:
 * <ul>
 *   <li>a {@linkplain #flow() flow consumer} that is invoked with the REST service,
 *       a shared parameters map, and any hook arguments, and</li>
 *   <li>an {@linkplain #enumImpl() associated enum constant} identifying this hook.</li>
 * </ul>
 *
 * @param <T> the enum type used to identify hook implementations
 * @author Cyborg Code Syndicate 💍👨💻
 */
@SuppressWarnings("java:S1452")
public interface ApiHookFlow<T extends Enum<T>> {

   /**
    * Returns the executable hook logic.
    *
    * <p>The returned {@link TriConsumer} is invoked for each hook
    * with:
    * <ul>
    *   <li>a {@link RestService} instance for performing API calls,</li>
    *   <li>a mutable {@link Map} into which the hook may write results or parameters, and</li>
    *   <li>an array of {@link String} arguments defined on the {@code @ApiHook} annotation.</li>
    * </ul>
    *
    * @return a three-argument consumer executing the hook flow
    */
   TriConsumer<RestService, Map<Object, Object>, String[]> flow();

   /**
    * Returns the enum constant that identifies this hook implementation.
    *
    * <p>This value is used reflectively to look up the correct
    * {@code ApiHookFlow} based on the {@code type} attribute of {@code @ApiHook}.</p>
    *
    * @return the enum identifying this hook
    */
   T enumImpl();

}
