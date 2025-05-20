package com.theairebellion.zeus.validator.util;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import com.theairebellion.zeus.validator.registry.AssertionRegistry;
import java.util.List;
import java.util.Map;

/**
 * Utility class for executing and validating assertions against provided data.
 *
 * <p>This class processes assertions, validates input data, and applies the appropriate validation
 * logic. It ensures compatibility between expected and actual values before evaluation.
 *
 * <p>Assertions are applied based on registered validation functions, and the results
 * are returned as {@link AssertionResult} objects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class AssertionUtil {

   private AssertionUtil() {
   }


   /**
    * Validates a set of assertions against a given data map.
    *
    * @param data       The data map containing key-value pairs to validate against.
    * @param assertions The list of assertions to be applied.
    * @param <T>        The type of values being validated.
    * @return A list of {@link AssertionResult} objects representing validation outcomes.
    * @throws IllegalArgumentException if the data map is null or no assertions are provided.
    */
   public static <T> List<AssertionResult<T>> validate(Map<String, T> data, List<Assertion> assertions) {
      if (data == null) {
         throw new IllegalArgumentException("The data map cannot be null.");
      }
      if (assertions == null || assertions.isEmpty()) {
         throw new IllegalArgumentException("At least one assertion must be provided.");
      }

      return assertions.stream().map(assertion -> validateSingle(data, assertion)).toList();
   }


   /**
    * Validates a single assertion against the data map.
    *
    * @param data      The data map containing key-value pairs.
    * @param assertion The assertion to be validated.
    * @param <T>       The type of the value being validated.
    * @return An {@link AssertionResult} indicating whether the assertion passed or failed.
    * @throws IllegalArgumentException if the key does not exist or has a null value.
    */
   private static <T> AssertionResult<T> validateSingle(Map<String, T> data, Assertion assertion) {
      validateAssertion(assertion);

      String key = assertion.getKey();
      T actualValue = data.get(key);

      if (actualValue == null) {
         throw new IllegalArgumentException(
               String.format("Key '%s' in assertion does not exist or has a null value in the data map.", key)
         );
      }

      AssertionType<?> type = assertion.getType();
      validateTypeCompatibility(type, actualValue);

      var validator = AssertionRegistry.getValidator(type);
      boolean passed = validator.test(actualValue, assertion.getExpected());

      return new AssertionResult<>(
            passed,
            type.type().name(),
            assertion.getExpected(),
            actualValue,
            assertion.isSoft()
      );
   }


   /**
    * Ensures that the given assertion is valid.
    *
    * @param assertion The assertion to validate.
    * @throws InvalidAssertionException if any required field in the assertion is missing or null.
    */
   private static void validateAssertion(Assertion assertion) {
      if (assertion == null) {
         throw new InvalidAssertionException("Assertion cannot be null.");
      }

      if (assertion.getKey() == null || assertion.getKey().isEmpty()) {
         throw new InvalidAssertionException("Assertion must have a non-empty key.");
      }
   }


   /**
    * Validates type compatibility between an assertion type and the actual value.
    *
    * @param type        The assertion type.
    * @param actualValue The actual value to be validated.
    * @throws IllegalArgumentException if the actual value type does not match the expected type.
    */
   private static void validateTypeCompatibility(AssertionType<?> type, Object actualValue) {
      Class<?> supportedType = type.getSupportedType();
      Class<?> actualValueClass = actualValue.getClass();

      if (!supportedType.isAssignableFrom(actualValueClass)) {
         throw new IllegalArgumentException(
               String.format(
                     "Assertion type '%s' is not compatible with the actual value type '%s'.",
                     type, actualValueClass.getName()
               )
         );
      }
   }

}
