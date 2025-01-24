package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateAs;
import com.theairebellion.zeus.api.authentication.Credentials;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.api.log.LogApi;
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
            AuthenticateAs authenticateAs = context.getTestMethod().get().getAnnotation(AuthenticateAs.class);
            if (authenticateAs != null) {
                LogApi.extended("Detected @AuthenticateAs annotation in test '{}'. Initializing credentials for user type: '{}'.",
                        context.getDisplayName(), authenticateAs.type());
                Credentials credentials = authenticateAs.credentials().getDeclaredConstructor().newInstance();
                String username = credentials.username();
                String password = credentials.password();
                Consumer<Quest> questConsumer = postQuestCreation(username,
                    password, authenticateAs);

                @SuppressWarnings("unchecked")
                var consumers = (java.util.List<Consumer<Quest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                                                                      .getOrComputeIfAbsent(
                                                                          StoreKeys.QUEST_CONSUMERS,
                                                                          key -> new java.util.ArrayList<Consumer<Quest>>()
                                                                      );
                consumers.add(questConsumer);
                LogApi.extended("Added quest consumer for username: {} to global store.", username);
            }
        } else {
            LogApi.warn("No test method found in the current context for test: {}", context.getDisplayName());
        }
    }


    private static Consumer<Quest> postQuestCreation(final String username, final String password,
                                                     final AuthenticateAs authenticateAs) {
        Consumer<Quest> questConsumer = (@Jailbreak Quest quest) -> {
            quest.getStorage().sub(API).put(USERNAME, username);
            quest.getStorage().sub(API).put(PASSWORD, password);
            LogApi.extended("Updated quest storage with credentials for username: {}", username);
            @Jailbreak RestServiceFluent restServiceFluent = quest.enters(RestServiceFluent.class);
            restServiceFluent.authenticate(username, password, authenticateAs.type());
        };
        return questConsumer;
    }

}
