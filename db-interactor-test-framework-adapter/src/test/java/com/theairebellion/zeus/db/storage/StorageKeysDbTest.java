package com.theairebellion.zeus.db.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageKeysDbTest {

    @Test
    void shouldReturnCorrectEnumName() {
        assertEquals("DB", StorageKeysDb.DB.name());
    }
}