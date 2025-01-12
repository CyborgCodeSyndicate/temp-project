package com.theairebellion.zeus.api.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationKey {

    private String username;
    private String password;
    private Class<? extends BaseAuthenticationClient> type;

}
