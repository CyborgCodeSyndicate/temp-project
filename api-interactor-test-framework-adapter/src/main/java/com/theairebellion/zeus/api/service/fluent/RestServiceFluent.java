package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.base.ClassLevelHook;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import static com.theairebellion.zeus.api.storage.StorageKeysApi.API;

@WorldName("API")
@Service
@Scope("prototype")
@Lazy
public class RestServiceFluent extends FluentService implements ClassLevelHook {

    private final RestService restService;


    @Autowired
    public RestServiceFluent(final RestService restService) {
        this.restService = restService;
    }


    public RestServiceFluent request(final Endpoint endpoint) {
        final Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }


    public RestServiceFluent request(final Endpoint endpoint, final Object body) {
        final Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }


    public RestServiceFluent validateResponse(final Response response, final Assertion<?>... assertions) {
        final List<AssertionResult<Object>> assertionResults = restService.validate(response, assertions);
        validation(assertionResults); // Provided by FluentService
        return this;
    }


    public RestServiceFluent requestAndValidate(final Endpoint endpoint, final Assertion<?>... assertions) {
        final Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }


    @Step("Request and validations for endpoint: {endpoint}")
    public RestServiceFluent requestAndValidate(final Endpoint endpoint,
                                                final Object body,
                                                final Assertion<?>... assertions) {
        final Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }


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


    private RestService getRestService() {
        return restService;
    }


    public <T> RestServiceFluent retryUntil(final RetryCondition<T> retryCondition, final Duration maxWait,
                                            final Duration retryInterval) {
        return (RestServiceFluent) super.retryUntil(retryCondition, maxWait, retryInterval, restService);
    }

}
