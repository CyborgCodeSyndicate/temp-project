package com.theairebellion.zeus.ui.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.Cookie;

import java.util.Set;

@AllArgsConstructor
@Getter
public class SessionInfo {

    private Set<Cookie> cookies;
    private String localStorage;

}
