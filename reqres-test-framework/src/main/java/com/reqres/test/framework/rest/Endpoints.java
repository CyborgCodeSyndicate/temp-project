package com.reqres.test.framework.rest;

import com.theairebellion.zeus.api.core.Endpoint;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import static com.reqres.test.framework.utils.Headers.API_KEY_HEADER;
import static com.reqres.test.framework.utils.Headers.API_KEY_VALUE;

public enum Endpoints implements Endpoint {

   GET_ALL_USERS(Method.GET, "/users?{page}"),
   GET_USER(Method.GET, "/users/{id}"),
   POST_CREATE_USER(Method.POST, "/users"),
   POST_LOGIN_USER(Method.POST, "/login"),
   DELETE_USER(Method.DELETE, "/users/{id}"),
   GET_ALL_RESOURCES(Method.GET, "/unknown?{page}"),
   GET_RESOURCE(Method.GET, "/unknown/{id}");

   private final Method method;
   private final String url;

   Endpoints(final Method method, final String url) {
      this.method = method;
      this.url = url;
   }

   @Override
   public Method method() {
      return method;
   }

   @Override
   public String url() {
      return url;
   }

   @Override
   public Enum<?> enumImpl() {
      return this;
   }

   @Override
   public RequestSpecification defaultConfiguration() {
      RequestSpecification spec = Endpoint.super.defaultConfiguration();
      spec.contentType(ContentType.JSON);
      spec.header(API_KEY_HEADER, API_KEY_VALUE);
      return spec;
   }

}
