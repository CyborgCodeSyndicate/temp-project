package com.theairebellion.zeus.api.client;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Represents a REST client for executing HTTP requests.
 * <p>
 * This interface defines a method to execute API requests using the given
 * {@code RequestSpecification} and HTTP method.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface RestClient {

    /**
     * Executes an API request using the provided request specification and HTTP method.
     *
     * @param spec   The {@code RequestSpecification} containing request details.
     * @param method The HTTP method to be used for the request.
     * @return The {@code Response} received from the server.
     */
    Response execute(RequestSpecification spec, Method method);

}
