package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.client.RestClientImpl;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

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


    @Override
    protected void printRequest(final String methodName, final String finalUrl, final String body,
                                final String headers) {
        super.printRequest(methodName, finalUrl, body, headers);
        logRequestDetails(methodName, finalUrl, headers, body);
    }


    @Override
    protected void printResponse(final String methodName, final String finalUrl, final Response response,
                                 final long duration) {
        super.printResponse(methodName, finalUrl, response, duration);
        logResponseDetails(methodName, finalUrl, response, duration);
    }


    private void logRequestDetails(String methodName, String url, String headers, String body) {
        Allure.step(String.format("Sending request to endpoint %s-%s.", methodName, url), () -> {
            addAttachmentIfPresent(ATTACHMENT_HTTP_METHOD, methodName);
            addAttachmentIfPresent(ATTACHMENT_URL, url);
            addQueryParamsAttachment(url);
            addAttachmentIfPresent(ATTACHMENT_HEADERS, headers);
            addAttachmentIfPresent(ATTACHMENT_REQUEST_BODY, body);
        });
    }


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
            addAttachmentIfPresent(ATTACHMENT_RESPONSE_HEADERS, response.getHeaders().toString());
            addAttachmentIfPresent(ATTACHMENT_RESPONSE_BODY, response.getBody().prettyPrint());
        });
    }


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
