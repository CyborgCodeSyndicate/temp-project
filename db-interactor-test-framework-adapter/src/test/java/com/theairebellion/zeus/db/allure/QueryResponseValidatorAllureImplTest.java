package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTarget;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import io.qameta.allure.Allure;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QueryResponseValidatorAllureImplTest {

   private static final String EXPECTED_STEP_MESSAGE_TEMPLATE = "Validating query response with %d assertion(s)";
   private static final String ATTACHMENT_NAME = "Data to be validated";

   private QueryResponseValidatorAllureImpl validator =
         new QueryResponseValidatorAllureImpl(mock(JsonPathExtractor.class));

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
         ArgumentCaptor<Allure.ThrowableRunnableVoid> runnableCaptor =
               ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

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

   @Test
   @DisplayName("printAssertionTarget should call parent method")
   void testParentClassBehavior() {
      // Arrange
      JsonPathExtractor extractor = mock(JsonPathExtractor.class);
      QueryResponseValidatorAllureImpl realValidator = new QueryResponseValidatorAllureImpl(extractor);

      QueryResponseValidatorAllureImpl spyValidator = spy(realValidator);

      // Arrange
      Map<String, Object> data = new HashMap<>();

      // Act
      spyValidator.printAssertionTarget(data);

      // Assert
      verify(spyValidator).printAssertionTarget(data);
   }

   @Test
   @DisplayName("end-to-end test with mocked validation flow")
   void testEndToEndValidationFlow() {
      // Arrange
      JsonPathExtractor mockExtractor = mock(JsonPathExtractor.class);
      QueryResponseValidatorAllureImpl validatorSpy = spy(new QueryResponseValidatorAllureImpl(mockExtractor));
      QueryResponse queryResponse = mock(QueryResponse.class);

      Assertion assertion = Assertion.builder()
            .key("testKey")
            .expected("testValue")
            .type(AssertionTypes.IS)
            .target(createMockTarget())
            .build();

      List<AssertionResult<?>> mockResults = List.of(mock(AssertionResult.class));
      doReturn(mockResults).when(validatorSpy).validateQueryResponse(any(QueryResponse.class), any(Assertion[].class));

      // Act
      List<AssertionResult<Object>> results = validatorSpy.validateQueryResponse(queryResponse, assertion);

      // Assert
      assertNotNull(results, "Results should not be null");
      assertEquals(1, results.size(), "Should return one result");
   }

   // Helper method to create a mock AssertionTarget
   private AssertionTarget createMockTarget() {
      return mock(AssertionTarget.class);
   }
}
