package com.theairebellion.zeus.framework.data;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DataProviderTest {

    @Test
    void testTestStaticData() {
        DataProvider dataProvider = mock(DataProvider.class);
        when(dataProvider.testStaticData()).thenReturn(Map.of("key", "value"));
        Map<String, Object> result = dataProvider.testStaticData();
        assertEquals(Map.of("key", "value"), result);
        verify(dataProvider).testStaticData();
        verifyNoMoreInteractions(dataProvider);
    }
}