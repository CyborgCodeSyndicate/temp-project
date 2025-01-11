package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import manifold.ext.rt.api.Jailbreak;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class Craftsman implements ParameterResolver {

    protected static final FrameworkConfig frameworkConfig = ConfigCache.getOrCreate(FrameworkConfig.class);


    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterContext.isAnnotated(Craft.class);
    }


    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
        throws ParameterResolutionException {

        Class<?> parameterType = parameterContext.getParameter().getType();

        Craft craft = parameterContext.findAnnotation(Craft.class).get();

        @Jailbreak Quest quest = (Quest) extensionContext.getStore(GLOBAL).get(QUEST.getKey());

        DataForge dataForge = ReflectionUtil.findEnumImplementationsOfInterface(DataForge.class, craft.model(),
            frameworkConfig.projectPackage());

        Object argument = parameterType.isAssignableFrom(Late.class)
                              ? dataForge.dataCreator()
                              : dataForge.dataCreator().join();

        quest.getStorage().sub(ARGUMENTS).put(dataForge.enumImpl(), argument);
        return argument;
    }

}
