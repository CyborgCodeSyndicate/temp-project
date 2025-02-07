package com.theairebellion.zeus.api.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageKeysApiTest {

    @Test
    void testEnumValues() {
        var values = StorageKeysApi.values();
        assertAll(
                () -> assertEquals(3, values.length),
                () -> assertNotNull(StorageKeysApi.valueOf("API")),
                () -> assertNotNull(StorageKeysApi.valueOf("USERNAME")),
                () -> assertNotNull(StorageKeysApi.valueOf("PASSWORD"))
        );
    }
}