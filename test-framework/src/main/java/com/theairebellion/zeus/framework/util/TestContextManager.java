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

/**
 * Manages the test execution context, providing utilities for storing and retrieving test-related data.
 * <p>
 * This class handles:
 * <ul>
 *     <li>Retrieving and decorating the test quest context.</li>
 *     <li>Storing test method arguments for later reference.</li>
 *     <li>Tracking the number of parameters in test methods.</li>
 * </ul>
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class TestContextManager extends AllureStepHelper {

    /**
     * Key used for tracking the total number of parameters in a test method.
     */
    private static final String TOTAL_PARAMS_KEY = "totalParams";

    /**
     * Retrieves the {@link SuperQuest} associated with the given test execution context.
     * <p>
     * This method extracts the {@link Quest} object from the global extension context store,
     * then decorates it using the {@link DecoratorsFactory} to return an instance of {@link SuperQuest}.
     * </p>
     *
     * @param extensionContext the JUnit extension context
     * @return a {@link SuperQuest} instance containing decorated quest data
     * @throws IllegalStateException if no {@link Quest} is found in the global store
     */
    public static SuperQuest getSuperQuest(ExtensionContext extensionContext) {
        Quest quest = Optional.ofNullable((Quest) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(QUEST))
                .orElseThrow(() -> new IllegalStateException("Quest not found in the global store."));

        ApplicationContext appCtx = SpringExtension.getApplicationContext(extensionContext);
        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);

        return decoratorsFactory.decorate(quest, SuperQuest.class);
    }

    /**
     * Stores a test argument in both the {@link SuperQuest} storage and the global JUnit extension context.
     * <p>
     * This method ensures that test arguments are persistently stored and can be retrieved later if needed.
     * The arguments are categorized using the {@link DataForge} enumeration.
     * </p>
     *
     * @param superQuest       the current {@link SuperQuest} instance managing test data
     * @param dataForge        an enum representing the type of argument
     * @param argument         the argument object to be stored
     * @param extensionContext the JUnit extension context
     */
    public static void storeArgument(SuperQuest superQuest, DataForge dataForge, Object argument, ExtensionContext extensionContext) {
        superQuest.getStorage().sub(ARGUMENTS).put(dataForge.enumImpl(), argument);

        ExtensionContext.Store store = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL);
        List<Object> args = Optional.ofNullable((List<Object>) store.get(ARGUMENTS)).orElseGet(ArrayList::new);
        args.add(argument);
        store.put(ARGUMENTS, args);
    }

    /**
     * Initializes parameter tracking for a test method.
     * <p>
     * This method stores the total number of parameters of the test method in the extension context.
     * The stored value can be used for validation or further processing within the test lifecycle.
     * </p>
     *
     * @param extensionContext the JUnit extension context
     */
    public static void initializeParameterTracking(ExtensionContext extensionContext) {
        extensionContext.getStore(ExtensionContext.Namespace.create(TestContextManager.class, extensionContext.getUniqueId()))
                .getOrComputeIfAbsent(TOTAL_PARAMS_KEY, key -> extensionContext.getRequiredTestMethod().getParameters().length);
    }

}
