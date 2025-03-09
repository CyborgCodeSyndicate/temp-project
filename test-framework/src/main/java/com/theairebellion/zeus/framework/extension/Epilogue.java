package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.framework.util.TestContextManager;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.theairebellion.zeus.framework.storage.StorageKeysTest.INTERCEPTED_REQUESTS_KEY;
import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;

@Order(Integer.MAX_VALUE)
public class Epilogue extends TestContextManager implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        setUpTestMetadata(context, getSuperQuest(context));
        SuperQuest superQuest = getSuperQuest(context);
        Map<Enum<?>, LinkedList<Object>> arguments = superQuest.getStorage().sub(StorageKeysTest.ARGUMENTS).getData();
        String htmlContent = generateHtmlContent(arguments);
        superQuest.getStorage().sub(StorageKeysTest.ALLURE_DESCRIPTION).put(StorageKeysTest.HTML, htmlContent);
        Throwable throwable = context.getExecutionException().orElse(null);
        String status = (throwable == null) ? "SUCCESS" : "FAILED";
        long startTime = context.getStore(ExtensionContext.Namespace.GLOBAL).get(START_TIME, long.class);
        long durationInSeconds = (System.currentTimeMillis() - startTime) / 1000;
        logTestOutcome(context.getDisplayName(), status, durationInSeconds, throwable);
        CustomAllureListener.stopParentStep();
        CustomAllureListener.startParentStep("Tear Down", CustomAllureListener.StepType.SUCCESS);
        attachFilteredLogsToAllure(ThreadContext.get("testName"));
        if (isUITest(context)) {
            List<byte[]> screenshotBytes = superQuest.getStorage()
                    .sub(StorageKeysTest.ALLURE_DESCRIPTION)
                    .getAllByClass(StorageKeysTest.HTML, byte[].class);
            Allure.addAttachment("Screenshot for " + ThreadContext.get("testName"), new ByteArrayInputStream(screenshotBytes.get(0)));
            List<Object> storedResponses = superQuest.getStorage().sub(StorageKeysTest.INTERCEPTED_REQUESTS)
                    .getAllByClass(INTERCEPTED_REQUESTS_KEY, Object.class);
            if (!storedResponses.isEmpty()) {
                Allure.addAttachment("Intercepted Requests", "text/html", new ByteArrayInputStream(formatResponses(storedResponses).getBytes(StandardCharsets.UTF_8)),
                        ".html");

            }
        }
        ThreadContext.remove("testName");
        setDescription(getSuperQuest(context));
        CustomAllureListener.stopParentStep();
    }

}
