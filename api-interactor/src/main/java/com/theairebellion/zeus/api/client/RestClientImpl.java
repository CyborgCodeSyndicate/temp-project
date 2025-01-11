package com.theairebellion.zeus.api.client;

import com.theairebellion.zeus.api.log.LogAPI;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RestClientImpl implements RestClient {


    @Override
    public Response execute(final RequestSpecification spec, final Method method) {
        long startTime = System.currentTimeMillis();
        FilterableRequestSpecification filterableSpec = (FilterableRequestSpecification) spec;

        String url = filterableSpec.getURI();
        String methodName = method.name();

        String rawRequestBody = filterableSpec.getBody() != null ? filterableSpec.getBody().toString() : null;
        String prettyRequestBody = tryPrettyPrintJson(rawRequestBody);

        String requestHeaders = filterableSpec.getHeaders() != null
                                    ? filterableSpec.getHeaders().toString()
                                    : "";

        printRequest(methodName, url, prettyRequestBody, requestHeaders);

        Response response = switch (method) {
            case GET -> spec.get();
            case PUT -> spec.put();
            case POST -> spec.post();
            case DELETE -> spec.delete();
            case HEAD -> spec.head();
            case PATCH -> spec.patch();
            default -> throw new UnsupportedOperationException("Method not supported");
        };
        long duration = System.currentTimeMillis() - startTime;

        printResponse(methodName, url, response, duration);
        return response;
    }


    protected void printRequest(final String methodName, final String finalUrl, String body, String headers) {
        LogAPI.step("Sending request to endpoint {}-{}.", methodName, finalUrl);
        LogAPI.extended("Request body: {}.", body != null ? body : "");
        LogAPI.extended("Request headers: {}.", headers != null ? headers : "");
    }


    protected void printResponse(final String methodName, final String finalUrl, final Response response,
                               final long duration) {
        LogAPI.step("Response with status: {} received from endpoint: {}-{} in {}ms.",
            response.getStatusCode(), methodName, finalUrl, duration);
        LogAPI.extended("Response body: {}.", response.body() != null ? response.body().prettyPrint() : "");
        LogAPI.extended("Response headers: {}.", response.getHeaders() != null ? response.getHeaders().toString() : "");
    }


    private String tryPrettyPrintJson(String content) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }
        try {
            return new JsonPath(content).prettyPrint();
        } catch (Exception e) {
            return content;
        }
    }

}
