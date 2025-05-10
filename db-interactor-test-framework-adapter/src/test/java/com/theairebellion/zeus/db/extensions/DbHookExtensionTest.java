package com.theairebellion.zeus.db.extensions;

import com.theairebellion.zeus.db.annotations.DbHook;
import com.theairebellion.zeus.db.config.DbConfig;
import com.theairebellion.zeus.db.config.DbConfigHolder;
import com.theairebellion.zeus.db.hooks.DbHookFlow;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.mockito.MockedStatic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.theairebellion.zeus.framework.hooks.HookExecution.AFTER;
import static com.theairebellion.zeus.framework.hooks.HookExecution.BEFORE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DbHookExtensionTest {

    private final DbHookExtension extension = new DbHookExtension();
    private final DbHook beforeHook = HooksClass.class.getAnnotationsByType(DbHook.class)[0];

    static class NoHooksClass {
        // No @DbHook annotations
    }

    @DbHook(when = BEFORE,
            type = "beforeType", arguments = {"arg1"}, order = 1)
    @DbHook(when = AFTER,
            type = "afterType", arguments = {"argA", "argB"}, order = 2)
    static class HooksClass {
        // Two hooks, one BEFORE and one AFTER
    }

    @Test
    @DisplayName("beforeAll with no hooks should put empty map into store")
    void beforeAll_noHooks_putsEmptyMap() throws Exception {
        ExtensionContext ctx = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        doReturn(NoHooksClass.class).when(ctx).getRequiredTestClass();
        doReturn(store).when(ctx).getStore(Namespace.GLOBAL);

        extension.beforeAll(ctx);

        verify(store, times(1)).put(eq(StoreKeys.HOOKS_PARAMS), eq(new HashMap<>()));
    }

    @Test
    @DisplayName("afterAll with no hooks should put empty map into store")
    void afterAll_noHooks_putsEmptyMap() throws Exception {
        ExtensionContext ctx = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        doReturn(NoHooksClass.class).when(ctx).getRequiredTestClass();
        doReturn(store).when(ctx).getStore(Namespace.GLOBAL);

        extension.afterAll(ctx);

        verify(store, times(1)).put(eq(StoreKeys.HOOKS_PARAMS), eq(new HashMap<>()));
    }

    @Test
    @DisplayName("beforeAll with hooks should invoke ReflectionUtil for BEFORE hooks")
    void beforeAll_withHooks_invokesReflectionUtil() throws Exception {
        ExtensionContext ctx = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);

        // <<< return the annotated HooksClass, not NoHooksClass
        doReturn(HooksClass.class).when(ctx).getRequiredTestClass();
        doReturn(store).when(ctx).getStore(Namespace.GLOBAL);

        try (MockedStatic<DbConfigHolder> dbh = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> rif = mockStatic(ReflectionUtil.class)) {

            var mockConfig = mock(DbConfig.class);
            when(mockConfig.projectPackage()).thenReturn("my.project");
            dbh.when(DbConfigHolder::getDbConfig).thenReturn(mockConfig);

            DbHookFlow dummyFlow = mock(DbHookFlow.class);
            when(dummyFlow.flow()).thenReturn((svc, storage, args) -> { /* no-op */ });
            rif.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(DbHookFlow.class), eq("beforeType"), eq("my.project")))
                    .thenReturn(dummyFlow);

            extension.beforeAll(ctx);

            // now this will actually have been invoked once:
            rif.verify(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                    DbHookFlow.class, "beforeType", "my.project"), times(1));
            verify(store, times(1)).put(eq(StoreKeys.HOOKS_PARAMS), any(Map.class));
        }
    }

    @Test
    @DisplayName("afterAll with hooks should invoke ReflectionUtil for AFTER hooks")
    void afterAll_withHooks_invokesReflectionUtil() throws Exception {
        ExtensionContext ctx = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);

        // <-- return the annotated HooksClass, not NoHooksClass
        doReturn(HooksClass.class).when(ctx).getRequiredTestClass();
        doReturn(store).when(ctx).getStore(Namespace.GLOBAL);

        try (MockedStatic<DbConfigHolder> dbh = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> rif = mockStatic(ReflectionUtil.class)) {
            // arrange your DbConfig
            var mockConfig = mock(DbConfig.class);
            when(mockConfig.projectPackage()).thenReturn("pkg");
            dbh.when(DbConfigHolder::getDbConfig).thenReturn(mockConfig);

            // prepare a dummy flow and stub the static finder
            DbHookFlow dummyFlow = mock(DbHookFlow.class);
            when(dummyFlow.flow()).thenReturn((svc, storage, args) -> {/* no-op */});
            rif.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(DbHookFlow.class), eq("afterType"), eq("pkg")))
                    .thenReturn(dummyFlow);

            // act
            extension.afterAll(ctx);

            // assert that we looked up the AFTER hook once
            rif.verify(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                    DbHookFlow.class, "afterType", "pkg"), times(1));
            // and that we still persisted something into the store
            verify(store, times(1)).put(eq(StoreKeys.HOOKS_PARAMS), any(Map.class));
        }
    }

    @Test
    @DisplayName("dbService should return same service instance on multiple calls")
    void dbService_cachesServiceInstance() throws Exception {
        // call private method via reflection twice and compare
        Method m = DbHookExtension.class.getDeclaredMethod("dbService");
        m.setAccessible(true);

        var first = m.invoke(extension);
        var second = m.invoke(extension);

        assertSame(first, second, "dbService should cache and return same instance");
        assertInstanceOf(DatabaseService.class, first, "Returned object should be a DatabaseService");
    }

    @Test
    @DisplayName("executeHook should invoke hookFlow.flow().accept with correct args")
    void executeHook_invokesHookFlow() throws Exception {
        DbHookExtension extension = new DbHookExtension();

        // grab the private method
        Method exec = DbHookExtension.class
                .getDeclaredMethod("executeHook", DbHook.class, Map.class);
        exec.setAccessible(true);

        // prepare a storage map to pass in
        Map<Object, Object> storage = new HashMap<>();

        // now mock the statics
        try (MockedStatic<DbConfigHolder> dbh = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> rif = mockStatic(ReflectionUtil.class)) {

            // stub DbConfigHolder.getDbConfig().projectPackage()
            var cfg = mock(com.theairebellion.zeus.db.config.DbConfig.class);
            when(cfg.projectPackage()).thenReturn("my.pkg");
            dbh.when(DbConfigHolder::getDbConfig).thenReturn(cfg);

            // prepare a dummy DbHookFlow, and capture the Flow.accept() args
            DbHookFlow dummyFlow = mock(DbHookFlow.class);
            AtomicReference<Object> svcRef = new AtomicReference<>();
            AtomicReference<Map> mapRef = new AtomicReference<>();
            AtomicReference<String[]> argsRef = new AtomicReference<>();

            when(dummyFlow.flow()).thenReturn((svc, storageObj, argsObj) -> {
                @SuppressWarnings("unchecked")
                Map<Object, Object> storageHooks = (Map<Object, Object>) storageObj;
                String[] hookArgs = (String[]) argsObj;

                svcRef.set(svc);
                mapRef.set(storageHooks);
                argsRef.set(hookArgs);
            });

            // stub ReflectionUtil to return our dummyFlow
            rif.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(DbHookFlow.class),
                            eq(beforeHook.type()),
                            eq("my.pkg")))
                    .thenReturn(dummyFlow);

            // invoke the private method
            exec.invoke(extension, beforeHook, storage);

            // verify we actually called into ReflectionUtil
            rif.verify(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                    DbHookFlow.class,
                    beforeHook.type(),
                    "my.pkg"), times(1));

            // and that our lambda ran with the expected arguments
            assertTrue(svcRef.get() instanceof DatabaseService,
                    "first arg should be the DatabaseService singleton");
            assertSame(storage, mapRef.get(),
                    "second arg should be the exact storage map passed in");
            assertArrayEquals(beforeHook.arguments(), argsRef.get(),
                    "third arg should be the annotation's arguments array");
        }
    }

    @Test
    @DisplayName("executeHook should wrap any exception in a RuntimeException")
    void executeHook_wrappingExceptions() throws Exception {
        DbHookExtension extension = new DbHookExtension();
        Method exec = DbHookExtension.class
                .getDeclaredMethod("executeHook", DbHook.class, Map.class);
        exec.setAccessible(true);

        Map<Object, Object> storage = new HashMap<>();

        try (MockedStatic<DbConfigHolder> dbh = mockStatic(DbConfigHolder.class);
             MockedStatic<ReflectionUtil> rif = mockStatic(ReflectionUtil.class)) {

            var cfg = mock(com.theairebellion.zeus.db.config.DbConfig.class);
            when(cfg.projectPackage()).thenReturn("my.pkg");
            dbh.when(DbConfigHolder::getDbConfig).thenReturn(cfg);

            // make ReflectionUtil throw
            rif.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(DbHookFlow.class),
                            eq(beforeHook.type()),
                            eq("my.pkg")))
                    .thenThrow(new RuntimeException("boom"));


            // invoking via reflection will wrap your RuntimeException in InvocationTargetException
            InvocationTargetException ite = assertThrows(
                    InvocationTargetException.class,
                    () -> exec.invoke(extension, beforeHook, storage)
            );

            Throwable cause = ite.getCause();
            assertInstanceOf(RuntimeException.class, cause, "should throw our wrapper RuntimeException");
            assertTrue(cause.getMessage().contains("Error executing DbHook: " + beforeHook.type()));
            assertNotNull(cause.getCause());
            assertEquals("boom", cause.getCause().getMessage(),
                    "the original exception should be the cause");
        }
    }
}
