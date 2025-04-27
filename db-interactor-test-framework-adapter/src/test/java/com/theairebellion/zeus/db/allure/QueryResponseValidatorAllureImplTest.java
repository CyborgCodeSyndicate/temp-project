package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryResponseValidatorAllureImplTest {

    private static final String EXPECTED_STEP_MESSAGE_TEMPLATE = "Validating query response with %d assertion(s)";
    private static final String ATTACHMENT_NAME = "Data to be validated";

    private final QueryResponseValidatorAllureImpl validator = new QueryResponseValidatorAllureImpl(mock(JsonPathExtractor.class));

    @Test
    @DisplayName("printAssertionTarget should add data to Allure report")
    void testPrintAssertionTarget() throws Throwable {
        // Arrange
        Map<String, Object> data = Map.of("key", "value");
        String expectedStepMessage = String.format(EXPECTED_STEP_MESSAGE_TEMPLATE, data.size());

        // Access the protected method using reflection
        Method method = QueryResponseValidatorAllureImpl.class.getDeclaredMethod("printAssertionTarget", Map.class);
        method.setAccessible(true);

        try (MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor = ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

            // Act
            method.invoke(validator, data);

            // Assert: verify that Allure.step is called with the expected message and a runnable.
            allureMock.verify(() -> Allure.step(messageCaptor.capture(), runnableCaptor.capture()), times(1));
            assertEquals(expectedStepMessage, messageCaptor.getValue(), "Step message should match expected");

            runnableCaptor.getValue().run();

            // Verify that the attachment is added.
            allureMock.verify(() -> Allure.addAttachment(eq(ATTACHMENT_NAME), eq(data.toString())));
        }
    }
}
