package com.theairebellion.zeus.framework.util;

import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;

public class TestContextManager extends AllureStepHelper {

    private static final String TOTAL_PARAMS_KEY = "totalParams";

    public static SuperQuest getSuperQuest(ExtensionContext extensionContext) {
        Quest quest = Optional.ofNullable((Quest) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(QUEST))
                .orElseThrow(() -> new IllegalStateException("Quest not found in the global store."));

        ApplicationContext appCtx = SpringExtension.getApplicationContext(extensionContext);

        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
        return decoratorsFactory.decorate(quest, SuperQuest.class);
    }

    public static void storeArgument(SuperQuest superQuest, DataForge dataForge, Object argument, ExtensionContext extensionContext) {
        superQuest.getStorage().sub(ARGUMENTS).put(dataForge.enumImpl(), argument);

        ExtensionContext.Store store = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL);

        List<Object> args = Optional.ofNullable((List<Object>) store.get(ARGUMENTS)).orElseGet(ArrayList::new);
        args.add(argument);
        store.put(ARGUMENTS, args);
    }

    public static void initializeParameterTracking(ExtensionContext extensionContext) {
        extensionContext.getStore(ExtensionContext.Namespace.create(TestContextManager.class, extensionContext.getUniqueId()))
                .getOrComputeIfAbsent(TOTAL_PARAMS_KEY, key -> extensionContext.getRequiredTestMethod().getParameters().length);
    }
}