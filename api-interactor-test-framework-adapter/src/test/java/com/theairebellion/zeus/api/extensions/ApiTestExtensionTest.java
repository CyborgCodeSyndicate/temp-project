package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateAs;
import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.framework.chain.FluentChain;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiTestExtensionTest {

    @AuthenticateAs(credentials = TestCreds.class, type = TestAuthClient.class)
    @Test
    public void dummyTestMethod() {
        // no-op, just for annotation coverage
    }

    @Test
    void testBeforeTestExecution_AnnotationPresent() throws Exception {
        // 1) mock the ExtensionContext
        ExtensionContext context = mock(ExtensionContext.class);

        // reflect on the test method that has @AuthenticateAs
        Method testMethod = this.getClass().getDeclaredMethod("dummyTestMethod");
        when(context.getTestMethod()).thenReturn(java.util.Optional.of(testMethod));

        // 2) mock store
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

        List<Consumer<Quest>> consumerList = new ArrayList<>();
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any()))
                .thenReturn(consumerList);

        // 3) create extension, call beforeTestExecution
        ApiTestExtension extension = new ApiTestExtension();
        extension.beforeTestExecution(context);

        // 4) we confirm one consumer was added => annotation logic worked
        assertEquals(1, consumerList.size());
    }

    // Also test a method with no annotation => consumerList stays empty
    @Test
    void testBeforeTestExecution_NoAnnotation() throws Exception {
        ExtensionContext context = mock(ExtensionContext.class);

        // a test method with no annotation:
        Method noAnnotationMethod = this.getClass()
                .getDeclaredMethod("testBeforeTestExecution_NoAnnotation");
        when(context.getTestMethod()).thenReturn(java.util.Optional.of(noAnnotationMethod));

        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

        List<Consumer<Quest>> consumerList = new ArrayList<>();
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any()))
                .thenReturn(consumerList);

        ApiTestExtension extensionSpy = spy(new ApiTestExtension());
        // We can skip stubbing postQuestCreation because it won't be called
        extensionSpy.beforeTestExecution(context);

        assertTrue(consumerList.isEmpty(),
                "No annotation => no consumer added => list is empty");
    }

    @Test
    void testBeforeTestExecution_ShouldInitializeStoreWhenEmpty() throws Exception {
        // 1) Mock context and get annotated method
        ExtensionContext context = mock(ExtensionContext.class);
        Method testMethod = this.getClass().getDeclaredMethod("dummyTestMethod");
        when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

        // 2) Mock store and capture initialization logic
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

        ArgumentCaptor<Function<StoreKeys, List<Consumer<Quest>>>> captor =
                ArgumentCaptor.forClass(Function.class);

        // 3) Track the actual list instance created by the extension
        final List<Consumer<Quest>>[] actualConsumers = new List[1];
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), captor.capture()))
                .thenAnswer(inv -> {
                    // Create and store the actual list instance
                    actualConsumers[0] = captor.getValue().apply(StoreKeys.QUEST_CONSUMERS);
                    return actualConsumers[0];
                });

        // 4) Execute the extension
        new ApiTestExtension().beforeTestExecution(context);

        // 5) Verify against the ACTUAL list instance
        assertNotNull(actualConsumers[0], "Consumer list should not be null");
        assertEquals(1, actualConsumers[0].size(),
                "Consumer should be added to the actual list used by the extension");
    }

    @Test
    void testPostQuestCreation_ShouldModifyQuestStorageAndAuthenticate() throws Exception {
        // Setup test data
        String username = "testUser";
        String password = "testPass";
        AuthenticateAs authAs = this.getClass()
                .getDeclaredMethod("dummyTestMethod")
                .getAnnotation(AuthenticateAs.class);

        // Access private method via reflection
        ApiTestExtension extension = new ApiTestExtension();
        Method method = ApiTestExtension.class.getDeclaredMethod(
                "postQuestCreation",
                String.class,
                String.class,
                AuthenticateAs.class
        );
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Consumer<Quest> consumer = (Consumer<Quest>) method.invoke(extension, username, password, authAs);

        // Create test objects with proper initialization
        @Jailbreak Quest quest = new Quest();
        Storage storage = quest.getStorage(); // Use proper getter

        // Mock RestServiceFluent and register it in Quest's worlds
        RestServiceFluent fluentMock = mock(RestServiceFluent.class);
        @Jailbreak Map<Class<? extends FluentChain>, FluentChain> worlds = quest.worlds;
        worlds.put(RestServiceFluent.class, fluentMock);

        // Execute the consumer
        consumer.accept(quest);

        // Verify storage modifications using proper Enum keys
        assertEquals(username, storage.sub(API).get(USERNAME, String.class));
        assertEquals(password, storage.sub(API).get(PASSWORD, String.class));

        // Verify authentication call
        verify(fluentMock).authenticate(
                eq(username),
                eq(password),
                eq(authAs.type())
        );
    }

}