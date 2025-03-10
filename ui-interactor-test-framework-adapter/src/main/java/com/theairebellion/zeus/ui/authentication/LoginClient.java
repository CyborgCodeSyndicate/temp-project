package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;

/**
 * Represents a UI-based authentication client.
 * <p>
 * Implementing classes are responsible for handling user login processes within the UI.
 * This interface defines a method for performing login operations, which may include
 * caching session details for reuse.
 * </p>
 *
 * This interface is commonly implemented by {@link BaseLoginClient}, which provides
 * session management capabilities, including local storage and cookie persistence.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface LoginClient {

    /**
     * Logs in a user through a UI interaction.
     *
     * @param uiService The UI service used to interact with the login page.
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param cache     Whether to cache session data for reuse.
     */
    void login(SuperUIServiceFluent<?> uiService, String username, String password, boolean cache);

}
