package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class FrameworkConfigHolderTest {

    @BeforeEach
    void clearConfigBeforeTests() throws Exception {
        var field = FrameworkConfigHolder.class.getDeclaredField("config");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void testGetFrameworkConfig() {
        try (MockedStatic<ConfigCache> mocked = mockStatic(ConfigCache.class)) {
            var mockConfig = mock(FrameworkConfig.class);
            when(ConfigCache.getOrCreate(FrameworkConfig.class)).thenReturn(mockConfig);
            var config1 = FrameworkConfigHolder.getFrameworkConfig();
            var config2 = FrameworkConfigHolder.getFrameworkConfig();
            assertNotNull(config1);
            assertNotNull(config2);
            mocked.verify(() -> ConfigCache.getOrCreate(FrameworkConfig.class), times(1));
            verifyNoMoreInteractions(mockConfig);
        }
    }
}
