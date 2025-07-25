package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UiTableValidatorTest {

   @Test
   void testUiTableValidatorInterfaceWithMockImplementation() {
      // Create a mock implementation of UiTableValidator
      UiTableValidator mockValidator = new UiTableValidator() {
         @Override
         public <T> List<AssertionResult<T>> validateTable(Object object, Assertion... assertions) {
            // Create mock AssertionResults
            AssertionResult<T> mockResult1 = new AssertionResult<>(
                  true,
                  "First assertion",
                  null,
                  null,
                  false
            );

            AssertionResult<T> mockResult2 = new AssertionResult<>(
                  false,
                  "Second assertion",
                  null,
                  null,
                  false
            );

            return List.of(mockResult1, mockResult2);
         }
      };

      // Create mock objects
      Object testObject = new Object();
      Assertion mockAssertion1 = Mockito.mock(Assertion.class);
      Assertion mockAssertion2 = Mockito.mock(Assertion.class);

      // Call validateTable
      List<AssertionResult<Object>> results = mockValidator.validateTable(testObject, mockAssertion1, mockAssertion2);

      // Verify results
      assertNotNull(results);
      assertEquals(2, results.size());
      assertTrue(results.get(0).isPassed());
      assertFalse(results.get(1).isPassed());
   }

   @Test
   void testUiTableValidatorWithNoAssertions() {
      // Create a mock implementation of UiTableValidator
      UiTableValidator mockValidator = new UiTableValidator() {
         @Override
         public <T> List<AssertionResult<T>> validateTable(Object object, Assertion... assertions) {
            // Return empty list when no assertions
            return List.of();
         }
      };

      // Create mock object
      Object testObject = new Object();

      // Call validateTable with no assertions
      List<AssertionResult<Object>> results = mockValidator.validateTable(testObject);

      // Verify results
      assertNotNull(results);
      assertTrue(results.isEmpty());
   }
}
