package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.base.ClassLevelHook;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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


    public RestServiceFluent request(Endpoint endpoint) {
        Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }


    public RestServiceFluent request(Endpoint endpoint, Object body) {
        Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return this;
    }


    public RestServiceFluent validateResponse(Response response, Assertion<?>... assertions) {
        List<AssertionResult<Object>> assertionResults = restService.validate(response, assertions);
        validation(assertionResults);
        return this;
    }


    public RestServiceFluent requestAndValidate(Endpoint endpoint, Assertion<?>... assertions) {
        Response response = restService.request(endpoint);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }


    @Step("Request and validations for endpoint: {endpoint}")
    public RestServiceFluent requestAndValidate(Endpoint endpoint, Object body, Assertion<?>... assertions) {
        Response response = restService.request(endpoint, body);
        quest.getStorage().sub(API).put(endpoint.enumImpl(), response);
        return validateResponse(response, assertions);
    }


    public RestServiceFluent authenticate(String username, String password,
                                          Class<? extends BaseAuthenticationClient> authenticationClient) {
        restService.authenticate(username, password, authenticationClient);
        return this;
    }


    public RestServiceFluent validate(Runnable assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }


    public RestServiceFluent validate(Consumer<SoftAssertions> assertion) {
        return (RestServiceFluent) super.validate(assertion);
    }


    private RestService getRestService() {
        return restService;
    }


}
