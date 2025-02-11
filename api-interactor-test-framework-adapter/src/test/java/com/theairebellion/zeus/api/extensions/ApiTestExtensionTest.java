package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.annotations.mock.TestAuthClient;
import com.theairebellion.zeus.api.annotations.mock.TestCreds;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.api.service.fluent.SuperRestServiceFluent;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiTestExtensionTest {

    private ExtensionContext context;
    private ExtensionContext.Store store;
    private ApplicationContext appContext;
    private DecoratorsFactory decoratorsFactory;

    @BeforeEach
    void setup() {
        context = mock(ExtensionContext.class);
        store = mock(ExtensionContext.Store.class);
        appContext = mock(ApplicationContext.class);
        decoratorsFactory = mock(DecoratorsFactory.class);

        when(context.getStore(ExtensionContext.Namespace.GLOBAL)).thenReturn(store);
    }

    @AuthenticateViaApiAs(credentials = TestCreds.class, type = TestAuthClient.class)
    @Test
    void dummyTestMethod() {
    }

    @Test
    void testBeforeTestExecution_AnnotationPresent() throws Exception {
        var testMethod = this.getClass().getDeclaredMethod("dummyTestMethod");
        when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

        List<Consumer<SuperQuest>> consumerList = new ArrayList<>();
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumerList);

        // Mock SpringExtension to return the application context
        try (MockedStatic<SpringExtension> springMock = mockStatic(SpringExtension.class)) {
            springMock.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appContext);
            when(appContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            new ApiTestExtension().beforeTestExecution(context);
            assertEquals(1, consumerList.size());
        }
    }

    @Test
    void testBeforeTestExecution_NoAnnotation() throws Exception {
        var noAnnotationMethod = this.getClass().getDeclaredMethod("testBeforeTestExecution_NoAnnotation");
        when(context.getTestMethod()).thenReturn(Optional.of(noAnnotationMethod));

        List<Consumer<SuperQuest>> consumerList = new ArrayList<>();
        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), any())).thenReturn(consumerList);

        new ApiTestExtension().beforeTestExecution(context);
        assertTrue(consumerList.isEmpty());
    }

    @Test
    void testBeforeTestExecution_ShouldInitializeStoreWhenEmpty() throws Exception {
        var testMethod = this.getClass().getDeclaredMethod("dummyTestMethod");
        when(context.getTestMethod()).thenReturn(Optional.of(testMethod));

        ArgumentCaptor<Function<StoreKeys, List<Consumer<SuperQuest>>>> captor = ArgumentCaptor.forClass(Function.class);
        final List<Consumer<SuperQuest>>[] actualConsumers = new List[1];

        when(store.getOrComputeIfAbsent(eq(StoreKeys.QUEST_CONSUMERS), captor.capture())).thenAnswer(inv -> {
            actualConsumers[0] = captor.getValue().apply(StoreKeys.QUEST_CONSUMERS);
            return actualConsumers[0];
        });

        try (MockedStatic<SpringExtension> springMock = mockStatic(SpringExtension.class)) {
            springMock.when(() -> SpringExtension.getApplicationContext(context)).thenReturn(appContext);
            when(appContext.getBean(DecoratorsFactory.class)).thenReturn(decoratorsFactory);

            new ApiTestExtension().beforeTestExecution(context);
            assertAll(
                    () -> assertNotNull(actualConsumers[0]),
                    () -> assertEquals(1, actualConsumers[0].size())
            );
        }
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
                DecoratorsFactory.class,
                String.class,
                String.class,
                Class.class,
                boolean.class
        );
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        var consumer = (Consumer<SuperQuest>) method.invoke(
                extension,
                decoratorsFactory,
                username,
                password,
                authAs.type(),
                authAs.cacheCredentials()
        );

        SuperQuest quest = mock(SuperQuest.class);
        Storage storage = mock(Storage.class);
        Storage subStorage = mock(Storage.class);

        when(quest.getStorage()).thenReturn(storage);
        when(storage.sub(API)).thenReturn(subStorage);

        var restServiceMock = mock(RestService.class);
        var fluentMock = mock(RestServiceFluent.class);
        when(fluentMock.authenticate(any(), any(), any())).thenReturn(fluentMock);

        Field restField = RestServiceFluent.class.getDeclaredField("restService");
        restField.setAccessible(true);
        restField.set(fluentMock, restServiceMock);

        when(quest.enters(RestServiceFluent.class)).thenReturn(fluentMock);

        var superFluentMock = mock(SuperRestServiceFluent.class);
        when(decoratorsFactory.decorate(fluentMock, SuperRestServiceFluent.class)).thenReturn(superFluentMock);

        Field originalField = SuperRestServiceFluent.class.getDeclaredField("original");
        originalField.setAccessible(true);
        originalField.set(superFluentMock, fluentMock);

        when(superFluentMock.getRestService()).thenReturn(restServiceMock);

        consumer.accept(quest);

        assertAll(
                () -> verify(subStorage, times(1)).put(USERNAME, username),
                () -> verify(subStorage, times(1)).put(PASSWORD, password),
                () -> verify(restServiceMock, times(1)).setCacheAuthentication(authAs.cacheCredentials()),
                () -> verify(fluentMock, times(1)).authenticate(eq(username), eq(password), eq(authAs.type()))
        );
    }
}
