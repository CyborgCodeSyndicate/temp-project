package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.allure.RestClientAllureImpl;
import com.theairebellion.zeus.api.allure.RestResponseValidatorAllureImpl;
import com.theairebellion.zeus.api.annotations.ApiHook;
import com.theairebellion.zeus.api.hooks.ApiHookFlow;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.exceptions.HookExecutionException;
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

import static com.theairebellion.zeus.api.config.ApiConfigHolder.getApiConfig;
import static com.theairebellion.zeus.framework.util.PropertiesUtil.addSystemProperties;

public class ApiHookExtension implements BeforeAllCallback, AfterAllCallback {

    static {
        synchronized (ApiHookExtension.class) {
            addSystemProperties();
        }
    }

    private RestService restService;


    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        Map<Object, Object> hooksStorage = new HashMap<>();
        ApiHook[] apiHooks = context.getRequiredTestClass().getAnnotationsByType(ApiHook.class);
        Arrays.stream(apiHooks)
            .filter(apiHook -> apiHook.when() == HookExecution.BEFORE)
            .sorted(Comparator.comparingInt(ApiHook::order))
            .forEach(apiHook -> executeHook(apiHook, hooksStorage));
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(StoreKeys.HOOKS_PARAMS, hooksStorage);
    }


    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        Map<Object, Object> hooksStorage = new HashMap<>();
        ApiHook[] apiHooks = context.getRequiredTestClass().getAnnotationsByType(ApiHook.class);
        Arrays.stream(apiHooks)
            .filter(apiHook -> apiHook.when() == HookExecution.AFTER)
            .sorted(Comparator.comparingInt(ApiHook::order))
            .forEach(apiHook -> executeHook(apiHook, hooksStorage));
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(StoreKeys.HOOKS_PARAMS, hooksStorage);
    }


    private void executeHook(ApiHook apiHook, Map<Object, Object> storageHooks) {
        try {
            ApiHookFlow<?> hookFlow = ReflectionUtil.findEnumImplementationsOfInterface(
                ApiHookFlow.class, apiHook.type(), getApiConfig().projectPackage());
            hookFlow.flow().accept(restService(), storageHooks, apiHook.arguments());
        } catch (Exception e) {
            throw new HookExecutionException("Error executing ApiHook: " + apiHook.type(), e);
        }
    }


    private RestService restService() {
        if (restService == null) {
            restService = new RestService(new RestClientAllureImpl(), new RestResponseValidatorAllureImpl());
        }
        return restService;
    }

}
