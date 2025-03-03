package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
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

@Order(Integer.MAX_VALUE)
public class Initiator implements InvocationInterceptor {


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
            sortedPreQuestAnnotations.forEach(preQuest -> processPreQuest(preQuest, superQuest));
        }

        invocation.proceed();
    }


    private List<Journey> getSortedJourneys(Method method) {
        return Arrays.stream(method.getAnnotationsByType(Journey.class))
                .sorted(Comparator.comparing(Journey::order))
                .collect(Collectors.toList());
    }


    private void processPreQuest(Journey preQuest, SuperQuest superQuest) {
        String journey = preQuest.value();
        JourneyData[] journeyData = preQuest.journeyData();

        PreQuestJourney preQuestJourney = ReflectionUtil.findEnumImplementationsOfInterface(
                PreQuestJourney.class, journey, getFrameworkConfig().projectPackage());

        preQuestJourney.journey().accept(superQuest, Arrays.stream(journeyData)
                .map(dataEnumStr -> processJourneyData(dataEnumStr, superQuest))
                .toArray());
    }


    private Object processJourneyData(JourneyData journeyData, SuperQuest quest) {
        DataForge dataForge = ReflectionUtil.findEnumImplementationsOfInterface(
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





