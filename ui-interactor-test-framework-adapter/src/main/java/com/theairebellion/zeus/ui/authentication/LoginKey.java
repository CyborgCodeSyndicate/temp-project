package com.theairebellion.zeus.ui.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginKey {

    private String username;
    private String password;
    private Class<? extends BaseLoginClient> type;

}
