package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.TestStaticData;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.exceptions.StaticTestDataInitializationException;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.STATIC_DATA;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 * JUnit 5 {@code ParameterResolver} extension for injecting a {@code Quest} instance
 * into test methods, handling static test data provisioning via {@code @TestStaticData}.
 *
 * <p>This extension resolves and provides a test execution context ({@code Quest})
 * for parameter injection and initializes static test data before test execution.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Order(Integer.MIN_VALUE)
@ExtendWith(SpringExtension.class)
public class Oracle implements ParameterResolver {

   /**
    * Determines whether the parameter is eligible for resolution by checking if it is of type {@code Quest}.
    *
    * @param parameterContext The context of the parameter to resolve.
    * @param extensionContext The context of the test execution.
    * @return {@code true} if the parameter type is assignable to {@code Quest}, otherwise {@code false}.
    * @throws ParameterResolutionException If an error occurs while resolving the parameter.
    */
   @Override
   public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
         throws ParameterResolutionException {
      var parameterType = parameterContext.getParameter().getType();
      return Quest.class.isAssignableFrom(parameterType);
   }

   /**
    * Resolves and injects a {@code Quest} instance into the test method.
    *
    * <p>This method initializes a new {@code Quest} instance, decorates it with additional
    * test utilities, and loads any static test data if defined by {@code @TestStaticData}.
    *
    * @param parameterContext The context of the parameter to resolve.
    * @param extensionContext The context of the test execution.
    * @return The resolved {@code Quest} instance.
    * @throws ParameterResolutionException If the required test context cannot be instantiated.
    */
   @Override
   public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
         throws ParameterResolutionException {
      ApplicationContext appCtx = SpringExtension.getApplicationContext(extensionContext);
      QuestFactory questFactory = appCtx.getBean(QuestFactory.class);
      DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);

      Quest quest = questFactory.createQuest();
      SuperQuest superQuest = decoratorsFactory.decorate(quest, SuperQuest.class);
      Map<String, Object> staticTestData = getStaticTestData(extensionContext);
      Storage storage = superQuest.getStorage();
      storage.put(STATIC_DATA, staticTestData);
      addHooksDataInTestStorage(storage, extensionContext);
      LogTest.info("The quest: '{}' has begun and is crafted.", extensionContext.getDisplayName());
      ExtensionContext.Store store = extensionContext.getStore(GLOBAL);
      @SuppressWarnings("unchecked")
      List<Consumer<SuperQuest>> consumers = (List<Consumer<SuperQuest>>) store.get(StoreKeys.QUEST_CONSUMERS);
      if (Objects.nonNull(consumers)) {
         consumers.forEach(
               questConsumer -> questConsumer.accept(decoratorsFactory.decorate(quest, SuperQuest.class)));
      }
      store.put(QUEST, quest);
      return quest;
   }

   /**
    * Retrieves and instantiates static test data defined via {@code @TestStaticData}.
    *
    * <p>If a test method is annotated with {@code @TestStaticData}, this method
    * dynamically instantiates the specified data provider and extracts its test data.
    *
    * @param extensionContext The context of the test execution.
    * @return A map containing static test data, or {@code null} if no provider is defined.
    */
   private static Map<String, Object> getStaticTestData(final ExtensionContext extensionContext) {
      Optional<Method> testMethod = extensionContext.getTestMethod();
      if (testMethod.isPresent()) {
         TestStaticData staticDataAnnotation = testMethod.get().getAnnotation(TestStaticData.class);
         if (staticDataAnnotation != null) {
            try {
               return staticDataAnnotation.value().getDeclaredConstructor()
                     .newInstance().testStaticData();

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                     | NoSuchMethodException e) {
               throw new StaticTestDataInitializationException(e);
            }
         }
      }
      return new HashMap<>();
   }

   /**
    * Copies test-level hook parameters from the JUnit {@code ExtensionContext} store
    * into the framework's {@code Storage}.
    *
    * <p>This method is used to propagate test-specific hook data (such as setup/teardown metadata)
    * from the JUnit context into the test execution context, allowing it to be accessed during the test run.
    *
    * @param storage The test storage object where hook data should be inserted.
    * @param context The JUnit extension context containing the stored hook parameters.
    */
   private static void addHooksDataInTestStorage(Storage storage, ExtensionContext context) {
      Map<Object, Object> hooksStorage = context.getStore(GLOBAL).get(StoreKeys.HOOKS_PARAMS, Map.class);
      if (hooksStorage != null) {
         storage.put(StorageKeysTest.HOOKS, hooksStorage);
      }
   }

}
