package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.util.ObjectFormatter;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.theairebellion.zeus.framework.allure.StepType.TEAR_DOWN;
import static com.theairebellion.zeus.framework.storage.StoreKeys.HTML;
import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static com.theairebellion.zeus.framework.util.AllureStepHelper.attachFilteredLogsToAllure;
import static com.theairebellion.zeus.framework.util.AllureStepHelper.logTestOutcome;
import static com.theairebellion.zeus.framework.util.AllureStepHelper.setDescription;
import static com.theairebellion.zeus.framework.util.AllureStepHelper.setUpTestMetadata;
import static com.theairebellion.zeus.framework.util.TestContextManager.getSuperQuest;

@Order(Integer.MAX_VALUE)
public class Epilogue implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        setUpTestMetadata(context);
        SuperQuest superQuest = getSuperQuest(context);
        Map<Enum<?>, LinkedList<Object>> arguments = superQuest.getStorage().sub(StorageKeysTest.ARGUMENTS).getData();
        String htmlContent = new ObjectFormatter().generateHtmlContent(arguments);
        List<String> htmlList = context.getStore(ExtensionContext.Namespace.GLOBAL).get(HTML, List.class);
        if (htmlList == null) {
            htmlList = new ArrayList<>();
        }
        htmlList.add(htmlContent);
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(HTML, htmlList);
        Throwable throwable = context.getExecutionException().orElse(null);
        String status = (throwable == null) ? "SUCCESS" : "FAILED";
        long startTime = context.getStore(ExtensionContext.Namespace.GLOBAL).get(START_TIME, long.class);
        long durationInSeconds = (System.currentTimeMillis() - startTime) / 1000;
        logTestOutcome(context.getDisplayName(), status, durationInSeconds, throwable);
        if(!CustomAllureListener.isParentStepActive(TEAR_DOWN)) {
            CustomAllureListener.stopParentStep();
            CustomAllureListener.startParentStep(TEAR_DOWN);
        }
        attachFilteredLogsToAllure(ThreadContext.get("testName"));
        ThreadContext.remove("testName");
        setDescription(context);
        CustomAllureListener.stopParentStep();
    }
}
