package com.theairebellion.zeus.api.extensions;

import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.authentication.Credentials;
import com.theairebellion.zeus.api.log.LogApi;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.api.service.fluent.SuperRestServiceFluent;
import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.*;

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
            ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
            DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);

            Consumer<SuperQuest> questConsumer = createQuestConsumer(decoratorsFactory,
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

    private Consumer<SuperQuest> createQuestConsumer(DecoratorsFactory decoratorsFactory, final String username,
                                                     final String password,
                                                     final Class<? extends BaseAuthenticationClient> clientType,
                                                     final boolean cacheCredentials) {
        CustomAllureListener.startParentStep("Performing Pre-Quest Authentication", CustomAllureListener.StepType.SUCCESS);
        return (SuperQuest quest) -> {
            quest.getStorage().sub(API).put(USERNAME, username);
            quest.getStorage().sub(API).put(PASSWORD, password);

            LogApi.extended("Updated quest storage with credentials for username: {}", username);
            RestServiceFluent restServiceFluent = quest.enters(RestServiceFluent.class);
            SuperRestServiceFluent superRestServiceFluent =
                    decoratorsFactory.decorate(restServiceFluent, SuperRestServiceFluent.class);
            superRestServiceFluent.getRestService().setCacheAuthentication(cacheCredentials);
            restServiceFluent.authenticate(username, password, clientType);
            CustomAllureListener.stopParentStep();
        };
    }

    private void addConsumerToStore(final ExtensionContext context, final Consumer<SuperQuest> questConsumer) {
        @SuppressWarnings("unchecked")
        List<Consumer<SuperQuest>> consumers =
                (List<Consumer<SuperQuest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                        .getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, key -> new ArrayList<Consumer<SuperQuest>>());

        consumers.add(questConsumer);
    }

}