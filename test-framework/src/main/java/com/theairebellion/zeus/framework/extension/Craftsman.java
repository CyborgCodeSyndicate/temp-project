package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.util.TestContextManager;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;

public class Craftsman extends TestContextManager implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.isAnnotated(Craft.class);
    }


    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        LogTest.debug("Resolving parameter: {}", parameterContext.getParameter().getName());
        initializeParameterTracking(extensionContext);

        Class<?> parameterType = parameterContext.getParameter().getType();
        boolean isLateResolution = parameterType.isAssignableFrom(Late.class);

        return resolveParameterInternal(parameterContext, extensionContext, isLateResolution);
    }

    private Object resolveParameterInternal(ParameterContext parameterContext, ExtensionContext extensionContext, boolean isLate) {
        try {
            Craft craft = parameterContext.findAnnotation(Craft.class)
                    .orElseThrow(() -> new ParameterResolutionException(
                            "Missing @Craft annotation on parameter: " + parameterContext.getParameter().getName()));
            SuperQuest superQuest = getSuperQuest(extensionContext);

            DataForge dataForge = ReflectionUtil.findEnumImplementationsOfInterface(
                    DataForge.class, craft.model(), getFrameworkConfig().projectPackage());
            Object argument = isLate ? dataForge.dataCreator() : dataForge.dataCreator().join();

            storeArgument(superQuest, dataForge, argument, extensionContext);

            return argument;
        } catch (Exception e) {
            throw new ParameterResolutionException("Failed to resolve parameter: " + parameterContext.getParameter().getName(), e);
        }
    }
}