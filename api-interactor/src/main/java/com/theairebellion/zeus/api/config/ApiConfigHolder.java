package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigCache;

public class ApiConfigHolder {

    private static ApiConfig config;


    public static ApiConfig getApiConfig() {
        if (config == null) {
            config = ConfigCache.getOrCreate(ApiConfig.class);
        }
        return config;
    }

}
