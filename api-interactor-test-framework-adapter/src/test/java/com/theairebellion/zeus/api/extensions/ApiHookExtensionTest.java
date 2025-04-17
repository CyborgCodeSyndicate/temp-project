package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.ApiHook;
import com.theairebellion.zeus.api.config.ApiConfig;
import com.theairebellion.zeus.api.config.ApiConfigHolder;
import com.theairebellion.zeus.api.hooks.ApiHookFlow;
import com.theairebellion.zeus.framework.hooks.HookExecution;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiHookExtension Tests")
class ApiHookExtensionTest {

    @Mock
    private ExtensionContext context;
    @Mock
    private ExtensionContext.Store store;

    private final ApiHookExtension extension = new ApiHookExtension();

    @Nested
    @DisplayName("beforeAll Tests")
    class BeforeAllTests {

        /**
         * used by the exception‑wrapping test
         */
        @ApiHook(type = "failType", when = HookExecution.BEFORE,
                arguments = {"arg1", "arg2"}, order = 42)
        static class DummyWithBeforeHook {
        }

        /**
         * used by the normal success test
         */
        @ApiHook(type = "beforeType", when = HookExecution.BEFORE,
                arguments = {"p", "q"}, order = 1)
        static class DummyWithValidBeforeHook {
        }

        @Test
        @DisplayName("beforeAll wraps any exception in RuntimeException")
        void beforeAllWrapsReflectionErrors() {
            // still use the “failType” dummy here
            doReturn(DummyWithBeforeHook.class)
                    .when(context).getRequiredTestClass();

            try (
                    var cfgMock = mockStatic(ApiConfigHolder.class);
                    var reflMock = mockStatic(ReflectionUtil.class)
            ) {
                ApiConfig cfg = mock(ApiConfig.class);
                cfgMock.when(ApiConfigHolder::getApiConfig).thenReturn(cfg);
                when(cfg.projectPackage()).thenReturn("dummy.pkg");

                reflMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(ApiHookFlow.class), eq("failType"), anyString()
                )).thenThrow(new RuntimeException("boom-from-reflection"));

