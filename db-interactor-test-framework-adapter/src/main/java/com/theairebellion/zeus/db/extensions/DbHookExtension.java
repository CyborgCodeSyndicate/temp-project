package com.theairebellion.zeus.db.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.annotations.DbHook;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.hooks.DbHookFlow;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.hooks.HookExecution;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

public class DbHookExtension implements BeforeAllCallback, AfterAllCallback {

    private DatabaseService databaseService;


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


    private void executeHook(DbHook dbHook, Map<Object, Object> storageHooks) {
        try {
            DbHookFlow hookFlow = ReflectionUtil.findEnumImplementationsOfInterface(
                DbHookFlow.class, dbHook.type(), getDbConfig().projectPackage());
            hookFlow.flow().accept(dbService(), storageHooks, dbHook.arguments());
        } catch (Exception e) {
            throw new RuntimeException("Error executing DbHook: " + dbHook.type(), e);
        }
    }


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
