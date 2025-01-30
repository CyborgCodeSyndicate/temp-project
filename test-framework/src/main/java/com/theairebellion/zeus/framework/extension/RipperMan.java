package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

import static com.theairebellion.zeus.framework.config.FrameworkConfigHolder.getFrameworkConfig;
import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;

public class RipperMan implements AfterTestExecutionCallback {


    @Override
    public void afterTestExecution(final ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            Ripper annotation = method.getAnnotation(Ripper.class);
            if (annotation != null) {
                executeRipperLogic(context, annotation.targets());
            }
        });
    }


    private void executeRipperLogic(ExtensionContext context, String[] targets) {
        LogTest.info("The ripper has arrived");

        @Jailbreak Quest quest = (Quest) context.getStore(ExtensionContext.Namespace.GLOBAL).get(QUEST);
        if (quest == null) {
            throw new IllegalStateException("Quest not found in the global store");
        }

        quest.getStorage().sub(StorageKeysTest.ARGUMENTS).joinLateArguments();

        Arrays.stream(targets).forEach(target -> {
            DataRipper dataRipper = ReflectionUtil.findEnumImplementationsOfInterface(
                DataRipper.class, target, getFrameworkConfig().projectPackage());

            dataRipper.eliminate().accept(quest);
            LogTest.info("DataRipper processed target: '{}'.", target);
        });
    }


}

