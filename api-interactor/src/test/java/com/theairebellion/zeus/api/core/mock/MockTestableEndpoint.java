package com.theairebellion.zeus.api.core.mock;

import com.theairebellion.zeus.api.core.Endpoint;
import io.restassured.specification.RequestSpecification;

import static org.mockito.Mockito.mock;

/**
 * Abstract base class for testable mock endpoints.
 * Overrides defaultConfiguration to avoid calling RestAssured directly.
 */
public abstract class MockTestableEndpoint implements Endpoint {

   private final RequestSpecification mockRequestSpec = mock(RequestSpecification.class);

   @Override
   public RequestSpecification defaultConfiguration() {
      // Return a mock instead of calling RestAssured.given()
      return mockRequestSpec;
   }
}

