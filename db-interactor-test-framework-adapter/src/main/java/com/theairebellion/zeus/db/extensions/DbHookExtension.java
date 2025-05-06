package com.theairebellion.zeus.db.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.annotations.DbHook;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.hooks.DbHookFlow;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.exceptions.HookExecutionException;
import com.theairebellion.zeus.framework.hooks.HookExecution;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;
import static com.theairebellion.zeus.framework.util.PropertiesUtil.addSystemProperties;

/**
 * JUnit 5 extension that processes {@link DbHook} annotations on the test class.
 * <p>
 * Executes database-related hooks before and after all tests, ordered by the
 * {@code order} attribute on the {@code @DbHook} annotation, and stores the hook
 * parameters in the global ExtensionContext store under {@link StoreKeys#HOOKS_PARAMS}.
 *
 * @author Cyborg Code Syndicate
 */
public class DbHookExtension implements BeforeAllCallback, AfterAllCallback {

   static {
      synchronized (DbHookExtension.class) {
         addSystemProperties();
      }
   }

   /**
    * Cached instance of {@link DatabaseService} used to execute hooks.
    */
   private DatabaseService databaseService;

   /**
    * Executes all {@link DbHook} annotations on the test class with {@link HookExecution#BEFORE}.
    * Hooks are sorted by {@code order}, executed, and their parameters stored.
    *
    * @param context the JUnit extension context for the test class
    * @throws Exception if any hook execution fails
    */
   @Override
   public void beforeAll(final ExtensionContext context) throws Exception {
      Map<Object, Object> hooksStorage = new HashMap<>();
      DbHook[] dbHooks = context.getRequiredTestClass().getAnnotationsByType(DbHook.class);
      Arrays.stream(dbHooks)
            .filter(dbHook -> dbHook.when() == HookExecution.BEFORE)
            .sorted(Comparator.comparingInt(DbHook::order))
            .forEach(dbHook -> executeHook(dbHook, hooksStorage));
      context.getStore(ExtensionContext.Namespace.GLOBAL).put(StoreKeys.HOOKS_PARAMS, hooksStorage);
   }

   /**
    * Executes all {@link DbHook} annotations on the test class with {@link HookExecution#AFTER}.
    * Hooks are sorted by {@code order}, executed, and their parameters stored.
    *
    * @param context the JUnit extension context for the test class
    * @throws Exception if any hook execution fails
    */
   @Override
   public void afterAll(final ExtensionContext context) throws Exception {
      Map<Object, Object> hooksStorage = new HashMap<>();
      DbHook[] dbHooks = context.getRequiredTestClass().getAnnotationsByType(DbHook.class);
      Arrays.stream(dbHooks)
            .filter(dbHook -> dbHook.when() == HookExecution.AFTER)
            .sorted(Comparator.comparingInt(DbHook::order))
            .forEach(dbHook -> executeHook(dbHook, hooksStorage));
      context.getStore(ExtensionContext.Namespace.GLOBAL).put(StoreKeys.HOOKS_PARAMS, hooksStorage);
   }

   /**
    * Resolves and executes a single {@link DbHook} by locating the corresponding {@link DbHookFlow}
    * implementation and invoking its flow.
    *
    * @param dbHook       the annotation instance containing hook metadata
    * @param storageHooks the map used to accumulate hook results and arguments
    */
   private void executeHook(DbHook dbHook, Map<Object, Object> storageHooks) {
      try {
         DbHookFlow<?> hookFlow = ReflectionUtil.findEnumImplementationsOfInterface(
               DbHookFlow.class, dbHook.type(), getDbConfig().projectPackage());
         hookFlow.flow().accept(dbService(), storageHooks, dbHook.arguments());
      } catch (Exception e) {
         throw new HookExecutionException("Error executing DbHook: " + dbHook.type(), e);
      }
   }

   /**
    * Lazily initializes and returns the singleton {@link DatabaseService}.
    * <p>
    * Configures JSON path extraction, a database connector manager, and an Allure result validator.
    *
    * @return the shared {@code DatabaseService} instance
    */
   private DatabaseService dbService() {
      if (databaseService == null) {
         JsonPathExtractor jsonPathExtractor = new JsonPathExtractor(new ObjectMapper());
         databaseService =
               new DatabaseService(jsonPathExtractor,
                     new DbClientManager(new BaseDbConnectorService()),
                     new QueryResponseValidatorAllureImpl(jsonPathExtractor));
      }
      return databaseService;
   }

}
