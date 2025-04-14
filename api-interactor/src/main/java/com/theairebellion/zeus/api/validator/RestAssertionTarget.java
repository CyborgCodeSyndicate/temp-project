package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

/**
 * Defines assertion targets for API response validation.
 *
 * <p>This enum categorizes different parts of an API response that can be validated,
 * ensuring structured assertions for response verification.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum RestAssertionTarget implements AssertionTarget {

   /**
    * Validates the HTTP status code of the response.
    */
   STATUS,

   /**
    * Validates the response body content.
    */
   BODY,

   /**
    * Validates specific headers in the response.
    */
   HEADER;

   /**
    * Retrieves the specific assertion target.
    *
    * @return The enum representing the assertion target.
    */
   @Override
   public Enum<?> target() {
      return this;
   }
}
