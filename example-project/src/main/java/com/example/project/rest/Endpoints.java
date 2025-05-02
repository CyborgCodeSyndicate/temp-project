package com.example.project.rest;

import com.theairebellion.zeus.api.core.Endpoint;
import io.restassured.http.Method;
import java.util.List;
import java.util.Map;

public enum Endpoints implements Endpoint<Endpoints> {
   ENDPOINT_EXAMPLE(Method.POST, "/create/{campaignId}/get"),
   CREATE_PET(Method.POST, "/pet");

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
   public Endpoints enumImpl() {
      return this;
   }


   @Override
   public Map<String, List<String>> headers() {
      return Map.of(
            "Content-Type", List.of("application/json"),
            "Accept", List.of("application/json")
      );
   }


}
