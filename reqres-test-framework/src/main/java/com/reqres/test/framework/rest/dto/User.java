package com.reqres.test.framework.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String name;
    private String job;

}
