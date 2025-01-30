package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.ConfigCache;

public class FrameworkConfigHolder {

    private static FrameworkConfig config;


    public static FrameworkConfig getFrameworkConfig() {
        if (config == null) {
            config = ConfigCache.getOrCreate(FrameworkConfig.class);
        }
        return config;
    }

}
