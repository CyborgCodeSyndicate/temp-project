package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.TestStaticData;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestFactory;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.STATIC_DATA;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Order(Integer.MIN_VALUE)
@ExtendWith(SpringExtension.class)
public class Oracle implements ParameterResolver {


    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
        throws ParameterResolutionException {
        var parameterType = parameterContext.getParameter().getType();
        return Quest.class.isAssignableFrom(parameterType);
    }


    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
        throws ParameterResolutionException {
        ApplicationContext appCtx = SpringExtension.getApplicationContext(extensionContext);
        QuestFactory questFactory = appCtx.getBean(QuestFactory.class);
        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);

        Quest quest = questFactory.createQuest();
        SuperQuest superQuest = decoratorsFactory.decorate(quest, SuperQuest.class);
        Map<String, Object> staticTestData = getStaticTestData(extensionContext);
        Storage storage = superQuest.getStorage();
        storage.put(STATIC_DATA, staticTestData);
        addHooksDataInTestStorage(storage, extensionContext);
        LogTest.info("Quest crafted for scenario: '{}'.", extensionContext.getDisplayName());
        ExtensionContext.Store store = extensionContext.getStore(GLOBAL);
        @SuppressWarnings("unchecked")
        List<Consumer<SuperQuest>> consumers = (List<Consumer<SuperQuest>>) store.get(StoreKeys.QUEST_CONSUMERS);
        if (Objects.nonNull(consumers)) {
            consumers.forEach(
                questConsumer -> questConsumer.accept(decoratorsFactory.decorate(quest, SuperQuest.class)));
        }
        store.put(QUEST, quest);
        return quest;
    }


    private static Map<String, Object> getStaticTestData(final ExtensionContext extensionContext) {
        Optional<Method> testMethod = extensionContext.getTestMethod();
        if (testMethod.isPresent()) {
            TestStaticData staticDataAnnotation = testMethod.get().getAnnotation(TestStaticData.class);
            if (staticDataAnnotation != null) {
                try {
                    return staticDataAnnotation.value().getDeclaredConstructor()
                               .newInstance().testStaticData();

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }


    private static void addHooksDataInTestStorage(Storage storage, ExtensionContext context) {
        Map<Object, Object> hooksStorage = context.getStore(GLOBAL).get(StoreKeys.HOOKS_PARAMS, Map.class);
        if (hooksStorage != null) {
            storage.put(StorageKeysTest.HOOKS, hooksStorage);
        }
    }

}
