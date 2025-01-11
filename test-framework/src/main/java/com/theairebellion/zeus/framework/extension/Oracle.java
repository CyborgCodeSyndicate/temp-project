package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestFactory;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

        @Jailbreak Quest quest = questFactory.createQuest();
        LogTest.info("Quest crafted for scenario: '{}'.", extensionContext.getDisplayName());
        ExtensionContext.Store store = extensionContext.getStore(GLOBAL);
        @SuppressWarnings("unchecked")
        List<Consumer<Quest>> consumers = (List<Consumer<Quest>>) store.get(StoreKeys.QUEST_CONSUMERS);
        if (Objects.nonNull(consumers)) {
            consumers.forEach(questConsumer -> questConsumer.accept(quest));
        }
        store.put(QUEST.getKey(), quest);
        return quest;
    }

}
