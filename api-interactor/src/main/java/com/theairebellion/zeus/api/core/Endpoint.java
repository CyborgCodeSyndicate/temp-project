package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Endpoint {

    ApiConfig apiConfig = ConfigCache.getOrCreate(ApiConfig.class);


    default String baseUrl() {
        return apiConfig.baseUrl();
    }

    Method method();

    String url();

    Enum<?> enumImpl();

    default Map<String, List<String>> headers() {
        return new HashMap<>();
    }

    default RequestSpecification defaultConfiguration() {
        RequestSpecification spec = RestAssured.given()
                                        .baseUri(baseUrl())
                                        .headers(headers());
        if (apiConfig.restAssuredLoggingEnabled()) {
            if (apiConfig.restAssuredLoggingLevel().equals("BASIC")) {
                spec.log().ifValidationFails();
            } else {
                spec.log().all();
            }
        }

        return spec;
    }

    default RequestSpecification prepareRequestSpec(Object body) {
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

}
