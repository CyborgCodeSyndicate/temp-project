package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;

public class Craftsman implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterContext.isAnnotated(Craft.class);
    }


    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
        throws ParameterResolutionException {

        Class<?> parameterType = parameterContext.getParameter().getType();

        Craft craft = parameterContext.findAnnotation(Craft.class)
                          .orElseThrow(() -> new ParameterResolutionException("@Craft annotation not found"));

        Quest quest = (Quest) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(QUEST);
        ApplicationContext appCtx = SpringExtension.getApplicationContext(extensionContext);
        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
        SuperQuest superQuest = decoratorsFactory.decorate(quest, SuperQuest.class);
        if (quest == null) {
            throw new IllegalStateException("Quest not found in the global store");
        }

        String model = craft.model();

        DataForge dataForge = ReflectionUtil.findEnumImplementationsOfInterface(
            DataForge.class, model, getFrameworkConfig().projectPackage());

        Object argument;
        if (parameterType.isAssignableFrom(Late.class)) {
            LogTest.extended("Creating Late argument for parameter: {}", parameterContext.getParameter().getName());
            argument = dataForge.dataCreator();
        } else {
            LogTest.extended("Creating immediate argument for parameter: {}", parameterContext.getParameter().getName());
            argument = dataForge.dataCreator().join();
        }

        superQuest.getStorage().sub(ARGUMENTS).put(dataForge.enumImpl(), argument);
        return argument;
    }


}
