package com.reqres.test.framework.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUser {

    private String email;
    private String password;

}
