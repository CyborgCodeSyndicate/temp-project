package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
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

        Object argument = parameterType.isAssignableFrom(Late.class)
                ? dataForge.dataCreator()
                : dataForge.dataCreator().join();

        superQuest.getStorage().sub(ARGUMENTS).put(dataForge.enumImpl(), argument);
        return argument;
    }

}
