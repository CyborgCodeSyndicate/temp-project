package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.validator.RestResponseValidatorImpl;
import io.qameta.allure.Allure;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Enhances {@link RestResponseValidatorImpl} with Allure reporting.
 *
 * <p>This class logs assertion validation details to Allure, providing
 * insights into the response validation process.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Component
public class RestResponseValidatorAllureImpl extends RestResponseValidatorImpl {

   /**
    * Logs validation targets to Allure reports.
    *
    * <p>Overrides {@link RestResponseValidatorImpl#printAssertionTarget(Map)} to attach
    * extracted validation data as an Allure step.
    *
    * @param data The extracted response data mapped to assertion keys.
    */
   @Override
   protected void printAssertionTarget(final Map<String, Object> data) {
      Allure.step("Validating response with " + data.size() + " assertion(s)", () -> {
         super.printAssertionTarget(data);
         Allure.addAttachment("Data to be validated", data.toString());
         if (data.containsKey("statusCode")) {
            Allure.addAttachment("Expected Status Code", data.get("statusCode").toString());
         }
         if (data.containsKey("headers")) {
            Allure.addAttachment("Expected Headers", data.get("headers").toString());
         }
         if (data.containsKey("body")) {
            Allure.addAttachment("Expected Response Body", data.get("body").toString());
         }
      });
   }

}
