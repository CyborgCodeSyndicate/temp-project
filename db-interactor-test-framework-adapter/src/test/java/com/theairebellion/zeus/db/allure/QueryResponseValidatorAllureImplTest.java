package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class QueryResponseValidatorAllureImplTest {

    private static final String STEP_MESSAGE = "Collected data to be validated";
    private static final String ATTACHMENT_NAME = "Data to be validated";
    private static final String STEP_ANNOTATION_MESSAGE = "Validating response with {assertions.length} assertion(s)";

    @Test
    void testPrintAssertionTarget() throws Throwable {
        JsonPathExtractor extractor = mock(JsonPathExtractor.class);
        QueryResponseValidatorAllureImpl validator = new QueryResponseValidatorAllureImpl(extractor);
        Map<String, Object> data = Map.of("key", "value");

        Method method = QueryResponseValidatorAllureImpl.class.getDeclaredMethod("printAssertionTarget", Map.class);
        method.setAccessible(true);

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> stepCaptor = ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            method.invoke(validator, data);

            allureMock.verify(() -> Allure.step(eq(STEP_MESSAGE), stepCaptor.capture()));
            stepCaptor.getValue().run();
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_NAME), eq(data.toString())));
        }
    }

    @Test
    void testStepAnnotationParameters() throws Exception {
        Method method = QueryResponseValidatorAllureImpl.class.getDeclaredMethod("printAssertionTarget", Map.class);
        Step stepAnnotation = method.getAnnotation(Step.class);
        assertEquals(STEP_ANNOTATION_MESSAGE, stepAnnotation.value());
    }

    @Test
    void testParentClassBehavior() throws Exception {
        JsonPathExtractor extractor = mock(JsonPathExtractor.class);
        QueryResponseValidatorAllureImpl validator = spy(new QueryResponseValidatorAllureImpl(extractor));
        Map<String, Object> data = new HashMap<>();

        Method method = QueryResponseValidatorAllureImpl.class.getDeclaredMethod("printAssertionTarget", Map.class);
        method.setAccessible(true);

        try (MockedStatic<Allure> ignored = mockStatic(Allure.class)) {
            method.invoke(validator, data);
            verify(validator).printAssertionTarget(data);
        }
    }
}