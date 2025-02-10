package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.ConfigCache;

public class UiFrameworkConfigHolder {

    private static UIFrameworkConfig config;


    public static UIFrameworkConfig getUiFrameworkConfig() {
        if (config == null) {
            config = ConfigCache.getOrCreate(UIFrameworkConfig.class);
        }
        return config;
    }

}