package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.client.RestClientImpl;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

/**
 * Enhances {@link RestClientImpl} with Allure reporting capabilities.
 *
 * <p>This class logs API request and response details to Allure, attaching
 * HTTP method, headers, body, response time, status codes, and other key
 * request/response metadata.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Component
public class RestClientAllureImpl extends RestClientImpl {

   private static final String ATTACHMENT_HTTP_METHOD = "HTTP Method";
   private static final String ATTACHMENT_URL = "URL";
   private static final String ATTACHMENT_HEADERS = "Headers";
   private static final String ATTACHMENT_REQUEST_BODY = "Request Body";
   private static final String ATTACHMENT_RESPONSE_TIME = "Response Time (ms)";
   private static final String ATTACHMENT_STATUS_CODE = "Status Code";
   private static final String ATTACHMENT_RESPONSE_HEADERS = "Response Headers";
   private static final String ATTACHMENT_RESPONSE_BODY = "Response Body";
   private static final int MAX_BODY_LENGTH = 10_000;

   /**
    * Logs API request details and attaches them to Allure reports.
    *
    * @param methodName The HTTP method used for the request.
    * @param finalUrl   The request URL.
    * @param body       The request body, if applicable.
    * @param headers    The request headers.
    */
   @Override
   protected void printRequest(final String methodName, final String finalUrl, final String body,
                               final String headers) {
      super.printRequest(methodName, finalUrl, body, headers);
      logRequestDetails(methodName, finalUrl, headers, body);
   }

   /**
    * Logs API response details and attaches them to Allure reports.
    *
    * @param methodName The HTTP method used for the request.
    * @param finalUrl   The request URL.
    * @param response   The received response.
    * @param duration   The time taken to execute the request in milliseconds.
    */
   @Override
   protected void printResponse(final String methodName, final String finalUrl, final Response response,
                                final long duration) {
      super.printResponse(methodName, finalUrl, response, duration);
      logResponseDetails(methodName, finalUrl, response, duration);
   }

   /**
    * Logs request details as Allure attachments.
    *
    * @param methodName The HTTP method used.
    * @param url        The request URL.
    * @param headers    The request headers.
    * @param body       The request body, if applicable.
    */
   private void logRequestDetails(String methodName, String url, String headers, String body) {
      Allure.step(String.format("Sending request to endpoint %s-%s.", methodName, url), () -> {
         addAttachmentIfPresent(ATTACHMENT_HTTP_METHOD, methodName);
         addAttachmentIfPresent(ATTACHMENT_URL, url);
         addQueryParamsAttachment(url);
         addAttachmentIfPresent(ATTACHMENT_HEADERS, headers);
         addAttachmentIfPresent(ATTACHMENT_REQUEST_BODY, body);
      });
   }

   /**
    * Logs response details as Allure attachments.
    *
    * @param methodName The HTTP method used.
    * @param url        The request URL.
    * @param response   The API response.
    * @param duration   The execution time in milliseconds.
    */
   private void logResponseDetails(String methodName, String url, Response response, long duration) {
      int statusCode = response.getStatusCode();
      boolean isError = statusCode >= 400;

      String stepTitle = isError
            ? String.format("❌ Error Response: %d from %s-%s in %dms.", statusCode, methodName, url, duration)
            : String.format("✅ Response: %d from %s-%s in %dms.", statusCode, methodName, url, duration);

      Allure.step(stepTitle, () -> {
         addAttachmentIfPresent(ATTACHMENT_HTTP_METHOD, methodName);
         addAttachmentIfPresent(ATTACHMENT_URL, url);
         addAttachmentIfPresent(ATTACHMENT_RESPONSE_TIME, String.valueOf(duration));
         addAttachmentIfPresent(ATTACHMENT_STATUS_CODE, String.valueOf(statusCode));

         // Safely handle null headers
         String headers = response.getHeaders() != null ? response.getHeaders().toString() : "";
         addAttachmentIfPresent(ATTACHMENT_RESPONSE_HEADERS, headers);

         // Safely handle null body
         String body = "";
         if (response.getBody() != null) {
            body = response.getBody().prettyPrint();
         }
         addAttachmentIfPresent(ATTACHMENT_RESPONSE_BODY, body);
      });
   }

   /**
    * Adds an attachment to Allure if the content is not null or empty.
    *
    * @param name    The attachment name.
    * @param content The attachment content.
    */
   private void addAttachmentIfPresent(String name, String content) {
      if (content != null && !content.trim().isEmpty()) {
         if (content.length() > MAX_BODY_LENGTH) {
            content = content.substring(0, MAX_BODY_LENGTH) + "... (truncated)";
         }
         Allure.addAttachment(name, content);
      }
   }

   private void addQueryParamsAttachment(String url) {
      if (url.contains("?")) {
         String queryParams = url.substring(url.indexOf("?") + 1);
         addAttachmentIfPresent("Query Parameters", queryParams);
      }
   }

}
