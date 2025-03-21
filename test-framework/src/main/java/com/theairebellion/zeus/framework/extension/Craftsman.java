package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.util.TestContextManager.*;

/**
 * JUnit 5 {@code ParameterResolver} for injecting test data using the {@code @Craft} annotation.
 * <p>
 * This extension dynamically resolves parameters annotated with {@code @Craft},
 * creating instances of the required model based on the provided configuration.
 * The resolved object is retrieved through reflection and stored within the test execution context.
 * </p>
 *
 * <p>
 * The resolver supports both immediate and lazy instantiation via the {@code Late} wrapper.
 * The created objects are injected into the test method at runtime, allowing
 * seamless data provisioning for test cases.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class Craftsman implements ParameterResolver {

    /**
     * Determines whether the parameter is eligible for resolution by checking
     * for the presence of the {@code @Craft} annotation.
     *
     * @param parameterContext The context of the parameter to resolve.
     * @param extensionContext The context of the test execution.
     * @return {@code true} if the parameter is annotated with {@code @Craft}, otherwise {@code false}.
     * @throws ParameterResolutionException If an error occurs while resolving the parameter.
     */
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.isAnnotated(Craft.class);
    }

    /**
     * Resolves and injects the required test data for parameters annotated with {@code @Craft}.
     * <p>
     * The method identifies the expected data model, retrieves or generates the corresponding
     * instance, and injects it into the test method. If the model is wrapped in a {@code Late} instance,
     * it will be initialized lazily.
     * </p>
     *
     * @param parameterContext The context of the parameter to resolve.
     * @param extensionContext The context of the test execution.
     * @return The resolved parameter object.
     * @throws ParameterResolutionException If the required data model cannot be instantiated.
     */
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