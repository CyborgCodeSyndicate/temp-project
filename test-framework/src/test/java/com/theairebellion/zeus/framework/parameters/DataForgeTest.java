package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.parameters.mock.MockDataForge;
import com.theairebellion.zeus.framework.parameters.mock.MockEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataForgeTest {

    @Test
    void testDataCreator() {
        String expectedData = "testData";
        DataForge dataForge = new MockDataForge(expectedData, MockEnum.INSTANCE);
        Late<Object> late = dataForge.dataCreator();
        assertNotNull(late);
        assertEquals(expectedData, late.join());
    }

    @Test
    void testEnumImpl() {
        DataForge dataForge = new MockDataForge("data", MockEnum.INSTANCE);
        Enum<?> enumValue = dataForge.enumImpl();
        assertNotNull(enumValue);
        assertEquals(MockEnum.INSTANCE, enumValue);
    }
}
