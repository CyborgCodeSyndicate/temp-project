package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.restassured.response.Response;

import java.util.List;

public interface RestResponseValidator {

    <T> List<AssertionResult<T>> validateResponse(Response response, Assertion<?>... assertions);


}
