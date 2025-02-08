package com.theairebellion.zeus.framework.assertion;

import org.assertj.core.api.SoftAssertions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CustomSoftAssertion extends SoftAssertions {

    private final Map<Class<?>, Object> requiredObjectsForPostErrorHandling = new ConcurrentHashMap<>();
    private static final Map<Class<?>, BiConsumer<AssertionError, Object>> postErrorHandlers = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Predicate<StackTraceElement[]>> errorCheckers = new ConcurrentHashMap<>();


    public <T> void registerObjectForPostErrorHandling(Class<T> clazz, T object) {
        requiredObjectsForPostErrorHandling.put(clazz, object);
    }


    public <T> Optional<T> getObjectFromType(Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(requiredObjectsForPostErrorHandling.get(clazz)));
    }


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


    public static <T> void registerCustomAssertion(Class<T> clazz,
                                                   BiConsumer<AssertionError, T> errorHandler,
                                                   Predicate<StackTraceElement[]> errorChecker) {
        postErrorHandlers.put(clazz, (BiConsumer<AssertionError, Object>) errorHandler);
        errorCheckers.put(clazz, errorChecker);
    }

}
