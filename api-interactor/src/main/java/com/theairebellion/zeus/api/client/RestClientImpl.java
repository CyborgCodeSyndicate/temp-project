package com.theairebellion.zeus.api.client;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.theairebellion.zeus.api.log.LogAPI.extended;
import static com.theairebellion.zeus.api.log.LogAPI.step;

@Component
@NoArgsConstructor
public class RestClientImpl implements RestClient {

    private static final Map<Method, Function<RequestSpecification, Response>> METHOD_EXECUTORS = Map.of(
        Method.GET, RequestSpecification::get,
        Method.POST, RequestSpecification::post,
        Method.PUT, RequestSpecification::put,
        Method.DELETE, RequestSpecification::delete,
        Method.PATCH, RequestSpecification::patch,
        Method.HEAD, RequestSpecification::head
    );


    @Override
    public Response execute(final RequestSpecification spec, final Method method) {
        if (!(spec instanceof FilterableRequestSpecification filterableSpec)) {
            throw new IllegalArgumentException("RequestSpecification is not of type FilterableRequestSpecification");
        }

        long startTime = System.nanoTime();

        String url = filterableSpec.getURI();
        String methodName = method.name();

        String rawRequestBody = Optional.ofNullable(filterableSpec.getBody()).map(Object::toString).orElse(null);
        String prettyRequestBody = tryPrettyPrintJson(rawRequestBody);

        String requestHeaders = Optional.ofNullable(filterableSpec.getHeaders())
                                    .map(Object::toString)
                                    .orElse("");

        printRequest(methodName, url, prettyRequestBody, requestHeaders);

        Response response = Optional.ofNullable(METHOD_EXECUTORS.get(method))
                                .orElseThrow(() -> new IllegalArgumentException("HTTP method " + method + " is not supported"))
                                .apply(spec);

        long duration = (System.nanoTime() - startTime) / 1_000_000;
        printResponse(methodName, url, response, duration);

        return response;
    }


    protected void printRequest(final String methodName, final String finalUrl, String body, String headers) {
        step("Sending request to endpoint {}-{}.", methodName, finalUrl);
        extended("Request body: {}.", body != null ? body : "");
        extended("Request headers: {}.", headers != null ? headers : "");
    }


    protected void printResponse(final String methodName, final String finalUrl, final Response response,
                               final long duration) {
        step("Response with status: {} received from endpoint: {}-{} in {}ms.",
            response.getStatusCode(), methodName, finalUrl, duration);
        extended("Response body: {}.", response.body() != null ? response.body().asPrettyString() : "");
        extended("Response headers: {}.", response.getHeaders() != null ? response.getHeaders().toString() : "");
    }


    protected String tryPrettyPrintJson(String content) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }
        try {
            return new JsonPath(content).prettify();
        } catch (Exception e) {
            return content;
        }
    }

}
