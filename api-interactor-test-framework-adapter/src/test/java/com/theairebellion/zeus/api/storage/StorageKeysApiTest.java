package com.theairebellion.zeus.api.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageKeysApiTest {

    @Test
    void testEnumValues() {
        StorageKeysApi[] values = StorageKeysApi.values();
        assertEquals(3, values.length);
        assertNotNull(StorageKeysApi.valueOf("API"));
        assertNotNull(StorageKeysApi.valueOf("USERNAME"));
        assertNotNull(StorageKeysApi.valueOf("PASSWORD"));
    }
}