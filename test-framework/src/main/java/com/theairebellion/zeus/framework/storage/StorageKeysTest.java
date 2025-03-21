package com.theairebellion.zeus.framework.storage;

/**
 * Defines keys used for organizing test data within the storage system.
 * <p>
 * This enum provides logical identifiers for various data groups used during a test execution.
 * These keys help segregate and retrieve test data accurately within the storage context.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public enum StorageKeysTest {

    /**
     * Key for storing argument-related test data.
     */
    ARGUMENTS,

    /**
     * Key for storing pre-execution argument data.
     */
    PRE_ARGUMENTS,

    /**
     * Key for storing static test data.
     */
    STATIC_DATA,
    HOOKS,

}
