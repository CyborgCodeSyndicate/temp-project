package com.theairebellion.zeus.db.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Holds the database configuration instance.
 * <p>
 * This class provides singleton-like access to the {@link DbConfig} instance,
 * ensuring that the configuration is only initialized once using the OWNER library.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class DbConfigHolder {

    private static DbConfig config;

    /**
     * Retrieves the database configuration.
     * <p>
     * If the configuration has not been initialized, it is created using the OWNER
     * {@link ConfigCache}.
     * </p>
     *
     * @return The {@link DbConfig} instance.
     */
    public static DbConfig getDbConfig() {
        if (config == null) {
            config = ConfigCache.getOrCreate(DbConfig.class);
        }
        return config;
    }

}
