package com.theairebellion.zeus.ui.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a unique key for identifying user login sessions.
 *
 * <p>This class is used to store authentication details, including the username, password,
 * and the specific {@link BaseLoginClient} type associated with a login session.
 * It serves as a key for caching user sessions in {@link BaseLoginClient} to
 * facilitate session reuse and prevent redundant logins.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@AllArgsConstructor
@Data
public class LoginKey {

   /**
    * The username associated with the login session.
    */
   private String username;

   /**
    * The password associated with the login session.
    */
   private String password;

   /**
    * The type of {@link BaseLoginClient} used for this login session.
    */
   private Class<? extends BaseLoginClient> type;

}
