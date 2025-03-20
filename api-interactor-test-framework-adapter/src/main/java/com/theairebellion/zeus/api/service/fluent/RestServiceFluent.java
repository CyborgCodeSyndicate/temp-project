package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.ai.metadata.model.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.base.ClassLevelHook;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.API;

/**
 * Provides fluent interactions for API requests and validations.
 * <p>
 * This class extends {@link FluentService} and enables streamlined API calls,
 * authentication, validations, and retry mechanisms.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@TestService("API")
@InfoAIClass(
        level = Level.LAST,
        description = "Provides fluent API interactions for sending requests, handling authentication, and validating responses. " +
                "Methods allow executing requests to defined endpoints, adding payloads, applying assertions, and managing retries.")
public class RestServiceFluent extends FluentService implements ClassLevelHook {

    private final RestService restService;

    /**
     * Constructs a fluent API service instance.
     *
     * @param restService The underlying {@link RestService} instance.
     */
    @Autowired
    public RestServiceFluent(final RestService restService) {
        this.restService = restService;
    }

    /**
     * Executes a request to the specified endpoint.
     *
     * @param endpoint The API endpoint.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Sends a request to a specified API endpoint and stores the response. " +
            "The endpoint must be defined in an enum implementing the Endpoint interface.")
    public RestServiceFluent request(
            final Endpoint endpoint) {
        final Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }

    /**
     * Executes a request with a request body.
     *
     * @param endpoint The API endpoint.
     * @param body     The request body.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Sends a request with a payload to a specified API endpoint and stores the response. " +
            "The endpoint must be an enum implementing the Endpoint interface, and the body represents the request payload.")
    public RestServiceFluent request(final Endpoint endpoint, @InfoAI(description = "The payload object to be sent in the request body.") final Object body) {
        final Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }

    /**
     * Validates an API response against provided assertions.
     *
     * @param response   The API response.
     * @param assertions The assertions to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Validates an API response using the provided assertions. " +
            "The response is checked against multiple conditions, ensuring expected API behavior.")
    public RestServiceFluent validateResponse(@InfoAI(description = "The API response object to be validated.") final Response response, final Assertion<?>... assertions) {
        final List<AssertionResult<Object>> assertionResults = restService.validate(response, assertions);
        validation(assertionResults);
        return this;
    }

    /**
     * Executes a request and validates the response.
     *
     * @param endpoint   The API endpoint.
     * @param assertions The assertions to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Sends a request to a specified API endpoint, stores the response, and validates it using provided assertions. " +
            "The endpoint must be an enum implementing the Endpoint interface.")
    public RestServiceFluent requestAndValidate(final Endpoint endpoint, final Assertion<?>... assertions) {
        final Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }

    /**
     * Executes a request with a request body and validates the response.
     *
     * @param endpoint   The API endpoint.
     * @param body       The request body.
     * @param assertions The assertions to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @Step("Request and validations for endpoint: {endpoint}")
    @InfoAI(description = "Sends a request with a payload to a specified API endpoint, stores the response, and validates it using provided assertions. " +
            "The endpoint must be an enum implementing the Endpoint interface, and the body represents the request payload.")
    public RestServiceFluent requestAndValidate(final Endpoint endpoint,
                                                @InfoAI(description = "The payload object to be sent in the request body.") final Object body,
                                                final Assertion<?>... assertions) {
        final Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }

    /**
     * Performs authentication using the specified credentials and authentication client.
     *
     * @param username             The username for authentication.
     * @param password             The password for authentication.
     * @param authenticationClient The authentication client class.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Authenticates the user by sending a username, password, and authentication client type. " +
            "Once authenticated, all subsequent requests will include authentication.")
    public RestServiceFluent authenticate(@InfoAI(description = "The username used for authentication.") final String username,
                                          @InfoAI(description = "The password associated with the username for authentication.") final String password,
                                          @InfoAI(description = "The authentication client class that defines the authentication mechanism.") Class<? extends BaseAuthenticationClient> authenticationClient) {
        restService.authenticate(username, password, authenticationClient);
        return this;
    }

    /**
     * Performs validation using a provided assertion.
     *
     * @param assertion The assertion to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Executes a validation using the provided assertion logic. " +
            "The assertion is executed as a runnable task.")
    @Override
    public RestServiceFluent validate(@InfoAI(description = "A runnable assertion task that executes validation logic.") final Runnable assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }

    /**
     * Performs validation using a provided assertion with {@link SoftAssertions}.
     *
     * @param assertion The assertion to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Executes a validation using the provided assertion logic with SoftAssertions. " +
            "This allows multiple assertions to be verified within a single validation step.")
    @Override
    public RestServiceFluent validate(@InfoAI(description = "A consumer function that applies multiple assertions using SoftAssertions.") final Consumer<SoftAssertions> assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }

    /**
     * Retrieves the underlying {@link RestService} instance.
     *
     * @return The {@code RestService} instance.
     */
    @InfoAI(description = "Retrieves the underlying RestService instance, " +
            "which handles API request execution and validation.")
    protected RestService getRestService() {
        return restService;
    }

    /**
     * Executes a retry mechanism until a specified condition is met.
     *
     * @param retryCondition The retry condition to be checked.
     * @param maxWait        The maximum duration to wait before giving up.
     * @param retryInterval  The interval between retries.
     * @param <T>            The type used in the retry condition function.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @InfoAI(description = "Executes a retry mechanism until the specified condition is met. " +
            "Retries execution at a fixed interval and stops when the condition is satisfied or the maximum wait time is reached.")
    public <T> RestServiceFluent retryUntil(@InfoAI(description = "The condition that determines whether the retry should continue or stop.") final RetryCondition<T> retryCondition,
                                            @InfoAI(description = "The maximum duration to wait before stopping retries.") final Duration maxWait,
                                            @InfoAI(description = "The interval between each retry attempt.") final Duration retryInterval) {
        return (RestServiceFluent) super.retryUntil(retryCondition, maxWait, retryInterval, restService);
    }


}
