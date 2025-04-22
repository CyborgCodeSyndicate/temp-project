package com.theairebellion.zeus.api.service.fluent;

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
    public RestServiceFluent request(final Endpoint<?> endpoint) {
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
    public RestServiceFluent request(final Endpoint<?> endpoint, final Object body) {
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
    public RestServiceFluent validateResponse(final Response response, final Assertion... assertions) {
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
    public RestServiceFluent requestAndValidate(final Endpoint<?> endpoint, final Assertion... assertions) {
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
    public RestServiceFluent requestAndValidate(final Endpoint<?> endpoint,
                                                final Object body,
                                                final Assertion... assertions) {
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
    public RestServiceFluent authenticate(final String username,
                                          final String password,
                                          final Class<? extends BaseAuthenticationClient> authenticationClient) {
        restService.authenticate(username, password, authenticationClient);
        return this;
    }

    /**
     * Performs validation using a provided assertion.
     *
     * @param assertion The assertion to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @Override
    public RestServiceFluent validate(final Runnable assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }

    /**
     * Performs validation using a provided assertion with {@link SoftAssertions}.
     *
     * @param assertion The assertion to validate.
     * @return The current {@code RestServiceFluent} instance for method chaining.
     */
    @Override
    public RestServiceFluent validate(final Consumer<SoftAssertions> assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }

    /**
     * Retrieves the underlying {@link RestService} instance.
     *
     * @return The {@code RestService} instance.
     */
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
    public <T> RestServiceFluent retryUntil(final RetryCondition<T> retryCondition, final Duration maxWait,
                                            final Duration retryInterval) {
        return (RestServiceFluent) super.retryUntil(retryCondition, maxWait, retryInterval, restService);
    }

}
