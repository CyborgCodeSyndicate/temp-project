package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.allure.StepType;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.util.ObjectFormatter;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;

/**
 * JUnit 5 {@code InvocationInterceptor} extension that processes pre-test setup
 * for test methods annotated with {@code @PreQuest}.
 * <p>
 * This extension extracts all {@code @PreQuest} annotations from a test method,
 * sorts them based on execution order, and executes the corresponding preconditions
 * before the test runs. The preconditions are dynamically resolved and injected
 * into the test execution context.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Order(Integer.MAX_VALUE)
public class Initiator implements InvocationInterceptor {

    /**
     * Intercepts test method execution to process {@code @PreQuest} preconditions.
     *
     * @param invocation        The invocation context of the test method.
     * @param invocationContext Reflective context of the method.
     * @param extensionContext  Context of the test execution.
     * @throws Throwable If an error occurs while processing preconditions.
     */
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {

        Optional<Method> testMethod = extensionContext.getTestMethod();

        if (testMethod.isPresent() && testMethod.get().isAnnotationPresent(PreQuest.class)) {
            Quest quest = (Quest) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(QUEST);
            if (quest == null) {
                throw new IllegalStateException("Quest not found in the global store");
            }

            List<Journey> sortedPreQuestAnnotations = getSortedJourneys(testMethod.get());

            ApplicationContext appCtx = SpringExtension.getApplicationContext(extensionContext);
            DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
            SuperQuest superQuest = decoratorsFactory.decorate(quest, SuperQuest.class);
            if (!CustomAllureListener.isStepActive(StepType.PROCESSING_PRE_QUESTS.getDisplayName())) {
                CustomAllureListener.startStep(StepType.PROCESSING_PRE_QUESTS);
            }
            sortedPreQuestAnnotations.forEach(preQuest -> processPreQuest(preQuest, superQuest));
            if (CustomAllureListener.isStepActive(StepType.PROCESSING_PRE_QUESTS.getDisplayName())) {
                CustomAllureListener.stopStep();
            }
        }
        if (!CustomAllureListener.isStepActive(StepType.TEST_EXECUTION.getDisplayName())) {
            CustomAllureListener.startStep(StepType.TEST_EXECUTION);
        }
        invocation.proceed();
    }

    /**
     * Retrieves and sorts {@code @Journey} annotations based on their execution order.
     *
     * @param method The test method containing {@code @Journey} annotations.
     * @return A sorted list of {@code @Journey} annotations.
     */
    private List<Journey> getSortedJourneys(Method method) {
        return Arrays.stream(method.getAnnotationsByType(Journey.class))
                .sorted(Comparator.comparing(Journey::order))
                .toList();
    }

    /**
     * Processes a single {@code @Journey} annotation, resolving and executing its precondition logic.
     *
     * @param preQuest   The {@code @Journey} annotation representing the precondition.
     * @param superQuest The test execution context enriched with preconditions.
     */
    private void processPreQuest(Journey preQuest, SuperQuest superQuest) {
        String journey = preQuest.value();
        JourneyData[] journeyData = preQuest.journeyData();

        PreQuestJourney<?> preQuestJourney = ReflectionUtil.findEnumImplementationsOfInterface(
                PreQuestJourney.class, journey, getFrameworkConfig().projectPackage());

        Object[] processedData = Arrays.stream(journeyData)
                .map(dataEnumStr -> processJourneyData(dataEnumStr, superQuest))
                .toArray();

        String stepName = StepType.PROCESSING_PRE_QUEST.getDisplayName() + ": " + preQuestJourney.toString();
        CustomAllureListener.startStep(stepName);
        String attachmentName = journey + "-Data";

        String formattedData = ObjectFormatter.formatProcessedData(journeyData, processedData);
        Allure.addAttachment(attachmentName, formattedData);

        preQuestJourney.journey().accept(superQuest, processedData);
        if (CustomAllureListener.isStepActive(stepName)) {
            CustomAllureListener.stopStep();
        }
    }

    /**
     * Resolves test data for {@code @JourneyData} annotations.
     *
     * @param journeyData The {@code @JourneyData} annotation containing the data model reference.
     * @param quest       The test execution context where the resolved data is stored.
     * @return The resolved test data object.
     */
    private Object processJourneyData(JourneyData journeyData, SuperQuest quest) {
        DataForge<?> dataForge = ReflectionUtil.findEnumImplementationsOfInterface(
                DataForge.class, journeyData.value(), getFrameworkConfig().projectPackage());

        Object argument;
        if (journeyData.late()) {
            LogTest.extended("Creating data using late binding for: {}", journeyData.value());
            argument = dataForge.dataCreator();
        } else {
            LogTest.extended("Joining data for: {}", journeyData.value());
            argument = dataForge.dataCreator().join();
        }
        quest.getStorage().sub(PRE_ARGUMENTS).put(dataForge.enumImpl(), argument);
        return argument;
    }

}





