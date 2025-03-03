package com.theairebellion.zeus.framework.decorators;

import org.springframework.stereotype.Component;

import java.util.IdentityHashMap;
import java.util.Map;

@Component
public class DecoratorsFactory {
    private final Map<Object, Object> cache = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public <T, K> K decorate(T target, Class<K> decoratorClass) {
        if (target == null) {
            return null;
        }

        if (cache.containsKey(target) && cache.get(target).getClass().equals(decoratorClass)) {
            return (K) cache.get(target);
        }

        K decorator;
        try {
            decorator = decoratorClass
                    .getDeclaredConstructor(target.getClass())
                    .newInstance(target);
        } catch (NoSuchMethodException e) {
            try {
                decorator = decoratorClass
                        .getDeclaredConstructor(target.getClass().getSuperclass())
                        .newInstance(target);
            } catch (ReflectiveOperationException e2) {
                throw new IllegalStateException(
                        "Failed to create decorator. "
                                + "No matching constructor found for " + decoratorClass.getName()
                                + " with " + target.getClass().getName()
                                + " or its superclass: " + target.getClass().getSuperclass().getName(),
                        e2
                );
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Failed to create decorator " + decoratorClass.getName()
                            + " for object " + target.getClass().getName(),
                    e
            );
        }

        cache.put(target, decorator);
        return decorator;
    }
}