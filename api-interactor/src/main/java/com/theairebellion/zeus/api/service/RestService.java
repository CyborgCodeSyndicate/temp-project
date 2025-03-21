package com.theairebellion.zeus.api.service;

import com.theairebellion.zeus.api.authentication.AuthenticationKey;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.exceptions.RestServiceException;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * Handles API request execution and response validation.
 * <p>
 * This service provides methods to execute API requests, validate responses, and manage authentication.
 * It integrates with {@link RestClient} for request execution and {@link RestResponseValidator} for validation.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Service
@Scope("prototype")
public class RestService {

    private final RestClient restClient;
    private final RestResponseValidator restResponseValidator;
    private BaseAuthenticationClient baseAuthenticationClient;

    private AuthenticationKey authenticationKey;
    @Setter
    private boolean cacheAuthentication;

    /**
     * Constructs a new {@code RestService} instance.
     *
     * @param restClient            The client responsible for executing API requests.
     * @param restResponseValidator The validator for API responses.
     */
    @Autowired
    public RestService(RestClient restClient, final RestResponseValidator restResponseValidator) {
        this.restClient = restClient;
        this.restResponseValidator = restResponseValidator;
    }

    /**
     * Constructs a new {@code RestService} instance with caching configuration.
     *
     * @param restClient            The client responsible for executing API requests.
     * @param restResponseValidator The validator for API responses.
     * @param cacheAuthentication   Whether authentication should be cached.
     */
    public RestService(RestClient restClient, final RestResponseValidator restResponseValidator, boolean cacheAuthentication) {
        this.restClient = restClient;
        this.restResponseValidator = restResponseValidator;
        this.cacheAuthentication = cacheAuthentication;
    }

    /**
     * Executes a request without a body.
     *
     * @param endpoint The API endpoint to call.
     * @return The response from the API.
     */
    public Response request(Endpoint endpoint) {
        return executeRequest(endpoint, null);
    }

    /**
     * Executes a request with a specified body.
     *
     * @param endpoint The API endpoint to call.
     * @param body     The request body.
     * @return The response from the API.
     */
    public Response request(Endpoint endpoint, Object body) {
        return executeRequest(endpoint, body);
    }

    /**
     * Validates a response against the provided assertions.
     *
     * @param response   The response to validate.
     * @param assertions The assertions to apply.
     * @param <T>        The type of the assertion results.
     * @return A list of assertion results.
     * @throws IllegalArgumentException if the response or assertions are null.
     */
    public <T> List<AssertionResult<T>> validate(Response response, Assertion<?>... assertions) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null for validation.");
        }
        if (assertions == null || assertions.length == 0) {
            throw new IllegalArgumentException("At least one assertion must be provided.");
        }
        return restResponseValidator.validateResponse(response, assertions);
    }

    /**
     * Executes a request and validates the response.
     *
     * @param endpoint   The API endpoint to call.
     * @param assertions The assertions to apply.
     * @param <T>        The type of the assertion results.
     * @return A list of assertion results.
     */
    public <T> List<AssertionResult<T>> requestAndValidate(Endpoint endpoint, Assertion<?>... assertions) {
        return requestAndValidate(endpoint, null, assertions);
    }

    /**
     * Executes a request with a body and validates the response.
     *
     * @param endpoint   The API endpoint to call.
     * @param body       The request body.
     * @param assertions The assertions to apply.
     * @param <T>        The type of the assertion results.
     * @return A list of assertion results.
     */
    public <T> List<AssertionResult<T>> requestAndValidate(Endpoint endpoint, Object body, Assertion<?>... assertions) {
        Response response = request(endpoint, body);
        return validate(response, assertions);
    }

    /**
     * Authenticates a user using a specified authentication client.
     *
     * @param username                  The username for authentication.
     * @param password                  The password for authentication.
     * @param authenticationClientClass The authentication client class to use.
     * @throws RestServiceException if authentication fails.
     */
    public void authenticate(String username, String password,
                             Class<? extends BaseAuthenticationClient> authenticationClientClass) {
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(password, "Password must not be null");
        Objects.requireNonNull(authenticationClientClass, "Authentication client class must not be null");
        try {
            baseAuthenticationClient = authenticationClientClass.getDeclaredConstructor().newInstance();
            authenticationKey = baseAuthenticationClient.authenticate(this, username, password, cacheAuthentication);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RestServiceException("Error instantiating or authenticating with BaseAuthenticationClient.", e);
        }
    }

    /**
     * Executes a request to the specified endpoint.
     *
     * @param endpoint The API endpoint.
     * @param body     The request body (optional).
     * @return The API response.
     * @throws RestServiceException if an error occurs during request execution.
     */
    private Response executeRequest(Endpoint endpoint, Object body) {
        if (endpoint == null) {
            throw new RestServiceException("Endpoint cannot be null.");
        }
        try {
            RequestSpecification spec = endpoint.prepareRequestSpec(body);
            if (baseAuthenticationClient != null) {
                Header authenticationHeader = baseAuthenticationClient.getAuthentication(authenticationKey);
                if (authenticationHeader != null) {
                    spec.header(authenticationHeader);
                }
            }
            return restClient.execute(spec, endpoint.method());
        } catch (Exception e) {
            throw new RestServiceException("Error executing request for endpoint: " + endpoint.url(), e);
        }
    }
}
