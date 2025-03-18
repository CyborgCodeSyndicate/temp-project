package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.theairebellion.zeus.api.config.ApiConfigHolder.getApiConfig;

@InfoAIClass(
    description = "Interface representing endpoints. Should be implemented as enum and methods to be overridden",
    creationType = CreationType.ENUM,
    useAsKeyInStorage = true)
public interface Endpoint {

    Method method();

    String url();

    Enum<?> enumImpl();

    default String baseUrl() {
        return getApiConfig().baseUrl();
    }

    default Map<String, List<String>> headers() {
        return Collections.emptyMap();
    }

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

    default RequestSpecification prepareRequestSpec(Object body) {
        validateEndpoint();
        RequestSpecification spec = defaultConfiguration().basePath(url());
        if (body != null) {
            spec.body(body);
        }
        return spec;
    }

    default Endpoint withQueryParam(String key, Object value) {
        return new ParametrizedEndpoint(this).withQueryParam(key, value);
    }

    default Endpoint withPathParam(String key, Object value) {
        return new ParametrizedEndpoint(this).withPathParam(key, value);
    }

    default Endpoint withHeader(String key, String value) {
        return new ParametrizedEndpoint(this).withHeader(key, value);
    }

    default Endpoint withHeader(String key, List<String> values) {
        return new ParametrizedEndpoint(this).withHeader(key, values);
    }


    private void validateEndpoint() {
        if (url() == null || url().isEmpty()) {
            throw new IllegalStateException("URL must not be null or empty for endpoint: " + enumImpl().name());
        }
        if (method() == null) {
            throw new IllegalStateException("HTTP method must not be null for endpoint: " + enumImpl().name());
        }
    }

    // default String aiPrint() {
    //     return enumImpl().name() + "{" +
    //                "method=" + method() +
    //                ", url='" + url() + '\'' +
    //                '}';
    // }

}
