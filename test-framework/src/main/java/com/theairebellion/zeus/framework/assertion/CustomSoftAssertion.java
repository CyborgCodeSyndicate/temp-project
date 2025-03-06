package com.theairebellion.zeus.framework.assertion;

import org.assertj.core.api.SoftAssertions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Custom extension of {@code SoftAssertions} with enhanced post-error handling capabilities.
 * <p>
 * This class provides additional functionality for managing assertion errors by allowing
 * custom error handlers and associated objects to be registered and processed automatically.
 * </p>
 *
 * <p>
 * Registered error handlers can define specific logic for handling assertion failures,
 * including conditional checks based on stack traces.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class CustomSoftAssertion extends SoftAssertions {

    /**
     * Stores objects required for post-error handling, mapped by their class type.
     */
    private final Map<Class<?>, Object> requiredObjectsForPostErrorHandling = new ConcurrentHashMap<>();

    /**
     * Stores custom post-error handlers mapped to their corresponding class types.
     */
    private static final Map<Class<?>, BiConsumer<AssertionError, Object>> postErrorHandlers = new ConcurrentHashMap<>();

    /**
     * Stores predicates to determine whether a post-error handler should be applied based on stack trace analysis.
     */
    private static final Map<Class<?>, Predicate<StackTraceElement[]>> errorCheckers = new ConcurrentHashMap<>();

    /**
     * Registers an object to be used in post-error handling logic.
     *
     * @param clazz  The class type of the object.
     * @param object The object to be registered for error handling.
     * @param <T>    The type of the object being registered.
     */
    public <T> void registerObjectForPostErrorHandling(Class<T> clazz, T object) {
        requiredObjectsForPostErrorHandling.put(clazz, object);
    }

    /**
     * Retrieves an object of the specified type that was registered for post-error handling.
     *
     * @param clazz The class type of the requested object.
     * @param <T>   The expected type of the object.
     * @return An {@code Optional} containing the registered object, if available.
     */
    public <T> Optional<T> getObjectFromType(Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(requiredObjectsForPostErrorHandling.get(clazz)));
    }

    /**
     * Collects an assertion error and processes it using registered custom handlers.
     * <p>
     * If an assertion failure occurs, the registered error handlers are invoked for
     * any applicable objects, based on the stack trace and configured predicates.
     * </p>
     *
     * @param error The assertion error that occurred.
     */
    @Override
    public void collectAssertionError(final AssertionError error) {
        postErrorHandlers.forEach((clazz, handler) -> {
            getObjectFromType(clazz).ifPresent(object -> {
                if (errorCheckers.getOrDefault(clazz, stackTrace -> true).test(error.getStackTrace())) {
                    handler.accept(error, object);
                }
            });
        });
        super.collectAssertionError(error);
    }

    /**
     * Registers a custom error handler for a specific type.
     * <p>
     * The provided handler is executed when an assertion failure occurs, and can
     * include an optional predicate to determine applicability based on the stack trace.
     * </p>
     *
     * @param clazz        The class type associated with the error handler.
     * @param errorHandler A function that handles assertion errors for the specified type.
     * @param errorChecker A predicate to determine whether the handler should be applied based on stack trace analysis.
     * @param <T>          The type associated with the error handler.
     */
    public static <T> void registerCustomAssertion(Class<T> clazz,
                                                   BiConsumer<AssertionError, T> errorHandler,
                                                   Predicate<StackTraceElement[]> errorChecker) {
        postErrorHandlers.put(clazz, (BiConsumer<AssertionError, Object>) errorHandler);
        errorCheckers.put(clazz, errorChecker);
    }

}
