package com.theairebellion.zeus.ui.components.interceptor;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an API response intercepted during UI tests.
 *
 * <p>This class stores the URL, HTTP status code, and response body of an intercepted
 * network request. It is primarily used for validating API responses during UI tests.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public class ApiResponse {

   private final String url;
   private final String method;
   private final int status;

   @Setter
   private String body;

   /**
    * Constructs an {@code ApiResponse} with the specified URL and HTTP status.
    *
    * @param url    the URL of the intercepted request.
    * @param method the HTTP method of the intercepted request.
    * @param status the HTTP status code of the response.
    */
   public ApiResponse(final String url, final String method, final int status) {
      this.url = url;
      this.method = method;
      this.status = status;
   }
}
