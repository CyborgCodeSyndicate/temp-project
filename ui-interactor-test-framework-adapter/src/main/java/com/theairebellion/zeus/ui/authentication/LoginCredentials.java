package com.theairebellion.zeus.ui.authentication;

/**
 * Represents user login credentials.
 *
 * <p>This interface defines the structure for storing and retrieving authentication
 * credentials used in UI-based login processes. Implementations of this interface
 * should provide user-specific credentials such as username and password.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface LoginCredentials {

   /**
    * Retrieves the username associated with these credentials.
    *
    * @return The username.
    */
   String username();

   /**
    * Retrieves the password associated with these credentials.
    *
    * @return The password.
    */
   String password();

}
