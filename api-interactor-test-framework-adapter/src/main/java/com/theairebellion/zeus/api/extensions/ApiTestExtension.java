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

import java.util.function.Consumer;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.API;
import static com.theairebellion.zeus.api.storage.StorageKeysApi.PASSWORD;
import static com.theairebellion.zeus.api.storage.StorageKeysApi.USERNAME;

public class ApiTestExtension implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        if (context.getTestMethod().isPresent()) {
            AuthenticateViaApiAs authenticateAs = context.getTestMethod().get().getAnnotation(AuthenticateViaApiAs.class);
            if (authenticateAs != null) {
                Credentials credentials = authenticateAs.credentials().getDeclaredConstructor().newInstance();
                String username = credentials.username();
                String password = credentials.password();
                Class<? extends BaseAuthenticationClient> type = authenticateAs.type();
                boolean cashCredentials = authenticateAs.cacheCredentials();

                Consumer<Quest> questConsumer = postQuestCreation(username,
                    password, type, cashCredentials);

                @SuppressWarnings("unchecked")
                var consumers = (java.util.List<Consumer<Quest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                                                                      .getOrComputeIfAbsent(
                                                                          StoreKeys.QUEST_CONSUMERS,
                                                                          key -> new java.util.ArrayList<Consumer<Quest>>()
                                                                      );
                consumers.add(questConsumer);
            }
        }
    }


    private static Consumer<Quest> postQuestCreation(final String username, final String password,
                                                     final Class<? extends BaseAuthenticationClient> type, boolean useCache) {
        return (@Jailbreak Quest quest) -> {
            quest.getStorage().sub(API).put(USERNAME, username);
            quest.getStorage().sub(API).put(PASSWORD, password);
            @Jailbreak RestServiceFluent restServiceFluent = quest.enters(RestServiceFluent.class);
            restServiceFluent.getRestService().setCacheAuthentication(useCache);
            restServiceFluent.authenticate(username, password, type);
        };
    }

}
