package com.bakery.project.rest.authentication;

import com.theairebellion.zeus.api.authentication.Credentials;

public class AdminAuth implements Credentials {

   @Override
   public String username() {
      return "portal";
   }


   @Override
   public String password() {
      return "";
   }

}
