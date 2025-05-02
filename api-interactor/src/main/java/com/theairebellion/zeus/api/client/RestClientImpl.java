package com.theairebellion.zeus.api.client;

import com.theairebellion.zeus.api.log.LogApi;
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

import static com.theairebellion.zeus.api.config.ApiConfigHolder.getApiConfig;
import static com.theairebellion.zeus.api.log.LogApi.extended;
import static com.theairebellion.zeus.api.log.LogApi.step;

/**
 * Implementation of {@code RestClient} for executing REST API requests.
 * <p>
 * This class provides an abstraction for sending HTTP requests using RestAssured,
 * logging request/response details, and handling execution logic.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
@NoArgsConstructor
public class RestClientImpl implements RestClient {

    private static final long SLOW_REQUEST_THRESHOLD_MS = 2000;
    private static final String LOG_TEMPLATE_RESPONSE_BODY = "Response body: {}.";

    /**
     * This configuration provides settings for API request execution, including logging behavior,
     * response body truncation, and other configurable options.
     */
    private static final Map<Method, Function<RequestSpecification, Response>> METHOD_EXECUTORS = Map.of(
            Method.GET, RequestSpecification::get,
            Method.POST, RequestSpecification::post,
            Method.PUT, RequestSpecification::put,
            Method.DELETE, RequestSpecification::delete,
            Method.PATCH, RequestSpecification::patch,
            Method.HEAD, RequestSpecification::head
    );

    /**
     * Hook for measuring time—override in tests to simulate slow calls.
     */
    protected long currentTimeNanos() {
        return System.nanoTime();
    }

    /**
     * Executes an API request with the specified request specification and HTTP method.
     * <p>
     * This method logs the request details, sends the request, logs the response,
     * and returns the response.
     * </p>
     *
     * @param spec   The {@code RequestSpecification} containing request details.
     * @param method The HTTP method to use for the request.
     * @return The {@code Response} received from the server.
     * @throws IllegalArgumentException If the request specification is not a {@code FilterableRequestSpecification}
     *                                  or if the HTTP method is not supported.
     */
    @Override
    public Response execute(final RequestSpecification spec, final Method method) {
        if (method == null) {
            throw new IllegalArgumentException("HTTP method must not be null");
        }

        if (!(spec instanceof FilterableRequestSpecification filterableSpec)) {
            throw new IllegalArgumentException("RequestSpecification is not of type FilterableRequestSpecification");
        }

        String url = filterableSpec.getURI();
        String methodName = method.name();

        String rawRequestBody = Optional.ofNullable(filterableSpec.getBody()).map(Object::toString).orElse(null);
        String prettyRequestBody = tryPrettyPrintJson(rawRequestBody);

        String requestHeaders = Optional.ofNullable(filterableSpec.getHeaders())
                .map(Object::toString)
                .orElse("");

        printRequest(methodName, url, prettyRequestBody, requestHeaders);

        long startTime = currentTimeNanos();

        Response response = Optional.ofNullable(METHOD_EXECUTORS.get(method))
                .orElseThrow(() -> new IllegalArgumentException("HTTP method " + method + " is not supported"))
                .apply(spec);

        long duration = (currentTimeNanos() - startTime) / 1_000_000;
        printResponse(methodName, url, response, duration);

        if (duration > SLOW_REQUEST_THRESHOLD_MS) {
            LogApi.warn("Request to endpoint {}-{} took too long: {}ms.", methodName, url, duration);
        }

        return response;
    }


    /**
     * Logs the request details before execution.
     *
     * @param methodName The HTTP method used for the request.
     * @param finalUrl   The full request URL.
     * @param body       The request body (if applicable).
     * @param headers    The request headers.
     */
    protected void printRequest(final String methodName, final String finalUrl, String body, String headers) {
        step("Sending request to endpoint {}-{}.", methodName, finalUrl);
        extended("Request body: {}.", body != null ? body : "");
        extended("Request headers: {}.", headers != null ? headers : "");
    }

    /**
     * Logs the response details after execution.
     *
     * @param methodName The HTTP method used for the request.
     * @param finalUrl   The full request URL.
     * @param response   The response received from the server.
     * @param duration   The duration of the request execution in milliseconds.
     */
    protected void printResponse(final String methodName, final String finalUrl, final Response response,
                                 final long duration) {
        step("Response with status: {} received from endpoint: {}-{} in {}ms.",
                response.getStatusCode(), methodName, finalUrl, duration);

        if (response.body() != null) {
            String bodyStr = response.body().asPrettyString();
            if (getApiConfig().logFullBody()) {
                extended(LOG_TEMPLATE_RESPONSE_BODY, bodyStr);
            } else {
                int limit = Math.min(bodyStr.length(), getApiConfig().shortenBody());
                extended(LOG_TEMPLATE_RESPONSE_BODY, bodyStr.substring(0, limit) + (bodyStr.length() > limit ? "..." : ""));
            }
        } else {
            extended(LOG_TEMPLATE_RESPONSE_BODY, "");
        }

        extended("Response headers: {}.", response.getHeaders() != null ? response.getHeaders().toString() : "");
    }

    /**
     * Attempts to pretty-print JSON request bodies.
     *
     * @param content The raw JSON string.
     * @return The formatted JSON string if valid, otherwise returns the original content.
     */
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
