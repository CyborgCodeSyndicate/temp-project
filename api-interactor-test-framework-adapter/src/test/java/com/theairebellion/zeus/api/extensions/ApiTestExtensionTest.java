package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.framework.chain.FluentChain;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
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

    @AuthenticateViaApiAs(credentials = TestCreds.class, type = TestAuthClient.class)
    @Test
    void dummyTestMethod() {
    }

    @Test
    void testBeforeTestExecution_AnnotationPresent() throws Exception {
        var context = mock(ExtensionContext.class);
        var testMethod = this.getClass().getDeclaredMethod("dummyTestMethod");
        when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

        var store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

        List<Consumer<Quest>> consumerList = new ArrayList<>();
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumerList);

        new ApiTestExtension().beforeTestExecution(context);
        assertEquals(1, consumerList.size());
    }

    @Test
    void testBeforeTestExecution_NoAnnotation() throws Exception {
        var context = mock(ExtensionContext.class);
        var noAnnotationMethod = this.getClass().getDeclaredMethod("testBeforeTestExecution_NoAnnotation");
        when(context.getTestMethod()).thenReturn(Optional.of(noAnnotationMethod));

        var store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

        List<Consumer<Quest>> consumerList = new ArrayList<>();
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumerList);

        new ApiTestExtension().beforeTestExecution(context);
        assertTrue(consumerList.isEmpty());
    }

    @Test
    void testBeforeTestExecution_ShouldInitializeStoreWhenEmpty() throws Exception {
        var context = mock(ExtensionContext.class);
        var testMethod = this.getClass().getDeclaredMethod("dummyTestMethod");
        when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

        var store = mock(ExtensionContext.Store.class);
        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);

        ArgumentCaptor<Function<StoreKeys, List<Consumer<Quest>>>> captor = ArgumentCaptor.forClass(Function.class);
        final List<Consumer<Quest>>[] actualConsumers = new List[1];

        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), captor.capture())).thenAnswer(inv -> {
            actualConsumers[0] = captor.getValue().apply(StoreKeys.QUEST_CONSUMERS);
            return actualConsumers[0];
        });

        new ApiTestExtension().beforeTestExecution(context);
        assertAll(
                () -> assertNotNull(actualConsumers[0]),
                () -> assertEquals(1, actualConsumers[0].size())
        );
    }

    @Test
    void testCreateQuestConsumer_ShouldModifyQuestStorageAndAuthenticate() throws Exception {
        var username = "testUser";
        var password = "testPass";

        var authAs = this
                .getClass()
                .getDeclaredMethod("dummyTestMethod")
                .getAnnotation(AuthenticateViaApiAs.class);

        var extension = new ApiTestExtension();
        var method = ApiTestExtension.class.getDeclaredMethod(
                "createQuestConsumer",
                String.class,
                String.class,
                Class.class,
                boolean.class
        );
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        var consumer = (Consumer<Quest>) method.invoke(
                extension,
                username,
                password,
                authAs.type(),
                authAs.cacheCredentials()
        );

        @Jailbreak Quest quest = new Quest();
        var storage = quest.getStorage();

        var restServiceMock = mock(com.theairebellion.zeus.api.service.RestService.class);

        var fluentMock = mock(RestServiceFluent.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        Field restField = RestServiceFluent.class.getDeclaredField("restService");
        restField.setAccessible(true);
        restField.set(fluentMock, restServiceMock);

        @Jailbreak Map<Class<? extends FluentChain>, FluentChain> worlds = quest.worlds;
        worlds.put(RestServiceFluent.class, fluentMock);

        consumer.accept(quest);

        assertAll(
                () -> assertEquals(username, storage.sub(API).get(USERNAME, String.class)),
                () -> assertEquals(password, storage.sub(API).get(PASSWORD, String.class))
        );

        verify(restServiceMock).setCacheAuthentication(authAs.cacheCredentials());
        verify(fluentMock).authenticate(eq(username), eq(password), eq(authAs.type()));
    }

}