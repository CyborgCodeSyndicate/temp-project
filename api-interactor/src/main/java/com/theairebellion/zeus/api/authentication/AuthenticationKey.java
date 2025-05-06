package com.theairebellion.zeus.api.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a unique authentication key for identifying authenticated sessions.
 *
 * <p>This key is used for caching authentication details and retrieving authentication headers
 * in the test automation framework.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@AllArgsConstructor
@Data
public class AuthenticationKey {

   /**
    * The username associated with the authentication session.
    */
   private String username;

   /**
    * The password associated with the authentication session.
    */
   private String password;

   /**
    * The type of authentication client handling this session.
    */
   private Class<? extends BaseAuthenticationClient> type;
}
