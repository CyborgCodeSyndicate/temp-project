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
            addAttachmentIfPresent(ATTACHMENT_HEADERS, headers);
            addAttachmentIfPresent(ATTACHMENT_REQUEST_BODY, body);
        });
    }


    private void logResponseDetails(String methodName, String url, Response response, long duration) {
        int statusCode = response.getStatusCode();
        Allure.step(
            String.format("Response with status: %d received from endpoint: %s-%s in %dms.", statusCode, methodName,
                url, duration), () -> {
                addAttachmentIfPresent(ATTACHMENT_HTTP_METHOD, methodName);
                addAttachmentIfPresent(ATTACHMENT_URL, url);
                addAttachmentIfPresent(ATTACHMENT_RESPONSE_TIME, String.valueOf(duration));
                addAttachmentIfPresent(ATTACHMENT_STATUS_CODE, String.valueOf(statusCode));
                addAttachmentIfPresent(ATTACHMENT_RESPONSE_HEADERS, response.getHeaders().toString());
                addAttachmentIfPresent(ATTACHMENT_RESPONSE_BODY, response.getBody().asString());
            });
    }


    private void addAttachmentIfPresent(String name, String content) {
        if (content != null && !content.trim().isEmpty()) {
            Allure.addAttachment(name, content);
        }
    }

}
