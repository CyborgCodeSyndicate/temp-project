package com.theairebellion.zeus.ui.components.interceptor;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ApiResponse {

    private String url;
    private int status;
    @Setter
    private String body;


    public ApiResponse(final String url, final int status) {
        this.url = url;
        this.status = status;
    }

}
