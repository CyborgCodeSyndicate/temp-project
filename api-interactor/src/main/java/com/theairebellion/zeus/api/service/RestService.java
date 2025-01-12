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

@Service
@Scope("prototype")
public class RestService {

    private final RestClient restClient;
    private final RestResponseValidator restResponseValidator;
    @Setter
    private BaseAuthenticationClient baseAuthenticationClient;

    private AuthenticationKey authenticationKey;


    @Autowired
    public RestService(RestClient restClient, final RestResponseValidator restResponseValidator) {
        this.restClient = restClient;
        this.restResponseValidator = restResponseValidator;
    }


    public Response request(Endpoint endpoint) {
        return execute(endpoint, null);
    }


    public Response request(Endpoint endpoint, Object body) {
        return execute(endpoint, body);
    }


    public <T> List<AssertionResult<T>> validate(Response response, Assertion<?>... assertions) {
        return restResponseValidator.validateResponse(response, assertions);
    }


    public <T> List<AssertionResult<T>> requestAndValidate(Endpoint endpoint, Object body, Assertion<?>... assertions) {
        Response response = execute(endpoint, body);
        return validate(response, assertions);
    }


    public <T> List<AssertionResult<T>> requestAndValidate(Endpoint endpoint, Assertion<?>... assertions) {
        Response response = execute(endpoint, null);
        return validate(response, assertions);
    }


    public void authenticate(String username, String password,
                             Class<? extends BaseAuthenticationClient> authenticationClient) {
        try {
            setBaseAuthenticationClient(authenticationClient.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        authenticationKey = baseAuthenticationClient.authenticate(this, username, password);
    }


    private Response execute(Endpoint endpoint, Object body) {
        try {
            RequestSpecification spec = endpoint.prepareRequestSpec(body);
            if (Objects.nonNull(baseAuthenticationClient)) {
                Header authentication = baseAuthenticationClient.getAuthentication(authenticationKey);
                if (Objects.nonNull(authentication)) {
                    spec.header(authentication);
                }
            }
            return restClient.execute(spec, endpoint.method());
        } catch (Exception e) {
            throw new RestServiceException("Error executing request: " + e.getMessage(), e);
        }
    }

}
