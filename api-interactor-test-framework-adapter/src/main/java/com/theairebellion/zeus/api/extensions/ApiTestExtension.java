package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.authentication.Credentials;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.API;
import static com.theairebellion.zeus.api.storage.StorageKeysApi.PASSWORD;
import static com.theairebellion.zeus.api.storage.StorageKeysApi.USERNAME;

public class ApiTestExtension implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        context.getTestMethod()
                .map(method -> method.getAnnotation(AuthenticateViaApiAs.class))
                .ifPresent(annotation -> handleAuthentication(context, annotation));
    }

    private void handleAuthentication(final ExtensionContext context, final AuthenticateViaApiAs annotation) {
        try {
            Credentials credentials = annotation.credentials()
                    .getDeclaredConstructor()
                    .newInstance();

            Consumer<Quest> questConsumer = createQuestConsumer(
                    credentials.username(),
                    credentials.password(),
                    annotation.type(),
                    annotation.cacheCredentials()
            );

            addConsumerToStore(context, questConsumer);

        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new IllegalStateException("Failed to instantiate credentials.", e);
        }
    }

    private Consumer<Quest> createQuestConsumer(final String username,
                                                final String password,
                                                final Class<? extends BaseAuthenticationClient> clientType,
                                                final boolean cacheCredentials) {
        return (@Jailbreak Quest quest) -> {
            quest.getStorage().sub(API).put(USERNAME, username);
            quest.getStorage().sub(API).put(PASSWORD, password);

            @Jailbreak RestServiceFluent restServiceFluent = quest.enters(RestServiceFluent.class);
            restServiceFluent.getRestService().setCacheAuthentication(cacheCredentials);
            restServiceFluent.authenticate(username, password, clientType);
        };
    }

    private void addConsumerToStore(final ExtensionContext context, final Consumer<Quest> questConsumer) {
        @SuppressWarnings("unchecked")
        List<Consumer<Quest>> consumers = (List<Consumer<Quest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, key -> new ArrayList<Consumer<Quest>>());

        consumers.add(questConsumer);
    }

}