package com.theairebellion.zeus.framework.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.theairebellion.zeus.util.reflections.ReflectionUtil.getFieldValue;

@Component
@Lazy
public class Services {

    private final ApplicationContext applicationContext;

    private final Map<Class<?>, Object> serviceCache = new HashMap<>();


    @Autowired
    public Services(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public <T extends ClassLevelHook, K> K service(Class<T> fluentServiceClass, Class<K> serviceClass) {

        return serviceClass.cast(serviceCache.computeIfAbsent(serviceClass, key -> {
            ClassLevelHook fluentService = applicationContext.getBeansOfType(ClassLevelHook.class)
                                               .values().stream()
                                               .filter(fluent -> fluent.getClass().equals(fluentServiceClass))
                                               .findFirst()
                                               .orElseThrow(() -> new IllegalStateException(
                                                   "No bean found for the specified fluentServiceClass: " + fluentServiceClass.getName()));

            return getFieldValue(fluentService, serviceClass);
        }));
    }

}
