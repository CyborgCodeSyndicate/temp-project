package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.ConfigCache;

public class UiConfigHolder {

    private static UiConfig config;


    public static UiConfig getUiConfig() {
        if (config == null) {
            config = ConfigCache.getOrCreate(UiConfig.class);
        }
        return config;
    }

}
