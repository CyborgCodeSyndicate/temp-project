package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.api.log.LogApi;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implements response validation for API tests.
 *
 * <p>This class validates HTTP responses against assertions, ensuring compliance with expected
 * values in status codes, headers, and body content.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Component
@NoArgsConstructor
public class RestResponseValidatorImpl implements RestResponseValidator {

   /**
    * Validates an API response against the provided assertions.
    *
    * <p>Extracts response status, headers, and body values based on defined assertions and
    * performs validation using {@link AssertionUtil}.
    *
    * @param response   The API response to validate.
    * @param assertions The assertions used to verify response correctness.
    * @param <T>        The expected data type for assertion validation.
    * @return A list of assertion results indicating pass or failure for each assertion.
    * @throws IllegalArgumentException  If response or assertions are null.
    * @throws InvalidAssertionException If assertion targets or keys are invalid.
    */
   @Override
   @SuppressWarnings("unchecked")
   public <T> List<AssertionResult<T>> validateResponse(final Response response, Assertion... assertions) {
      LogApi.info("Starting response validation with {} assertion(s).", assertions.length);
      Map<String, T> data = new HashMap<>();

      for (Assertion assertion : assertions) {
         Object target = assertion.getTarget();
         if (!(target instanceof RestAssertionTarget restTarget)) {
            throw new InvalidAssertionException("Invalid or unknown assertion target: " + target);
         }

         switch (restTarget) {
            case STATUS -> handleStatusAssertion(response, data, assertion);
            case BODY -> handleBodyAssertion(response, data, assertion);
            case HEADER -> handleHeaderAssertion(response, data, assertion);
            default -> throw new InvalidAssertionException("Unhandled assertion target: " + target);
         }
      }

      printAssertionTarget((Map<String, Object>) data);
      return AssertionUtil.validate(data, List.of(assertions));
   }

   /**
    * Logs the extracted validation targets for debugging purposes.
    *
    * @param data The extracted response data mapped to assertion keys.
    */
   protected void printAssertionTarget(Map<String, Object> data) {
      LogApi.extended("Validation target: [{}]", data.toString());
   }


   private <T> void handleStatusAssertion(Response response, Map<String, T> data, Assertion assertion) {
      final String key = "AssertionKeyForStatus";
      data.put(key, (T) Integer.valueOf(response.getStatusCode()));
      assertion.setKey(key);
   }

   private <T> void handleBodyAssertion(Response response, Map<String, T> data, Assertion assertion) {
      String key = assertion.getKey();
      if (key == null) {
         throw new InvalidAssertionException("Assertion must have a non-null key. Key must contain a valid "
               + "JsonPath expression.");
      }

      T value = response.jsonPath().get(key);
      if (value == null) {
         throw new IllegalArgumentException("JsonPath expression: '" + key + "' not found in response body.");
      }

      data.put(key, value);
   }

   private <T> void handleHeaderAssertion(Response response, Map<String, T> data, Assertion assertion) {
      String key = assertion.getKey();
      if (key == null) {
         throw new InvalidAssertionException("Assertion must have a non-null key.");
      }

      String header = response.getHeader(key);
      if (header == null) {
         throw new IllegalArgumentException("Header '" + key + "' not found in response.");
      }

      data.put(key, (T) header);
   }
}
