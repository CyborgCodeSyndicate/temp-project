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

public class Craftsman implements ParameterResolver {

    protected static final FrameworkConfig FRAMEWORK_CONFIG = ConfigCache.getOrCreate(FrameworkConfig.class);

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

        @Jailbreak Quest quest = (Quest) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(QUEST);
        if (quest == null) {
            throw new IllegalStateException("Quest not found in the global store");
        }

        String model = craft.model();

        DataForge dataForge = ReflectionUtil.findEnumImplementationsOfInterface(
                DataForge.class, model, FRAMEWORK_CONFIG.projectPackage());

        Object argument = parameterType.isAssignableFrom(Late.class)
                ? dataForge.dataCreator()
                : dataForge.dataCreator().join();

        quest.getStorage().sub(ARGUMENTS).put(dataForge.enumImpl(), argument);
        return argument;
    }

}
