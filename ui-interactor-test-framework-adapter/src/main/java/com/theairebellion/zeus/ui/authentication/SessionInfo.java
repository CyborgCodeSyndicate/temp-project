package com.theairebellion.zeus.ui.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.Cookie;

import java.util.Set;

/**
 * Represents session information for a logged-in user.
 * <p>
 * This class stores authentication-related data, including browser cookies and
 * local storage content, to enable session restoration without requiring re-login.
 * It is primarily used in {@link BaseLoginClient} for caching and restoring user sessions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@AllArgsConstructor
@Getter
public class SessionInfo {

    /**
     * The set of cookies associated with the authenticated session.
     */
    private Set<Cookie> cookies;

    /**
     * The JSON representation of local storage data associated with the session.
     */
    private String localStorage;

}
