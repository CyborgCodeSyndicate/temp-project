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
    private BaseAuthenticationClient baseAuthenticationClient;

    private AuthenticationKey authenticationKey;
    @Setter
    private boolean cacheAuthentication;


    @Autowired
    public RestService(RestClient restClient, final RestResponseValidator restResponseValidator) {
        this.restClient = restClient;
        this.restResponseValidator = restResponseValidator;
    }

    public RestService(RestClient restClient, final RestResponseValidator restResponseValidator, boolean cacheAuthentication) {
        this.restClient = restClient;
        this.restResponseValidator = restResponseValidator;
        this.cacheAuthentication = cacheAuthentication;

    }



    public Response request(Endpoint endpoint) {
        return executeRequest(endpoint, null);
    }


    public Response request(Endpoint endpoint, Object body) {
        return executeRequest(endpoint, body);
    }


    public <T> List<AssertionResult<T>> validate(Response response, Assertion<?>... assertions) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null for validation.");
        }
        if (assertions == null || assertions.length == 0) {
            throw new IllegalArgumentException("At least one assertion must be provided.");
        }
        return restResponseValidator.validateResponse(response, assertions);
    }


    public <T> List<AssertionResult<T>> requestAndValidate(Endpoint endpoint, Assertion<?>... assertions) {
        return requestAndValidate(endpoint, null, assertions);
    }


    public <T> List<AssertionResult<T>> requestAndValidate(Endpoint endpoint, Object body, Assertion<?>... assertions) {
        Response response = request(endpoint, body);
        return validate(response, assertions);
    }


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


    private Response executeRequest(Endpoint endpoint, Object body) {
        try {
            Objects.requireNonNull(endpoint, "Endpoint cannot be null.");

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
