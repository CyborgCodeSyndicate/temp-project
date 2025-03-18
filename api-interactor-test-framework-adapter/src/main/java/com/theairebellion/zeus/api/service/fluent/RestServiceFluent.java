package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.ai.metadata.model.Level;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.annotations.InfoAI;
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

@TestService("API")
@InfoAIClass(level = Level.LAST,
    description = "RestServiceFluent can be called from Quest object and provides methods for Rest actions and validations")
public class RestServiceFluent extends FluentService implements ClassLevelHook {

    protected final RestService restService;


    @Autowired
    public RestServiceFluent(final RestService restService) {
        this.restService = restService;
    }


    @InfoAI(description = "Method for sending requests to endpoints that add the response in storage")
    public RestServiceFluent request(
        final Endpoint endpoint) {
        final Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }


    @InfoAI(
        description = "Method for sending requests to endpoints with body object as payload that add the response in storage")
    public RestServiceFluent request(final Endpoint endpoint, final @InfoAI(description = "The body object that will be sent as payload") Object body) {
        final Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }


    @InfoAI(
        description = "Method for validating the response by the assertions sent as arguments")
    public RestServiceFluent validateResponse(final Response response, final Assertion<?>... assertions) {
        final List<AssertionResult<Object>> assertionResults = restService.validate(response, assertions);
        validation(assertionResults); // Provided by FluentService
        return this;
    }


    @InfoAI(
        description = "Method for sending requests to endpoint and validating the received response by the assertions sent as arguments")
    public RestServiceFluent requestAndValidate(final Endpoint endpoint, final Assertion<?>... assertions) {
        final Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }


    @Step("Request and validations for endpoint: {endpoint}")
    @InfoAI(
        description = "Method for sending requests to endpoint with body as payload and validating the received response by the assertions sent as arguments")
    public RestServiceFluent requestAndValidate(final Endpoint endpoint,
                                                final Object body,
                                                final Assertion<?>... assertions) {
        final Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }


    @InfoAI(
        description = "Method for authenticating by sending username and password and type of authentication. All next request will have the authentication")
    public RestServiceFluent authenticate(final String username,
                                          final String password,
                                          final Class<? extends BaseAuthenticationClient> authenticationClient) {
        restService.authenticate(username, password, authenticationClient);
        return this;
    }


    @Override
    public RestServiceFluent validate(final Runnable assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }


    @Override
    public RestServiceFluent validate(final Consumer<SoftAssertions> assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }


    protected RestService getRestService() {
        return restService;
    }


    public <T> RestServiceFluent retryUntil(final RetryCondition<T> retryCondition, final Duration maxWait,
                                            final Duration retryInterval) {
        return (RestServiceFluent) super.retryUntil(retryCondition, maxWait, retryInterval, restService);
    }

}
