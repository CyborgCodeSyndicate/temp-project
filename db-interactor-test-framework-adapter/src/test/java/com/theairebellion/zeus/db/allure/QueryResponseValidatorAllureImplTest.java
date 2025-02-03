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

    @Test
    void testPrintAssertionTarget() throws Exception {
        JsonPathExtractor extractor = mock(JsonPathExtractor.class);
        QueryResponseValidatorAllureImpl validator = new QueryResponseValidatorAllureImpl(extractor);
        Map<String, Object> data = Map.of("key", "value");

        Method method = QueryResponseValidatorAllureImpl.class
                .getDeclaredMethod("printAssertionTarget", Map.class);
        method.setAccessible(true);

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<Allure.ThrowableRunnableVoid> stepCaptor =
                    ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            method.invoke(validator, data);

            // Verify step creation
            allureMock.verify(() ->
                    Allure.step(
                            eq("Collected data to be validated"),
                            stepCaptor.capture()
                    )
            );

            // Execute the captured step logic with exception handling
            try {
                stepCaptor.getValue().run();
            } catch (Throwable e) {
                throw new AssertionError("Allure step execution failed", e);
            }

            // Verify attachment creation
            allureMock.verify(() ->
                    Allure.addAttachment(eq("Data to be validated"), eq(data.toString()))
            );
        }
    }

    @Test
    void testStepAnnotationParameters() throws Exception {
        Method method = QueryResponseValidatorAllureImpl.class
                .getDeclaredMethod("printAssertionTarget", Map.class);
        Step stepAnnotation = method.getAnnotation(Step.class);

        assertEquals(
                "Validating response with {assertions.length} assertion(s)",
                stepAnnotation.value(),
                "Step annotation has incorrect message"
        );
    }

    @Test
    void testParentClassBehavior() throws Exception {
        JsonPathExtractor extractor = mock(JsonPathExtractor.class);
        QueryResponseValidatorAllureImpl validator = spy(new QueryResponseValidatorAllureImpl(extractor));
        Map<String, Object> data = new HashMap<>();

        Method method = QueryResponseValidatorAllureImpl.class
                .getDeclaredMethod("printAssertionTarget", Map.class);
        method.setAccessible(true);

        try (MockedStatic<Allure> ignored = mockStatic(Allure.class)) {
            method.invoke(validator, data);
            verify(validator, times(1)).printAssertionTarget(data);
        }
    }
}