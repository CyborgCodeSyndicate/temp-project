package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.config.FrameworkConfig;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import manifold.ext.rt.api.Jailbreak;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

import static com.theairebellion.zeus.framework.storage.StoreKeys.QUEST;

public class RipperMan implements AfterTestExecutionCallback {

    protected static final FrameworkConfig FRAMEWORK_CONFIG = ConfigCache.getOrCreate(FrameworkConfig.class);


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
                DataRipper.class, target, FRAMEWORK_CONFIG.projectPackage());

            dataRipper.eliminate().accept(quest);
            LogTest.info("DataRipper processed target: '{}'.", target);
        });
    }

}

