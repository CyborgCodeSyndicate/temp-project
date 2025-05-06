package com.theairebellion.zeus.api.core;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.theairebellion.zeus.api.config.ApiConfigHolder.getApiConfig;

/**
 * Represents an API endpoint definition.
 *
 * <p>This interface defines the essential properties and methods for interacting with an API endpoint.
 * It supports request configuration, query and path parameter management, and automatic request logging.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface Endpoint<T extends Enum<T>> {

   /**
    * Retrieves the HTTP method associated with this endpoint.
    *
    * @return The HTTP method of the endpoint.
    */
   Method method();

   /**
    * Retrieves the relative URL path of this endpoint.
    *
    * @return The endpoint URL.
    */
   String url();

   /**
    * Retrieves the enum implementation of this endpoint.
    *
    * @return The enum representing this endpoint.
    */
   T enumImpl();

   /**
    * Retrieves the base URL for this endpoint.
    *
    * @return The base URL from the API configuration.
    */
   default String baseUrl() {
      return getApiConfig().baseUrl();
   }

   /**
    * Retrieves the headers associated with this endpoint.
    *
    * <p>By default, this returns an empty map unless overridden.
    *
    * @return A map containing header key-value pairs.
    */
   default Map<String, List<String>> headers() {
      return Collections.emptyMap();
   }

   /**
    * Prepares a default {@link RequestSpecification} with base configuration.
    *
    * <p>Configures base URI, headers, and logging based on API configuration.
    *
    * @return The configured request specification.
    */
   default RequestSpecification defaultConfiguration() {
      RequestSpecification spec = RestAssured.given()
            .baseUri(baseUrl())
            .headers(headers());

      if (getApiConfig().restAssuredLoggingEnabled()) {
         switch (getApiConfig().restAssuredLoggingLevel()) {
            case "BASIC" -> spec.log().ifValidationFails();
            case "ALL" -> spec.log().all();
            case "NONE" -> { /* No logging */ }
            default -> throw new IllegalArgumentException("Unsupported logging level");
         }
      }

      return spec;
   }

   /**
    * Prepares a request specification for the endpoint, optionally including a request body.
    *
    * @param body The request body to be sent with the request.
    * @return A configured {@link RequestSpecification} instance.
    */
   default RequestSpecification prepareRequestSpec(Object body) {
      validateEndpoint();
      RequestSpecification spec = defaultConfiguration().basePath(url());
      if (body != null) {
         spec.body(body);
      }
      return spec;
   }

   /**
    * Creates a new instance of this endpoint with an added query parameter.
    *
    * @param key   The query parameter name.
    * @param value The query parameter value.
    * @return A new instance of the endpoint with the query parameter added.
    */
   default Endpoint<T> withQueryParam(String key, Object value) {
      return new ParametrizedEndpoint<>(this).withQueryParam(key, value);
   }

   /**
    * Creates a new instance of this endpoint with an added path parameter.
    *
    * @param key   The path parameter name.
    * @param value The path parameter value.
    * @return A new instance of the endpoint with the path parameter added.
    */
   default Endpoint<T> withPathParam(String key, Object value) {
      return new ParametrizedEndpoint<>(this).withPathParam(key, value);
   }

   /**
    * Creates a new instance of this endpoint with an added request header.
    *
    * @param key   The header name.
    * @param value The header value.
    * @return A new instance of the endpoint with the header added.
    */
   default Endpoint<T> withHeader(String key, String value) {
      return new ParametrizedEndpoint<>(this).withHeader(key, value);
   }

   /**
    * Creates a new instance of this endpoint with an added request header supporting multiple values.
    *
    * @param key    The header name.
    * @param values A list of values for the header.
    * @return A new instance of the endpoint with the multi-value header added.
    */
   default Endpoint<T> withHeader(String key, List<String> values) {
      return new ParametrizedEndpoint<>(this).withHeader(key, values);
   }

   /**
    * Validates that the endpoint has the necessary configurations set.
    *
    * <p>Ensures that the URL and HTTP method are not null or empty before making a request.
    *
    * @throws IllegalStateException if the URL or method is missing.
    */
   private void validateEndpoint() {
      if (url() == null || url().isEmpty()) {
         throw new IllegalStateException("URL must not be null or empty for endpoint: " + enumImpl().name());
      }
      if (method() == null) {
         throw new IllegalStateException("HTTP method must not be null for endpoint: " + enumImpl().name());
      }
   }
}