                RuntimeException ex = assertThrows(RuntimeException.class,
                        () -> extension.beforeAll(context),
                        "Expected reflection failure to be wrapped"
                );
                assertThat(ex.getMessage()).contains("Error executing DbHook: failType");
                assertThat(ex.getCause()).hasMessage("boom-from-reflection");
            }
        }

        @Test
        @DisplayName("beforeAll should execute only BEFORE hooks and put map in store")
        void beforeAll_executesBeforeHooksAndStores() throws Exception {
            // *** crucial change: return the *valid* dummy here ***
            doReturn(DummyWithValidBeforeHook.class)
                    .when(context).getRequiredTestClass();
            when(context.getStore(ExtensionContext.Namespace.GLOBAL))
                    .thenReturn(store);

            try (
                    var cfgMock = mockStatic(ApiConfigHolder.class);
                    var reflMock = mockStatic(ReflectionUtil.class)
            ) {
                ApiConfig cfg = mock(ApiConfig.class);
                cfgMock.when(ApiConfigHolder::getApiConfig).thenReturn(cfg);
                when(cfg.projectPackage()).thenReturn("any.pkg");

                // now stub the "beforeType" hook
                ApiHookFlow hookFlow = mock(ApiHookFlow.class);
                when(hookFlow.flow()).thenReturn(
                        (restSvc, storage, args) -> storage.put(args[0], args[1])
                );
                reflMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(ApiHookFlow.class),
                        eq("beforeType"),   // <- must match your valid dummy
                        anyString()
                )).thenReturn(hookFlow);

                extension.beforeAll(context);

                ArgumentCaptor<Map<Object, Object>> cap =
                        ArgumentCaptor.forClass(Map.class);
                verify(store).put(eq(StoreKeys.HOOKS_PARAMS), cap.capture());

                Map<Object, Object> saved = cap.getValue();
                assertThat(saved)
                        .hasSize(1)
                        .containsEntry("p", "q");
            }
        }
    }

    @Nested
    @DisplayName("afterAll Tests")
    class AfterAllTests {

        /**
         * Dummy class for AFTER hook testing
         */
        @ApiHook(type = "afterType", when = HookExecution.AFTER,
                arguments = {"a", "b"})
        static class DummyWithAfterHook {
        }

        @Test
        @DisplayName("afterAll executes only AFTER hooks and puts map in store")
        void afterAllExecutesAfterHooksAndStores() throws Exception {
            doReturn(DummyWithAfterHook.class)
                    .when(context).getRequiredTestClass();
            when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

            try (
                    var cfgMock = mockStatic(ApiConfigHolder.class);
                    var reflMock = mockStatic(ReflectionUtil.class)
            ) {
                ApiConfig cfg = mock(ApiConfig.class);
                cfgMock.when(ApiConfigHolder::getApiConfig).thenReturn(cfg);
                when(cfg.projectPackage()).thenReturn("ignored.pkg");

                ApiHookFlow hookFlow = mock(ApiHookFlow.class);
                when(hookFlow.flow()).thenReturn((restSvc, storage, args) -> storage.put(args[0], args[1]));

                reflMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                        eq(ApiHookFlow.class), eq("afterType"), anyString()
                )).thenReturn(hookFlow);

                extension.afterAll(context);

                ArgumentCaptor<Map<Object, Object>> captor = ArgumentCaptor.forClass(Map.class);
                verify(store).put(eq(StoreKeys.HOOKS_PARAMS), captor.capture());

                Map<Object, Object> saved = captor.getValue();
                assertThat(saved).containsEntry("a", "b");
            }
        }
    }

    @Nested
    @DisplayName("Filter‑out‑wrong‑hooks Tests")
    class FilterOutWrongHooksTests {

        @ApiHook(type = "onlyAfter", when = HookExecution.AFTER, arguments = {"x", "y"})
        static class DummyOnlyAfter {
        }

        @ApiHook(type = "onlyBefore", when = HookExecution.BEFORE, arguments = {"m", "n"})
        static class DummyOnlyBefore {
        }

        @Test
        @DisplayName("beforeAll should filter out AFTER‑only hooks")
        void beforeAll_filtersOutAfterOnly() throws Exception {
            // tell the extension we “ran” DummyOnlyAfter
            doReturn(DummyOnlyAfter.class).when(context).getRequiredTestClass();
            when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

            // exercise beforeAll — no static mocks needed because executeHook never fires
            extension.beforeAll(context);

            ArgumentCaptor<Map<Object, Object>> cap = ArgumentCaptor.forClass(Map.class);
            verify(store).put(eq(StoreKeys.HOOKS_PARAMS), cap.capture());

            // since only an AFTER hook was present, the BEFORE filter dropped it => empty map
            assertThat(cap.getValue()).isEmpty();
        }

        @Test
        @DisplayName("afterAll should filter out BEFORE‑only hooks")
        void afterAll_filtersOutBeforeOnly() throws Exception {
            doReturn(DummyOnlyBefore.class).when(context).getRequiredTestClass();
            when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

            extension.afterAll(context);

            ArgumentCaptor<Map<Object, Object>> cap = ArgumentCaptor.forClass(Map.class);
            verify(store).put(eq(StoreKeys.HOOKS_PARAMS), cap.capture());

            assertThat(cap.getValue()).isEmpty();
        }
    }

    @Nested
    @DisplayName("restService() Memoization Tests")
    class RestServiceMemoizationTests {
        @Test
        @DisplayName("restService() should create one instance and then reuse it")
        void restService_memoizes() throws Exception {
            // grab the private method
            var m = ApiHookExtension.class
                    .getDeclaredMethod("restService");
            m.setAccessible(true);

            // first call yields non‑null
            Object first = m.invoke(extension);
            assertThat(first).isNotNull();

            // second call must return the *same* object
            Object second = m.invoke(extension);
            assertThat(second).isSameAs(first);
        }
    }

}
