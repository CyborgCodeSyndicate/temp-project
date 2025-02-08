package com.theairebellion.zeus.ui.components.interceptor;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ApiResponse {

    private final String url;
    private final int status;
    @Setter
    private String body;


    public ApiResponse(final String url, final int status) {
        this.url = url;
        this.status = status;
    }

}
