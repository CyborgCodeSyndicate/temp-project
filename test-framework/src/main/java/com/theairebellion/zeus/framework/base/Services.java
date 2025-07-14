package com.theairebellion.zeus.framework.base;

import com.theairebellion.zeus.framework.log.LogTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static com.theairebellion.zeus.util.reflections.ReflectionUtil.getFieldValues;

/**
 * Manages and provides access to fluent service instances.
 *
 * <p>This class is responsible for retrieving and caching service instances,
 * ensuring efficient access to dynamically managed components.
 *
 * <p>Services are lazily initialized and stored in an internal cache to optimize performance.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Component
@Lazy
public class Services {

   /**
    * The application context for retrieving service beans.
    */
   private final ApplicationContext applicationContext;

   /**
    * Cache for storing retrieved service instances.
    */
   private final Map<Class<?>, Object> serviceCache = new HashMap<>();

   /**
    * Constructs a new {@code Services} instance with the provided application context.
    *
    * @param applicationContext The application context used for retrieving service beans.
    */
   @Autowired
   public Services(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
   }

   /**
    * Retrieves a service instance for the specified fluent service class and service type.
    *
    * <p>If the requested service is not already cached, it is retrieved from the application context
    * and stored for future access.
    *
    * @param fluentServiceClass The class representing the fluent service.
    * @param serviceClass       The type of service to retrieve.
    * @param <T>                The type of the fluent service.
    * @param <K>                The type of the requested service.
    * @return The requested service instance.
    * @throws IllegalStateException if no matching service bean is found.
    */
   public <T extends ClassLevelHook, K> K service(Class<T> fluentServiceClass, Class<K> serviceClass) {

      return serviceClass.cast(serviceCache.computeIfAbsent(serviceClass, key -> {
         ClassLevelHook fluentService = applicationContext.getBeansOfType(ClassLevelHook.class)
               .values().stream()
               .filter(fluent -> fluent.getClass().equals(fluentServiceClass))
               .findFirst()
               .orElseThrow(() -> new IllegalStateException(
                     "No bean found for the specified fluentServiceClass: " + fluentServiceClass.getName()));

         List<K> fieldValues = getFieldValues(fluentService, serviceClass);
         if (fieldValues.size() > 1) {
            LogTest.warn(
                  "There is more than one service from type: {} inside class: {}. The first one will be taken: {}",
                  serviceClass, fluentServiceClass, fieldValues.get(0));
         }
         return fieldValues.get(0);
      }));
   }

}
