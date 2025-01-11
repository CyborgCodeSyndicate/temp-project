package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import manifold.ext.rt.api.Jailbreak;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Order(Integer.MAX_VALUE)
public class Initiator implements InvocationInterceptor {

    protected static final FrameworkConfig frameworkConfig = ConfigCache.getOrCreate(FrameworkConfig.class);


    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext
    ) throws Throwable {

        Optional<Method> testMethod = extensionContext.getTestMethod();
        if (testMethod.isPresent() && null != testMethod.get().getAnnotation(PreQuest.class)) {
            @Jailbreak Quest quest = (Quest) extensionContext.getStore(GLOBAL).get(QUEST.getKey());

            Journey[] annotations = testMethod.get().getAnnotationsByType(Journey.class);
            List<Journey> sorterPreQuestsAnnotations = Arrays.stream(annotations)
                                                           .sorted(Comparator.comparing(Journey::order))
                                                           .toList();

            sorterPreQuestsAnnotations.forEach(preQuest -> {
                String journey = preQuest.value();
                JourneyData[] journeyData = preQuest.journeyData();
                PreQuestJourney preQuestJourney = ReflectionUtil.findEnumImplementationsOfInterface(
                    PreQuestJourney.class, journey, frameworkConfig.projectPackage());

                List<Object> data = new ArrayList<>();

                Arrays.stream(journeyData).forEach(dataEnumStr -> {
                    DataForge dataForge = ReflectionUtil.findEnumImplementationsOfInterface(DataForge.class,
                        dataEnumStr.value(), frameworkConfig.projectPackage());

                    Object argument = dataEnumStr.late()
                                          ? dataForge.dataCreator()
                                          : dataForge.dataCreator().join();
                    data.add(argument);
                    quest.getStorage().sub(PRE_ARGUMENTS).put(dataForge.enumImpl(), argument);
                });
                preQuestJourney.journey().accept(quest, data.toArray());

            });
        }
        invocation.proceed();
    }

}




