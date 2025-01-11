package com.theairebellion.zeus.api.client;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public interface RestClient {

    Response execute(RequestSpecification spec, Method method);

}
