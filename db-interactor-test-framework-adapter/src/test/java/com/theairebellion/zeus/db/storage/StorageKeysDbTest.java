package com.theairebellion.zeus.db.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageKeysDbTest {

    @Test
    void testEnum() {
        assertEquals("DB", StorageKeysDb.DB.name());
    }
}