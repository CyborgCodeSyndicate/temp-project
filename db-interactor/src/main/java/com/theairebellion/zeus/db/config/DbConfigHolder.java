package com.theairebellion.zeus.db.config;

import org.aeonbits.owner.ConfigCache;

public class DbConfigHolder {

    private static DbConfig config;


    public static DbConfig getDbConfig() {
        if (config == null) {
            config = ConfigCache.getOrCreate(DbConfig.class);
        }
        return config;
    }

}
